package gov.nih.nci.cagrid.gums.client.wsrf;

import gov.nih.nci.cagrid.gums.ifs.bean.UserApplication;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
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