package gov.nih.nci.cagrid.gums.idp;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.Crypt;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.GUMSObject;
import gov.nih.nci.cagrid.gums.idp.bean.Application;
import gov.nih.nci.cagrid.gums.idp.bean.ApplicationReview;
import gov.nih.nci.cagrid.gums.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.gums.idp.bean.InvalidLoginFault;
import gov.nih.nci.cagrid.gums.idp.bean.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.gums.idp.bean.NoSuchUserFault;
import gov.nih.nci.cagrid.gums.idp.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.gums.idp.bean.User;
import gov.nih.nci.cagrid.gums.idp.bean.UserFilter;
import gov.nih.nci.cagrid.gums.idp.bean.UserRole;
import gov.nih.nci.cagrid.gums.idp.bean.UserStatus;

import org.globus.wsrf.utils.FaultHelper;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class IdentityProvider extends GUMSObject {

	private IdPProperties properties;

	private UserManager userManager;

	public static final String ADMIN_USER_ID = "gums";

	public static final String ADMIN_PASSWORD = "password";

	public IdentityProvider(Database db) throws GUMSInternalFault {
		try {
			this.properties = new IdPProperties(db);
			this.userManager = new UserManager(db, this.properties);

			if (!this.userManager.userExists(ADMIN_USER_ID)) {
				User u = new User();
				u.setUserId(ADMIN_USER_ID);
				u.setPassword(ADMIN_PASSWORD);
				u.setEmail("gums@gums.org");
				u.setFirstName("Mr.");
				u.setLastName("Administrator");
				u.setOrganization("caBIG");
				u.setAddress("3184 Graves Hall");
				u.setAddress2("333 W. Tenth Avenue");
				u.setCity("Columbus");
				u.setState("OH");
				u.setZipcode("43210");
				u.setPhoneNumber("555-555-5555");
				u.setStatus(UserManager.ACTIVE);
				u.setRole(UserManager.ADMINISTRATOR);
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

	public String register(Application a) throws GUMSInternalFault,
			InvalidUserPropertyFault {
		IdPRegistrationPolicy policy = properties.getRegistrationPolicy();
		ApplicationReview ar = policy.register(a);
		UserStatus status = ar.getStatus();
		UserRole role = ar.getRole();
		String message = ar.getMessage();
		if (status == null) {
			status = UserManager.PENDING;
		}

		if (role == null) {
			role = UserManager.NON_ADMINISTRATOR;
		}
		if (message == null) {
			message = "None";
		}

		User u = new User();
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
		u.setPhoneNumber(a.getPhoneNumber());
		u.setRole(role);
		u.setStatus(status);
		userManager.addUser(u);
		return message;
	}

	public User[] findUsers(BasicAuthCredential credential, UserFilter filter)
			throws GUMSInternalFault, InvalidLoginFault, PermissionDeniedFault {
		User requestor = verifyUser(credential);
		verifyAdministrator(requestor);
		return this.userManager.getUsers(filter, false);
	}

	public void updateUser(BasicAuthCredential credential, User u)
			throws GUMSInternalFault, InvalidLoginFault, PermissionDeniedFault,
			NoSuchUserFault, InvalidUserPropertyFault {
		User requestor = verifyUser(credential);
		verifyAdministrator(requestor);
		this.userManager.updateUser(u);
	}

	private void verifyAdministrator(User u) throws PermissionDeniedFault {
		if (!u.getRole().equals(UserManager.ADMINISTRATOR)) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("You are NOT an administrator.");
			throw fault;
		}
	}

	private User verifyUser(BasicAuthCredential credential)
			throws GUMSInternalFault, InvalidLoginFault {
		try {
			User u = this.userManager.getUser(credential.getUserId());
			if (!u.getPassword().equals(Crypt.crypt(credential.getPassword()))) {
				InvalidLoginFault fault = new InvalidLoginFault();
				fault.setFaultString("The uid or password is incorrect.");
				throw fault;
			}
			if (!u.getStatus().equals(UserManager.ACTIVE)) {
				if (u.getStatus().equals(UserManager.SUSPENDED)) {
					InvalidLoginFault fault = new InvalidLoginFault();
					fault.setFaultString("The account has been suspended.");
					throw fault;

				} else if (u.getStatus().equals(UserManager.REJECTED)) {
					InvalidLoginFault fault = new InvalidLoginFault();
					fault
							.setFaultString("The application for the account was rejected.");
					throw fault;

				} else if (u.getStatus().equals(UserManager.PENDING)) {
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
			throws GUMSInternalFault, InvalidLoginFault, PermissionDeniedFault{
		User requestor = verifyUser(credential);
		verifyAdministrator(requestor);
		userManager.removeUser(userId);
	}

	public IdPProperties getProperties() {
		return properties;
	}
}
