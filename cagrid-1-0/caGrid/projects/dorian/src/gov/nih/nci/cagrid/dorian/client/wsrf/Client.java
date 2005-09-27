package gov.nih.nci.cagrid.gums.client.wsrf;

import gov.nih.nci.cagrid.gums.bean.AttributeDescriptor;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: Client.java,v 1.1 2005-09-27 18:31:18 langella Exp $
 */
public class Client {

	public static void main(String[] args) {
		try {
			//String serviceURI = "https://localhost:8443/wsrf/services/cagrid/gums";
			String serviceURI = "http://localhost:8080/wsrf/services/cagrid/gums";
		
			GUMSRegistrationClient client = new GUMSRegistrationClient(serviceURI);
			
			AttributeDescriptor[] des = client.getRequiredUserAttributes();
			 for(int i=0; i<des.length; i++){
				System.out.println(des[i].getNamespace()+" - "+des[i].getName());
			 }
		
			 
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}