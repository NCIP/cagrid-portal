package gov.nih.nci.cagrid.introduce.servicetasks.undeployment;

import gov.nih.nci.cagrid.introduce.servicetasks.beans.deployment.Deployment;
import gov.nih.nci.cagrid.introduce.servicetasks.beans.deployment.Jar;
import gov.nih.nci.cagrid.introduce.servicetasks.deployment.DeploymentFileGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.iterators.ArrayIterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.globus.wsrf.encoding.DeserializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.xml.sax.InputSource;


public class UndeployServiceTool extends Task {

    private String webAppLocation;
    private String webAppDeployLocation;
    private String webAppDeployLibLocation;
    private String webAppDeploySchemaLocation;
    private String serviceDeploymentPath;
    private String serviceDeploymentDirectoryName;
    private String servicePrefix;
    private String serviceName;

    private Deployment undeployService = null;

    private Map<String, Deployment> otherDeployedServices = new HashMap<String, Deployment>();


    public void execute() throws BuildException {
        super.execute();

        Properties properties = new Properties();
        properties.putAll(this.getProject().getProperties());

        // 1.get some basic properties about the deployment
        webAppLocation = properties.getProperty("webapp.dir");
        webAppDeployLocation = properties.getProperty("webapp.deploy.dir");
        webAppDeployLibLocation = properties.getProperty("webapp.deploy.lib.dir");
        webAppDeploySchemaLocation = properties.getProperty("webapp.deploy.schema.dir");
        serviceDeploymentPath = properties.getProperty("service.deployment.dir");
        serviceDeploymentDirectoryName = properties.getProperty("service.deployment.dir.name");
        servicePrefix = properties.getProperty("service.deployment.prefix");
        serviceName = properties.getProperty("service.name");

        this.log("webapp.dir" + webAppLocation);
        this.log("webapp.deploy.dir" + webAppDeployLocation);
        this.log("webapp.deploy.lib.dir" + webAppDeployLibLocation);
        this.log("webapp.deploy.schema.dir" + webAppDeploySchemaLocation);
        this.log("service.deployment.dir" + serviceDeploymentPath);
        this.log("service.deployment.dir.name" + serviceDeploymentDirectoryName);
        this.log("service.deployment.prefix" + servicePrefix);
        this.log("service.name" + serviceName);

        if (webAppLocation == null || webAppDeployLocation == null || webAppDeployLibLocation == null
            || webAppDeploySchemaLocation == null || serviceName == null || servicePrefix == null
            || serviceDeploymentPath == null || serviceDeploymentDirectoryName == null) {
            throw new BuildException(
                "The properties: webapp.location, webapp.deploy.dir, webapp.deploy.lib.dir, webapp.deploy.schema.dir, service.deployment.prefix, service.deployment.dir.name, service.deployment.dir and service.name must be set");
        }

        // 2. look for the service that is going to be removed
        File deploymentDescriptor = new File(serviceDeploymentPath + File.separator
            + DeploymentFileGenerator.DEPLOYMENT_PERSISTENCE_FILE);
        if (!deploymentDescriptor.exists() || !deploymentDescriptor.canRead()) {
            throw new BuildException(
                "Service does not seem to be an Introduce generated service or file system permissions are preventing access.");
        }

        InputSource is = null;
        try {
            is = new InputSource(new FileInputStream(deploymentDescriptor));
        } catch (FileNotFoundException e) {
            throw new BuildException("Connot find deployment descriptor: " + deploymentDescriptor, e);
        }

        try {
            undeployService = (Deployment) ObjectDeserializer.deserialize(is, Deployment.class);
        } catch (DeserializationException e) {
            throw new BuildException("Cannot deserialize deployment descriptor: " + deploymentDescriptor, e);
        }

        // 3. build up map of other services dependencies
        loadOtherIntroduceServices();

        // 4. process the data for removal
        processForRemoval();

    }


    private void processForRemoval() throws BuildException {

        // process the removal of jars
        if (this.undeployService.getJars() != null && this.undeployService.getJars().getJar() != null) {
            for (int jarI = 0; jarI < this.undeployService.getJars().getJar().length; jarI++) {
                Jar currentJar = this.undeployService.getJars().getJar()[jarI];
                if (canRemoveJar(currentJar)) {
                    File jarFile = new File(webAppDeployLibLocation + File.separator + currentJar.getLocation()
                        + File.separator + currentJar.getName());
                    if (jarFile != null && jarFile.exists()) {
                        System.out.println("Removing jar file: " + jarFile.getAbsolutePath());
                        boolean deleted = jarFile.delete();
                        if (!deleted) {
                            System.out.println("ERROR: unable to delete jar on undeploy: " + jarFile.getAbsolutePath());
                            throw new BuildException("ERROR: unable to delete jar on undeploy: "
                                + jarFile.getAbsolutePath());
                        }
                    }
                }
            }
        }

        // process the removal of endorsed jars
        // TODO: support me

        // process the removal of schema
        if (canRemoveSchemaDir()) {
            File schemaLocation = new File(webAppDeploySchemaLocation + File.separator
                + undeployService.getServiceName());
            if (schemaLocation.exists() && schemaLocation.canRead()) {
                System.out.println("Removing schema location: " + schemaLocation.getAbsolutePath());
                boolean deleted = deleteDir(schemaLocation);
                if (!deleted) {
                    System.out.println("ERROR: unable to completely remove schema location: "
                        + schemaLocation.getAbsolutePath());
                    throw new BuildException("ERROR: unable to completely remove schema location: "
                        + schemaLocation.getAbsolutePath());
                }
            }
        }

        // process the removal of the etc dir
        File etcLocation = new File(serviceDeploymentPath);
        if (etcLocation.exists() && etcLocation.canRead()) {
            System.out.println("Removing etc location: " + etcLocation.getAbsolutePath());
            boolean deleted = deleteDir(etcLocation);
            if (!deleted) {
                System.out.println("WARNING: unable to completely remove etc location: "
                    + etcLocation.getAbsolutePath());
            }
        }

    }


    private boolean canRemoveSchemaDir() {
        Collection<Deployment> col = otherDeployedServices.values();
        boolean found = false;
        Iterator<Deployment> it = col.iterator();
        while (it.hasNext() && !found) {
            Deployment serviceD = it.next();
            if (serviceD.getServiceName().equals(undeployService.getServiceName())) {
                found = true;
                break;
            }
        }

        return !found;
    }


    private boolean canRemoveJar(Jar currentJar) {
        Collection<Deployment> col = otherDeployedServices.values();
        boolean found = false;
        Iterator<Deployment> it = col.iterator();
        while (it.hasNext() && !found) {
            Deployment serviceD = it.next();
            if (serviceD.getJars() != null && serviceD.getJars().getJar() != null) {
                Iterator jarIt = new ArrayIterator(serviceD.getJars().getJar());
                while (jarIt.hasNext() && !found) {
                    Jar thisJar = (Jar) jarIt.next();
                    if (thisJar.getLocation().equals(currentJar.getLocation())
                        && thisJar.getName().equals(currentJar.getName())) {
                        found = true;
                        break;
                    }
                }
            }
        }
        return !found;
    }


    private void loadOtherIntroduceServices() throws BuildException {
        File etcDir = new File(webAppDeployLocation + File.separator + "etc");
        if (!etcDir.exists() || !etcDir.canRead()) {
            throw new BuildException("Cannot read the web app etc dir: " + etcDir.getAbsolutePath());
        }

        File[] etcFiles = etcDir.listFiles();
        for (int i = 0; i < etcFiles.length; i++) {
            if (etcFiles[i].exists() && etcFiles[i].canRead() && etcFiles[i].isDirectory()) {
                File deploymentDescriptorFile = new File(etcFiles[i].getAbsolutePath() + File.separator
                    + DeploymentFileGenerator.DEPLOYMENT_PERSISTENCE_FILE);
                if (deploymentDescriptorFile.exists() && deploymentDescriptorFile.canRead()) {
                    InputSource is = null;
                    try {
                        is = new InputSource(new FileInputStream(deploymentDescriptorFile));
                    } catch (FileNotFoundException e) {
                        throw new BuildException("Connot find deployment descriptor: " + deploymentDescriptorFile, e);
                    }
                    Deployment introduceServiceDeployment = null;
                    try {
                        introduceServiceDeployment = (Deployment) ObjectDeserializer.deserialize(is, Deployment.class);
                    } catch (DeserializationException e) {
                        throw new BuildException("Cannot deserialize deployment descriptor: "
                            + deploymentDescriptorFile, e);
                    }
                    if (!introduceServiceDeployment.getServiceName().equals(undeployService.getServiceName())
                        || !introduceServiceDeployment.getDeploymentPrefix().equals(
                            undeployService.getDeploymentPrefix())) {
                        otherDeployedServices.put(introduceServiceDeployment.getDeploymentPrefix() + "/"
                            + introduceServiceDeployment.getServiceName(), introduceServiceDeployment);
                    }
                }
            }
        }

    }


    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    System.err.println("could not remove directory: " + dir.getAbsolutePath());
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
