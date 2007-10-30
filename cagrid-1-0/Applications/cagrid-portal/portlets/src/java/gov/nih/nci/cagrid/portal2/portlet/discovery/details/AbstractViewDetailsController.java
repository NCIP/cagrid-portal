/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.details;

import gov.nih.nci.cagrid.portal2.portlet.discovery.DiscoveryModel;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class AbstractViewDetailsController extends AbstractController {
	
	private DiscoveryModel discoveryModel;
	
	private String successViewName;

	/**
	 * 
	 */
	public AbstractViewDetailsController() {
		// TODO Auto-generated constructor stub
	}
	
	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {

		ModelAndView mav = new ModelAndView(getSuccessViewName());
		doHandle(request, response, mav);
		return mav;
	}
	
	protected abstract void doHandle(RenderRequest request, RenderResponse reponse, ModelAndView mav);
	
	@Required
	public String getSuccessViewName() {
		return successViewName;
	}

	public void setSuccessViewName(String successView) {
		this.successViewName = successView;
	}
	
	@Required
	public DiscoveryModel getDiscoveryModel() {
		return discoveryModel;
	}

	public void setDiscoveryModel(DiscoveryModel discoveryModel) {
		this.discoveryModel = discoveryModel;
	}

}
