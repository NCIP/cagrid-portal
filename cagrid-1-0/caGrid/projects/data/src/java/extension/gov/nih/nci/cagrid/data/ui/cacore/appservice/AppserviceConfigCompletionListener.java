package gov.nih.nci.cagrid.data.ui.cacore.appservice;

import java.util.EventListener;

/** 
 *  AppserviceConfigCompletionListener
 *  Simple event listener to perform notification when an appservice
 *  config panel is complete
 * 
 * @author David Ervin
 * 
 * @created Mar 23, 2007 1:57:47 PM
 * @version $Id: AppserviceConfigCompletionListener.java,v 1.1 2007-03-28 17:33:11 dervin Exp $ 
 */
public interface AppserviceConfigCompletionListener extends EventListener {

    public void completionStatusChanged(boolean isComplete);
}
