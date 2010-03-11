/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.news;

import gov.nih.nci.cagrid.portal.dao.news.NewsItemDao;
import gov.nih.nci.cagrid.portal.domain.news.NewsItem;
import gov.nih.nci.cagrid.portal.portlet.InterPortletMessageSender;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractCommandController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectItemForNewsController extends AbstractCommandController {
	
	private String redirectUrlPreferenceName;
	private InterPortletMessageSender interPortletMessageSender;
	private NewsItemDao newsItemDao;

	/**
	 * 
	 */
	public SelectItemForNewsController() {

	}

	/**
	 * @param commandClass
	 */
	public SelectItemForNewsController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SelectItemForNewsController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.AbstractCommandController#handleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void handleAction(ActionRequest request, ActionResponse response,
			Object obj, BindException errors) throws Exception {
		SelectItemCommand command = (SelectItemCommand)obj;
		NewsItem item = getNewsItemDao().getById(command.getItemId());
		getInterPortletMessageSender().send(request, item);
		String redirectUrl = request.getPreferences().getValue(getRedirectUrlPreferenceName(), null);
		if(redirectUrl == null){
			throw new Exception("No redirect URL preference provided.");
		}
		response.sendRedirect(redirectUrl);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.AbstractCommandController#handleRender(javax.portlet.RenderRequest, javax.portlet.RenderResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView handleRender(RenderRequest arg0,
			RenderResponse arg1, Object arg2, BindException arg3)
			throws Exception {
		throw new IllegalArgumentException(getClass().getName() + " doesn't handle render requests.");
	}

	@Required
	public String getRedirectUrlPreferenceName() {
		return redirectUrlPreferenceName;
	}

	public void setRedirectUrlPreferenceName(String redirectUrl) {
		this.redirectUrlPreferenceName = redirectUrl;
	}

	@Required
	public InterPortletMessageSender getInterPortletMessageSender() {
		return interPortletMessageSender;
	}

	public void setInterPortletMessageSender(
			InterPortletMessageSender interPortletMessageSender) {
		this.interPortletMessageSender = interPortletMessageSender;
	}

	@Required
	public NewsItemDao getNewsItemDao() {
		return newsItemDao;
	}

	public void setNewsItemDao(NewsItemDao newsItemDao) {
		this.newsItemDao = newsItemDao;
	}

}
