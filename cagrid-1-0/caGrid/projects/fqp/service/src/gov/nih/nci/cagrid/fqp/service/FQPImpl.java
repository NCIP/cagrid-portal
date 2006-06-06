package gov.nih.nci.cagrid.fqp.service;

import java.rmi.RemoteException;

import javax.naming.InitialContext;

import org.apache.axis.MessageContext;
import org.globus.wsrf.Constants;

/** 
 *  gov.nih.nci.cagrid.fqpI
 *  TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class FQPImpl {
    private ServiceConfiguration configuration;
	
	public FQPImpl() throws RemoteException {
	
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


}

