/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.details;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeFacade;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeNode;
import gov.nih.nci.cagrid.portal.util.filter.ServiceFilter;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.portlet.ModelAndView;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import java.util.HashMap;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
public class ViewServiceDetailsController extends AbstractDiscoveryViewObjectController {


    private TreeFacade serviceDetailsTreeFacade;

    private ServiceMetadataTreeNodeListener serviceMetadataTreeNodeListener;

    private GridServiceDao gridServiceDao;
    private ServiceFilter servicefilter;
    private String portalUserAttributeName;
    private String loginUrl;

    /**
     *
     */
    public ViewServiceDetailsController() {

    }

    protected Object getObject(RenderRequest request) {

        TreeNode rootNode = null;
        GridService gridService = getDiscoveryModel().getSelectedService();
        if (gridService != null) {
            //Associate with current session
            gridService = getGridServiceDao().getById(gridService.getId());
            if (servicefilter.willBeFiltered(gridService)) {
                logger.debug("Service should be filtered. Will not return Grid Service details");
                return null;
            }
            rootNode = getServiceDetailsTreeFacade().getRootNode();
            if (rootNode == null || !((GridService) rootNode.getContent()).getId().equals(gridService.getId())) {
                logger.debug("Creating new tree for gridService:" + gridService.getId());
                rootNode = createRootNode(gridService);
            }
            //this will refresh basic details like service
            // status which can change more frequently 
            rootNode.setContent(gridService);

            getServiceDetailsTreeFacade().setRootNode(rootNode);
        } else {
            logger.debug("No grid service selected.");
        }
        return rootNode;
    }

    @Override
    protected void addData(RenderRequest request, ModelAndView mav) {
        super.addData(request, mav);    //To change body of overridden methods use File | Settings | File Templates.
        PortalUser user = (PortalUser) request.getPortletSession()
                .getAttribute(getPortalUserAttributeName(),
                        PortletSession.APPLICATION_SCOPE);
        if (user != null) {
            mav.addObject("portalUser", user);
        } else {
            mav.addObject("loginUrl", request.getPreferences().getValue(
                    "loginUrl", getLoginUrl()));
        }
    }

    private TreeNode createRootNode(GridService gridService) {
        TreeNode rootNode = new TreeNode(null, "gridService");
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

    public ServiceFilter getServicefilter() {
        return servicefilter;
    }

    public void setServicefilter(ServiceFilter servicefilter) {
        this.servicefilter = servicefilter;
    }

    public String getPortalUserAttributeName() {
        return portalUserAttributeName;
    }

    public void setPortalUserAttributeName(String portalUserAttributeName) {
        this.portalUserAttributeName = portalUserAttributeName;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
}
