/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.authn;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class IdPUrl {

	private String url;
	private String label;
	
	/**
	 * 
	 */
	public IdPUrl() {

	}
	
	public IdPUrl(String label, String url){
		this.label = label;
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
