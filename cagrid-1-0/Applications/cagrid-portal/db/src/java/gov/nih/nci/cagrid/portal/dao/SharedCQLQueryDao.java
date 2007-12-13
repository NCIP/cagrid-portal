/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao;

import java.util.List;

import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.dataservice.SharedCQLQuery;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SharedCQLQueryDao extends AbstractDao<SharedCQLQuery> {

	/**
	 * 
	 */
	public SharedCQLQueryDao() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return SharedCQLQuery.class;
	}

	public List<SharedCQLQuery> getByDataService(GridDataService dataService) {
		return getHibernateTemplate().find(
				"from SharedCQLQuery s where s.targetService.id = ?",
				new Object[] { dataService.getId() });
	}

}
