/**
 * 
 */
package gov.nih.nci.cagrid.portal2.aggr.regsvc;

import gov.nih.nci.cagrid.portal2.aggr.AbstractMonitor;
import gov.nih.nci.cagrid.portal2.aggr.ServiceUrlProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class RegisteredServiceMonitor extends AbstractMonitor {

	private static final Log logger = LogFactory
			.getLog(RegisteredServiceMonitor.class);

	/*
	 * Pulls service URLs from an IndexService
	 */
	private ServiceUrlProvider dynamicServiceUrlProvider;

	/*
	 * Pulls service URLs from a cache (e.g. a database)
	 */
	private ServiceUrlProvider cachedServiceUrlProvider;



	public void checkForNewServices() {

		logger.debug("Checking for new services...");
		
		Collection<RegisteredServiceEvent> events = new ArrayList<RegisteredServiceEvent>();
		Set<String> urls = getIndexServiceUrls();
		logger.debug("Checking " + urls.size() + " urls.");
		for (String indexSvcUrl : urls) {
			logger.debug("On Index Service: " + indexSvcUrl);
			Set<String> cachedSvcUrls = null;
			try {
				cachedSvcUrls = getCachedServiceUrlProvider().getUrls(
						indexSvcUrl);
			} catch (Exception ex) {
				logger.warn("Error retrieving cached service urls: "
						+ ex.getMessage(), ex);
				continue;
			}

			Set<String> dynamicSvcUrls = null;
			try {
				dynamicSvcUrls = getDynamicServiceUrlProvider().getUrls(
						indexSvcUrl);
			} catch (Exception ex) {
				logger.warn("Error retrieving dynamic service urls: "
						+ ex.getMessage(), ex);
			}

			if (cachedSvcUrls == null) {
				throw new IllegalArgumentException(
						"cached service urls must not be null");
			}

			if (dynamicSvcUrls == null) {
				throw new IllegalArgumentException(
						"dynamic service urls must not be null");
			}

			logger.debug("Found " + cachedSvcUrls.size()
					+ " cached services and " + dynamicSvcUrls.size()
					+ " dynamic services.");

			for (String svcUrl : dynamicSvcUrls) {
				if (!cachedSvcUrls.contains(svcUrl)) {
					RegisteredServiceEvent event = new RegisteredServiceEvent(
							this);
					event.setIndexServiceUrl(indexSvcUrl);
					event.setServiceUrl(svcUrl);
					events.add(event);
				}
			}
		}

		logger.debug("Events to publish: " + events.size());
		for (RegisteredServiceEvent event : events) {
//			logger.debug("Publishing event for " + event.getServiceUrl());
			getApplicationContext().publishEvent(event);
		}

	}

	public ServiceUrlProvider getCachedServiceUrlProvider() {
		return cachedServiceUrlProvider;
	}

	public void setCachedServiceUrlProvider(
			ServiceUrlProvider cachedServiceUrlProvider) {
		this.cachedServiceUrlProvider = cachedServiceUrlProvider;
	}

	public ServiceUrlProvider getDynamicServiceUrlProvider() {
		return dynamicServiceUrlProvider;
	}

	public void setDynamicServiceUrlProvider(
			ServiceUrlProvider dynamicServiceUrlProvider) {
		this.dynamicServiceUrlProvider = dynamicServiceUrlProvider;
	}

	

}
