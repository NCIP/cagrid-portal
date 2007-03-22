package org.cagrid.gridftp.authorization.plugin;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * A tuple representing the GridFTP request that the user made. The
 * requests consists of three parts: 1. Requester identity 2. Requested
 * operation 3. The target resource that the request applies to GridFTPOperation
 * defines the allowed operations
 * 
 * @see GridFTPOperation
 * @author <A HREF="MAILTO:jpermar at bmi.osu.edu">Justin Permar</A>
 * @created Mar 20, 2007
 * @version $Id: GridFTPTuple.java,v 1.1 2007-03-22 18:54:44 jpermar Exp $
 */
public class GridFTPTuple {
	private String _identity;
	private GridFTPOperation _operation;
	private String _url;


	/**
	 * Create a tuple for the GridFTP request
	 * 
	 * @param identity
	 *            the requester identity. An example is
	 * 
	 * <pre>
	 *  /O=cagrid.org/OU=training/OU=caBIG User Group/OU=IdP [1]/CN=myuser
	 * </pre>
	 * 
	 * @param operation
	 *            the requested operation
	 * @param url
	 *            the complete URL of the requested resource. Only the path of
	 *            the resource is actually kept for authorization purposes,
	 *            since that is the only relevant information. the complete URL
	 *            of the requested resource. Example:
	 * 
	 * <pre>
	 *  ftp://www.myserver.com/my/file
	 * </pre>
	 * 
	 * @see GridFTPOperation
	 * @throws MalformedURLException
	 */
	public GridFTPTuple(String identity, GridFTPOperation operation, String url) throws MalformedURLException {
		setIdentity(identity);
		setOperation(operation);
		setURL(url);
	}


	private void setIdentity(String _identity) {
		this._identity = _identity;
	}


	public String getIdentity() {
		return _identity;
	}


	private void setOperation(GridFTPOperation _operation) {
		this._operation = _operation;
	}


	public GridFTPOperation getOperation() {
		return _operation;
	}


	private void setURL(String _url) throws MalformedURLException {
		URL url = new URL(_url);
		this._url = url.getPath();
		// this._url = _url;
	}


	public String getURL() {
		return _url;
	}


	/**
	 * @return the tuple in string format. An example is
	 * 
	 * <pre>
	 * (identity, read, /my/file)
	 * </pre>
	 */
	public String toString() {
		return "(" + _identity + "," + _operation + "," + _url + ")";
	}
}