/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.directory;

import gov.nih.nci.cagrid.portal2.portlet.util.Scroller;
import gov.nih.nci.cagrid.portal2.util.PortalUtils;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DirectoryViewController extends AbstractController {

	private String viewName;

	/**
	 * 
	 */
	public DirectoryViewController() {

	}

	public void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {

		logger.debug("action request");
		try {
			DirectoryBean directory = getDirectoryBean(request);

			Scroller scroller = directory.getScroller();
			String scrollOp = request.getParameter("scrollOp");
			logger.debug("scrollOp = '" + scrollOp + "'");
			if (!PortalUtils.isEmpty(scrollOp)) {
				if ("first".equals(scrollOp)) {
					scroller.first();
				} else if ("previous".equals(scrollOp)) {
					scroller.previous();
				} else if ("next".equals(scrollOp)) {
					scroller.next();
				} else if ("last".equals(scrollOp)) {
					scroller.last();
				} else {
					logger.warn("Invalid scroll operation: '" + scrollOp + "'");
				}
			}
		} catch (Exception ex) {
			String msg = "Error handling action request: " + ex.getMessage();
			logger.error(msg, ex);
			throw ex;
		}
	}

	private DirectoryBean getDirectoryBean(PortletRequest request) {

		DirectoryBean directory = (DirectoryBean) request.getPortletSession()
				.getAttribute("directoryBean");
		if (directory == null) {
			logger.debug("Putting new directoryBean in session");
			directory = (DirectoryBean) getApplicationContext().getBean(
					"directoryBeanPrototype");
			request.getPortletSession()
					.setAttribute("directoryBean", directory);
		}
		return directory;
	}

	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getViewName());

		logger.debug("render request");
		try {

			mav.addObject("directoryBean", getDirectoryBean(request));
		} catch (Exception ex) {
			String msg = "Error handling render request: " + ex.getMessage();
			logger.error(msg, ex);
			throw ex;
		}
		return mav;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

}
