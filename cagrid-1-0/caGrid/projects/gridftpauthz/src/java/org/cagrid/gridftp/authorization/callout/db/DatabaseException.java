package org.cagrid.gridftp.authorization.callout.db;

/**
 * This exception is thrown whenever a database exception of
 * any kind occurs.
 * 
 * @author <A HREF="MAILTO:jpermar at bmi.osu.edu">Justin Permar</A>
 * @created Mar 20, 2007
 * @version $Id: DatabaseException.java,v 1.1 2007-03-21 13:59:19 jpermar Exp $
 */
public class DatabaseException extends Exception {

	/**
	 * Default <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -5250386282238013288L;


	/**
	 * @param msg
	 *            the reason for the exception
	 */
	public DatabaseException(String msg) {
		super(msg);
	}


	/**
	 * @param t
	 *            the wrapped throwable
	 */
	public DatabaseException(Throwable t) {
		super(t);
	}


	/**
	 * @param msg
	 *            the reason for the exception
	 * @param t
	 *            the wrapped throwable
	 */
	public DatabaseException(String msg, Throwable t) {
		super(msg, t);
	}

}
