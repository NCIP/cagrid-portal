package gov.nih.nci.cagrid.data.enumeration.service.globus;

import gov.nih.nci.cagrid.data.enumeration.service.EnumerationDataServiceImpl;

import java.rmi.RemoteException;

/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This class implements each method in the portType of the service.  Each method call represented
 * in the port type will be then mapped into the unwrapped implementation which the user provides
 * in the EnumerationDataServiceImpl class.  This class handles the boxing and unboxing of each method call
 * so that it can be correclty mapped in the unboxed interface that the developer has designed and 
 * has implemented.  Authorization callbacks are automatically made for each method based
 * on each methods authorization requirements.
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class EnumerationDataServiceProviderImpl{
	
	EnumerationDataServiceImpl impl;
	
	public EnumerationDataServiceProviderImpl() throws RemoteException {
		impl = new EnumerationDataServiceImpl();
	}
	

	public gov.nih.nci.cagrid.data.enumeration.stubs.EnumerationQueryResponse enumerationQuery(gov.nih.nci.cagrid.data.enumeration.stubs.EnumerationQueryRequest params) throws RemoteException, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType {
		EnumerationDataServiceAuthorization.authorizeEnumerationQuery();
		gov.nih.nci.cagrid.data.enumeration.stubs.EnumerationQueryResponse boxedResult = new gov.nih.nci.cagrid.data.enumeration.stubs.EnumerationQueryResponse();
		boxedResult.setEnumerateResponse(impl.enumerationQuery(params.getCqlQuery().getCQLQuery()));
		return boxedResult;
	}

}
