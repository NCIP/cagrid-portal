package gov.nci.nih.cabig.client;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.globus.cagrid.RProteomics.stubs.Echo;
import org.globus.cagrid.RProteomics.stubs.EchoResponse;

import caBIG.RProteomics.*;

public class TestClient {
	protected static final String DEFAULT_URL_BASE =
		  "http://localhost:8080/active-bpel/services/";
	public static final String XML_SCHEMA_NAMESPACE =
		  "http://www.w3.org/2001/XMLSchema";
	private static final String RPROT_TYPES_NAMESPACE = 
		"gme://RProteomics.caBIG/5/edu.duke.cabig.rproteomics.domain.serviceInterface";
	private static final String RPROT_NAMESPACE= 
		"http://www.globus.org/namespaces/cagrid/RProteomics/RProteomics";
	protected String urlString;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	String urlPrefix = DEFAULT_URL_BASE;
	  if (args.length > 0)
	    urlPrefix = args[0];

	  try {
	    TestClient testClient = new TestClient();
	    testClient.setURL(urlPrefix + "RproteomicsClientPartnerLinkTypeService");
	    System.out.println(testClient.callService());
	  }
	  catch (Exception e) {
	    System.err.println(e.toString());
	  }
	}

	public String getURL() {
	  return urlString;
	}
	public void setURL(String u) {
	  urlString = u;
	}

	/**
	 * Creates a SOAP RPC call, calls the BPEL process Web service, and returns
	 * the result returned from the server.
	 *
	 * @return the string returned by the BPEL process
	 */
	public String callService() throws Exception {
	  // Call BPEL process Web service using RPC
	  Call call = createCall();
	  String result = null;
	  EchoResponse echoResponse = null;
	  LsidType[] lsidArrayResponse = null;
	  try {
		  Echo echoInputType = new Echo();
		  LsidType byt = new LsidType("ravi");
		  LsidType byt1 = new LsidType("kiran");
		  LsidType[] lsidArray = new LsidType[] {byt, byt1} ;
		  echoInputType.setLsids(lsidArray);
		  echoResponse = (EchoResponse) call.invoke(new Object[] {echoInputType}); 
		  lsidArrayResponse = echoResponse.getResponse();
		  for(int i =0; i< lsidArrayResponse.length;i++) {
			  result = result + lsidArrayResponse[i].getValue();
		  }
	  }
	  catch (Exception e) {
	    result = "Exception seen: " + e.toString();
	    e.printStackTrace();
	  }

	  return result;
	}

	/**
	 * Creates and returns an RPC SOAP call.
	 *
	 * @return an RPC SOAP call
	 */
	protected Call createCall() throws Exception {
	  QName echoQName = new QName(RPROT_NAMESPACE, "echo");
	  QName echoResponseQName = new QName(RPROT_NAMESPACE, "echoResponse");

	  Service service = new Service();
	  Call call = (Call)service.createCall();
	  System.out.println("Calling : "+ urlString);
	  call.setTargetEndpointAddress(new java.net.URL(urlString));

	  call.setOperationName("invokeEcho");
	  
	  call.addParameter("parameters", echoQName, ParameterMode.IN);
	  call.setReturnType(echoResponseQName, EchoResponse.class);
/*
	  // Set serializers and deserializers
	  register(call, BrandYearType.class, bytQName);
	  register(call, Type.class, typeQName);
	  register(call, IdAmountInstock.class, iaiQName);
*/
	  register(call, Echo.class, echoQName);
	  register(call, EchoResponse.class, echoResponseQName);
	  return call;
	}

	protected void register(Call call, Class klass, QName qname) {
	  System.out.println("Registering: "+ qname);
	  call.registerTypeMapping(klass,qname,BeanSerializerFactory.class,
				   BeanDeserializerFactory.class);
	}

}
