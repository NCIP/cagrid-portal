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
public class JBossComponentInstaller extends
		AbstractDownloadedComponentInstaller {

	/**
	 * 
	 */
	public JBossComponentInstaller() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.AbstractExternalComponentInstaller#getComponentId()
	 */
	@Override
	protected String getComponentId() {
		return "jboss";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.AbstractExternalComponentInstaller#getShouldCheckCondition()
	 */
	@Override
	protected Condition getShouldCheckCondition() {
		return new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isJBossContainer() && model.isJBossInstalled();
			}
		};
	}

	protected Condition getShouldInstallCondition() {
		return new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isJBossContainer()
						&& (!model.isJBossInstalled() || model
								.isTrue(Constants.INSTALL_JBOSS));
			}
		};
	}

}
