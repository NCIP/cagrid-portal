package gov.nih.nci.cagrid.fqp.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.fqp.processor.FederatedQueryEngine;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;
import gov.nih.nci.cagrid.fqp.results.service.globus.resource.FederatedQueryResultsResourceHome;
import gov.nih.nci.cagrid.fqp.results.stubs.types.FederatedQueryResultsReference;
import gov.nih.nci.cagrid.fqp.results.stubs.types.InternalErrorFault;
import gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault;

import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.impl.work.WorkManagerImpl;

import commonj.work.WorkManager;


/**
 * TODO:I am the service side implementation class. IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class FederatedQueryProcessorImpl extends FederatedQueryProcessorImplBase {
    private static final int DEFAULT_POOL_SIZE = 10;

    protected static Log LOG = LogFactory.getLog(FederatedQueryProcessorImpl.class.getName());

    private FQPAsynchronousExecutionUtil asynchronousExecutor = null;
    private WorkManager workManager = null;


    public FederatedQueryProcessorImpl() throws RemoteException {
        super();
    }


    public gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection execute(gov.nih.nci.cagrid.dcql.DCQLQuery query)
        throws RemoteException, gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault {
        FederatedQueryEngine engine = new FederatedQueryEngine(null, null);
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


    public gov.nih.nci.cagrid.cqlresultset.CQLQueryResults executeAndAggregateResults(
        gov.nih.nci.cagrid.dcql.DCQLQuery query) throws RemoteException,
        gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault {
        FederatedQueryEngine engine = new FederatedQueryEngine(null, null);
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


    public gov.nih.nci.cagrid.fqp.results.stubs.types.FederatedQueryResultsReference executeAsynchronously(
        gov.nih.nci.cagrid.dcql.DCQLQuery query) throws RemoteException {
        FederatedQueryResultsReference ref = null;
        try {
            ref = getAsynchronousExecutor().executeAsynchronousQuery(query, null, null);
        } catch (FederatedQueryProcessingException ex) {
            throw new RemoteException("Error setting up resource: " + ex.getMessage(), ex);
        }
        return ref;
    }


    public gov.nih.nci.cagrid.fqp.results.stubs.types.FederatedQueryResultsReference query(
        gov.nih.nci.cagrid.dcql.DCQLQuery query,
        org.cagrid.gaards.cds.delegated.stubs.types.DelegatedCredentialReference delegatedCredentialReference,
        org.cagrid.fqp.execution.QueryExecutionParameters queryExecutionParameters) throws RemoteException {
        FederatedQueryResultsReference ref = null;
        try {
            ref = getAsynchronousExecutor().executeAsynchronousQuery(query, delegatedCredentialReference,
                queryExecutionParameters);
        } catch (FederatedQueryProcessingException ex) {
            throw new RemoteException("Error setting up resource: " + ex.getMessage(), ex);
        }
        return ref;
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


    private synchronized FQPAsynchronousExecutionUtil getAsynchronousExecutor() throws InternalErrorFault {
        if (asynchronousExecutor == null) {
            // get FQP result resource home
            FederatedQueryResultsResourceHome resultHome = null;
            try {
                resultHome = getFederatedQueryResultsResourceHome();
            } catch (Exception e) {
                LOG.error("Problem locating result home: " + e.getMessage(), e);
                InternalErrorFault fault = new InternalErrorFault();
                fault.setFaultString("Problem locating result home:" + e.getMessage());
                FaultHelper helper = new FaultHelper(fault);
                helper.addFaultCause(e);
                throw (InternalErrorFault) helper.getFault();
            }

            // determine the resource lease time
            int leaseMinutes = -1;
            try {
                String leaseProp = getConfiguration().getInitialResultLeaseInMinutes();
                LOG.debug("Result Lease Minutes property was:" + leaseProp);
                leaseMinutes = Integer.parseInt(leaseProp);
            } catch (Exception e) {
                LOG.error("Problem determing result lease duration, using default");
            }

            // create the executor instance
            if (leaseMinutes == -1) {
                asynchronousExecutor = new FQPAsynchronousExecutionUtil(resultHome, getWorkManager());
            } else {
                asynchronousExecutor = new FQPAsynchronousExecutionUtil(resultHome, getWorkManager());
            }
        }
        return asynchronousExecutor;
    }
}
