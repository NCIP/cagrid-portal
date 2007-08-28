/**
 * 
 */
package org.cagrid.installer.validator;

import java.io.File;
import java.util.Map;

import org.pietschy.wizard.InvalidStateException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class PathExistsValidator implements Validator {
	
	private String propertyName;
	private String message;
	
	public PathExistsValidator(String propertyName){
		this(propertyName, propertyName + " does not exist");
	}
	public PathExistsValidator(String propertyName, String message){
		this.propertyName = propertyName;
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.steps.Validator#validate(java.util.Map)
	 */
	public void validate(Map state)
			throws InvalidStateException {
		String propertyValue = (String) state.get(this.propertyName);
		if(propertyValue != null){
			File f = new File(propertyValue);
			if(!f.exists()){
				throw new InvalidStateException(this.message + " - " + f.getAbsolutePath());
			}
		}
	}

}
