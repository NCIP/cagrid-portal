
package gov.nih.nci.cagrid.workflow.handlers;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.globus.axis.gsi.GSIConstants;
import org.globus.axis.util.Util;
import org.globus.wsrf.impl.security.util.AuthUtil;

/**
 * @author madduri
 *
 */
public class SimpleGlobusAuthorizationHandler extends BasicHandler {
	static {
		Util.registerTransport();
	}
	
	public void invoke(MessageContext msgContext) throws AxisFault {
		// TODO Auto-generated method stub
		 msgContext.setProperty(
                 GSIConstants.GSI_AUTHORIZATION,
                 org.globus.gsi.gssapi.auth.NoAuthorization.getInstance());
		 msgContext.setProperty(GSIConstants.GSI_TRANSPORT,
                 GSIConstants.ENCRYPTION);
		/* msgContext.setProperty(org.globus.wsrf.security.Constants.GSI_SEC_CONV,
					org.globus.wsrf.security.Constants.ENCRYPTION);*/

	}

}
