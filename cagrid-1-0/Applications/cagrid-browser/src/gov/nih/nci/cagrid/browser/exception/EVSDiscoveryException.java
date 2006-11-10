package gov.nih.nci.cagrid.browser.exception;

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Aug 2, 2005
 * Time: 6:24:30 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * EVSDiscoveryException is a runtime exception
 * thrown when cagrid-browser application
 * throws an exception when communicating with
 * the EVS Service (using caCore API)
 * <p/>
 * This runtime exception is caught by the webapp container
 */
public class EVSDiscoveryException extends RuntimeException {
    public EVSDiscoveryException() {
    }

    public EVSDiscoveryException(String message) {
        super(message);
    }

    public EVSDiscoveryException(String message, Throwable cause) {
        super(message, cause);
    }
}

//~ Formatted by Jindent --- http://www.jindent.com
