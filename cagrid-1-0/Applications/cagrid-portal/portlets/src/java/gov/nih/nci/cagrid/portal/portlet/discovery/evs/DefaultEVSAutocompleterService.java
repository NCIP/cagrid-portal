package gov.nih.nci.cagrid.portal.portlet.discovery.evs;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DefaultEVSAutocompleterService implements EVSAutocompleterService {

    EVSService evsService;

    public List<EVSConceptDTO> autoCompleteConcept(String partialConceptCode) {
        List<EVSConceptDTO> returnList = new ArrayList<EVSConceptDTO>();
        returnList.addAll(evsService.getConceptsForKeyword(partialConceptCode));
        return returnList;
    }


    public void setEvsService(EVSService evsService) {
        this.evsService = evsService;
    }
}
