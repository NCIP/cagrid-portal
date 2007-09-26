/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.map;

import java.util.HashSet;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.PortletRequestUtils;
import org.springframework.web.portlet.mvc.Controller;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class MapViewDeleteUrlController implements Controller {

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.Controller#handleActionRequest(javax.portlet.ActionRequest, javax.portlet.ActionResponse)
	 */
	public void handleActionRequest(ActionRequest request, ActionResponse response)
			throws Exception {
		String urlToDelete = PortletRequestUtils.getStringParameter(request, "urlToDelete");
		if(urlToDelete != null){
			PortletPreferences prefs = request.getPreferences();
			String[] urls = prefs.getValues(MapViewEditController.GEO_RSS_URL_PREF, null);
			if(urls == null || urls.length == 0){
				//This shouldn't happen
				throw new IllegalStateException("No existing URLs, so cannot delete one.");
			}
			Set<String> urlsSet = new HashSet<String>();
			for(String url : urls){
				urlsSet.add(url);
			}
			urlsSet.remove(urlToDelete);
			String[] newUrls = urlsSet.toArray(new String[urlsSet.size()]);
			prefs.setValues(MapViewEditController.GEO_RSS_URL_PREF, newUrls);
			prefs.store();
		}
		response.setRenderParameter("action", "");
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.Controller#handleRenderRequest(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	public ModelAndView handleRenderRequest(RenderRequest arg0,
			RenderResponse arg1) throws Exception {
		// This shouldn't be called
		if(true) throw new RuntimeException("MapViewDeleteUrlController.handleRenderRequest should not be called.");
		return null;
	}

}
