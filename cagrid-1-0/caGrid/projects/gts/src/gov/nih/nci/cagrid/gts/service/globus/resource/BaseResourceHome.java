package gov.nih.nci.cagrid.gts.service.globus.resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.Resource;
import org.globus.wsrf.impl.SingletonResourceHome;


/**
 * This class implements a resource home
 */

public class BaseResourceHome extends SingletonResourceHome {

	static final Log logger = LogFactory.getLog(BaseResourceHome.class);

	private String gtsConfig;


	public Resource findSingleton() {
		logger.info("Creating a single resource.");
		try {
			BaseResource resource = new BaseResource();
			resource.initialize();
			return resource;
		} catch (Exception e) {
			logger.error("Exception when creating the resource: " + e);
			return null;
		}
	}


	public String getGtsConfig() {
		return gtsConfig;
	}


	public void setGtsConfig(String gtsConfig) {
		this.gtsConfig = gtsConfig;
	}
}