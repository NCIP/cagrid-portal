package gov.nih.nci.cagrid.workflow.service;

import gov.nih.nci.cagrid.workflow.stubs.types.StartInputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WSDLReferences;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowExceptionType;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowStatusType;

public interface WorkflowEngineAdapter {
	
	public String deployWorkflow(String bpelFileName, 
			String workflowName, 
			WSDLReferences[] wsdlRefArray) throws WorkflowExceptionType;
	
	public WorkflowStatusType startWorkflow(StartInputType startInput) throws WorkflowExceptionType;
	
	public WorkflowStatusType getWorkflowStatus() throws WorkflowExceptionType;
	
	public void suspend() throws WorkflowExceptionType;
	
	public void resume() throws WorkflowExceptionType;
	
	public void cancel() throws WorkflowExceptionType;
	
}