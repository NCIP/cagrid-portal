package org.cagrid.gaards.authentication.service;

import org.cagrid.gaards.authentication.common.AuthenticationProfile;

public abstract class BasicAuthenticationWithOneTimePasswordSubjectProvider
		extends BaseSubjectProvider {
	public BasicAuthenticationWithOneTimePasswordSubjectProvider() {
		super();
		addSupportedProfile(AuthenticationProfile.BASIC_AUTHENTICATION_WITH_ONE_TIME_PASSWORD);
	}

}
