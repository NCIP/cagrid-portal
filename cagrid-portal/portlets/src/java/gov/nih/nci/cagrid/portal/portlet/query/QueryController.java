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
package gov.nih.nci.cagrid.portal.portlet.query;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import message.MessageHelper;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class QueryController extends AbstractController {

	private String viewName;

	private String commandName;
	
	private GridServiceDao gridServiceDao;

	/**
	 * 
	 */
	public QueryController() {

	}

	protected void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		QueryBean bean = getQueryBean(request);

		String url = request.getParameter("url");
		bean.setUrl(url);
		bean.setQuery(request.getParameter("query"));
		bean.query();
		response.setRenderParameter("url", url);
	}

	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getViewName());

		QueryBean bean = getQueryBean(request);
		bean.setUrl(getSelectedGridServiceUrl(request));
		mav.addObject(getCommandName(), bean);

		return mav;
	}

	private QueryBean getQueryBean(PortletRequest request) {
		QueryBean bean = (QueryBean) request.getPortletSession().getAttribute(
				getCommandName());
		if (bean == null) {
			bean = (QueryBean) getApplicationContext().getBean(
					"queryBeanPrototype");
			request.getPortletSession().setAttribute(getCommandName(), bean);
		}
		return bean;
	}

	public String getSelectedGridServiceUrl(PortletRequest request) {
		String url = request.getParameter("url");
		if (PortalUtils.isEmpty(url)) {

			logger.debug("looking for grid service id in session");
			
			PortletSession portletSession = request.getPortletSession(true);
			String id = getInstanceID(request);
			String msgSessionId = MessageHelper.getSessionID(request);
			MessageHelper.loadPrefs(request, id, msgSessionId);
			MessageHelper helper = new MessageHelper(portletSession, id,
					msgSessionId);
			Integer svcId = (Integer) helper.get("selectedGridServiceId");
			
			logger.debug("selectedGridServiceId: "+ svcId);
			
			GridService service = getGridServiceDao().getById(svcId);
			if (service != null) {
				url = service.getUrl();
				logger.debug("found grid service: " + url);
			}else{
				logger.error("No grid service found for id: " + svcId);
			}

		}else{
			logger.debug("Got url from request: " + url);
		}

		return url;
	}

	public String getInstanceID(PortletRequest request) {
		return "QueryController" + MessageHelper.getPortletID(request);
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

}
