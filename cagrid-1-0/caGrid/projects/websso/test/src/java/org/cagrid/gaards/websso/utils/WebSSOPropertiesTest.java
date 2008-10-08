package org.cagrid.gaards.websso.utils;

import org.cagrid.gaards.websso.utils.WebSSOProperties;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class WebSSOPropertiesTest extends AbstractDependencyInjectionSpringContextTests {

	protected String[] getConfigLocations() {
		return new String[] { "classpath:websso-beans.xml" };
	}

	private WebSSOProperties webSSOProperties;

	public void setWebSSOProperties(WebSSOProperties webSSOProperties) {
		this.webSSOProperties = webSSOProperties;
	}

	public void testValidateIssuedCredentialPathLength() {
		assertEquals(0, webSSOProperties.getCredentialDelegationServiceInformation().getIssuedCredentialPathLength());
	}	
}