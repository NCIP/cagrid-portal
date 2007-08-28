package gov.nci.nih.cagrid.validator.steps.gme;

import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;

import com.atomicobject.haste.framework.Step;

/** 
 *  BaseGmeTestStep
 *  Base step for all GME related testing
 * 
 * @author David Ervin
 * 
 * @created Aug 27, 2007 4:12:17 PM
 * @version $Id: BaseGmeTestStep.java,v 1.1 2007-08-28 14:03:14 dervin Exp $ 
 */
public abstract class BaseGmeTestStep extends Step {
    
    private String serviceUrl;
    private XMLDataModelService gmeHandle;
    
    public BaseGmeTestStep(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
    

    public abstract void runStep() throws Throwable;
    
    
    protected String getServiceUrl() {
        return this.serviceUrl;
    }
    
    
    protected XMLDataModelService getGmeHandle() throws MobiusException {
        if (gmeHandle == null) {
            GridServiceResolver.getInstance().setDefaultFactory(
                new GlobusGMEXMLDataModelServiceFactory());
            gmeHandle = (XMLDataModelService) GridServiceResolver.getInstance()
                .getGridService(serviceUrl);
        }
        return gmeHandle;
    }
}
