package org.cagrid.gridftp.authorization.plugin.db;

import java.util.Properties;

/**
 * 
 * This exception is thrown when the expected Property key cannot be found
 * in a Properties object.
 * 
 * @see Properties
 * 
 * @author <A HREF="MAILTO:jpermar at bmi.osu.edu">Justin Permar</A>
 * 
 * @created Mar 20, 2007 
 * @version $Id: PropertyNotFoundException.java,v 1.1 2007-03-22 18:54:44 jpermar Exp $
 */
public class PropertyNotFoundException extends Exception {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 5133678198208954506L;

	/**
	 * 
	 * @param msg the reason for the exception, typically specifying the
	 * property key that wasn't found.
	 */
	public PropertyNotFoundException(String msg) {
		super(msg);
	}
}
