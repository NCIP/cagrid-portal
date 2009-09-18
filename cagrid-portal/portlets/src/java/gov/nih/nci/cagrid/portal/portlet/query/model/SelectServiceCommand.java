/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.model;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectServiceCommand {
	
	private String dataServiceUrl;

	/**
	 * 
	 */
	public SelectServiceCommand() {

	}

	public String getDataServiceUrl() {
		return dataServiceUrl;
	}

	public void setDataServiceUrl(String serviceUrl) {
		this.dataServiceUrl = serviceUrl;
	}

}
