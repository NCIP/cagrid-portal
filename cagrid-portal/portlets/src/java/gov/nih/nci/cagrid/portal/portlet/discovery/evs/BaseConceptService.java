/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
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
