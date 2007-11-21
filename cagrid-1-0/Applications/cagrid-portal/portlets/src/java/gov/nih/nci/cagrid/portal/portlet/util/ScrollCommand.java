/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.util;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ScrollCommand {

	private ScrollOperation scrollOp;
	
	/**
	 * 
	 */
	public ScrollCommand() {

	}

	public ScrollOperation getScrollOp() {
		return scrollOp;
	}

	public void setScrollOp(ScrollOperation scrollOp) {
		this.scrollOp = scrollOp;
	}

}
