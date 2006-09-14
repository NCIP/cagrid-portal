package gov.nih.nci.cagrid.fqp.service.globus;

import gov.nih.nci.cagrid.fqp.service.FederatedQueryProcessorImpl;

import java.rmi.RemoteException;

/** 
 *  DO NOT EDIT:  This class is autogenerated!
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class FederatedQueryProcessorProviderImpl{
	
	FederatedQueryProcessorImpl impl;
	
	public FederatedQueryProcessorProviderImpl() throws RemoteException {
		impl = new FederatedQueryProcessorImpl();
	}
	

	public gov.nih.nci.cagrid.fqp.stubs.QueryResponse query(gov.nih.nci.cagrid.fqp.stubs.QueryRequest params) throws RemoteException {
		gov.nih.nci.cagrid.fqp.stubs.QueryResponse boxedResult = new gov.nih.nci.cagrid.fqp.stubs.QueryResponse();
		boxedResult.setCQLQueryResultCollection(impl.query(params.getDCQLQuery().getDCQLQuery()));
		return boxedResult;
	}

}
