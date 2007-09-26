/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.map;

import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.domain.Participant;
import gov.nih.nci.cagrid.portal2.util.PortalUtils;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class MapViewController extends AbstractController {

	private static final Log logger = LogFactory.getLog(MapViewController.class);
	private String viewName;
	private GridServiceDao gridServiceDao;
	private ParticipantDao participantDao;

	/**
	 * 
	 */
	public MapViewController() {

	}
	
	public void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		String category = request.getParameter("category");
		logger.debug("category = " + category);
		
		response.setRenderParameter("category", category);
	}
	
	protected ModelAndView handleRenderRequestInternal(RenderRequest request, RenderResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView(getViewName());
		
		String category = request.getParameter("category");
		logger.debug("category = " + category);
		
		MapBean mapBean = (MapBean) getApplicationContext().getBean("mapBean");
		
		try{
			if(PortalUtils.isEmpty(category)){
				category = "all";
			}
			
			List<GridService> services = null;
			List<Participant> participants = null;
			if("all".equals(category)){
				services = getGridServiceDao().getAll();
				participants = getParticipantDao().getAll();
			}else if("services".equals(category)){
				services = getGridServiceDao().getAll();
			}else if("dataServices".equals(category)){
				services = getGridServiceDao().getAllDataServices();
			}else if("analyticalServices".equals(category)){
				services = getGridServiceDao().getAllAnalyticalServices();
			}else if("participants".equals(category)){
				participants = getParticipantDao().getAll();
			}else{
				participants = getParticipantDao().getByWorkspaceAbbreviation(category);
			}
			
			if(services == null){
				services = new ArrayList<GridService>();
			}
			if(participants == null){
				participants = new ArrayList<Participant>();
			}
			
			logger.debug("Retrieved " + services.size() + " services.");
			for(GridService service : services){
				mapBean.addService(service);
			}
			logger.debug("Retrieved " + participants.size() + " participants.");
			for(Participant participant : participants){
				mapBean.addParticipant(participant);
			}			
		}catch(Exception ex){
			String msg = "Error populating map bean: " + ex.getMessage();
			logger.error(msg, ex);
			throw new Exception(msg, ex);
		}
		
		logger.debug("Setting category to " + category);
		mapBean.setCategory(category);
		mav.addObject("mapBean", mapBean);
		
		return mav;
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

	public ParticipantDao getParticipantDao() {
		return participantDao;
	}

	public void setParticipantDao(ParticipantDao participantDao) {
		this.participantDao = participantDao;
	}


}
