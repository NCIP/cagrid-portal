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
package gov.nih.nci.cagrid.portal.portlet.sample;

import gov.nih.nci.cagrid.evsgridservice.client.EVSGridServiceClient;
import gov.nih.nci.cagrid.evs.service.EVSMetaThesaurusSearchParams;
import gov.nih.nci.evs.domain.MetaThesaurusConcept;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class EVSSearchHelper {

    // default value.   
    private String evsGridServiceUrl = "http://cagrid-service.nci.nih.gov:8080/wsrf/services/cagrid/EVSGridService";

    public EVSSearchHelper() {
    }

    public EVSSearchHelper(String evsGridServiceUrl) {
        this.evsGridServiceUrl = evsGridServiceUrl;
    }

    public String conceptCodeSearch(String keyword) throws Exception{
        EVSGridServiceClient client = new EVSGridServiceClient(getEvsGridServiceUrl());

        EVSMetaThesaurusSearchParams evsMetaThesaurusSearchParam = new EVSMetaThesaurusSearchParams();
        evsMetaThesaurusSearchParam.setSearchTerm(keyword);
        evsMetaThesaurusSearchParam.setLimit(1);
        evsMetaThesaurusSearchParam.setSource("*");
        evsMetaThesaurusSearchParam.setCui(false);
        evsMetaThesaurusSearchParam.setShortResponse(false);
        evsMetaThesaurusSearchParam.setScore(false);

        MetaThesaurusConcept[] metaConcept = client.searchMetaThesaurus(evsMetaThesaurusSearchParam);

        if (metaConcept != null && metaConcept.length > 0){
            MetaThesaurusConcept meta = metaConcept[0];
            return "Concept Code: " + meta.getCui();
        }

        else{
            return "Concept not found";
        }

    }

    public String getEvsGridServiceUrl() {
        return evsGridServiceUrl;
    }

    public void setEvsGridServiceUrl(String evsGridServiceUrl) {
        this.evsGridServiceUrl = evsGridServiceUrl;
    }
}
