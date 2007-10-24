/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import java.util.HashMap;

import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal2.portlet.SharedApplicationModel;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeFacade;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeNode;

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
public class ViewCQLQueryController extends AbstractController {

	private static final Log logger = LogFactory
			.getLog(ViewCQLQueryController.class);
	private String viewName;
	private TreeFacade cqlQueryTreeFacade;
	private CQLQueryTreeNodeListener cqlQueryTreeNodeListener;
	private SharedApplicationModel sharedApplicationModel;

	/**
	 * 
	 */
	public ViewCQLQueryController() {

	}

	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getViewName());

		UMLClass umlClass = getSharedApplicationModel().getSelectedUMLClass();
		if (umlClass == null) {
			logger.debug("no UMLClass selected");
		} else {
			logger.debug("UMLClass:" + umlClass.getId() + " selected");
			
			TreeNode rootNode = getCqlQueryTreeFacade().getRootNode();
			if(rootNode == null || !umlClass.getId().equals(((CQLQueryBean)rootNode.getContent()).getUmlClass().getId())){
				logger.debug("Creating new tree for UMLClass: " + umlClass.getId());
				rootNode = createNode(umlClass);
			}
			getCqlQueryTreeFacade().setRootNode(rootNode);
			mav.addObject("rootNode", rootNode);
		}

		return mav;
	}

	private TreeNode createNode(UMLClass umlClass) {
		TreeNode rootNode = new TreeNode(null, "UMLClass:" + umlClass.getId());
		rootNode.setLabel(umlClass.getClassName());
		CQLQueryBean bean = (CQLQueryBean) getApplicationContext().getBean("cqlQueryBeanPrototype");
		bean.setUmlClass(umlClass);
		rootNode.setContent(bean);
		getCqlQueryTreeNodeListener().onOpen(rootNode, new HashMap());
		return rootNode;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public TreeFacade getCqlQueryTreeFacade() {
		return cqlQueryTreeFacade;
	}

	public void setCqlQueryTreeFacade(TreeFacade cqlQueryTreeFacade) {
		this.cqlQueryTreeFacade = cqlQueryTreeFacade;
	}

	public CQLQueryTreeNodeListener getCqlQueryTreeNodeListener() {
		return cqlQueryTreeNodeListener;
	}

	public void setCqlQueryTreeNodeListener(
			CQLQueryTreeNodeListener cqlQueryTreeNodeListener) {
		this.cqlQueryTreeNodeListener = cqlQueryTreeNodeListener;
	}

	public SharedApplicationModel getSharedApplicationModel() {
		return sharedApplicationModel;
	}

	public void setSharedApplicationModel(
			SharedApplicationModel sharedApplicationModel) {
		this.sharedApplicationModel = sharedApplicationModel;
	}

}
