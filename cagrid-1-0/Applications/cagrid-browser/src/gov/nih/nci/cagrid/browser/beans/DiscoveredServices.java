package gov.nih.nci.cagrid.browser.beans;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//~--- classes ----------------------------------------------------------------

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Jun 15, 2005
 * Time: 11:04:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class DiscoveredServices {
    private GSH  navigatedService;
    private List servicesList;

    //~--- constructors -------------------------------------------------------

    public DiscoveredServices() {
        this.servicesList = new ArrayList();
    }

    //~--- methods ------------------------------------------------------------

    public void addAll(Collection c) {
        this.servicesList.addAll(c);
    }

    public void addGSH(GSH gshURL) {
        this.servicesList.add(gshURL);
    }

    public void removeAll() {
        this.servicesList.clear();
    }

    public void removeGSH(GSH gshURL) {
        this.servicesList.remove(gshURL);
    }

    //~--- get methods --------------------------------------------------------

    public GSH getNavigatedService() {
        return navigatedService;
    }

    public List getServicesList() {
        return servicesList;
    }

    //~--- set methods --------------------------------------------------------

    public void setNavigatedService(GSH navigatedService) {
        this.navigatedService = navigatedService;
        this.navigatedService.fillInMetadata();
    }

    public void setServicesList(List servicesList) {
        this.servicesList = servicesList;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
