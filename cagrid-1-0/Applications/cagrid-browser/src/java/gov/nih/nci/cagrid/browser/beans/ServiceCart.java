package gov.nih.nci.cagrid.browser.beans;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

//~--- classes ----------------------------------------------------------------

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Jul 4, 2005
 * Time: 5:23:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceCart {
    List shoppedServices;

    //~--- constructors -------------------------------------------------------

    public ServiceCart() {
        shoppedServices = new ArrayList();
    }

    //~--- methods ------------------------------------------------------------

    public void addGSH(GSH gsh) {
        shoppedServices.add(gsh);
    }

    public void removeAll() {
        shoppedServices.clear();
    }

    public void removeGSH(GSH gsh) {
        shoppedServices.remove(gsh);
    }

    //~--- get methods --------------------------------------------------------

    public List getShoppedServices() {
        return shoppedServices;
    }

    //~--- set methods --------------------------------------------------------

    public void setShoppedServices(List shoppedServices) {
        this.shoppedServices = shoppedServices;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
