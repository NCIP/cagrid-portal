/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.news;

import gov.nih.nci.cagrid.portal2.domain.news.NewsChannel;
import gov.nih.nci.cagrid.portal2.portlet.util.Scroller;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ItemsListBean {
	
	private NewsChannel channel;
	private Scroller scroller;

	/**
	 * 
	 */
	public ItemsListBean() {

	}

	public NewsChannel getChannel() {
		return channel;
	}

	public void setChannel(NewsChannel channel) {
		this.channel = channel;
	}

	public Scroller getScroller() {
		return scroller;
	}

	public void setScroller(Scroller scroller) {
		this.scroller = scroller;
	}

}
