/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.shared;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SharedQueryBeanValidator implements Validator {

	/**
	 * 
	 */
	public SharedQueryBeanValidator() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class klass) {
		return SharedQueryBean.class.isAssignableFrom(klass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 *      org.springframework.validation.Errors)
	 */
	public void validate(Object obj, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "query.name",
				"field.required.name", null, "A name is required.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "query.description",
				"field.required.description", null,
				"A description is required.");
	}

}
