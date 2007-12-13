/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao;

import java.util.List;

import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class CQLQueryInstanceDao extends AbstractDao<CQLQueryInstance> {

	/**
	 * 
	 */
	public CQLQueryInstanceDao() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return CQLQueryInstance.class;
	}

	public List<CQLQueryInstance> getByDataService(GridDataService dataService) {
		return getHibernateTemplate().find(
				"from CQLQueryInstance inst where inst.dataService.id = ?",
				new Object[] { dataService.getId() });
	}

}
