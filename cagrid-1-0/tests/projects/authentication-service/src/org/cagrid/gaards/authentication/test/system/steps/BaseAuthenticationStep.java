package org.cagrid.gaards.authentication.test.system.steps;

import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;

public abstract class BaseAuthenticationStep extends Step {
	private String serviceURL;
	private AuthenticationOutcome outcome;

	public BaseAuthenticationStep(String serviceURL,
			AuthenticationOutcome outcome) {
		this.serviceURL = serviceURL;
		this.outcome = outcome;
	}

	public abstract SAMLAssertion authenticate() throws Exception;

	public final void runStep() throws Throwable {
		SAMLAssertion saml = null;
		Exception error = null;
		try {
			saml = authenticate();
		} catch (Exception e) {
			error = e;
		}
		outcome.check(saml, error);
	}

	public String getServiceURL() {
		return serviceURL;
	}

	

}
