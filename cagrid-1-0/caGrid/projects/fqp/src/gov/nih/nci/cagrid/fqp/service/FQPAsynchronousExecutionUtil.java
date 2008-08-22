package gov.nih.nci.cagrid.fqp.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.fqp.common.FQPConstants;
import gov.nih.nci.cagrid.fqp.common.SecurityUtils;
import gov.nih.nci.cagrid.fqp.processor.FederatedQueryEngine;
import gov.nih.nci.cagrid.fqp.results.service.globus.resource.FQPResultResource;
import gov.nih.nci.cagrid.fqp.results.service.globus.resource.FQPResultResourceHome;
import gov.nih.nci.cagrid.fqp.results.stubs.types.FederatedQueryResultsReference;
import gov.nih.nci.cagrid.fqp.stubs.types.InternalErrorFault;

import java.rmi.RemoteException;
import java.util.Calendar;

import org.apache.axis.MessageContext;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.security.SecurityManager;
import org.globus.wsrf.utils.AddressingUtils;

import commonj.work.Work;
import commonj.work.WorkManager;

/**
 * FQPAsynchronousExecutionUtil
 * Performs asynchronous execution of a federated query in the context of 
 * a federated query results resource.
 * 
 * @author ervin
 *
 */
public class FQPAsynchronousExecutionUtil {
    
    private static final int DEFAULT_RESULT_LEASE_MINS = 30;
    
    private static Log LOG = LogFactory.getLog(FQPAsynchronousExecutionUtil.class);

    private FQPResultResourceHome resourceHome;
    private int leaseDurration = DEFAULT_RESULT_LEASE_MINS;
    private WorkManager workManager = null;
    
    public FQPAsynchronousExecutionUtil(FQPResultResourceHome resourceHome, WorkManager workManager) {
        this(resourceHome, workManager, DEFAULT_RESULT_LEASE_MINS);
    }
    
    
    public FQPAsynchronousExecutionUtil(FQPResultResourceHome resourceHome, WorkManager workManager, int leaseDurration) {
        this.resourceHome = resourceHome;
        this.workManager = workManager;
        this.leaseDurration = leaseDurration;
    }


    public FederatedQueryResultsReference executeAsynchronousQuery(DCQLQuery query) throws RemoteException {
        // create an FQP result resource
        FQPResultResource fqpResultResource = null;
        ResourceKey key = null;
        try {
            key = resourceHome.createFQPResultResource();
            fqpResultResource = (FQPResultResource) resourceHome.find(key);
        } catch (Exception e) {
            LOG.error("Problem creating and accessing resource:" + e.getMessage(), e);
            InternalErrorFault fault = new InternalErrorFault();
            fault.setFaultString("Problem creating and accessing resource:" + e.getMessage());
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            throw helper.getFault();
        }

        // configure security on the resource so only the creator of the
        // resource (whoever is executing this) can operate on it
        try {
            // may be null if no current caller
            fqpResultResource.setSecurityDescriptor(SecurityUtils.createResultsResourceSecurityDescriptor());
        } catch (Exception e) {
            LOG.error("Problem configuring security on resource:" + e.getMessage(), e);
            InternalErrorFault fault = new InternalErrorFault();
            fault.setFaultString("Problem configuring security on resource:" + e.getMessage());
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            throw helper.getFault();
        }

        LOG.info("Resource created for, and owned by: " + SecurityManager.getManager().getCaller());

        // set initial status
        fqpResultResource.setStatusMessage("Queued for processing");
        // set to terminate after lease expires
        Calendar termTime = Calendar.getInstance();
        termTime.add(Calendar.MINUTE, leaseDurration);
        fqpResultResource.setTerminationTime(termTime);

        // create a worker thread to execute query
        Work work = new QueryExecutionWork(fqpResultResource, query);

        // add worker to pool
        try {
            workManager.schedule(work);
        } catch (Exception e) {
            LOG.error("Problem scheduling processing:" + e.getMessage(), e);
            InternalErrorFault fault = new InternalErrorFault();
            fault.setFaultString("Problem scheduling processing:" + e.getMessage());
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            throw helper.getFault();
        }

        // return handle to resource
        return createEPR(key);
    }
    

    private FederatedQueryResultsReference createEPR(ResourceKey key) throws RemoteException {
        MessageContext ctx = MessageContext.getCurrentContext();
        String transportURL = (String) ctx.getProperty(org.apache.axis.MessageContext.TRANS_URL);
        transportURL = transportURL.substring(0, transportURL.lastIndexOf('/') + 1);
        transportURL += FQPConstants.RESULTS_SERVICE_NAME;
        try {
            EndpointReferenceType epr = AddressingUtils.createEndpointReference(transportURL, key);
            return new FederatedQueryResultsReference(epr);
        } catch (Exception e) {
            LOG.error("Problem returning reference to result:" + e.getMessage(), e);
            InternalErrorFault fault = new InternalErrorFault();
            fault.setFaultString("Problem returning reference to result:" + e.getMessage());
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            throw helper.getFault();
        }
    }
    
    
    /**
     * Work implemenation which uses the federated query engine to execute the query
     * and store the results in the provided resource.
     * 
     * TODO: use delegation to execute queries
     * 
     * @author oster
     */
    class QueryExecutionWork implements Work {
        FQPResultResource resource;
        DCQLQuery query;


        public QueryExecutionWork(FQPResultResource resource, DCQLQuery query) {
            this.resource = resource;
            this.query = query;
        }


        public void run() {
            FederatedQueryEngine engine = new FederatedQueryEngine();

            try {
                this.resource.setStatusMessage("Processing");
                this.resource.setComplete(false);
                DCQLQueryResultsCollection collection = engine.execute(query);
                this.resource.setResults(collection);
                this.resource.setStatusMessage("Completed");
                this.resource.setComplete(true);
            } catch (Exception e) {
                this.resource.setResults(null);
                this.resource.setComplete(true);
                this.resource.setProcessingException(e);
            }
        }


        public boolean isDaemon() {
            return false;
        }


        public void release() {
            // Do nothing
        }
    }
}
