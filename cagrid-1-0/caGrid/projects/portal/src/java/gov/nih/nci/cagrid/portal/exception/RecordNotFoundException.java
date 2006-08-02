package gov.nih.nci.cagrid.portal.exception;


/**
 * Will be thrown if an appropriate record
 * is not found in the database
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 21, 2006
 * Time: 4:46:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class RecordNotFoundException extends RuntimeException {
    /**
     * Constructs a new runtime exception with <code>null</code> as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public RecordNotFoundException() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public RecordNotFoundException(String message) {
        super(message);
    }
}
