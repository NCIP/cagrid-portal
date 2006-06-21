package gov.nih.nci.cagrid.workflow.wms.service;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.workflow.stubs.Invoke;
import gov.nih.nci.cagrid.workflow.wms.stubs.service.WMSInputType;
import gov.nih.nci.cagrid.workflow.wms.stubs.service.WMSOutputType;
import gov.nih.nci.cagrid.workflow.wms.stubs.service.WorkflowOuputType;

import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;

import javax.naming.InitialContext;
import javax.xml.namespace.QName;

import org.apache.axis.MessageContext;
import org.globus.wsrf.Constants;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.globus.wsrf.utils.AnyHelper;
import org.globus.wsrf.utils.XmlUtils;
import org.w3c.dom.Document;

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

	private String deploy(String bpelFileName, String pddFileName, String workflowName) throws Exception {
		return ActiveBPELAdapter.deployBpr(bpelFileName, pddFileName, workflowName);
	}
	
	private String invokeProcess(String partnerLinkName, String message) throws Exception {
		return ActiveBPELAdapter.invokeProcess(partnerLinkName, message);
	}
	
	public WMSOutputType runWorkflow(WMSInputType wmsInput)
			throws RemoteException {
		String workflowName = wmsInput.getWorkflowName();
		String bpelProcess = wmsInput.getBpelDoc();
		File bpelFile = null;
		try {
			String bpelFileName = System.getProperty("java.io.tmpdir") + workflowName + ".bpel";
			bpelFile = new File(bpelFileName);
			bpelFile.deleteOnExit();
			Utils.stringBufferToFile(new StringBuffer(bpelProcess),
					bpelFileName);
			String pathtoPDD = "c:\\Simple.pdd";
			System.out.println(bpelFileName + " " + pathtoPDD + " " + workflowName);
			deploy(bpelFileName, pathtoPDD, workflowName);
		} catch (Exception e){
			throw new RemoteException ("Exception deploying workflow:"+ workflowName ,e );
		}
		try {
			Document doc = XmlUtils.newDocument(new FileInputStream(bpelFile));
			Invoke invoke = (Invoke) ObjectDeserializer.toObject(wmsInput
					.getInputArgs().get_any()[0], Invoke.class);
			QName qname = new QName("http://workflow.cagrid.nci.nih.gov/SampleService1", "invoke");
			
			String output = invokeProcess("Sample1PartnerLinkTypeService", 
					ObjectSerializer.toString(invoke, qname));
			System.out.println("Result: " + output);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("Exception running the workflow:" + workflowName, e);
		}
		WMSOutputType output = new WMSOutputType();
		WorkflowOuputType wOutput = new WorkflowOuputType();
		wOutput.set_any(AnyHelper.toAnyArray(output));
		return output;
	}

}
