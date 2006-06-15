package gov.nih.nci.cagrid.data.service;

import java.rmi.RemoteException;

import javax.naming.InitialContext;

import org.apache.axis.MessageContext;
import org.globus.wsrf.Constants;

/** 
 *  gov.nih.nci.cagrid.dataI
 *  TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class DataServiceImpl {
	private ServiceConfiguration configuration;
	
	public DataServiceImpl() throws RemoteException {
		
	}
	
	public ServiceConfiguration getConfiguration() throws Exception {
		if (this.configuration != null) {
			return this.configuration;
		}
		MessageContext ctx = MessageContext.getCurrentContext();
		
		String servicePath = ctx.getTargetService();
		
		String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/serviceconfiguration";
		try {
			javax.naming.Context initialContext = new InitialContext();
			this.configuration = (ServiceConfiguration) initialContext.lookup(jndiName);
		} catch (Exception e) {
			throw new Exception("Unable to instantiate service configuration.", e);
		}
		
		return this.configuration;
	}
	
	
	public gov.nih.nci.cagrid.cqlresultset.CQLQueryResults query(gov.nih.nci.cagrid.cqlquery.CQLQuery cqlQuery) throws RemoteException, gov.nih.nci.cagrid.data.stubs.QueryProcessingException, gov.nih.nci.cagrid.data.stubs.MalformedQueryException {
		gov.nih.nci.cagrid.data.cql.CQLQueryProcessor processor = null;
		try {
			Class qpClass = Class.forName(getConfiguration().getQueryProcessorClass());
			processor = (gov.nih.nci.cagrid.data.cql.CQLQueryProcessor) qpClass.newInstance();
			processor.initialize(configurationToMap());
		} catch (Exception ex) {
			return null;
		}
		try {
			return processor.processQuery(cqlQuery);
		} catch (Exception ex) {
			return null;
		}
	}
	
	
	private java.util.Map configurationToMap() throws Exception {
		java.util.Map map = new java.util.HashMap();
		java.lang.Class configClass = getConfiguration().getClass();
		for (int i = 0; i < configClass.getMethods().length; i++) {
			if (configClass.getMethods()[i].getName().startsWith("get")) {
				String value = (String) configClass.getMethods()[i].invoke(getConfiguration(), new Object[] {});
				map.put(configClass.getMethods()[i].getName().substring(3), value);
			}
		}
		return map;
	}
	
	
}

