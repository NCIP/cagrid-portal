package gov.nih.nci.cagrid.gums.idp;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.ca.CertificateAuthority;
import gov.nih.nci.cagrid.gums.common.Crypt;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.GUMSObject;
import gov.nih.nci.cagrid.gums.idp.bean.Application;
import gov.nih.nci.cagrid.gums.idp.bean.ApplicationReview;
import gov.nih.nci.cagrid.gums.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.gums.idp.bean.CountryCode;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUser;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUserRole;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUserStatus;
import gov.nih.nci.cagrid.gums.idp.bean.InvalidLoginFault;
import gov.nih.nci.cagrid.gums.idp.bean.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.gums.idp.bean.NoSuchUserFault;
import gov.nih.nci.cagrid.gums.idp.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.gums.idp.bean.StateCode;

import java.security.cert.X509Certificate;

import org.globus.wsrf.utils.FaultHelper;
import org.opensaml.SAMLAssertion;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class IdentityProvider extends GUMSObject {

	private UserManager userManager;

	public static String ADMIN_USER_ID = "gums";

	public static String ADMIN_PASSWORD = "password";

	private IdPConfiguration conf;

	private AssertionCredentialsManager assertionManager;

	public IdentityProvider(IdPConfiguration conf, Database db,
			CertificateAuthority ca) throws GUMSInternalFault {
		try {
			this.conf = conf;
			this.userManager = new UserManager(db, conf);
			this.assertionManager = new AssertionCredentialsManager(conf, ca,
					db);

			if (!this.userManager.userExists(ADMIN_USER_ID)) {
				IdPUser u = new IdPUser();
				u.setUserId(ADMIN_USER_ID);
				u.setPassword(ADMIN_PASSWORD);
				u.setEmail("gums@gums.org");
				u.setFirstName("Mr.");
				u.setLastName("Administrator");
				u.setOrganization("caBIG");
				u.setAddress("3184 Graves Hall");
				u.setAddress2("333 W. Tenth Avenue");
				u.setCity("Columbus");
				u.setState(StateCode.OH);
				u.setZipcode("43210");
				u.setCountry(CountryCode.US);
				u.setPhoneNumber("555-555-5555");
				u.setStatus(IdPUserStatus.Active);
				u.setRole(IdPUserRole.Administrator);
				this.userManager.addUser(u);
			}

		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault
					.setFaultString("Error initializing the Identity Manager Provider.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}

	public SAMLAssertion authenticate(BasicAuthCredential credential)
			throws GUMSInternalFault, InvalidLoginFault {
		IdPUser requestor = verifyUser(credential);
		return assertionManager.getAuthenticationAssertion(requestor
				.getUserId(), requestor.getEmail());
	}

	public X509Certificate getIdpCertificate() throws GUMSInternalFault {
		return assertionManager.getIdPCertificate();
	}

	public String register(Application a) throws GUMSInternalFault,
			InvalidUserPropertyFault {

		IdPRegistrationPolicy policy = conf.getRegistrationPolicy();
		ApplicationReview ar = policy.register(a);
		IdPUserStatus status = ar.getStatus();
		IdPUserRole role = ar.getRole();
		String message = ar.getMessage();
		if (status == null) {
			status = IdPUserStatus.Pending;
		}

		if (role == null) {
			role = IdPUserRole.Non_Administrator;
		}
		if (message == null) {
			message = "None";
		}

		IdPUser u = new IdPUser();
		u.setUserId(a.getUserId());
		u.setEmail(a.getEmail());
		u.setPassword(a.getPassword());
		u.setFirstName(a.getFirstName());
		u.setLastName(a.getLastName());
		u.setOrganization(a.getOrganization());
		u.setAddress(a.getAddress());
		u.setAddress2(a.getAddress2());
		u.setCity(a.getCity());
		u.setState(a.getState());
		u.setZipcode(a.getZipcode());
		u.setCountry(a.getCountry());
		u.setPhoneNumber(a.getPhoneNumber());
		u.setRole(role);
		u.setStatus(status);
		userManager.addUser(u);
		return message;
	}

	public IdPUser getUser(BasicAuthCredential credential, String uid)
			throws GUMSInternalFault, InvalidLoginFault, PermissionDeniedFault, NoSuchUserFault {
		IdPUser requestor = verifyUser(credential);
		verifyAdministrator(requestor);
		return this.userManager.getUser(uid);
	}

	public IdPUser[] findUsers(BasicAuthCredential credential,
			IdPUserFilter filter) throws GUMSInternalFault, InvalidLoginFault,
			PermissionDeniedFault {
		IdPUser requestor = verifyUser(credential);
		verifyAdministrator(requestor);
		return this.userManager.getUsers(filter, false);
	}

	public void updateUser(BasicAuthCredential credential, IdPUser u)
			throws GUMSInternalFault, InvalidLoginFault, PermissionDeniedFault,
			NoSuchUserFault, InvalidUserPropertyFault {
		IdPUser requestor = verifyUser(credential);
		verifyAdministrator(requestor);
		this.userManager.updateUser(u);
	}

	private void verifyAdministrator(IdPUser u) throws PermissionDeniedFault {
		if (!u.getRole().equals(IdPUserRole.Administrator)) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("You are NOT an administrator.");
			throw fault;
		}
	}

	private IdPUser verifyUser(BasicAuthCredential credential)
			throws GUMSInternalFault, InvalidLoginFault {
		try {
			IdPUser u = this.userManager.getUser(credential.getUserId());
			if (!u.getPassword().equals(Crypt.crypt(credential.getPassword()))) {
				InvalidLoginFault fault = new InvalidLoginFault();
				fault.setFaultString("The uid or password is incorrect.");
				throw fault;
			}
			if (!u.getStatus().equals(IdPUserStatus.Active)) {
				if (u.getStatus().equals(IdPUserStatus.Suspended)) {
					InvalidLoginFault fault = new InvalidLoginFault();
					fault.setFaultString("The account has been suspended.");
					throw fault;

				} else if (u.getStatus().equals(IdPUserStatus.Rejected)) {
					InvalidLoginFault fault = new InvalidLoginFault();
					fault
							.setFaultString("The application for the account was rejected.");
					throw fault;

				} else if (u.getStatus().equals(IdPUserStatus.Pending)) {
					InvalidLoginFault fault = new InvalidLoginFault();
					fault
							.setFaultString("The application for this account has not yet been reviewed.");
					throw fault;
				} else {
					InvalidLoginFault fault = new InvalidLoginFault();
					fault.setFaultString("Unknown Reason");
					throw fault;
				}
			}

			return u;
		} catch (NoSuchUserFault e) {
			InvalidLoginFault fault = new InvalidLoginFault();
			fault.setFaultString("User Id or password is incorrect");
			throw fault;
		}

	}

	public void removeUser(BasicAuthCredential credential, String userId)
			throws GUMSInternalFault, InvalidLoginFault, PermissionDeniedFault {
		IdPUser requestor = verifyUser(credential);
		verifyAdministrator(requestor);
		userManager.removeUser(userId);
	}

}
