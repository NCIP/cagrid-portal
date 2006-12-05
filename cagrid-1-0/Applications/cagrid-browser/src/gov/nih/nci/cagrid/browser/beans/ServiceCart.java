package gov.nih.nci.cagrid.browser.beans;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//~--- classes ----------------------------------------------------------------

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Jul 4, 2005
 * Time: 5:23:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceCart {
    public static final String SERVICE_CART_BEAN = "serviceCart";
	public static final String SERVICE_CART_FAILED_NOT_FOUND = "serviceCartFailed.notFound";
	
	private String serviceCartFailureMessage;
	
	
	Set shoppedServices;

    //~--- constructors -------------------------------------------------------

    public ServiceCart() {
        shoppedServices = new HashSet();
    }

    //~--- methods ------------------------------------------------------------

    public void addService(CaGridService gsh) {
        shoppedServices.add(gsh);
    }

    public void removeAll() {
        shoppedServices.clear();
    }

    public void removeService(CaGridService gsh) {
        shoppedServices.remove(gsh);
    }

    //~--- get methods --------------------------------------------------------

    public List getShoppedServices() {
        return new ArrayList(shoppedServices);
    }

    //~--- set methods --------------------------------------------------------

    public void setShoppedServices(List shoppedServices) {
        this.shoppedServices = new HashSet(shoppedServices);
    }

	public String getServiceCartFailureMessage() {
		return serviceCartFailureMessage;
	}

	public void setServiceCartFailureMessage(String serviceCartFailureMessage) {
		this.serviceCartFailureMessage = serviceCartFailureMessage;
	}
}

//~ Formatted by Jindent --- http://www.jindent.com
