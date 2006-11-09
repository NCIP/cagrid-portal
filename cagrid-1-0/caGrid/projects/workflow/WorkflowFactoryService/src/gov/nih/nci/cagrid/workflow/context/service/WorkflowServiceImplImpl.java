package gov.nih.nci.cagrid.workflow.context.service;

import gov.nih.nci.cagrid.workflow.context.service.globus.resource.WorkflowResource;
import gov.nih.nci.cagrid.workflow.context.stubs.types.StartCalledOnStartedWorkflowFaultType;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowExceptionType;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowOutputType;

import java.rmi.RemoteException;

import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceContextException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceHome;
import org.globus.wsrf.ResourceKey;

/**
 * 
 */
public class WorkflowServiceImplImpl extends WorkflowServiceImplImplBase {

	public WorkflowServiceImplImpl() throws RemoteException {
		super();
	}

	public gov.nih.nci.cagrid.workflow.stubs.types.WorkflowStatusType start(gov.nih.nci.cagrid.workflow.stubs.types.StartInputType startInputElement) throws RemoteException, gov.nih.nci.cagrid.workflow.stubs.types.WorkflowExceptionType, gov.nih.nci.cagrid.workflow.context.stubs.types.StartCalledOnStartedWorkflowFaultType {
		gov.nih.nci.cagrid.workflow.stubs.types.WorkflowStatusType status = null;
		WorkflowResource resource = null;
		try {
			resource = getWorkflowResource();
			status = resource.start(startInputElement);
		} catch (ResourceException nsre) {
			throw new RemoteException("Exception starting workflow", nsre);
		}
		return status;
	}

	private WorkflowResource getWorkflowResource() throws ResourceException, ResourceContextException {
		ResourceContext resourceContext = ResourceContext.getResourceContext();
		ResourceHome resourceHome = resourceContext.getResourceHome();
		ResourceKey resourceKey = resourceContext.getResourceKey();
		WorkflowResource resource = (WorkflowResource) resourceHome.find(resourceKey);
		return resource;
	}

	public gov.nih.nci.cagrid.workflow.stubs.types.WorkflowStatusType getStatus() throws RemoteException, gov.nih.nci.cagrid.workflow.stubs.types.WorkflowExceptionType {
		return getWorkflowResource().getStatus();
	}

	public gov.nih.nci.cagrid.workflow.stubs.types.WorkflowStatusType pause() throws RemoteException, gov.nih.nci.cagrid.workflow.stubs.types.WorkflowExceptionType, gov.nih.nci.cagrid.workflow.context.stubs.types.CannotPauseWorkflowFaultType {
		return getWorkflowResource().pause();
	}

	public gov.nih.nci.cagrid.workflow.stubs.types.WorkflowStatusType resume() throws RemoteException, gov.nih.nci.cagrid.workflow.stubs.types.WorkflowExceptionType, gov.nih.nci.cagrid.workflow.context.stubs.types.CannotResumeWorkflowFaultType {
		return getWorkflowResource().resume();
	}

	public void cancel() throws RemoteException, gov.nih.nci.cagrid.workflow.stubs.types.WorkflowExceptionType, gov.nih.nci.cagrid.workflow.context.stubs.types.CannotCancelWorkflowFaultType {
		getWorkflowResource().cancel();
	}

	public gov.nih.nci.cagrid.workflow.stubs.types.WorkflowOutputType getWorkflowOutput() throws RemoteException, gov.nih.nci.cagrid.workflow.stubs.types.WorkflowExceptionType {
		return getWorkflowResource().getWorkflowOutput();
	}
}
