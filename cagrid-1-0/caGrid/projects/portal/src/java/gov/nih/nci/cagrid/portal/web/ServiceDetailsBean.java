package gov.nih.nci.cagrid.portal.web;

import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.manager.GridServiceManager;

import javax.faces.context.FacesContext;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 18, 2006
 * Time: 4:33:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceDetailsBean {
    private RegisteredService service;
    private GridServiceManager gridServiceManager;

    public String navigateTo() {
        Integer pk = new Integer((String) FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("navigatedServicePk"));

        service = (RegisteredService) gridServiceManager.getObjectByPrimaryKey(RegisteredService.class, pk);
        return "success";
    }

    public RegisteredService getService() {
        return service;
    }

    public void setService(RegisteredService service) {
        this.service = service;
    }

    public GridServiceManager getGridServiceManager() {
        return gridServiceManager;
    }

    public void setGridServiceManager(GridServiceManager gridServiceManager) {
        this.gridServiceManager = gridServiceManager;
    }
}
