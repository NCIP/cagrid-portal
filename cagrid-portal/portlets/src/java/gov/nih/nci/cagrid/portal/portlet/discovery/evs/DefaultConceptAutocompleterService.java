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

import org.springframework.beans.factory.annotation.Required;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@RemoteProxy(name = "EVSAutomcompleter",
        creator = SpringCreator.class,
        creatorParams = @Param(name = "beanName",
                value = "evsAutoCompleter"))
public class DefaultConceptAutocompleterService implements ConceptAutocompleterService {

    private ConceptService conceptService;
    private ConceptCodeSearchProvider ccSearchProvider;

    @RemoteMethod
    public List<EVSConceptDTO> autoCompleteConcept(String partialConceptName) {
        List<EVSConceptDTO> returnList = new ArrayList<EVSConceptDTO>();
        returnList.addAll(conceptService.getConceptsForKeyword(partialConceptName));
        return returnList;
    }

    @RemoteMethod
    public String resultCount(String code) {
     return String.valueOf(ccSearchProvider.search(code).size());
    }


    public ConceptCodeSearchProvider getCcSearchProvider() {
        return ccSearchProvider;
    }

    public void setCcSearchProvider(ConceptCodeSearchProvider ccSearchProvider) {
        this.ccSearchProvider = ccSearchProvider;
    }

    @Required
    public ConceptService getConceptService() {
        return conceptService;
    }

    public void setConceptService(ConceptService conceptService) {
        this.conceptService = conceptService;
    }


}
