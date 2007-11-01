/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.details;

import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.portlet.AbstractViewObjectController;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeFacade;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeNode;

import java.util.HashMap;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.portlet.ModelAndView;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewServiceDetailsController extends AbstractDiscoveryViewObjectController {
	
	
	private TreeFacade serviceDetailsTreeFacade;
	
	private ServiceMetadataTreeNodeListener serviceMetadataTreeNodeListener;
	
	private GridServiceDao gridServiceDao;

	/**
	 * 
	 */
	public ViewServiceDetailsController() {

	}
	
	protected Object getObject(RenderRequest request){

		TreeNode rootNode = null;
		GridService gridService = getDiscoveryModel().getSelectedService();
		if (gridService != null) {
			//Associate with current session
			gridService = getGridServiceDao().getById(gridService.getId());
			rootNode = getServiceDetailsTreeFacade().getRootNode();
			if(rootNode == null || !((GridService)rootNode.getContent()).getId().equals(gridService.getId())){
				logger.debug("Creating new tree for gridService:" + gridService.getId());
				rootNode = createRootNode(gridService);
			}
			getServiceDetailsTreeFacade().setRootNode(rootNode);
		} else {
			logger.debug("No grid service selected.");
		}
		return rootNode;
	}
	
	private TreeNode createRootNode(GridService gridService) {
		TreeNode rootNode = new TreeNode(null, "gridService");
		rootNode.setContent(gridService);
		getServiceMetadataTreeNodeListener().handleGridServiceNode(rootNode, new HashMap(), gridService);
		return rootNode;
	}

	@Required
	public TreeFacade getServiceDetailsTreeFacade() {
		return serviceDetailsTreeFacade;
	}

	public void setServiceDetailsTreeFacade(TreeFacade treeFacade) {
		this.serviceDetailsTreeFacade = treeFacade;
	}

	@Required
	public ServiceMetadataTreeNodeListener getServiceMetadataTreeNodeListener() {
		return serviceMetadataTreeNodeListener;
	}

	public void setServiceMetadataTreeNodeListener(
			ServiceMetadataTreeNodeListener serviceMetadataTreeNodeListener) {
		this.serviceMetadataTreeNodeListener = serviceMetadataTreeNodeListener;
	}
	


	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

}
