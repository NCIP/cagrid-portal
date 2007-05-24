package gov.nih.nci.cagrid.data.ui.auditors;

import gov.nih.nci.cagrid.data.service.auditing.DataServiceAuditor;

import java.util.EventListener;

/** 
 *  AuditorAdditionListener
 *  Listens for changes to the auditor selection on a data service
 * 
 * @author David Ervin
 * 
 * @created May 21, 2007 11:57:48 AM
 * @version $Id: AuditorAdditionListener.java,v 1.1 2007-05-24 16:11:22 dervin Exp $ 
 */
public interface AuditorAdditionListener extends EventListener {

    public void auditorAdded(DataServiceAuditor auditor, String auditorClass, String instanceName);
    
    
    public void auditorRemoved(String auditorClass, String instanceName);
}
