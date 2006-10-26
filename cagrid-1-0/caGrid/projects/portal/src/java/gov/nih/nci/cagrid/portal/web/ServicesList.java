package gov.nih.nci.cagrid.portal.web;

import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.manager.GridServiceManager;
import gov.nih.nci.cagrid.portal.utils.EPRPingService;
import org.apache.log4j.Category;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import java.rmi.RemoteException;
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
    private GridServiceManager gridServiceManager;
    private EPRPingService pingService;
    private int listSize;

    private boolean navigatedServiceActive;

    private Category _logger = Category.getInstance(getClass().getName());

    public String navigateToService() throws FacesException {
        try {
            Integer pk = new Integer((String) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestParameterMap().get("navigatedServicePk"));

            navigatedService = (RegisteredService) gridServiceManager.getObjectByPrimaryKey(RegisteredService.class, pk);
        } catch (NumberFormatException e) {
            _logger.error(e);
            throw new FacesException(e);
        } catch (PortalRuntimeException e) {
            _logger.error(e);
            throw new FacesException(e);
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


    public boolean isNavigatedServiceActive() {
        try {
            navigatedServiceActive = pingService.ping(navigatedService.getHandle());
        } catch (RemoteException e) {
            return false;
        }
        return navigatedServiceActive;
    }

    public int getListSize() {
        return list.size();
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

    public void setGridServiceManager(GridServiceManager gridServiceManager) {
        this.gridServiceManager = gridServiceManager;
    }

    public void setNavigatedService(RegisteredService navigatedService) {
        this.navigatedService = navigatedService;
    }


    public void setPingService(EPRPingService pingService) {
        this.pingService = pingService;
    }
}
