package gov.nih.nci.cagrid.introduce.security.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dorian.service.Dorian;
import gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadata;
import gov.nih.nci.cagrid.syncgts.bean.SyncReport;


import java.io.File;
import java.rmi.RemoteException;

import javax.naming.InitialContext;
import javax.xml.namespace.QName;

import org.apache.axis.MessageContext;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.Constants;
import org.globus.wsrf.config.ContainerConfig;
import org.globus.wsrf.utils.AddressingUtils;


/**
 * gov.nih.nci.cagrid.introduce.securityI TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 */
public class ServiceSecurityImpl {
	private ServiceConfiguration configuration;
	private ServiceSecurityMetadata metadata;


	public ServiceSecurityImpl() throws RemoteException {
		try {
			EndpointReferenceType type = AddressingUtils.createEndpointReference(null);
			String configFileEnd = (String)MessageContext.getCurrentContext().getProperty("securityMetadata");
			String configFile = ContainerConfig.getBaseDirectory() + File.separator+configFileEnd;
			File f = new File(configFile);
			if(!f.exists()){
				throw new RemoteException("The security metadata file ("+configFile+") could not be found!!!");
			}
			metadata = (ServiceSecurityMetadata) Utils.deserializeDocument(configFile, ServiceSecurityMetadata.class);
			
		} catch (Exception e) {
			FaultHelper.printStackTrace(e);
			throw new RemoteException(Utils.getExceptionMessage(e));
		}
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


	public gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadata getServiceSecurityMetadata()
		throws RemoteException {
		return metadata;
	}

}
