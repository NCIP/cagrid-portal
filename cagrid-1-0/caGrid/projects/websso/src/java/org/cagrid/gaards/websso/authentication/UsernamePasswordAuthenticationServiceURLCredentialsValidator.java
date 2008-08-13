package org.cagrid.gaards.websso.authentication;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * A validator to check if UserNamePasswordCredentials is valid.
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.4 $ $Date: 2008-08-13 13:52:32 $
 * @since 3.0
 */
public final class UsernamePasswordAuthenticationServiceURLCredentialsValidator implements Validator {

    public boolean supports(final Class clazz) {
        return UsernamePasswordAuthenticationServiceURLCredentials.class.isAssignableFrom(clazz);
    }

    public void validate(final Object o, final Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username","required.username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password","required.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "authenticationServiceURL","required.authentication.service");
        if (((UsernamePasswordAuthenticationServiceURLCredentials)o).getAuthenticationServiceURL().equals("-"))
        {
	        errors.rejectValue("authenticationServiceURL", "error.authentication.service.incorrect", null);
        }        
    }
}
