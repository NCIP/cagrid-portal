/**
 * 
 */
package gov.nih.nci.cagrid.portal2.aggr.status;

import gov.nih.nci.cagrid.portal2.aggr.AbstractMonitor;
import gov.nih.nci.cagrid.portal2.aggr.ServiceUrlProvider;
import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal2.domain.StatusChange;

import java.sql.SQLException;
import java.util.Date;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Transactional
public class ServiceStatusMonitor extends AbstractMonitor {

	private static final Log logger = LogFactory
			.getLog(ServiceStatusMonitor.class);

	private ServiceUrlProvider cachedServiceUrlProvider;

	private ServiceStatusPolicy serviceStatusPolicy;

	private GridServiceDao gridServiceDao;

	/**
	 * 
	 */
	public ServiceStatusMonitor() {

	}

	public void checkServiceStatus() {

		for (String indexServiceUrl : getIndexServiceUrls()) {

			Set<String> serviceUrls = getCachedServiceUrlProvider().getUrls(
					indexServiceUrl);
			logger.debug("Got " + serviceUrls.size()
					+ " cached service URLs for index service "
					+ indexServiceUrl);
			for (String serviceUrl : serviceUrls) {

				GridService gridService = null;
				try {
					gridService = getGridServiceDao().getByUrl(serviceUrl);
				} catch (Exception ex) {
					String msg = "Error retrieving " + serviceUrl + ": "
							+ ex.getMessage();
					logger.error(msg);
					throw new RuntimeException(msg, ex);
				}

				if (gridService == null) {
					String msg = "Couldn't find service " + serviceUrl;
					logger.error(msg);
					throw new RuntimeException(msg);
				}

				try {

					if (getServiceStatusPolicy().shouldBanService(
							gridService.getStatusHistory())) {
						logger.debug("Banning " + serviceUrl);
						getGridServiceDao().banService(gridService);
					}

				} catch (Exception ex) {
					String msg = "Error checking status: " + ex.getMessage();
					logger.error(msg, ex);
					throw new RuntimeException(msg, ex);
				}

			}
		}

	}

	public ServiceUrlProvider getCachedServiceUrlProvider() {
		return cachedServiceUrlProvider;
	}

	public void setCachedServiceUrlProvider(
			ServiceUrlProvider cachedServiceUrlProvider) {
		this.cachedServiceUrlProvider = cachedServiceUrlProvider;
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

	public ServiceStatusPolicy getServiceStatusPolicy() {
		return serviceStatusPolicy;
	}

	public void setServiceStatusPolicy(ServiceStatusPolicy serviceStatusPolicy) {
		this.serviceStatusPolicy = serviceStatusPolicy;
	}

}
