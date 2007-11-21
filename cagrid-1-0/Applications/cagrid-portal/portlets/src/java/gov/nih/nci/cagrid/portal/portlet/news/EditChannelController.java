/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.news;

import gov.nih.nci.cagrid.portal.dao.news.NewsChannelDao;
import gov.nih.nci.cagrid.portal.domain.news.NewsChannel;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.SimpleFormController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class EditChannelController extends SimpleFormController {

	private NewsChannelDao newsChannelDao;

	/**
	 * 
	 */
	public EditChannelController() {

	}

	protected Object formBackingObject(PortletRequest request) throws Exception {
		NewsChannel channel = new NewsChannel();
		String channelId = request.getParameter("channelId");
		if (channelId != null) {
			channel = getNewsChannelDao().getById(Integer.parseInt(channelId));
		}
		return channel;
	}

	protected void onSubmitAction(ActionRequest request,
			ActionResponse response, Object command, BindException errors)
			throws Exception {
		NewsChannel channel = (NewsChannel)command;
		String editOp = request.getParameter("editOp");
		logger.debug("editOp: " + editOp);
		if("save".equals(editOp)){
			getNewsChannelDao().save(channel);	
		}else if("delete".equals(editOp)){
			logger.debug("Deleting channel " + channel.getId());
			getNewsChannelDao().delete(channel);
		}else{
			throw new Exception("invalid editOp: " + editOp);
		}
		getNewsChannelDao().getHibernateTemplate().flush();
		response.setRenderParameter("operation", "editChannel");
		response.setRenderParameter("editOp", editOp);
	}

	protected ModelAndView onSubmitRender(RenderRequest request,
			RenderResponse response, Object command, BindException errors)
			throws Exception {
			ModelAndView mav = new ModelAndView(getSuccessView());
			String editOp = request.getParameter("editOp");
			if("save".equals(editOp)){
				mav.addObject("confirmMessage", "Channel saved.");
				mav.addObject(getCommandName(), command);
			}else if("delete".equals(editOp)){
				mav.addObject("confirmMessage", "Channel deleted.");
			}
		return mav;
	}

	public NewsChannelDao getNewsChannelDao() {
		return newsChannelDao;
	}

	public void setNewsChannelDao(NewsChannelDao newChannelDao) {
		this.newsChannelDao = newChannelDao;
	}

}
