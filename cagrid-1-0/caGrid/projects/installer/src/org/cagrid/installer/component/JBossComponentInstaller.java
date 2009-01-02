/**
 * 
 */
package org.cagrid.installer.component;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.tasks.installer.ConfigureJBossTask;
import org.cagrid.installer.tasks.installer.DeployGlobusToJBossTask;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;


/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class JBossComponentInstaller extends AbstractDownloadedComponentInstaller {

    @Override
    public void addInstallDownloadedComponentTasks(CaGridInstallerModel model, RunTasksStep deployContainer) {
        super.addInstallDownloadedComponentTasks(model, deployContainer);

        deployContainer.getTasks().add(
            new ConditionalTask(new DeployGlobusToJBossTask(model.getMessage("deploying.globus.jboss.title"), model.getMessage("deploying.globus.jboss.title")),
                new Condition() {

                    public boolean evaluate(WizardModel m) {
                        CaGridInstallerModel model = (CaGridInstallerModel) m;
                        return model.isJBossContainer() && model.isDeployGlobusRequired()
                            && model.isConfigureContainerSelected();

                    }

                }));

        deployContainer.getTasks().add(
            new ConditionalTask(new ConfigureJBossTask(model.getMessage("configuring.jboss.title"), model.getMessage("configuring.jboss.title")),
                new Condition() {

                    public boolean evaluate(WizardModel m) {
                        CaGridInstallerModel model = (CaGridInstallerModel) m;
                        return model.isJBossContainer() && model.isConfigureContainerSelected();
                    }

                }));

    }


    /**
	 * 
	 */
    public JBossComponentInstaller() {

    }


    /*
     * (non-Javadoc)
     * @see
     * org.cagrid.installer.AbstractExternalComponentInstaller#getComponentId()
     */
    @Override
    protected String getComponentId() {
        return "jboss";
    }


    /*
     * (non-Javadoc)
     * @seeorg.cagrid.installer.AbstractExternalComponentInstaller#
     * getShouldCheckCondition()
     */
    @Override
    protected Condition getShouldCheckReinstallCondition() {
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
                return model.isJBossContainer() && (!model.isJBossInstalled() || model.isTrue(Constants.INSTALL_JBOSS));
            }
        };
    }

}
