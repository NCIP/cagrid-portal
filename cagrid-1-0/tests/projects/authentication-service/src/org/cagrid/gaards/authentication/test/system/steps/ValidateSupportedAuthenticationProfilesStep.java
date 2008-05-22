package org.cagrid.gaards.authentication.test.system.steps;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;

import org.cagrid.gaards.authentication.client.AuthenticationClient;

public class ValidateSupportedAuthenticationProfilesStep extends Step {

	private ServiceContainer container;
	private Set<QName> expectedProfiles;

	public ValidateSupportedAuthenticationProfilesStep(
			ServiceContainer container, Set<QName> profiles) {
		this.container = container;
		this.expectedProfiles = profiles;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return super.getName();
	}

	@Override
	public void runStep() throws Throwable {
		String serviceURL = this.container.getContainerBaseURI().toString()
				+ "cagrid/AuthenticationService";
		AuthenticationClient client = new AuthenticationClient(serviceURL);
		Set<QName> profiles = client.getSupportedAuthenticationProfiles();

		Iterator<QName> itr = profiles.iterator();
		while (itr.hasNext()) {
			QName profile = itr.next();
			if (!this.expectedProfiles.contains(profile)) {
				throw new Exception(
						"The profile "
								+ profile.getNamespaceURI()
								+ ":"
								+ profile.getLocalPart()
								+ " is supported by the service but is not in the list of expected profiles.");
			}
		}

		Iterator<QName> itr2 = expectedProfiles.iterator();
		while (itr2.hasNext()) {
			QName profile = itr2.next();
			if (!profiles.contains(profile)) {
				throw new Exception(
						"The profile "
								+ profile.getNamespaceURI()
								+ ":"
								+ profile.getLocalPart()
								+ " is in the list of expected profiles but is not supported by the service.");
			}
		}

	}
}
