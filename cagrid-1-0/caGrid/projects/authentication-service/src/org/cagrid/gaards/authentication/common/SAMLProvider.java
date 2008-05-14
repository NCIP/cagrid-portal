package org.cagrid.gaards.authentication.common;

import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import javax.security.auth.Subject;

public interface SAMLProvider {
    SAMLAssertion getSAML(Subject subject) throws InsufficientAttributeException;
}
