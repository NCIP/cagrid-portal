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

import java.util.List;

import gov.nih.nci.cagrid.portal.dao.news.NewsItemDao;
import gov.nih.nci.cagrid.portal.domain.news.NewsItem;
import gov.nih.nci.cagrid.portal.portlet.InterPortletMessageReceiver;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewNewsController extends AbstractController {

	private String successView;
	private String selectedItemAttribute = ViewNewsController.class.getName() + ".selectedNewsItem";
	private NewsItemDao newsItemDao;
	private int newsItemsLimit = 10;
	private InterPortletMessageReceiver interPortletMessageReceiver;
	
	/**
	 * 
	 */
	public ViewNewsController() {

	}
	
	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
            RenderResponse response)
     throws Exception{
		ModelAndView mav = new ModelAndView(getSuccessView());
		List<NewsItem> items = getNewsItemDao().getLatestNewsItems(getNewsItemsLimit());
		NewsItem selectedItem = getSelectedItem(request);
		
		//Make sure it hasn't been deleted
		if(selectedItem != null && !items.contains(selectedItem)){
			selectedItem = null;
		}
		
		//If no selectedItem, just use latest.
		if(selectedItem == null){
			if(items.size() > 0){
				selectedItem = items.get(0);
			}
		}
		
		if(selectedItem != null){
			mav.addObject("selectedItem", selectedItem);
		}
		mav.addObject("items", items);
		return mav;
	}
	
	protected NewsItem getSelectedItem(PortletRequest request){
		NewsItem selectedItem = (NewsItem) getInterPortletMessageReceiver().receive(request);
		if(selectedItem == null){
			selectedItem = (NewsItem)request.getPortletSession().getAttribute(getSelectedItemAttribute());
		}
		return selectedItem;
	}

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public String getSelectedItemAttribute() {
		return selectedItemAttribute;
	}

	public void setSelectedItemAttribute(String selectedItemAttribute) {
		this.selectedItemAttribute = selectedItemAttribute;
	}

	public NewsItemDao getNewsItemDao() {
		return newsItemDao;
	}

	public void setNewsItemDao(NewsItemDao newsItemDao) {
		this.newsItemDao = newsItemDao;
	}

	public int getNewsItemsLimit() {
		return newsItemsLimit;
	}

	public void setNewsItemsLimit(int newsItemsLimit) {
		this.newsItemsLimit = newsItemsLimit;
	}

	public InterPortletMessageReceiver getInterPortletMessageReceiver() {
		return interPortletMessageReceiver;
	}

	public void setInterPortletMessageReceiver(
			InterPortletMessageReceiver interPortletMessageReceiver) {
		this.interPortletMessageReceiver = interPortletMessageReceiver;
	}

}
