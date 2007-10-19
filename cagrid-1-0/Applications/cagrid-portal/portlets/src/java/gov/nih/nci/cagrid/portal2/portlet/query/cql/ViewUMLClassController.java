/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal2.portlet.SharedApplicationModel;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeFacade;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeNode;

import java.util.HashMap;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewUMLClassController extends AbstractController {
	
	private static final Log logger = LogFactory.getLog(ViewUMLClassController.class);
	private String successView;
	private TreeFacade umlClassTreeFacade;
	private UMLClassTreeNodeListener umlClassTreeNodeListener;
	private SharedApplicationModel sharedApplicationModel;
	private HibernateTemplate hibernateTemplate;

	/**
	 * 
	 */
	public ViewUMLClassController() {
	
	}
	
	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {

		logger.debug("Handling render request");
		ModelAndView mav = new ModelAndView(getSuccessView());
		
		UMLClass umlClass = getSharedApplicationModel().getSelectedUMLClass();
		if(umlClass == null){
			logger.debug("no UMLClass selected");
		}else{
			logger.debug("UMLClass:" + umlClass.getId() + " selected");
			
			//Need to associate with current session
			umlClass = (UMLClass) getHibernateTemplate().get(umlClass.getClass(), umlClass.getId());
			if(umlClass == null){
				throw new Exception("Couldn't retrieve UMLClass from database.");
			}
			
			TreeNode rootNode = getUmlClassTreeFacade().getRootNode();
			if(rootNode == null || !umlClass.getId().equals(((UMLClass)rootNode.getContent()).getId())){
				logger.debug("Creating new tree for UMLClass:" + umlClass.getId());
				rootNode = createNode(umlClass);
			}
			getUmlClassTreeFacade().setRootNode(rootNode);
			mav.addObject("rootNode", rootNode);
		}
		return mav;
	}
	

	private TreeNode createNode(UMLClass umlClass) {
		
		TreeNode node = new TreeNode(null, "UMLClass:" + umlClass.getId());
		node.setLabel(umlClass.getClassName());
		node.setContent(new UMLClassBean(umlClass));
		getUmlClassTreeNodeListener().onOpen(node, new HashMap());
		return node;
	}

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
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

	public SharedApplicationModel getSharedApplicationModel() {
		return sharedApplicationModel;
	}

	public void setSharedApplicationModel(
			SharedApplicationModel sharedApplicationModel) {
		this.sharedApplicationModel = sharedApplicationModel;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
