package gov.nih.nci.cagrid.portal.exception;


/**
 * Denotes a fatal exception in
 * properly initializing the Portal
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 2, 2006
 * Time: 10:12:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class PortalInitializationException extends PortalRuntimeException {

    public PortalInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PortalInitializationException(Throwable cause) {
        super(cause);
    }
}
