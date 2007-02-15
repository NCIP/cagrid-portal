package gov.nih.nci.cagrid.data.upgrades;

/** 
 *  UpgradeException
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 8, 2007 
 * @version $Id: UpgradeException.java,v 1.1 2007-02-15 16:02:10 dervin Exp $ 
 */
public class UpgradeException extends Exception {

	public UpgradeException(Exception cause) {
		super(cause);
	}
	
	
	public UpgradeException(String message) {
		super(message);
	}
	
	
	public UpgradeException(String message, Exception cause) {
		super(message, cause);
	}
}
