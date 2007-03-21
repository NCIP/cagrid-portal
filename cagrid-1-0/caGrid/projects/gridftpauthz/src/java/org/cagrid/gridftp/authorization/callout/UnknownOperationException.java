package org.cagrid.gridftp.authorization.callout;

/**
 * This exception is thrown when an unknown GridFTP
 * operation is requested. The allowed operations are listed as an enum in
 * GridFTPOperation.Operation
 * 
 * @see GridFTPOperation
 * @author <A HREF="MAILTO:jpermar at bmi.osu.edu">Justin Permar</A>
 * @created Mar 20, 2007
 * @version $Id: UnknownOperationException.java,v 1.1 2007-03-21 13:59:19 jpermar Exp $
 */
public class UnknownOperationException extends Exception {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -4645355890088923661L;


	/**
	 * @param msg
	 *            the reason for the exception
	 */
	public UnknownOperationException(String msg) {
		super(msg);
	}
}
