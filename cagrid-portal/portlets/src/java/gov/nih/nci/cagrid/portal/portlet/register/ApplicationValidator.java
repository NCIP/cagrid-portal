/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.register;

import gov.nih.nci.cagrid.dorian.idp.bean.Application;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ApplicationValidator implements Validator {

	private String[] requiredFields = new String[] { "userId", "email",
			"password", "firstName", "lastName", "organization", "address",
			"city", "state", "country", "phoneNumber" };

	/**
	 * 
	 */
	public ApplicationValidator() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class klass) {
		return Application.class.isAssignableFrom(klass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 *      org.springframework.validation.Errors)
	 */
	public void validate(Object obj, Errors errors) {
		for (String requiredField : getRequiredFields()) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, requiredField,
					"field.required.register." + requiredField, "Missing required field.");
		}
	}

	public String[] getRequiredFields() {
		return requiredFields;
	}

	public void setRequiredFields(String[] requiredFields) {
		this.requiredFields = requiredFields;
	}

}
