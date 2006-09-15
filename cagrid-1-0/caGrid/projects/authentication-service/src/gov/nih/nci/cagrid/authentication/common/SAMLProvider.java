package gov.nih.nci.cagrid.authentication.common;

import javax.security.auth.Subject;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

public interface SAMLProvider {
    SAMLAssertion getSAML(Subject subject) throws InsufficientAttributeException;
}
