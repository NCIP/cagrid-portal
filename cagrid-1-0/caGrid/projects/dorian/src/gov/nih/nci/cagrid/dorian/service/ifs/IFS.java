package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Runner;
import gov.nih.nci.cagrid.common.ThreadManager;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dorian.common.AddressValidator;
import gov.nih.nci.cagrid.dorian.common.LoggingObject;
import gov.nih.nci.cagrid.dorian.conf.IdentityFederationConfiguration;
import gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateRecord;
import gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateRequest;
import gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateUpdate;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserPolicy;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidHostCertificateFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidHostCertificateRequestFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdPStatus;
import gov.nih.nci.cagrid.dorian.service.Database;
import gov.nih.nci.cagrid.dorian.service.PropertyManager;
import gov.nih.nci.cagrid.dorian.service.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.service.ca.CertificateAuthorityFault;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidProxyFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidTrustedIdPFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidUserFault;
import gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.stubs.types.UserPolicyFault;
import gov.nih.nci.cagrid.gridca.common.CRLEntry;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gts.client.GTSAdminClient;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLAttribute;
import gov.nih.nci.cagrid.opensaml.SAMLAttributeStatement;
import gov.nih.nci.cagrid.opensaml.SAMLAuthenticationStatement;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bouncycastle.asn1.x509.CRLReason;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IFS extends LoggingObject implements Publisher {

	private UserManager um;

	private TrustedIdPManager tm;

	private IdentityFederationConfiguration conf;

	private CertificateAuthority ca;

	private Object mutex = new Object();

	public static final String ADMINISTRATORS = "administrators";
	private Group administrators;
	private GroupManager groupManager;
	private HostCertificateManager hostManager;
	private ThreadManager threadManager;


	public IFS(IdentityFederationConfiguration conf, Database db, PropertyManager properties, CertificateAuthority ca,
		IFSDefaults defaults) throws DorianInternalFault {
		this.conf = conf;
		this.ca = ca;
		threadManager = new ThreadManager();
		tm = new TrustedIdPManager(conf, db);
		um = new UserManager(db, conf, properties, ca, tm, this, defaults);
		um.buildDatabase();
		this.groupManager = new GroupManager(db);
		if (!this.groupManager.groupExists(ADMINISTRATORS)) {
			this.groupManager.addGroup(ADMINISTRATORS);
			this.administrators = this.groupManager.getGroup(ADMINISTRATORS);
			if (defaults.getDefaultUser() != null) {
				this.administrators.addMember(defaults.getDefaultUser().getGridId());
			} else {
				logWarning("COULD NOT ADD DEFAULT USER TO ADMINISTRATORS GROUP, NO DEFAULT USER WAS FOUND!!!");
			}
		} else {
			this.administrators = this.groupManager.getGroup(ADMINISTRATORS);
		}
		this.hostManager = new HostCertificateManager(db, this.conf, ca, this);
		publishCRL();
	}


	public IFSUserPolicy[] getUserPolicies(String callerGridIdentity) throws DorianInternalFault, PermissionDeniedFault {
		IFSUser caller = getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		return tm.getAccountPolicies();
	}


	public String getUserIdVerifyTrustedIdP(X509Certificate idpCert, String identity) throws DorianInternalFault,
		InvalidUserFault, InvalidTrustedIdPFault, PermissionDeniedFault {
		if (identity == null) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("No credentials specified.");
			throw fault;
		}
		TrustedIdP idp = tm.getTrustedIdPByDN(idpCert.getSubjectDN().getName());
		IFSUser usr = um.getUser(identity);
		if (usr.getIdPId() != idp.getId()) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("Not a valid user of the IdP " + idp.getName());
			throw fault;
		}
		return usr.getUID();
	}


	public TrustedIdP addTrustedIdP(String callerGridIdentity, TrustedIdP idp) throws DorianInternalFault,
		InvalidTrustedIdPFault, PermissionDeniedFault {
		IFSUser caller = getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		return tm.addTrustedIdP(idp);
	}


	public void updateTrustedIdP(String callerGridIdentity, TrustedIdP idp) throws DorianInternalFault,
		InvalidTrustedIdPFault, PermissionDeniedFault {
		IFSUser caller = getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		TrustedIdP curr = tm.getTrustedIdPById(idp.getId());
		boolean statusChanged = false;
		if ((idp.getStatus() != null) && (!idp.getStatus().equals(curr.getStatus()))) {
			statusChanged = true;
		}
		tm.updateIdP(idp);
		if (statusChanged) {
			publishCRL();
		}
	}


	public void removeTrustedIdP(String callerGridIdentity, long idpId) throws DorianInternalFault,
		InvalidTrustedIdPFault, PermissionDeniedFault {
		IFSUser caller = getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		tm.removeTrustedIdP(idpId);
		IFSUserFilter uf = new IFSUserFilter();
		uf.setIdPId(idpId);
		IFSUser[] users = um.getUsers(uf);
		for (int i = 0; i < users.length; i++) {
			try {
				um.removeUser(users[i]);
				this.hostManager.ownerRemovedUpdateHostCertificates(users[i].getGridId());
				this.groupManager.removeUserFromAllGroups(users[i].getGridId());
			} catch (Exception e) {
				logError(e.getMessage(), e);
			}
		}
	}


	public TrustedIdP[] getTrustedIdPs(String callerGridIdentity) throws DorianInternalFault, PermissionDeniedFault {
		IFSUser caller = getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		return tm.getTrustedIdPs();
	}


	public IFSUser getUser(String callerGridIdentity, long idpId, String uid) throws DorianInternalFault,
		InvalidUserFault, PermissionDeniedFault {
		IFSUser caller = um.getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);

		return um.getUser(idpId, uid);
	}


	public IFSUser[] findUsers(String callerGridIdentity, IFSUserFilter filter) throws DorianInternalFault,
		PermissionDeniedFault {
		IFSUser caller = getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		return um.getUsers(filter);
	}


	public void updateUser(String callerGridIdentity, IFSUser usr) throws DorianInternalFault, InvalidUserFault,
		PermissionDeniedFault {
		IFSUser caller = um.getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);

		um.updateUser(usr);
	}


	public void removeUserByLocalIdIfExists(X509Certificate idpCert, String localId) throws DorianInternalFault {
		try {
			TrustedIdP idp = tm.getTrustedIdPByDN(idpCert.getSubjectDN().getName());
			IFSUser usr = um.getUser(idp.getId(), localId);
			um.removeUser(usr);
			this.hostManager.ownerRemovedUpdateHostCertificates(usr.getGridId());
			this.groupManager.removeUserFromAllGroups(usr.getGridId());
		} catch (InvalidUserFault e) {

		} catch (InvalidTrustedIdPFault f) {
			logError(f.getFaultString(), f);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An unexpected error occurred removing the grid user, the IdP "
				+ idpCert.getSubjectDN().getName() + " could not be resolved!!!");
			throw fault;
		}
	}


	public void removeUser(String callerGridIdentity, IFSUser usr) throws DorianInternalFault, InvalidUserFault,
		PermissionDeniedFault {
		IFSUser caller = um.getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		um.removeUser(usr);
		this.hostManager.ownerRemovedUpdateHostCertificates(usr.getGridId());
		this.groupManager.removeUserFromAllGroups(usr.getGridId());
	}


	public IFSUser renewUserCredentials(String callerGridIdentity, IFSUser usr) throws DorianInternalFault,
		InvalidUserFault, PermissionDeniedFault {
		try {
			IFSUser caller = um.getUser(callerGridIdentity);
			verifyActiveUser(caller);
			verifyAdminUser(caller);
			return um.renewUserCredentials(tm.getTrustedIdPById(usr.getIdPId()), usr);
		} catch (InvalidTrustedIdPFault f) {
			logError(f.getFaultString(), f);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An unexpected error occurred renewing the user's credentials.");
			throw fault;
		}
	}


	public void addAdmin(String callerGridIdentity, String gridIdentity) throws RemoteException, DorianInternalFault,
		PermissionDeniedFault {
		IFSUser caller = getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		if (!this.administrators.isMember(gridIdentity)) {
			IFSUser admin = getUser(gridIdentity);
			verifyActiveUser(admin);
			this.administrators.addMember(gridIdentity);
		}
	}


	public void removeAdmin(String callerGridIdentity, String gridIdentity) throws RemoteException,
		DorianInternalFault, PermissionDeniedFault {
		IFSUser caller = getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		this.administrators.removeMember(gridIdentity);
	}


	public String[] getAdmins(String callerGridIdentity) throws RemoteException, DorianInternalFault,
		PermissionDeniedFault {
		IFSUser caller = getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		List members = this.administrators.getMembers();
		String[] admins = new String[members.size()];
		for (int i = 0; i < members.size(); i++) {
			admins[i] = (String) members.get(i);
		}
		return admins;
	}


	public X509Certificate[] createProxy(SAMLAssertion saml, PublicKey publicKey, ProxyLifetime lifetime,
		int delegationPathLength) throws DorianInternalFault, InvalidAssertionFault, InvalidProxyFault,
		UserPolicyFault, PermissionDeniedFault {

		if (!saml.isSigned()) {
			InvalidAssertionFault fault = new InvalidAssertionFault();
			fault.setFaultString("The assertion specified is invalid, it MUST be signed by a trusted IdP");
			throw fault;
		}

		// Determine whether or not the assertion is expired
		Calendar cal = new GregorianCalendar();
		Date now = cal.getTime();
		if ((now.before(saml.getNotBefore())) || (now.after(saml.getNotOnOrAfter()))) {
			InvalidAssertionFault fault = new InvalidAssertionFault();
			fault.setFaultString("The Assertion is not valid at " + now + ", the assertion is valid from "
				+ saml.getNotBefore() + " to " + saml.getNotOnOrAfter());
			throw fault;
		}

		// Make sure the assertion is trusted
		TrustedIdP idp = tm.getTrustedIdP(saml);
		SAMLAuthenticationStatement auth = getAuthenticationStatement(saml);

		// We need to verify the authentication method now
		boolean allowed = false;
		for (int i = 0; i < idp.getAuthenticationMethod().length; i++) {
			if (idp.getAuthenticationMethod(i).getValue().equals(auth.getAuthMethod())) {
				allowed = true;
			}
		}
		if (!allowed) {
			InvalidAssertionFault fault = new InvalidAssertionFault();
			fault.setFaultString("The authentication method " + auth.getAuthMethod()
				+ " is not acceptable for the IdP " + idp.getName());
			throw fault;
		}

		// If the user does not exist, add them
		String uid = this.getAttribute(saml, idp.getUserIdAttributeDescriptor().getNamespaceURI(), idp
			.getUserIdAttributeDescriptor().getName());
		String email = this.getAttribute(saml, idp.getEmailAttributeDescriptor().getNamespaceURI(), idp
			.getEmailAttributeDescriptor().getName());
		String firstName = this.getAttribute(saml, idp.getFirstNameAttributeDescriptor().getNamespaceURI(), idp
			.getFirstNameAttributeDescriptor().getName());
		String lastName = this.getAttribute(saml, idp.getLastNameAttributeDescriptor().getNamespaceURI(), idp
			.getLastNameAttributeDescriptor().getName());

		AddressValidator.validateEmail(email);

		IFSUser usr = null;
		if (!um.determineIfUserExists(idp.getId(), uid)) {
			try {
				usr = new IFSUser();
				usr.setIdPId(idp.getId());
				usr.setUID(uid);
				usr.setFirstName(firstName);
				usr.setLastName(lastName);
				usr.setEmail(email);
				usr.setUserStatus(IFSUserStatus.Pending);
				usr = um.addUser(idp, usr);
			} catch (Exception e) {
				logError(e.getMessage(), e);
				DorianInternalFault fault = new DorianInternalFault();
				fault.setFaultString("An unexpected error occurred in adding the user " + usr.getUID()
					+ " from the IdP " + idp.getName());
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
				throw fault;
			}
		} else {
			try {
				usr = um.getUser(idp.getId(), uid);
				boolean performUpdate = false;

				if ((usr.getFirstName() == null) || (!usr.getFirstName().equals(firstName))) {
					usr.setFirstName(firstName);
					performUpdate = true;
				}
				if ((usr.getLastName() == null) || (!usr.getLastName().equals(lastName))) {
					usr.setLastName(lastName);
					performUpdate = true;
				}
				if ((usr.getEmail() == null) || (!usr.getEmail().equals(email))) {
					usr.setEmail(email);
					performUpdate = true;
				}
				if (performUpdate) {
					um.updateUser(usr);
				}

			} catch (Exception e) {
				logError(e.getMessage(), e);
				DorianInternalFault fault = new DorianInternalFault();
				fault.setFaultString("An unexpected error occurred in obtaining the user " + usr.getUID()
					+ " from the IdP " + idp.getName());
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
				throw fault;
			}
		}

		// Validate that the proxy is of valid length

		if (IFSUtils.getProxyValid(lifetime).after(IFSUtils.getMaxProxyLifetime(conf))) {
			InvalidProxyFault fault = new InvalidProxyFault();
			fault.setFaultString("The proxy valid length exceeds the maximum proxy valid length (hrs="
				+ conf.getProxyPolicy().getProxyLifetime().getHours() + ", mins="
				+ conf.getProxyPolicy().getProxyLifetime().getMinutes() + ", sec="
				+ conf.getProxyPolicy().getProxyLifetime().getSeconds() + ")");
			throw fault;
		}

		// Run the policy
		AccountPolicy policy = null;
		try {
			Class c = Class.forName(idp.getUserPolicyClass());
			policy = (AccountPolicy) c.newInstance();
			policy.configure(conf, um);

		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An unexpected error occurred in creating an instance of the user policy "
				+ idp.getUserPolicyClass());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
		policy.applyPolicy(idp, usr);

		// Check to see if authorized
		this.verifyActiveUser(usr);

		// Check to see if the user's credentials are ok, includes checking if
		// the credentials are expired and if the proxy time is ok in regards to
		// the credentials time

		X509Certificate cert = null;

		try {
			cert = CertUtil.loadCertificate(usr.getCertificate().getCertificateAsString());

		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error loading the user's credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
		}

		if (CertUtil.isExpired(cert)) {

			usr.setUserStatus(IFSUserStatus.Expired);
			try {
				um.updateUser(usr);
			} catch (Exception e) {
				DorianInternalFault fault = new DorianInternalFault();
				fault.setFaultString("Unexpected Error, updating the user's status");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
			}

			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("The credentials for this account have expired.");
			throw fault;

		} else if (IFSUtils.getProxyValid(lifetime).after(cert.getNotAfter())) {
			InvalidProxyFault fault = new InvalidProxyFault();
			fault.setFaultString("The proxy valid length exceeds the expiration date of the user's certificate.");
			throw fault;
		}

		// create the proxy

		try {
			X509Certificate[] certs = ca.createImpersonationProxyCertificate(um.getCredentialsManagerUID(
				usr.getIdPId(), usr.getUID()), null, publicKey, lifetime, delegationPathLength);
			return certs;
		} catch (Exception e) {
			InvalidProxyFault fault = new InvalidProxyFault();
			fault.setFaultString("An unexpected error occurred in creating the user " + usr.getGridId() + "'s proxy.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (InvalidProxyFault) helper.getFault();
			throw fault;
		}

	}


	// ///////////////////////////////
	/* HOST CERTIFICATE OPERATIONS */
	// ///////////////////////////////
	public HostCertificateRecord requestHostCertificate(String callerGridId, HostCertificateRequest req)
		throws DorianInternalFault, InvalidHostCertificateRequestFault, InvalidHostCertificateFault,
		PermissionDeniedFault {
		IFSUser caller = getUser(callerGridId);
		verifyActiveUser(caller);
		long id = hostManager.requestHostCertifcate(callerGridId, req);
		HostCertificateRecord record = null;
		if (this.conf.getCredentialPolicy().isHostCertificateAutoApproval()) {
			record = hostManager.approveHostCertifcate(id);
		} else {
			record = hostManager.getHostCertificateRecord(id);
		}
		return record;
	}


	public HostCertificateRecord[] getHostCertificatesForCaller(String callerGridId) throws DorianInternalFault,
		PermissionDeniedFault {
		IFSUser caller = getUser(callerGridId);
		verifyActiveUser(caller);
		List<HostCertificateRecord> list = hostManager.getHostCertificateRecords(callerGridId);
		HostCertificateRecord[] records = new HostCertificateRecord[list.size()];
		for (int i = 0; i < list.size(); i++) {
			records[i] = list.get(i);
		}

		return records;
	}


	public HostCertificateRecord approveHostCertificate(String callerGridId, long recordId) throws DorianInternalFault,
		InvalidHostCertificateFault, PermissionDeniedFault {
		IFSUser caller = getUser(callerGridId);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		return hostManager.approveHostCertifcate(recordId);
	}


	public HostCertificateRecord[] findHostCertificates(String callerGridId, HostCertificateFilter f)
		throws DorianInternalFault, PermissionDeniedFault {
		IFSUser caller = getUser(callerGridId);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		List<HostCertificateRecord> list = hostManager.findHostCertificates(f);
		HostCertificateRecord[] records = new HostCertificateRecord[list.size()];
		for (int i = 0; i < list.size(); i++) {
			records[i] = list.get(i);
		}
		return records;
	}


	public void updateHostCertificateRecord(String callerGridId, HostCertificateUpdate update)
		throws DorianInternalFault, InvalidHostCertificateFault, PermissionDeniedFault {
		IFSUser caller = getUser(callerGridId);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		hostManager.updateHostCertificateRecord(update);
	}


	public void renewHostCertificate(String callerGridId, long recordId) throws DorianInternalFault,
		InvalidHostCertificateFault, PermissionDeniedFault {
		IFSUser caller = getUser(callerGridId);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		hostManager.renewHostCertificate(recordId);
	}


	public void publishCRL() {
		if (conf.getCRLPublish() != null) {
			if ((conf.getCRLPublish().getGts() != null) && (conf.getCRLPublish().getGts().length > 0)) {
				Runner runner = new Runner() {
					public void execute() {
						synchronized (mutex) {
							String[] services = conf.getCRLPublish().getGts();
							if ((services != null) && (services.length > 0)) {
								try {
									X509CRL crl = getCRL();
									gov.nih.nci.cagrid.gts.bean.X509CRL x509 = new gov.nih.nci.cagrid.gts.bean.X509CRL();
									x509.setCrlEncodedString(CertUtil.writeCRL(crl));
									String authName = ca.getCACertificate().getSubjectDN().getName();
									for (int i = 0; i < services.length; i++) {
										String uri = services[i];
										try {
											debug("Publishing CRL to the GTS " + uri);
											GTSAdminClient client = new GTSAdminClient(uri, null);
											client.updateCRL(authName, x509);
											debug("Published CRL to the GTS " + uri);
										} catch (Exception ex) {
											getLog().error("Error publishing the CRL to the GTS " + uri + "!!!", ex);
										}

									}

								} catch (Exception e) {
									getLog().error("Unexpected Error publishing the CRL!!!", e);
								}
							}
						}
					}
				};
				try {
					threadManager.executeInBackground(runner);
				} catch (Exception t) {
					t.getMessage();
				}
			}
		}
	}


	public X509CRL getCRL() throws DorianInternalFault {

		Map<String, DisabledUser> users = this.um.getDisabledUsers();
		Map<Long, CRLEntry> list = new HashMap<Long, CRLEntry>();

		Iterator<DisabledUser> itr = users.values().iterator();
		while (itr.hasNext()) {
			DisabledUser usr = itr.next();
			Long sn = new Long(usr.getSerialNumber());
			if (!list.containsKey(sn)) {
				CRLEntry entry = new CRLEntry(BigInteger.valueOf(sn.longValue()), usr.getCRLReason());
				list.put(sn, entry);
			}
			List<Long> hostCerts = this.hostManager.getHostCertificateRecordsSerialNumbers(usr.getGridIdentity());
			for (int i = 0; i < hostCerts.size(); i++) {
				if (!list.containsKey(hostCerts.get(i))) {
					CRLEntry entry = new CRLEntry(BigInteger.valueOf(hostCerts.get(i).longValue()), usr.getCRLReason());
					list.put(hostCerts.get(i), entry);
				}
			}
		}

		List<Long> hosts = this.hostManager.getDisabledHostCertificatesSerialNumbers();
		for (int i = 0; i < hosts.size(); i++) {
			if (!list.containsKey(hosts.get(i))) {
				CRLEntry entry = new CRLEntry(BigInteger.valueOf(hosts.get(i).longValue()),
					CRLReason.PRIVILEGE_WITHDRAWN);
				list.put(hosts.get(i), entry);
			}
		}

		CRLEntry[] entries = new CRLEntry[list.size()];
		Iterator<CRLEntry> itr2 = list.values().iterator();
		int count = 0;
		while (itr2.hasNext()) {
			entries[count] = itr2.next();
			count++;
		}
		try {
			X509CRL crl = ca.getCRL(entries);
			return crl;

		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected error obtaining the CRL.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addDescription(Utils.getExceptionMessage(e));
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}

	}


	private IFSUser getUser(String gridId) throws DorianInternalFault, PermissionDeniedFault {
		try {
			return um.getUser(gridId);
		} catch (InvalidUserFault f) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("You are NOT an Administrator!!!");
			throw fault;
		}
	}


	private void verifyAdminUser(IFSUser usr) throws DorianInternalFault, PermissionDeniedFault {
		if (administrators.isMember(usr.getGridId())) {
			return;
		} else {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("You are NOT an Administrator!!!");
			throw fault;
		}
	}


	private void verifyActiveUser(IFSUser usr) throws DorianInternalFault, PermissionDeniedFault {

		try {
			TrustedIdP idp = this.tm.getTrustedIdPById(usr.getIdPId());

			if (!idp.getStatus().equals(TrustedIdPStatus.Active)) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault.setFaultString("Access for your Identity Provider has been suspended!!!");
				throw fault;
			}
		} catch (InvalidTrustedIdPFault f) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("Unexpected error in determining your Identity Provider has been suspended!!!");
			throw fault;
		}

		if (!usr.getUserStatus().equals(IFSUserStatus.Active)) {
			if (usr.getUserStatus().equals(IFSUserStatus.Suspended)) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault.setFaultString("The account has been suspended.");
				throw fault;

			} else if (usr.getUserStatus().equals(IFSUserStatus.Rejected)) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault.setFaultString("The request for an account was rejected.");
				throw fault;

			} else if (usr.getUserStatus().equals(IFSUserStatus.Pending)) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault.setFaultString("The request for an account has not been reviewed.");
				throw fault;
			} else if (usr.getUserStatus().equals(IFSUserStatus.Expired)) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault.setFaultString("The credentials for this account have expired.");
				throw fault;
			} else {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault.setFaultString("Unknown Reason");
				throw fault;
			}
		}

	}


	protected UserManager getUserManager() {
		return um;
	}


	private String getAttribute(SAMLAssertion saml, String namespace, String name) throws InvalidAssertionFault {
		Iterator itr = saml.getStatements();
		while (itr.hasNext()) {
			Object o = itr.next();
			if (o instanceof SAMLAttributeStatement) {
				SAMLAttributeStatement att = (SAMLAttributeStatement) o;
				Iterator attItr = att.getAttributes();
				while (attItr.hasNext()) {
					SAMLAttribute a = (SAMLAttribute) attItr.next();
					if ((a.getNamespace().equals(namespace)) && (a.getName().equals(name))) {
						Iterator vals = a.getValues();
						while (vals.hasNext()) {

							String val = Utils.clean((String) vals.next());
							if (val != null) {
								return val;
							}
						}
					}
				}
			}
		}
		InvalidAssertionFault fault = new InvalidAssertionFault();
		fault.setFaultString("The assertion does not contain the required attribute, " + namespace + ":" + name);
		throw fault;
	}


	private SAMLAuthenticationStatement getAuthenticationStatement(SAMLAssertion saml) throws InvalidAssertionFault {
		Iterator itr = saml.getStatements();
		SAMLAuthenticationStatement auth = null;
		while (itr.hasNext()) {
			Object o = itr.next();
			if (o instanceof SAMLAuthenticationStatement) {
				if (auth != null) {
					InvalidAssertionFault fault = new InvalidAssertionFault();
					fault.setFaultString("The assertion specified contained more that one authentication statement.");
					throw fault;
				}
				auth = (SAMLAuthenticationStatement) o;
			}
		}
		if (auth == null) {
			InvalidAssertionFault fault = new InvalidAssertionFault();
			fault.setFaultString("No authentication statement specified in the assertion provided.");
			throw fault;
		}
		return auth;
	}


	public void clearDatabase() throws DorianInternalFault {
		this.um.clearDatabase();
		this.tm.clearDatabase();
		this.groupManager.clearDatabase();
		this.hostManager.clearDatabase();
		try {
			ca.clearCertificateAuthority();
		} catch (CertificateAuthorityFault e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString(e.getFaultString());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}

	}
}
