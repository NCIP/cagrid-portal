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
package gov.nih.nci.cagrid.portal.portlet;


import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class AbstractViewObjectController extends AbstractController {
	
	
	private String successViewName;
	private String objectName;
	private String errorsAttributeName;

	/**
	 * 
	 */
	public AbstractViewObjectController() {

	}
	
	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {

		ModelAndView mav = null;
		BindException errors = null;
		if(getErrorsAttributeName() != null){
			errors = (BindException)request.getAttribute(getErrorsAttributeName());
		}
		if(errors != null){
			mav = new ModelAndView(getSuccessViewName(), errors.getModel());
		}else{
			mav = new ModelAndView(getSuccessViewName());
		}
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

	public String getErrorsAttributeName() {
		return errorsAttributeName;
	}

	public void setErrorsAttributeName(String errorsAttributeName) {
		this.errorsAttributeName = errorsAttributeName;
	}

}
