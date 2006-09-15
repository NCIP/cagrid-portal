package gov.nih.nci.cagrid.authentication.common;

import gov.nih.nci.cagrid.authentication.bean.Credential;

import javax.security.auth.Subject;

public interface SubjectProvider {
    
    Subject getSubject(Credential credential) throws InvalidCredentialException;

}
