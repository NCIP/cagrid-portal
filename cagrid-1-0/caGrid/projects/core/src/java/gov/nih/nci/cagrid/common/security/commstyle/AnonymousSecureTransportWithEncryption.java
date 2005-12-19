package gov.nih.nci.cagrid.common.security.commstyle;


import org.apache.axis.client.Stub;
import org.globus.axis.util.Util;
import org.globus.wsrf.security.Constants;
/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class AnonymousSecureTransportWithEncryption implements
		CommunicationStyle {
	public AnonymousSecureTransportWithEncryption() {

	}

	public void configure(Stub stub) throws CommunicationStyleException {
		try {
			Util.registerTransport();
			stub._setProperty(Constants.GSI_TRANSPORT, Constants.ENCRYPTION);
			stub._setProperty(org.globus.wsrf.security.Constants.GSI_ANONYMOUS,
					Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommunicationStyleException(e.getMessage());
		}
	}

}
