package gov.nih.nci.cagrid.gums.ifs;

import org.opensaml.SAMLAssertion;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IFS {
	
	public IFS(){
		
	}
	
	public void createProxy(SAMLAssertion saml){
		//Make sure the assertion is trusted
		//If the user does not exist, add them
		//Run the policy
		//Check to see if authorized
		//create the proxy
	}
	

}
