/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.participant;

import gov.nih.nci.cagrid.portal2.domain.Person;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ParticipantValidator implements Validator {

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class klass) {
		return Person.class.isAssignableFrom(klass);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object object, Errors errors) {
		Person person = (Person)object;
		validateName(person, errors);
		validateContactInfo(person, errors);
	}
	
	public void validateName(Person person, Errors errors){
		ValidationUtils.rejectIfEmpty(errors, "firstName", "FIRST_NAME_REQUIRED", "First name is required.");
		ValidationUtils.rejectIfEmpty(errors, "lastName", "LAST_NAME_REQUIRED", "Last name is required.");
	}
	
	public void validateContactInfo(Person person, Errors errors){
		ValidationUtils.rejectIfEmpty(errors, "phoneNumber", "PHONE_NUMBER_REQUIRED", "Phone number is required.");
		ValidationUtils.rejectIfEmpty(errors, "emailAddress", "EMAIL_ADDRESS_REQUIRED", "Email addres is required.");
	}

}
