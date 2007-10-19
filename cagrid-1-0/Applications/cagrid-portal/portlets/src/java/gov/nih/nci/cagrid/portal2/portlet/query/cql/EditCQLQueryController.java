/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import gov.nih.nci.cagrid.portal2.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeFacade;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.SimpleFormController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class EditCQLQueryController extends SimpleFormController {

	private static final Log logger = LogFactory
			.getLog(EditCQLQueryController.class);
	private TreeFacade umlClassTreeFacade;
	private TreeFacade cqlQueryTreeFacade;
	private String successOperation;
	private List<String> predicates = new ArrayList<String>();

	/**
	 * 
	 */
	public EditCQLQueryController() {

	}

	protected Object formBackingObject(PortletRequest request) throws Exception {

		CriterionBean bean = new CriterionBean();

		// Make sure path specified
		String path = request.getParameter("path");
		if (path == null) {
			throw new Exception("No path specified");
		}

		int dotIdx = path.lastIndexOf(".");
		String umlClassPath = path.substring(0, dotIdx);
		String umlAttName = path.substring(dotIdx + 1);

		// First check the working query to see if we already have it
		TreeNode node = getCqlQueryTreeFacade().getRootNode()
				.find(umlClassPath);
		if (node != null) {
			if (!(node.getContent() instanceof CriteriaBean)) {
				throw new Exception("node content for "
						+ path
						+ " not instance of CriteriaBean. got "
						+ (node.getContent() == null ? null : node.getContent()
								.getClass().getName()));
			}
			
			//Check if criterion has been specified
			CriteriaBean criteria = (CriteriaBean)node.getContent();
			for(CriterionBean crit : criteria.getCriteria()){
				if(umlAttName.equals(crit.getUmlAttribute().getName())){
					bean = crit;
					break;
				}
			}
		}
		
		if(bean == null){
			logger.debug("No existing bean found for " + path);

			node = getUmlClassTreeFacade().getRootNode().find(umlClassPath);
			if (node == null) {
				throw new Exception("Couldn't find node for " + path);
			}
			UMLAttribute selectedAtt = null;
			UMLClassBean umlClassBean = (UMLClassBean) node.getContent();
			for(UMLAttribute att : umlClassBean.getAttributes()){
				if(umlAttName.equals(att.getName())){
					selectedAtt = att;
					break;
				}
			}
			if(selectedAtt == null){
				throw new Exception("No attribute found for " + path);
			}
			bean = new CriterionBean();
			bean.setUmlAttribute(selectedAtt);
		}

		return bean;
	}

	protected Map referenceData(PortletRequest request, Object command,
			Errors errors) throws Exception {
		Map data = new HashMap();
		data.put("path", request.getAttribute("path"));
		data.put("predicates", getPredicates());
		return data;
	}

	protected void onSubmitAction(ActionRequest request,
			ActionResponse response, Object command, BindException errors)
			throws Exception {
		String path = request.getParameter("path");
		if(path == null){
			throw new Exception("No path specified");
		}
		
		//Add CriterionBean to CQLQuery tree.
		
		response.setRenderParameter("operation", getSuccessOperation());
	}

	protected ModelAndView onSubmitRender(RenderRequest request,
			RenderResponse response, Object command, BindException errors)
			throws Exception {
		throw new IllegalArgumentException("This method should not be called.");
	}

	public TreeFacade getUmlClassTreeFacade() {
		return umlClassTreeFacade;
	}

	public void setUmlClassTreeFacade(TreeFacade umlClassTreeFacade) {
		this.umlClassTreeFacade = umlClassTreeFacade;
	}

	public TreeFacade getCqlQueryTreeFacade() {
		return cqlQueryTreeFacade;
	}

	public void setCqlQueryTreeFacade(TreeFacade cqlQueryTreeFacade) {
		this.cqlQueryTreeFacade = cqlQueryTreeFacade;
	}

	public String getSuccessOperation() {
		return successOperation;
	}

	public void setSuccessOperation(String successOperation) {
		this.successOperation = successOperation;
	}

	public List<String> getPredicates() {
		return predicates;
	}

	public void setPredicates(List<String> predicates) {
		this.predicates = predicates;
	}

}
