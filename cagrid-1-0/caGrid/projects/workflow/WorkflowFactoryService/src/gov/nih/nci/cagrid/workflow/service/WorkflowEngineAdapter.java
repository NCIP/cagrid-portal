package gov.nih.nci.cagrid.workflow.service;

import gov.nih.nci.cagrid.workflow.stubs.types.StartInputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WSDLReferences;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowExceptionType;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowStatusType;

public interface WorkflowEngineAdapter {
	
	public String deployWorkflow(String bpelFileName, 
			String workflowName, 
			WSDLReferences[] wsdlRefArray) throws WorkflowExceptionType;
	
	public WorkflowStatusType startWorkflow(String workflowName, StartInputType startInput) throws WorkflowExceptionType;
	
	public WorkflowStatusType getWorkflowStatus(String workflowName) throws WorkflowExceptionType;
	
	public void suspend(String workflowName) throws WorkflowExceptionType;
	
	public void resume(String workflowName) throws WorkflowExceptionType;
	
	public void cancel(String workflowName) throws WorkflowExceptionType;
	
}