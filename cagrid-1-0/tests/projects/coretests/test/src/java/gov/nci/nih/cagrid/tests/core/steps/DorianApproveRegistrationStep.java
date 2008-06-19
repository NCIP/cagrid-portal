/*
 * Created on Jul 14, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.GridCredential;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import org.cagrid.gaards.dorian.client.IdPAdministrationClient;
import org.cagrid.gaards.dorian.idp.Application;
import org.cagrid.gaards.dorian.idp.IdPUser;
import org.cagrid.gaards.dorian.idp.IdPUserFilter;
import org.cagrid.gaards.dorian.idp.IdPUserStatus;
import org.globus.gsi.GlobusCredential;

/**
 * This step approves a user application by finding the user in dorian and
 * marking the account status as active.
 * 
 * @author Patrick McConnell
 */
public class DorianApproveRegistrationStep extends Step {
	private String serviceURL;
	private Application application;
	private GridCredential credential;

	public DorianApproveRegistrationStep(Application application,
			String serviceURL, GridCredential credential) {
		super();
		this.application = application;
		this.serviceURL = serviceURL;
		this.credential = credential;
	}

	@Override
	public void runStep() throws Throwable {
		GlobusCredential proxy = null;
		if (credential != null) {
			proxy = credential.getCredential();
		}
		IdPAdministrationClient client = new IdPAdministrationClient(
				this.serviceURL, proxy);

		// find users
		IdPUserFilter filter = new IdPUserFilter();
		filter.setUserId(this.application.getUserId());
		filter.setStatus(IdPUserStatus.Pending);
		IdPUser[] users = client.findUsers(filter);
		assertNotNull(users);
		assertTrue(users.length > 0);

		// find user
		IdPUser user = findUser(users, this.application);
		assertNotNull(user);

		// accept application
		user.setStatus(IdPUserStatus.Active);
		client.updateUser(user);
	}

	private IdPUser findUser(IdPUser[] users, Application application) {
		for (IdPUser user : users) {
			if (user.getUserId().equals(application.getUserId())) {
				return user;
			}
		}
		return null;
	}
}
