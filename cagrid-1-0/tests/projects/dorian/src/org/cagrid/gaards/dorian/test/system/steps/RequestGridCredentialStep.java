package org.cagrid.gaards.dorian.test.system.steps;

import gov.nih.nci.cagrid.testing.system.haste.Step;

import org.cagrid.gaards.authentication.test.system.steps.AuthenticationStep;
import org.cagrid.gaards.dorian.client.GridUserClient;
import org.cagrid.gaards.dorian.federation.ProxyLifetime;
import org.globus.gsi.GlobusCredential;

public class RequestGridCredentialStep extends Step {

	private String serviceURL;
	private AuthenticationStep auth;
	private GlobusCredential gridCredential;

	public RequestGridCredentialStep(String serviceURL, AuthenticationStep auth) {
		this.serviceURL = serviceURL;
		this.auth = auth;
	}

	public void runStep() throws Throwable {
		ProxyLifetime lifetime = new ProxyLifetime();
		lifetime.setHours(12);
		GridUserClient client = new GridUserClient(this.serviceURL);
		this.gridCredential = client.createProxy(this.auth.getSAML(), lifetime,
				0);
		assertNotNull(this.gridCredential);
	}

	public GlobusCredential getGridCredential() {
		return gridCredential;
	}
}
