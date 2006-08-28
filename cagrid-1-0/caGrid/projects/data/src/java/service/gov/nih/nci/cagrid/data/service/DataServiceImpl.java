package gov.nih.nci.cagrid.data.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.stubs.MalformedQueryException;
import gov.nih.nci.cagrid.data.stubs.QueryProcessingException;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Properties;

import org.apache.axis.utils.ClassUtils;

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
	
	
	public gov.nih.nci.cagrid.cqlresultset.CQLQueryResults query(gov.nih.nci.cagrid.cqlquery.CQLQuery cqlQuery) 
		throws RemoteException, gov.nih.nci.cagrid.data.stubs.QueryProcessingException, gov.nih.nci.cagrid.data.stubs.MalformedQueryException {
		gov.nih.nci.cagrid.data.cql.CQLQueryProcessor processor = null;
		try {
			Properties configParameters = ServiceConfigUtil.getConfigurationParameters();
			Class qpClass = Class.forName(configParameters.getProperty(
				DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY));
			processor = (gov.nih.nci.cagrid.data.cql.CQLQueryProcessor) qpClass.newInstance();
			InputStream configStream = ClassUtils.getResourceAsStream(
				getClass(), "server-config.wsdd");
			processor.initialize(configParameters, configStream);
		} catch (Exception ex) {
			QueryProcessingException qpe = new QueryProcessingException();
			FaultHelper helper = new FaultHelper(qpe);
			helper.addFaultCause(ex);
			helper.setDescription(ex.getMessage());
			throw (QueryProcessingException) helper.getFault();
		}
		try {
			return processor.processQuery(cqlQuery);
		} catch (gov.nih.nci.cagrid.data.QueryProcessingException ex) {
			QueryProcessingException qpe = new QueryProcessingException();
			FaultHelper helper = new FaultHelper(qpe);
			helper.addFaultCause(ex);
			helper.setDescription(ex.getMessage());
			throw (QueryProcessingException) helper.getFault();
		} catch (gov.nih.nci.cagrid.data.MalformedQueryException ex) {
			MalformedQueryException mqe = new MalformedQueryException();
			FaultHelper helper = new FaultHelper(mqe);
			helper.addFaultCause(ex);
			helper.setDescription(ex.getMessage());
			throw (MalformedQueryException) helper.getFault();
		}
	}	
}

