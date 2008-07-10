package org.cagrid.gaards.dorian.test.system.steps;

import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.util.List;

import org.cagrid.gaards.dorian.client.GridAdministrationClient;
import org.cagrid.gaards.dorian.federation.IFSUser;
import org.cagrid.gaards.dorian.federation.IFSUserFilter;
import org.cagrid.gaards.dorian.federation.IFSUserStatus;

public class UpdateGridUserStatusStep extends Step {

	private String serviceURL;
	private GridCredentialRequestStep admin;
	private GridCredentialRequestStep user;
	private IFSUserStatus status;

	public UpdateGridUserStatusStep(String serviceURL,
			GridCredentialRequestStep admin, GridCredentialRequestStep user, IFSUserStatus status) {
		this.serviceURL = serviceURL;
		this.admin = admin;
		this.user = user;
		this.status = status;
	}

	public void runStep() throws Throwable {
		GridAdministrationClient client = new GridAdministrationClient(
				serviceURL, this.admin.getGridCredential());
		IFSUserFilter f = new IFSUserFilter();
		f.setGridId(user.getGridCredential().getIdentity());
		List<IFSUser> users = client.findUsers(f);
		assertNotNull(users);
		assertEquals(1, users.size());
		IFSUser usr = users.get(0);
		usr.setUserStatus(this.status);
		client.updateUser(usr);
		List<IFSUser> users2 = client.findUsers(f);
		assertNotNull(users2);
		assertEquals(1, users2.size());
		assertEquals(usr, users2.get(0));

	}

}
