/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import gov.nih.nci.cagrid.portal2.portlet.PortletConstants;
import gov.nih.nci.cagrid.portal2.portlet.SharedApplicationModel;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SelectCQLQueryInstanceController extends AbstractController {

	private static final Log logger = LogFactory
			.getLog(SelectCQLQueryInstanceController.class);

	private String successOperation;
	private SharedApplicationModel sharedApplicationModel;

	/**
	 * 
	 */
	public SelectCQLQueryInstanceController() {

	}

	public void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		logger.debug("setting up cql query results view.");

		String selectedCqlQueryInstanceId = request
				.getParameter(PortletConstants.SELECTED_CQL_QUERY_INSTANCE_ID_PARAM);
		//TODO: validate ID
		getSharedApplicationModel().setSelectedCqlQueryInstanceId(
				Integer.valueOf(selectedCqlQueryInstanceId));

		response.setRenderParameter("operation", getSuccessOperation());
	}

	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		logger.error("Got render request. Going to throw IllegalArgumentException.");
		throw new IllegalArgumentException("this method shouldn't be called");
	}

	public String getSuccessOperation() {
		return successOperation;
	}

	public void setSuccessOperation(String successOperation) {
		this.successOperation = successOperation;
	}

	public SharedApplicationModel getSharedApplicationModel() {
		return sharedApplicationModel;
	}

	public void setSharedApplicationModel(
			SharedApplicationModel sharedApplicationModel) {
		this.sharedApplicationModel = sharedApplicationModel;
	}

}
