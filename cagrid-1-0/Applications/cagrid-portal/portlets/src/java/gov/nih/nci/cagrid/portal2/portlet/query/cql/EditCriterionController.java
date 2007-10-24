/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import gov.nih.nci.cagrid.portal2.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal2.portlet.tree.NodeState;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeFacade;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeNode;
import gov.nih.nci.cagrid.portal2.portlet.util.PortletUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
public class EditCriterionController extends SimpleFormController {

	private static final Log logger = LogFactory
			.getLog(EditCriterionController.class);
	private TreeFacade umlClassTreeFacade;
	private TreeFacade cqlQueryTreeFacade;
	private String successOperation;
	private List<String> predicates = new ArrayList<String>();
	private CQLQueryTreeNodeListener cqlQueryTreeNodeListener;

	/**
	 * 
	 */
	public EditCriterionController() {

	}

	protected Object formBackingObject(PortletRequest request) throws Exception {

		CriterionBean bean = null;

		// Make sure path specified
		String path = request.getParameter("path");
		if (path == null) {
			throw new Exception("No path specified");
		}
		
		logger.debug("path = " + path);

		int sepIdx = path.lastIndexOf("/");
		String umlClassPath = path.substring(0, sepIdx);
		String umlAttName = path.substring(sepIdx + 1);

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
			logger.debug("Found existing node for path " + path);
			//Check if criterion has been specified
			CriteriaBean criteria = (CriteriaBean)node.getContent();
			for(CriterionBean crit : criteria.getCriteria()){
				if(umlAttName.equals(crit.getUmlAttribute().getName())){
					logger.debug("Found existing criterion for " + umlAttName);
					bean = crit;
					break;
				}
			}
		}
		
		if(bean == null){
			logger.debug("No existing criterion bean found for " + path);

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
			logger.debug("selectedAtt = " + selectedAtt.getName() + ":" + selectedAtt.getId());
			bean = new CriterionBean();
			bean.setUmlAttribute(selectedAtt);
		}

		return bean;
	}

	protected Map referenceData(PortletRequest request, Object command,
			Errors errors) throws Exception {
		Map data = new HashMap();
		data.put("path", request.getParameter("path"));
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
		String editOp = request.getParameter("editOperation");
		if(editOp == null){
			throw new Exception("No editOperation specified");
		}
		
		//Get the attribute path (without the root node);
		String attPath = path.substring(path.indexOf("/", path.indexOf(":") + 1) + 1);
		logger.debug("attPath = " + attPath);
		
		//Insert or delete CriterionBean.
		CriterionBean criterion = (CriterionBean)command;
		
		TreeNode rootNode = getCqlQueryTreeFacade().getRootNode();
		CQLQueryBean query = (CQLQueryBean)rootNode.getContent();
		
		
		if("delete".equals(editOp)){
			query.delete(attPath);
			//Delete empty TreeNodes
			deleteEmptyTreeNodes(rootNode, attPath);
		}else{
			query.insert(attPath, criterion);
			
			//Open all nodes to the node that was added
			TreeNode parentNode = rootNode;
			Map params = new HashMap();
			String[] parts = PortletUtils.parsePath(attPath);
			while(parts.length > 1){
				
				getCqlQueryTreeNodeListener().onOpen(parentNode, params);
				for(Iterator i = parentNode.getChildren().iterator(); i.hasNext();){
					TreeNode child = (TreeNode)i.next();
					if(child.getName().equals(parts[0])){
						child.setState(NodeState.OPEN);
						parentNode = child;
						break;
					}
				}
				parts = PortletUtils.parsePath(parts[1]);
			}
		}
		
		
		
		response.setRenderParameter("operation", getSuccessOperation());
	}

	private void deleteEmptyTreeNodes(TreeNode parentNode, String path) {
		String[] parts = PortletUtils.parsePath(path);
		for(Iterator i = parentNode.getChildren().iterator(); i.hasNext();){
			TreeNode childNode = (TreeNode)i.next();
			if(parts[0].equals(childNode.getName())){
				deleteEmptyTreeNodes(childNode, parts[1]);
				CriteriaBean assocBean = (CriteriaBean)childNode.getContent();
				if(assocBean.isEmpty()){
					i.remove();
				}
			}
		}
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

	public static Log getLogger() {
		return logger;
	}

	public CQLQueryTreeNodeListener getCqlQueryTreeNodeListener() {
		return cqlQueryTreeNodeListener;
	}

	public void setCqlQueryTreeNodeListener(
			CQLQueryTreeNodeListener cqlQueryTreeNodeListener) {
		this.cqlQueryTreeNodeListener = cqlQueryTreeNodeListener;
	}

}
