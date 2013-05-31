/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.news;

import gov.nih.nci.cagrid.portal.dao.news.NewsChannelDao;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ListChannelsController extends AbstractController {

	private String successView;
	private NewsChannelDao newsChannelDao;
	
	/**
	 * 
	 */
	public ListChannelsController() {

	}
	
	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
            RenderResponse response)
     throws Exception{
		ModelAndView mav = new ModelAndView(getSuccessView());
		mav.addObject("channels", getNewsChannelDao().getAll());
		return mav;
	}

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public NewsChannelDao getNewsChannelDao() {
		return newsChannelDao;
	}

	public void setNewsChannelDao(NewsChannelDao newsChannelDao) {
		this.newsChannelDao = newsChannelDao;
	}

}
