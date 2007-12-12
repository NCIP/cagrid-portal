/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.news;

import gov.nih.nci.cagrid.portal.dao.news.NewsChannelDao;
import gov.nih.nci.cagrid.portal.dao.news.NewsItemDao;
import gov.nih.nci.cagrid.portal.domain.news.NewsChannel;
import gov.nih.nci.cagrid.portal.domain.news.NewsItem;
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
public class EditItemController extends SimpleFormController {

	private NewsItemDao newsItemDao;
	private NewsChannelDao newsChannelDao;
	
	/**
	 * 
	 */
	public EditItemController() {
		setRenderParameters(new String[]{"operation", "itemId"});
	}
	
	
	
	protected Object formBackingObject(PortletRequest request) throws Exception {
		NewsItem item = new NewsItem();
		String itemId = request.getParameter("itemId");
		if(itemId != null){
			item = getNewsItemDao().getById(Integer.parseInt(itemId));
		}else{
			String channelId = request.getParameter("channelId");
			if(channelId == null){
				throw new Exception("Either itemId or channelId must be given.");
			}
			NewsChannel channel = getNewsChannelDao().getById(Integer.parseInt(channelId));
			item.setChannel(channel);
		}
		return item;
	}
	
	protected void initBinder(PortletRequest request,
			PortletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(String.class, "title",
				new XSSFilterEditor(binder.getBindingResult(), "title"));
		binder.registerCustomEditor(String.class, "link",
				new XSSFilterEditor(binder.getBindingResult(), "link"));
		binder.registerCustomEditor(String.class, "description",
				new XSSFilterEditor(binder.getBindingResult(), "description"));
		binder.registerCustomEditor(String.class, "height",
				new XSSFilterEditor(binder.getBindingResult(), "heigth"));
		binder.registerCustomEditor(String.class, "width",
				new XSSFilterEditor(binder.getBindingResult(), "width"));
	}
	
	protected void onSubmitAction(ActionRequest request,
			ActionResponse response, Object command, BindException errors)
			throws Exception {
		NewsItem item = (NewsItem)command;
		NewsChannel channel = item.getChannel();
		channel = getNewsChannelDao().getById(channel.getId());
		String editOp = request.getParameter("editOp");
		logger.debug("editOp: " + editOp);
		if("save".equals(editOp)){
			getNewsItemDao().save(item);
			if(!channel.getItems().contains(item)){
				channel.getItems().add(item);
				getNewsChannelDao().save(channel);
			}
			getNewsItemDao().getHibernateTemplate().flush();
		}else if("delete".equals(editOp)){
			item = getNewsItemDao().getById(item.getId());
			channel.getItems().remove(item);
			item.setChannel(null);
			getNewsItemDao().delete(item);
			getNewsChannelDao().save(channel);
			getNewsItemDao().getHibernateTemplate().flush();
			
		}else{
			throw new Exception("invalid editOp: " + editOp);
		}
		response.setRenderParameter("operation", "editItem");
		response.setRenderParameter("editOp", editOp);
		response.setRenderParameter("channelId", String.valueOf(channel.getId()));
	}
	
	protected ModelAndView onSubmitRender(RenderRequest request,
			RenderResponse response, Object command, BindException errors)
			throws Exception {
			ModelAndView mav = new ModelAndView(getSuccessView());
			String editOp = request.getParameter("editOp");
			if("save".equals(editOp)){
				mav.addObject("confirmMessage", "Item saved.");
				mav.addObject(getCommandName(), command);
			}else if("delete".equals(editOp)){
				mav.addObject("confirmMessage", "Item deleted.");
			}
			mav.addObject("channelId", request.getParameter("channelId"));
		return mav;
	}

	public NewsItemDao getNewsItemDao() {
		return newsItemDao;
	}

	public void setNewsItemDao(NewsItemDao newsItemDao) {
		this.newsItemDao = newsItemDao;
	}

	public NewsChannelDao getNewsChannelDao() {
		return newsChannelDao;
	}

	public void setNewsChannelDao(NewsChannelDao newsChannelDao) {
		this.newsChannelDao = newsChannelDao;
	}

}
