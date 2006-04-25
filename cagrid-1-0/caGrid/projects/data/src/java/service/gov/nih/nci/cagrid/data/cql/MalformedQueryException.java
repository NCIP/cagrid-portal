package gov.nih.nci.cagrid.data.cql;

/** 
 *  MalformedQueryException
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 25, 2006 
 * @version $Id$ 
 */
public class MalformedQueryException extends Exception {

	public MalformedQueryException(String message) {
		super(message);
	}
	
	
	public MalformedQueryException(Exception ex) {
		super(ex);
	}
	
	
	public MalformedQueryException(String message, Exception ex) {
		super(message, ex);
	}
}
