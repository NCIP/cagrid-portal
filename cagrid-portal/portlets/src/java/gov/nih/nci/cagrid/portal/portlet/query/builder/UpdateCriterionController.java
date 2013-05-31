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
package gov.nih.nci.cagrid.portal.portlet.query.builder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;

import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryBean;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryTreeNodeListener;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CriteriaBean;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CriterionBean;
import gov.nih.nci.cagrid.portal.portlet.tree.NodeState;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeFacade;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeNode;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import gov.nih.nci.cagrid.portal.portlet.util.XSSFilterEditor;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class UpdateCriterionController extends
		AbstractQueryActionController {
	
	private CQLQueryTreeNodeListener cqlQueryTreeNodeListener;
	private TreeFacade cqlQueryTreeFacade;

	/**
	 * 
	 */
	public UpdateCriterionController() {

	}

	/**
	 * @param commandClass
	 */
	public UpdateCriterionController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public UpdateCriterionController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}
	
	protected Object getCommand(PortletRequest request)
    	throws Exception{
		return getUserModel().getSelectedCriterion();
	}
	
	protected void initBinder(PortletRequest request,
			PortletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(String.class, "value",
				new XSSFilterEditor(binder.getBindingResult(), "value"));
	}
    

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void doHandleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {
		
		String editOp = request.getParameter("editOperation");
		if(editOp == null){
			throw new Exception("No editOperation specified");
		}
		if("cancel".equals(editOp)){
			return;
		}
		
		
		String path = request.getParameter("path");
		if(path == null){
			throw new Exception("No path specified");
		}
		
		
		//Get the attribute path (without the root node);
		String attPath = path.substring(path.indexOf("/", path.indexOf(":") + 1) + 1);
		logger.debug("attPath = " + attPath);
		
		//Insert or delete CriterionBean.
		CriterionBean criterion = (CriterionBean)obj;
		
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
	
	@Required
	public TreeFacade getCqlQueryTreeFacade() {
		return cqlQueryTreeFacade;
	}

	public void setCqlQueryTreeFacade(TreeFacade cqlQueryTreeFacade) {
		this.cqlQueryTreeFacade = cqlQueryTreeFacade;
	}
	
	@Required
	public CQLQueryTreeNodeListener getCqlQueryTreeNodeListener() {
		return cqlQueryTreeNodeListener;
	}

	public void setCqlQueryTreeNodeListener(
			CQLQueryTreeNodeListener cqlQueryTreeNodeListener) {
		this.cqlQueryTreeNodeListener = cqlQueryTreeNodeListener;
	}

}
