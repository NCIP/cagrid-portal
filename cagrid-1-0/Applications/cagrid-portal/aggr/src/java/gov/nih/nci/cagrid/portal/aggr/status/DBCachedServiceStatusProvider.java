/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Transactional
public class DBCachedServiceStatusProvider implements ServiceStatusProvider {

	private GridServiceDao gridServiceDao;

	/**
	 * 
	 */
	public DBCachedServiceStatusProvider() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.aggr.status.ServiceStatusProvider#getStatus(java.lang.String)
	 */
	public ServiceStatus getStatus(String serviceUrl) {

		GridService service = null;
		try {
			service = getGridServiceDao().getByUrl(serviceUrl);
		} catch (Exception ex) {
			throw new RuntimeException("Error retrieving service " + serviceUrl
					+ ": " + ex.getMessage(), ex);
		}
		if (service == null) {
			throw new RuntimeException("Couldn't find service " + serviceUrl);
		}
		return service.getCurrentStatus();
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

}
