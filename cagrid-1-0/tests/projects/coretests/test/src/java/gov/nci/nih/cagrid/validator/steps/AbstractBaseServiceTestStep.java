package gov.nci.nih.cagrid.validator.steps;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  AbstractBaseServiceTestStep
 *  Base step from which all service testing steps must inherit
 * 
 * @author David Ervin
 * 
 * @created Sep 5, 2007 11:47:16 AM
 * @version $Id: AbstractBaseServiceTestStep.java,v 1.1 2007-09-05 17:01:35 dervin Exp $ 
 */
public abstract class AbstractBaseServiceTestStep extends Step {
    
    private String serviceUrl;
    private File tempDir;
    
    public AbstractBaseServiceTestStep(String serviceUrl, File tempDir) {
        this.serviceUrl = serviceUrl;
        this.tempDir = tempDir;
    }
    

    public String getServiceUrl() {
        return this.serviceUrl;
    }
    
    
    public File getTempDir() {
        return this.tempDir;
    }
}
