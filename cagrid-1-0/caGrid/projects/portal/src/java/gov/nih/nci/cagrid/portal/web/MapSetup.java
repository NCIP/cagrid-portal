package gov.nih.nci.cagrid.portal.web;

import gov.nih.nci.cagrid.portal.common.GeoCodeValues;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.manager.GridServiceManager;
import gov.nih.nci.cagrid.portal.manager.ResearchCenterManager;
import gov.nih.nci.cagrid.portal.map.InvalidMapNodeException;
import gov.nih.nci.cagrid.portal.map.MapNode;
import org.apache.log4j.Category;

import javax.faces.FacesException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    private List nodes = new ArrayList();

    private Category _logger = Category.getInstance(getClass().getName());

    public String navigateToServicesMap() throws FacesException {
        _logger.debug("Setting up map of services");

        try {
            List services = gridServiceManager.getAllServices();
            //clear nodes
            nodes.clear();

            for (Iterator iter = services.iterator(); iter.hasNext();) {
                RegisteredService service = (RegisteredService) iter.next();

                if (service.getResearchCenter() != null) {
                    GeoCodeValues geoVal = new GeoCodeValues(service.getResearchCenter().getLatitude(), service.getResearchCenter().getLongitude());

                    //form a MapNode object and add it to the list
                    try {
                        List displayText = new ArrayList();
                        displayText.add(service.getResearchCenter().getShortName());
                        nodes.add(new MapNode(service.getName(), geoVal, displayText));
                    } catch (InvalidMapNodeException e) {
                        //is expected. Continue
                    }
                }
            }

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

            List rcCenters = rcManager.getUniqueCenters();

            //clear nodes
            nodes.clear();

        } catch (PortalRuntimeException e) {
            //log so admin knows
            _logger.error(e);
            //send this to the view for the user
            throw new FacesException(e);
        }
        _logger.debug("Navigating to centers map");
        return "success";
    }

    public void setNodes(List nodes) {
        this.nodes = nodes;
    }

    public List getNodes() {
        return nodes;
    }

    public void setGridServiceManager(GridServiceManager gridServiceManager) {
        this.gridServiceManager = gridServiceManager;
    }

    public void setRcManager(ResearchCenterManager rcManager) {
        this.rcManager = rcManager;
    }
}
