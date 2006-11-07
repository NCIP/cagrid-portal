package gov.nih.nci.cagrid.workflow.service;

import gov.nih.nci.cagrid.workflow.stubs.types.StartInputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WSDLReferences;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowExceptionType;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowOutputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowStatusType;

import java.net.URL;

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

import AeAdminServices.BpelEngineAdminLocator;
import AeAdminServices.RemoteDebugSoapBindingStub;

public class ActiveBPELAdapter implements WorkflowEngineAdapter {
	
	private String abAdminRoot = 
		"http://localhost:8080/active-bpel/services/";
	private String abAdminUrl = abAdminRoot + "BpelEngineAdmin";
	
	private RemoteDebugSoapBindingStub mRemote = null;
	
	private WorkflowStatusType workflowStatus = null;
	
	private WorkflowOutputType workflowOutput = null;
	
	public ActiveBPELAdapter(String abAdminUrl)  {
		if (abAdminUrl != null) {
			this.abAdminUrl = abAdminUrl;
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
		String bprFileName = BPRCreator.makeBpr(bpelFileName,workflowName, wsdlRefArray);
		returnString = this.mRemote.deployBpr(workflowName + ".bpr", getBase64EncodedBpr(bprFileName));
		return returnString;
	}
	
	
	public  WorkflowStatusType invokeProcess( 
			String partnerLinkName, StartInputType startInput) throws Exception {
		String serviceUrl = this.abAdminRoot + partnerLinkName;
		return callService(serviceUrl, startInput);
	}
	
	private static String getBase64EncodedBpr(String pathToBpr) {
		return Base64.encodeFromFile(pathToBpr);
	}
	
	public WorkflowStatusType callService(String serviceUrl, 
			StartInputType startInput) throws Exception {
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
		return this.workflowStatus;
	}


	public String deployWorkflow(String bpelFileName, String workflowName, WSDLReferences[] wsdlRefArray) throws WorkflowExceptionType {
		try {
			return deployBpr(bpelFileName, workflowName, wsdlRefArray);
		} catch (Exception e) {
			throw new WorkflowExceptionType();
		}
	}


	public WorkflowStatusType startWorkflow(String workflowName, 
			StartInputType startInput) throws WorkflowExceptionType {
		WorkflowStatusType status = WorkflowStatusType.Pending;
		System.out.println("Starting workflow : " + workflowName);
		try {
			this.invokeProcess(workflowName, startInput);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			status = WorkflowStatusType.Failed;
			e.printStackTrace();
		}
		return status;
	}


	public WorkflowStatusType getWorkflowStatus(String workflowName) throws WorkflowExceptionType {
		try {
			QName workflowQName = new QName(workflowName);
			System.out.println("API Version: " + mRemote.getAPIVersion());
			String statusString = getWorkflowStatus(workflowQName);
			return getStatusFromString(statusString);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkflowExceptionType();
		}
	}


	public void suspend(String workflowName) throws WorkflowExceptionType {
		try {
			AeProcessFilter filter = new AeProcessFilter();
			filter.setProcessName(new QName(workflowName));
			AeProcessListResult list = mRemote.getProcessList( filter );
			long processId =  list.getRowDetails()[0].getProcessId();
			this.mRemote.suspendProcess(processId);
		} catch (Exception e) {
			throw new WorkflowExceptionType();
		}
	}


	public void resume(String workflowName) throws WorkflowExceptionType {
		try {
			AeProcessFilter filter = new AeProcessFilter();
			filter.setProcessName(new QName(workflowName));
			AeProcessListResult list = mRemote.getProcessList( filter );
			long processId =  list.getRowDetails()[0].getProcessId();
			this.mRemote.resumeProcess(processId);
		} catch (Exception e) {
			throw new WorkflowExceptionType();
		}		
		
	}


	public void cancel(String workflowName) throws WorkflowExceptionType {
		try {
			AeProcessFilter filter = new AeProcessFilter();
			filter.setProcessName(new QName(workflowName));
			AeProcessListResult list = mRemote.getProcessList( filter );
			long processId =  list.getRowDetails()[0].getProcessId();
			this.mRemote.terminateProcess(processId);
		} catch (Exception e) {
			throw new WorkflowExceptionType();
		}		
	}
	
	public String getProcessStatus(QName workflowName) throws Exception {
		AeProcessFilter filter = new AeProcessFilter();
	      filter.setProcessName(workflowName);
	      AeProcessListResult list = mRemote.getProcessList( filter );
	      System.out.println("State: " + list.getRowDetails()[0].getState());
	      displayProcessList(workflowName);
	      return "State: " +list.getRowDetails()[0].getState() ;
	}
	
	public String getWorkflowStatus(QName workflowQName) throws Exception {
		AeProcessFilter filter = new AeProcessFilter();
	     filter.setProcessName(workflowQName);
	     AeProcessListResult list = mRemote.getProcessList( filter );
	     AeProcessInstanceDetail[] details = list.getRowDetails();
	     AeProcessInstanceDetail detail = details[0];
	     System.out.println("State:" + detail.getState());
	     displayProcessList(workflowQName);
	     return " " + detail.getState();
	}

	public int displayProcessList(QName workflowName) throws Exception {
	      AeProcessFilter filter = new AeProcessFilter();
	      filter.setProcessName(workflowName);
	      //filter.setAdvancedQuery("");
	      AeProcessListResult list = mRemote.getProcessList( filter );
	      if ( list.getTotalRowCount() <= 0 )
	      {
	         System.out.println( "No process info to display.");
	         return 0 ;
	      }
	      else
	      {
	         AeProcessInstanceDetail[] details = list.getRowDetails();
	         System.out.println( "PID\tState\tName" );
	         for ( int i = 0 ; i < list.getTotalRowCount() ; i++ )
	         {
	            AeProcessInstanceDetail detail = details[i];
	            System.out.println( detail.getProcessId()   + "\t" +
	                                detail.getState() + "\t" +
	                                detail.getName() );
	         }

	         return list.getTotalRowCount();
	      }
	   }

	public static WorkflowStatusType getStatusFromString(String state) {
		WorkflowStatusType status = WorkflowStatusType.Pending;
		return status;
	}

	public WorkflowOutputType getWorkflowOutput() {
		return workflowOutput;
	}

	public void setWorkflowOutput(WorkflowOutputType workflowOutput) {
		this.workflowOutput = workflowOutput;
	}

	public WorkflowStatusType getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(WorkflowStatusType workflowStatus) {
		this.workflowStatus = workflowStatus;
	}
}
