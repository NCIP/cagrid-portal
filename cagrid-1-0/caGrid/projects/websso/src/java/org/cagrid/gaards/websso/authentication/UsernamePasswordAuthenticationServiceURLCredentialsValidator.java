package org.cagrid.gaards.websso.authentication;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * A validator to check if UserNamePasswordCredentials is valid.
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.3 $ $Date: 2008-02-26 18:02:55 $
 * @since 3.0
 */
public final class UsernamePasswordAuthenticationServiceURLCredentialsValidator implements Validator {

    public boolean supports(final Class clazz) {
        return UsernamePasswordAuthenticationServiceURLCredentials.class.isAssignableFrom(clazz);
    }

    public void validate(final Object o, final Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username",
            "required.username", null);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password",
            "required.password", null);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "authenticationServiceURL",
                "required.authentication.service", null);
        if (((UsernamePasswordAuthenticationServiceURLCredentials)o).getAuthenticationServiceURL().equals("-"))
        {
	        errors.rejectValue("authenticationServiceURL", "error.authentication.service.incorrect", null);
        }
        
    }
}
