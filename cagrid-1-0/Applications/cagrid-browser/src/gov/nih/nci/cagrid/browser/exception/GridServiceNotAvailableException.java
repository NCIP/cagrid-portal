package gov.nih.nci.cagrid.browser.exception;

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Aug 1, 2005
 * Time: 7:05:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridServiceNotAvailableException extends RuntimeException {
    public GridServiceNotAvailableException() {
        super("Grid Service is temporarily unavailable");
    }

    public GridServiceNotAvailableException(String message) {
        super(message);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
