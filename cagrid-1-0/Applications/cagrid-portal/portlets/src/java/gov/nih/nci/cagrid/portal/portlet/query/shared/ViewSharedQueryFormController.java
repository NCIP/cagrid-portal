/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.shared;

import javax.portlet.RenderRequest;

import org.springframework.web.portlet.ModelAndView;

import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryRenderController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewSharedQueryFormController extends
		AbstractQueryRenderController {

	/**
	 * 
	 */
	public ViewSharedQueryFormController() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController#getObject(javax.portlet.RenderRequest)
	 */
	@Override
	protected Object getObject(RenderRequest request) {
		return getQueryModel().getWorkingSharedQuery();
	}
	
	protected void addData(RenderRequest request, ModelAndView mav){
		mav.addObject("confirmMessage", request.getParameter("confirmMessage"));
	}

}
