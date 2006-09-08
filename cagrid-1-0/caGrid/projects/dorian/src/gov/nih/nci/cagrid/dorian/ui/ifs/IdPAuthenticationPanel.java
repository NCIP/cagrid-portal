package gov.nih.nci.cagrid.dorian.ui.ifs;

import gov.nih.nci.cagrid.dorian.ui.IdPConf;
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
	
	private IdPConf idp;
	public IdPAuthenticationPanel(IdPConf conf){
		this.idp = conf;
		
	}
	
	public IdPConf getIdPInfo(){
		return idp;
	}
	
	public abstract SAMLAssertion authenticate() throws Exception;

}
