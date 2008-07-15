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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.globus.gsi.GlobusCredential;

/** 
 *  CQLAggregator
 *  Runs CQL against multiple data services in parallel and aggregates the results
 * 
 * @author David Ervin
 * 
 * @created Jun 26, 2008 2:10:45 PM
 * @version $Id: CQLAggregator.java,v 1.1 2008-07-15 15:34:21 dervin Exp $ 
 */
public class CQLAggregator {
    
    public static final int DEFAULT_MAX_WORKER_THREADS = 5;

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
        return aggregateQueryResults(query, dataServiceUrls, credential, DEFAULT_MAX_WORKER_THREADS);
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
     * @param numThreads
     *      The number of threads to use in conncurent query processing
     * @return
     *      The aggregated object results of the CQL query
     * @throws RemoteDataServiceException
     * @throws FederatedQueryProcessingException
     */
    public static CQLQueryResults aggregateQueryResults(
        CQLQuery query, String[] dataServiceUrls, GlobusCredential credential, int numThreads) 
        throws RemoteDataServiceException, FederatedQueryProcessingException {
        List<CQLObjectResult> objectResults = new LinkedList<CQLObjectResult>();
        
        // create thread pool executor with max of MAX_WORKER_THREADS
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        Map<String, FutureTask<CQLQueryResults>> workers = 
            new HashMap<String, FutureTask<CQLQueryResults>>();
        
        // set up worker tasks for each URL
        for (String url : dataServiceUrls) {
            QueryExecutor exec = new QueryExecutor(query, url, credential);
            FutureTask worker = new FutureTask<CQLQueryResults>(exec);
            executor.execute(worker);
            workers.put(url, worker);
        }
        
        try {
            // get results from each task
            for (String service : workers.keySet()) {
                FutureTask<CQLQueryResults> task = workers.get(service);
                CQLQueryResults results = task.get();
                // ensure the query results are of the correct data type
                if (!results.getTargetClassname().equals(query.getTarget().getName())) {
                    throw new RemoteDataServiceException("Data service (" + service
                        + ") returned results of type (" + results.getTargetClassname() 
                        + ") when type (" + query.getTarget().getName() + ") was requested!");
                }
                if (results.getObjectResult() != null) {
                    // add the results together
                    Collections.addAll(objectResults, results.getObjectResult());
                }
            }
        } catch (InterruptedException ex) {
            throw new FederatedQueryProcessingException("Error executing aggregation in parallel: " + ex.getMessage(), ex);
        } catch (ExecutionException ex) {
            throw new FederatedQueryProcessingException("Error executing aggregation in parallel: " + ex.getMessage(), ex);
        } finally {
            System.out.println("SHUTTING DOWN EXECUTOR");
            System.out.flush();
            executor.shutdownNow();
            System.out.println("SHUT DOWN EXECUTOR");
            System.out.flush();
        }
        
        // generate the aggregate query result
        CQLQueryResults aggregate = new CQLQueryResults();
        CQLObjectResult[] resultArray = new CQLObjectResult[objectResults.size()];
        objectResults.toArray(resultArray);
        aggregate.setObjectResult(resultArray);
        aggregate.setTargetClassname(query.getTarget().getName());
        
        return aggregate;
    }
    
    
    private static class QueryExecutor implements Callable<CQLQueryResults> {
        private CQLQuery query = null;
        private String serviceURL = null;
        private GlobusCredential credential = null;
        
        public QueryExecutor(CQLQuery query, String serviceURL, GlobusCredential credential) {
            this.query = query;
            this.serviceURL = serviceURL;
            this.credential = credential;
        }
        
        
        public CQLQueryResults call() throws RemoteDataServiceException {
            System.out.println("Querying " + serviceURL);
            System.out.flush();
            CQLQueryResults results = DataServiceQueryExecutor.queryDataService(query, serviceURL, credential);
            System.out.println("Done with " + serviceURL);
            System.out.flush();
            return results;
        }
    }
}
