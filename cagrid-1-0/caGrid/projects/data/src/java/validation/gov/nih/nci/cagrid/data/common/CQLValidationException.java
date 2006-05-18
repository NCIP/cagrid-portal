package gov.nih.nci.cagrid.data.common;

/**
 * Exception thrown when CQL object validation fails
 * <p/>
 * Created by the caGrid Team
 * User: kherm
 * Date: May 15, 2006
 * Time: 11:55:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class CQLValidationException extends Exception {

    public CQLValidationException(String message) {
        super(message);
    }

    public CQLValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CQLValidationException(Throwable cause) {
        super(cause);
    }
}
