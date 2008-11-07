package gov.nih.nci.cagrid.fqp.common;

import gov.nih.nci.cagrid.fqp.results.common.FederatedQueryResultsConstants;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;
import javax.xml.namespace.QName;

import org.globus.gsi.jaas.JaasSubject;
import org.globus.security.gridmap.GridMap;
import org.globus.wsrf.impl.security.authorization.Authorization;
import org.globus.wsrf.impl.security.descriptor.GSISecureConvAuthMethod;
import org.globus.wsrf.impl.security.descriptor.GSISecureMsgAuthMethod;
import org.globus.wsrf.impl.security.descriptor.GSITransportAuthMethod;
import org.globus.wsrf.impl.security.descriptor.NoneAuthMethod;
import org.globus.wsrf.impl.security.descriptor.ResourceSecurityDescriptor;
import org.globus.wsrf.impl.security.descriptor.SecurityDescriptorException;
import org.globus.wsrf.security.SecurityException;
import org.globus.wsrf.security.SecurityManager;


/**
 * @author oster
 * 
 */
public class SecurityUtils {

	/**
	 * Create a new ResourceSecurityDescriptor using the current Subject as
	 * owner and caller as only GridMap-authorized identity. If there is no
	 * caller (no security being used), then null is returned.
	 * 
	 * @return the created ResourceSecurityDescriptor, or null
	 * @throws SecurityException
	 * @throws SecurityDescriptorException
	 */
	public static ResourceSecurityDescriptor createResultsResourceSecurityDescriptor() 
        throws SecurityDescriptorException {
		ResourceSecurityDescriptor desc = null;

		String peer = SecurityManager.getManager().getCaller();
		if (peer != null) {
			desc = new ResourceSecurityDescriptor();

			Subject subject = JaasSubject.getCurrentSubject();
			desc.setSubject(subject);

			GridMap gridmap = new GridMap();
			// just map the user to the system user who is running the container
			gridmap.map(peer, System.getProperty("user.name"));
			desc.setGridMap(gridmap);
			desc.setAuthz(Authorization.AUTHZ_GRIDMAP);

			// add in any secure authentication method
			List<Object> authMethods = new ArrayList<Object>();
			authMethods.add(GSISecureMsgAuthMethod.BOTH);
			authMethods.add(GSITransportAuthMethod.BOTH);
			authMethods.add(GSISecureConvAuthMethod.BOTH);

			List<Object> openAuthMethods = new ArrayList<Object>();
			openAuthMethods.add(NoneAuthMethod.getInstance());
			desc.setMethodAuthMethods(new QName(FederatedQueryResultsConstants.SERVICE_NS, "getServiceSecurityMetadata"),
				openAuthMethods);
			desc.setAuthMethods(authMethods);
		}
		return desc;
	}
}
