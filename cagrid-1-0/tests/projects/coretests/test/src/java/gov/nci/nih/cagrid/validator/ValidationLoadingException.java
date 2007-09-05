package gov.nci.nih.cagrid.validator;

/** 
 *  ValidationLoadingException
 *  Exception thrown to indicate a problem with loading a 
 *  deployment validation description
 * 
 * @author David Ervin
 * 
 * @created Sep 5, 2007 11:42:17 AM
 * @version $Id: ValidationLoadingException.java,v 1.1 2007-09-05 17:01:35 dervin Exp $ 
 */
public class ValidationLoadingException extends Exception {

    public ValidationLoadingException(String message) {
        super(message);
    }
    
    
    public ValidationLoadingException(Exception cause) {
        super(cause);
    }
    
    
    public ValidationLoadingException(String message, Exception cause) {
        super(message, cause);
    }
}
