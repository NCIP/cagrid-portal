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
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class GridGrouperClient extends ServiceSecurityClient implements
		GridGrouperI {
	protected GridGrouperPortType portType;

	private Object portTypeMutex;

	public GridGrouperClient(String url) throws MalformedURIException,
			RemoteException {
		this(url, null);
	}

	public GridGrouperClient(String url, GlobusCredential proxy)
			throws MalformedURIException, RemoteException {
		super(url, proxy);
		initialize();
	}

	public GridGrouperClient(EndpointReferenceType epr)
			throws MalformedURIException, RemoteException {
		this(epr, null);
	}

	public GridGrouperClient(EndpointReferenceType epr, GlobusCredential proxy)
			throws MalformedURIException, RemoteException {
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
		InputStream resourceAsStream = ClassUtils.getResourceAsStream(
				getClass(), "client-config.wsdd");
		if (resourceAsStream != null) {
			// we found it, so tell axis to configure an engine to use it
			EngineConfiguration engineConfig = new FileProvider(
					resourceAsStream);
			// set the engine of the locator
			locator.setEngine(new AxisClient(engineConfig));
		}
		GridGrouperPortType port = null;
		try {
			port = locator.getGridGrouperPortTypePort(getEndpointReference());
		} catch (Exception e) {
			throw new RemoteException("Unable to locate portType:"
					+ e.getMessage(), e);
		}

		return port;
	}

	protected void configureStubSecurity(Stub stub, String method)
			throws RemoteException {

		boolean https = false;
		if (epr.getAddress().getScheme().equals("https")) {
			https = true;
		}

		if (method.equals("getServiceSecurityMetadata")) {
			if (https) {
				resetStub(stub);
				stub._setProperty(
						org.globus.wsrf.security.Constants.GSI_TRANSPORT,
						org.globus.wsrf.security.Constants.SIGNATURE);
				stub._setProperty(
						org.globus.wsrf.security.Constants.GSI_ANONYMOUS,
						Boolean.TRUE);
				stub
						._setProperty(
								org.globus.wsrf.security.Constants.AUTHORIZATION,
								org.globus.wsrf.impl.security.authorization.NoAuthorization
										.getInstance());
			}
			return;
		}

		if (this.securityMetadata == null) {
			operations = new HashMap();
			this.authorization = NoAuthorization.getInstance();
			this.securityMetadata = getServiceSecurityMetadata();
			ServiceSecurityMetadataOperations ssmo = securityMetadata
					.getOperations();
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

		CommunicationMechanism serviceDefault = securityMetadata
				.getDefaultCommunicationMechanism();

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
			ProtectionLevelType level = mechanism.getGSITransport()
					.getProtectionLevel();
			if (level != null) {
				if ((level.equals(ProtectionLevelType.privacy))
						|| (level.equals(ProtectionLevelType.either))) {
					stub._setProperty(
							org.globus.wsrf.security.Constants.GSI_TRANSPORT,
							org.globus.wsrf.security.Constants.ENCRYPTION);
				} else {
					stub._setProperty(
							org.globus.wsrf.security.Constants.GSI_TRANSPORT,
							org.globus.wsrf.security.Constants.SIGNATURE);
				}

			} else {
				stub._setProperty(
						org.globus.wsrf.security.Constants.GSI_TRANSPORT,
						org.globus.wsrf.security.Constants.SIGNATURE);
			}
			delegationAllowed = false;

		} else if (https) {
			stub._setProperty(org.globus.wsrf.security.Constants.GSI_TRANSPORT,
					org.globus.wsrf.security.Constants.SIGNATURE);
			delegationAllowed = false;
		} else if (mechanism.getGSISecureConversation() != null) {
			ProtectionLevelType level = mechanism.getGSISecureConversation()
					.getProtectionLevel();
			if (level != null) {
				if ((level.equals(ProtectionLevelType.privacy))
						|| (level.equals(ProtectionLevelType.either))) {
					stub._setProperty(
							org.globus.wsrf.security.Constants.GSI_SEC_CONV,
							org.globus.wsrf.security.Constants.ENCRYPTION);

				} else {
					stub._setProperty(
							org.globus.wsrf.security.Constants.GSI_SEC_CONV,
							org.globus.wsrf.security.Constants.SIGNATURE);
				}

			} else {
				stub._setProperty(
						org.globus.wsrf.security.Constants.GSI_SEC_CONV,
						org.globus.wsrf.security.Constants.ENCRYPTION);
			}

		} else if (mechanism.getGSISecureMessage() != null) {
			ProtectionLevelType level = mechanism.getGSISecureMessage()
					.getProtectionLevel();
			if (level != null) {
				if ((level.equals(ProtectionLevelType.privacy))
						|| (level.equals(ProtectionLevelType.either))) {
					stub._setProperty(
							org.globus.wsrf.security.Constants.GSI_SEC_MSG,
							org.globus.wsrf.security.Constants.ENCRYPTION);
				} else {
					stub._setProperty(
							org.globus.wsrf.security.Constants.GSI_SEC_MSG,
							org.globus.wsrf.security.Constants.SIGNATURE);
				}

			} else {
				stub._setProperty(
						org.globus.wsrf.security.Constants.GSI_SEC_MSG,
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
				System.out.println("Error loading default proxy: "
						+ e.getMessage());
			}

		}

		if ((credentialsAllowed) && (proxy != null)) {
			try {
				org.ietf.jgss.GSSCredential gss = new org.globus.gsi.gssapi.GlobusGSSCredentialImpl(
						proxy, org.ietf.jgss.GSSCredential.INITIATE_AND_ACCEPT);
				stub._setProperty(
						org.globus.axis.gsi.GSIConstants.GSI_CREDENTIALS, gss);
			} catch (org.ietf.jgss.GSSException ex) {
				throw new RemoteException(ex.getMessage());
			}
		} else if (anonymousAllowed) {
			stub._setProperty(org.globus.wsrf.security.Constants.GSI_ANONYMOUS,
					Boolean.TRUE);
		}

		if (authorizationAllowed) {
			if (authorization == null) {
				stub._setProperty(
						org.globus.wsrf.security.Constants.AUTHORIZATION,
						NoAuthorization.getInstance());
			} else {
				stub._setProperty(
						org.globus.wsrf.security.Constants.AUTHORIZATION,
						getAuthorization());
			}
		}
		if (delegationAllowed) {
			if (getDelegationMode() != null) {
				stub._setProperty(org.globus.axis.gsi.GSIConstants.GSI_MODE,
						getDelegationMode());
			}
		}
	}

	public String getProxyIdentity() {
		if (getProxy() != null) {
			return getProxy().getIdentity();
		} else {
			try {
				GlobusCredential cred = ProxyUtil.getDefaultProxy();
				if (cred.getTimeLeft() > 0) {
					return cred.getIdentity();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}
	}

    public java.lang.String[] getSubjectsWithGroupPrivilege(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,gov.nih.nci.cagrid.gridgrouper.bean.GroupPrivilegeType privilege) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"getSubjectsWithGroupPrivilege");
        gov.nih.nci.cagrid.gridgrouper.stubs.GetSubjectsWithGroupPrivilegeRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.GetSubjectsWithGroupPrivilegeRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.GetSubjectsWithGroupPrivilegeRequestGroup groupContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GetSubjectsWithGroupPrivilegeRequestGroup();
        groupContainer.setGroupIdentifier(group);
        params.setGroup(groupContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GetSubjectsWithGroupPrivilegeRequestPrivilege privilegeContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GetSubjectsWithGroupPrivilegeRequestPrivilege();
        privilegeContainer.setGroupPrivilegeType(privilege);
        params.setPrivilege(privilegeContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GetSubjectsWithGroupPrivilegeResponse boxedResult = portType.getSubjectsWithGroupPrivilege(params);
        return boxedResult.getSubjectIdentifier();
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
    public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor updateStem(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,gov.nih.nci.cagrid.gridgrouper.bean.StemUpdate update) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemModifyFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"updateStem");
        gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemRequestStem stemContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemRequestStem();
        stemContainer.setStemIdentifier(stem);
        params.setStem(stemContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemRequestUpdate updateContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemRequestUpdate();
        updateContainer.setStemUpdate(update);
        params.setUpdate(updateContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemResponse boxedResult = portType.updateStem(params);
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
    public void grantStemPrivilege(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,java.lang.String subject,gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType privilege) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.GrantPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.SchemaFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"grantStemPrivilege");
        gov.nih.nci.cagrid.gridgrouper.stubs.GrantStemPrivilegeRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.GrantStemPrivilegeRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.GrantStemPrivilegeRequestStem stemContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GrantStemPrivilegeRequestStem();
        stemContainer.setStemIdentifier(stem);
        params.setStem(stemContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GrantStemPrivilegeRequestSubject subjectContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GrantStemPrivilegeRequestSubject();
        subjectContainer.setSubjectIdentifier(subject);
        params.setSubject(subjectContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GrantStemPrivilegeRequestPrivilege privilegeContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GrantStemPrivilegeRequestPrivilege();
        privilegeContainer.setStemPrivilegeType(privilege);
        params.setPrivilege(privilegeContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GrantStemPrivilegeResponse boxedResult = portType.grantStemPrivilege(params);
      }
    }
    public void revokeStemPrivilege(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,java.lang.String subject,gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType privilege) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.RevokePrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.SchemaFault, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"revokeStemPrivilege");
        gov.nih.nci.cagrid.gridgrouper.stubs.RevokeStemPrivilegeRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.RevokeStemPrivilegeRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.RevokeStemPrivilegeRequestStem stemContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.RevokeStemPrivilegeRequestStem();
        stemContainer.setStemIdentifier(stem);
        params.setStem(stemContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.RevokeStemPrivilegeRequestSubject subjectContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.RevokeStemPrivilegeRequestSubject();
        subjectContainer.setSubjectIdentifier(subject);
        params.setSubject(subjectContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.RevokeStemPrivilegeRequestPrivilege privilegeContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.RevokeStemPrivilegeRequestPrivilege();
        privilegeContainer.setStemPrivilegeType(privilege);
        params.setPrivilege(privilegeContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.RevokeStemPrivilegeResponse boxedResult = portType.revokeStemPrivilege(params);
      }
    }
    public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor addChildStem(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,java.lang.String extension,java.lang.String displayExtension) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemAddFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"addChildStem");
        gov.nih.nci.cagrid.gridgrouper.stubs.AddChildStemRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.AddChildStemRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.AddChildStemRequestStem stemContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.AddChildStemRequestStem();
        stemContainer.setStemIdentifier(stem);
        params.setStem(stemContainer);
        params.setExtension(extension);
        params.setDisplayExtension(displayExtension);
        gov.nih.nci.cagrid.gridgrouper.stubs.AddChildStemResponse boxedResult = portType.addChildStem(params);
        return boxedResult.getStemDescriptor();
      }
    }
    public void deleteStem(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemDeleteFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"deleteStem");
        gov.nih.nci.cagrid.gridgrouper.stubs.DeleteStemRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.DeleteStemRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.DeleteStemRequestStem stemContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.DeleteStemRequestStem();
        stemContainer.setStemIdentifier(stem);
        params.setStem(stemContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.DeleteStemResponse boxedResult = portType.deleteStem(params);
      }
    }
    public gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor getGroup(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"getGroup");
        gov.nih.nci.cagrid.gridgrouper.stubs.GetGroupRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.GetGroupRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.GetGroupRequestGroup groupContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GetGroupRequestGroup();
        groupContainer.setGroupIdentifier(group);
        params.setGroup(groupContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GetGroupResponse boxedResult = portType.getGroup(params);
        return boxedResult.getGroupDescriptor();
      }
    }
    public gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor[] getChildGroups(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"getChildGroups");
        gov.nih.nci.cagrid.gridgrouper.stubs.GetChildGroupsRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.GetChildGroupsRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.GetChildGroupsRequestStem stemContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GetChildGroupsRequestStem();
        stemContainer.setStemIdentifier(stem);
        params.setStem(stemContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GetChildGroupsResponse boxedResult = portType.getChildGroups(params);
        return boxedResult.getGroupDescriptor();
      }
    }
    public gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor addChildGroup(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,java.lang.String extension,java.lang.String displayExtension) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupAddFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"addChildGroup");
        gov.nih.nci.cagrid.gridgrouper.stubs.AddChildGroupRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.AddChildGroupRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.AddChildGroupRequestStem stemContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.AddChildGroupRequestStem();
        stemContainer.setStemIdentifier(stem);
        params.setStem(stemContainer);
        params.setExtension(extension);
        params.setDisplayExtension(displayExtension);
        gov.nih.nci.cagrid.gridgrouper.stubs.AddChildGroupResponse boxedResult = portType.addChildGroup(params);
        return boxedResult.getGroupDescriptor();
      }
    }
    public void deleteGroup(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupDeleteFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"deleteGroup");
        gov.nih.nci.cagrid.gridgrouper.stubs.DeleteGroupRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.DeleteGroupRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.DeleteGroupRequestGroup groupContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.DeleteGroupRequestGroup();
        groupContainer.setGroupIdentifier(group);
        params.setGroup(groupContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.DeleteGroupResponse boxedResult = portType.deleteGroup(params);
      }
    }
    public gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor updateGroup(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,gov.nih.nci.cagrid.gridgrouper.bean.GroupUpdate update) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupModifyFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"updateGroup");
        gov.nih.nci.cagrid.gridgrouper.stubs.UpdateGroupRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.UpdateGroupRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.UpdateGroupRequestGroup groupContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.UpdateGroupRequestGroup();
        groupContainer.setGroupIdentifier(group);
        params.setGroup(groupContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.UpdateGroupRequestUpdate updateContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.UpdateGroupRequestUpdate();
        updateContainer.setGroupUpdate(update);
        params.setUpdate(updateContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.UpdateGroupResponse boxedResult = portType.updateGroup(params);
        return boxedResult.getGroupDescriptor();
      }
    }
    public void addMember(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,java.lang.String subject) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.MemberAddFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"addMember");
        gov.nih.nci.cagrid.gridgrouper.stubs.AddMemberRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.AddMemberRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.AddMemberRequestGroup groupContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.AddMemberRequestGroup();
        groupContainer.setGroupIdentifier(group);
        params.setGroup(groupContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.AddMemberRequestSubject subjectContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.AddMemberRequestSubject();
        subjectContainer.setSubjectIdentifier(subject);
        params.setSubject(subjectContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.AddMemberResponse boxedResult = portType.addMember(params);
      }
    }
    public gov.nih.nci.cagrid.gridgrouper.bean.MemberDescriptor[] getMembers(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,gov.nih.nci.cagrid.gridgrouper.bean.MemberFilter filter) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"getMembers");
        gov.nih.nci.cagrid.gridgrouper.stubs.GetMembersRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.GetMembersRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.GetMembersRequestGroup groupContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GetMembersRequestGroup();
        groupContainer.setGroupIdentifier(group);
        params.setGroup(groupContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GetMembersRequestFilter filterContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GetMembersRequestFilter();
        filterContainer.setMemberFilter(filter);
        params.setFilter(filterContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GetMembersResponse boxedResult = portType.getMembers(params);
        return boxedResult.getMemberDescriptor();
      }
    }
    public boolean isMemberOf(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,java.lang.String member,gov.nih.nci.cagrid.gridgrouper.bean.MemberFilter filter) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"isMemberOf");
        gov.nih.nci.cagrid.gridgrouper.stubs.IsMemberOfRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.IsMemberOfRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.IsMemberOfRequestGroup groupContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.IsMemberOfRequestGroup();
        groupContainer.setGroupIdentifier(group);
        params.setGroup(groupContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.IsMemberOfRequestMember memberContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.IsMemberOfRequestMember();
        memberContainer.setSubjectIdentifier(member);
        params.setMember(memberContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.IsMemberOfRequestFilter filterContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.IsMemberOfRequestFilter();
        filterContainer.setMemberFilter(filter);
        params.setFilter(filterContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.IsMemberOfResponse boxedResult = portType.isMemberOf(params);
        return boxedResult.isResponse();
      }
    }
    public gov.nih.nci.cagrid.gridgrouper.bean.MembershipDescriptor[] getMemberships(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,gov.nih.nci.cagrid.gridgrouper.bean.MemberFilter filter) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"getMemberships");
        gov.nih.nci.cagrid.gridgrouper.stubs.GetMembershipsRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.GetMembershipsRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.GetMembershipsRequestGroup groupContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GetMembershipsRequestGroup();
        groupContainer.setGroupIdentifier(group);
        params.setGroup(groupContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GetMembershipsRequestFilter filterContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GetMembershipsRequestFilter();
        filterContainer.setMemberFilter(filter);
        params.setFilter(filterContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GetMembershipsResponse boxedResult = portType.getMemberships(params);
        return boxedResult.getMembershipDescriptor();
      }
    }
    public void deleteMember(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,java.lang.String member) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.MemberDeleteFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"deleteMember");
        gov.nih.nci.cagrid.gridgrouper.stubs.DeleteMemberRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.DeleteMemberRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.DeleteMemberRequestGroup groupContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.DeleteMemberRequestGroup();
        groupContainer.setGroupIdentifier(group);
        params.setGroup(groupContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.DeleteMemberRequestMember memberContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.DeleteMemberRequestMember();
        memberContainer.setSubjectIdentifier(member);
        params.setMember(memberContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.DeleteMemberResponse boxedResult = portType.deleteMember(params);
      }
    }
    public gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor addCompositeMember(gov.nih.nci.cagrid.gridgrouper.bean.GroupCompositeType type,gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier composite,gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier left,gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier right) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.MemberAddFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"addCompositeMember");
        gov.nih.nci.cagrid.gridgrouper.stubs.AddCompositeMemberRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.AddCompositeMemberRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.AddCompositeMemberRequestType typeContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.AddCompositeMemberRequestType();
        typeContainer.setGroupCompositeType(type);
        params.setType(typeContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.AddCompositeMemberRequestComposite compositeContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.AddCompositeMemberRequestComposite();
        compositeContainer.setGroupIdentifier(composite);
        params.setComposite(compositeContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.AddCompositeMemberRequestLeft leftContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.AddCompositeMemberRequestLeft();
        leftContainer.setGroupIdentifier(left);
        params.setLeft(leftContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.AddCompositeMemberRequestRight rightContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.AddCompositeMemberRequestRight();
        rightContainer.setGroupIdentifier(right);
        params.setRight(rightContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.AddCompositeMemberResponse boxedResult = portType.addCompositeMember(params);
        return boxedResult.getGroupDescriptor();
      }
    }
    public gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor deleteCompositeMember(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.MemberDeleteFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"deleteCompositeMember");
        gov.nih.nci.cagrid.gridgrouper.stubs.DeleteCompositeMemberRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.DeleteCompositeMemberRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.DeleteCompositeMemberRequestGroup groupContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.DeleteCompositeMemberRequestGroup();
        groupContainer.setGroupIdentifier(group);
        params.setGroup(groupContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.DeleteCompositeMemberResponse boxedResult = portType.deleteCompositeMember(params);
        return boxedResult.getGroupDescriptor();
      }
    }
    public void grantGroupPrivilege(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,java.lang.String subject,gov.nih.nci.cagrid.gridgrouper.bean.GroupPrivilegeType privilege) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.GrantPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"grantGroupPrivilege");
        gov.nih.nci.cagrid.gridgrouper.stubs.GrantGroupPrivilegeRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.GrantGroupPrivilegeRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.GrantGroupPrivilegeRequestGroup groupContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GrantGroupPrivilegeRequestGroup();
        groupContainer.setGroupIdentifier(group);
        params.setGroup(groupContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GrantGroupPrivilegeRequestSubject subjectContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GrantGroupPrivilegeRequestSubject();
        subjectContainer.setSubjectIdentifier(subject);
        params.setSubject(subjectContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GrantGroupPrivilegeRequestPrivilege privilegeContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.GrantGroupPrivilegeRequestPrivilege();
        privilegeContainer.setGroupPrivilegeType(privilege);
        params.setPrivilege(privilegeContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.GrantGroupPrivilegeResponse boxedResult = portType.grantGroupPrivilege(params);
      }
    }
    public void revokeGroupPrivilege(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,java.lang.String subject,gov.nih.nci.cagrid.gridgrouper.bean.GroupPrivilegeType privilege) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.RevokePrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.SchemaFault {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"revokeGroupPrivilege");
        gov.nih.nci.cagrid.gridgrouper.stubs.RevokeGroupPrivilegeRequest params = new gov.nih.nci.cagrid.gridgrouper.stubs.RevokeGroupPrivilegeRequest();
        gov.nih.nci.cagrid.gridgrouper.stubs.RevokeGroupPrivilegeRequestGroup groupContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.RevokeGroupPrivilegeRequestGroup();
        groupContainer.setGroupIdentifier(group);
        params.setGroup(groupContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.RevokeGroupPrivilegeRequestSubject subjectContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.RevokeGroupPrivilegeRequestSubject();
        subjectContainer.setSubjectIdentifier(subject);
        params.setSubject(subjectContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.RevokeGroupPrivilegeRequestPrivilege privilegeContainer = new gov.nih.nci.cagrid.gridgrouper.stubs.RevokeGroupPrivilegeRequestPrivilege();
        privilegeContainer.setGroupPrivilegeType(privilege);
        params.setPrivilege(privilegeContainer);
        gov.nih.nci.cagrid.gridgrouper.stubs.RevokeGroupPrivilegeResponse boxedResult = portType.revokeGroupPrivilege(params);
      }
    }

}
