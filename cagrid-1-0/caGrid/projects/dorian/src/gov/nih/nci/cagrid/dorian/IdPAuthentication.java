package gov.nih.nci.cagrid.gums;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.GUMSFault;
import gov.nih.nci.cagrid.gums.idp.bean.InvalidLoginFault;

import org.opensaml.SAMLAssertion;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public interface IdPAuthentication {
	public SAMLAssertion authenticate() throws GUMSFault,GUMSInternalFault, InvalidLoginFault;
	
}
