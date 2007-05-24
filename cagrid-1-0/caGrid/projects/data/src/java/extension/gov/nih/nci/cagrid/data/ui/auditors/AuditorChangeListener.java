package gov.nih.nci.cagrid.data.ui.auditors;

import java.util.EventListener;

/** 
 *  AuditorChangeListener
 *  Listens for changes to the data service auditor selection
 * 
 * @author David Ervin
 * 
 * @created May 22, 2007 11:13:11 AM
 * @version $Id: AuditorChangeListener.java,v 1.2 2007-05-24 16:11:22 dervin Exp $ 
 */
public interface AuditorChangeListener extends EventListener {

    public void auditorSelectionChanged(String className, String instanceName);
}
