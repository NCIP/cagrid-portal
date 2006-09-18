package gov.nih.nci.cagrid.portal.web;

import gov.nih.nci.cagrid.portal.domain.PointOfContact;
import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
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
public class PortalApplicationBean {

    //list of all Registered Services
    private List services = new ArrayList();
    //list of all RC's
    private List rc = new ArrayList();
    //list of all POC's
    private List poc = new ArrayList();

    private GridServiceManager gridServiceManager;

    private Category _logger = Category.getInstance(getClass().getName());


    public List getServices() throws FacesException {
        try {
            services = gridServiceManager.getAllServices();
            //sort resultset
            java.util.Collections.sort(services);
        } catch (Exception e) {
            String msg = "Error retreiving services from Database";
            _logger.error("msg" + e.getMessage());
            throw new FacesException(msg, e);
        }
        return services;
    }

    public void setServices(List services) {
        this.services = services;
    }

    public List getRc() throws FacesException {
        try {
            rc = gridServiceManager.loadAll(ResearchCenter.class);
        } catch (DataAccessException e) {
            String msg = "Error retreiving Research Center information from DB";
            throw new FacesException(msg, e);
        }
        return rc;
    }

    public void setRc(List rc) {
        this.rc = rc;
    }

    public List getPoc() throws FacesException {
        try {
            poc = gridServiceManager.loadAll(PointOfContact.class);
        } catch (DataAccessException e) {
            String msg = "Error retreiving POC from DB";
            throw new FacesException(msg, e);
        }
        return poc;
    }

    public void setPoc(List poc) {
        this.poc = poc;
    }

    public GridServiceManager getGridServiceManager() {
        return gridServiceManager;
    }

    public void setGridServiceManager(GridServiceManager gridServiceManager) {
        this.gridServiceManager = gridServiceManager;
    }
}
