/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ServiceErrorInterpretorTest extends TestCase {

	private static final String FQP_ERROR = "AxisFault\n"
			+ "faultCode: {http://schemas.xmlsoap.org/soap/envelope/}Server.generalException\n"
			+ "faultSubcode:\n"
			+ "faultString: Problem executing query: gov.nih.nci.cagrid.fqp.processor.exceptions.RemoteDataServiceException: Problem query data service at URL:http://141.161.25.20:8080/wsrf/services/cagrid/GridPIR\n"
			+ "faultActor:\n"
			+ "faultNode:\n"
			+ "faultDetail:\n"
			+ "{http://fqp.cagrid.nci.nih.gov/FederatedQueryProcessor/types}FederatedQueryProcessingFault:2008-06-02T17:11:03.879Zhttp://localhost:8085/wsrf5/services/cagrid/FederatedQueryResultsde2a39b0-30c6-11dd-b9ca-d8cc30f0acd62008-06-02T17:11:03.879Z\n"
			+ "at gov.nih.nci.cagrid.fqp.results.service.FederatedQueryResultsImpl.getResults(FederatedQueryResultsImpl.java:32)\n"
			+ "at g";
	


	private static final String AUTHENTICATION_FAILED_ERROR = "AxisFault\n"
			+ "faultCode: {http://schemas.xmlsoap.org/soap/envelope/}Server.userException\n"
			+ "faultSubcode:\n"
			+ "faultString: GSSException: Defective credential detected [Caused by: Proxy file (/tmp/x509up_u3533) not found.]\n"
			+ "faultActor:\n"
			+ "faultNode:\n"
			+ "faultDetail:\n"
			+ "{http://xml.apache.org/axis/}stackTrace:Defective credential detected. Caused by org.globus.gsi.GlobusCredentialException: Proxy file (/tmp/x509up_u3533) not found.\n"
			+ "at org.globus.gsi.GlobusCredential.<init>(GlobusCredential.java:102)\n"
			+ "at org.globus.gsi.GlobusCredential.reloadDefaultCredential(GlobusCredential.java:544)\n"
			+ "at org.globus.gsi.GlobusCredential.getDefaultCredential(GlobusCredential.java:529)\n"
			+ "at org.globus.gsi.gssapi.GlobusGSSManagerImpl.createCredential(GlobusGSSManagerImpl.java:125)\n"
			+ "at org.globus.gsi.gssapi.GlobusGSSManagerImpl.createCredential(GlobusGSSManagerImpl.java:66)\n"
			+ "at org.globus.gsi.gssapi.GlobusGSSManagerImpl.createContext(GlobusGSSManagerImpl.java:263)\n"
			+ "at org.globus.axis.transport.SSLContextHelper.init(SSLContextHelper.java:112)\n"
			+ "at org.globus.axis.transport.SSLContextHelper.<init>(SSLContextHelper.java:60)\n";
	
	
	
	private static final String AUTHENTICATION_FAILED_MESSAGE = "You must be logged in in order to query this service.";

	private static final String MAX_BYTES_EXCEEDED_ERROR = "AxisFault\n"
			+ "faultCode: {http://schemas.xmlsoap.org/soap/envelope/}Server.userException\n"
			+ "faultSubcode:\n"
			+ "faultString: java.io.IOException: maxBytes (3000000) exceeded\n"
			+ "faultActor:\n"
			+ "faultNode:\n"
			+ "faultDetail:\n"
			+ "{http://xml.apache.org/axis/}stackTrace:java.io.IOException: maxBytes (3000000) exceeded\n"
			+ "at org.apache.axis.transport.http.SocketInputStream.assertBytesLimit(SocketInputStream.java:96)\n"
			+ "at org.apache.axis.transport.http.SocketInputStream.read(SocketInputStream.java:85)\n"
			+ "at org.apache.xerces.impl.XMLEntityManager$RewindableInputStream.read(Unknown Source)\n"
			+ "at org.apache.xerces.impl.io.UTF8Reader.read(Unknown Source)\n"
			+ "at org.apache.xerces.impl.XMLEntityScanner.load(Unknown Source)\n"
			+ "at org.apache.xerces.impl.XMLEntityScanner.scanName(Unknown Source)\n";
	
	private static final String MAX_BYTES_EXCEEDED_MESSAGE = "The service you are querying has returned too much data.";

	private static final String UNKNOWN_ERROR = "AxisFault\n"
			+ "faultCode: {http://schemas.xmlsoap.org/soap/envelope/}Server.generalException\n"
			+ "faultSubcode:\n"
			+ "faultString:\n"
			+ "faultActor:\n"
			+ "faultNode:\n"
			+ "faultDetail:\n"
			+ "{http://gov.nih.nci.cagrid.data/DataServiceExceptions}QueryProcessingException:2008-06-03T10:52:09.764Zhttp://cabio-gridservice.nci.nih.gov:80/wsrf-cabio/services/cagrid/CaBIOSvcQueryProcessingException -- No field namesdf found for class gov.nih.nci.cabio.domain.Pathway2008-06-03T10:52:09.764Z\n"
			+ "at gov.nih.nci.cagrid.data.service.DataServiceImpl.query(DataServiceImpl.java:42)\n"
			+ "at gov.nih.nci.cagrid.data.service.globus.DataServiceProviderImpl.query(DataServiceProviderImpl.java:24)\n"
			+ "at sun.reflect.GeneratedMethodAccessor4621.invoke(Unknown Source)\n"
			+ "at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)\n"
			+ "at java.lang.reflect.Method.invoke(Method.java:585)\n"
			+ "at org.apache.axis.providers.java.RPCProvider.invokeMethod(RPCProvider.java:384)\n";
	
	private static final String UNKNOWN_MESSAGE = "The service that you are attempting to query encountered an error.";

	private List<ServiceErrorInterpretor> interpretors = new ArrayList<ServiceErrorInterpretor>();

	/**
	 * 
	 */
	public ServiceErrorInterpretorTest() {

	}

	/**
	 * @param name
	 */
	public ServiceErrorInterpretorTest(String name) {
		super(name);
	}

	public void setUp() throws Exception {
		super.setUp();
		FQPServiceErrorInterpretor fqp = new FQPServiceErrorInterpretor();
		fqp.setPattern("Problem query data service at URL:");
		fqp
				.setMessage("One of the data services ({0}) involved in your federated query has encountered and error.");
		fqp.afterPropertiesSet();
		interpretors.add(fqp);
		StringMatchServiceErrorInterpretor sm = new StringMatchServiceErrorInterpretor();
		sm
				.setPattern(".*faultString: GSSException: Defective credential detected \\[Caused by: Proxy file \\(.*\\) not found\\.\\].*");
		sm.setMessage(AUTHENTICATION_FAILED_MESSAGE);
		sm.afterPropertiesSet();
		interpretors.add(sm);
		sm = new StringMatchServiceErrorInterpretor();
		sm.setPattern(".*maxBytes \\(\\d+\\) exceeded.*");
		sm
				.setMessage(MAX_BYTES_EXCEEDED_MESSAGE);
		sm.afterPropertiesSet();
		interpretors.add(sm);
		sm = new StringMatchServiceErrorInterpretor();
		sm.setPattern(".*");
		sm
				.setMessage(UNKNOWN_MESSAGE);
		sm.afterPropertiesSet();
		interpretors.add(sm);
	}
	
	public void testFQPError(){
		assertEquals(getMessage(FQP_ERROR), "One of the data services (http://141.161.25.20:8080/wsrf/services/cagrid/GridPIR) involved in your federated query has encountered and error.");
	}

	public void testAuthenticationFailed() {
		assertEquals(getMessage(AUTHENTICATION_FAILED_ERROR), AUTHENTICATION_FAILED_MESSAGE);
	}

	public void testMaxBytesExceeded() {
		assertEquals(getMessage(MAX_BYTES_EXCEEDED_ERROR), MAX_BYTES_EXCEEDED_MESSAGE);
	}

	public void testUnknownError() {
		assertEquals(getMessage(UNKNOWN_ERROR), UNKNOWN_MESSAGE);
	}

	private String getMessage(String errorMessage) {
		String message = null;
		for (ServiceErrorInterpretor interpretor : interpretors) {
			message = interpretor.getErrorMessage(errorMessage);
			if (message != null) {
				break;
			}
		}
		return message;
	}

}
