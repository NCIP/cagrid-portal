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
public class AntComponentInstaller extends AbstractDownloadedComponentInstaller {

	/**
	 * 
	 */
	public AntComponentInstaller() {

	}

	protected String getComponentId() {
		return "ant";
	}

	protected Condition getShouldInstallCondition(){
		return new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return  model.isTrue(Constants.INSTALL_CONFIGURE_CAGRID) && (!model.isAntInstalled()
						|| model.isTrue(Constants.INSTALL_ANT));
			}
		};
	}

	protected Condition getShouldCheckReinstallCondition() {
		return new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isAntInstalled() && model.isTrue(Constants.INSTALL_CONFIGURE_CAGRID);
			}
		};
	}

}
