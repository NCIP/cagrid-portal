package gov.nih.nci.cagrid.portal2.portlet.pointofcontact;

import gov.nih.nci.cagrid.portal2.dao.PointOfContactDao;
import gov.nih.nci.cagrid.portal2.domain.metadata.common.PointOfContact;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletRequest;
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
public class ListPointOfContactsController extends AbstractController {
	
	private static final Log logger = LogFactory.getLog(ListPointOfContactsController.class);
	
	private PointOfContactDao pointOfContactDao;
	private int defaultRange = 10;
	private String listView;
	
	protected ModelAndView handleRenderRequestInternal(RenderRequest request, RenderResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView(getListView());
        MessageHelper.loadPrefs(request);
        MessageHelper helper = new MessageHelper(request);
        
        List pointOfContactIds = (List)helper.get("pointOfContactIds");
        
       
        if(pointOfContactIds == null){
        	logger.debug("################### pointOfContactIds is null #################");
        
        }else if(pointOfContactIds.size() == 0){
        	logger.debug("################### pointOfContactIds.size() = " + pointOfContactIds.size() + " #################");
        }
        
        if(pointOfContactIds != null && pointOfContactIds.size() > 0){
        	
        	//TODO: do a better search
        	List<PointOfContact> pointOfContacts = new ArrayList<PointOfContact>();
        	for(Iterator i = pointOfContactIds.iterator(); i.hasNext();){
        		Integer pocId = (Integer)i.next();
        		PointOfContact poc = getPointOfContactDao().getById(pocId);
        		pointOfContacts.add(poc);
        	}
        	
        	int total = pointOfContacts.size();
            List pointOfContactsInRange = new ArrayList();
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
            	PointOfContact pointOfContact = (PointOfContact)pointOfContacts.get(i);
            	logger.debug("Adding point of contact " + pointOfContact);
            	pointOfContactsInRange.add(pointOfContacts.get(i));
            }
            
            mav.addObject("pointOfContactsInRange", pointOfContactsInRange);
            mav.addObject("total", String.valueOf(total));
            if(offset > 0){
            	mav.addObject("first", "0");
            	mav.addObject("previous", String.valueOf(offset - range));
            }
            if(offset + range < total){
            	mav.addObject("next", String.valueOf(offset + range));
            	mav.addObject("last", String.valueOf(total - range));
            }
        }
		
		return mav;
	}
	
	public String getInstanceID(PortletRequest request)
    {
        return "ListPointOfContacts" + MessageHelper.getPortletID(request);
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

	public PointOfContactDao getPointOfContactDao() {
		return pointOfContactDao;
	}

	public void setPointOfContactDao(PointOfContactDao pointOfContactDao) {
		this.pointOfContactDao = pointOfContactDao;
	}

}
