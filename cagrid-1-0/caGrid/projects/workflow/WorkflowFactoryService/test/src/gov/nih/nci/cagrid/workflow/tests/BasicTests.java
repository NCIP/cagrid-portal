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
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowOutputType;
import gov.nih.nci.cagrid.workflow.stubs.types.WorkflowStatusType;

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
	private static WorkflowFactoryServiceClient factoryClient = null;
	private static WorkflowServiceImplClient serviceClient = null;
	private static EndpointReferenceType epr = null;
	
	
	public BasicTests(String name) {
		super(name);
	}
	
	private void setup() {
		
	}
	public void testBasic() throws Exception {
		String inputFile = "inputTest1.xml";
		assertTrue(TEST_CONTAINER != null);
		FileWriter writer = null;
		this.factoryClient = new WorkflowFactoryServiceClient(url);
		WMSInputType input = createInput("Simple.bpel");
		WMSOutputType output = this.factoryClient.createWorkflow(input);
		this.epr = output.getWorkflowEPR();
		assertTrue(epr != null);
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
		status = this.serviceClient.getStatus();
		System.out.println(status.getValue());
		assertTrue(status !=null);
		assertTrue(status.getValue().equals("Active"));
		
	}

	public void testStatus() throws Exception {
		if (this.epr != null) {
			this.serviceClient = new WorkflowServiceImplClient(this.epr);
		} else {
			System.out.println("epr is null");
		}
		WorkflowStatusType status = this.serviceClient.getStatus();
		System.out.println(status.getValue());
		assertTrue(status != null);
	}
	
	public void testGetOutput() throws Exception {
		WorkflowOutputType output = this.serviceClient.getWorkflowOutput();
		System.out.println("result: " + AnyHelper.toSingleString(output.get_any()));
		assertTrue(output != null);
		
	}
	public void testCancel() throws Exception {
		
	}
	private static WMSInputType createInput(String bpelFile) throws Exception {
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
