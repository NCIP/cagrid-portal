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

import gov.nih.nci.cagrid.portal.dao.ConceptHierarchyNodeDao;
import gov.nih.nci.cagrid.portal.domain.ConceptHierarchyNode;
import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashSet;
import java.util.Set;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalConceptService implements ConceptService {

    private ConceptHierarchyNodeDao conceptHierarchyNodeDao;

    public Set<EVSConceptDTO> getConceptsForKeyword(String keyword) throws CaGridPortletApplicationException {
        Set<EVSConceptDTO> _resultset = new HashSet<EVSConceptDTO>();

        for (ConceptHierarchyNode node : conceptHierarchyNodeDao.getByName(keyword)) {
            _resultset.add(new EVSConceptDTO(node));
        }
        return _resultset;
    }

    @Required
    public ConceptHierarchyNodeDao getConceptHierarchyNodeDao() {
        return conceptHierarchyNodeDao;
    }

    public void setConceptHierarchyNodeDao(ConceptHierarchyNodeDao conceptHierarchyNodeDao) {
        this.conceptHierarchyNodeDao = conceptHierarchyNodeDao;
    }
}
