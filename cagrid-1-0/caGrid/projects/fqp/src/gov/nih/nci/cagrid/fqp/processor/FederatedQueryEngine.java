package gov.nih.nci.cagrid.fqp.processor;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.dcqlresult.DCQLResult;
import gov.nih.nci.cagrid.fqp.common.FederatedQueryProcessorConstants;
import gov.nih.nci.cagrid.fqp.common.SerializationUtils;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;
import gov.nih.nci.cagrid.fqp.processor.exceptions.RemoteDataServiceException;

import java.io.StringWriter;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.fqp.execution.QueryExecutionParameters;
import org.cagrid.fqp.execution.TargetDataServiceQueryBehavior;
import org.globus.gsi.GlobusCredential;
import org.globus.wsrf.impl.work.WorkManagerImpl;

import commonj.work.Work;
import commonj.work.WorkException;
import commonj.work.WorkItem;
import commonj.work.WorkManager;


/**
 * @author Srini Akkala
 * @author Scott Oster
 * @author David Ervin
 */
public class FederatedQueryEngine {
    // default number of worker threads
    public static final int DEFAULT_POOL_SIZE = 5;

    private static Log LOG = LogFactory.getLog(FederatedQueryEngine.class.getName());
    
    private GlobusCredential credential = null;
    private QueryExecutionParameters executionParameters = null;
    private WorkManager workManager = null;
    private List<FQPProcessingStatusListener> statusListeners = null;


    /**
     * Creates a new federated query engine instance.  Either or both parameters
     * may be null.
     * @param credential
     *      The globus credential to be used when making queries against data services (may be null)
     * @param executionParameters
     *      The query execution parameters (may be null)
     */
    public FederatedQueryEngine(GlobusCredential credential, QueryExecutionParameters executionParameters) {
        this(credential, executionParameters, null);
    }
    
    
    /**
     * Creates a new federated query engine instance.
     * @param credential
     *      The globus credential to be used when making queries against data services (may be null)
     * @param executionParameters
     *      The query execution parameters (may be null)
     * @param workManager
     *      The work manager instance which will handle scheduling and execution of query processing tasks
     */
    public FederatedQueryEngine(GlobusCredential credential, QueryExecutionParameters executionParameters, WorkManager workManager) {
        this.credential = credential;
        if (executionParameters == null) {
            this.executionParameters = FederatedQueryProcessorConstants.DEFAULT_QUERY_EXECUTION_PARAMETERS;
        } else {
            this.executionParameters = executionParameters;
        }
        if (workManager == null) {
            this.workManager = new WorkManagerImpl(DEFAULT_POOL_SIZE);
        } else {
            this.workManager = workManager;
        }
        this.statusListeners = new LinkedList<FQPProcessingStatusListener>();
    }


    /**
     * Call Federated Query Processor, and send the generated CQLQuery to each
     * targeted service, placing each results into a single DCQLQueryResults
     * object.
     * 
     * @param dcqlQuery
     * @return The results of executing the DCQL query
     * @throws FederatedQueryProcessingException
     */
    public DCQLQueryResultsCollection execute(DCQLQuery dcqlQuery) throws FederatedQueryProcessingException {
        // create a new processor instance and debug the query
        FederatedQueryProcessor processor = new FederatedQueryProcessor(credential);
        debugDCQLQuery("Beginning processing of DCQL", dcqlQuery);

        // allow the processor to convert the DCQL into a CQL query
        LOG.debug("Processing DCQL to single CQL query");
        CQLQuery cqlQuery = processor.processDCQLQuery(dcqlQuery.getTargetObject());

        LOG.debug("Creating work items for each target data service");
        Map<WorkItem, QueryExecutionContext> workExecution = 
            new HashMap<WorkItem, QueryExecutionContext>();
        for (String url : dcqlQuery.getTargetServiceURL()) {
            QueryExecutionContext executionContext = 
                new QueryExecutionContext(cqlQuery, url, credential, executionParameters);
            Work executor = new QueryExecutor(executionContext);
            try {
                WorkItem workItem = workManager.schedule(executor);
                workExecution.put(workItem, executionContext);
            } catch (WorkException ex) {
                throw new FederatedQueryProcessingException(
                    "Error scheduling query aggregation work: " + ex.getMessage(), ex);
            }
        }
        
        LOG.debug("Work scheduled, waiting for completion");
        workManager.waitForAll(workExecution.keySet(), WorkManager.INDEFINITE);
        LOG.debug("Work completed");
        
        // compile results from all workers
        boolean failFast = true;
        if (executionParameters.getTargetDataServiceQueryBehavior().getFailOnFirstError() != null) {
            failFast =
                executionParameters.getTargetDataServiceQueryBehavior().getFailOnFirstError().booleanValue();
        }
        List<DCQLResult> dcqlResults = new LinkedList<DCQLResult>();
        for (WorkItem worker : workExecution.keySet()) {
            QueryExecutionContext context = workExecution.get(worker);
            LOG.debug("Analyizing results from " + context.getServiceURL());
            CQLQueryResults results = context.getResults();
            Exception processingException = context.getProcessingException();
            if (results != null) {
                LOG.debug("Query returned non-null results");
                // ensure the query results are of the correct data type
                if (!results.getTargetClassname().equals(cqlQuery.getTarget().getName())) {
                    throw new RemoteDataServiceException("Data service (" + context.getServiceURL()
                        + ") returned results of type (" + results.getTargetClassname() 
                        + ") when type (" + cqlQuery.getTarget().getName() + ") was requested!");
                }
                if (results.getObjectResult() != null) {
                    // results are good!
                    DCQLResult dcqlResult = new DCQLResult();
                    dcqlResult.setTargetServiceURL(context.getServiceURL());
                    dcqlResult.setCQLQueryResultCollection(results);
                    dcqlResults.add(dcqlResult);
                } else {
                    FederatedQueryProcessingException ex = new FederatedQueryProcessingException(
                        "Data service " + context.getServiceURL() + " returned non-object results for the query!");
                    fireInvalidResult(context.getServiceURL(), ex);
                    if (failFast) {
                        throw ex;
                    }
                }
            } else if (processingException != null && failFast) {
                throw new FederatedQueryProcessingException(
                    "Data service " + context.getServiceURL() + 
                    " threw a query processing exception: " + processingException.getMessage(), 
                    processingException);
            }
        }
        
        LOG.debug("Compiling results as DCQL query results collection");
        DCQLQueryResultsCollection resultsCollection = new DCQLQueryResultsCollection();
        DCQLResult[] resultArray = new DCQLResult[dcqlResults.size()];
        dcqlResults.toArray(resultArray);
        resultsCollection.setDCQLResult(resultArray);
        return resultsCollection;
    }


    /**
     * Call Federated Query Processor, and send the generated CQLQuery to each
     * targeted service, aggregating the results into a single CQLQueryResults
     * object.
     * 
     * @param dcqlQuery
     * @return Aggregated results of the DCQL query
     * @throws FederatedQueryProcessingException
     */
    public CQLQueryResults executeAndAggregateResults(DCQLQuery dcqlQuery) throws FederatedQueryProcessingException {
        // perform the DCQL query as normal, then aggregate
        DCQLQueryResultsCollection dcqlResults = execute(dcqlQuery);
        
        LOG.debug("Aggregating DCQL results");
        String targetClassname = dcqlQuery.getTargetObject().getName();
        CQLQueryResults aggregate = DCQLAggregator.aggregateDCQLResults(dcqlResults, targetClassname);
        
        return aggregate;
    }


    private void debugDCQLQuery(String logMessage, DCQLQuery dcqlQuery) {
        if (LOG.isDebugEnabled()) {
            try {
                StringWriter s = new StringWriter();
                SerializationUtils.serializeDCQLQuery(dcqlQuery, s);
                LOG.debug(logMessage + ":\n" + s.toString());
                s.close();
            } catch (Exception e) {
                LOG.error("Problem in debug printout of DCQL query:" + e.getMessage(), e);
            }
        }
    }
    
    
    // ------------------------------
    // status listener implementation
    // ------------------------------
    
    
    public void addStatusListener(FQPProcessingStatusListener listener) {
        this.statusListeners.add(listener);
    }
    
    
    public FQPProcessingStatusListener[] getStatusListeners() {
        FQPProcessingStatusListener[] listeners = new FQPProcessingStatusListener[statusListeners.size()];
        statusListeners.toArray(listeners);
        return listeners;
    }
    
    
    public boolean removeStatusListener(FQPProcessingStatusListener listener) {
        return statusListeners.remove(listener);
    }
    
    
    protected synchronized void fireStatusOk(String serviceURL) {
        for (FQPProcessingStatusListener listener : statusListeners) {
            listener.targetServiceOk(serviceURL);
        }
    }
    
    
    protected synchronized void fireConnectionRefused(String serviceURL) {
        for (FQPProcessingStatusListener listener : statusListeners) {
            listener.targetServiceConnectionRefused(serviceURL);
        }
    }
    
    
    protected synchronized void fireServiceExeption(String serviceURL, Exception ex) {
        for (FQPProcessingStatusListener listener : statusListeners) {
            listener.targetServiceThrowsException(serviceURL, ex);
        }
    }
    
    
    protected synchronized void fireInvalidResult(String serviceURL, FederatedQueryProcessingException ex) {
        for (FQPProcessingStatusListener listener : statusListeners) {
            listener.targetServiceReturnedInvalidResult(serviceURL, ex);
        }
    }
    
    
    private static class QueryExecutionContext {
        private CQLQuery query = null;
        private String serviceURL = null;
        
        private GlobusCredential credential = null;
        private QueryExecutionParameters executionParameters = null;
        
        private CQLQueryResults results = null;
        private Exception processingException = null;
        
        public QueryExecutionContext(CQLQuery query, String serviceURL, GlobusCredential credential, QueryExecutionParameters executionParameters) {
            this.query = query;
            this.serviceURL = serviceURL;
            this.credential = credential;
            this.executionParameters = executionParameters;
        }


        public GlobusCredential getCredential() {
            return credential;
        }
        
        
        public QueryExecutionParameters getExecutionParameters() {
            return executionParameters;
        }
        

        public Exception getProcessingException() {
            return processingException;
        }


        public void setProcessingException(Exception processingException) {
            this.processingException = processingException;
        }


        public CQLQuery getQuery() {
            return query;
        }


        public CQLQueryResults getResults() {
            return results;
        }


        public void setResults(CQLQueryResults results) {
            this.results = results;
        }


        public String getServiceURL() {
            return serviceURL;
        }
    }
    
    
    // NON-STATIC so it can call the fire..() methods to immediatly update status
    private class QueryExecutor implements Work {
        private QueryExecutionContext queryContext = null;
        
        public QueryExecutor(QueryExecutionContext queryContext) {
            this.queryContext = queryContext;
        }
        
        
        public boolean isDaemon() {
            return false;
        }
        
        
        public void release() {
            // nothing to release
        }
        
        
        public void run() {
            LOG.debug("Querying target data service " + queryContext.getServiceURL());
            TargetDataServiceQueryBehavior behavior = 
                queryContext.getExecutionParameters().getTargetDataServiceQueryBehavior();
            CQLQueryResults results = null;
            Exception queryException = null;
            
            int maxRetries = behavior.getRetries() != null ? 
                behavior.getRetries().intValue() : 
                FederatedQueryProcessorConstants.DEFAULT_TARGET_QUERY_BEHAVIOR.getRetries().intValue();
            int tryCount = 0;
            long retryTimeout = (behavior.getTimeoutPerRetry() != null ?
                behavior.getTimeoutPerRetry().intValue() :
                FederatedQueryProcessorConstants.DEFAULT_TARGET_QUERY_BEHAVIOR.getTimeoutPerRetry().intValue())
                * 1000; // miliseconds
            
            do {
                tryCount++;
                try {
                    results = DataServiceQueryExecutor.queryDataService(
                        queryContext.getQuery(), queryContext.getServiceURL(), queryContext.getCredential());
                } catch (Exception ex) {
                    LOG.warn("Query failed to execute: " + ex.getMessage());
                    queryException = ex;
                    if (ex instanceof ConnectException && tryCount < maxRetries) {
                        // connection refused, so we'll come back later
                        try {
                            Thread.sleep(retryTimeout);
                        } catch (InterruptedException iex) {
                            // these things happen
                        }
                    }
                }
            } while (results == null && tryCount < maxRetries);
            
            LOG.debug("Done querying data service " + queryContext.getServiceURL());
            
            if (results != null) {
                queryContext.setResults(results);
                fireStatusOk(queryContext.getServiceURL());
            } else if (queryException != null) {
                queryContext.setProcessingException(queryException);
                if (queryException instanceof ConnectException) {
                    fireConnectionRefused(queryContext.getServiceURL());
                } else {
                    fireServiceExeption(queryContext.getServiceURL(), queryException);
                }
            } else {
                fireInvalidResult(queryContext.getServiceURL(), 
                    new FederatedQueryProcessingException("Service returned no results!"));
            }
        }
    }
}
