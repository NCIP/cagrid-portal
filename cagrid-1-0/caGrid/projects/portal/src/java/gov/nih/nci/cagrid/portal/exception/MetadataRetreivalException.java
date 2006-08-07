package gov.nih.nci.cagrid.portal.exception;


/**
 * unchecked exception when there is a problem
 * retreiving appropriate metadata
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 18, 2006
 * Time: 5:24:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetadataRetreivalException extends Exception {
    public MetadataRetreivalException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetadataRetreivalException(Throwable cause) {
        super(cause);
    }

    public MetadataRetreivalException(String message) {
        super(message);
    }
}
