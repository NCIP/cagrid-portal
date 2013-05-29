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
