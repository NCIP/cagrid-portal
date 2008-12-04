/**
 * 
 */
package org.cagrid.installer.component;

import java.util.Map;
import java.util.MissingResourceException;

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
import org.cagrid.installer.tasks.CompileCaGridTask;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.tasks.ConfigureTargetGridTask;
import org.cagrid.installer.util.InstallerUtils;
import org.cagrid.installer.validator.Validator;
import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;


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
     * 
     * @see org.cagrid.installer.AbstractDownloadedComponentInstaller#getComponentId()
     */
    @Override
    protected String getComponentId() {
        return "cagrid";
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
                return model.isCaGridInstalled();
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
                return model.isTrue(Constants.INSTALL_CAGRID);
            }
        };
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.cagrid.installer.ExternalComponentInstaller#addCheckInstallSteps(org.cagrid.installer.model.CaGridInstallerModel)
     */
    public void addCheckInstallSteps(CaGridInstallerModel model) {
        super.addCheckInstallSteps(model);

        // If user has NOT selected to install caGrid, and installer
        // has not located it, ask where it is.
        FilePropertyConfigurationOption browseToCaGridOpt = new FilePropertyConfigurationOption(Constants.CAGRID_HOME,
            model.getMessage("directory"), "", true);
        browseToCaGridOpt.setBrowseLabel(model.getMessage("browse"));
        browseToCaGridOpt.setDirectoriesOnly(true);
        PropertyConfigurationStep checkCaGridInstalledStep = new PropertyConfigurationStep(model
            .getMessage("check.cagrid.installed.title"), model.getMessage("check.cagrid.installed.desc"));
        checkCaGridInstalledStep.getOptions().add(browseToCaGridOpt);
        checkCaGridInstalledStep.getValidators().add(new Validator() {

            public void validate(Map state) throws InvalidStateException {
                String caGridHome = (String) state.get(Constants.CAGRID_HOME);
                if (!InstallerUtils.checkCaGridIsValid(caGridHome)) {
                    // TODO: externalize this message
                    throw new InvalidStateException("" + caGridHome
                        + "' does not point to a valid caGrid installation.");
                }
            }

        });
        model.add(checkCaGridInstalledStep, new Condition() {
            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return !model.isTrue(Constants.INSTALL_CAGRID) && !model.isCaGridInstalled();
            }
        });

        // Check if caGrid should be reconfigured
        PropertyConfigurationStep checkReconfigureCaGridStep = new PropertyConfigurationStep(model
            .getMessage("check.reconfigure.cagrid.title"), model.getMessage("check.reconfigure.cagrid.desc"));
        checkReconfigureCaGridStep.getOptions()
            .add(
                new BooleanPropertyConfigurationOption(Constants.RECONFIGURE_CAGRID, model.getMessage("yes"), false,
                    false));
        model.add(checkReconfigureCaGridStep, new Condition() {
            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return model.isCaGridInstalled() && model.isSet(Constants.TARGET_GRID)
                    && !model.isTrue(Constants.INSTALL_CAGRID);
            }
        });

        // Presents list of available target grids
        String[] targetGrids = model.getProperty(Constants.AVAILABLE_TARGET_GRIDS).split(",");
        PropertyConfigurationStep selectTargetGridStep = new PropertyConfigurationStep(model
            .getMessage("select.target.grid.title"), model.getMessage("select.target.grid.desc"));
        LabelValuePair[] targetGridPairs = new LabelValuePair[targetGrids.length];
        for (int i = 0; i < targetGrids.length; i++) {
            String targetGridLabel = targetGrids[i];
            try {
                targetGridLabel = model.getMessage("target.grid." + targetGrids[i] + ".label");
            } catch (MissingResourceException ex) {
                logger.warn("Couldn't find label for target grid '" + targetGrids[i] + "'.");
            }
            targetGridPairs[i] = new LabelValuePair(targetGridLabel, targetGrids[i]);
        }
        selectTargetGridStep.getOptions().add(
            new ListPropertyConfigurationOption(Constants.TARGET_GRID, model.getMessage("target.grid"),
                targetGridPairs, true));
        model.add(selectTargetGridStep, new Condition() {
            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return !model.isSet(Constants.TARGET_GRID) || model.isTrue(Constants.RECONFIGURE_CAGRID);
            }
        });

    }


    public void addInstallDownloadedComponentTasks(CaGridInstallerModel model, RunTasksStep installStep) {

        super.addInstallDownloadedComponentTasks(model, installStep);

        installStep.getTasks().add(
            new ConditionalTask(new CompileCaGridTask(model.getMessage("compiling.cagrid.title"), ""), new Condition() {

                public boolean evaluate(WizardModel m) {
                    CaGridInstallerModel model = (CaGridInstallerModel) m;
                    return model.isTrue(Constants.INSTALL_CAGRID);
                }

            }));

        // TODO: figure out why this fails on Mac sometimes
        ConfigureTargetGridTask configTargetGridTask = new ConfigureTargetGridTask(model
            .getMessage("configuring.target.grid"), "");
        configTargetGridTask.setAbortOnError(false);
        installStep.getTasks().add(new ConditionalTask(configTargetGridTask, new Condition() {
            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return !model.isSet(Constants.TARGET_GRID) || model.isTrue(Constants.RECONFIGURE_CAGRID)
                    || model.isTrue(Constants.INSTALL_CAGRID);
            }
        }));
    }

}
