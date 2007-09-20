/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.directory;

import gov.nih.nci.cagrid.portal2.portlet.util.Scroller;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DirectoryBean {
	
	private String type;
	private String category;
	private Scroller scroller;

	/**
	 * 
	 */
	public DirectoryBean() {
	}

	public Scroller getScroller() {
		return scroller;
	}

	public void setScroller(Scroller scroller) {
		this.scroller = scroller;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
