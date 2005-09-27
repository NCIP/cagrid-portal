package gov.nih.nci.cagrid.security.commstyle;


import org.apache.axis.client.Stub;
import org.globus.wsrf.security.Constants;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: AnonymousSecureConversationWithEncryption.java,v 1.1 2005-09-27 20:09:50 langella Exp $
 */
public class AnonymousSecureConversationWithEncryption implements
		CommunicationStyle {
	public AnonymousSecureConversationWithEncryption() {

	}

	public void configure(Stub stub) throws CommunicationStyleException {
		try {
			stub._setProperty(Constants.GSI_SEC_CONV, Constants.ENCRYPTION);
			stub._setProperty(org.globus.wsrf.security.Constants.GSI_ANONYMOUS,
					Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommunicationStyleException(e.getMessage());
		}
	}

}
