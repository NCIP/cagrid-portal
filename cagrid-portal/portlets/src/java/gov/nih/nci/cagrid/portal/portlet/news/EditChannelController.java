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
import gov.nih.nci.cagrid.portal.portlet.PortletConstants;
import gov.nih.nci.cagrid.portal.portlet.util.XSSFilterEditor;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
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
		setRenderParameters(new String[]{"operation"});
	}
	
	protected void initBinder(PortletRequest request,
			PortletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(String.class, "title",
				new XSSFilterEditor(binder.getBindingResult(), "title"));
		binder.registerCustomEditor(String.class, "link",
				new XSSFilterEditor(binder.getBindingResult(), "link"));
		binder.registerCustomEditor(String.class, "description",
				new XSSFilterEditor(binder.getBindingResult(), "description"));
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
