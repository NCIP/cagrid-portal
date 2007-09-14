/**
 * 
 */
package gov.nih.nci.cagrid.portal2.aggr;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class AbstractMonitor implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	private Set<String> indexServiceUrls = new HashSet<String>();

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

	public Set<String> getIndexServiceUrls() {
		return indexServiceUrls;
	}

	public void setIndexServiceUrls(Set<String> indexServiceUrls) {
		this.indexServiceUrls = indexServiceUrls;
	}
	
}
