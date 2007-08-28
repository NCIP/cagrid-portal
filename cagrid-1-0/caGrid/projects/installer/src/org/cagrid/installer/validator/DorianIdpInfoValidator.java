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

	private static final int DEFAULT_IDP_USER_USERID_LENGTH = 6;

	private static final int DEFAULT_IDP_USER_PASSWORD_LENGTH = 13;

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
		pairs
				.add(new String[] {
						Constants.DORIAN_IDP_MAX_CONSEC_INVALID_LOGINS,
						this.model
								.getMessage("dorian.idp.max.consec.invalid.logins") });
		pairs.add(new String[] { Constants.DORIAN_IDP_MAX_TOTAL_INVALID_LOGINS,
				this.model.getMessage("dorian.idp.max.total.invalid.logins") });
		pairs
				.add(new String[] {
						Constants.DORIAN_IDP_PWD_LOCKOUT_HOURS,
						this.model
								.getMessage(Constants.DORIAN_IDP_PWD_LOCKOUT_HOURS) });
		pairs
				.add(new String[] {
						Constants.DORIAN_IDP_PWD_LOCKOUT_MINUTES,
						this.model
								.getMessage(Constants.DORIAN_IDP_PWD_LOCKOUT_MINUTES) });
		pairs
				.add(new String[] {
						Constants.DORIAN_IDP_PWD_LOCKOUT_SECONDS,
						this.model
								.getMessage(Constants.DORIAN_IDP_PWD_LOCKOUT_SECONDS) });

		for (String[] pair : pairs) {
			try {
				Integer.parseInt((String) state.get(pair[0]));
			} catch (Exception ex) {
				throw new InvalidStateException(pair[1] + ": " + message);
			}
		}

		if (Integer.parseInt((String) state.get(Constants.DORIAN_IDP_UID_MIN)) > DEFAULT_IDP_USER_USERID_LENGTH) {
			throw new InvalidStateException(
					"The userid minimum length must be less than or equal \nto the default IdP user's userid length, which is "
							+ DEFAULT_IDP_USER_USERID_LENGTH + ".");
		}
		if (Integer.parseInt((String) state.get(Constants.DORIAN_IDP_UID_MAX)) < DEFAULT_IDP_USER_USERID_LENGTH) {
			throw new InvalidStateException(
					"The userid maximum length must be greater than or equal \nto the default IdP user's userid length, which is "
							+ DEFAULT_IDP_USER_USERID_LENGTH + ".");
		}
		if (Integer.parseInt((String) state.get(Constants.DORIAN_IDP_PWD_MIN)) > DEFAULT_IDP_USER_PASSWORD_LENGTH) {
			throw new InvalidStateException(
					"The password minimum length must be less than or equal \nto the default IdP user's password length, which is "
							+ DEFAULT_IDP_USER_PASSWORD_LENGTH + ".");
		}
		if (Integer.parseInt((String) state.get(Constants.DORIAN_IDP_PWD_MAX)) < DEFAULT_IDP_USER_PASSWORD_LENGTH) {
			throw new InvalidStateException(
					"The password maximum length must be greater than or equal \nto the default IdP user's password length, which is "
							+ DEFAULT_IDP_USER_PASSWORD_LENGTH + ".");
		}
	}

}
