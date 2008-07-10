package org.cagrid.fqp.test.common;

import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.fqp.client.FederatedQueryProcessorClient;
import gov.nih.nci.cagrid.fqp.processor.FederatedQueryEngine;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;
import gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault;

import java.rmi.RemoteException;

/** 
 *  FederatedQueryProcessorHelper
 *  Wraps an FQP implementation (Engine or Client) with a consistent interface
 * 
 * @author David Ervin
 * 
 * @created Jul 10, 2008 1:33:38 PM
 * @version $Id: FederatedQueryProcessorHelper.java,v 1.1 2008-07-10 18:35:58 dervin Exp $ 
 */
public class FederatedQueryProcessorHelper {

    private FederatedQueryProcessorClient fqpClient;
    private FederatedQueryEngine fqpEngine;
    
    public FederatedQueryProcessorHelper(FederatedQueryProcessorClient client) {
        this.fqpClient = client;
    }
    
    
    public FederatedQueryProcessorHelper(FederatedQueryEngine engine) {
        this.fqpEngine = engine;
    }
    
    
    public DCQLQueryResultsCollection execute(DCQLQuery query) throws RemoteException, 
        FederatedQueryProcessingFault, FederatedQueryProcessingException {
        DCQLQueryResultsCollection results = null;
        if (fqpClient != null) {
            results = fqpClient.execute(query);
        } else if (fqpEngine != null) {
            results = fqpEngine.execute(query);
        } else {
            throw new IllegalStateException("NO CLIENT OR ENGINE!");
        }
        
        return results;
    }
    
    
    public CQLQueryResults executeAndAggregateResults(DCQLQuery query) throws RemoteException,
        FederatedQueryProcessingFault, FederatedQueryProcessingException {
        CQLQueryResults results = null;
        if (fqpClient != null) {
            results = fqpClient.executeAndAggregateResults(query);
        } else if (fqpEngine != null) {
            results = fqpEngine.executeAndAggregateResults(query);
        } else {
            throw new IllegalStateException("NO CLIENT OR ENGINE!");
        }
        
        return results;
    }
}
