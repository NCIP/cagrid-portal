/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.builder;

import gov.nih.nci.cagrid.portal2.dao.UMLClassDao;
import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal2.portlet.query.AbstractQueryRenderController;
import gov.nih.nci.cagrid.portal2.portlet.query.cql.UMLClassBean;
import gov.nih.nci.cagrid.portal2.portlet.query.cql.UMLClassTreeNodeListener;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeFacade;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeNode;

import java.util.HashMap;

import javax.portlet.RenderRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewUmlClassTreeController extends
		AbstractQueryRenderController {
	
	private TreeFacade umlClassTreeFacade;
	private UMLClassTreeNodeListener umlClassTreeNodeListener;

	private UMLClassDao umlClassDao;
	
	/**
	 * 
	 */
	public ViewUmlClassTreeController() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.portlet.AbstractViewObjectController#getObject(javax.portlet.RenderRequest)
	 */
	@Override
	protected Object getObject(RenderRequest request) {
		TreeNode rootNode = null;
		UMLClass umlClass = getQueryModel().getSelectedUmlClass();
		if(umlClass == null){
			logger.debug("no UMLClass selected");
		}else{
			logger.debug("UMLClass:" + umlClass.getId() + " selected");
			
			//Need to associate with current session
			umlClass = getUmlClassDao().getById(umlClass.getId());
			rootNode = getUmlClassTreeFacade().getRootNode();
			if(rootNode == null || !umlClass.getId().equals(((UMLClassBean)rootNode.getContent()).getUmlClass().getId())){
				logger.debug("Creating new tree for UMLClass:" + umlClass.getId());
				rootNode = createNode(umlClass);
			}
			getUmlClassTreeFacade().setRootNode(rootNode);
		}
		return rootNode;
	}
	
	private TreeNode createNode(UMLClass umlClass) {
		
		TreeNode node = new TreeNode(null, "UMLClass:" + umlClass.getId());
		node.setLabel(umlClass.getClassName());
		node.setContent(new UMLClassBean(umlClass));
		getUmlClassTreeNodeListener().onOpen(node, new HashMap());
		return node;
	}

	public UMLClassDao getUmlClassDao() {
		return umlClassDao;
	}

	public void setUmlClassDao(UMLClassDao umlClassDao) {
		this.umlClassDao = umlClassDao;
	}

	public TreeFacade getUmlClassTreeFacade() {
		return umlClassTreeFacade;
	}

	public void setUmlClassTreeFacade(TreeFacade umlClassTreeFacade) {
		this.umlClassTreeFacade = umlClassTreeFacade;
	}

	public UMLClassTreeNodeListener getUmlClassTreeNodeListener() {
		return umlClassTreeNodeListener;
	}

	public void setUmlClassTreeNodeListener(
			UMLClassTreeNodeListener umlClassTreeNodeListener) {
		this.umlClassTreeNodeListener = umlClassTreeNodeListener;
	}

}
