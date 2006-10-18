package gov.nih.nci.cagrid.workflow.service;

import gov.nih.nci.cagrid.workflow.stubs.types.StartInputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WMSInputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WMSOutputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WSDLReferences;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowOutputType;

import java.net.URL;

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

public class ActiveBPELAdapter {
	
	/** Deploys the ActiveBPEL specific BPEL archive
	 * @param bpelFileName
	 * @param workflowName
	 * @param wsdlRefArray
	 * @return
	 * @throws Exception
	 */
	public static String deployBpr(String abAdminUrl, String bpelFileName, String workflowName, WSDLReferences[] wsdlRefArray) throws Exception {
		String returnString = "success";
		String bprFileName = BPRCreator.makeBpr(bpelFileName,workflowName, wsdlRefArray);
		BpelEngineAdminLocator locator = new BpelEngineAdminLocator();
		URL url = new URL(abAdminUrl);
		RemoteDebugSoapBindingStub mRemote = (RemoteDebugSoapBindingStub) locator
					.getAeActiveWebflowAdminPort(url);
		returnString = mRemote.deployBpr(workflowName + ".bpr", getBase64EncodedBpr(bprFileName));
		return returnString;
	}
	
	
	public static WMSOutputType invokeProcess(String abServiceRoot, String partnerLinkName, StartInputType startInput) throws Exception {
		String serviceUrl = abServiceRoot + partnerLinkName;
		return callService(serviceUrl, startInput);
	}
	
	private static String getBase64EncodedBpr(String pathToBpr) {
		return Base64.encodeFromFile(pathToBpr);
	}
	
	public  static WMSOutputType callService(String serviceUrl, StartInputType startInput) throws Exception {
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
		WMSOutputType output = new WMSOutputType();
	
		WorkflowOutputType outputType = new WorkflowOutputType();
		outputType.set_any(new MessageElement[]{new MessageElement(res.getAsDOM())});
		//output.setOutputType(outputType);
		return output;
	}
}
