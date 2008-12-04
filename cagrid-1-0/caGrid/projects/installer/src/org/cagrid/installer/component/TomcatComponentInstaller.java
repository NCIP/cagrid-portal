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
public class TomcatComponentInstaller extends
		AbstractDownloadedComponentInstaller {

	/**
	 * 
	 */
	public TomcatComponentInstaller() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.AbstractExternalComponentInstaller#getComponentId()
	 */
	@Override
	protected String getComponentId() {
		return "tomcat";
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
				return model.isTomcatContainer() && model.isTomcatInstalled();
			}
		};
	}

	protected Condition getShouldInstallCondition() {
		return new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTomcatContainer()
						&& (!model.isTomcatInstalled() || model
								.isTrue(Constants.INSTALL_TOMCAT));
			}
		};
	}

}
