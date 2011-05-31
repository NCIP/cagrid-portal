package gov.nih.nci.cagrid.portal.util;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalDBRuntimeException extends RuntimeException{

    public PortalDBRuntimeException() {
    }

    public PortalDBRuntimeException(String s) {
        super(s);
    }

    public PortalDBRuntimeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PortalDBRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
