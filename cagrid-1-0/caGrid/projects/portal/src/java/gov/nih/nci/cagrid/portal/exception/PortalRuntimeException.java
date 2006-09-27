package gov.nih.nci.cagrid.portal.exception;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 25, 2006
 * Time: 6:20:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class PortalRuntimeException extends RuntimeException {

    public PortalRuntimeException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public PortalRuntimeException(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public PortalRuntimeException(String message, Throwable cause) {
        super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public PortalRuntimeException(Throwable cause) {
        super(cause);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
