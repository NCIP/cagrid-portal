package gov.nih.nci.cagrid.data.ui.auditors;

import java.util.EventListener;

/** 
 *  AuditorPropertyChangeListener
 *  Listens for property edit events
 * 
 * @author David Ervin
 * 
 * @created May 22, 2007 10:33:46 AM
 * @version $Id: AuditorPropertyChangeListener.java,v 1.1 2007-05-24 16:11:22 dervin Exp $ 
 */
public interface AuditorPropertyChangeListener extends EventListener {

    public void propertyValueEdited(String key, String newValue);
}
