package gov.nih.nci.cagrid.workflow.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import javax.xml.namespace.QName;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.workflow.client.WorkflowFactoryServiceClient;
import gov.nih.nci.cagrid.workflow.context.client.WorkflowServiceImplClient;
import gov.nih.nci.cagrid.workflow.stubs.types.StartInputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WMSInputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WMSOutputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WSDLReferences;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowInputType;

import org.apache.axis.message.MessageElement;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.globus.wsrf.test.GridTestCase;
import org.globus.wsrf.utils.AnyHelper;
import org.globus.wsrf.utils.XmlUtils;
import org.w3c.dom.Element;

public class BasicTests extends GridTestCase {

	public String url = "http://localhost:8080/wsrf/services/cagrid/WorkflowFactoryService";
	public BasicTests(String name) {
		super(name);
	}
	
	public void testBasic() throws Exception {
		String inputFile = "inputTest1.xml";
		assertTrue(TEST_CONTAINER != null);
		WSDLReferences[] wsdlRefArray = new WSDLReferences[1];
		wsdlRefArray[0] = new WSDLReferences();
		wsdlRefArray[0].setServiceUrl(new URI("http://localhost:9080/wsrf/services/cagrid/SampleService1"));
		wsdlRefArray[0].setWsdlLocation("http://localhost:8080/wsrf/share/schema/SampleService1/SampleService1_flattened.wsdl");
		wsdlRefArray[0].setWsdlNamespace(new URI("http://workflow.cagrid.nci.nih.gov/SampleService1"));
		FileWriter writer = null;
		WorkflowFactoryServiceClient client = new WorkflowFactoryServiceClient(url);
		WMSInputType input = createInput("Simple.bpel");
		WMSOutputType output = client.createWorkflow(input);
		EndpointReferenceType epr = output.getWorkflowEPR();
		writer = new FileWriter("workflow_" + input.getWorkflowName() + "_epr");
		writer.write( 
				ObjectSerializer.toString(epr, new QName("", "WMS_EPR")));
		WorkflowServiceImplClient wclient = new WorkflowServiceImplClient(epr);
		StartInputType startInput = new StartInputType();
		WorkflowInputType inputArgs = new WorkflowInputType();
		FileInputStream in = new FileInputStream("input.xml");
		Element e2 = XmlUtils.newDocument(in).getDocumentElement();
		System.out.println(XmlUtils.toString(e2));
		MessageElement anyContent = AnyHelper.toAny(new MessageElement(e2));
		inputArgs.set_any(new MessageElement[] {anyContent});
		startInput.setInputArgs(inputArgs);
		wclient.start(startInput);
	}

	public static WMSInputType createInput(String bpelFile) throws Exception {
		WMSInputType input = new WMSInputType();
		String bpelProcess = Utils.fileToStringBuffer(new File(bpelFile)).toString();
		input.setBpelDoc(bpelProcess);
		input.setWorkflowName("Simple");
		WSDLReferences[] wsdlRefArray = new WSDLReferences[1];
		wsdlRefArray[0] = new WSDLReferences();
		wsdlRefArray[0].setServiceUrl(new URI("http://localhost:8080/wsrf/services/cagrid/SampleService1"));
		wsdlRefArray[0].setWsdlLocation("http://localhost:8080/wsrf/share/schema/SampleService1/SampleService1_flattened.wsdl");
		wsdlRefArray[0].setWsdlNamespace(new URI("http://workflow.cagrid.nci.nih.gov/SampleService1"));
		input.setWsdlReferences(wsdlRefArray);
		return input;
	}
}
