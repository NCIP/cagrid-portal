/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.content;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ContentViewController extends AbstractContentViewController {

	public void handleActionRequest(ActionRequest arg0, ActionResponse arg1) throws Exception {
		// nothing to do
	}

	public ModelAndView handleRenderRequest(RenderRequest request, RenderResponse response) throws Exception {
		return new ModelAndView(request.getPreferences().getValue(getViewNameParam(), getViewNameDefault()));
	}

}
