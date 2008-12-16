/**
 * 
 */
package org.cagrid.installer.component;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.tasks.installer.ConfigureTomcatTask;
import org.cagrid.installer.tasks.installer.DeployGlobusToTomcatTask;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;


/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class TomcatComponentInstaller extends AbstractDownloadedComponentInstaller {

    /**
	 * 
	 */
    public TomcatComponentInstaller() {

    }


    /*
     * (non-Javadoc)
     * @see
     * org.cagrid.installer.AbstractExternalComponentInstaller#getComponentId()
     */
    @Override
    protected String getComponentId() {
        return "tomcat";
    }


    /*
     * (non-Javadoc)
     * @seeorg.cagrid.installer.AbstractExternalComponentInstaller#
     * getShouldCheckCondition()
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
                    && (!model.isTomcatInstalled() || model.isTrue(Constants.INSTALL_TOMCAT));
            }
        };
    }


    @Override
    public void addInstallDownloadedComponentTasks(CaGridInstallerModel model, RunTasksStep deployContainer) {
        // TODO Auto-generated method stub
        super.addInstallDownloadedComponentTasks(model, deployContainer);

        deployContainer.getTasks().add(
            new ConditionalTask(new DeployGlobusToTomcatTask(model.getMessage("deploying.globus.tomcat.title"), model.getMessage("deploying.globus.tomcat.title")),
                new Condition() {

                    public boolean evaluate(WizardModel m) {
                        CaGridInstallerModel model = (CaGridInstallerModel) m;
                        return model.isTomcatInstalled() && model.isDeployGlobusRequired()
                            && model.isConfigureContainerSelected();

                    }

                }));

        deployContainer.getTasks().add(
            new ConditionalTask(new ConfigureTomcatTask(model.getMessage("configuring.tomcat.title"), model.getMessage("configuring.tomcat.title")),
                new Condition() {

                    public boolean evaluate(WizardModel m) {
                        CaGridInstallerModel model = (CaGridInstallerModel) m;
                        return model.isTomcatInstalled() && model.isConfigureContainerSelected();
                    }

                }));
    }

}
