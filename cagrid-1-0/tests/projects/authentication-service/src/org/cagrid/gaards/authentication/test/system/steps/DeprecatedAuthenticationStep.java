package org.cagrid.gaards.authentication.test.system.steps;

import gov.nih.nci.cagrid.authentication.bean.Credential;
import gov.nih.nci.cagrid.authentication.client.AuthenticationClient;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;

public class DeprecatedAuthenticationStep extends BaseAuthenticationStep {

	private Credential credential;

	public DeprecatedAuthenticationStep(ServiceContainer container,
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
		AuthenticationClient client = new AuthenticationClient(serviceURL,
				this.credential);
		return client.authenticate();
	}

}
