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
 * @version $Id: ServiceContainerFactory.java,v 1.5 2007-12-18 16:28:36 dervin Exp $ 
 */
public class ServiceContainerFactory {
    
    public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";
    
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
        return createContainer(type, false);
    }
    
    
    /**
     * Creates a new service container of the specified type.
     * The service will start on a port determined automatically by the PortFactory.
     * 
     * @param type
     *      The container type
     * @param secure
     *      If set to true, the default security options for the container
     *      will be enabled
     * @return
     *      The service container instance 
     * @throws IOException
     */
    public static ServiceContainer createContainer(ServiceContainerType type, boolean secure) throws IOException {
        PortPreference ports = PortFactory.getPort();
        return createContainer(type, secure, null, ports);
    }
    
    
    /**
     * Creates a new, secured service container of the specified type.
     * The service will start on a port determined automatically by the PortFactory.
     * 
     * @param type
     *      The type of container
     * @param securityDescriptor
     *      The security descriptor which the container will use
     * @return
     *      The service container instance
     * @throws IOException
     */
    public static ServiceContainer createContainer(ServiceContainerType type, File securityDescriptor) throws IOException {
        PortPreference ports = PortFactory.getPort();
        return createContainer(type, true, securityDescriptor, ports);
    }
    
    
    @Deprecated
    public static ServiceContainer createContainer(
        ServiceContainerType type, File securityDescriptor, PortPreference ports) throws IOException {
        return createContainer(type, true, securityDescriptor, ports);
    }
    
    
    /**
     * Creates a new service container, with all options configured by the caller.
     * 
     * @param type
     *      The type of container
     * @param secure
     *      If set to true, security will be enabled on the container
     * @param securityDescriptor
     *      If non-null, represents the security descriptor which the container will use.
     *      Only relavent if @secure == true
     * @param ports
     *      The port preference to use when starting the container
     * @return
     *      The service container instance
     * @throws IOException
     */
    public static ServiceContainer createContainer(
        ServiceContainerType type, boolean secure, File securityDescriptor, PortPreference ports) throws IOException {
        File containerTempDir = getTempDirectory(type);
        String zipLocation = type.getZip();
        String introduceLocation = getIntroduceBaseDir();
        if (introduceLocation != null) {
            zipLocation = introduceLocation + File.separator + zipLocation;
        }
        File containerZip = new File(zipLocation);
        ContainerProperties props = new ContainerProperties(containerTempDir,
        	containerZip, ports, secure, securityDescriptor,
        	null, null, null);
        
        ServiceContainer container = null;
        switch (type) {
            case GLOBUS_CONTAINER:
                container = new GlobusServiceContainer(props);
                break;
            case TOMCAT_CONTAINER:
                container = new TomcatServiceContainer(props);
                break;
            case JBOSS_CONTAINER:
                
                break;
            default:
                throw new AssertionError("Service container type: " + type + " is not valid");
        }
        return container;
    }
    
    
    /**
     * 
     * @param type
     * @param securityDescriptor
     * @param ports
     * @param maxStartupTime units are seconds
     * @param maxShutdownTime units are seconds
     * @param heapSizeInMegabytes whole value between 32 and probably 1024. This is passed to the command line. E.g,. -Xmx128m. Pass null here for default
     * @return
     *      The service container instance
     * @throws IOException
     */
    public static ServiceContainer createContainer(
        ServiceContainerType type, File securityDescriptor, PortPreference ports,
        Integer maxStartupTime, Integer maxShutdownTime, Integer heapSizeInMegabytes) throws IOException {
        File containerTempDir = getTempDirectory(type);
        String zipLocation = type.getZip();
        String introduceLocation = getIntroduceBaseDir();
        if (introduceLocation != null) {
            zipLocation = introduceLocation + File.separator + zipLocation;
        }
        File containerZip = new File(zipLocation);
        ContainerProperties props = new ContainerProperties(containerTempDir,
        	containerZip, ports, securityDescriptor != null, securityDescriptor, 
        	maxStartupTime, maxShutdownTime, heapSizeInMegabytes);
        
        ServiceContainer container = null;
        switch (type) {
            case GLOBUS_CONTAINER:
                container = new GlobusServiceContainer(props);
                break;
            case TOMCAT_CONTAINER:
                container = new TomcatServiceContainer(props);
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
    
    
    private static String getIntroduceBaseDir() {
        String dir = System.getProperty(INTRODUCE_DIR_PROPERTY);
        return dir;
    }
}
