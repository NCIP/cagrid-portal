package gov.nih.nci.cagrid.syncgts.service;

import gov.nih.nci.cagrid.syncgts.bean.SyncDescription;
import gov.nih.nci.cagrid.syncgts.common.SyncGTSI;
import gov.nih.nci.cagrid.syncgts.core.SyncGTS;
import gov.nih.nci.cagrid.syncgts.core.SyncGTSDefault;
import gov.nih.nci.cagrid.syncgts.service.globus.resource.BaseResourceHome;

import java.rmi.RemoteException;

import org.globus.wsrf.ResourceContext;


/**
 * gov.nih.nci.cagrid.syncgtsI TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 */
public class SyncGTSImpl implements SyncGTSI {

	public SyncGTSImpl() throws RemoteException {
		try {
			BaseResourceHome home = (BaseResourceHome) ResourceContext.getResourceContext().getResourceHome();
			SyncGTSDefault.setServiceSyncDescriptionLocation(home.getSyncDescription());
			SyncDescription description = SyncGTSDefault.getSyncDescription();
			try {
				SyncGTS sync = SyncGTS.getInstance();
				sync.syncOnce(description);
				sync.syncAndResyncInBackground(description, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException("Error Starting SyncGTS Service: " + ex.getMessage());
		}

	}

}
