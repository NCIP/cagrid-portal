package org.cagrid.gaards.cds.service;

import java.rmi.RemoteException;

import org.apache.axis.MessageContext;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.gaards.cds.common.DelegationIdentifier;
import org.cagrid.gaards.cds.delegated.service.DelegatedCredentialResourceHome;
import org.cagrid.gaards.cds.delegated.stubs.types.DelegatedCredentialReference;
import org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault;
import org.cagrid.tools.database.Database;
import org.globus.wsrf.security.SecurityManager;
import org.globus.wsrf.utils.AddressingUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

/**
 * TODO:I am the service side implementation class. IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.1
 */
public class CredentialDelegationServiceImpl extends
		CredentialDelegationServiceImplBase {

	private Log log;
	private DelegationManager cds;
	private DelegatedCredentialResourceHome home;

	public CredentialDelegationServiceImpl() throws RemoteException {
		super();
		try {
			this.log = LogFactory.getLog(this.getClass().getName());
			String conf = this.getConfiguration().getCdsConfiguration();
			String properties = this.getConfiguration().getCdsProperties();
			FileSystemResource fsr = new FileSystemResource(conf);
			XmlBeanFactory factory = new XmlBeanFactory(fsr);
			PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
			cfg.setLocation(new FileSystemResource(properties));
			cfg.postProcessBeanFactory(factory);
			Database db = (Database) factory
					.getBean(ConfigurationConstants.DATABASE_CONFIGURATION_BEAN);
			db.createDatabaseIfNeeded();
			cds = (DelegationManager) factory
					.getBean(ConfigurationConstants.CDS_BEAN);

			home = (DelegatedCredentialResourceHome) getDelegatedCredentialResourceHome();
			home.setCDS(cds);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw Errors.getInternalFault(
					"Error initializing the Credential Delegation Service.", e);
		}
	}

	private String getCallerIdentity() throws PermissionDeniedFault {
		String caller = SecurityManager.getManager().getCaller();
		if ((caller == null) || (caller.equals("<anonymous>"))) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString(Errors.AUTHENTICATION_REQUIRED);
			throw fault;
		}
		return caller;
	}

  public org.cagrid.gaards.cds.common.DelegationSigningRequest initiateDelegation(org.cagrid.gaards.cds.common.DelegationRequest req) throws RemoteException, org.cagrid.gaards.cds.stubs.types.CDSInternalFault, org.cagrid.gaards.cds.stubs.types.InvalidPolicyFault, org.cagrid.gaards.cds.stubs.types.DelegationFault, org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault {
		return cds.initiateDelegation(getCallerIdentity(), req);
	}

  public org.cagrid.gaards.cds.delegated.stubs.types.DelegatedCredentialReference approveDelegation(org.cagrid.gaards.cds.common.DelegationSigningResponse delegationSigningResponse) throws RemoteException, org.cagrid.gaards.cds.stubs.types.CDSInternalFault, org.cagrid.gaards.cds.stubs.types.DelegationFault, org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault {

		DelegationIdentifier id = cds.approveDelegation(getCallerIdentity(),
				delegationSigningResponse);

		try {
			MessageContext ctx = MessageContext.getCurrentContext();
			String transportURL = (String) ctx
					.getProperty(org.apache.axis.MessageContext.TRANS_URL);
			transportURL = transportURL.substring(0, transportURL
					.lastIndexOf('/') + 1);

			transportURL += "DelegatedCredential";

			EndpointReferenceType epr = AddressingUtils
					.createEndpointReference(transportURL, home
							.getResourceKey(id));
			return new DelegatedCredentialReference(epr);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw Errors.getInternalFault("Unexpected error creating EPR.", e);
		}

	}

  public org.cagrid.gaards.cds.common.DelegationRecord[] findDelegatedCredentials(org.cagrid.gaards.cds.common.DelegationRecordFilter filter) throws RemoteException, org.cagrid.gaards.cds.stubs.types.CDSInternalFault, org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault {
		return this.cds.findDelegatedCredentials(getCallerIdentity(), filter);
	}

  public void updateDelegatedCredentialStatus(org.cagrid.gaards.cds.common.DelegationIdentifier id,org.cagrid.gaards.cds.common.DelegationStatus status) throws RemoteException, org.cagrid.gaards.cds.stubs.types.CDSInternalFault, org.cagrid.gaards.cds.stubs.types.DelegationFault, org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault {
		this.cds.updateDelegatedCredentialStatus(getCallerIdentity(), id,
				status);
	}

}
