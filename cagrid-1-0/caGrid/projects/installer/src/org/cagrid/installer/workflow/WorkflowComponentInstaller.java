/**
 * 
 */
package org.cagrid.installer.workflow;

import org.cagrid.installer.CaGridComponentInstaller;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.ConfigureServiceMetadataStep;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.tasks.ConditionalTask;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class WorkflowComponentInstaller implements CaGridComponentInstaller {

	private Condition installWorkflow;

	/**
	 * 
	 */
	public WorkflowComponentInstaller() {
		installWorkflow = new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_WORKFLOW);
			}

		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.ComponentInstaller#addInstallTasks(org.cagrid.installer.model.CaGridInstallerModel,
	 *      org.cagrid.installer.steps.RunTasksStep)
	 */
	public void addInstallTasks(CaGridInstallerModel model,
			RunTasksStep installStep) {

		installStep.getTasks().add(
				new ConditionalTask(new DeployActiveBPELTask(model
						.getMessage("installing.activebpel.title"), ""),
						installWorkflow));

		installStep.getTasks().add(
				new ConditionalTask(new ConfigureBPELAdminSecurityTask(model
						.getMessage("installing.activebpel.title"), "", null),
						installWorkflow));

		installStep.getTasks().add(
				new ConditionalTask(new DeployWorkflowServiceTask(model
						.getMessage("installing.workflow.title"), "",
						"workflow"), installWorkflow));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.ComponentInstaller#addSteps(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	public void addSteps(CaGridInstallerModel model) {
		ConfigureServiceMetadataStep editWorkflowSvcMetaStep = new ConfigureServiceMetadataStep(
				"", model.getMessage("workflow.edit.service.metadata.title"),
				model.getMessage("workflow.edit.service.metadata.desc")) {
			protected String getServiceMetadataPath() {
				return model.getServiceDestDir()
						+ "/workflow/WorkflowFactoryService/etc/serviceMetadata.xml";
			}
		};
		model.add(editWorkflowSvcMetaStep, installWorkflow);

		ServicePropertiesWorkflowFileEditorStep editWorkflowServicePropertiesStep = new ServicePropertiesWorkflowFileEditorStep(
				"workflow", model
						.getMessage("workflow.edit.service.properties.title"),
				model.getMessage("workflow.edit.service.properties.desc"),
				model.getMessage("edit.properties.property.name"), model
						.getMessage("edit.properties.property.value"));
		model.add(editWorkflowServicePropertiesStep, installWorkflow);

		DeployPropertiesWorkflowFileEditorStep editWorkflowDeployPropertiesStep = new DeployPropertiesWorkflowFileEditorStep(
				"workflow", model
						.getMessage("workflow.edit.deploy.properties.title"),
				model.getMessage("workflow.edit.deploy.properties.desc"), model
						.getMessage("edit.properties.property.name"), model
						.getMessage("edit.properties.property.value"));
		model.add(editWorkflowDeployPropertiesStep, installWorkflow);

		ConfigureBPELAdminSecurityStep configureBPELAdminStep = new ConfigureBPELAdminSecurityStep(
				model.getMessage("bpel.admin.config.title"), model
						.getMessage("bpel.admin.config.desc"));
		model.add(configureBPELAdminStep, installWorkflow);
	}

}
