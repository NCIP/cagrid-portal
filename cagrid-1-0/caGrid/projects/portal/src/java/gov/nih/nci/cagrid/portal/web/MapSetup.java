package gov.nih.nci.cagrid.portal.web;

import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.manager.GridServiceManager;
import gov.nih.nci.cagrid.portal.manager.ResearchCenterManager;
import org.apache.log4j.Category;

import javax.faces.FacesException;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 3, 2006
 * Time: 4:32:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapSetup {

    private GridServiceManager gridServiceManager;
    private ResearchCenterManager rcManager;

    private Category _logger = Category.getInstance(getClass().getName());

    public String navigateToServicesMap() throws FacesException {
        _logger.debug("Setting up map of services");

        try {
            ServicesList services = (ServicesList) PortalWebUtils.getBean("services");
            services.setList(gridServiceManager.getAllServices());
        } catch (PortalRuntimeException e) {
            //log so admin knows
            _logger.error(e);
            //send this to the view for the user
            throw new FacesException(e);
        }
        _logger.debug("Navigating to services map");
        return "success";
    }

    public String navigateToRCMap() throws FacesException {
        _logger.debug("Setting up Map of centers");

        try {
            CenterList centers = (CenterList) PortalWebUtils.getBean("centers");
            centers.setList(rcManager.getUniqueCenters());
        } catch (PortalRuntimeException e) {
            //log so admin knows
            _logger.error(e);
            //send this to the view for the user
            throw new FacesException(e);
        }
        _logger.debug("Navigating to centers map");
        return "success";
    }

    public void setGridServiceManager(GridServiceManager gridServiceManager) {
        this.gridServiceManager = gridServiceManager;
    }

    public void setRcManager(ResearchCenterManager rcManager) {
        this.rcManager = rcManager;
    }
}
