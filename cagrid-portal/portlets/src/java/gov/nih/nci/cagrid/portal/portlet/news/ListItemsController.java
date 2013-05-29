/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.news;

import gov.nih.nci.cagrid.portal.dao.news.NewsChannelDao;
import gov.nih.nci.cagrid.portal.domain.news.NewsChannel;
import gov.nih.nci.cagrid.portal.portlet.util.Scroller;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ListItemsController extends AbstractController {

	private NewsChannelDao newsChannelDao;
	private String successView;
	private String objectName;
	private String sessionAttributeName;

	/**
	 * 
	 */
	public ListItemsController() {

	}

	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getSuccessView());
		ItemsListBean itemsListBean = null;
		String channelId = request.getParameter("channelId");
		if (channelId != null) {
			NewsChannel channel = getNewsChannelDao().getById(
					Integer.parseInt(channelId));
			itemsListBean = (ItemsListBean) getApplicationContext()
					.getBean("itemsListBeanPrototype");
			itemsListBean.setChannel(channel);
			itemsListBean.getScroller().setObjects(channel.getItems());
			request.getPortletSession().setAttribute(getSessionAttributeName(),
					itemsListBean);
		}else{
			itemsListBean = (ItemsListBean) request.getPortletSession().getAttribute(getSessionAttributeName());
			if(itemsListBean == null){
				throw new Exception("No ItemsListBean found under " + getSessionAttributeName());
			}
		}
		mav.addObject(getObjectName(), itemsListBean);
		return mav;
	}

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public NewsChannelDao getNewsChannelDao() {
		return newsChannelDao;
	}

	public void setNewsChannelDao(NewsChannelDao newsChannelDao) {
		this.newsChannelDao = newsChannelDao;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getSessionAttributeName() {
		return sessionAttributeName;
	}

	public void setSessionAttributeName(String scrollerAttributeName) {
		this.sessionAttributeName = scrollerAttributeName;
	}

}
