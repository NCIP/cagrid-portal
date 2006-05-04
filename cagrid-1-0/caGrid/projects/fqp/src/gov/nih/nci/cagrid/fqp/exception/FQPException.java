package gov.nih.nci.cagrid.fqp.exception;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: May 3, 2006
 * Time: 4:55:06 PM
 * To change this template use File | Settings | File Templates.
 *
 * The most generic Federated Query Processor (FQP) exception. This exception
 * will containt a nested exception specific to the error condition generated
 */

public class FQPException extends Exception{

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @since 1.4
     */
    public FQPException(Throwable cause) {
        super(cause);
    }


    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.4
     */
    public FQPException(String message, Throwable cause) {
        super(message, cause);
    }
}
