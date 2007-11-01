/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.model;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectServiceCommand {
	
	//TODO: Should provide custom validator and editor to conver to EPR.
	private String serviceUrl;

	/**
	 * 
	 */
	public SelectServiceCommand() {

	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

}
