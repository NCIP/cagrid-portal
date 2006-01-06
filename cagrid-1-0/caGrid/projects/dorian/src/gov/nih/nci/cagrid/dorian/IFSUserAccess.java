package gov.nih.nci.cagrid.dorian;

import gov.nih.nci.cagrid.dorian.bean.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.common.DorianFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidProxyFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.dorian.ifs.bean.UserPolicyFault;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import org.globus.gsi.GlobusCredential;

public interface IFSUserAccess {

	public GlobusCredential createProxy(SAMLAssertion saml,
			ProxyLifetime lifetime) throws DorianFault, DorianInternalFault,
			InvalidAssertionFault, InvalidProxyFault, UserPolicyFault,
			PermissionDeniedFault;

}
