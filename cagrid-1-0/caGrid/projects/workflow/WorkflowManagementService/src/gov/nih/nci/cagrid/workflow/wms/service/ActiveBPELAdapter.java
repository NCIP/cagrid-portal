package gov.nih.nci.cagrid.workflow.wms.service;

import java.net.URL;

import org.activebpel.rt.base64.Base64;

import AeAdminServices.BpelEngineAdminLocator;
import AeAdminServices.RemoteDebugSoapBindingStub;

public class ActiveBPELAdapter {
	//TODO: Read this from service config.
	protected static final String abAdminUrl = "http://localhost:8080/active-bpel/services/BpelEngineAdmin";
	
	
	public static String deployBpr(String bpelFileName, String pddFileName, String workflowName) throws Exception {
		String returnString = "success";
		String bprFileName = BPRCreator.makeBpr(bpelFileName, pddFileName, workflowName);
		BpelEngineAdminLocator locator = new BpelEngineAdminLocator();
		URL url = new URL(abAdminUrl);
		RemoteDebugSoapBindingStub mRemote = (RemoteDebugSoapBindingStub) locator
					.getAeActiveWebflowAdminPort(url);
		returnString = mRemote.deployBpr(workflowName + ".bpr", getBase64EncodedBpr(bprFileName));
		return returnString;
	}
	
	
	public static String invokeProcess(String partnerLinkName, String message) throws Exception {
		BpelEngineAdminLocator locator = new BpelEngineAdminLocator();
		URL url = new URL(abAdminUrl);
		RemoteDebugSoapBindingStub mRemote = (RemoteDebugSoapBindingStub) locator
					.getAeActiveWebflowAdminPort(url);
		String output = mRemote.invokeProcess(partnerLinkName, message);
		return output;
	}
	
	private static String getBase64EncodedBpr(String pathToBpr) {
		return Base64.encodeFromFile(pathToBpr);
	}

}
