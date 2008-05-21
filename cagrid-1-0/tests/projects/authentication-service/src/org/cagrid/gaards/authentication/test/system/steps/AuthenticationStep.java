package org.cagrid.gaards.authentication.test.system.steps;

import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;

import org.cagrid.gaards.authentication.Credential;
import org.cagrid.gaards.authentication.client.AuthenticationClient;

public class AuthenticationStep extends BaseAuthenticationStep {

	private Credential credential;

	public AuthenticationStep(ServiceContainer container,
			AuthenticationOutcome outcome, Credential credential) {
		super(container, outcome);
		this.credential = credential;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return super.getName();
	}

	public SAMLAssertion authenticate() throws Exception {
		String serviceURL = getContainer().getContainerBaseURI().toString()
				+ "cagrid/AuthenticationService";
		AuthenticationClient client = new AuthenticationClient(serviceURL);
		return client.authenticate(this.credential);
	}

}
