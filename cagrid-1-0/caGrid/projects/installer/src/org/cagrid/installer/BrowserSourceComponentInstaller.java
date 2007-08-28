/**
 * 
 */
package org.cagrid.installer;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class BrowserSourceComponentInstaller extends
		AbstractDownloadedComponentInstaller {

	/**
	 * 
	 */
	public BrowserSourceComponentInstaller() {

	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.AbstractDownloadedComponentInstaller#getComponentId()
	 */
	@Override
	protected String getComponentId() {
		return "browser";
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.AbstractDownloadedComponentInstaller#getShouldCheckCondition()
	 */
	@Override
	protected Condition getShouldCheckCondition() {
		return new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isBrowserInstalled();
			}
		};
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.AbstractDownloadedComponentInstaller#getShouldInstallCondition()
	 */
	@Override
	protected Condition getShouldInstallCondition() {
		return new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_BROWSER);
			}
		};
	}

}
