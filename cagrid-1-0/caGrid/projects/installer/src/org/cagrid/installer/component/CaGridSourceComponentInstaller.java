/**
 * 
 */
package org.cagrid.installer.component;

import java.util.Map;
import java.util.MissingResourceException;

import javax.swing.text.LabelView;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.steps.options.BooleanPropertyConfigurationOption;
import org.cagrid.installer.steps.options.FilePropertyConfigurationOption;
import org.cagrid.installer.steps.options.ListPropertyConfigurationOption;
import org.cagrid.installer.steps.options.ListPropertyConfigurationOption.LabelValuePair;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.tasks.cagrid.CompileCaGridTask;
import org.cagrid.installer.tasks.cagrid.ConfigureTargetGridTask;
import org.cagrid.installer.util.InstallerUtils;
import org.cagrid.installer.validator.Validator;
import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

import sun.text.Normalizer.Mode;


/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class CaGridSourceComponentInstaller extends AbstractDownloadedComponentInstaller {

    private static final Log logger = LogFactory.getLog(CaGridSourceComponentInstaller.class);


    /**
     * 
     */
    public CaGridSourceComponentInstaller() {

    }


    /*
     * (non-Javadoc)
     * @see
     * org.cagrid.installer.AbstractDownloadedComponentInstaller#getComponentId
     * ()
     */
    @Override
    protected String getComponentId() {
        return "cagrid";
    }


    /*
     * (non-Javadoc)
     * @seeorg.cagrid.installer.AbstractDownloadedComponentInstaller#
     * getShouldCheckCondition()
     */
    @Override
    protected Condition getShouldCheckReinstallCondition() {
        return new Condition() {
            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return model.isTrue(Constants.INSTALL_CONFIGURE_CAGRID) && model.isCaGridInstalled();
            }
        };
    }


    /*
     * (non-Javadoc)
     * @seeorg.cagrid.installer.AbstractDownloadedComponentInstaller#
     * getShouldInstallCondition()
     */
    @Override
    protected Condition getShouldInstallCondition() {
        return new Condition() {
            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return model.isTrue(Constants.INSTALL_CONFIGURE_CAGRID) && (!model.isCaGridInstalled() || model.isTrue(Constants.REINSTALL_CAGRID));
            }
        };
    }


    /*
     * (non-Javadoc)
     * @see
     * org.cagrid.installer.ExternalComponentInstaller#addCheckInstallSteps(
     * org.cagrid.installer.model.CaGridInstallerModel)
     */
    public void addCheckInstallSteps(CaGridInstallerModel model) {
        
super.addCheckInstallSteps(model);
    }


    public void addInstallDownloadedComponentTasks(CaGridInstallerModel model, RunTasksStep installStep) {

        super.addInstallDownloadedComponentTasks(model, installStep);

        installStep.getTasks().add(
            new ConditionalTask(new CompileCaGridTask(model.getMessage("compiling.cagrid.title"), model
                .getMessage("compiling.cagrid.title")), new Condition() {

                public boolean evaluate(WizardModel m) {
                    CaGridInstallerModel model = (CaGridInstallerModel) m;
                    return model.isTrue(Constants.INSTALL_CONFIGURE_CAGRID) && (!model.isCaGridInstalled() || model.isTrue(Constants.REINSTALL_CAGRID));
                }

            }));
    }

}
