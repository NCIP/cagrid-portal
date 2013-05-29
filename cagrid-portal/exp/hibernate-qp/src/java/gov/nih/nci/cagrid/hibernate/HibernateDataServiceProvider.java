/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.hibernate;

import gov.nih.nci.cagrid.data.service.DataServiceInitializationException;

import java.rmi.RemoteException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class HibernateDataServiceProvider {

	private HibernateDataServiceImpl impl;
	
	/**
	 * @throws DataServiceInitializationException 
	 * 
	 */
	public HibernateDataServiceProvider() throws DataServiceInitializationException {
		impl = new HibernateDataServiceImpl();
	}

	public gov.nih.nci.cagrid.data.stubs.QueryResponse query(
			gov.nih.nci.cagrid.data.stubs.QueryRequest params)
			throws RemoteException,
			gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType,
			gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType {
		gov.nih.nci.cagrid.data.stubs.QueryResponse boxedResult = new gov.nih.nci.cagrid.data.stubs.QueryResponse();
		boxedResult.setCQLQueryResultCollection(impl.query(params.getCqlQuery()
				.getCQLQuery()));
		return boxedResult;
	}

}
