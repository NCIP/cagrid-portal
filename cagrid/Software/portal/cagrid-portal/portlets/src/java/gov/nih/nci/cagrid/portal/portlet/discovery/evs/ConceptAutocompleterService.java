package gov.nih.nci.cagrid.portal.portlet.discovery.evs;

import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface ConceptAutocompleterService {
    List<EVSConceptDTO> autoCompleteConcept(String partialConceptName);

    String resultCount(String code);

}
