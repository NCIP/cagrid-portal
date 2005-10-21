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

import org.globus.wsrf.utils.FaultHelper;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class IdentityManagerProvider extends GUMSObject {

	private IdPProperties properties;

	private UserManager userManager;

	public IdentityManagerProvider(Database db) throws GUMSInternalFault {
		try {
			this.properties = new IdPProperties(db);
			this.userManager = new UserManager(db, this.properties);
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
		User u = new User();
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
		u.setRole(ar.getRole());
		u.setStatus(ar.getStatus());
		userManager.addUser(u);
		return ar.getMessage();
}


	public User[] findUsers(BasicAuthCredential credential, UserFilter filter)
			throws GUMSInternalFault, InvalidLoginFault, PermissionDeniedFault {
		User requestor = verifyUser(credential);
		verifyAdministrator(requestor);
		return this.userManager.getUsers(filter, true);
	}

	public void updateUser(BasicAuthCredential credential, User u)
			throws GUMSInternalFault, InvalidLoginFault, PermissionDeniedFault, NoSuchUserFault {
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
			User u = this.userManager.getUser(credential.getEmail());
			if (!u.getPassword().equals(Crypt.crypt(credential.getPassword()))) {
				InvalidLoginFault fault = new InvalidLoginFault();
				fault
						.setFaultString("The mmail address or password is incorrect.");
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
			fault.setFaultString("Email address or password is incorrect");
			throw fault;
		}

	}

	public IdPProperties getProperties() {
		return properties;
	}
}
