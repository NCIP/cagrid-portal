package gov.nih.nci.cagrid.portal.portlet.discovery.evs;

import gov.nih.nci.cagrid.portal.dao.ConceptHierarchyNodeDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
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
    private ConceptHierarchyNodeDao conceptHierarchyNodeDao;


    public List<EVSConceptDTO> autoCompleteConcept(String partialConceptName) {
        List<EVSConceptDTO> returnList = new ArrayList<EVSConceptDTO>();
        returnList.addAll(conceptService.getConceptsForKeyword(partialConceptName));
        return returnList;
    }

    public String resultCount(String code) {
        List<GridService> svcs = conceptHierarchyNodeDao.getServicesByCode(code);
        return String.valueOf(svcs.size());
    }

    @Required
    public ConceptHierarchyNodeDao getConceptHierarchyNodeDao() {
        return conceptHierarchyNodeDao;
    }

    public void setConceptHierarchyNodeDao(ConceptHierarchyNodeDao conceptHierarchyNodeDao) {
        this.conceptHierarchyNodeDao = conceptHierarchyNodeDao;
    }

    public ConceptService getConceptService() {
        return conceptService;
    }

    public void setConceptService(ConceptService conceptService) {
        this.conceptService = conceptService;
    }


}
