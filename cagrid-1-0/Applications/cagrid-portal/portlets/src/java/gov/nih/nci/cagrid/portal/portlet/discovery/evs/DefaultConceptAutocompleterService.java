package gov.nih.nci.cagrid.portal.portlet.discovery.evs;

import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DefaultConceptAutocompleterService implements ConceptAutocompleterService {

    private ConceptService conceptService;
    private ConceptCodeSearchProvider ccSearchProvider;


    public List<EVSConceptDTO> autoCompleteConcept(String partialConceptName) {
        List<EVSConceptDTO> returnList = new ArrayList<EVSConceptDTO>();
        returnList.addAll(conceptService.getConceptsForKeyword(partialConceptName));
        return returnList;
    }

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
