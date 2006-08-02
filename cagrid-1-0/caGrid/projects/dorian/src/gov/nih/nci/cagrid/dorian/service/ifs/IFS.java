package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dorian.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.ca.CertificateAuthorityFault;
import gov.nih.nci.cagrid.dorian.ca.DorianCertificateAuthority;
import gov.nih.nci.cagrid.dorian.common.AddressValidator;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.common.LoggingObject;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserPolicy;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserRole;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdPStatus;
import gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.stubs.InvalidProxyFault;
import gov.nih.nci.cagrid.dorian.stubs.InvalidTrustedIdPFault;
import gov.nih.nci.cagrid.dorian.stubs.InvalidUserFault;
import gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.stubs.UserPolicyFault;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLAttribute;
import gov.nih.nci.cagrid.opensaml.SAMLAttributeStatement;
import gov.nih.nci.cagrid.opensaml.SAMLAuthenticationStatement;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IFS extends LoggingObject {

	private UserManager um;

	private TrustedIdPManager tm;

	private IFSConfiguration conf;
	
	private CertificateAuthority ca;

	public IFS(IFSConfiguration conf, Database db, CertificateAuthority ca)
			throws DorianInternalFault {
		this.conf = conf;
		this.ca = ca;
		tm = new TrustedIdPManager(conf, db);
		um = new UserManager(db, conf, ca, tm);
		um.buildDatabase();
		um.publishCRL();
	}

	public IFSUserPolicy[] getUserPolicies(String callerGridIdentity)
			throws DorianInternalFault, PermissionDeniedFault {
		IFSUser caller = getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		return conf.getUserPolicies();
	}

	public String getUserIdVerifyTrustedIdP(X509Certificate idpCert,
			String identity) throws DorianInternalFault, InvalidUserFault,
			InvalidTrustedIdPFault, PermissionDeniedFault {
		if (identity == null) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("No credentials specified.");
			throw fault;
		}
		TrustedIdP idp = tm.getTrustedIdPByDN(idpCert.getSubjectDN().getName());
		IFSUser usr = um.getUser(identity);
		if (usr.getIdPId() != idp.getId()) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault
					.setFaultString("Not a valid user of the IdP "
							+ idp.getName());
			throw fault;
		}
		return usr.getUID();
	}

	public TrustedIdP addTrustedIdP(String callerGridIdentity, TrustedIdP idp)
			throws DorianInternalFault, InvalidTrustedIdPFault,
			PermissionDeniedFault {
		IFSUser caller = getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		return tm.addTrustedIdP(idp);
	}

	public void updateTrustedIdP(String callerGridIdentity, TrustedIdP idp)
			throws DorianInternalFault, InvalidTrustedIdPFault,
			PermissionDeniedFault {
		IFSUser caller = getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		TrustedIdP curr = tm.getTrustedIdPById(idp.getId());
		boolean statusChanged = false;
		if ((idp.getStatus() != null)
				&& (!idp.getStatus().equals(curr.getStatus()))) {
			statusChanged = true;
		}
		tm.updateIdP(idp);
		if (statusChanged) {
			um.publishCRL();
		}
	}

	public void removeTrustedIdP(String callerGridIdentity, long idpId)
			throws DorianInternalFault, InvalidTrustedIdPFault,
			PermissionDeniedFault {
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
			} catch (Exception e) {
				logError(e.getMessage(), e);
			}
		}
	}

	public TrustedIdP[] getTrustedIdPs(String callerGridIdentity)
			throws DorianInternalFault, PermissionDeniedFault {
		IFSUser caller = getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		return tm.getTrustedIdPs();
	}

	public IFSUser getUser(String callerGridIdentity, long idpId, String uid)
			throws DorianInternalFault, InvalidUserFault, PermissionDeniedFault {
		IFSUser caller = um.getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);

		return um.getUser(idpId, uid);
	}

	public IFSUser[] findUsers(String callerGridIdentity, IFSUserFilter filter)
			throws DorianInternalFault, PermissionDeniedFault {
		IFSUser caller = getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		return um.getUsers(filter);
	}

	public void updateUser(String callerGridIdentity, IFSUser usr)
			throws DorianInternalFault, InvalidUserFault, PermissionDeniedFault {
		IFSUser caller = um.getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);

		um.updateUser(usr);
	}

	public void removeUser(String callerGridIdentity, IFSUser usr)
			throws DorianInternalFault, InvalidUserFault, PermissionDeniedFault {
		IFSUser caller = um.getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		um.removeUser(usr);
	}

	public IFSUser renewUserCredentials(String callerGridIdentity, IFSUser usr)
			throws DorianInternalFault, InvalidUserFault, PermissionDeniedFault {
		IFSUser caller = um.getUser(callerGridIdentity);
		verifyActiveUser(caller);
		verifyAdminUser(caller);
		return um.renewUserCredentials(usr);
	}

	public X509Certificate[] createProxy(SAMLAssertion saml,
			PublicKey publicKey, ProxyLifetime lifetime)
			throws DorianInternalFault, InvalidAssertionFault,
			InvalidProxyFault, UserPolicyFault, PermissionDeniedFault {

		if (!saml.isSigned()) {
			InvalidAssertionFault fault = new InvalidAssertionFault();
			fault
					.setFaultString("The assertion specified is invalid, it MUST be signed by a trusted IdP");
			throw fault;
		}

		// Determine whether or not the assertion is expired
		Calendar cal = new GregorianCalendar();
		Date now = cal.getTime();
		if ((now.before(saml.getNotBefore()))
				|| (now.after(saml.getNotOnOrAfter()))) {
			InvalidAssertionFault fault = new InvalidAssertionFault();
			fault.setFaultString("The Assertion is not valid at " + now
					+ ", the assertion is valid from " + saml.getNotBefore()
					+ " to " + saml.getNotOnOrAfter());
			throw fault;
		}

		// Make sure the assertion is trusted
		TrustedIdP idp = tm.getTrustedIdP(saml);
		SAMLAuthenticationStatement auth = getAuthenticationStatement(saml);

		// We need to verify the authentication method now
		boolean allowed = false;
		for (int i = 0; i < idp.getAuthenticationMethod().length; i++) {
			if (idp.getAuthenticationMethod(i).getValue().equals(
					auth.getAuthMethod())) {
				allowed = true;
			}
		}
		if (!allowed) {
			InvalidAssertionFault fault = new InvalidAssertionFault();
			fault.setFaultString("The authentication method "
					+ auth.getAuthMethod() + " is not acceptable for the IdP "
					+ idp.getName());
			throw fault;
		}

		// If the user does not exist, add them
		String uid = this.getAttribute(saml, idp.getUserIdAttributeDescriptor()
				.getNamespaceURI(), idp.getUserIdAttributeDescriptor()
				.getName());
		String email = this.getAttribute(saml, idp
				.getEmailAttributeDescriptor().getNamespaceURI(), idp
				.getEmailAttributeDescriptor().getName());
		String firstName = this.getAttribute(saml, idp
				.getFirstNameAttributeDescriptor().getNamespaceURI(), idp
				.getFirstNameAttributeDescriptor().getName());
		String lastName = this.getAttribute(saml, idp
				.getLastNameAttributeDescriptor().getNamespaceURI(), idp
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
				usr.setUserRole(IFSUserRole.Non_Administrator);
				usr.setUserStatus(IFSUserStatus.Pending);
				usr = um.addUser(usr);
			} catch (Exception e) {
				logError(e.getMessage(), e);
				DorianInternalFault fault = new DorianInternalFault();
				fault
						.setFaultString("An unexpected error occurred in adding the user "
								+ usr.getUID()
								+ " from the IdP "
								+ idp.getName());
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
				throw fault;
			}
		} else {
			try {
				usr = um.getUser(idp.getId(), uid);
				boolean performUpdate = false;

				if ((usr.getFirstName() == null)
						|| (!usr.getFirstName().equals(firstName))) {
					usr.setFirstName(firstName);
					performUpdate = true;
				}
				if ((usr.getLastName() == null)
						|| (!usr.getLastName().equals(lastName))) {
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
				fault
						.setFaultString("An unexpected error occurred in obtaining the user "
								+ usr.getUID()
								+ " from the IdP "
								+ idp.getName());
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
				throw fault;
			}
		}

		// Validate that the proxy is of valid length
		if (IFSUtils.getProxyValid(lifetime).after(conf.getMaxProxyLifetime())) {
			InvalidProxyFault fault = new InvalidProxyFault();
			fault
					.setFaultString("The proxy valid length exceeds the maximum proxy valid length (hrs="
							+ conf.getMaxProxyLifetimeHours()
							+ ", mins="
							+ conf.getMaxProxyLifetimeMinutes()
							+ ", sec="
							+ conf.getMaxProxyLifetimeSeconds() + ")");
			throw fault;
		}

		// Run the policy
		UserPolicy policy = null;
		try {
			Class c = Class.forName(idp.getUserPolicyClass());
			policy = (UserPolicy) c.newInstance();
			policy.configure(conf, um);

		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault
					.setFaultString("An unexpected error occurred in creating an instance of the user policy "
							+ idp.getUserPolicyClass());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}

		policy.applyPolicy(usr);

		// Check to see if authorized
		this.verifyActiveUser(usr);

		// Check to see if the user's credentials are ok, includes checking if
		// the credentials are expired and if the proxy time is ok in regards to
		// the credentials time

		X509Certificate cert = null;

		try {
			cert = CertUtil.loadCertificate(usr.getCertificate()
					.getCertificateAsString());

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
				fault
						.setFaultString("Unexpected Error, updating the user's status");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
			}
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault
					.setFaultString("The credentials for this account have expired.");
			throw fault;

		} else if (IFSUtils.getProxyValid(lifetime).after(cert.getNotAfter())) {
			InvalidProxyFault fault = new InvalidProxyFault();
			fault
					.setFaultString("The proxy valid length exceeds the expiration date of the user's certificate.");
			throw fault;
		}

		// create the proxy

		try {
			PrivateKey key = um.getUsersPrivateKey(usr);
			X509Certificate[] certs = IFSProxyCreator
					.createImpersonationProxyCertificate(
							new X509Certificate[] { cert }, key, publicKey,
							lifetime);
			return certs;
		} catch (Exception e) {
			InvalidProxyFault fault = new InvalidProxyFault();
			fault
					.setFaultString("An unexpected error occurred in creating the user "
							+ usr.getGridId() + "'s proxy.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (InvalidProxyFault) helper.getFault();
			throw fault;
		}

	}

	private IFSUser getUser(String gridId) throws DorianInternalFault,
			PermissionDeniedFault {
		try {
			return um.getUser(gridId);
		} catch (InvalidUserFault f) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("You are NOT an Administrator!!!");
			throw fault;
		}
	}

	private void verifyAdminUser(IFSUser usr) throws DorianInternalFault,
			PermissionDeniedFault {
		if (usr.getUserRole().equals(IFSUserRole.Administrator)) {
			return;
		} else {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("You are NOT an Administrator!!!");
			throw fault;
		}
	}

	private void verifyActiveUser(IFSUser usr) throws DorianInternalFault,
			PermissionDeniedFault {

		try {
			TrustedIdP idp = this.tm.getTrustedIdPById(usr.getIdPId());

			if (!idp.getStatus().equals(TrustedIdPStatus.Active)) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault
						.setFaultString("Access for your Identity Provider has been suspended!!!");
				throw fault;
			}
		} catch (InvalidTrustedIdPFault f) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault
					.setFaultString("Unexpected error in determining your Identity Provider has been suspended!!!");
			throw fault;
		}

		if (!usr.getUserStatus().equals(IFSUserStatus.Active)) {
			if (usr.getUserStatus().equals(IFSUserStatus.Suspended)) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault.setFaultString("The account has been suspended.");
				throw fault;

			} else if (usr.getUserStatus().equals(IFSUserStatus.Rejected)) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault
						.setFaultString("The request for an account was rejected.");
				throw fault;

			} else if (usr.getUserStatus().equals(IFSUserStatus.Pending)) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault
						.setFaultString("The request for an account has not been reviewed.");
				throw fault;
			} else if (usr.getUserStatus().equals(IFSUserStatus.Expired)) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault
						.setFaultString("The credentials for this account have expired.");
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

	private String getAttribute(SAMLAssertion saml, String namespace,
			String name) throws InvalidAssertionFault {
		Iterator itr = saml.getStatements();
		while (itr.hasNext()) {
			Object o = itr.next();
			if (o instanceof SAMLAttributeStatement) {
				SAMLAttributeStatement att = (SAMLAttributeStatement) o;
				Iterator attItr = att.getAttributes();
				while (attItr.hasNext()) {
					SAMLAttribute a = (SAMLAttribute) attItr.next();
					if ((a.getNamespace().equals(namespace))
							&& (a.getName().equals(name))) {
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
		fault
				.setFaultString("The assertion does not contain the required attribute, "
						+ namespace + ":" + name);
		throw fault;
	}

	private SAMLAuthenticationStatement getAuthenticationStatement(
			SAMLAssertion saml) throws InvalidAssertionFault {
		Iterator itr = saml.getStatements();
		SAMLAuthenticationStatement auth = null;
		while (itr.hasNext()) {
			Object o = itr.next();
			if (o instanceof SAMLAuthenticationStatement) {
				if (auth != null) {
					InvalidAssertionFault fault = new InvalidAssertionFault();
					fault
							.setFaultString("The assertion specified contained more that one authentication statement.");
					throw fault;
				}
				auth = (SAMLAuthenticationStatement) o;
			}
		}
		if (auth == null) {
			InvalidAssertionFault fault = new InvalidAssertionFault();
			fault
					.setFaultString("No authentication statement specified in the assertion provided.");
			throw fault;
		}
		return auth;
	}

	public void clearDatabase() throws DorianInternalFault {
		this.um.clearDatabase();
		this.tm.clearDatabase();
		if (ca instanceof DorianCertificateAuthority) {
			try {
				((DorianCertificateAuthority) ca).clearDatabase();
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
}
