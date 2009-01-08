package org.cagrid.gaards.websso.authentication;

import org.cagrid.gaards.authentication.common.AuthenticationProfile;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * A validator to check if UserNamePasswordCredentials is valid.
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.5 $ $Date: 2009-01-08 23:16:37 $
 * @since 3.0
 */
public final class UsernamePasswordAuthenticationServiceURLCredentialsValidator implements Validator {

    @SuppressWarnings("unchecked")
	public boolean supports(final Class clazz) {
        return UsernamePasswordAuthenticationServiceURLCredentials.class.isAssignableFrom(clazz);
    }

    public void validate(final Object o, final Errors errors) {
    	UsernamePasswordAuthenticationServiceURLCredentials userNamePasswordCredentials = (UsernamePasswordAuthenticationServiceURLCredentials) o;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username","required.username");
		if (UsernamePasswordAuthenticationServiceURLCredentials.BASIC_AUTHENTICATION
				.equals(userNamePasswordCredentials.getServiceProfileType())) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password","required.password");
		}
		if (UsernamePasswordAuthenticationServiceURLCredentials.ONE_TIME_PASSWORD
				.equals(userNamePasswordCredentials.getServiceProfileType())) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "onetimepassword", "required.password");
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dorianName","required.dorian.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors,
				"authenticationServiceURL", "required.authentication.service");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors,
				"authenticationServiceProfile","required.authentication.profile");

		if (userNamePasswordCredentials.getDorianName().equals("-")) {
			errors.rejectValue("dorianName",
					"error.authentication.dorian.name.incorrect", null);
		}
		if (userNamePasswordCredentials.getAuthenticationServiceURL().equals(
				"-")) {
			errors.rejectValue("authenticationServiceURL",
					"error.authentication.service.incorrect", null);
		}
		if (userNamePasswordCredentials.getAuthenticationServiceProfile()
				.equals("-")) {
			errors.rejectValue("authenticationServiceProfile",
					"error.authentication.profile.incorrect", null);
		}
	}
}
