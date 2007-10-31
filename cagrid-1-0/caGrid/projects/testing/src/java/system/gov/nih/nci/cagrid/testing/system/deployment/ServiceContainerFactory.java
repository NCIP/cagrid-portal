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
 * @version $Id: ServiceContainerFactory.java,v 1.1 2007-10-31 19:29:07 dervin Exp $ 
 */
public class ServiceContainerFactory {
    
    public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";
    
    public static ServiceContainer createContainer(ServiceContainerType type) throws IOException {
        PortPreference ports = PortPreference.getDefaultPortPreference();
        return createContainer(type, null, ports);
    }
    
    
    public static ServiceContainer createContainer(
        ServiceContainerType type, File securityDescriptor, PortPreference ports) throws IOException {
        File containerTempDir = getTempDirectory(type);
        String zipLocation = type.getZip();
        String introduceLocation = getIntroduceBaseDir();
        if (introduceLocation != null) {
            zipLocation = introduceLocation + File.separator + zipLocation;
        }
        File containerZip = new File(zipLocation);
        ContainerProperties props = new ContainerProperties();
        props.setContainerDirectory(containerTempDir);
        props.setContainerZip(containerZip);
        props.setPortPreference(ports);
        props.setSecure(securityDescriptor != null);
        props.setSecurityDescriptor(securityDescriptor);
        
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
