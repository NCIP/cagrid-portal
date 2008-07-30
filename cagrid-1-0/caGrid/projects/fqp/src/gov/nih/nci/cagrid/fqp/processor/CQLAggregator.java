package gov.nih.nci.cagrid.fqp.processor;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLObjectResult;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;
import gov.nih.nci.cagrid.fqp.processor.exceptions.RemoteDataServiceException;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.gsi.GlobusCredential;
import org.globus.wsrf.impl.work.WorkManagerImpl;

import commonj.work.Work;
import commonj.work.WorkException;
import commonj.work.WorkItem;
import commonj.work.WorkManager;

/** 
 *  CQLAggregator
 *  Performs DCQL Aggregation queries in paralell using the Globus 
 *  implementation of the commonj work interfaces
 * 
 * @author David Ervin
 * 
 * @created Jul 28, 2008 1:16:46 PM
 * @version $Id: CQLAggregator.java,v 1.3 2008-07-30 13:38:06 dervin Exp $ 
 */
public class CQLAggregator {
    
    // default number of worker threads
    public static final int DEFAULT_POOL_SIZE = 5;

    private static Log logger = LogFactory.getLog(CQLAggregator.class);
    private static WorkManager workManager = null;
    
    private CQLAggregator() {
        // prevents instantiation
    }
    
    
    /**
     * Performs a CQL query aggregation
     * 
     * @param query
     *      The CQL query to aggregate across multiple data services
     * @param dataServiceUrls
     *      The URLs of the data services
     * @param credential
     *      <b>Can be null.</b> The globus credential to connect with
     * @return
     *      The aggregated object results of the CQL query
     * @throws RemoteDataServiceException
     * @throws FederatedQueryProcessingException
     */
    public static CQLQueryResults aggregateQueryResults(
        CQLQuery query, String[] dataServiceUrls, GlobusCredential credential) 
        throws RemoteDataServiceException, FederatedQueryProcessingException {
        logger.debug("Creating object results list");
        List<CQLObjectResult> objectResults = new LinkedList<CQLObjectResult>();
        
        logger.debug("Creating work items for each data service");
        Map<WorkItem, QueryExecutionContext> workExecution = 
            new HashMap<WorkItem, QueryExecutionContext>();
        for (String url : dataServiceUrls) {
            QueryExecutionContext executionContext = new QueryExecutionContext(query, url, credential);
            Work executor = new QueryExecutor(executionContext);
            try {
                WorkItem workItem = getWorkManager().schedule(executor);
                workExecution.put(workItem, executionContext);
            } catch (WorkException ex) {
                throw new FederatedQueryProcessingException(
                    "Error scheduling query aggregation work: " + ex.getMessage(), ex);
            }
        }
        
        logger.debug("Work scheduled, waiting for completion");
        getWorkManager().waitForAll(workExecution.keySet(), WorkManager.INDEFINITE);
        logger.debug("Work completed");
        
        // aggregate results from all workers
        for (WorkItem worker : workExecution.keySet()) {
            QueryExecutionContext context = workExecution.get(worker);
            logger.debug("Analyizing results from " + context.getServiceURL());
            CQLQueryResults results = context.getResults();
            Exception processingException = context.getProcessingException();
            if (results != null) {
                logger.debug("Query returned non-null results");
                // ensure the query results are of the correct data type
                if (!results.getTargetClassname().equals(query.getTarget().getName())) {
                    throw new RemoteDataServiceException("Data service (" + context.getServiceURL()
                        + ") returned results of type (" + results.getTargetClassname() 
                        + ") when type (" + query.getTarget().getName() + ") was requested!");
                }
                if (results.getObjectResult() != null) {
                    // add the results together
                    Collections.addAll(objectResults, results.getObjectResult());
                } else {
                    throw new FederatedQueryProcessingException(
                        "Data service " + context.getServiceURL() + " returned non-object results for the query!");
                }
            } else if (processingException != null) {
                throw new FederatedQueryProcessingException(
                    "Data service " + context.getServiceURL() + 
                    " threw a query processing exception: " + processingException.getMessage(), 
                    processingException);
            }
        }
        
        // generate the aggregate query result
        CQLQueryResults aggregate = new CQLQueryResults();
        CQLObjectResult[] resultArray = new CQLObjectResult[objectResults.size()];
        objectResults.toArray(resultArray);
        aggregate.setObjectResult(resultArray);
        aggregate.setTargetClassname(query.getTarget().getName());
        
        return aggregate;
    }
    
    
    private static WorkManager getWorkManager() {
        if (workManager == null) {
            int poolSize = DEFAULT_POOL_SIZE;
            // TODO: make this configurable, i.e. with a service property
            workManager = new WorkManagerImpl(poolSize);
        }
        return workManager;
    }
    
    
    public void setWorkManager(WorkManager manager) {
        workManager = manager;
    }
    
    
    private static class QueryExecutionContext {
        private CQLQuery query = null;
        private CQLQueryResults results = null;
        private GlobusCredential credential = null;
        private String serviceURL = null;
        private Exception processingException = null;
        
        public QueryExecutionContext(CQLQuery query, String serviceURL, GlobusCredential credential) {
            this.query = query;
            this.serviceURL = serviceURL;
            this.credential = credential;
        }


        public GlobusCredential getCredential() {
            return credential;
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
    
    
    private static class QueryExecutor implements Work {
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
            logger.debug("CQL Aggregator querying data service " + queryContext.getServiceURL());
            CQLQueryResults results = null;
            try {
                results = DataServiceQueryExecutor.queryDataService(
                    queryContext.getQuery(), queryContext.getServiceURL(), queryContext.getCredential());
            } catch (Exception ex) {
                logger.error("Query processing failed with exception: " + ex.getMessage());
                queryContext.setProcessingException(ex);
                ex.printStackTrace();
            }
            logger.debug("CQL Aggregator done with " + queryContext.getServiceURL());
            queryContext.setResults(results);
        }
    }
}
