/**
 * 
 */
package org.cagrid.installer.component;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class GlobusComponentInstaller extends
		AbstractDownloadedComponentInstaller {

	/**
	 * 
	 */
	public GlobusComponentInstaller() {
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.AbstractDownloadedComponentInstaller#getComponentId()
	 */
	@Override
	protected String getComponentId() {
		return "globus";
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.AbstractDownloadedComponentInstaller#getShouldCheckCondition()
	 */
	@Override
	protected Condition getShouldCheckReinstallCondition() {
		return new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isGlobusInstalled() && model.isTrue(Constants.INSTALL_CONFIGURE_CAGRID);
			}
		};
	}
	
	protected Condition getShouldInstallCondition(){
		return new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_CONFIGURE_CAGRID) && (!model.isGlobusInstalled()
						|| model.isTrue(Constants.REINSTALL_GLOBUS));
			}
		};
	}

}
