package gov.nih.nci.cagrid.workflow.context.service.globus.resource;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.workflow.context.stubs.types.StartCalledOnStartedWorkflowFaultType;
import gov.nih.nci.cagrid.workflow.service.ActiveBPELAdapter;
import gov.nih.nci.cagrid.workflow.service.ServiceConfiguration;
import gov.nih.nci.cagrid.workflow.service.WorkflowFactoryServiceImpl;
import gov.nih.nci.cagrid.workflow.stubs.types.StartInputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WMSInputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WSDLReferences;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowExceptionType;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowOutputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowStatusType;

import java.io.File;
import java.net.URL;
import java.util.Calendar;

import javax.naming.InitialContext;

import org.apache.axis.MessageContext;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.MessageElement;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.message.addressing.AddressingHeaders;
import org.apache.axis.message.addressing.To;
import org.globus.wsrf.Constants;
import org.globus.wsrf.Resource;

public class WorkflowResource implements Resource {

	public String workflowName = null;

	public String bpelFileName = null;

	public String bpelProcess = null;

	public static String BPEL_EXTENSION = ".bpel";

	private boolean started = false;
	
	private String partnerLinkName = null;
	
	private WorkflowStatusType workflowStatus = WorkflowStatusType.Pending;
	
	//Discover this from jndi config
	private  ActiveBPELAdapter abAdapter = null;
	
	private ServiceConfiguration configuration = null;
	
	private WorkflowOutputType outputType = null;
	
	
	public WorkflowResource(WMSInputType input, Calendar terminationTime)
			throws Exception {
		File bpelFile = null;
		this.workflowName = input.getWorkflowName();
		this.bpelProcess = input.getBpelDoc();
		System.out.println("abAdmin: " + this.getConfiguration().getAbEndpoint());
		String bpelFileName = System.getProperty("java.io.tmpdir")
				+ File.separator + workflowName + BPEL_EXTENSION;
		bpelFile = new File(bpelFileName);
		bpelFile.deleteOnExit();
		Utils.stringBufferToFile(new StringBuffer(bpelProcess), bpelFileName);
		WSDLReferences[] wsdlRefArray = input.getWsdlReferences();
		this.outputType = new WorkflowOutputType();
		this.abAdapter = new ActiveBPELAdapter(null, this.outputType, this.workflowStatus);
		String returnString = deploy(bpelFileName, workflowName, wsdlRefArray);
		
		System.out.println("Created a Workflow resource with key:" 
				+ input.getWorkflowName());
	}

	private String deploy(String bpelFileName, String workflowName,
			WSDLReferences[] wsdlRefArray) throws Exception {
		return this.abAdapter.deployBpr(bpelFileName,
				workflowName, wsdlRefArray);
	}

	public WorkflowStatusType start(StartInputType startInput) 
		throws WorkflowExceptionType, StartCalledOnStartedWorkflowFaultType {
		this.partnerLinkName = "SimpleService"; 
		if (this.started) {
			throw new StartCalledOnStartedWorkflowFaultType();
		}
		try {
			this.abAdapter.startWorkflow(this.partnerLinkName, startInput);
			this.workflowStatus = WorkflowStatusType.Active;
		} catch (Exception e) {
			this.workflowStatus = WorkflowStatusType.Failed;
			throw new WorkflowExceptionType();
		}
		return this.workflowStatus;
	}

	public WorkflowStatusType getStatus() throws WorkflowExceptionType {
		this.workflowStatus = this.abAdapter.getWorkflowStatus(this.workflowName);
		return this.workflowStatus;
	}
	
	public WorkflowStatusType pause() throws WorkflowExceptionType {
		this.abAdapter.suspend(this.workflowName);
		this.workflowStatus = WorkflowStatusType.Pending;
		return this.workflowStatus;
	}
	
	public WorkflowStatusType resume() throws WorkflowExceptionType {
		this.abAdapter.resume(this.workflowName);
		this.workflowStatus = WorkflowStatusType.Active;
		return this.workflowStatus;
	}
	
	public void cancel() throws WorkflowExceptionType {
		this.abAdapter.cancel(this.workflowName);
	}
	
	public WorkflowOutputType getWorkflowOutput() {
		return this.abAdapter.getWorkflowOutput();
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
