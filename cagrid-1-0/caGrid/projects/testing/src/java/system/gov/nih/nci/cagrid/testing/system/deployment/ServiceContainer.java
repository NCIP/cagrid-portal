package gov.nih.nci.cagrid.testing.system.deployment;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.ZipUtilities;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;

/** 
 *  ServiceContainer
 *  Performs operations on a service container
 * 
 * @author David Ervin
 * 
 * @created Oct 12, 2007 9:37:44 AM
 * @version $Id: ServiceContainer.java,v 1.3 2007-11-05 16:19:58 dervin Exp $ 
 */
public abstract class ServiceContainer {
    
    private ContainerProperties properties = null;
    
    protected boolean unpacked = false;    
    protected boolean started = false;
    
    public ServiceContainer(ContainerProperties properties) {
        this.properties = properties;
    }
    
    
    public void unpackContainer() throws ContainerException {
        try {
            ZipUtilities.unzip(properties.getContainerZip(), properties.getContainerDirectory());
        } catch (IOException ex) {
            throw new ContainerException("Error unziping container: " + ex.getMessage(), ex);
        }
        unpacked = true;
    }
    
    
    public void deleteContainer() throws ContainerException {
        if (started) {
            throw new ContainerException("Cannot delete running container");
        }
        Utils.deleteDir(properties.getContainerDirectory());
    }
    
    
    public void startContainer() throws ContainerException {
        if (started) {
            throw new ContainerException("Container is already started");
        }
        if (!unpacked) {
            throw new ContainerException("Container has not been unpacked");
        }
        startup();
        started = true;
    }
    
    
    public void stopContainer() throws ContainerException {
        if (!unpacked) {
            throw new ContainerException("Container has not been unpacked");
        }
        shutdown();
        started = false;
    }
    
    
    public void deployService(File serviceDir) throws Exception {
        deployService(serviceDir, null);
    }
    
    
    public void deployService(File serviceDir, List<String> deployArgs) throws Exception {
        if (started) {
            throw new ContainerException("Container has already been started");
        }
        if (!unpacked) {
            throw new ContainerException("Container has not been unpacked");
        }
        deploy(serviceDir, deployArgs);
    }
    
    
    public boolean isStarted() {
        return started;
    }
    
    
    public boolean isUnpacked() {
        return unpacked;
    }
    
    
    public ContainerProperties getProperties() {
        return properties;
    }
    
    
    public synchronized URI getContainerBaseURI() throws MalformedURIException {
        String url = "";
        try {
            if (getProperties().isSecure()) {
                url += "https://";
            } else {
                url += "http://";
            }
            url += "localhost:" + getProperties().getPortPreference().getPort() + "/wsrf/services/";
        } catch (NoAvailablePortException e) {
            throw new MalformedURIException("Problem getting port:" + e.getMessage());
        }
        return new URI(url);
    }
    
        
    protected abstract void startup() throws ContainerException;
    
    
    protected abstract void shutdown() throws ContainerException;
    
    
    protected abstract void deploy(File serviceDir, List<String> deployArgs) throws ContainerException;
}
