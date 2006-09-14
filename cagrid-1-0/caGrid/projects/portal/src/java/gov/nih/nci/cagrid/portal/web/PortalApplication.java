package gov.nih.nci.cagrid.portal.web;

import gov.nih.nci.cagrid.portal.manager.GridServiceManager;
import org.apache.log4j.Category;
import org.springframework.dao.DataAccessException;

import javax.faces.FacesException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 12, 2006
 * Time: 4:11:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class PortalApplication {

    //list of all Registered Services
    private List services = new ArrayList();
    private GridServiceManager gridServiceManager;

    private Category _logger = Category.getInstance(getClass().getName());


    public List getServices() throws FacesException {
        try {
            services = gridServiceManager.getAllServices();
        } catch (DataAccessException e) {
            String msg = "Error retreiving services from Database";
            _logger.error("msg" + e.getMessage());
            throw new FacesException(msg, e);
        }
        return services;
    }

    public void setServices(List services) {
        this.services = services;
    }

    public GridServiceManager getGridServiceManager() {
        return gridServiceManager;
    }

    public void setGridServiceManager(GridServiceManager gridServiceManager) {
        this.gridServiceManager = gridServiceManager;
    }
}
