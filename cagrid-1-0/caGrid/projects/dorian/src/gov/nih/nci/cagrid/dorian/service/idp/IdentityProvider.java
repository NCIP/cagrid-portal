package gov.nih.nci.cagrid.dorian.service.idp;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.common.Crypt;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.common.LoggingObject;
import gov.nih.nci.cagrid.dorian.idp.bean.Application;
import gov.nih.nci.cagrid.dorian.idp.bean.ApplicationReview;
import gov.nih.nci.cagrid.dorian.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.dorian.idp.bean.CountryCode;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUser;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserRole;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserStatus;
import gov.nih.nci.cagrid.dorian.idp.bean.StateCode;
import gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.dorian.stubs.NoSuchUserFault;
import gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.security.cert.X509Certificate;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class IdentityProvider extends LoggingObject {

	private UserManager userManager;

	public static String ADMIN_USER_ID = "dorian";

	public static String ADMIN_PASSWORD = "password";

	private IdPConfiguration conf;

	private AssertionCredentialsManager assertionManager;

	public IdentityProvider(IdPConfiguration conf, Database db,
			CertificateAuthority ca) throws DorianInternalFault {
		try {
			this.conf = conf;
			this.userManager = new UserManager(db, conf);
			this.assertionManager = new AssertionCredentialsManager(conf, ca,
					db);

			if (!this.userManager.userExists(ADMIN_USER_ID)) {
				IdPUser u = new IdPUser();
				u.setUserId(ADMIN_USER_ID);
				u.setPassword(ADMIN_PASSWORD);
				u.setEmail("dorian@dorian.org");
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
			DorianInternalFault fault = new DorianInternalFault();
			fault
					.setFaultString("Error initializing the Identity Manager Provider.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}

	public SAMLAssertion authenticate(BasicAuthCredential credential)
			throws DorianInternalFault, PermissionDeniedFault {
		IdPUser requestor = authenticateAndVerifyUser(credential);
		return assertionManager.getAuthenticationAssertion(requestor
				.getUserId(), requestor.getEmail());
	}

	public X509Certificate getIdPCertificate() throws DorianInternalFault {
		return assertionManager.getIdPCertificate();
	}

	public String register(Application a) throws DorianInternalFault,
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

	public IdPUser getUser(String requestorUID, String uid)
			throws DorianInternalFault, PermissionDeniedFault,
			NoSuchUserFault {
		IdPUser requestor = verifyUser(requestorUID);
		verifyAdministrator(requestor);
		return this.userManager.getUser(uid);
	}

	public IdPUser[] findUsers(String requestorUID,
			IdPUserFilter filter) throws DorianInternalFault,
			PermissionDeniedFault {
		IdPUser requestor = verifyUser(requestorUID);
		verifyAdministrator(requestor);
		return this.userManager.getUsers(filter, false);
	}

	public void updateUser(String requestorUID, IdPUser u)
			throws DorianInternalFault, PermissionDeniedFault,
			NoSuchUserFault, InvalidUserPropertyFault {
		IdPUser requestor = verifyUser(requestorUID);
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

	private IdPUser verifyUser(String uid) throws DorianInternalFault,
			PermissionDeniedFault {
		try {
			IdPUser u = this.userManager.getUser(uid);
			verifyUser(u);
			return u;
		} catch (NoSuchUserFault e) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("Invalid User!!!");
			throw fault;
		}
	}

	private void verifyUser(IdPUser u) throws DorianInternalFault,
			PermissionDeniedFault {

		if (!u.getStatus().equals(IdPUserStatus.Active)) {
			if (u.getStatus().equals(IdPUserStatus.Suspended)) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault.setFaultString("The account has been suspended.");
				throw fault;

			} else if (u.getStatus().equals(IdPUserStatus.Rejected)) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault
						.setFaultString("The application for the account was rejected.");
				throw fault;

			} else if (u.getStatus().equals(IdPUserStatus.Pending)) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault
						.setFaultString("The application for this account has not yet been reviewed.");
				throw fault;
			} else {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault.setFaultString("Unknown Reason");
				throw fault;
			}
		}

	}

	private IdPUser authenticateAndVerifyUser(BasicAuthCredential credential)
			throws DorianInternalFault, PermissionDeniedFault {
		try {
			IdPUser u = this.userManager.getUser(credential.getUserId());
			if (!u.getPassword().equals(Crypt.crypt(credential.getPassword()))) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault.setFaultString("The uid or password is incorrect.");
				throw fault;
			}
			verifyUser(u);
			return u;
		} catch (NoSuchUserFault e) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("User Id or password is incorrect");
			throw fault;
		}

	}

	public void removeUser(String requestorUID, String userId)
			throws DorianInternalFault, PermissionDeniedFault {
		IdPUser requestor = verifyUser(requestorUID);
		verifyAdministrator(requestor);
		userManager.removeUser(userId);
	}

}
