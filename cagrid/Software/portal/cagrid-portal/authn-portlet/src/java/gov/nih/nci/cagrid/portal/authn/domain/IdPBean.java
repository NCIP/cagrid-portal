/**
 * 
 */
package gov.nih.nci.cagrid.portal.authn.domain;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 *
 */
public class IdPBean {
	private String label;
	private String url;
    
	public IdPBean(String label, String url) {
		this.label = label;
		this.url = url;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
