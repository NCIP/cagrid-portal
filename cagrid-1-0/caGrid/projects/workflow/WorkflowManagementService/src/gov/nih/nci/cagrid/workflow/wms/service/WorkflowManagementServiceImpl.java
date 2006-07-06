package gov.nih.nci.cagrid.workflow.wms.service;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.workflow.wms.stubs.service.WMSInputType;
import gov.nih.nci.cagrid.workflow.wms.stubs.service.WMSOutputType;
import gov.nih.nci.cagrid.workflow.wms.stubs.service.WSDLReferences;

import java.io.File;
import java.rmi.RemoteException;

import javax.naming.InitialContext;

import org.apache.axis.MessageContext;
import org.globus.wsrf.Constants;

/**
 * gov.nih.nci.cagrid.workflow.wmsI TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class WorkflowManagementServiceImpl {
	private ServiceConfiguration configuration;

	protected static String abAdminUrl = "http://localhost:8080/active-bpel/services/BpelEngineAdmin";

	protected static String BPEL_NS = "http://schemas.xmlsoap.org/ws/2003/03/business-process/";

	public WorkflowManagementServiceImpl() throws RemoteException {

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

	private String deploy(String bpelFileName, String workflowName, WSDLReferences[] wsdlRefArray) throws Exception {
		return ActiveBPELAdapter.deployBpr(bpelFileName ,workflowName, wsdlRefArray);
	}
	
	private WMSOutputType invokeProcess(String partnerLinkName, WMSInputType wmsInput) throws Exception {
		return ActiveBPELAdapter.invokeProcess(partnerLinkName, wmsInput);
	}
	
	/** This is the method to deploy and run workflow
	 * @param wmsInput
	 * @return
	 * @throws RemoteException
	 */
	public WMSOutputType runWorkflow(WMSInputType wmsInput)
			throws RemoteException {
		String workflowName = wmsInput.getWorkflowName();
		String bpelProcess = wmsInput.getBpelDoc();
		WMSOutputType output;
		File bpelFile = null;
		try {
			String bpelFileName = System.getProperty("java.io.tmpdir") + File.separator+ workflowName + ".bpel";
			bpelFile = new File(bpelFileName);
			bpelFile.deleteOnExit();
			Utils.stringBufferToFile(new StringBuffer(bpelProcess),
					bpelFileName);
			WSDLReferences[] wsdlRefArray = wmsInput.getWsdlReferences();
			deploy(bpelFileName, workflowName, wsdlRefArray);
		} catch (Exception e){
			throw new RemoteException ("Exception deploying workflow:"+ workflowName ,e );
		}
		try {
			String serviceName = workflowName + "Service";
			output = invokeProcess(serviceName, 
					wmsInput);
					
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("Exception running the workflow:" + workflowName, e);
		}
		return output;
	}

}
