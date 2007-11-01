/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class AbstractViewObjectController extends AbstractController {
	
	
	private String successViewName;
	private String objectName;

	/**
	 * 
	 */
	public AbstractViewObjectController() {

	}
	
	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {

		ModelAndView mav = new ModelAndView(getSuccessViewName());
		Object obj = getObject(request);
		mav.addObject(getObjectName(), obj);
		addData(request, mav);
		return mav;
	}
	
	protected abstract Object getObject(RenderRequest request);
	
	protected void addData(RenderRequest request, ModelAndView mav){
		
	}
	
	@Required
	public String getSuccessViewName() {
		return successViewName;
	}

	public void setSuccessViewName(String successView) {
		this.successViewName = successView;
	}
	

	@Required
	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String commandName) {
		this.objectName = commandName;
	}

}
