/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.tree;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class TextContent {
	
	private String text;

	/**
	 * 
	 */
	public TextContent() {
		this(null);
	}

	public TextContent(String text) {
		setText(text);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
