/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet;

import gov.nih.nci.cagrid.portal.dao.UMLClassDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRelationshipInstance;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRelationshipType;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryCommand;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CriterionBean;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * 
 */
public class UserModel {

	private PortalUser portalUser;
	private CatalogEntry currentCatalogEntry;
	private CatalogEntryRelationshipType currentRelationshipType;
	private CatalogEntryRelationshipInstance currentRelationshipInstance;
	private QueryInstance currentQueryInstance;
	
	//From QueryModel
	private GridDataService selectedService;
	private UMLClassDao umlClassDao;
	private UMLClass selectedUmlClass;
	//TODO: this should be changed to a QueryCommand, i.e. not CQL-specific
	private CQLQueryCommand workingQuery;
	private CriterionBean selectedCriterion;
	private QueryInstance selectedQueryInstance;
	
	/**
	 * 
	 */
	public UserModel() {

	}
	
	
	public void selectUmlClassForQuery(Integer umlClassId) {
        if (getSelectedUmlClass() != null
                && getSelectedUmlClass().getId().equals(umlClassId)) {
            // Do nothing
        } else {
            UMLClass umlClass = getUmlClassDao().getById(umlClassId);
            setSelectedUmlClass(umlClass);
            setSelectedService(umlClass.getModel().getService());
        }
    }
	

	public PortalUser getPortalUser() {
		return portalUser;
	}

	public void setPortalUser(PortalUser portalUser) {
		this.portalUser = portalUser;
	}

	public CatalogEntry getCurrentCatalogEntry() {
		return currentCatalogEntry;
	}

	public void setCurrentCatalogEntry(CatalogEntry currentCatalogEntry) {
		this.currentCatalogEntry = currentCatalogEntry;
	}

	public CatalogEntryRelationshipType getCurrentRelationshipType() {
		return currentRelationshipType;
	}

	public void setCurrentRelationshipType(
			CatalogEntryRelationshipType currentRelationshipType) {
		this.currentRelationshipType = currentRelationshipType;
	}

	public CatalogEntryRelationshipInstance getCurrentRelationshipInstance() {
		return currentRelationshipInstance;
	}

	public void setCurrentRelationshipInstance(
			CatalogEntryRelationshipInstance currentRelationshipInstance) {
		this.currentRelationshipInstance = currentRelationshipInstance;
	}

	public QueryInstance getCurrentQueryInstance() {
		return currentQueryInstance;
	}

	public void setCurrentQueryInstance(QueryInstance currentQueryInstance) {
		this.currentQueryInstance = currentQueryInstance;
	}

	public void setSelectedService(GridDataService selectedService) {
		this.selectedService = selectedService;
	}

	public GridDataService getSelectedService() {
		return selectedService;
	}


	public UMLClassDao getUmlClassDao() {
		return umlClassDao;
	}


	public void setUmlClassDao(UMLClassDao umlClassDao) {
		this.umlClassDao = umlClassDao;
	}


	public UMLClass getSelectedUmlClass() {
		return selectedUmlClass;
	}


	public void setSelectedUmlClass(UMLClass selectedUmlClass) {
		this.selectedUmlClass = selectedUmlClass;
	}


	public CQLQueryCommand getWorkingQuery() {
		return workingQuery;
	}


	public void setWorkingQuery(CQLQueryCommand workingQuery) {
		this.workingQuery = workingQuery;
	}


	public CriterionBean getSelectedCriterion() {
		return selectedCriterion;
	}


	public void setSelectedCriterion(CriterionBean selectedCriterion) {
		this.selectedCriterion = selectedCriterion;
	}


	public QueryInstance getSelectedQueryInstance() {
		return selectedQueryInstance;
	}


	public void setSelectedQueryInstance(QueryInstance selectedQueryInstance) {
		this.selectedQueryInstance = selectedQueryInstance;
	}

}
