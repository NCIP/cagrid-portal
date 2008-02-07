package gov.nih.nci.cagrid.dorian.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.common.LoggingObject;
import gov.nih.nci.cagrid.dorian.common.SAMLConstants;
import gov.nih.nci.cagrid.dorian.conf.CertificateAuthorityType;
import gov.nih.nci.cagrid.dorian.conf.DorianConfiguration;
import gov.nih.nci.cagrid.dorian.conf.IdentityFederationConfiguration;
import gov.nih.nci.cagrid.dorian.idp.bean.Application;
import gov.nih.nci.cagrid.dorian.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUser;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateRecord;
import gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateRequest;
import gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateUpdate;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserPolicy;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.dorian.ifs.bean.SAMLAttributeDescriptor;
import gov.nih.nci.cagrid.dorian.ifs.bean.SAMLAuthenticationMethod;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdPStatus;
import gov.nih.nci.cagrid.dorian.service.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.service.ca.DBCertificateAuthority;
import gov.nih.nci.cagrid.dorian.service.ca.EracomCertificateAuthority;
import gov.nih.nci.cagrid.dorian.service.ca.EracomWrappingCertificateAuthority;
import gov.nih.nci.cagrid.dorian.service.idp.IdentityProvider;
import gov.nih.nci.cagrid.dorian.service.idp.UserManager;
import gov.nih.nci.cagrid.dorian.service.ifs.AutoApprovalAutoRenewalPolicy;
import gov.nih.nci.cagrid.dorian.service.ifs.IFS;
import gov.nih.nci.cagrid.dorian.service.ifs.IFSDefaults;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidHostCertificateFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidHostCertificateRequestFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidProxyFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidTrustedIdPFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidUserFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.dorian.stubs.types.NoSuchUserFault;
import gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.stubs.types.UserPolicyFault;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.cagrid.tools.database.Database;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class Dorian extends LoggingObject {

	private Database db;

	public static final String IDP_ADMIN_USER_ID = "dorian";

	public static final String IDP_ADMIN_PASSWORD = "DorianAdmin$1";

	private CertificateAuthority ca;

	private IdentityProvider identityProvider;

	private IFS ifs;

	private IdentityFederationConfiguration ifsConfiguration;

	private DorianConfiguration configuration;

	private PropertyManager properties;

	public Dorian(DorianConfiguration conf, String serviceId)
			throws DorianInternalFault {
		this(conf, serviceId, false);
	}

	public Dorian(DorianConfiguration conf, String serviceId, boolean ignoreCRL)
			throws DorianInternalFault {
		try {

			this.configuration = conf;
			UserManager.ADMIN_USER_ID = IDP_ADMIN_USER_ID;
			UserManager.ADMIN_PASSWORD = IDP_ADMIN_PASSWORD;
			this.db = new Database(conf.getDatabaseConfiguration(),
					getConfiguration().getDorianInternalId());
			this.db.createDatabaseIfNeeded();
			this.properties = new PropertyManager(this.db);

			if (this.properties.getCertificateAuthorityType() == null) {
				this.properties.setCertificateAuthorityType(configuration
						.getDorianCAConfiguration()
						.getCertificateAuthorityType());
			} else if (!this.properties.getCertificateAuthorityType().equals(
					configuration.getDorianCAConfiguration()
							.getCertificateAuthorityType())) {
				DorianInternalFault fault = new DorianInternalFault();
				fault
						.setFaultString("Certificate Authority type conflict detected, this Dorian was created using a "
								+ this.properties.getCertificateAuthorityType()
										.getValue()
								+ " CA but the configuration file specifies the usage of a "
								+ configuration.getDorianCAConfiguration()
										.getCertificateAuthorityType()
										.getValue() + " CA.");
				throw fault;
			}

			if (configuration.getDorianCAConfiguration()
					.getCertificateAuthorityType().equals(
							CertificateAuthorityType.Eracom)) {
				this.ca = new EracomCertificateAuthority(configuration
						.getDorianCAConfiguration());
			} else if (configuration.getDorianCAConfiguration()
					.getCertificateAuthorityType().equals(
							CertificateAuthorityType.EracomHybrid)) {
				this.ca = new EracomWrappingCertificateAuthority(db,
						configuration.getDorianCAConfiguration());
			} else {
				this.ca = new DBCertificateAuthority(db, configuration
						.getDorianCAConfiguration());
			}

			this.identityProvider = new IdentityProvider(configuration
					.getIdentityProviderConfiguration(), db, ca);

			TrustedIdP idp = new TrustedIdP();
			idp.setName(conf.getIdentityProviderConfiguration()
					.getIdentityProviderName());
			SAMLAuthenticationMethod[] methods = new SAMLAuthenticationMethod[1];
			methods[0] = SAMLAuthenticationMethod
					.fromString("urn:oasis:names:tc:SAML:1.0:am:password");
			idp.setAuthenticationMethod(methods);
			idp.setUserPolicyClass(AutoApprovalAutoRenewalPolicy.class
					.getName());
			idp
					.setIdPCertificate(CertUtil
							.writeCertificate(this.identityProvider
									.getIdPCertificate()));
			idp.setStatus(TrustedIdPStatus.Active);
			SAMLAttributeDescriptor uid = new SAMLAttributeDescriptor();
			uid.setNamespaceURI(SAMLConstants.UID_ATTRIBUTE_NAMESPACE);
			uid.setName(SAMLConstants.UID_ATTRIBUTE);
			idp.setUserIdAttributeDescriptor(uid);

			SAMLAttributeDescriptor firstName = new SAMLAttributeDescriptor();
			firstName
					.setNamespaceURI(SAMLConstants.FIRST_NAME_ATTRIBUTE_NAMESPACE);
			firstName.setName(SAMLConstants.FIRST_NAME_ATTRIBUTE);
			idp.setFirstNameAttributeDescriptor(firstName);

			SAMLAttributeDescriptor lastName = new SAMLAttributeDescriptor();
			lastName
					.setNamespaceURI(SAMLConstants.LAST_NAME_ATTRIBUTE_NAMESPACE);
			lastName.setName(SAMLConstants.LAST_NAME_ATTRIBUTE);
			idp.setLastNameAttributeDescriptor(lastName);

			SAMLAttributeDescriptor email = new SAMLAttributeDescriptor();
			email.setNamespaceURI(SAMLConstants.EMAIL_ATTRIBUTE_NAMESPACE);
			email.setName(SAMLConstants.EMAIL_ATTRIBUTE);
			idp.setEmailAttributeDescriptor(email);

			IFSUser usr = null;
			try {
				IdPUser idpUsr = identityProvider.getUser(IDP_ADMIN_USER_ID,
						IDP_ADMIN_USER_ID);
				usr = new IFSUser();
				usr.setUID(idpUsr.getUserId());
				usr.setFirstName(idpUsr.getFirstName());
				usr.setLastName(idpUsr.getLastName());
				usr.setEmail(idpUsr.getEmail());
				usr.setUserStatus(IFSUserStatus.Active);
			} catch (Exception e) {
			}

			ifsConfiguration = configuration
					.getIdentityFederationConfiguration();
			IFSDefaults defaults = new IFSDefaults(idp, usr);
			this.ifs = new IFS(ifsConfiguration, db, properties, ca, defaults,
					ignoreCRL);

			if (this.properties.getVersion() != PropertyManager.CURRENT_VERSION) {
				DorianInternalFault fault = new DorianInternalFault();
				fault
						.setFaultString("Version conflict detected, your are running Dorian "
								+ PropertyManager.CURRENT_VERSION
								+ " against a Dorian "
								+ properties.getVersion() + " database.");
				throw fault;
			}

		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault
					.setFaultString("An unexpected error occurred in configuring the service.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}

	public DorianConfiguration getConfiguration() {
		return configuration;
	}

	public Database getDatabase() {
		return this.db;
	}

	public X509Certificate getCACertificate() throws DorianInternalFault {
		try {
			return this.ca.getCACertificate();
		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault
					.setFaultString("An unexpected error occurred, in obtaining the CA certificate.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}

	public X509Certificate getIdPCertificate() throws DorianInternalFault {
		return identityProvider.getIdPCertificate();
	}

	public void changeIdPUserPassword(BasicAuthCredential credential,
			String newPassword) throws DorianInternalFault,
			PermissionDeniedFault, InvalidUserPropertyFault {
		this.identityProvider.changePassword(credential, newPassword);
	}

	public IdPUser[] findIdPUsers(String gridIdentity, IdPUserFilter filter)
			throws DorianInternalFault, PermissionDeniedFault {
		String uid = null;
		try {
			uid = ifs.getUserIdVerifyTrustedIdP(identityProvider
					.getIdPCertificate(), gridIdentity);
		} catch (Exception e) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("Invalid IdP User.");
			throw fault;
		}
		return this.identityProvider.findUsers(uid, filter);
	}

	public void updateIdPUser(String gridIdentity, IdPUser u)
			throws DorianInternalFault, PermissionDeniedFault, NoSuchUserFault,
			InvalidUserPropertyFault {
		String uid = null;
		try {
			uid = ifs.getUserIdVerifyTrustedIdP(identityProvider
					.getIdPCertificate(), gridIdentity);
		} catch (Exception e) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("Invalid IdP User.");
			throw fault;
		}
		this.identityProvider.updateUser(uid, u);
	}

	public void removeIdPUser(String gridIdentity, String userId)
			throws DorianInternalFault, PermissionDeniedFault {
		String uid = null;
		try {
			uid = ifs.getUserIdVerifyTrustedIdP(identityProvider
					.getIdPCertificate(), gridIdentity);
		} catch (Exception e) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("Invalid IdP User.");
			throw fault;
		}
		this.identityProvider.removeUser(uid, userId);
		this.ifs.removeUserByLocalIdIfExists(identityProvider
				.getIdPCertificate(), userId);
	}

	public SAMLAssertion authenticate(BasicAuthCredential credential)
			throws DorianInternalFault, PermissionDeniedFault {
		return this.identityProvider.authenticate(credential);
	}

	public String registerWithIdP(Application a) throws DorianInternalFault,
			InvalidUserPropertyFault {
		return this.identityProvider.register(a);
	}

	/** *************** IFS FUNCTIONS ********************** */

	public IFSUserPolicy[] getIFSUserPolicies(String callerGridIdentity)
			throws DorianInternalFault, PermissionDeniedFault {
		return ifs.getUserPolicies(callerGridIdentity);
	}

	public X509Certificate[] createProxy(SAMLAssertion saml,
			PublicKey publicKey, ProxyLifetime lifetime,
			int delegationPathLength) throws DorianInternalFault,
			InvalidAssertionFault, InvalidProxyFault, UserPolicyFault,
			PermissionDeniedFault {
		return this.ifs.createProxy(saml, publicKey, lifetime,
				delegationPathLength);
	}

	public TrustedIdP[] getTrustedIdPs(String callerGridIdentity)
			throws DorianInternalFault, PermissionDeniedFault {
		return ifs.getTrustedIdPs(callerGridIdentity);
	}

	public TrustedIdP addTrustedIdP(String callerGridIdentity, TrustedIdP idp)
			throws DorianInternalFault, InvalidTrustedIdPFault,
			PermissionDeniedFault {
		return ifs.addTrustedIdP(callerGridIdentity, idp);
	}

	public void updateTrustedIdP(String callerGridIdentity, TrustedIdP idp)
			throws DorianInternalFault, InvalidTrustedIdPFault,
			PermissionDeniedFault {
		ifs.updateTrustedIdP(callerGridIdentity, idp);
	}

	public void removeTrustedIdP(String callerGridIdentity, TrustedIdP idp)
			throws DorianInternalFault, InvalidTrustedIdPFault,
			PermissionDeniedFault {
		ifs.removeTrustedIdP(callerGridIdentity, idp.getId());
	}

	public IFSUser[] findIFSUsers(String callerGridIdentity,
			IFSUserFilter filter) throws DorianInternalFault,
			PermissionDeniedFault {
		return ifs.findUsers(callerGridIdentity, filter);
	}

	public void updateIFSUser(String callerGridIdentity, IFSUser usr)
			throws DorianInternalFault, InvalidUserFault, PermissionDeniedFault {
		ifs.updateUser(callerGridIdentity, usr);
	}

	public void removeIFSUser(String callerGridIdentity, IFSUser usr)
			throws DorianInternalFault, InvalidUserFault, PermissionDeniedFault {
		ifs.removeUser(callerGridIdentity, usr);
	}

	public IFSUser renewIFSUserCredentials(String callerGridIdentity,
			IFSUser usr) throws DorianInternalFault, InvalidUserFault,
			PermissionDeniedFault {
		return ifs.renewUserCredentials(callerGridIdentity, usr);
	}

	public void addAdmin(String callerGridIdentity, String gridIdentity)
			throws RemoteException, DorianInternalFault, PermissionDeniedFault {
		ifs.addAdmin(callerGridIdentity, gridIdentity);
	}

	public void removeAdmin(String callerGridIdentity, String gridIdentity)
			throws RemoteException, DorianInternalFault, PermissionDeniedFault {
		ifs.removeAdmin(callerGridIdentity, gridIdentity);
	}

	public String[] getAdmins(String callerGridIdentity)
			throws RemoteException, DorianInternalFault, PermissionDeniedFault {
		return ifs.getAdmins(callerGridIdentity);
	}

	public HostCertificateRecord requestHostCertificate(String callerGridId,
			HostCertificateRequest req) throws DorianInternalFault,
			InvalidHostCertificateRequestFault, InvalidHostCertificateFault,
			PermissionDeniedFault {
		return ifs.requestHostCertificate(callerGridId, req);
	}

	public HostCertificateRecord[] getOwnedHostCertificates(String callerGridId)
			throws DorianInternalFault, PermissionDeniedFault {
		return ifs.getHostCertificatesForCaller(callerGridId);

	}

	public HostCertificateRecord approveHostCertificate(String callerGridId,
			long recordId) throws DorianInternalFault,
			InvalidHostCertificateFault, PermissionDeniedFault {
		return ifs.approveHostCertificate(callerGridId, recordId);
	}

	public HostCertificateRecord[] findHostCertificates(String callerGridId,
			HostCertificateFilter hostCertificateFilter)
			throws DorianInternalFault, PermissionDeniedFault {
		return ifs.findHostCertificates(callerGridId, hostCertificateFilter);
	}

	public void updateHostCertificateRecord(String callerGridId,
			HostCertificateUpdate update) throws DorianInternalFault,
			InvalidHostCertificateFault, PermissionDeniedFault {
		ifs.updateHostCertificateRecord(callerGridId, update);
	}

	public HostCertificateRecord renewHostCertificate(String callerGridId,
			long recordId) throws DorianInternalFault,
			InvalidHostCertificateFault, PermissionDeniedFault {
		return ifs.renewHostCertificate(callerGridId, recordId);
	}

	public void clearDatabase() throws DorianInternalFault {
		this.identityProvider.clearDatabase();
		this.ifs.clearDatabase();
	}

}
