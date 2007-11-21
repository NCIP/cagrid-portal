/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.tree;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class TreeController extends AbstractController {

	private String viewName;
	private TreeNode rootNode;
	private TreeFacade treeFacade;
	
	/**
	 * 
	 */
	public TreeController() {

		rootNode = new TreeNode(null, "root");
		rootNode.setContent(new TextContent("Here's root content"));
		rootNode.setState(NodeState.OPEN);
		
		TreeNode child1 = new TreeNode(rootNode, "child1");
		child1.setContent(new TextContent("Here's child1 content."));
		rootNode.getChildren().add(child1);
		
		TreeNode child2 = new TreeNode(rootNode, "child2");
		child2.setContent(new TextContent("Here's child2 content."));
		rootNode.getChildren().add(child2);
		
		TreeNode child2a = new TreeNode(child2, "child2a");
		child2a.setContent(new TextContent("Here's child2a content."));
		child2.getChildren().add(child2a);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView(getViewName());
		getTreeFacade().setRootNode(rootNode);
		mav.addObject("rootNode", rootNode);
		
		return mav;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public TreeFacade getTreeFacade() {
		return treeFacade;
	}

	public void setTreeFacade(TreeFacade treeFacade) {
		this.treeFacade = treeFacade;
	}

}
