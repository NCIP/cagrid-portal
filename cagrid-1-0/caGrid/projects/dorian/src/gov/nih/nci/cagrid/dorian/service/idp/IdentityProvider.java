package gov.nih.nci.cagrid.dorian.service.idp;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.common.LoggingObject;
import gov.nih.nci.cagrid.dorian.conf.IdentityProviderConfiguration;
import gov.nih.nci.cagrid.dorian.idp.bean.Application;
import gov.nih.nci.cagrid.dorian.idp.bean.ApplicationReview;
import gov.nih.nci.cagrid.dorian.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUser;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserRole;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserStatus;
import gov.nih.nci.cagrid.dorian.service.Database;
import gov.nih.nci.cagrid.dorian.service.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.dorian.stubs.types.NoSuchUserFault;
import gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault;
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

	private AssertionCredentialsManager assertionManager;

	private IdPRegistrationPolicy registrationPolicy;
	


	public IdentityProvider(IdentityProviderConfiguration conf, Database db, CertificateAuthority ca)
		throws DorianInternalFault {
		try {
			this.registrationPolicy = (IdPRegistrationPolicy) Class.forName(conf.getRegistrationPolicy()).newInstance();
			this.userManager = new UserManager(db, conf);
			this.assertionManager = new AssertionCredentialsManager(conf, ca, db);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error initializing the Identity Manager Provider.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}


	public SAMLAssertion authenticate(BasicAuthCredential credential) throws DorianInternalFault, PermissionDeniedFault {
		IdPUser requestor = userManager.authenticateAndVerifyUser(credential);
		return assertionManager.getAuthenticationAssertion(requestor.getUserId(), requestor.getFirstName(), requestor
			.getLastName(), requestor.getEmail());
	}


	public void changePassword(BasicAuthCredential credential, String newPassword) throws DorianInternalFault,
		PermissionDeniedFault, InvalidUserPropertyFault {
		IdPUser requestor = userManager.authenticateAndVerifyUser(credential);
		requestor.setPassword(newPassword);
		try {
			this.userManager.updateUser(requestor);
		} catch (NoSuchUserFault e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An unexpected error occurred in trying to change the requested user's password.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}


	public X509Certificate getIdPCertificate() throws DorianInternalFault {
		return assertionManager.getIdPCertificate();
	}


	public String register(Application a) throws DorianInternalFault, InvalidUserPropertyFault {
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


	public IdPUser getUser(String requestorUID, String uid) throws DorianInternalFault, PermissionDeniedFault,
		NoSuchUserFault {
		IdPUser requestor = verifyUser(requestorUID);
		verifyAdministrator(requestor);
		return this.userManager.getUser(uid);
	}


	public IdPUser[] findUsers(String requestorUID, IdPUserFilter filter) throws DorianInternalFault,
		PermissionDeniedFault {
		IdPUser requestor = verifyUser(requestorUID);
		verifyAdministrator(requestor);
		return this.userManager.getUsers(filter, false);
	}


	public void updateUser(String requestorUID, IdPUser u) throws DorianInternalFault, PermissionDeniedFault,
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


	private IdPUser verifyUser(String uid) throws DorianInternalFault, PermissionDeniedFault {
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


	public void removeUser(String requestorUID, String userId) throws DorianInternalFault, PermissionDeniedFault {
		IdPUser requestor = verifyUser(requestorUID);
		verifyAdministrator(requestor);
		userManager.removeUser(userId);
	}


	public void clearDatabase() throws DorianInternalFault {
		assertionManager.clearDatabase();
		userManager.clearDatabase();
	}

}
