/**
 * $Id: QueryWizardTreeBeanInit.java,v 1.1 2006-10-03 12:33:21 joshua Exp $
 *
 */
package gov.nih.nci.cagrid.browser.tree.beans;

import org.apache.log4j.Logger;

/**
 *
 * @version $Revision: 1.1 $
 * @author Joshua Phillips
 *
 */
public class QueryWizardTreeBeanInit {
    
    private static Logger logger = Logger.getLogger(QueryWizardTreeBeanInit.class);
    
    private String serviceUrl;
    
    private String domainObject;

    
    public String getDomainObject() {
        return domainObject;
    }


    public void setDomainObject(String domainObject) {
        this.domainObject = domainObject;
    }


    public String getServiceUrl() {
        return serviceUrl;
    }


    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }


    public String init(){
	String outcome = "success";
	
	logger.debug("serviceUrl=" + getServiceUrl() + ", domainObject=" + getDomainObject());
	
	
	
	return outcome;
    }
    
}
