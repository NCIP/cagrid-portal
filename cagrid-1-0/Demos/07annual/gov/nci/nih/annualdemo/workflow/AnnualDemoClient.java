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
	
	public WMSInputType createInput1(Calendar terminationTime, 
			String bpelFile) throws Exception {
		WMSInputType input = new WMSInputType();
		input.setTerminationTime(terminationTime);
		String bpelProcess = Utils.fileToStringBuffer(new File(bpelFile)).toString();
		input.setBpelDoc(bpelProcess);
		input.setWorkflowName("geWorkBenchWF");
		WSDLReferences[] wsdlRefArray = new WSDLReferences[2];
		wsdlRefArray[0] = new WSDLReferences();
		wsdlRefArray[0].setServiceUrl(new URI("http://156.145.31.100:8080/wsrf/services/cagrid/HierarchicalClustering"));
		wsdlRefArray[0].setWsdlLocation("http://spirulina.ci.uchicago.edu:8080/wsrf/share/schema/_cagrid_HierarchicalClustering/HierarchicalClustering.wsdl");
		wsdlRefArray[0].setWsdlNamespace(new URI("http://cagrid.geworkbench.columbia.edu/HierarchicalClustering"));
		
		wsdlRefArray[1] = new WSDLReferences();
		wsdlRefArray[1].setServiceUrl(new URI("http://156.145.31.100:8080/wsrf/services/cagrid/HierarchicalClustering"));
		wsdlRefArray[1].setWsdlLocation("http://spirulina.ci.uchicago.edu:8080/wsrf/geWorkbench.wsdl");
		wsdlRefArray[1].setWsdlNamespace(new URI("http://annualdemo.geworkbench.cagrid.nci.nih.gov/bpel"));
		
		
		
		
		input.setWsdlReferences(wsdlRefArray);
		return input;
	}
	
	public WMSInputType createInput2(Calendar terminationTime, 
			String bpelFile) throws Exception {
		WMSInputType input = new WMSInputType();
		input.setTerminationTime(terminationTime);
		String bpelProcess = Utils.fileToStringBuffer(new File(bpelFile)).toString();
		input.setBpelDoc(bpelProcess);
		input.setWorkflowName("genepattern");
		WSDLReferences[] wsdlRefArray = new WSDLReferences[2];
		wsdlRefArray[0] = new WSDLReferences();
		wsdlRefArray[0].setServiceUrl(new URI("http://node255.broad.mit.edu:6060/wsrf/services/cagrid/ConsensusClusteringSTATMLService"));
		wsdlRefArray[0].setWsdlLocation("http://18.103.11.105:6060/wsrf/share/schema/_cagrid_ConsensusClusteringSTATMLService/ConsensusClusteringSTATMLService.wsdl");
		wsdlRefArray[0].setWsdlNamespace(new URI("http://org.genepattern.cagrid.service/ConsensusClusteringSTATMLService/service"));
		
		wsdlRefArray[1] = new WSDLReferences();
		wsdlRefArray[1].setServiceUrl(new URI("http://node255.broad.mit.edu:6060/wsrf/services/cagrid/ConsensusClusteringSTATMLService"));
		wsdlRefArray[1].setWsdlLocation("http://spirulina.ci.uchicago.edu:8080/wsrf/genepattern.wsdl");
		wsdlRefArray[1].setWsdlNamespace(new URI("http://annualdemo.genepattern.cagrid.nci.nih.gov/bpel"));
		
		
		
		
		input.setWsdlReferences(wsdlRefArray);
		return input;
	}
	
	public WMSInputType createInput3(Calendar terminationTime, 
			String bpelFile) throws Exception {
		WMSInputType input = new WMSInputType();
		input.setTerminationTime(terminationTime);
		String bpelProcess = Utils.fileToStringBuffer(new File(bpelFile)).toString();
		input.setBpelDoc(bpelProcess);
		input.setWorkflowName("PreProp");
		WSDLReferences[] wsdlRefArray = new WSDLReferences[3];
		wsdlRefArray[0] = new WSDLReferences();
		wsdlRefArray[0].setServiceUrl(new URI("http://spirulina.ci.uchicago.edu:8080/wsrf/services/cagrid/MageTranslationServices"));
		wsdlRefArray[0].setWsdlLocation("http://spirulina.ci.uchicago.edu:8080/wsrf/share/schema/MageTranslationServices/MageTranslationServices.wsdl");
		wsdlRefArray[0].setWsdlNamespace(new URI("http://annualdemo.cagrid.nci.nih.gov/MageTranslationServices"));
		wsdlRefArray[1] = new WSDLReferences();
		wsdlRefArray[1].setServiceUrl(new URI("http://156.145.31.100:8080/wsrf/services/cagrid/HierarchicalClustering"));
		wsdlRefArray[1].setWsdlLocation("http://spirulina.ci.uchicago.edu:8080/wsrf/share/schema/_cagrid_HierarchicalClustering/HierarchicalClustering.wsdl");
		wsdlRefArray[1].setWsdlNamespace(new URI("http://cagrid.geworkbench.columbia.edu/HierarchicalClustering"));
		
		wsdlRefArray[2] = new WSDLReferences();
		wsdlRefArray[2].setServiceUrl(new URI("http://156.145.31.100:8080/wsrf/services/cagrid/HierarchicalClustering"));
		wsdlRefArray[2].setWsdlLocation("http://spirulina.ci.uchicago.edu:8080/wsrf/geWorkbench.wsdl");
		wsdlRefArray[2].setWsdlNamespace(new URI("http://annualdemo.geworkbench.cagrid.nci.nih.gov/bpel"));
		
		input.setWsdlReferences(wsdlRefArray);
		return input;
	}
	public  WMSInputType createInput(Calendar terminationTime, 
			String bpelFile) throws Exception {
		WMSInputType input = new WMSInputType();
		input.setTerminationTime(terminationTime);
		String bpelProcess = Utils.fileToStringBuffer(new File(bpelFile)).toString();
		input.setBpelDoc(bpelProcess);
		input.setWorkflowName("AnnualDemo07");
		WSDLReferences[] wsdlRefArray = new WSDLReferences[4];
		wsdlRefArray[0] = new WSDLReferences();
		wsdlRefArray[0].setServiceUrl(new URI("http://spirulina.ci.uchicago.edu:8080/wsrf/services/cagrid/MageTranslationServices"));
		wsdlRefArray[0].setWsdlLocation("http://spirulina.ci.uchicago.edu:8080/wsrf/share/schema/MageTranslationServices/MageTranslationServices.wsdl");
		wsdlRefArray[0].setWsdlNamespace(new URI("http://annualdemo.cagrid.nci.nih.gov/MageTranslationServices"));
		
		wsdlRefArray[1] = new WSDLReferences();
		wsdlRefArray[1].setServiceUrl(new URI("http://cagrid-caarray-demo.nci.nih.gov:80/wsrf/services/caGrid/CaArraySvc"));
		wsdlRefArray[1].setWsdlLocation("http://spirulina.ci.uchicago.edu:8080/wsrf/AnnualDemo07.wsdl");
		wsdlRefArray[1].setWsdlNamespace(new URI("http://annualdemo.cagrid.nci.nih.gov/bpel"));
		
		
		wsdlRefArray[2] = new WSDLReferences();
		wsdlRefArray[2].setServiceUrl(new URI("http://cagrid-caarray-demo.nci.nih.gov:80/wsrf/services/caGrid/CaArraySvc"));
		wsdlRefArray[2].setWsdlLocation("http://cagrid-caarray-demo.nci.nih.gov:80/wsrf/share/schema/CaArraySvc/CaArraySvc.wsdl");
		wsdlRefArray[2].setWsdlNamespace(new URI("http://caarray.cagrid.nci.nih.gov/CaArraySvc"));
		
		wsdlRefArray[3] = new WSDLReferences();
		wsdlRefArray[3].setServiceUrl(new URI("http://cagrid-caarray-demo.nci.nih.gov:80/wsrf/services/caGrid/CaArraySvc"));
		wsdlRefArray[3].setWsdlLocation("http://cagrid-caarray-demo.nci.nih.gov:80/wsrf/share/schema/CaArraySvc/DataService.wsdl");
		wsdlRefArray[3].setWsdlNamespace(new URI("http://gov.nih.nci.cagrid.data/DataService"));
		
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
		FileInputStream in = new FileInputStream("input11.xml");
		Element e2 = XmlUtils.newDocument(in).getDocumentElement();
		System.out.println(XmlUtils.toString(e2));
		MessageElement anyContent = AnyHelper.toAny(new MessageElement(e2));
		inputArgs.set_any(new MessageElement[] {anyContent});
		startInput.setInputArgs(inputArgs);
		WorkflowStatusType status = this.serviceClient.start(startInput);
		//status = this.serviceClient.getStatus();
		System.out.println(status.getValue());
	}
	public void run1() throws Exception {
		FileWriter writer = null;
		this.factoryClient = new WorkflowFactoryServiceClient(url);
		WMSInputType input = createInput1(null, "geWorkBench.bpel");
		WMSOutputType output = this.factoryClient.createWorkflow(input);
		this.epr = output.getWorkflowEPR();
		writer = new FileWriter("workflow_" + input.getWorkflowName() + "_epr");
		writer.write( 
				ObjectSerializer.toString(epr, new QName("", "WMS_EPR")));
		this.serviceClient = new WorkflowServiceImplClient(this.epr);
		StartInputType startInput = new StartInputType();
		WorkflowInputType inputArgs = new WorkflowInputType();
		FileInputStream in = new FileInputStream("input10.xml");
		Element e2 = XmlUtils.newDocument(in).getDocumentElement();
		System.out.println(XmlUtils.toString(e2));
		MessageElement anyContent = AnyHelper.toAny(new MessageElement(e2));
		inputArgs.set_any(new MessageElement[] {anyContent});
		startInput.setInputArgs(inputArgs);
		WorkflowStatusType status = this.serviceClient.start(startInput);
		//status = this.serviceClient.getStatus();
		System.out.println(status.getValue());
	}
	public void run2() throws Exception {
		FileWriter writer = null;
		this.factoryClient = new WorkflowFactoryServiceClient(url);
		WMSInputType input = createInput2(null, "genepattern.bpel");
		WMSOutputType output = this.factoryClient.createWorkflow(input);
		this.epr = output.getWorkflowEPR();
		writer = new FileWriter("workflow_" + input.getWorkflowName() + "_epr");
		writer.write( 
				ObjectSerializer.toString(epr, new QName("", "WMS_EPR")));
		this.serviceClient = new WorkflowServiceImplClient(this.epr);
		StartInputType startInput = new StartInputType();
		WorkflowInputType inputArgs = new WorkflowInputType();
		FileInputStream in = new FileInputStream("input3.xml");
		Element e2 = XmlUtils.newDocument(in).getDocumentElement();
		System.out.println(XmlUtils.toString(e2));
		MessageElement anyContent = AnyHelper.toAny(new MessageElement(e2));
		inputArgs.set_any(new MessageElement[] {anyContent});
		startInput.setInputArgs(inputArgs);
		WorkflowStatusType status = this.serviceClient.start(startInput);
		//status = this.serviceClient.getStatus();
		System.out.println(status.getValue());
	}
	public void run3() throws Exception {
		FileWriter writer = null;
		this.factoryClient = new WorkflowFactoryServiceClient(url);
		WMSInputType input = createInput3(null, "PreProp.bpel");
		WMSOutputType output = this.factoryClient.createWorkflow(input);
		this.epr = output.getWorkflowEPR();
		writer = new FileWriter("workflow_" + input.getWorkflowName() + "_epr");
		writer.write( 
				ObjectSerializer.toString(epr, new QName("", "WMS_EPR")));
		this.serviceClient = new WorkflowServiceImplClient(this.epr);
		StartInputType startInput = new StartInputType();
		WorkflowInputType inputArgs = new WorkflowInputType();
		FileInputStream in = new FileInputStream("input6.xml");
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
