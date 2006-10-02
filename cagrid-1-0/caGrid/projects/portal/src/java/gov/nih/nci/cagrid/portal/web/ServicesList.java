package gov.nih.nci.cagrid.portal.web;

import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.manager.GridServiceManager;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 28, 2006
 * Time: 2:28:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServicesList {

    private List list = new ArrayList();
    private RegisteredService navigatedService;
    private String navigatedType;
    private GridServiceManager gridServiceManager;
    private boolean showModel = false;


    public String navigateToService() {
        Integer pk = new Integer((String) FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("navigatedServicePk"));

        navigatedService = (RegisteredService) gridServiceManager.getObjectByPrimaryKey(RegisteredService.class, pk);

        if (navigatedService.getDomainModel() != null) {
            navigatedType = RegisteredService.DATA_SERVICE;
        } else {
            navigatedType = RegisteredService.ANALYTICAL_SERVICE;
        }

        return "success";
    }

    public void setupKeywordSearch(String keyword) throws FacesException {
        try {
            list = gridServiceManager.keywordSearch(keyword);
        } catch (PortalRuntimeException e) {
            new FacesException(e);
        }
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public RegisteredService getNavigatedService() {
        return navigatedService;
    }

    public String getNavigatedType() {
        return navigatedType;
    }

    public boolean isShowModel() {
        return showModel;
    }

    public void setShowModel(boolean showModel) {
        this.showModel = showModel;
    }

    public void setGridServiceManager(GridServiceManager gridServiceManager) {
        this.gridServiceManager = gridServiceManager;
    }

    public void setNavigatedType(String navigatedType) {
        this.navigatedType = navigatedType;
    }

    public void setNavigatedService(RegisteredService navigatedService) {
        this.navigatedService = navigatedService;
    }
}
