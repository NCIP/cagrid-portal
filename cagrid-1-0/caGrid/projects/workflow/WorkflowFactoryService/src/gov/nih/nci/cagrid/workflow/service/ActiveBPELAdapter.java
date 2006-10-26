package gov.nih.nci.cagrid.workflow.service;

import gov.nih.nci.cagrid.workflow.stubs.types.StartInputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WSDLReferences;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowExceptionType;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowOutputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowStatusType;

import java.net.MalformedURLException;
import java.net.URL;

import org.activebpel.rt.bpel.impl.list.AeProcessFilter;
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
	
	public  static WorkflowStatusType callService(String serviceUrl, 
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
		WorkflowStatusType output = WorkflowStatusType.Active;
		WorkflowOutputType outputType = new WorkflowOutputType();
		outputType.set_any(new MessageElement[]{new MessageElement(res.getAsDOM())});
		//output.setOutputType(outputType);
		return output;
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
		String partnerLinkName = workflowName + "Service";
		try {
			this.invokeProcess(partnerLinkName, startInput);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			status = WorkflowStatusType.Failed;
		}
		return status;
	}


	public WorkflowStatusType getWorkflowStatus(String workflowName) throws WorkflowExceptionType {
		// TODO Auto-generated method stub
		return null;
	}


	public void suspend(String workflowName) throws WorkflowExceptionType {
		// TODO Auto-generated method stub
		
	}


	public void resume(String workflowName) throws WorkflowExceptionType {
		// TODO Auto-generated method stub
		
	}


	public void cancel(String workflowName) throws WorkflowExceptionType {
		// TODO Auto-generated method stub
		
	}
	
	/*public int displayProcessList() throws Exception {
	      AeProcessFilter filter = new AeProcessFilter();
	      filter.setAdvancedQuery("");
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
	                                detail.getStateReason() + "\t" +
	                                detail.getName() );
	         }

	         return list.getTotalRowCount();
	      }
	   }*/

}
