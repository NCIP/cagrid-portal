package gov.nih.nci.cagrid.portal.map;

import javax.faces.FacesException;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 2, 2006
 * Time: 5:45:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class PortalMapComponentException extends FacesException {

    public PortalMapComponentException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public PortalMapComponentException(Throwable throwable) {
        super(throwable);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public PortalMapComponentException(String string) {
        super(string);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public PortalMapComponentException(String string, Throwable throwable) {
        super(string, throwable);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
