package org.cagrid.fqp.test.common.steps;

import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;
import gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import org.cagrid.fqp.test.common.FederatedQueryProcessorHelper;

/** 
 *  AggregationStep
 *  Performs a simple FQP aggregation and compare the result to Gold
 * 
 * @author David Ervin
 * 
 * @created Jul 10, 2008 12:17:40 PM
 * @version $Id: AggregationStep.java,v 1.5 2008-08-25 16:49:07 dervin Exp $ 
 */
public class AggregationStep extends BaseQueryExecutionStep {
    
    private FederatedQueryProcessorHelper queryProcessor;
    private String[] testServiceUrls;
    
    public AggregationStep(String queryFilename, String goldFilename, 
        FederatedQueryProcessorHelper queryProcessorHelper, String[] testServiceUrls) {
        super(queryFilename, goldFilename);
        this.queryProcessor = queryProcessorHelper;
        this.testServiceUrls = testServiceUrls;
    }
    

    public void runStep() throws Throwable {
        DCQLQuery query = deserializeQuery();
        query.setTargetServiceURL(testServiceUrls);
        CQLQueryResults testResults = performAggregation(query);
        CQLQueryResults goldResults = loadGoldCqlResults();
        verifyObjectResults(testResults, goldResults);
    }
    
    
    private CQLQueryResults performAggregation(DCQLQuery query) throws RemoteException, 
        FederatedQueryProcessingException, FederatedQueryProcessingFault {
        return queryProcessor.executeAndAggregateResults(query);
    }
    
    
    private void verifyObjectResults(CQLQueryResults testResults, CQLQueryResults goldResults) {
        // iterators to extract objects from the results
        CQLQueryResultsIterator testIter = new CQLQueryResultsIterator(
            testResults, getClass().getResourceAsStream(CLIENT_WSDD));
        CQLQueryResultsIterator goldIter = new CQLQueryResultsIterator(
            goldResults, getClass().getResourceAsStream(CLIENT_WSDD));
        
        // turn results into lists
        List testItems = new LinkedList();
        List goldItems = new LinkedList();
        while (testIter.hasNext()) {
            testItems.add(testIter.next());
        }
        while (goldIter.hasNext()) {
            goldItems.add(goldIter.next());
        }
        
        // compare
        assertEquals("Incorrect number of results", goldItems.size(), testItems.size());
        
        for (Object testObject : testItems) {
            assertTrue("Unexpected Test data object not found in gold data", goldItems.contains(testObject));
        }
        
        for (Object goldObject : testItems) {
            assertTrue("Expected Gold data object not found in test data", testItems.contains(goldObject));
        }
    }
}
