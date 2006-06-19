package gov.nih.nci.cagrid.data.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.stubs.QueryProcessingException;

import java.rmi.RemoteException;
import java.util.Map;

/** 
 *  gov.nih.nci.cagrid.dataI
 *  TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class DataServiceImpl {
	
	public DataServiceImpl() throws RemoteException {
		
	}
	
	public Map getConfiguration() throws Exception {
		return ServiceConfigUtil.getConfigurationMap();
	}
	
	
	public gov.nih.nci.cagrid.cqlresultset.CQLQueryResults query(gov.nih.nci.cagrid.cqlquery.CQLQuery cqlQuery) 
		throws RemoteException, gov.nih.nci.cagrid.data.stubs.QueryProcessingException, gov.nih.nci.cagrid.data.stubs.MalformedQueryException {
		gov.nih.nci.cagrid.data.cql.CQLQueryProcessor processor = null;
		try {
			Class qpClass = Class.forName((String) getConfiguration().get(
				DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY));
			processor = (gov.nih.nci.cagrid.data.cql.CQLQueryProcessor) qpClass.newInstance();
			processor.initialize(getConfiguration());
		} catch (Exception ex) {
			QueryProcessingException qpe = new QueryProcessingException();
			FaultHelper helper = new FaultHelper(qpe);
			helper.addFaultCause(ex);
			helper.setDescription(ex.getMessage());
			throw (QueryProcessingException) helper.getFault();
		}
		try {
			return processor.processQuery(cqlQuery);
		} catch (Exception ex) {
			QueryProcessingException qpe = new QueryProcessingException();
			FaultHelper helper = new FaultHelper(qpe);
			helper.addFaultCause(ex);
			helper.setDescription(ex.getMessage());
			throw (QueryProcessingException) helper.getFault();
		}
	}	
}

