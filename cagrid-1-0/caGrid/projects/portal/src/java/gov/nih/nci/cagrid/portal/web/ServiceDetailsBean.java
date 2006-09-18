package gov.nih.nci.cagrid.portal.web;

import gov.nih.nci.cagrid.portal.domain.RegisteredService;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 18, 2006
 * Time: 4:33:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceDetailsBean {
    private RegisteredService navigatedService;

    public RegisteredService getNavigatedService() {
        return navigatedService;
    }

    public void setNavigatedService(RegisteredService navigatedService) {
        this.navigatedService = navigatedService;
    }
}
