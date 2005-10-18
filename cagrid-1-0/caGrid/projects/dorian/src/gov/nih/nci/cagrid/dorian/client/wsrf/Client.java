package gov.nih.nci.cagrid.gums.client.wsrf;

import gov.nih.nci.cagrid.gums.ifs.bean.UserApplication;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: Client.java,v 1.4 2005-10-18 23:23:48 langella Exp $
 */
public class Client {

	public static void main(String[] args) {
		try {
			//String serviceURI = "https://localhost:8443/wsrf/services/cagrid/gums";
			String serviceURI = "http://localhost:8080/wsrf/services/cagrid/gums";
			GUMSRegistrationClient client = new GUMSRegistrationClient(serviceURI);	
			System.out.println("OUT"+client.registerUser(new UserApplication()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}