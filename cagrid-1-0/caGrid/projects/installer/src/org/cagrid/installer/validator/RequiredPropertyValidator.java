/**
 * 
 */
package org.cagrid.installer.validator;

import java.util.Map;

import org.pietschy.wizard.InvalidStateException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class RequiredPropertyValidator implements Validator {

	private String propertyName;
	private String message;
	
	public RequiredPropertyValidator(String propertyName){
		this(propertyName, propertyName + " is required");
	}
	public RequiredPropertyValidator(String propertyName, String message){
		this.propertyName = propertyName;
		this.message = message;
	}
	
	/* (non-Javadoc)
	 * @see org.cagrid.installer.steps.Validator#validate(java.util.Map)
	 */
	public void validate(Map state)
			throws InvalidStateException {
		String value = (String) state.get(this.propertyName);
		if(value == null || value.trim().length() == 0){
			throw new InvalidStateException(message);
		}
	}

}
