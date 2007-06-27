/**
 * 
 */
package org.cagrid.installer.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.pietschy.wizard.InvalidStateException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DorianIdpInfoValidator implements Validator {

	private CaGridInstallerModel model;

	private String message;

	public DorianIdpInfoValidator(CaGridInstallerModel model, String message) {
		this.model = model;
		this.message = message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.validator.Validator#validate(java.util.Map)
	 */
	public void validate(Map state) throws InvalidStateException {
		List<String[]> pairs = new ArrayList<String[]>();
		pairs.add(new String[] { Constants.DORIAN_IDP_UID_MIN,
				this.model.getMessage("dorian.idp.uid.min") });
		pairs.add(new String[] { Constants.DORIAN_IDP_UID_MAX,
				this.model.getMessage("dorian.idp.uid.max") });
		pairs.add(new String[] { Constants.DORIAN_IDP_PWD_MIN,
				this.model.getMessage("dorian.idp.pwd.min") });
		pairs.add(new String[] { Constants.DORIAN_IDP_PWD_MAX,
				this.model.getMessage("dorian.idp.pwd.max") });
		
		for(String[] pair : pairs){
			try{
				Integer.parseInt((String)state.get(pair[0]));
			}catch(Exception ex){
				throw new InvalidStateException(pair[1] + ": " + message);
			}
		}
	}

}
