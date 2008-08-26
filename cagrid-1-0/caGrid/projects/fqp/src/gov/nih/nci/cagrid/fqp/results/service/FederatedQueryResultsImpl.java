package gov.nih.nci.cagrid.fqp.results.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.fqp.results.service.globus.resource.FQPResultResource;
import gov.nih.nci.cagrid.fqp.results.stubs.types.ProcessingNotCompleteFault;
import gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault;

import java.rmi.RemoteException;

import org.globus.wsrf.ResourceContext;


/**
 * TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class FederatedQueryResultsImpl extends FederatedQueryResultsImplBase {

    public FederatedQueryResultsImpl() throws RemoteException {
        super();
    }


    public gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection getResults() throws RemoteException,
        gov.nih.nci.cagrid.fqp.results.stubs.types.ProcessingNotCompleteFault,
        gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault,
        gov.nih.nci.cagrid.fqp.stubs.types.InternalErrorFault {
        FQPResultResource resource = (FQPResultResource) ResourceContext.getResourceContext().getResource();
        if (!resource.isComplete()) {
            ProcessingNotCompleteFault fault = new ProcessingNotCompleteFault();
            fault.setFaultString("The query processing is not yet complete; current status is: "
                + resource.getStatusMessage());
            throw fault;
        } else if (resource.getProcessingException() != null) {
            FederatedQueryProcessingFault fault = new FederatedQueryProcessingFault();
            fault.setFaultString("Problem executing query: " + resource.getProcessingException());
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(resource.getProcessingException());
            throw helper.getFault();
        }
        return resource.getResults();
    }


    public boolean isProcessingComplete() throws RemoteException {
        FQPResultResource resource = (FQPResultResource) ResourceContext.getResourceContext().getResource();
        return resource.isComplete();
    }
}
