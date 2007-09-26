/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.PortletRequestUtils;
import org.springframework.web.portlet.mvc.Controller;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class MapViewEditController implements Controller {
	
	public static final String GEO_RSS_URL_PREF = "geoRSSUrls";
	public static final String APPID_PREF = "appid";
	private static final Log logger = LogFactory.getLog(MapViewEditController.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.portlet.mvc.Controller#handleActionRequest(javax.portlet.ActionRequest,
	 *      javax.portlet.ActionResponse)
	 */
	public void handleActionRequest(ActionRequest request,
			ActionResponse response) throws Exception {
		String[] urls = PortletRequestUtils.getStringParameters(request, GEO_RSS_URL_PREF);
		if (urls != null) {
			PortletPreferences preferences = request.getPreferences();
			preferences.setValues(GEO_RSS_URL_PREF, urls);
			preferences.store();
		}
		String appid = PortletRequestUtils.getStringParameter(request,
				APPID_PREF);
		if (appid != null) {
			PortletPreferences preferences = request.getPreferences();
			preferences.setValue(APPID_PREF, appid);
			preferences.store();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.portlet.mvc.Controller#handleRenderRequest(javax.portlet.RenderRequest,
	 *      javax.portlet.RenderResponse)
	 */
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {
		
		String[] urls = request.getPreferences().getValues(GEO_RSS_URL_PREF, null);
		if(urls == null){
			logger.debug("urls is null");
		}else{
			logger.debug("urls.length == " + urls.length);
		}
		
		ModelAndView mav = new ModelAndView("edit");
		mav
				.addObject(APPID_PREF, request.getPreferences().getValue(APPID_PREF,
						null));
		mav.addObject(GEO_RSS_URL_PREF, urls);
		return mav;
	}

}
