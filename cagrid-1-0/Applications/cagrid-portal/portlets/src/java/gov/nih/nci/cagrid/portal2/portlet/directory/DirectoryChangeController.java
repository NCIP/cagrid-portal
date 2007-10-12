/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.directory;

import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal2.domain.Participant;
import gov.nih.nci.cagrid.portal2.portlet.SharedApplicationModel;
import gov.nih.nci.cagrid.portal2.portlet.discovery.DiscoveryResultsBean;
import gov.nih.nci.cagrid.portal2.portlet.discovery.DiscoveryType;
import gov.nih.nci.cagrid.portal2.util.PortalUtils;

import java.util.Iterator;
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
	
	private SharedApplicationModel sharedApplicationModel;

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
			String type = null;
			List objects = null;
			if(category.startsWith("search:")){
				String resultsId = category.substring(category.indexOf(":") + 1);
				DiscoveryResultsBean results = getSharedApplicationModel().getDiscoveryResultsBean(resultsId);
				if(results == null){
					throw new Exception("No results found for '" + category + "'");
				}
				type = results.getType().toString();
				objects = results.getObjects();
			}else if ("services".equals(category)) {
				type = DiscoveryType.SERVICE.toString();
				objects = getGridServiceDao().getAll();
			} else if ("dataServices".equals(category)) {
				type = DiscoveryType.SERVICE.toString();
				objects = getGridServiceDao().getAllDataServices();
			} else if ("analyticalServices".equals(category)) {
				type = DiscoveryType.SERVICE.toString();
				objects = getGridServiceDao().getAllAnalyticalServices();
			} else if ("participants".equals(category)) {
				type = DiscoveryType.PARTICIPANT.toString();
				objects = getParticipantDao().getAll();
			} else {
				type = DiscoveryType.PARTICIPANT.toString();
				objects = getParticipantDao().getByWorkspaceAbbreviation(
						category);
			}
			
			//HACK: Initialize associations so that directory view works outside
			//of current session.
			if(DiscoveryType.PARTICIPANT.toString().equals(type)){
				for(Iterator i = objects.iterator(); i.hasNext();){
					Participant p = (Participant)i.next();
					p.getParticipation();
				}
			}

			directory.setType(type);
			directory.setCategory(category);
			directory.getScroller().setObjects(objects);
			getSharedApplicationModel().setSelectedDirectoryBean(directory);

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

	public SharedApplicationModel getSharedApplicationModel() {
		return sharedApplicationModel;
	}

	public void setSharedApplicationModel(
			SharedApplicationModel sharedApplicationState) {
		this.sharedApplicationModel = sharedApplicationState;
	}

}
