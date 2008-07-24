package gov.nih.nci.cagrid.fqp.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.fqp.common.FQPConstants;
import gov.nih.nci.cagrid.fqp.common.SecurityUtils;
import gov.nih.nci.cagrid.fqp.processor.FederatedQueryEngine;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;
import gov.nih.nci.cagrid.fqp.results.service.globus.resource.FQPResultResource;
import gov.nih.nci.cagrid.fqp.results.service.globus.resource.FQPResultResourceHome;
import gov.nih.nci.cagrid.fqp.results.stubs.types.FederatedQueryResultsReference;
import gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault;
import gov.nih.nci.cagrid.fqp.stubs.types.InternalErrorFault;

import java.rmi.RemoteException;
import java.util.Calendar;

import org.apache.axis.MessageContext;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.work.WorkManagerImpl;
import org.globus.wsrf.security.SecurityManager;
import org.globus.wsrf.utils.AddressingUtils;

import commonj.work.Work;
import commonj.work.WorkManager;


/**
 * Federated Query Service
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class FederatedQueryProcessorImpl extends FederatedQueryProcessorImplBase {
    private static final int DEFAULT_POOL_SIZE = 10;
    private static final int DEFAULT_RESULT_LEASE_MINS = 30;
    
    private WorkManager workManager = null;

    protected static Log LOG = LogFactory.getLog(FederatedQueryProcessorImpl.class.getName());

    public FederatedQueryProcessorImpl() throws RemoteException {
        super();
    }


    public gov.nih.nci.cagrid.cqlresultset.CQLQueryResults executeAndAggregateResults(
        gov.nih.nci.cagrid.dcql.DCQLQuery query) throws RemoteException,
        gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault {
        FederatedQueryEngine engine = new FederatedQueryEngine();
        CQLQueryResults results = null;
        try {
            results = engine.executeAndAggregateResults(query);
        } catch (FederatedQueryProcessingException e) {
            LOG.error("Problem executing query: " + e.getMessage(), e);
            FederatedQueryProcessingFault fault = new FederatedQueryProcessingFault();
            fault.setFaultString("Problem executing query: " + e.getMessage());
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            throw helper.getFault();
        }
        return results;
    }


    public gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection execute(gov.nih.nci.cagrid.dcql.DCQLQuery query)
        throws RemoteException, gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault {
        FederatedQueryEngine engine = new FederatedQueryEngine();
        DCQLQueryResultsCollection results = null;
        try {
            results = engine.execute(query);
        } catch (FederatedQueryProcessingException e) {
            LOG.error("Problem executing query: " + e.getMessage());
            FederatedQueryProcessingFault fault = new FederatedQueryProcessingFault();
            fault.setFaultString("Problem executing query: " + e.getMessage());
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            throw helper.getFault();
        }
        return results;
    }


    public gov.nih.nci.cagrid.fqp.results.stubs.types.FederatedQueryResultsReference executeAsynchronously(
        gov.nih.nci.cagrid.dcql.DCQLQuery query) throws RemoteException {

        // get FQP result resource home
        FQPResultResourceHome resultHome = null;
        try {
            resultHome = (FQPResultResourceHome) getFederatedQueryResultsResourceHome();
        } catch (Exception e) {
            LOG.error("Problem locating result home: " + e.getMessage(), e);
            InternalErrorFault fault = new InternalErrorFault();
            fault.setFaultString("Problem locating result home:" + e.getMessage());
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            throw helper.getFault();
        }

        // create an FQP result resource
        FQPResultResource fqpResultResource = null;
        ResourceKey key = null;
        try {
            key = resultHome.createFQPResultResource();
            fqpResultResource = (FQPResultResource) resultHome.find(key);
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
        termTime.add(Calendar.MINUTE, getLeaseDurationInMinutes());
        fqpResultResource.setTerminationTime(termTime);

        // create a worker thread to execute query
        Work work = new QueryExecutionWork(fqpResultResource, query);

        // add worker to pool
        try {
            getWorkManager().schedule(work);
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


    private gov.nih.nci.cagrid.fqp.results.stubs.types.FederatedQueryResultsReference createEPR(ResourceKey key)
        throws RemoteException {
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


    public int getLeaseDurationInMinutes() {
        int mins = DEFAULT_RESULT_LEASE_MINS;
        try {
            String leaseProp = getConfiguration().getInitialResultLeaseInMinutes();
            LOG.debug("Result Lease Minutes property was:" + leaseProp);
            mins = Integer.parseInt(leaseProp);
        } catch (Exception e) {
            LOG.error("Problem determing result lease duration, using default(" + mins + ").", e);
        }
        return mins;
    }


    public synchronized WorkManager getWorkManager() {
        if (this.workManager == null) {
            int poolSize = DEFAULT_POOL_SIZE;
            try {
                String poolString = getConfiguration().getThreadPoolSize();
                LOG.debug("ThreadPoolSize property was:" + poolString);
                poolSize = Integer.parseInt(poolString);
            } catch (Exception e) {
                LOG.error("Problem determing pool size, using default(" + poolSize + ").", e);
            }
            this.workManager = new WorkManagerImpl(poolSize);
        }

        return this.workManager;
    }


    public synchronized void setWorkManager(WorkManager workManager) {
        this.workManager = workManager;
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
