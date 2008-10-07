package gov.nih.nci.cagrid.fqp.results.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.enumeration.stubs.response.EnumerationResponseContainer;
import gov.nih.nci.cagrid.fqp.processor.DCQLAggregator;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;
import gov.nih.nci.cagrid.fqp.results.service.globus.resource.FederatedQueryResultsResource;
import gov.nih.nci.cagrid.fqp.results.stubs.types.ProcessingNotCompleteFault;
import gov.nih.nci.cagrid.fqp.results.utils.FQPEnumerationExecutionUtil;
import gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault;

import java.rmi.RemoteException;

import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceContextException;
import org.globus.wsrf.ResourceException;


/**
 * TODO:I am the service side implementation class. IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class FederatedQueryResultsImpl extends FederatedQueryResultsImplBase {

    public FederatedQueryResultsImpl() throws RemoteException {
        super();
    }


    public gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection getResults() throws RemoteException,
        gov.nih.nci.cagrid.fqp.results.stubs.types.ProcessingNotCompleteFault,
        gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault,
        gov.nih.nci.cagrid.fqp.results.stubs.types.InternalErrorFault {
        FederatedQueryResultsResource resource = getResource();
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
        FederatedQueryResultsResource resource = getResource();
        return resource.isComplete();
    }


    public gov.nih.nci.cagrid.cqlresultset.CQLQueryResults getAggregateResults() throws RemoteException,
        gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault,
        gov.nih.nci.cagrid.fqp.results.stubs.types.ProcessingNotCompleteFault,
        gov.nih.nci.cagrid.fqp.results.stubs.types.InternalErrorFault {
        FederatedQueryResultsResource resource = getResource();
        if (!resource.isComplete()) {
            FaultHelper helper = new FaultHelper(new ProcessingNotCompleteFault());
            helper.addDescription("Query processing not complete!");
            throw (ProcessingNotCompleteFault) helper.getFault();
        }
        DCQLQueryResultsCollection dcqlResults = resource.getResults();
        CQLQueryResults cqlResults = DCQLAggregator.aggregateDCQLResults(
            dcqlResults, resource.getQuery().getTargetObject().getName());
        return cqlResults;
    }


    public gov.nih.nci.cagrid.enumeration.stubs.response.EnumerationResponseContainer enumerate()
        throws RemoteException, gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault,
        gov.nih.nci.cagrid.fqp.results.stubs.types.ProcessingNotCompleteFault,
        gov.nih.nci.cagrid.fqp.results.stubs.types.InternalErrorFault {
        FederatedQueryResultsResource resource = getResource();
        DCQLQueryResultsCollection dcqlResults = resource.getResults();
        CQLQueryResults cqlResults = DCQLAggregator.aggregateDCQLResults(
            dcqlResults, resource.getQuery().getTargetObject().getName());
        EnumerationResponseContainer response = null;
        try {
            response = FQPEnumerationExecutionUtil.setUpEnumeration(cqlResults);
        } catch (FederatedQueryProcessingException ex) {
            FaultHelper helper = new FaultHelper(new FederatedQueryProcessingFault());
            helper.addDescription("Error setting up WS-Enumeration of results");
            helper.addDescription(ex.getMessage());
            helper.addFaultCause(ex);
            throw (FederatedQueryProcessingFault) helper.getFault();
        }
        return response;
    }


    public org.cagrid.transfer.context.stubs.types.TransferServiceContextReference transfer() throws RemoteException,
        gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault,
        gov.nih.nci.cagrid.fqp.results.stubs.types.ProcessingNotCompleteFault,
        gov.nih.nci.cagrid.fqp.results.stubs.types.InternalErrorFault {
        // TODO: Implement this autogenerated method
        throw new RemoteException("Not yet implemented");
    }

    
    private static FederatedQueryResultsResource getResource() throws ResourceException, ResourceContextException {
        FederatedQueryResultsResource resource = 
            (FederatedQueryResultsResource) ResourceContext.getResourceContext().getResource();
        return resource;
    }
}
