/**
 * 
 */
package gov.nih.nci.cagrid.portal2.servlet.authn;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class TargetUrlValidator implements Validator {

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class klass) {
		return LoginCommand.class.isAssignableFrom(klass);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object command, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "url", "TARGET_URL_REQUIRED", "A 'url' request parameter is required.");
	}

}
