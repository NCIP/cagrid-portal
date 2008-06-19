package org.cagrid.gaards.dorian.idp;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.security.cert.X509Certificate;

import org.cagrid.gaards.authentication.BasicAuthentication;
import org.cagrid.gaards.authentication.Credential;
import org.cagrid.gaards.authentication.faults.AuthenticationProviderFault;
import org.cagrid.gaards.authentication.faults.CredentialNotSupportedFault;
import org.cagrid.gaards.authentication.faults.InvalidCredentialFault;
import org.cagrid.gaards.dorian.ca.CertificateAuthority;
import org.cagrid.gaards.dorian.common.LoggingObject;
import org.cagrid.gaards.dorian.stubs.types.DorianInternalFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidUserPropertyFault;
import org.cagrid.gaards.dorian.stubs.types.NoSuchUserFault;
import org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault;
import org.cagrid.tools.database.Database;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class IdentityProvider extends LoggingObject {

	private UserManager userManager;

	private AssertionCredentialsManager assertionManager;

	private IdPRegistrationPolicy registrationPolicy;

	public IdentityProvider(IdentityProviderProperties conf, Database db,
			CertificateAuthority ca) throws DorianInternalFault {
		try {
			this.registrationPolicy = conf.getRegistrationPolicy();
			this.userManager = new UserManager(db, conf);
			this.assertionManager = new AssertionCredentialsManager(conf, ca,
					db);
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

	public SAMLAssertion authenticate(Credential credential)
			throws AuthenticationProviderFault, InvalidCredentialFault,
			CredentialNotSupportedFault {
		IdPUser requestor = userManager.authenticateAndVerifyUser(credential);
		try {
			return assertionManager.getAuthenticationAssertion(requestor
					.getUserId(), requestor.getFirstName(), requestor
					.getLastName(), requestor.getEmail());
		} catch (DorianInternalFault e) {
			logError(e.getMessage(), e);
			AuthenticationProviderFault fault = new AuthenticationProviderFault();
			fault.setFaultString("An unexpected database error occurred.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (AuthenticationProviderFault) helper.getFault();
			throw fault;
		}
	}

	public void changePassword(BasicAuthentication credential,
			String newPassword) throws DorianInternalFault,
			PermissionDeniedFault, InvalidUserPropertyFault {
		try {
			IdPUser requestor = userManager
					.authenticateAndVerifyUser(credential);
			requestor.setPassword(newPassword);
			try {
				this.userManager.updateUser(requestor);
			} catch (NoSuchUserFault e) {
				logError(e.getMessage(), e);
				DorianInternalFault fault = new DorianInternalFault();
				fault
						.setFaultString("An unexpected error occurred in trying to change the requested user's password.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
				throw fault;
			}
		} catch (AuthenticationProviderFault e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString(e.getFaultString());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} catch (CredentialNotSupportedFault e) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString(e.getFaultString());
			throw fault;
		} catch (InvalidCredentialFault e) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString(e.getFaultString());
			throw fault;
		}
	}

	public X509Certificate getIdPCertificate() throws DorianInternalFault {
		return assertionManager.getIdPCertificate();
	}

	public String register(Application a) throws DorianInternalFault,
			InvalidUserPropertyFault {
		ApplicationReview ar = this.registrationPolicy.register(a);
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
			throws DorianInternalFault, PermissionDeniedFault, NoSuchUserFault {
		IdPUser requestor = verifyUser(requestorUID);
		verifyAdministrator(requestor);
		return this.userManager.getUser(uid);
	}

	public IdPUser[] findUsers(String requestorUID, IdPUserFilter filter)
			throws DorianInternalFault, PermissionDeniedFault {
		IdPUser requestor = verifyUser(requestorUID);
		verifyAdministrator(requestor);
		return this.userManager.getUsers(filter, false);
	}

	public void updateUser(String requestorUID, IdPUser u)
			throws DorianInternalFault, PermissionDeniedFault, NoSuchUserFault,
			InvalidUserPropertyFault {
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
			userManager.verifyUser(u);
			return u;
		} catch (NoSuchUserFault e) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("Invalid User!!!");
			throw fault;
		}
	}

	public void removeUser(String requestorUID, String userId)
			throws DorianInternalFault, PermissionDeniedFault {
		IdPUser requestor = verifyUser(requestorUID);
		verifyAdministrator(requestor);
		userManager.removeUser(userId);
	}

	public void clearDatabase() throws DorianInternalFault {
		assertionManager.clearDatabase();
		userManager.clearDatabase();
	}

	public boolean doesUserExist(String userId) throws DorianInternalFault {
		return this.userManager.userExists(userId);
	}

}
