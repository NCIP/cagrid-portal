package gov.nih.nci.cagrid.workflow.service;

import gov.nih.nci.cagrid.workflow.stubs.types.StartInputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WSDLReferences;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowExceptionType;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowOutputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowStatusType;

import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.activebpel.rt.bpel.impl.list.AeProcessFilter;
import org.activebpel.rt.bpel.impl.list.AeProcessInstanceDetail;
import org.activebpel.rt.bpel.impl.list.AeProcessListResult;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.MessageElement;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.message.addressing.AddressingHeaders;
import org.apache.axis.message.addressing.Constants;
import org.apache.axis.message.addressing.To;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import AeAdminServices.BpelEngineAdminLocator;
import AeAdminServices.RemoteDebugSoapBindingStub;

public class ActiveBPELAdapter implements WorkflowEngineAdapter {
	
	static final Log logger = LogFactory.getLog(ActiveBPELAdapter.class);
	
	private String abAdminRoot = 
		"http://localhost:8080/active-bpel/services/";
	private String abAdminUrl = abAdminRoot + "BpelEngineAdmin";
	
	private RemoteDebugSoapBindingStub mRemote = null;
	
	private WorkflowStatusType workflowStatus = null;
	
	private WorkflowOutputType workflowOutput = null;
	
	private long processId;
	
	private QName workflowQName = null;
	
	private String workflowName = null;
	
	private String bprFileName = null;
	
	public ActiveBPELAdapter(String abAdminUrl)  {
		if (abAdminUrl != null) {
			this.abAdminUrl = abAdminUrl + "BpelEngineAdmin";
		}
		BpelEngineAdminLocator locator = new BpelEngineAdminLocator();
		try {
			URL url = new URL(this.abAdminUrl);
			this.mRemote = (RemoteDebugSoapBindingStub) locator.getAeActiveWebflowAdminPort(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ActiveBPELAdapter(String abAdminUrl,
			WorkflowOutputType output, WorkflowStatusType workflowStatus) {
		this(abAdminUrl);
		this.workflowOutput = output;
		this.workflowStatus = workflowStatus;
		
	}
	/** Deploys the ActiveBPEL specific BPEL archive
	 * @param bpelFileName
	 * @param workflowName
	 * @param wsdlRefArray
	 * @return
	 * @throws Exception
	 */
	
	public String deployBpr(String bpelFileName, String workflowName, WSDLReferences[] wsdlRefArray) throws Exception {
		String returnString = "success";
		this.workflowName = workflowName;
		this.bprFileName = BPRCreator.makeBpr(bpelFileName,this.workflowName, wsdlRefArray);
		returnString = this.mRemote.deployBpr(this.workflowName + ".bpr", 
				getBase64EncodedBpr(this.bprFileName));
		this.workflowQName = new QName(this.workflowName);
		return returnString;
	}
	
	
	private  WorkflowStatusType invokeProcess( 
			String partnerLinkName, StartInputType startInput) throws Exception {
		String serviceUrl = this.abAdminRoot + partnerLinkName;
		SOAPEnvelope env = new SOAPEnvelope();
		env.getBody().addChildElement(
				new SOAPBodyElement(startInput.getInputArgs().get_any()[0]));
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new URL(serviceUrl));
		AddressingHeaders headers = new AddressingHeaders();
		headers.setTo(new To(serviceUrl));
		call.setProperty(Constants.ENV_ADDRESSING_REQUEST_HEADERS, headers);
		
		SOAPEnvelope res = call.invoke(env);
		System.out.println("Result " + res.getAsString());
	    this.workflowStatus = WorkflowStatusType.Active;
		this.workflowOutput.set_any(new MessageElement[]{new MessageElement(res.getAsDOM())});
		//output.setOutputType(outputType);
		this.processId = getProcessId();
		return this.workflowStatus;
	}
	
	private static String getBase64EncodedBpr(String pathToBpr) {
		return Base64.encodeFromFile(pathToBpr);
	}
	
	public String deployWorkflow(String bpelFileName, String workflowName, WSDLReferences[] wsdlRefArray) throws WorkflowExceptionType {
		try {
			return deployBpr(bpelFileName, workflowName, wsdlRefArray);
		} catch (Exception e) {
			throw new WorkflowExceptionType();
		}
	}


	public WorkflowStatusType startWorkflow(StartInputType startInput) throws WorkflowExceptionType {
		WorkflowStatusType status = WorkflowStatusType.Pending;
		System.out.println("Starting workflow : " + this.workflowName);
		try {
			this.invokeProcess(this.workflowName + "Service", startInput);
		} catch (Exception e) {
			status = WorkflowStatusType.Failed;
			e.printStackTrace();
		}
		return status;
	}


	public WorkflowStatusType getWorkflowStatus() throws WorkflowExceptionType {
		try {
			AeProcessInstanceDetail detail = null;
			AeProcessFilter filter = new AeProcessFilter();
			filter.setProcessName(this.workflowQName);
			AeProcessListResult list = mRemote.getProcessList( filter );
			AeProcessInstanceDetail[] details = list.getRowDetails();
			for (int i=0; i < details.length; i++) {
				if (details[i].getProcessId() == this.processId) {
					detail = details[i];
				}
			}
			logger.info("State:" + detail.getState());
			int status = detail.getState();
			return getStatusFromString(status);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkflowExceptionType();
		}
	}


	public void suspend() throws WorkflowExceptionType {
		try {
			this.mRemote.suspendProcess(this.processId);
		} catch (Exception e) {
			throw new WorkflowExceptionType();
		}
	}


	public void resume() throws WorkflowExceptionType {
		try {
			this.mRemote.resumeProcess(this.processId);
		} catch (Exception e) {
			throw new WorkflowExceptionType();
		}		
		
	}


	public void cancel() throws WorkflowExceptionType {
		try {
			this.mRemote.terminateProcess(this.processId);
		} catch (Exception e) {
			throw new WorkflowExceptionType();
		}		
	}
	
	
	private int displayProcessList(QName workflowName) throws Exception {
	      AeProcessFilter filter = new AeProcessFilter();
	      filter.setProcessName(workflowName);
	      //filter.setAdvancedQuery("");
	      AeProcessListResult list = mRemote.getProcessList( filter );
	      if ( list.getTotalRowCount() <= 0 )
	      {
	         logger.debug( "No process info to display.");
	         return 0 ;
	      }
	      else
	      {
	         AeProcessInstanceDetail[] details = list.getRowDetails();
	         logger.debug("PID\tState\tName" );
	         for ( int i = 0 ; i < list.getTotalRowCount() ; i++ )
	         {
	            AeProcessInstanceDetail detail = details[i];
	            logger.debug( detail.getProcessId()   + "\t" +
	                                detail.getState() + "\t" +
	                                detail.getName() );
	         }

	         return list.getTotalRowCount();
	      }
	   }

	public static WorkflowStatusType getStatusFromString(int state) {
		if (state == 0) {
			return WorkflowStatusType.Pending;
		} else if (state == 1) {
			return WorkflowStatusType.Active;
		} else if (state == 2) {
			return WorkflowStatusType.Suspended;
		} else if (state == 3) {
			return WorkflowStatusType.Done;
		} else if (state == 4) {
			return WorkflowStatusType.Failed;
		} else {
			return WorkflowStatusType.Pending;
		}
		
	}
	
	private long getProcessId() throws RemoteException {
		AeProcessFilter filter = new AeProcessFilter();
		filter.setProcessName(this.workflowQName);
		AeProcessListResult list = mRemote.getProcessList( filter );
		return list.getRowDetails()[0].getProcessId();
		
	}
	public WorkflowOutputType getWorkflowOutput() {
		return workflowOutput;
	}

	public void setWorkflowOutput(WorkflowOutputType workflowOutput) {
		this.workflowOutput = workflowOutput;
	}

	public void setWorkflowStatus(WorkflowStatusType workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public void remove() throws  RemoteException {
		this.mRemote.terminateProcess(this.processId);
		// If activebpel exposes remote interface to remove bpr files
	}
}
