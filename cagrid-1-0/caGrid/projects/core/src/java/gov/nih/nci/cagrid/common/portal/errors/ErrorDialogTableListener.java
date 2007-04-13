package gov.nih.nci.cagrid.common.portal.errors;

import java.util.EventListener;

/** 
 *  ErrorDialogTableListener
 *  TODO:DOCUMENT ME
 * 
 * @author David Ervin
 * 
 * @created Apr 13, 2007 9:43:11 AM
 * @version $Id: ErrorDialogTableListener.java,v 1.1 2007-04-13 17:19:32 dervin Exp $ 
 */
public interface ErrorDialogTableListener extends EventListener {

    public void showDetailsClicked(ErrorContainer container);
    
    
    public void showErrorClicked(ErrorContainer container);
}
