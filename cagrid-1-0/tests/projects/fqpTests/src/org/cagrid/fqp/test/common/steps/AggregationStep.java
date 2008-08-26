package org.cagrid.fqp.test.common.steps;

import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;
import gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault;

import java.rmi.RemoteException;

import org.cagrid.fqp.test.common.FederatedQueryProcessorHelper;
import org.cagrid.fqp.test.common.QueryResultsVerifier;

/** 
 *  AggregationStep
 *  Performs a simple FQP aggregation and compare the result to Gold
 * 
 * @author David Ervin
 * 
 * @created Jul 10, 2008 12:17:40 PM
 * @version $Id: AggregationStep.java,v 1.6 2008-08-26 20:05:55 dervin Exp $ 
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
        QueryResultsVerifier.verifyCqlResults(testResults, goldResults);
    }
    
    
    private CQLQueryResults performAggregation(DCQLQuery query) throws RemoteException, 
        FederatedQueryProcessingException, FederatedQueryProcessingFault {
        return queryProcessor.executeAndAggregateResults(query);
    }
}
