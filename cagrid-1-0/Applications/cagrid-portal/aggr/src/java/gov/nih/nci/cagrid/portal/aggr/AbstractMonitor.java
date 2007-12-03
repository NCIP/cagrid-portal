/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class AbstractMonitor implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	private String[] indexServiceUrls = new String[0];

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public ApplicationContext getApplicationContext(){
		return this.applicationContext;
	}

	public String[] getIndexServiceUrls() {
		return indexServiceUrls;
	}

	public void setIndexServiceUrls(String[] indexServiceUrls) {
		this.indexServiceUrls = indexServiceUrls;
	}
	
}
