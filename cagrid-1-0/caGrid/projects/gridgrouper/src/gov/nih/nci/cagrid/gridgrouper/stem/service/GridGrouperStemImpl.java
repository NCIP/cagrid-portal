package gov.nih.nci.cagrid.gridgrouper.stem.service;

import gov.nih.nci.cagrid.gridgrouper.stem.service.globus.resource.StemResource;

import java.rmi.RemoteException;

import javax.naming.InitialContext;

import org.apache.axis.MessageContext;
import org.globus.wsrf.Constants;
import org.globus.wsrf.ResourceContext;

/**
 * gov.nih.nci.cagrid.gridgrouper.stemI TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class GridGrouperStemImpl {
	private ServiceConfiguration configuration;

	public GridGrouperStemImpl() throws RemoteException {

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

	public java.lang.String getStemName() throws RemoteException {
		StemResource stemResource = (StemResource) ResourceContext.getResourceContext().getResource();
		return stemResource.getStemName();
	}

}
