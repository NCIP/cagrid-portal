package gov.nih.nci.cagrid.rproteomics.service.globus;

import gov.nih.nci.cagrid.rproteomics.common.RPDataI;
import gov.nih.nci.cagrid.rproteomics.service.RPDataImpl;

import java.rmi.RemoteException;

/** 
 *  DO NOT EDIT:  This class is autogenerated!
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class RPDataProviderImpl{
	
	RPDataI impl;
	
	public RPDataProviderImpl() {
		impl = new RPDataImpl();
	}
	








	public gov.nih.nci.cagrid.rproteomics.stubs.QueryResponse query(gov.nih.nci.cagrid.rproteomics.stubs.Query params) throws RemoteException, gov.nih.nci.cagrid.rproteomics.stubs.MalformedQueryException {
		gov.nih.nci.cagrid.rproteomics.stubs.QueryResponse boxedResult = new gov.nih.nci.cagrid.rproteomics.stubs.QueryResponse();
		boxedResult.setResponse(impl.query(params.getQuery()));
		return boxedResult;
	}

}