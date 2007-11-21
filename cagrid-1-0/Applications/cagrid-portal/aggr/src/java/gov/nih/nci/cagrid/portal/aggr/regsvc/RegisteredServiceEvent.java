/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.regsvc;

import org.springframework.context.ApplicationEvent;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class RegisteredServiceEvent extends ApplicationEvent {
	
	private String serviceUrl;
	
	private String indexServiceUrl;



	/**
	 * @param event source
	 */
	public RegisteredServiceEvent(Object source) {
		super(source);
	}

	
	public String getIndexServiceUrl() {
		return indexServiceUrl;
	}

	public void setIndexServiceUrl(String indexServiceUrl) {
		this.indexServiceUrl = indexServiceUrl;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
}
