/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.directory;

import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal2.util.PortalUtils;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DirectoryChangeController extends AbstractController {

	private String successAction;

	private GridServiceDao gridServiceDao;

	private ParticipantDao participantDao;

	/**
	 * 
	 */
	public DirectoryChangeController() {

	}

	protected void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {

		String category = request.getParameter("category");
		if (PortalUtils.isEmpty(category)) {
			category = "services";
		}
		logger.debug("category = " + category);

		try {

			DirectoryBean directory = (DirectoryBean) getApplicationContext()
					.getBean("directoryBeanPrototype");
			request.getPortletSession()
					.setAttribute("directoryBean", directory);
			String type = null;
			List objects = null;
			if ("services".equals(category)) {
				type = "services";
				objects = getGridServiceDao().getAll();
			} else if ("dataServices".equals(category)) {
				type = "services";
				objects = getGridServiceDao().getAllDataServices();
			} else if ("analyticalServices".equals(category)) {
				type = "services";
				objects = getGridServiceDao().getAllAnalyticalServices();
			} else if ("participants".equals(category)) {
				type = "participants";
				objects = getParticipantDao().getAll();
			} else {
				type = "participants";
				objects = getParticipantDao().getByWorkspaceAbbreviation(
						category);
			}

			directory.setType(type);
			directory.setCategory(category);
			directory.getScroller().setObjects(objects);

		} catch (Exception ex) {
			String msg = "Error populating scroller: " + ex.getMessage();
			logger.error(msg, ex);
			throw new Exception(msg, ex);
		}

		response.setRenderParameter("action", getSuccessAction());
	}

	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		throw new IllegalArgumentException("This method should not be called.");
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

	public ParticipantDao getParticipantDao() {
		return participantDao;
	}

	public void setParticipantDao(ParticipantDao participantDao) {
		this.participantDao = participantDao;
	}

	public String getSuccessAction() {
		return successAction;
	}

	public void setSuccessAction(String successAction) {
		this.successAction = successAction;
	}

}
