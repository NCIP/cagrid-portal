package org.cagrid.gridftp.authorization.plugin;

/**
 * Type-safe representation of a GridFTP operation. The
 * corresponding valid operations are defined in CAS.
 * 
 * @see <a
 *      href="http://www.globus.org/toolkit/docs/4.0/security/cas/WS_AA_CAS_HOWTO_Setup_GridFTP.html">WS_AA_CAS_HOWTO_Setup_GridFTP</a>
 * @author <A HREF="MAILTO:jpermar at bmi.osu.edu">Justin Permar</A>
 * @created Mar 20, 2007
 * @version $Id: GridFTPOperation.java,v 1.2 2007-03-23 15:07:40 jpermar Exp $
 */

public enum GridFTPOperation {

	read("read"), lookup("lookup"), write("write"), create("create"), delete("delete"), chdir("chdir");

	private String _operation;


	private GridFTPOperation(String op) {
		_operation = op;
	}


	public String toString() {
		return _operation;
	}

}
