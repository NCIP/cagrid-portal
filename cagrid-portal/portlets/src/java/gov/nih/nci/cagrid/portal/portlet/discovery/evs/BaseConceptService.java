package gov.nih.nci.cagrid.portal.portlet.discovery.evs;

import gov.nih.nci.system.applicationservice.EVSApplicationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public abstract class BaseConceptService implements ConceptService {
    protected static final Log logger = LogFactory.getLog(ConceptService.class);

    protected EVSApplicationService appService;
    protected String vocabulary = "default";
    protected int searchLimit = 100; //default

    @Required
    public EVSApplicationService getAppService() {
        return appService;
    }

    public void setAppService(EVSApplicationService appService) {
        this.appService = appService;
    }

    public String getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(String vocabulary) {
        this.vocabulary = vocabulary;
    }

    public int getSearchLimit() {
        return searchLimit;
    }

    public void setSearchLimit(int searchLimit) {
        this.searchLimit = searchLimit;
    }
}
