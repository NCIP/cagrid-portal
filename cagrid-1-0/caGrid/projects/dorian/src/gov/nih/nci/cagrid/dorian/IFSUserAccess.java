package gov.nih.nci.cagrid.gums;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.gums.common.GUMSFault;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidAssertionFault;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidProxyFault;
import gov.nih.nci.cagrid.gums.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.gums.ifs.bean.UserPolicyFault;

import org.globus.gsi.GlobusCredential;
import org.opensaml.SAMLAssertion;

public interface IFSUserAccess {

	public GlobusCredential createProxy(SAMLAssertion saml,
			ProxyLifetime lifetime) throws GUMSFault, GUMSInternalFault,
			InvalidAssertionFault, InvalidProxyFault, UserPolicyFault,
			PermissionDeniedFault;

}
