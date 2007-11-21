/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.aggr.AbstractMonitor;
import gov.nih.nci.cagrid.portal.aggr.ServiceUrlProvider;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ServiceStatusChangeMonitor extends AbstractMonitor {

	private static final Log logger = LogFactory
			.getLog(ServiceStatusChangeMonitor.class);

	private ServiceStatusProvider dynamicServiceStatusProvider;

	private ServiceStatusProvider cachedServiceStatusProvider;

	private ServiceUrlProvider cachedServiceUrlProvider;

	/**
	 * 
	 */
	public ServiceStatusChangeMonitor() {

	}

	public void checkForServiceStatusChanges() {

		for (String indexServiceUrl : getIndexServiceUrls()) {

			Set<String> serviceUrls = getCachedServiceUrlProvider().getUrls(
					indexServiceUrl);
			logger.debug("Got " + serviceUrls.size()
					+ " cached service URLs for index service "
					+ indexServiceUrl);
			for (String serviceUrl : serviceUrls) {

				ServiceStatus dynamicStatus = null;
				try {
					dynamicStatus = getDynamicServiceStatusProvider()
							.getStatus(serviceUrl);
				} catch (Exception ex) {
					logger.warn("Could not get dynamic service status: "
							+ ex.getMessage(), ex);
					continue;
				}
				ServiceStatus cachedStatus = null;
				try {
					cachedStatus = getCachedServiceStatusProvider().getStatus(
							serviceUrl);
				} catch (Exception ex) {
					logger.warn("Could not get cached service status: "
							+ ex.getMessage(), ex);
					continue;
				}

				if (dynamicStatus == null) {
					String msg = "dynamic status must not be null";
					logger.error(msg);
					throw new IllegalArgumentException(msg);
				}
				if (cachedStatus == null) {
					String msg = "cached status must not be null";
					logger.error(msg);
					throw new IllegalArgumentException(msg);
				}

				if (ServiceStatus.BANNED.equals(cachedStatus)) {
					logger.debug(serviceUrl
							+ " is banned. No event will be published.");
				} else if (!dynamicStatus.equals(cachedStatus)) {
					ServiceStatusChangeEvent event = new ServiceStatusChangeEvent(
							this);
					event.setOldStatus(cachedStatus);
					event.setNewStatus(dynamicStatus);
					event.setServiceUrl(serviceUrl);
					getApplicationContext().publishEvent(event);
				}

			}
		}

	}

	public ServiceStatusProvider getCachedServiceStatusProvider() {
		return cachedServiceStatusProvider;
	}

	public void setCachedServiceStatusProvider(
			ServiceStatusProvider cachedServiceStatusProvider) {
		this.cachedServiceStatusProvider = cachedServiceStatusProvider;
	}

	public ServiceUrlProvider getCachedServiceUrlProvider() {
		return cachedServiceUrlProvider;
	}

	public void setCachedServiceUrlProvider(
			ServiceUrlProvider cachedServiceUrlProvider) {
		this.cachedServiceUrlProvider = cachedServiceUrlProvider;
	}

	public ServiceStatusProvider getDynamicServiceStatusProvider() {
		return dynamicServiceStatusProvider;
	}

	public void setDynamicServiceStatusProvider(
			ServiceStatusProvider dynamicServiceStatusProvider) {
		this.dynamicServiceStatusProvider = dynamicServiceStatusProvider;
	}

}
