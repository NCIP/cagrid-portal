package gov.nih.nci.cagrid.dorian.ui.ifs;

import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import javax.swing.JPanel;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public abstract class IdPAuthenticationPanel extends JPanel{
	
	private String uri;
	public IdPAuthenticationPanel(String uri){
		this.uri = uri;
		
	}
	
	
	
	public String getURI() {
		return uri;
	}



	public abstract SAMLAssertion authenticate() throws Exception;

}
