/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.builder;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.RenderRequest;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.portlet.ModelAndView;

import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryRenderController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewCriterionController extends AbstractQueryRenderController {
	
	private List<String> predicates = new ArrayList<String>();

	/**
	 * 
	 */
	public ViewCriterionController() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController#getObject(javax.portlet.RenderRequest)
	 */
	@Override
	protected Object getObject(RenderRequest request) {
		return getUserModel().getSelectedCriterion();
	}
	
	protected void addData(RenderRequest request, ModelAndView mav){
		mav.addObject("path", getUserModel().getSelectedCriterion().getPath());
		mav.addObject("predicates", getPredicates());
	}

	@Required
	public List<String> getPredicates() {
		return predicates;
	}

	public void setPredicates(List<String> predicates) {
		this.predicates = predicates;
	}

}
