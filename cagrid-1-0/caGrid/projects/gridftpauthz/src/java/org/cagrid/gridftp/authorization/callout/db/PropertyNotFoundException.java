package org.cagrid.gridftp.authorization.callout.db;

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
 * @version $Id: PropertyNotFoundException.java,v 1.1 2007-03-21 13:59:19 jpermar Exp $
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
