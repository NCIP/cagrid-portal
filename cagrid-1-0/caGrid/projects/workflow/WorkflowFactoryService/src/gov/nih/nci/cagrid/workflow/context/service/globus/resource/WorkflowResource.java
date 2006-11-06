package gov.nih.nci.cagrid.workflow.context.service.globus.resource;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.workflow.context.stubs.types.StartCalledOnStartedWorkflowFaultType;
import gov.nih.nci.cagrid.workflow.service.ActiveBPELAdapter;
import gov.nih.nci.cagrid.workflow.service.WorkflowFactoryServiceImpl;
import gov.nih.nci.cagrid.workflow.stubs.types.StartInputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WMSInputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WSDLReferences;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowExceptionType;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowStatusType;

import java.io.File;
import java.util.Calendar;

import org.globus.wsrf.Resource;

public class WorkflowResource implements Resource {

	public String workflowName = null;

	public String bpelFileName = null;

	public String bpelProcess = null;

	public static String BPEL_EXTENSION = ".bpel";

	private boolean started = false;
	
	private String partnerLinkName = null;
	
	private WorkflowStatusType workflowStatus = WorkflowStatusType.Pending;
	
	private static ActiveBPELAdapter abAdapter = new ActiveBPELAdapter(null);
	
	
	public WorkflowResource(WMSInputType input, Calendar terminationTime)
			throws Exception {
		File bpelFile = null;
		this.workflowName = input.getWorkflowName();
		this.bpelProcess = input.getBpelDoc();
		// TODO Auto-generated constructor stub
		String bpelFileName = System.getProperty("java.io.tmpdir")
				+ File.separator + workflowName + BPEL_EXTENSION;
		bpelFile = new File(bpelFileName);
		bpelFile.deleteOnExit();
		Utils.stringBufferToFile(new StringBuffer(bpelProcess), bpelFileName);
		WSDLReferences[] wsdlRefArray = input.getWsdlReferences();
		String returnString = deploy(bpelFileName, workflowName, wsdlRefArray);
		System.out.println("Created a Workflow resource with key:" 
				+ input.getWorkflowName());
	}

	private String deploy(String bpelFileName, String workflowName,
			WSDLReferences[] wsdlRefArray) throws Exception {
		return abAdapter.deployBpr(bpelFileName,
				workflowName, wsdlRefArray);
	}

	public WorkflowStatusType start(StartInputType startInput) 
		throws WorkflowExceptionType, StartCalledOnStartedWorkflowFaultType {
		this.partnerLinkName = "SimpleService"; 
		if (this.started) {
			throw new StartCalledOnStartedWorkflowFaultType();
		}
		try {
			this.abAdapter.startWorkflow(this.partnerLinkName, startInput);
			this.workflowStatus = WorkflowStatusType.Active;
		} catch (Exception e) {
			this.workflowStatus = WorkflowStatusType.Failed;
			throw new WorkflowExceptionType();
		}
		return this.workflowStatus;
	}

	public WorkflowStatusType getStatus() {
		return this.workflowStatus;
	}
	
	public WorkflowStatusType pause() throws WorkflowExceptionType {
		this.abAdapter.suspend(this.workflowName);
		this.workflowStatus = WorkflowStatusType.Pending;
		return this.workflowStatus;
	}
	
	public WorkflowStatusType resume() throws WorkflowExceptionType {
		this.abAdapter.resume(this.workflowName);
		this.workflowStatus = WorkflowStatusType.Active;
		return this.workflowStatus;
	}
	
	public void cancel() throws WorkflowExceptionType {
		this.abAdapter.cancel(this.workflowName);
	}
}
