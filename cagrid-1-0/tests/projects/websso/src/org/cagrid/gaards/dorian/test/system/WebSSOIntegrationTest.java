package org.cagrid.gaards.dorian.test.system;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.xml.sax.SAXException;
import junit.framework.TestCase;
import sun.net.www.protocol.https.HttpsURLConnectionImpl;
import com.meterware.httpunit.*;
import com.meterware.httpunit.controls.SelectionFormControl;


public class WebSSOIntegrationTest extends TestCase{

	String webssoClient1URL = "http://localhost:44910/webssoclientacegiexample-1.3-dev/protected";
	String webssoClient2URL = "http://localhost:45019/webssoclientjasigexample-1.3-dev/protected";
	
	@Override
	protected void setUp() throws Exception {
		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnectionImpl.setDefaultHostnameVerifier(hostnameVerifier);
		
		super.setUp();	
	}
   
   public void testMultipleApplicationsLoginAndLogout() throws Exception {
	  //login 1
	  WebConversation conversation = new WebConversation();
		WebRequest request = new GetMethodWebRequest(webssoClient1URL);
		WebResponse response = tryGetResponse(conversation, request);
		request = applicationLogin(conversation, response);
		response = tryGetResponse(conversation, request);
		assertUserInformation(response);
		
		//login 2	
		WebRequest request2 = new GetMethodWebRequest(webssoClient2URL);//login into application2
		WebResponse response2 = tryGetResponse(conversation, request2);
		assertUserInformation(response2);
		
		//logout	
	    WebLink link = response.getLinkWith("logout");
		link.click();  //logout of the application            
		WebRequest newRequest = new GetMethodWebRequest(webssoClient1URL);
	    WebResponse outputResponse= tryGetResponse(conversation, newRequest);
		WebTable usersData = outputResponse.getTableStartingWith("Single Sign On User Data");		
		
		assertNull("users data must be null ",usersData); //return null for user data (ie) logged out of second application
  }

  private WebRequest applicationLogin(WebConversation conversation,
			WebResponse response) throws SAXException, Exception {
		WebRequest request;
		WebForm loginForm = response.getForms()[0];
		request = loginForm.getRequest();
		
		SelectionFormControl asDorianOptions=(SelectionFormControl)response.getElementWithID("dorianName");
		request.setParameter("dorianName", asDorianOptions.getOptionValues()[1]);
		
		response = tryGetResponse(conversation, request);
		loginForm = response.getForms()[0];
		request = loginForm.getRequest();
		
		SelectionFormControl asUrlOptions=(SelectionFormControl)response.getElementWithID("authenticationServiceURL");
		String[] urls = asUrlOptions.getOptionValues();
		request.setParameter("authenticationServiceURL",urls[1]);

		response = tryGetResponse(conversation, request);
		loginForm = response.getForms()[0];
		request = loginForm.getRequest();

		SelectionFormControl asProfileOptions=(SelectionFormControl)response.getElementWithID("authenticationServiceProfile");
		HttpUnitOptions.setScriptingEnabled(false);
		String[] profiles = asProfileOptions.getOptionValues();
		request.setParameter("authenticationServiceProfile",profiles[1]);
		
		HTMLElement serviceProfileType=response.getElementWithID("serviceProfileType");
		serviceProfileType.setAttribute("value", profiles[1]);
		
		request.setParameter("username", "jdoe1");
		request.setParameter("password", "K00lM0N$$1");
		return request;
  }
  
  private void assertUserInformation(WebResponse response) throws SAXException {
	  	WebTable usersData = response.getTableStartingWith("Single Sign On User Data");
		String[][] tableRows = usersData.asText();
		String userGridInformation = tableRows[1][0];
	
		assertTrue(userGridInformation
				.contains("/C=US/O=abc/OU=xyz/OU=caGrid/OU=Dorian/CN=jdoe1"));
		assertTrue(userGridInformation.contains("John1"));
		assertTrue(userGridInformation.contains("Doe1"));
		assertTrue(userGridInformation
				.contains("/wsrf/services/cagrid/DelegatedCredential"));
		assertTrue(userGridInformation.contains("jdoe1@cagrid.org"));
  }
  
	/**
	 * try getting a response for the given Conversation and Request
	 */
	public WebResponse tryGetResponse(WebConversation conversation,WebRequest request) throws Exception {
		WebResponse response=null;
		try {
			response = conversation.getResponse( request );
		} catch (HttpNotFoundException nfe) {
			System.err.println("The URL '"+request.getURL()+"' is not active any more");
			throw nfe;
		}
		return response;
	}
}


