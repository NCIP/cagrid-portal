package org.cagrid.gridftp.authorization.plugin;

import java.net.MalformedURLException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.cagrid.gridftp.authorization.plugin.db.DatabaseAuthCallout;

/**
 * 
 * This is an abstract convenience class for developers writing
 * their own authorization plugins.
 * 
 * To use this class, simply inherit from it and implement
 * <code>authorizeOperation(String, org.cagrid.gridftp.authorization.callout.GridFTPOperation.Operation, String)</code>
 * Note that your concrete subclass must have a default (empty) constructor.
 * 
 *  For an example implementation, refer to
 *  {@link DatabaseAuthCallout}
 * 
 * @author <A HREF="MAILTO:jpermar at bmi.osu.edu">Justin Permar</A>
 * 
 * @created Mar 20, 2007 
 * @version $Id: AbstractAuthCallout.java,v 1.1 2007-03-22 18:54:44 jpermar Exp $
 */
public abstract class AbstractAuthCallout implements Authorize {

	static String _logFilePattern = "%t/AuthCallout.%u.log";
	protected Logger _logger = Logger.getLogger("AuthCallout");
	//TODO change logger to apache logger
	//protected Log _logger = LogFactory.getLog(AuthLogger.class.getName());
	
	public AbstractAuthCallout() {
		try {
			Handler handler = new FileHandler(_logFilePattern);
			handler.setFormatter(new SimpleFormatter());
			_logger.addHandler(handler);
			_logger.setLevel(Level.INFO);
		} catch (Exception e) {
			System.out.println("Could not configure logging due to reason: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public boolean authorize(String identity, String operation, String target) {
		boolean authorized = false;
		
		_logger.info("identity: " + identity);
		_logger.info("operation: " + operation);
		_logger.info("target URL: " + target);

		//there are only a few valid operation strings
		//convert to the appropriate typesafe enum
		GridFTPOperation op;
		try {
			op = GridFTPOperation.valueOf(operation);
			GridFTPTuple tuple = new GridFTPTuple(identity, op, target);
			authorized = authorizeOperation(tuple);
		} catch (IllegalArgumentException e) {
			//need to make an authorization decision. Clearly this implementation version
			//does not match the GridFTP supported operations, so basically barf.
			String msg = "Detected unsupported operation: " + operation;
			_logger.log(Level.SEVERE, msg, e);
		} catch (MalformedURLException e) {
			String msg = "Malformed URL: " + target;
			_logger.severe(msg);
		}
		
		return authorized;
	}

	/**
	 * Override and implement this method to make a concrete authorization
	 * plugin.
	 * @param tuple the tuple representing the request
	 * @return true if authorization is successful, false otherwise
	 * @see GridFTPTuple
	 */
	public abstract boolean authorizeOperation(GridFTPTuple tuple);
	
}
