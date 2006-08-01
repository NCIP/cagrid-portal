package gov.nih.nci.cagrid.gridgrouper.client;

import gov.nih.nci.cagrid.common.security.ProxyUtil;
import gov.nih.nci.cagrid.gridgrouper.common.GridGrouperI;
import gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperPortType;
import gov.nih.nci.cagrid.gridgrouper.stubs.service.GridGrouperServiceAddressingLocator;
import gov.nih.nci.cagrid.introduce.security.client.ServiceSecurityClient;
import gov.nih.nci.cagrid.metadata.security.CommunicationMechanism;
import gov.nih.nci.cagrid.metadata.security.Operation;
import gov.nih.nci.cagrid.metadata.security.ProtectionLevelType;
import gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadataOperations;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.HashMap;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.AxisClient;
import org.apache.axis.client.Stub;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.axis.utils.ClassUtils;
import org.globus.gsi.GlobusCredential;
import org.globus.wsrf.impl.security.authorization.NoAuthorization;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GridGrouperClient extends ServiceSecurityClient implements GridGrouperI {
	protected GridGrouperPortType portType;
	private Object portTypeMutex;
	

	public GridGrouperClient(String url) throws MalformedURIException, RemoteException {
		this(url, null);
	}

	public GridGrouperClient(String url, GlobusCredential proxy) throws MalformedURIException, RemoteException {
		super(url, proxy);
		initialize();
	}

	public GridGrouperClient(EndpointReferenceType epr) throws MalformedURIException, RemoteException {
		this(epr, null);
	}

	public GridGrouperClient(EndpointReferenceType epr, GlobusCredential proxy) throws MalformedURIException,
		RemoteException {
		super(epr, proxy);
		initialize();
	}

	private void initialize() throws RemoteException {
		this.portTypeMutex = new Object();
		this.portType = createPortType();
	}

	private GridGrouperPortType createPortType() throws RemoteException {

		GridGrouperServiceAddressingLocator locator = new GridGrouperServiceAddressingLocator();
		// attempt to load our context sensitive wsdd file
		InputStream resourceAsStream = ClassUtils.getResourceAsStream(getClass(), "client-config.wsdd");
		if (resourceAsStream != null) {
			// we found it, so tell axis to configure an engine to use it
			EngineConfiguration engineConfig = new FileProvider(resourceAsStream);
			// set the engine of the locator
			locator.setEngine(new AxisClient(engineConfig));
		}
		GridGrouperPortType port = null;
		try {
			port = locator.getGridGrouperPortTypePort(getEndpointReference());
		} catch (Exception e) {
			throw new RemoteException("Unable to locate portType:" + e.getMessage(), e);
		}

		return port;
	}

	protected void configureStubSecurity(Stub stub, String method) throws RemoteException {

		boolean https = false;
		if (epr.getAddress().getScheme().equals("https")) {
			https = true;
		}

		if (method.equals("getServiceSecurityMetadata")) {
			if (https) {
				resetStub(stub);
				stub._setProperty(org.globus.wsrf.security.Constants.GSI_TRANSPORT,
					org.globus.wsrf.security.Constants.SIGNATURE);
				stub._setProperty(org.globus.wsrf.security.Constants.GSI_ANONYMOUS, Boolean.TRUE);
				stub._setProperty(org.globus.wsrf.security.Constants.AUTHORIZATION,
					org.globus.wsrf.impl.security.authorization.NoAuthorization.getInstance());
			}
			return;
		}

		if (this.securityMetadata == null) {
			operations = new HashMap();
			this.authorization = NoAuthorization.getInstance();
			this.securityMetadata = getServiceSecurityMetadata();
			ServiceSecurityMetadataOperations ssmo = securityMetadata.getOperations();
			if (ssmo != null) {
				Operation[] ops = ssmo.getOperation();
				if (ops != null) {
					for (int i = 0; i < ops.length; i++) {
						operations.put(ops[i].getName(), ops[i]);
					}
				}
			}

		}
		resetStub(stub);

		CommunicationMechanism serviceDefault = securityMetadata.getDefaultCommunicationMechanism();

		CommunicationMechanism mechanism = null;
		if (operations.containsKey(method)) {
			Operation o = (Operation) operations.get(method);
			mechanism = o.getCommunicationMechanism();
		} else {
			mechanism = serviceDefault;
		}
		boolean anonymousAllowed = true;
		boolean authorizationAllowed = true;
		boolean delegationAllowed = true;
		boolean credentialsAllowed = true;

		if ((https) && (mechanism.getGSITransport() != null)) {
			ProtectionLevelType level = mechanism.getGSITransport().getProtectionLevel();
			if (level != null) {
				if ((level.equals(ProtectionLevelType.privacy)) || (level.equals(ProtectionLevelType.either))) {
					stub._setProperty(org.globus.wsrf.security.Constants.GSI_TRANSPORT,
						org.globus.wsrf.security.Constants.ENCRYPTION);
				} else {
					stub._setProperty(org.globus.wsrf.security.Constants.GSI_TRANSPORT,
						org.globus.wsrf.security.Constants.SIGNATURE);
				}

			} else {
				stub._setProperty(org.globus.wsrf.security.Constants.GSI_TRANSPORT,
					org.globus.wsrf.security.Constants.SIGNATURE);
			}
			delegationAllowed = false;

		} else if (https) {
			stub._setProperty(org.globus.wsrf.security.Constants.GSI_TRANSPORT,
				org.globus.wsrf.security.Constants.SIGNATURE);
			delegationAllowed = false;
		} else if (mechanism.getGSISecureConversation() != null) {
			ProtectionLevelType level = mechanism.getGSISecureConversation().getProtectionLevel();
			if (level != null) {
				if ((level.equals(ProtectionLevelType.privacy)) || (level.equals(ProtectionLevelType.either))) {
					stub._setProperty(org.globus.wsrf.security.Constants.GSI_SEC_CONV,
						org.globus.wsrf.security.Constants.ENCRYPTION);

				} else {
					stub._setProperty(org.globus.wsrf.security.Constants.GSI_SEC_CONV,
						org.globus.wsrf.security.Constants.SIGNATURE);
				}

			} else {
				stub._setProperty(org.globus.wsrf.security.Constants.GSI_SEC_CONV,
					org.globus.wsrf.security.Constants.ENCRYPTION);
			}

		} else if (mechanism.getGSISecureMessage() != null) {
			ProtectionLevelType level = mechanism.getGSISecureMessage().getProtectionLevel();
			if (level != null) {
				if ((level.equals(ProtectionLevelType.privacy)) || (level.equals(ProtectionLevelType.either))) {
					stub._setProperty(org.globus.wsrf.security.Constants.GSI_SEC_MSG,
						org.globus.wsrf.security.Constants.ENCRYPTION);
				} else {
					stub._setProperty(org.globus.wsrf.security.Constants.GSI_SEC_MSG,
						org.globus.wsrf.security.Constants.SIGNATURE);
				}

			} else {
				stub._setProperty(org.globus.wsrf.security.Constants.GSI_SEC_MSG,
					org.globus.wsrf.security.Constants.ENCRYPTION);
			}
			delegationAllowed = false;
			anonymousAllowed = false;
		} else {
			anonymousAllowed = false;
			authorizationAllowed = false;
			delegationAllowed = false;
			credentialsAllowed = false;
		}

		if ((credentialsAllowed) && (proxy == null)) {
			try {
				GlobusCredential cred = ProxyUtil.getDefaultProxy();
				if (cred.getTimeLeft() > 0) {
					proxy = cred;
				}
			} catch (Exception e) {
				System.out.println("Error loading default proxy: " + e.getMessage());
			}

		}

		if ((credentialsAllowed) && (proxy != null)) {
			try {
				org.ietf.jgss.GSSCredential gss = new org.globus.gsi.gssapi.GlobusGSSCredentialImpl(proxy,
					org.ietf.jgss.GSSCredential.INITIATE_AND_ACCEPT);
				stub._setProperty(org.globus.axis.gsi.GSIConstants.GSI_CREDENTIALS, gss);
			} catch (org.ietf.jgss.GSSException ex) {
				throw new RemoteException(ex.getMessage());
			}
		} else if (anonymousAllowed) {
			stub._setProperty(org.globus.wsrf.security.Constants.GSI_ANONYMOUS, Boolean.TRUE);
		}

		if (authorizationAllowed) {
			if (authorization == null) {
				stub._setProperty(org.globus.wsrf.security.Constants.AUTHORIZATION, NoAuthorization.getInstance());
			} else {
				stub._setProperty(org.globus.wsrf.security.Constants.AUTHORIZATION, getAuthorization());
			}
		}
		if (delegationAllowed) {
			if (getDelegationMode() != null) {
				stub._setProperty(org.globus.axis.gsi.GSIConstants.GSI_MODE, getDelegationMode());
			}
		}
	}

    public boolean hasStemPrivilege(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,java.lang.String subject,gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType privilege) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"hasStemPrivilege");
        gov.nih.nci.cagrid.gridgrouper.stubs.HasStemPrivilegeRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.HasStemPrivilegeRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.HasStemPrivilegeRequestStem stemContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.HasStemPrivilegeRequestStem();
        stemContainer.setStemIdentifier(stem);
        params.setStem(stemContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.HasStemPrivilegeRequestSubject subjectContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.HasStemPrivilegeRequestSubject();
        subjectContainer.setSubjectIdentifier(subject);
        params.setSubject(subjectContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.HasStemPrivilegeRequestPrivilege privilegeContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.HasStemPrivilegeRequestPrivilege();
        privilegeContainer.setStemPrivilegeType(privilege);
        params.setPrivilege(privilegeContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.HasStemPrivilegeResponse boxedResult = portType.hasStemPrivilege(params);
        return boxedResult.isResponse();
      }
    }
    public gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadata getServiceSecurityMetadata() throws RemoteException {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"getServiceSecurityMetadata");
        gov.nih.nci.cagrid.introduce.security.GetServiceSecurityMetadataRequest params = new gov.nih.nci.cagrid.introduce.security.GetServiceSecurityMetadataRequest();
        gov.nih.nci.cagrid.introduce.security.GetServiceSecurityMetadataResponse boxedResult = portType.getServiceSecurityMetadata(params);
        return boxedResult.getServiceSecurityMetadata();
      }
    }
    public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor getStem(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"getStem");
        gov.nih.nci.cagrid.gridgrouper.stubs.GetStemRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.GetStemRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.GetStemRequestStem stemContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GetStemRequestStem();
        stemContainer.setStemIdentifier(stem);
        params.setStem(stemContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GetStemResponse boxedResult = portType.getStem(params);
        return boxedResult.getStemDescriptor();
      }
    }
    public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor[] getChildStems(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier parentStem) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"getChildStems");
        gov.nih.nci.cagrid.gridgrouper.stubs.GetChildStemsRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.GetChildStemsRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.GetChildStemsRequestParentStem parentStemContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GetChildStemsRequestParentStem();
        parentStemContainer.setStemIdentifier(parentStem);
        params.setParentStem(parentStemContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GetChildStemsResponse boxedResult = portType.getChildStems(params);
        return boxedResult.getStemDescriptor();
      }
    }
    public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor getParentStem(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier childStem) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"getParentStem");
        gov.nih.nci.cagrid.gridgrouper.stubs.GetParentStemRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.GetParentStemRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.GetParentStemRequestChildStem childStemContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GetParentStemRequestChildStem();
        childStemContainer.setStemIdentifier(childStem);
        params.setChildStem(childStemContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GetParentStemResponse boxedResult = portType.getParentStem(params);
        return boxedResult.getStemDescriptor();
      }
    }
    public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor updateStemDescription(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,java.lang.String description) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemModifyFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"updateStemDescription");
        gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemDescriptionRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemDescriptionRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemDescriptionRequestStem stemContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemDescriptionRequestStem();
        stemContainer.setStemIdentifier(stem);
        params.setStem(stemContainer);
        params.setDescription(description);
        gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemDescriptionResponse boxedResult = portType.updateStemDescription(params);
        return boxedResult.getStemDescriptor();
      }
    }
    public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor updateStemDisplayExtension(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,java.lang.String displayExtension) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemModifyFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"updateStemDisplayExtension");
        gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemDisplayExtensionRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemDisplayExtensionRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemDisplayExtensionRequestStem stemContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemDisplayExtensionRequestStem();
        stemContainer.setStemIdentifier(stem);
        params.setStem(stemContainer);
        params.setDisplayExtension(displayExtension);
        gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemDisplayExtensionResponse boxedResult = portType.updateStemDisplayExtension(params);
        return boxedResult.getStemDescriptor();
      }
    }
    public java.lang.String[] getSubjectsWithStemPrivilege(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType privilege) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"getSubjectsWithStemPrivilege");
        gov.nih.nci.cagrid.gridgrouper.stubs.GetSubjectsWithStemPrivilegeRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.GetSubjectsWithStemPrivilegeRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.GetSubjectsWithStemPrivilegeRequestStem stemContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GetSubjectsWithStemPrivilegeRequestStem();
        stemContainer.setStemIdentifier(stem);
        params.setStem(stemContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GetSubjectsWithStemPrivilegeRequestPrivilege privilegeContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GetSubjectsWithStemPrivilegeRequestPrivilege();
        privilegeContainer.setStemPrivilegeType(privilege);
        params.setPrivilege(privilegeContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GetSubjectsWithStemPrivilegeResponse boxedResult = portType.getSubjectsWithStemPrivilege(params);
        return boxedResult.getSubjectIdentifier();
      }
    }
    public gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilege[] getStemPrivileges(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,java.lang.String subject) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"getStemPrivileges");
        gov.nih.nci.cagrid.gridgrouper.stubs.GetStemPrivilegesRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.GetStemPrivilegesRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.GetStemPrivilegesRequestStem stemContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GetStemPrivilegesRequestStem();
        stemContainer.setStemIdentifier(stem);
        params.setStem(stemContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GetStemPrivilegesRequestSubject subjectContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GetStemPrivilegesRequestSubject();
        subjectContainer.setSubjectIdentifier(subject);
        params.setSubject(subjectContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GetStemPrivilegesResponse boxedResult = portType.getStemPrivileges(params);
        return boxedResult.getStemPrivilege();
      }
    }

}
