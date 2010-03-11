package gov.nih.nci.cagrid.portal.search;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalSearchRuntimeException extends RuntimeException {
    public PortalSearchRuntimeException() {
    }

    public PortalSearchRuntimeException(String s) {
        super(s);
    }

    public PortalSearchRuntimeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PortalSearchRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
