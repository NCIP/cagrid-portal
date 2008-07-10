package org.cagrid.gaards.dorian.test.system.steps;

import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.util.List;

import org.cagrid.gaards.dorian.client.LocalAdministrationClient;
import org.cagrid.gaards.dorian.idp.IdPUser;
import org.cagrid.gaards.dorian.idp.IdPUserFilter;
import org.cagrid.gaards.dorian.idp.IdPUserStatus;

public class UpdateLocalUserStatusStep extends Step {

	private String serviceURL;
	private GridCredentialRequestStep admin;
	private String localUser;
	private IdPUserStatus status;

	public UpdateLocalUserStatusStep(String serviceURL,
			GridCredentialRequestStep admin, String uid, IdPUserStatus status) {
		this.serviceURL = serviceURL;
		this.admin = admin;
		this.localUser = uid;
		this.status = status;
	}

	public void runStep() throws Throwable {
		LocalAdministrationClient client = new LocalAdministrationClient(
				serviceURL, this.admin.getGridCredential());
		IdPUserFilter f = new IdPUserFilter();
		f.setUserId(this.localUser);
		List<IdPUser> users = client.findUsers(f);
		assertNotNull(users);
		assertEquals(1, users.size());
		IdPUser usr = users.get(0);
		usr.setStatus(this.status);
		client.updateUser(usr);
		List<IdPUser> users2 = client.findUsers(f);
		assertNotNull(users2);
		assertEquals(1, users2.size());
		assertEquals(usr, users2.get(0));

	}

}
