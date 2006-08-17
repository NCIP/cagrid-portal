package gov.nih.nci.cagrid.wsenum.utils;

/** 
 *  EnumerationCreationException
 *  Generic exception for enumerate response factory to throw
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 17, 2006 
 * @version $Id$ 
 */
public class EnumerationCreationException extends Exception {

	public EnumerationCreationException(String message) {
		super(message);
	}
	
	
	public EnumerationCreationException(Throwable th) {
		super(th);
	}
	
	
	public EnumerationCreationException(String message, Throwable th) {
		super(message, th);
	}
}
