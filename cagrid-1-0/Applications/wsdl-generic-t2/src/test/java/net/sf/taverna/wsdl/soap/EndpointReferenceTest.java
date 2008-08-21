package net.sf.taverna.wsdl.soap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import net.sf.taverna.wsdl.parser.TypeDescriptor;
import net.sf.taverna.wsdl.parser.WSDLParser;
import net.sf.taverna.wsdl.testutils.LocationConstants;
import org.apache.axis.client.Call;

import org.apache.axis.message.SOAPBodyElement;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Node;

public class EndpointReferenceTest implements LocationConstants{

	@Ignore("Integration test")
	@Test
	public void testEndpointReference() throws Exception {
		BodyBuilder builder = createBuilder("http://192.168.1.65:8080/wsrf/services/CounterService?wsdl", "add");	
		String parameters = "<parameters xmlns=\"http://counter.com\">5</parameters>";
		Map<String,Object> inputMap = new HashMap<String, Object>();
		inputMap.put("parameters", parameters);
		String EPR = "<wsa:EndpointReference xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/03/addressing\" xmlns=\"http://counter.com\"><wsa:Address>http://192.168.1.65:8080/wsrf/services/CounterService</wsa:Address>" +
				"<wsa:ReferenceProperties><CounterKey>3201085</CounterKey></wsa:ReferenceProperties>" +
				"<wsa:ReferenceParameters/></wsa:EndpointReference> ";
		System.out.println(EPR);
		inputMap.put("EndpointReference",EPR);
		
		List<String> outputNames = new ArrayList<String>();		
		WSDLParser wsdlParser = new WSDLParser("http://192.168.1.65:8080/wsrf/services/CounterService?wsdl");	
		WSDLSOAPInvoker invoker = new WSDLSOAPInvoker(wsdlParser,
				"add", outputNames);
		//Call call = invoker.getCall(null);
		//invoker.processEndpointReference(inputMap, call);
		invoker.invoke(inputMap);
		assert(outputNames!=null);
		
		//SOAPBodyElement body = builder.build(inputMap);
		
		//String xml = body.getAsString();
		
		//System.out.println(xml);
	}
	
	
	
	protected BodyBuilder createBuilder(String wsdl, String operation) throws Exception {
		WSDLParser parser = new WSDLParser(wsdl);
		return BodyBuilderFactory.instance().create(parser, operation, parser.getOperationInputParameters(operation));
	}
}
