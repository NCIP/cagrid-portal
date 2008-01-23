/**
 * 
 */
package org.cagrid.installer.transfer;

import org.cagrid.installer.CaGridComponentInstaller;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.tasks.CaGridInstallerAntTask;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.tasks.DeployServiceTask;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;


/**
 * @author hastings
 */
public class TransferComponentInstaller implements CaGridComponentInstaller {

    /**
     * 
     */
    public TransferComponentInstaller() {

    }


    /*
     * (non-Javadoc)
     * 
     * @see org.cagrid.installer.ComponentInstaller#addInstallTasks(org.cagrid.installer.model.CaGridInstallerModel,
     *      org.cagrid.installer.steps.RunTasksStep)
     */
    public void addInstallTasks(CaGridInstallerModel model, RunTasksStep installStep) {

        installStep.getTasks().add(
            new ConditionalTask(new DeployServiceTask(model.getMessage("installing.transfer.title"), "", "transfer"),
                new Condition() {

                    public boolean evaluate(WizardModel m) {
                        CaGridInstallerModel model = (CaGridInstallerModel) m;
                        return model.isTrue(Constants.INSTALL_TRANSFER) && !model.isGlobusContainer();
                    }

                }));
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.cagrid.installer.ComponentInstaller#addSteps(org.cagrid.installer.model.CaGridInstallerModel)
     */
    public void addSteps(CaGridInstallerModel model) {

    }

}
