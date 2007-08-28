/**
 * 
 */
package org.cagrid.installer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.tasks.DownloadFileTask;
import org.cagrid.installer.tasks.UnTarInstallTask;
import org.cagrid.installer.workflow.DeployActiveBPELTask;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ActiveBPELComponentInstaller extends
		AbstractDownloadedComponentInstaller {

	private static final Log logger = LogFactory
			.getLog(ActiveBPELComponentInstaller.class);

	/**
	 * 
	 */
	public ActiveBPELComponentInstaller() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.AbstractDownloadedComponentInstaller#getComponentId()
	 */
	@Override
	protected String getComponentId() {
		return "activebpel";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.AbstractDownloadedComponentInstaller#getShouldCheckCondition()
	 */
	@Override
	protected Condition getShouldCheckCondition() {
		return new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_WORKFLOW)
						&& model.isActiveBPELInstalled();
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.AbstractDownloadedComponentInstaller#getShouldInstallCondition()
	 */
	@Override
	protected Condition getShouldInstallCondition() {
		return new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				boolean installWorkflow = model
						.isTrue(Constants.INSTALL_WORKFLOW);
				boolean isActiveBPELInstalled = model.isActiveBPELInstalled();
				boolean installActiveBPEL = model
						.isTrue(Constants.INSTALL_ACTIVEBPEL);
				boolean shouldInstall = installWorkflow
						&& (!isActiveBPELInstalled || installActiveBPEL);

//				logger.debug("installWorkflow = " + installWorkflow
//						+ ", isActiveBPELInstalled = " + isActiveBPELInstalled
//						+ ", installActiveBPEL = " + installActiveBPEL
//						+ ", shouldInstall = " + shouldInstall);

				return shouldInstall;
			}
		};
	}

}
