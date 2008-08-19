package gov.nih.nci.cagrid.portal.portlet.discovery.evs;

import gov.nih.nci.cagrid.portal.dao.ConceptHierarchyNodeDao;
import gov.nih.nci.cagrid.portal.domain.DomainObject;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.portlet.discovery.filter.ServiceFilter;

import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class SemanticConceptCodeSearchProvider implements ConceptCodeSearchProvider{

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
