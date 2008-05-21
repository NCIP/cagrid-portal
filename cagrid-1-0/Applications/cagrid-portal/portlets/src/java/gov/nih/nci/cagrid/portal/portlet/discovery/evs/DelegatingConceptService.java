package gov.nih.nci.cagrid.portal.portlet.discovery.evs;

import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DelegatingConceptService extends BaseConceptService {

    private List<ConceptService> conceptServices = new ArrayList<ConceptService>();


    public Set<EVSConceptDTO> getConceptsForKeyword(String keyword) throws CaGridPortletApplicationException {
        Set<EVSConceptDTO> _conceptSet = new HashSet<EVSConceptDTO>();

        outer:
        for (ConceptService service : conceptServices) {
            Set<EVSConceptDTO> _tempResult;
            try {
                _tempResult = service.getConceptsForKeyword(keyword);
            } catch (CaGridPortletApplicationException e) {
                logger.warn("Encountered error searching for concepts.Returning incomplete Resultset");
                return _conceptSet;
            }
            for (EVSConceptDTO _dto : _tempResult) {
                if (_conceptSet.size() >= getSearchLimit()) {
                    logger.debug("Reached search limit for concepts");
                    break outer;
                }
                _conceptSet.add(_dto);

            }
        }
        return _conceptSet;
    }


    public List<ConceptService> getConceptServices() {
        return conceptServices;
    }

    public void setConceptServices(List<ConceptService> conceptServices) {
        this.conceptServices = conceptServices;
    }
}
