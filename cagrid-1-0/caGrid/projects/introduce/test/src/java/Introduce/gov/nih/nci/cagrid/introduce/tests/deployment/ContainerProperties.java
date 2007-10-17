package gov.nih.nci.cagrid.introduce.tests.deployment;

import java.io.File;

/** 
 *  ContainerProperties
 *  Properties of the service container
 * 
 * @author David Ervin
 * 
 * @created Oct 12, 2007 12:10:03 PM
 * @version $Id: ContainerProperties.java,v 1.1 2007-10-17 17:00:44 dervin Exp $ 
 */
public class ContainerProperties {

    private File containerDirectory = null;
    private File containerZip = null;
    private PortPreference portPreference = null;
    private boolean secure;
    private File securityDescriptor = null;
    
    public ContainerProperties() {
    }
    
    
    public ContainerProperties(File containerDirectory, File containerZip, PortPreference portPreference, boolean secure, File securityDescriptor) {
        this.containerDirectory = containerDirectory;
        this.containerZip = containerZip;
        this.portPreference = portPreference;
        this.secure = secure;
        this.securityDescriptor = securityDescriptor;
    }

    
    public File getContainerDirectory() {
        return containerDirectory;
    }
    
    
    public void setContainerDirectory(File containerDirectory) {
        this.containerDirectory = containerDirectory;
    }
    
    
    public File getContainerZip() {
        return containerZip;
    }
    
    
    public void setContainerZip(File containerZip) {
        this.containerZip = containerZip;
    }
    
    
    public PortPreference getPortPreference() {
        return portPreference;
    }
    
    
    public void setPortPreference(PortPreference portPreference) {
        this.portPreference = portPreference;
    }
    
    
    public boolean isSecure() {
        return secure;
    }
    
    
    public void setSecure(boolean secure) {
        this.secure = secure;
    }
    
    
    public File getSecurityDescriptor() {
        return securityDescriptor;
    }
    
    
    public void setSecurityDescriptor(File securityDescriptor) {
        this.securityDescriptor = securityDescriptor;
    }
}
