/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.metachange;

import org.springframework.context.ApplicationEvent;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class MetadataChangeEvent extends ApplicationEvent {

	private String serviceUrl;
	
	/**
	 * 
	 */
	public MetadataChangeEvent(Object source) {
		super(source);
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

}
