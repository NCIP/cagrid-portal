/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.portlet.discovery.evs;

import gov.nih.nci.cagrid.portal.domain.DomainObject;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryResults;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryType;
import gov.nih.nci.cagrid.portal.portlet.discovery.search.KeywordSearchBean;
import gov.nih.nci.cagrid.portal.portlet.discovery.search.KeywordSearchService;

import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class KeywordConceptCodeSearchProvider implements ConceptCodeSearchProvider{

    private KeywordSearchService keywordSearchService;


    public List<? extends DomainObject> search(String conceptCode) {
        KeywordSearchBean searchBean = new KeywordSearchBean();
        searchBean.setKeywords(conceptCode);
        searchBean.setDiscoveryType(DiscoveryType.SERVICE);
        searchBean.setSearchFields(new String[]{"conceptCode"});

        DiscoveryResults svcs = keywordSearchService.search(searchBean);
        return svcs.getObjects();
    }

    public KeywordSearchService getKeywordSearchService() {
        return keywordSearchService;
    }

    public void setKeywordSearchService(KeywordSearchService keywordSearchService) {
        this.keywordSearchService = keywordSearchService;
    }
}
