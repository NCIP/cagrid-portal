package gov.nih.nci.cagrid.workflow.service.impl.service.globus.resource;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.workflow.factory.service.TavernaWorkflowServiceConfiguration;
import gov.nih.nci.cagrid.workflow.service.impl.service.globus.resource.WorkflowLauncherWrapper;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;

import workflowmanagementfactoryservice.StartInputType;
import workflowmanagementfactoryservice.WMSInputType;
import workflowmanagementfactoryservice.WMSOutputType;
import workflowmanagementfactoryservice.WorkflowOutputType;
import workflowmanagementfactoryservice.WorkflowStatusType;


/** 
 * The implementation of this TavernaWorkflowServiceImplResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */


public class TavernaWorkflowServiceImplResource extends TavernaWorkflowServiceImplResourceBase {

	private String scuflDoc = null;
	private String outputDoc = null;
	private String inputDoc = null;
	private String baseDir = null;

	private WorkflowStatusType workflowStatus= WorkflowStatusType.Pending;

	private static TavernaWorkflowServiceConfiguration config = null;

	public TavernaWorkflowServiceImplResource() {
		try {
			config = TavernaWorkflowServiceConfiguration.getConfiguration();
			baseDir = config.getBaseRepositoryDir();
			//System.out.println("basedir:" + System.getenv("HOME"));
			if (baseDir.equals("$home/Library/Application Support/Taverna-1.7.1/repository/"))
			{
				baseDir = baseDir.replaceAll("\\$home", System.getenv("HOME"));
			}
			//System.out.println("New Resource Created: " + this.getResourceKey().toString());
			System.out.println("\n\nThe Taverna Basedir is set to: " + baseDir);
			System.out.println("NOTE: Please set the Taverna base directly correctly. This can be set in the service.properties file of service code.\n\n");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getBaseDir() {
		return baseDir;
	}


	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public String getInputDoc() {
		return inputDoc;
	}

	public void setInputDoc(String inputDoc) {
		this.inputDoc = inputDoc;
	}

	public String getOutputDoc() {
		return outputDoc;
	}

	public void setOutputDoc(String workflowOuput) {
		this.outputDoc = workflowOuput;
	}

	public String getScuflDoc() {
		return scuflDoc;
	}

	public void setScuflDoc(String scuflDoc) {
		this.scuflDoc = scuflDoc;
	}



	public void createWorkflow(WMSInputType wMSInputElement)
	{
		try {

			String [] keys = this.getResourceKey().toString().trim().split("TavernaWorkflowServiceImplResultsKey=");
			System.out.println("Workflow NAME :" + wMSInputElement.getWorkflowName());

			String scuflDocTemp = "/tmp/" + keys[1] + "--input.xml";
			Utils.stringBufferToFile(new StringBuffer(wMSInputElement.getScuflDoc()), scuflDocTemp);
			this.setScuflDoc(scuflDocTemp);

			String output = "/tmp/" + keys[1] + "--output.xml";
			this.setOutputDoc(output);



		} catch (Exception e){
			this.workflowStatus = WorkflowStatusType.Failed;
			e.printStackTrace();
			System.exit(1);
		}

	}

	public WorkflowStatusType start (StartInputType startInput) {

		String [] keys = this.getResourceKey().toString().trim().split("TavernaWorkflowServiceImplResultsKey=");
		try {

			if(startInput != null)
			{
				System.out.println("InputFile provided for the workflow.");
				String inputDocTemp = "/tmp/" + keys[1] + "--input.xml";
				Utils.stringBufferToFile(new StringBuffer(startInput.getInputArgs()), inputDocTemp);
				this.setInputDoc(inputDocTemp);
			}

			String [] args = { "-workflow", this.getScuflDoc(), 
					"-outputdoc", this.getOutputDoc(),
					"-basedir", "/Users/sulakhe/Desktop/Library/Application Support/Taverna-1.7.1/repository/",
					"-inputdoc", this.getInputDoc()	};
			System.out.println("In Service resource: \n");
			System.out.println("Launching WorkflowLauncher: \n");

			new WorkflowLauncherWrapper().run(args);

			// Need to return the WorkflowStatusType with status as "Done"
			this.workflowStatus = WorkflowStatusType.Done;

		} catch (Exception e) {
			e.printStackTrace();
			this.workflowStatus = WorkflowStatusType.Failed;
		}
		return workflowStatus;
	}

	public WorkflowOutputType getWorkflowOutput()
	{
		WorkflowOutputType workflowOuputElement = new WorkflowOutputType();
		try {
			if( (new File(this.getOutputDoc()).exists()) && (this.workflowStatus.equals(WorkflowStatusType.Done)))
			{
				workflowOuputElement.setOutputFile(Utils.fileToStringBuffer( new File(this.getOutputDoc())).toString());
			}
			else
			{
				throw new RemoteException("Either the Workflow execution is not Completed or Failed.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return workflowOuputElement;
	}


	public WorkflowStatusType getStatus()
	{
		return this.workflowStatus;
	}

}
