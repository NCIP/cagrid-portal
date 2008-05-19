package gov.nih.nci.cagrid.testing.system.deployment;

import gov.nih.nci.cagrid.testing.core.TestingConstants;

import java.io.File;
import java.io.IOException;

/** 
 *  ServiceContainerFactory
 *  Creates service container instances
 * 
 * @author David Ervin
 * 
 * @created Oct 16, 2007 12:09:02 PM
 * @version $Id: ServiceContainerFactory.java,v 1.2 2008-05-19 14:09:37 hastings Exp $ 
 */
public class ServiceContainerFactory {

    /**
     * Creates a new service container of the specified type.
     * No security will be enabled, and the service will start on a 
     * port determined automatically by the PortFactory.
     *  
     * @param type
     *      The container type
     * @return
     *      The service container instance
     * @throws IOException
     */
    public static ServiceContainer createContainer(ServiceContainerType type) throws IOException {
        ContainerPorts ports = PortFactory.getContainerPorts();
        return createContainer(type , ports);
    }
    

    /**
     * Creates a new service container, with all options configured by the caller.
     * 
     * @param type
     *      The type of container
     * @param ports
     *      The port preference to use when starting the container
     * @return
     *      The service container instance
     * @throws IOException
     */
    public static ServiceContainer createContainer(
        ServiceContainerType type, ContainerPorts ports) throws IOException {
        File containerTempDir = getTempDirectory(type);
        String zipLocation = type.getZip();
        File containerZip = new File(zipLocation);
        ContainerProperties props = new ContainerProperties(containerTempDir,
        	containerZip, ports, false,
        	null, null, null);
        
        ServiceContainer container = null;
        switch (type) {
            case GLOBUS_CONTAINER:
                container = new GlobusServiceContainer(props);
                break;
            case TOMCAT_CONTAINER:
                container = new TomcatServiceContainer(props);
                break;
            case SECURE_TOMCAT_CONTAINER:
                props.setSecure(true);
                container = new TomcatSecureServiceContainer(props);
                break;
            case JBOSS_CONTAINER:
                
                break;
            default:
                throw new AssertionError("Service container type: " + type + " is not valid");
        }
        return container;
    }

    
    
    private static File getTempDirectory(ServiceContainerType type) throws IOException {
        File tempDir = new File(TestingConstants.TEST_TEMP_DIR);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        File tempContainerDir = File.createTempFile(type.toString(), "tmp", tempDir);
        // create a directory, not a file
        tempContainerDir.delete();
        tempContainerDir.mkdirs();
        return tempContainerDir;
    }
    
}
