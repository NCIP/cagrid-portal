package org.cagrid.gridftp.authorization.plugin.gridgrouper;

/** 
 *  SchemaValidationException
 *  Exception thrown when schema validation goes awry
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 25, 2006 
 * @version $Id: SchemaValidationException.java,v 1.1 2007-04-02 22:04:55 jpermar Exp $ 
 */
public class SchemaValidationException extends Exception {

	public SchemaValidationException(String message) {
		super(message);
	}
	
	
	public SchemaValidationException(Exception ex) {
		super(ex);
	}
	
	
	public SchemaValidationException(String message, Exception ex) {
		super(message, ex);
	}
}
