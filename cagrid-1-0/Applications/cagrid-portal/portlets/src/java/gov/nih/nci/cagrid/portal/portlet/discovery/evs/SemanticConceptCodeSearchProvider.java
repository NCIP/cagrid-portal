package gov.nih.nci.cagrid.portal.portlet.discovery.evs;

import gov.nih.nci.cagrid.portal.dao.ConceptHierarchyNodeDao;
import gov.nih.nci.cagrid.portal.domain.DomainObject;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;

import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class SemanticConceptCodeSearchProvider implements ConceptCodeSearchProvider{

    private ConceptHierarchyNodeDao conceptHierarchyNodeDao;

    public List<? extends DomainObject> search(String conceptCode) {
        List<GridService> svcs = conceptHierarchyNodeDao.getServicesByCode(conceptCode);
        svcs = PortletUtils.filterServicesByInvalidMetadata(PortletUtils.filterDormantServices(PortletUtils.filterBannedServices(svcs)));
        return svcs;
    }

    public ConceptHierarchyNodeDao getConceptHierarchyNodeDao() {
        return conceptHierarchyNodeDao;
    }

    public void setConceptHierarchyNodeDao(ConceptHierarchyNodeDao conceptHierarchyNodeDao) {
        this.conceptHierarchyNodeDao = conceptHierarchyNodeDao;
    }
}
