package gov.nci.nih.cabig.annualdemo.client;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.globus.cagrid.RProteomics.stubs.EchoResponse;

import caBIG.RProteomics.PercentileType;
import caBIG.RProteomics.WindowType;
import gov.nih.nci.cagrid.cql.CQLQueryType;
import gov.nih.nci.cagrid.rproteomics.stubs.*;

public class TestClient {
	protected static final String DEFAULT_URL_BASE =
		  "http://localhost:8080/active-bpel/services/";
	public static final String XML_SCHEMA_NAMESPACE =
		  "http://www.w3.org/2001/XMLSchema";
	private static final String RPROT_TYPES_NAMESPACE = 
		"gme://RProteomics.caBIG/5/edu.duke.cabig.rproteomics.domain.serviceInterface";
	private static final String RPROT_NAMESPACE= 
		"http://www.globus.org/namespaces/cagrid/RProteomics/RProteomics";
	private static final String RPROT_DATA_NAMESPACE =
		"http://rproteomics.cagrid.nci.nih.gov/RPData";
	private static final String RPROT_DATA_TYPES_NAMESPACE=
		"http://CQL.caBIG/1/gov.nih.nci.cagrid.CQL";
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
	    testClient.setURL(urlPrefix + "WorkFlowClientService");
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
	public String callService() throws Exception {
	  // Call BPEL process Web service using RPC
	  Call call = createCall();
	  String result = null;
	  WorkFlowOutputType reponse = null;
	  WorkFlowInputType input = new WorkFlowInputType();
	  CQLQueryType query = new CQLQueryType();
	  //EchoResponse echoResponse = null;
	  LsidType[] lsidArrayResponse = null;
	  try {
		  Echo echoInputType = new Echo();
		  LsidType id1 = new LsidType("urn:lsid:rproteomics.cabig.duhs.duke.edu:scanfeatures:5077304f-af23-46fb-b69b-de67881597c8");
		  LsidType id2 = new LsidType("urn:lsid:rproteomics.cabig.duhs.duke.edu:scanfeatures:9d694d22-f049-43ac-b3dd-c94f1df544c4");
		  LsidType[] lsidArray = new LsidType[] {id1} ;
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
	}*/
	

	public String callService() throws Exception {
		String result = null;
		try {
			CQLQueryType query = new CQLQueryType();
            int windowSize = 1023;
			WindowType windowType = new WindowType(windowSize);
			PercentileType percentileType = new PercentileType(75);
			WorkFlowInputType input = new WorkFlowInputType();
			input.setQueryType(query);
			input.setWindowType(windowType);
			input.setPercentileType(percentileType);
			Call call = createCall();
			WorkFlowOutputType output = (WorkFlowOutputType) call.invoke(new Object[] {input});
			
		} catch (Exception e) {
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
	  QName workFlowInputQName = new QName(RPROT_DATA_NAMESPACE, "WorkFlowInputType");
	  QName workFlowOuputQName = new QName(RPROT_DATA_NAMESPACE, "WorkFlowOutputType");

	  Service service = new Service();
	  Call call = (Call)service.createCall();
	  System.out.println("Calling : "+ urlString);
	  call.setTargetEndpointAddress(new java.net.URL(urlString));

	  call.setOperationName("startWorkFlow");
	  
	  call.addParameter("parameters", workFlowInputQName, ParameterMode.IN);
	  call.setReturnType(workFlowOuputQName, EchoResponse.class);

	  register(call, WorkFlowInputType.class, workFlowInputQName);
	  register(call, WorkFlowOutputType.class, workFlowOuputQName);
	  return call;
	}

	protected void register(Call call, Class klass, QName qname) {
	  System.out.println("Registering: "+ qname);
	  call.registerTypeMapping(klass,qname,BeanSerializerFactory.class,
				   BeanDeserializerFactory.class);
	}

}
