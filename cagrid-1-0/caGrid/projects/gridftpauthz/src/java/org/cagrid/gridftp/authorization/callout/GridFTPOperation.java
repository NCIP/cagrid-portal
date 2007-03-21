package org.cagrid.gridftp.authorization.callout;

/**
 * Type-safe representation of a GridFTP operation. The
 * corresponding valid operations are defined in CAS.
 * 
 * @see <a
 *      href="http://www.globus.org/toolkit/docs/4.0/security/cas/WS_AA_CAS_HOWTO_Setup_GridFTP.html">WS_AA_CAS_HOWTO_Setup_GridFTP</a>
 * @author <A HREF="MAILTO:jpermar at bmi.osu.edu">Justin Permar</A>
 * @created Mar 20, 2007
 * @version $Id: GridFTPOperation.java,v 1.1 2007-03-21 13:59:19 jpermar Exp $
 */

public enum GridFTPOperation {

	READ("read"), LOOKUP("lookup"), WRITE("write"), CREATE("create"), DELETE("delete"), CHDIR("chdir");

	private String _operation;


	private GridFTPOperation(String op) {
		_operation = op;
	}


	public String toString() {
		return _operation;
	}

}
