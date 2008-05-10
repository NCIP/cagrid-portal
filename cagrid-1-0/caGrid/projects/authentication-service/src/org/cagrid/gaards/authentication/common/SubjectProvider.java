package org.cagrid.gaards.authentication.common;



import javax.security.auth.Subject;

import org.cagrid.gaards.authentication.Credential;

public interface SubjectProvider {
    
    Subject getSubject(Credential credential) throws InvalidCredentialException;

}
