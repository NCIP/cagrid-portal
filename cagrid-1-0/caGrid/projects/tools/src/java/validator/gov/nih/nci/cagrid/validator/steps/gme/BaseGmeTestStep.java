package gov.nih.nci.cagrid.validator.steps.gme;

import gov.nih.nci.cagrid.validator.steps.AbstractBaseServiceTestStep;

import java.io.File;
import java.util.Properties;

import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;

/** 
 *  BaseGmeTestStep
 *  Base step for all GME related testing
 * 
 * @author David Ervin
 * 
 * @created Aug 27, 2007 4:12:17 PM
 * @version $Id: BaseGmeTestStep.java,v 1.1 2008-03-25 14:20:30 dervin Exp $ 
 */
public abstract class BaseGmeTestStep extends AbstractBaseServiceTestStep {
    
    private XMLDataModelService gmeHandle;
    
    public BaseGmeTestStep() {
        super();
    }
    
    
    public BaseGmeTestStep(String serviceUrl, File tempDir, Properties configuration) {
        super(serviceUrl, tempDir, configuration);
    }
    

    public abstract void runStep() throws Throwable;
    
    
    protected XMLDataModelService getGmeHandle() throws MobiusException {
        if (gmeHandle == null) {
            GridServiceResolver.getInstance().setDefaultFactory(
                new GlobusGMEXMLDataModelServiceFactory());
            gmeHandle = (XMLDataModelService) GridServiceResolver.getInstance()
                .getGridService(getServiceUrl());
        }
        return gmeHandle;
    }
}
