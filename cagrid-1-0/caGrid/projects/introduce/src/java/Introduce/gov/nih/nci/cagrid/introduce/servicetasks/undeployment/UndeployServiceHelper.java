package gov.nih.nci.cagrid.introduce.servicetasks.undeployment;

import gov.nih.nci.cagrid.introduce.servicetasks.beans.deployment.Deployment;
import gov.nih.nci.cagrid.introduce.servicetasks.beans.deployment.Jar;
import gov.nih.nci.cagrid.introduce.servicetasks.deployment.DeploymentFileGeneratorTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.iterators.ArrayIterator;
import org.globus.wsrf.encoding.DeserializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.xml.sax.InputSource;


public class UndeployServiceHelper {

    private String webAppDeployLocation;
    private String webAppDeployLibLocation;
    private String webAppDeploySchemaLocation;
    private String webAppDeployEtcLocation;
    private String serviceDeploymentDirectoryName;
    private String servicePrefix;
    private String serviceName;

    private Deployment undeployService = null;

    private Map<String, Deployment> otherDeployedServices = new HashMap<String, Deployment>();


    public UndeployServiceHelper(String webAppDeployLocation, String webAppDeployLibLocation,
        String webAppDeploySchemaLocation, String webAppDeployEtcLocation, String serviceDeploymentDirectoryName,
        String servicePrefix, String serviceName) {
        this.webAppDeployLocation = webAppDeployLocation;
        this.webAppDeployLibLocation = webAppDeployLibLocation;
        this.webAppDeploySchemaLocation = webAppDeploySchemaLocation;
        this.webAppDeployEtcLocation = webAppDeployEtcLocation;
        this.serviceDeploymentDirectoryName = serviceDeploymentDirectoryName;
        this.serviceName = serviceName;
        this.servicePrefix = servicePrefix;

    }


    public void execute() throws Exception {       

        System.out.println("webapp.deploy.dir=" + webAppDeployLocation);
        System.out.println("webapp.deploy.lib.dir=" + webAppDeployLibLocation);
        System.out.println("webapp.deploy.schema.dir=" + webAppDeploySchemaLocation);
        System.out.println("webapp.deploy.etc.dir=" + webAppDeployEtcLocation);
        System.out.println("service.deployment.dir.name=" + serviceDeploymentDirectoryName);
        System.out.println("service.deployment.prefix=" + servicePrefix);
        System.out.println("service.name=" + serviceName);

        if (webAppDeployLocation == null || webAppDeployLibLocation == null || webAppDeploySchemaLocation == null
            || webAppDeployEtcLocation == null || serviceName == null || servicePrefix == null
            || serviceDeploymentDirectoryName == null) {
            throw new Exception(
                "The properties: webapp.deploy.dir, webapp.deploy.lib.dir, webapp.deploy.schema.dir, webapp.deploy.etc.dir, service.deployment.dir.name, service.deployment.prefix, and service.name must be set");
        }

        // 2. look for the service that is going to be removed
        File deploymentDescriptor = new File(webAppDeployEtcLocation + File.separator + serviceDeploymentDirectoryName
            + File.separator + DeploymentFileGeneratorTask.DEPLOYMENT_PERSISTENCE_FILE);
        if (!deploymentDescriptor.exists() || !deploymentDescriptor.canRead()) {
            throw new Exception(
                "Service does not seem to be an Introduce generated service or file system permissions are preventing access.");
        }

        InputSource is = null;
        try {
            is = new InputSource(new FileInputStream(deploymentDescriptor));
        } catch (FileNotFoundException e) {
            throw new Exception("Connot find deployment descriptor: " + deploymentDescriptor, e);
        }

        try {
            undeployService = (Deployment) ObjectDeserializer.deserialize(is, Deployment.class);
        } catch (DeserializationException e) {
            throw new Exception("Cannot deserialize deployment descriptor: " + deploymentDescriptor, e);
        }

        // 3. build up map of other services dependencies
        loadOtherIntroduceServices();

        // 4. process the data for removal
        processForRemoval();

    }


    private void processForRemoval() throws Exception {

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
                            throw new Exception("ERROR: unable to delete jar on undeploy: " + jarFile.getAbsolutePath());
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
                    throw new Exception("ERROR: unable to completely remove schema location: "
                        + schemaLocation.getAbsolutePath());
                }
            }
        }

        // process the removal of the etc dir
        File etcLocation = new File(webAppDeployEtcLocation + File.separator + serviceDeploymentDirectoryName);
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


    public static Map<String, Deployment> loadIntroduceServices(String webAppEtcDeployLocation) throws Exception {

        Map<String, Deployment> deployedServices = new HashMap<String, Deployment>();
        File etcDir = new File(webAppEtcDeployLocation);
        if (!etcDir.exists() || !etcDir.canRead()) {
            throw new Exception("Cannot read the web app etc dir: " + etcDir.getAbsolutePath());
        }

        File[] etcFiles = etcDir.listFiles();
        for (int i = 0; i < etcFiles.length; i++) {
            if (etcFiles[i].exists() && etcFiles[i].canRead() && etcFiles[i].isDirectory()) {
                File deploymentDescriptorFile = new File(etcFiles[i].getAbsolutePath() + File.separator
                    + DeploymentFileGeneratorTask.DEPLOYMENT_PERSISTENCE_FILE);
                if (deploymentDescriptorFile.exists() && deploymentDescriptorFile.canRead()) {
                    InputSource is = null;
                    try {
                        is = new InputSource(new FileInputStream(deploymentDescriptorFile));
                    } catch (FileNotFoundException e) {
                        throw new Exception("Connot find deployment descriptor: " + deploymentDescriptorFile, e);
                    }
                    Deployment introduceServiceDeployment = null;
                    try {
                        introduceServiceDeployment = (Deployment) ObjectDeserializer.deserialize(is, Deployment.class);
                    } catch (DeserializationException e) {
                        throw new Exception("Cannot deserialize deployment descriptor: " + deploymentDescriptorFile, e);
                    }

                    deployedServices.put(introduceServiceDeployment.getDeploymentPrefix() + "/"
                        + introduceServiceDeployment.getServiceName(), introduceServiceDeployment);
                }
            }
        }

        return deployedServices;

    }


    private void loadOtherIntroduceServices() throws Exception {
        otherDeployedServices = loadIntroduceServices(webAppDeployEtcLocation);
        if (otherDeployedServices.containsKey(undeployService.getDeploymentPrefix() + "/"
            + undeployService.getServiceName())) {
            otherDeployedServices
                .remove(undeployService.getDeploymentPrefix() + "/" + undeployService.getServiceName());
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
