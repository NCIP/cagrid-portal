package gov.nih.nci.cagrid.workflow.management.service;

import gov.nih.nci.cagrid.workflow.management.common.WorkFlowManagementServiceI;
import gov.nih.nci.cagrid.workflow.management.service.globus.ServiceConfiguration;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.jar.JarFile;

import org.activebpel.rt.AeException;
import org.activebpel.rt.bpel.def.validation.IAeBaseErrorReporter;
import org.activebpel.rt.bpel.server.deploy.AeAbstractDeploymentContext;
import org.activebpel.rt.bpel.server.deploy.AeBpelDeployer;
import org.activebpel.rt.bpel.server.deploy.IAeDeploymentSource;
import org.activebpel.rt.bpel.server.deploy.bpr.AeBprContext;
import org.activebpel.rt.bpel.server.deploy.bpr.AeBprDeploymentSource;

import javax.naming.InitialContext;
import javax.xml.rpc.ServiceException;

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
		System.out.println("Sending ");
		RemoteDebugSoapBindingStub mRemote = null;
		AeBpelDeployer abDeployer = new AeBpelDeployer();
		IAeDeploymentSource aSource = new AeBprDeploymentSource(
				new AeBprContext(null, null, null));
		IAeBaseErrorReporter aReporter = null;
		try {
			abDeployer.deployBpel(aSource, aReporter);
		} catch (AeException aee) {

		}
		BpelEngineAdminLocator locator = new BpelEngineAdminLocator();
	      URL url = null;
		try {
			url = new URL( "http://spirulina.uchicago.edu:8080/active-bpel/services/BpelEngineAdmin" );
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      try {
			mRemote = (RemoteDebugSoapBindingStub)locator.getAeActiveWebflowAdminPort( url );
			//mRemote.deployBpr(aBprFilename, aBase64File)
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (bpelDoc.toString());
		
	}
	
	public static JarFile makeBPR(TProcess bpelDoc, Document pddDocument ) throws Exception {
		JarFile activeBPR = new JarFile("deploy.bpr");
		
		return activeBPR;
	}
}
