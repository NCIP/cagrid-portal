package org.cagrid.fqp.test.remote.secure.steps;

import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.fqp.client.FederatedQueryProcessorClient;
import gov.nih.nci.cagrid.fqp.results.client.FederatedQueryResultsClient;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.fqp.test.common.QueryResultsVerifier;
import org.cagrid.fqp.test.common.UrlReplacer;
import org.cagrid.fqp.test.common.steps.BaseQueryExecutionStep;
import org.cagrid.gaards.cds.delegated.stubs.types.DelegatedCredentialReference;

public class DelegatedCredentialQueryStep extends BaseQueryExecutionStep {
    
    private static final Log LOG = LogFactory.getLog(DelegatedCredentialQueryStep.class);
    
    public static final int QUERY_EXECUTION_WAIT_TIME = 20; // seconds
    
    public static final String[] QUERY_URL_PLACEHOLDERS = {
        "DATA_SERVICE_1", "DATA_SERVICE_2"
    };
    
    private EndpointReferenceType fqpEPR;
    private DelegatedCredentialReference delegationRef;
    private String[] serviceUrls;
    
    public DelegatedCredentialQueryStep(String queryFilename, String goldFilename,
        EndpointReferenceType fqpEPR, DelegatedCredentialReference delegationRef,
        String[] serviceUrls) {
        super(queryFilename, goldFilename);
        this.fqpEPR = fqpEPR;
        this.delegationRef = delegationRef;
        this.serviceUrls = serviceUrls;
    }
    

    public void runStep() throws Throwable {
        LOG.debug("Testing with query " + getQueryFilename());
        DCQLQuery query = getCompletedQuery();
        
        LOG.debug("Starting query");
        FederatedQueryProcessorClient fqpClient = new FederatedQueryProcessorClient(fqpEPR);
        
        LOG.debug("Awaiting results");
        final FederatedQueryResultsClient resultsClient = fqpClient.query(query, delegationRef, null);
        FutureTask<Boolean> queryCompletionTask = new FutureTask<Boolean>(new Callable<Boolean>() {
            public Boolean call() throws RemoteException, InterruptedException {
                boolean complete = false;
                while (!complete) {
                    complete = resultsClient.isProcessingComplete();
                    Thread.sleep(500);
                }
                return Boolean.valueOf(complete);
            }
        });
        
        LOG.debug("Checking for completion");
        Boolean complete = Boolean.FALSE;
        try {
            complete = queryCompletionTask.get(QUERY_EXECUTION_WAIT_TIME, TimeUnit.SECONDS);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error while waiting for query to complete: " + ex.getMessage());
        } finally {
            if (!queryCompletionTask.isDone()) {
                queryCompletionTask.cancel(true);
            }
        }
        assertTrue("Query did not complete in the allotted time (" + QUERY_EXECUTION_WAIT_TIME + " sec)", 
            complete != null && complete.booleanValue());
        
        DCQLQueryResultsCollection result = resultsClient.getResults();
        assertNotNull("DCQL query results were null", result);
        
        LOG.debug("Verifying against " + getGoldFilenname());
        DCQLQueryResultsCollection gold = loadGoldDcqlResults();
        QueryResultsVerifier.verifyDcqlResults(result, gold);
    }
    
    
    private DCQLQuery getCompletedQuery() {
        assertEquals("Unexpected number of service urls", QUERY_URL_PLACEHOLDERS.length, serviceUrls.length);
        LOG.debug("Filling placeholder URLs with real ones");
        DCQLQuery original = deserializeQuery();
        Map<String, String> urlReplacements = new HashMap<String, String>();
        for (int i = 0; i < QUERY_URL_PLACEHOLDERS.length; i++) {
            urlReplacements.put(QUERY_URL_PLACEHOLDERS[i], serviceUrls[i]);
        }
        DCQLQuery replaced = null;
        try {
            replaced = UrlReplacer.replaceUrls(original, urlReplacements);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Unable to replace URL placeholders in DCQL query: " + ex.getMessage());
        }
        return replaced;
    }
}