/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery;

import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.domain.GridService;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import message.MessageHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ListServicesController extends AbstractController {
	
	private static final Log logger = LogFactory.getLog(ListServicesController.class);
	
	private GridServiceDao gridServiceDao;
	private boolean showAllIfNoneProvided;
	private int defaultRange = 10;
	private String listView;
	
	protected ModelAndView handleRenderRequestInternal(RenderRequest request, RenderResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView(getListView());
		PortletSession portletSession = request.getPortletSession(true);
        String id = getInstanceID(request);
        String msgSessionId = MessageHelper.getSessionID(request);
        
        // load this portlet's inputs and outputs from preferences
        MessageHelper.loadPrefs(request, id, msgSessionId);

        MessageHelper helper = new MessageHelper(portletSession, id, msgSessionId);
        List gridServices = (List)helper.get("gridServices");
        if(gridServices == null){
        	if(isShowAllIfNoneProvided()){
        		gridServices = getGridServiceDao().getAll();
        		logger.info("No gridServices message found. Fetching all.");
        		helper.send("gridServices", gridServices);
        	}else{
        		logger.info("No gridServices message found, and shouldn't fetch.");
        		gridServices = new ArrayList();
        	}
        }
        
        int total = gridServices.size();
        List gridServicesInRange = new ArrayList();
        int range = getDefaultRange();
        String rangeParam = request.getParameter("range");
        if(rangeParam != null){
        	try{
        		range = Integer.parseInt(rangeParam);
        	}catch(Exception ex){
        		logger.error("Error parsing range parameter '" + range + "', using default = " + range);
        	}
        }
        if(range < 1){
        	range = getDefaultRange();
        	logger.error("Error - range is < 0, using default = " + range);
        }
        
        int offset = 0;
        String offsetParam = request.getParameter("offset");
        if(offsetParam != null){
        	try{
        		offset = Integer.parseInt(offsetParam);
        	}catch(Exception ex){
        		logger.error("Error parsing offset parameter '" + offset + "', using default = 0");
        	}
        }
        
        if(offset < 0){
        	offset = 0;
        }else if(offset >= total){
        	offset = total - 1;
        }
        
        logger.debug("range = " + range + ", offset = " + offset + ", total = " + total);
        for(int i = offset; i < range + offset && i < total; i++){
        	GridService gridService = (GridService)gridServices.get(i);
        	logger.debug("Adding grid service " + gridService.getUrl());
        	gridServicesInRange.add(gridServices.get(i));
        }
        
        mav.addObject("gridServicesInRange", gridServicesInRange);
        mav.addObject("total", String.valueOf(total));
        if(offset > 0){
        	mav.addObject("first", "0");
        	mav.addObject("previous", String.valueOf(offset - range));
        }
        if(offset + range < total){
        	mav.addObject("next", String.valueOf(offset + range));
        	mav.addObject("last", String.valueOf(total - range));
        }
		
		return mav;
	}
	
	public String getInstanceID(PortletRequest request)
    {
        return "ListServices" + MessageHelper.getPortletID(request);
    }

	public int getDefaultRange() {
		return defaultRange;
	}

	public void setDefaultRange(int defaultRange) {
		this.defaultRange = defaultRange;
	}

	public String getListView() {
		return listView;
	}

	public void setListView(String listView) {
		this.listView = listView;
	}

	public boolean isShowAllIfNoneProvided() {
		return showAllIfNoneProvided;
	}

	public void setShowAllIfNoneProvided(boolean showAllIfNoneProvided) {
		this.showAllIfNoneProvided = showAllIfNoneProvided;
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

}
