/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryInstance;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewCQLQueryInstanceHistoryController extends AbstractController {

	private String viewName;
	private CQLQueryService cqlQueryService;
	
	/**
	 * 
	 */
	public ViewCQLQueryInstanceHistoryController() {

	}
	
	public void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		logger.error("Got action request. Going to throw IllegalArgumentException.");
		throw new IllegalArgumentException("this method shouldn't be called");
	}
	
	
	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getViewName());
		
		List<CQLQueryInstance> instances = getCqlQueryService().getSubmittedCqlQueries();
		logger.debug("Got " + instances.size() + " instances.");
		mav.addObject("instances", instances);

		return mav;
	}


	public String getViewName() {
		return viewName;
	}


	public void setViewName(String viewName) {
		this.viewName = viewName;
	}


	public CQLQueryService getCqlQueryService() {
		return cqlQueryService;
	}

	public void setCqlQueryService(CQLQueryService cqlQueryService) {
		this.cqlQueryService = cqlQueryService;
	}
}
