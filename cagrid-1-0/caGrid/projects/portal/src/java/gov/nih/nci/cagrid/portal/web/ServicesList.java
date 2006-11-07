package gov.nih.nci.cagrid.portal.web;

import gov.nih.nci.cagrid.portal.domain.PointOfContact;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.manager.GridServiceManager;
import gov.nih.nci.cagrid.portal.utils.EPRPingService;
import org.apache.log4j.Category;

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
    private int count;

    private RegisteredService navigatedService;
    private GridServiceManager gridServiceManager;

    private int navigatedServiceStatus;

    private Category _logger = Category.getInstance(getClass().getName());

    public String navigateToService() throws FacesException {
        try {
            int index = Integer.parseInt((String) FacesContext.getCurrentInstance().
                    getExternalContext().getRequestParameterMap().get("navigatedServiceIndex"));
            navigatedService = (RegisteredService) list.get(index);

            setNavigatedServiceStatus(EPRPingService.ping(navigatedService.getHandle()));

        } catch (NumberFormatException e) {
            _logger.error(e);
            throw new FacesException(e);
        } catch (PortalRuntimeException e) {
            _logger.error(e);
            throw new FacesException(e);
        }

        return "success";
    }

    public String navigateToPOC() throws FacesException {
        try {
            int index = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestParameterMap().get("navigatedPOCIndex"));
            PointOfContact poc = (PointOfContact) navigatedService.getResearchCenter().getPocCollection().toArray()[index];

            PeopleList people = (PeopleList) PortalWebUtils.getBean("people");
            people.setNavigatedPOC(poc);


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


    public int getCount() {
        return gridServiceManager.getCount(RegisteredService.class);
    }

    public int getNavigatedServiceStatus() {
        return navigatedServiceStatus;
    }

    public void setNavigatedServiceStatus(int navigatedServiceStatus) {
        this.navigatedServiceStatus = navigatedServiceStatus;
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


}
