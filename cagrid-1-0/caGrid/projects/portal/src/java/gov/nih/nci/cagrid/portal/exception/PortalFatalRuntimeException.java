package gov.nih.nci.cagrid.portal.exception;

/**
 * Denotes a Serious runtime
 * exception with the Portal.
 * Should be intercepted and admin notified
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 4, 2006
 * Time: 1:14:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class PortalFatalRuntimeException extends PortalRuntimeException {

    public PortalFatalRuntimeException() {
    }

    public PortalFatalRuntimeException(String message) {
        super(message);
    }

    public PortalFatalRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PortalFatalRuntimeException(Throwable cause) {
        super(cause);
    }
}
