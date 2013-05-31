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

import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DelegatingConceptService extends BaseConceptService {

    private List<ConceptService> conceptServices = new ArrayList<ConceptService>();


    public Set<EVSConceptDTO> getConceptsForKeyword(String keyword) throws CaGridPortletApplicationException {
    	//Use TreeSet so that we don't lose the ordering from the concept service
        Set<EVSConceptDTO> _conceptSet = new TreeSet<EVSConceptDTO>();

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
