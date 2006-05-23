package gov.nih.nci.cagrid.workflow.management.service;

import gov.nih.nci.cagrid.workflow.management.common.WorkFlowManagementServiceI;
import gov.nih.nci.cagrid.workflow.management.service.globus.ServiceConfiguration;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.jar.JarFile;

import javax.naming.InitialContext;
import javax.xml.rpc.ServiceException;

import org.activebpel.rt.base64.Base64;
import org.activebpel.rt.bpel.server.admin.rdebug.server.AeRemoteDebugImpl;
import org.apache.axis.MessageContext;
import org.globus.wsrf.Constants;
import org.w3c.dom.Document;
import org.xmlsoap.schemas.ws._2003._03.business_process.TProcess;

import AeAdminServices.BpelEngineAdminLocator;
import AeAdminServices.RemoteDebugSoapBindingStub;

/**
 * gov.nih.nci.cagrid.workflow.managementI TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class WorkFlowManagementServiceImpl implements
		WorkFlowManagementServiceI {
	private ServiceConfiguration configuration;

	public WorkFlowManagementServiceImpl() throws RemoteException {

	}

	public ServiceConfiguration getConfiguration() throws Exception {
		if (this.configuration != null) {
			return this.configuration;
		}
		MessageContext ctx = MessageContext.getCurrentContext();

		String servicePath = ctx.getTargetService();

		String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath
				+ "/serviceconfiguration";
		try {
			javax.naming.Context initialContext = new InitialContext();
			this.configuration = (ServiceConfiguration) initialContext
					.lookup(jndiName);
		} catch (Exception e) {
			throw new Exception("Unable to instantiate service configuration.",
					e);
		}

		return this.configuration;
	}

	public java.lang.String runWorkFlow(
			org.xmlsoap.schemas.ws._2003._03.business_process.TProcess bpelDoc)
			throws RemoteException {
		RemoteDebugSoapBindingStub mRemote = null;
        String output = null;
		try {
			
			BpelEngineAdminLocator locator = new BpelEngineAdminLocator();
			URL url = new URL(
					"http://localhost:8080/active-bpel/services/BpelEngineAdmin");
			System.out.println("Deploying1");
			mRemote = (RemoteDebugSoapBindingStub) locator
					.getAeActiveWebflowAdminPort(url);
			String filePath = "/autonfs/home/madduri/globus/temp.bpr";

			BPRCreator.makeBPR(bpelDoc, filePath);

			output = mRemote.deployBpr("temp.bpr", BPRCreator
					.getBase64EncodedBpr(filePath));
			String defaultEventLocatorClass = "org.activebpel.rt.axis.bpel.rdebug.client.AeEventHandlerLocator"; //$NON-NLS-1$
	        String defaultBpLocatorClass = "org.activebpel.rt.axis.bpel.rdebug.client.AeBreakpointHandlerLocator"; //$NON-NLS-1$
			AeRemoteDebugImpl remoteImpl = new AeRemoteDebugImpl(defaultEventLocatorClass,
					defaultBpLocatorClass);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return output;

	}

}
