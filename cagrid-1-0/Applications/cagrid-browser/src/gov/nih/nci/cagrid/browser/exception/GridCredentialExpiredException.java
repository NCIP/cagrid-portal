package gov.nih.nci.cagrid.browser.exception;

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Aug 3, 2005
 * Time: 2:25:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridCredentialExpiredException extends Exception {
    public GridCredentialExpiredException() {}

    public GridCredentialExpiredException(String message) {
        super(message);
    }

    public GridCredentialExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
