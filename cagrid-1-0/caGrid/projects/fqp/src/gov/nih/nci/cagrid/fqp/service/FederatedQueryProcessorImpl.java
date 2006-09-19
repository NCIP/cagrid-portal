package gov.nih.nci.cagrid.fqp.service;

import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.fqp.processor.FederatedQueryEngine;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;

import java.rmi.RemoteException;


/**
 * Federated Query Service
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class FederatedQueryProcessorImpl extends FederatedQueryProcessorImplBase {

	public FederatedQueryProcessorImpl() throws RemoteException {
		super();
	}


	public gov.nih.nci.cagrid.cqlresultset.CQLQueryResults executeAndAggregateResults(
		gov.nih.nci.cagrid.dcql.DCQLQuery query) throws RemoteException {
		FederatedQueryEngine engine = new FederatedQueryEngine();
		CQLQueryResults results = null;
		try {
			results = engine.executeAndAggregateResults(query);
		} catch (FederatedQueryProcessingException e) {
			// TODO: change to proper faults
			throw new RemoteException(e.getMessage(), e);
		}
		return results;
	}


	public gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection execute(gov.nih.nci.cagrid.dcql.DCQLQuery query)
		throws RemoteException {
		FederatedQueryEngine engine = new FederatedQueryEngine();
		DCQLQueryResultsCollection results = null;
		try {
			results = engine.execute(query);
		} catch (FederatedQueryProcessingException e) {
			// TODO: change to proper faults
			throw new RemoteException(e.getMessage(), e);
		}
		return results;
	}

}
