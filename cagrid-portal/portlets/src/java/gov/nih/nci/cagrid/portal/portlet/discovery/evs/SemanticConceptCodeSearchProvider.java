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
import gov.nih.nci.cagrid.portal.domain.DomainObject;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.util.filter.ServiceFilter;

import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class SemanticConceptCodeSearchProvider implements ConceptCodeSearchProvider {

    private ConceptHierarchyNodeDao conceptHierarchyNodeDao;
    private ServiceFilter servicefilter;

    public List<? extends DomainObject> search(String conceptCode) {
        List<GridService> svcs = conceptHierarchyNodeDao.getServicesByCode(conceptCode);
        svcs = servicefilter.filter(svcs);
        return svcs;
    }

    public ConceptHierarchyNodeDao getConceptHierarchyNodeDao() {
        return conceptHierarchyNodeDao;
    }

    public void setConceptHierarchyNodeDao(ConceptHierarchyNodeDao conceptHierarchyNodeDao) {
        this.conceptHierarchyNodeDao = conceptHierarchyNodeDao;
    }

    public ServiceFilter getServicefilter() {
        return servicefilter;
    }

    public void setServicefilter(ServiceFilter servicefilter) {
        this.servicefilter = servicefilter;
    }
}
