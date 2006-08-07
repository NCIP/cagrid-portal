package gov.nih.nci.cagrid.portal.exception;


/**
 *
 * Denotes a fatal exception in
 * properly initializing the Portal
 *
 *
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 2, 2006
 * Time: 10:12:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class PortalInitializationException extends RuntimeException {
    public PortalInitializationException(Throwable cause) {
        super(cause);
    }
}
