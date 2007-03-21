package org.cagrid.gridftp.authorization.callout;

/**
 * This is the interface that all Java Authorization plugins must
 * implement. An authorization plugin developer simply needs to implement this
 * interface in order to develop a working authorization plugin. There is a more
 * convenient and type-safe way to develop an authorization plugin. Refer to
 * {@link AbstractAuthCallout} for details.
 * 
 * @see AbstractAuthCallout
 * @see <a
 *      href="http://www.globus.org/toolkit/docs/4.0/security/cas/WS_AA_CAS_HOWTO_Setup_GridFTP.html">WS_AA_CAS_HOWTO_Setup_GridFTP</a>
 * @author <A HREF="MAILTO:jpermar at bmi.osu.edu">Justin Permar</A>
 * @created Mar 20, 2007
 * @version $Id: Authorize.java,v 1.1 2007-03-21 13:59:19 jpermar Exp $
 */
public interface Authorize {

	/**
	 * Perform an authorization check on the given request parameters.
	 * 
	 * @param identity
	 *            the requester identity. An example is
	 * 
	 * <pre>
	 *   /O=cagrid.org/OU=training/OU=caBIG User Group/OU=IdP [1]/CN=myuser
	 * </pre>
	 * 
	 * @param operation
	 *            the requested operation. Supported operations are listed in
	 *            the CAS documentation.
	 * @param target
	 *            the complete URL of the requested resource. Example:
	 * 
	 * <pre>
	 *   ftp://www.myserver.com/my/file
	 * </pre>
	 * 
	 * @return true if the request is authorized, false otherwise.
	 * @see <a
	 *      href="http://www.globus.org/toolkit/docs/4.0/security/cas/WS_AA_CAS_HOWTO_Setup_GridFTP.html">WS_AA_CAS_HOWTO_Setup_GridFTP</a>
	 */
	public boolean authorize(String identity, String operation, String target);

}
