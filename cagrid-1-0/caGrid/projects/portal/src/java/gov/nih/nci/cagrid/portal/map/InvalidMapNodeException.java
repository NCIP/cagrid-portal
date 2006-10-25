package gov.nih.nci.cagrid.portal.map;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 23, 2006
 * Time: 4:00:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvalidMapNodeException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public InvalidMapNodeException(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public InvalidMapNodeException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
