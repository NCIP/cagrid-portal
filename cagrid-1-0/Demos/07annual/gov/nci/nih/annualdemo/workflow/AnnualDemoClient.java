package gov.nci.nih.annualdemo.workflow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Calendar;

import javax.xml.namespace.QName;

import org.apache.axis.message.MessageElement;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.globus.wsrf.utils.AnyHelper;
import org.globus.wsrf.utils.XmlUtils;
import org.w3c.dom.Element;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.workflow.client.WorkflowFactoryServiceClient;
import gov.nih.nci.cagrid.workflow.context.client.WorkflowServiceImplClient;
import gov.nih.nci.cagrid.workflow.stubs.types.StartInputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WMSInputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WMSOutputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WSDLReferences;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowInputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowStatusType;


public class AnnualDemoClient {
	public String url = "http://spirulina.ci.uchicago.edu:8080/wsrf/services/cagrid/WorkflowFactoryService";
	private  WorkflowFactoryServiceClient factoryClient = null;
	private  WorkflowServiceImplClient serviceClient = null;
	private  EndpointReferenceType epr = null;
	
	public  WMSInputType createInput(Calendar terminationTime, 
			String bpelFile) throws Exception {
		WMSInputType input = new WMSInputType();
		input.setTerminationTime(terminationTime);
		String bpelProcess = Utils.fileToStringBuffer(new File(bpelFile)).toString();
		input.setBpelDoc(bpelProcess);
		input.setWorkflowName("AnnualDemo07");
		WSDLReferences[] wsdlRefArray = new WSDLReferences[4];
		wsdlRefArray[0] = new WSDLReferences();
		wsdlRefArray[0].setServiceUrl(new URI("http://cagrid-portal.nci.nih.gov:8080/wsrf/services/cagrid/CaArraySvc"));
		wsdlRefArray[0].setWsdlLocation("http://spirulina.ci.uchicago.edu:8080/wsrf/AnnualDemo07.wsdl");
		wsdlRefArray[0].setWsdlNamespace(new URI("http://annualdemo.cagrid.nci.nih.gov/bpel"));
		wsdlRefArray[1] = new WSDLReferences();
		wsdlRefArray[1].setServiceUrl(new URI("http://cagrid-portal.nci.nih.gov:8080/wsrf/services/cagrid/CaArraySvc"));
		wsdlRefArray[1].setWsdlLocation("http://cagrid-portal.nci.nih.gov:8080/wsrf/share/schema/CaArraySvc/CaArraySvc.wsdl");
		wsdlRefArray[1].setWsdlNamespace(new URI("http://caarray.cagrid.nci.nih.gov/CaArraySvc"));
		wsdlRefArray[2] = new WSDLReferences();
		wsdlRefArray[2].setServiceUrl(new URI("http://cagrid-portal.nci.nih.gov:8080/wsrf/services/cagrid/CaArraySvc"));
		wsdlRefArray[2].setWsdlLocation("http://cagrid-portal.nci.nih.gov:8080/wsrf/share/schema/CaArraySvc/DataService.wsdl");
		wsdlRefArray[2].setWsdlNamespace(new URI("http://gov.nih.nci.cagrid.data/DataService"));
		wsdlRefArray[3] = new WSDLReferences();
		wsdlRefArray[3].setServiceUrl(new URI("http://spirulina.ci.uchicago.edu:8080/wsrf/services/cagrid/MageTranslationServices"));
		wsdlRefArray[3].setWsdlLocation("http://spirulina.ci.uchicago.edu:8080/wsrf/share/schema/MageTranslationServices/MageTranslationServices.wsdl");
		wsdlRefArray[3].setWsdlNamespace(new URI("http://annualdemo.cagrid.nci.nih.gov/MageTranslationServices"));
		input.setWsdlReferences(wsdlRefArray);
		return input;
	}
	
	public void run() throws Exception {
		FileWriter writer = null;
		this.factoryClient = new WorkflowFactoryServiceClient(url);
		WMSInputType input = createInput(null, "AnnualDemo.bpel");
		WMSOutputType output = this.factoryClient.createWorkflow(input);
		this.epr = output.getWorkflowEPR();
		writer = new FileWriter("workflow_" + input.getWorkflowName() + "_epr");
		writer.write( 
				ObjectSerializer.toString(epr, new QName("", "WMS_EPR")));
		this.serviceClient = new WorkflowServiceImplClient(this.epr);
		StartInputType startInput = new StartInputType();
		WorkflowInputType inputArgs = new WorkflowInputType();
		FileInputStream in = new FileInputStream("input.xml");
		Element e2 = XmlUtils.newDocument(in).getDocumentElement();
		System.out.println(XmlUtils.toString(e2));
		MessageElement anyContent = AnyHelper.toAny(new MessageElement(e2));
		inputArgs.set_any(new MessageElement[] {anyContent});
		startInput.setInputArgs(inputArgs);
		WorkflowStatusType status = this.serviceClient.start(startInput);
		//status = this.serviceClient.getStatus();
		System.out.println(status.getValue());
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		AnnualDemoClient demo = new AnnualDemoClient();
		demo.run();

	}

}
