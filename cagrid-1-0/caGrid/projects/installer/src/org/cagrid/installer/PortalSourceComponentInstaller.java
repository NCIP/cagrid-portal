package org.cagrid.installer;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalSourceComponentInstaller extends AbstractDownloadedComponentInstaller{

    public PortalSourceComponentInstaller() {
    }

    @Override
    protected String getComponentId() {
        return "portal";
    }

    @Override
    protected Condition getShouldCheckCondition() {
        return new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isPortalInstalled();
			}
		};
    }

    @Override
    protected Condition getShouldInstallCondition() {
       return new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_PORTAL);
			}
		};
    }
}