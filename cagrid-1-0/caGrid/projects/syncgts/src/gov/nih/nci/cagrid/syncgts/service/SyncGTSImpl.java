package gov.nih.nci.cagrid.syncgts.service;

import gov.nih.nci.cagrid.syncgts.bean.SyncDescription;
import gov.nih.nci.cagrid.syncgts.core.SyncGTS;
import gov.nih.nci.cagrid.syncgts.core.SyncGTSDefault;

import java.io.File;
import java.rmi.RemoteException;

import javax.naming.InitialContext;

import org.apache.axis.MessageContext;
import org.globus.wsrf.Constants;
import org.globus.wsrf.config.ContainerConfig;


/**
 * gov.nih.nci.cagrid.syncgtsI TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 */
public class SyncGTSImpl {
	private static final String SYNC_DESCRIPTION = "syncDescription";
	private ServiceConfiguration configuration;


	public SyncGTSImpl() throws RemoteException {
		try {
			String configFileEnd = (String) MessageContext.getCurrentContext().getProperty(SYNC_DESCRIPTION);
			String configFile = ContainerConfig.getBaseDirectory() + File.separator + configFileEnd;
			SyncGTSDefault.setServiceSyncDescriptionLocation(configFile);
			SyncDescription description = SyncGTSDefault.getSyncDescription();
			try {
				SyncGTS sync = SyncGTS.getInstance();
				if ((getConfiguration().getPerformFirstSync() != null)
					&& (getConfiguration().getPerformFirstSync().equalsIgnoreCase("true"))) {
					sync.syncOnce(description);
				}
				sync.syncAndResyncInBackground(description, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException("Error Starting SyncGTS Service: " + ex.getMessage());
		}

	}


	public ServiceConfiguration getConfiguration() throws Exception {
		if (this.configuration != null) {
			return this.configuration;
		}
		MessageContext ctx = MessageContext.getCurrentContext();

		String servicePath = ctx.getTargetService();

		String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/serviceconfiguration";
		try {
			javax.naming.Context initialContext = new InitialContext();
			this.configuration = (ServiceConfiguration) initialContext.lookup(jndiName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Unable to instantiate service configuration.", e);
		}

		return this.configuration;
	}

}
