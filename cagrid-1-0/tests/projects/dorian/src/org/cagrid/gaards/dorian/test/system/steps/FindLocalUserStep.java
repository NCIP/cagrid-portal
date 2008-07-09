package org.cagrid.gaards.dorian.test.system.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.util.List;

import org.cagrid.gaards.dorian.client.LocalAdministrationClient;
import org.cagrid.gaards.dorian.idp.Application;
import org.cagrid.gaards.dorian.idp.IdPUser;
import org.cagrid.gaards.dorian.idp.IdPUserFilter;
import org.cagrid.gaards.dorian.idp.IdPUserRole;
import org.cagrid.gaards.dorian.idp.IdPUserStatus;

public class FindLocalUserStep extends Step {

	private Application application;
	private String serviceURL;
	private IdPUserStatus status;
	private IdPUserRole role;
	private RequestGridCredentialStep admin;
	private IdPUser localUser;

	public FindLocalUserStep(String serviceURL,
			RequestGridCredentialStep admin, Application app,
			IdPUserStatus status, IdPUserRole role) {
		this.serviceURL = serviceURL;
		this.application = app;
		this.status = status;
		this.role = role;
		this.admin = admin;
	}

	public void runStep() throws Throwable {
		LocalAdministrationClient client = new LocalAdministrationClient(
				serviceURL, this.admin.getGridCredential());
		IdPUserFilter f = new IdPUserFilter();
		f.setUserId(this.application.getUserId());
		List<IdPUser> users = client.findUsers(f);
		assertNotNull(users);
		assertEquals(1, users.size());
		assertEquals(this.application.getUserId(), users.get(0).getUserId());
		assertEquals(this.application.getFirstName(), users.get(0)
				.getFirstName());
		assertEquals(this.application.getLastName(), users.get(0).getLastName());
		assertEquals(this.application.getOrganization(), users.get(0)
				.getOrganization());
		assertEquals(this.application.getAddress(), users.get(0).getAddress());
		assertEquals(Utils.clean(this.application.getAddress2()), Utils.clean(users.get(0).getAddress2()));
		assertEquals(this.application.getCity(), users.get(0).getCity());
		assertEquals(this.application.getState(), users.get(0).getState());
		assertEquals(this.application.getZipcode(), users.get(0).getZipcode());
		assertEquals(this.application.getCountry(), users.get(0).getCountry());
		assertEquals(this.application.getPhoneNumber(), users.get(0)
				.getPhoneNumber());
		assertEquals(this.application.getEmail(), users.get(0).getEmail());
		assertEquals(this.status, users.get(0).getStatus());
		assertEquals(this.role, users.get(0).getRole());
		this.localUser = users.get(0);
	}

}
