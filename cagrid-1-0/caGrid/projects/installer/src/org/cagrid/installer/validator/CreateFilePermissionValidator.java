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
public class CreateFilePermissionValidator implements Validator {
	
	private String propName;
	private String message;
	
	public CreateFilePermissionValidator(String propName, String message){
		this.propName = propName;
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.validator.Validator#validate(java.util.Map)
	 */
	public void validate(Map state) throws InvalidStateException {
		String path = (String)state.get(this.propName);
		if(path != null){
			File f = new File(path);
			if(!f.exists()){
				try{
					f.mkdirs();
				}catch(Exception ex){
					throw new InvalidStateException(this.message);
				}
			}else{
				if(!f.canWrite()){
					throw new InvalidStateException(this.message);
				}
			}
		}
	}

}
