/**
 * 
 */
package gov.nih.nci.cagrid.portal.liferay;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * 
 */
public class CaGridPortalContext implements InitializingBean {

	private ApplicationContext applicationContext;
	private List<String> configLocations = new ArrayList<String>();

	/**
	 * 
	 */
	public CaGridPortalContext() {

	}

	public void afterPropertiesSet() throws Exception {
		if (getApplicationContext() == null && getConfigLocations().size() > 0) {
			String[] locations = new String[getConfigLocations().size()];
			for (int i = 0; i < locations.length; i++) {
				locations[i] = getConfigLocations().get(i);
			}
			try {
				setApplicationContext(new ClassPathXmlApplicationContext(
						locations));
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(
						"Error loading application context: " + ex.getMessage(),
						ex);
			}
		}

	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public List<String> getConfigLocations() {
		return configLocations;
	}

	public void setConfigLocations(List<String> configLocations) {
		this.configLocations = configLocations;
	}

}
