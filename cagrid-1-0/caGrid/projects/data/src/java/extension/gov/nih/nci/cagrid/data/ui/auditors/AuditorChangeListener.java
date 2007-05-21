package gov.nih.nci.cagrid.data.ui.auditors;

import gov.nih.nci.cagrid.data.service.auditing.DataServiceAuditor;

import java.util.EventListener;

/** 
 *  AuditorChangeListener
 *  Listens for changes to the auditor selection on a data service
 * 
 * @author David Ervin
 * 
 * @created May 21, 2007 11:57:48 AM
 * @version $Id: AuditorChangeListener.java,v 1.1 2007-05-21 19:07:57 dervin Exp $ 
 */
public interface AuditorChangeListener extends EventListener {

    public void auditorAdded(DataServiceAuditor auditor, String auditorClass, String instanceName);
    
    
    public void auditorRemoved(String auditorClass, String instanceName);
}
