/**
 * 
 */
package org.cagrid.installer.validator;

import java.net.URL;
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
public class DorianIfsInfoValidator implements Validator {

	private String nanMessage;

	private String badUrlMessage;

	private CaGridInstallerModel model;

	public DorianIfsInfoValidator(CaGridInstallerModel model, String nanMessage, String badUrlMessage) {
		this.nanMessage = nanMessage;
		this.badUrlMessage = badUrlMessage;
		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.validator.Validator#validate(java.util.Map)
	 */
	public void validate(Map state) throws InvalidStateException {
		List<String[]> pairs = new ArrayList<String[]>();
		pairs.add(new String[] { Constants.DORIAN_IFS_IDPNAME_MIN,
				this.model.getMessage("dorian.ifd.idpname.min") });
		pairs.add(new String[] { Constants.DORIAN_IFS_IDPNAME_MAX,
				this.model.getMessage("dorian.ifs.idpname.max") });

		pairs.add(new String[] { Constants.DORIAN_IFS_CREDLIFETIME_YEARS,
				this.model.getMessage("dorian.ifs.credlifetime.years") });
		pairs.add(new String[] { Constants.DORIAN_IFS_CREDLIFETIME_MONTHS,
				this.model.getMessage("dorian.ifs.credlifetime.months") });
		pairs.add(new String[] { Constants.DORIAN_IFS_CREDLIFETIME_DAYS,
				this.model.getMessage("dorian.ifs.credlifetime.days") });
		pairs.add(new String[] { Constants.DORIAN_IFS_CREDLIFETIME_HOURS,
				this.model.getMessage("dorian.ifs.credlifetime.hours") });
		pairs.add(new String[] { Constants.DORIAN_IFS_CREDLIFETIME_MINUTES,
				this.model.getMessage("dorian.ifs.credlifetime.minutes") });
		pairs.add(new String[] { Constants.DORIAN_IFS_CREDLIFETIME_SECONDS,
				this.model.getMessage("dorian.ifs.credlifetime.seconds") });

		for (String[] pair : pairs) {
			try {
				Integer.parseInt((String) state.get(pair[0]));
			} catch (Exception ex) {
				throw new InvalidStateException(pair[1] + ": " + nanMessage);
			}
		}

		try {
			new URL((String) state.get(Constants.DORIAN_IFS_GTS_URL));
		} catch (Exception ex) {
			throw new InvalidStateException(this.model
					.getMessage("dorian.ifs.gts.url")
					+ ": " + badUrlMessage);
		}
	}

}
