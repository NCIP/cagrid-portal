package gov.nih.nci.cagrid.fqp.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.fqp.common.FederatedQueryProcessorConstants;
import gov.nih.nci.cagrid.fqp.common.SecurityUtils;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;
import gov.nih.nci.cagrid.fqp.results.service.globus.resource.FederatedQueryResultsResource;
import gov.nih.nci.cagrid.fqp.results.service.globus.resource.FederatedQueryResultsResourceHome;
import gov.nih.nci.cagrid.fqp.results.stubs.types.FederatedQueryResultsReference;
import gov.nih.nci.cagrid.fqp.results.stubs.types.InternalErrorFault;

import java.util.Calendar;

import org.apache.axis.MessageContext;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.fqp.execution.QueryExecutionParameters;
import org.cagrid.gaards.cds.delegated.stubs.types.DelegatedCredentialReference;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.security.SecurityManager;
import org.globus.wsrf.utils.AddressingUtils;

import commonj.work.WorkManager;

/**
 * FQPAsynchronousExecutionUtil
 * Performs asynchronous execution of a federated query in the context of 
 * a federated query results resource.
 * 
 * @author ervin
 */
public class FQPAsynchronousExecutionUtil {
    
    private static final int DEFAULT_RESULT_LEASE_MINS = 30;
    
    private static Log LOG = LogFactory.getLog(FQPAsynchronousExecutionUtil.class);

    private FederatedQueryResultsResourceHome resourceHome;
    private int leaseDurration = DEFAULT_RESULT_LEASE_MINS;
    private WorkManager workManager = null;
    
    public FQPAsynchronousExecutionUtil(FederatedQueryResultsResourceHome resourceHome, WorkManager workManager) {
        this(resourceHome, workManager, DEFAULT_RESULT_LEASE_MINS);
    }
    
    
    public FQPAsynchronousExecutionUtil(FederatedQueryResultsResourceHome resourceHome, WorkManager workManager, int leaseDurration) {
        this.resourceHome = resourceHome;
        this.workManager = workManager;
        this.leaseDurration = leaseDurration;
    }


    public FederatedQueryResultsReference executeAsynchronousQuery(
        DCQLQuery query, DelegatedCredentialReference delegatedCredential, QueryExecutionParameters executionParameters) 
        throws InternalErrorFault, FederatedQueryProcessingException {
        // use the resource home to create an FQP result resource
        FederatedQueryResultsResource fqpResultResource = null;
        ResourceKey key = null;
        try {
            key = resourceHome.createResource();
            fqpResultResource = (FederatedQueryResultsResource) resourceHome.find(key);
        } catch (Exception e) {
            String message = "Problem creating and accessing resource:" + e.getMessage();
            LOG.error(message, e);
            InternalErrorFault fault = new InternalErrorFault();
            fault.setFaultString(message);
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            throw (InternalErrorFault) helper.getFault();
        }

        // configure security on the resource so only the creator of the
        // resource (whoever is executing this method) can operate on it
        try {
            // may be null if no current caller
            fqpResultResource.setSecurityDescriptor(SecurityUtils.createResultsResourceSecurityDescriptor());
        } catch (Exception e) {
            String message = "Problem configuring caller-only security on resource: " + e.getMessage();
            LOG.error(message, e);
            InternalErrorFault fault = new InternalErrorFault();
            fault.setFaultString(message);
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            throw (InternalErrorFault) helper.getFault();
        }

        LOG.info("Resource created for, and owned by: " + SecurityManager.getManager().getCaller());

        // set to terminate after lease expires
        Calendar termTime = Calendar.getInstance();
        termTime.add(Calendar.MINUTE, leaseDurration);
        fqpResultResource.setTerminationTime(termTime);

        // set the query
        fqpResultResource.setQuery(query);
        
        // set the credential (if any)
        fqpResultResource.setDelegatedCredentialReference(delegatedCredential);
        
        // set the query execution parameters
        fqpResultResource.setQueryExecutionParameters(executionParameters);
        
        // set the work manager for tasks the resource needs to do
        fqpResultResource.setWorkManager(workManager);
        
        // start the resource working on the query
        fqpResultResource.beginQueryProcessing();
        
        // return handle to resource
        FederatedQueryResultsReference resultsReference = createEPR(key);
        return resultsReference;
    }
    

    private FederatedQueryResultsReference createEPR(ResourceKey key) throws InternalErrorFault {
        MessageContext ctx = MessageContext.getCurrentContext();
        String transportURL = (String) ctx.getProperty(org.apache.axis.MessageContext.TRANS_URL);
        transportURL = transportURL.substring(0, transportURL.lastIndexOf('/') + 1);
        transportURL += FederatedQueryProcessorConstants.RESULTS_SERVICE_NAME;
        try {
            EndpointReferenceType epr = AddressingUtils.createEndpointReference(transportURL, key);
            return new FederatedQueryResultsReference(epr);
        } catch (Exception e) {
            LOG.error("Problem returning reference to result:" + e.getMessage(), e);
            InternalErrorFault fault = new InternalErrorFault();
            fault.setFaultString("Problem returning reference to result:" + e.getMessage());
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            throw (InternalErrorFault) helper.getFault();
        }
    }
}
