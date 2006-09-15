package gov.nih.nci.security.authentication.ext.pricipals;

import java.security.Principal;

public class UserIdPrincipal implements Principal {
    
    private String userId;
    
    public UserIdPrincipal(String userId){
	this.userId = userId;
    }

    public String getName() {
	return this.userId;
    }

}
