/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery;

import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal2.portlet.SharedApplicationModel;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeFacade;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeNode;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectUMLClassController extends AbstractController {

	private String successAction;
	private TreeFacade treeFacade;
	private SharedApplicationModel sharedApplicationModel;
	
	/**
	 * 
	 */
	public SelectUMLClassController() {

	}
	
	public void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		
		String path = request.getParameter("selectedUMLClassPath");
		if(path == null){
			throw new Exception("selectedUMLClassPath parameter not set");
		}
		
		TreeNode node = getTreeFacade().getRootNode().find(path);
		if(node == null){
			throw new Exception("Couldn't find node for path '" + path + "'");
		}
		
		UMLClass klass = (UMLClass) node.getContent();
		if(klass == null){
			throw new Exception("node '" + path + "' does not contain a UMLClass object");
		}
		
		getSharedApplicationModel().setSelectedUMLClass(klass);
		
		response.setRenderParameter("operation", getSuccessAction());
	}
	

	public String getSuccessAction() {
		return successAction;
	}

	public void setSuccessAction(String successAction) {
		this.successAction = successAction;
	}

	public TreeFacade getTreeFacade() {
		return treeFacade;
	}

	public void setTreeFacade(TreeFacade treeFacade) {
		this.treeFacade = treeFacade;
	}

	public SharedApplicationModel getSharedApplicationModel() {
		return sharedApplicationModel;
	}

	public void setSharedApplicationModel(
			SharedApplicationModel sharedApplicationModel) {
		this.sharedApplicationModel = sharedApplicationModel;
	}
}
