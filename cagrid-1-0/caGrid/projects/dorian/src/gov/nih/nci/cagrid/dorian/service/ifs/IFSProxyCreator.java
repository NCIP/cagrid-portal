package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.gridca.common.ProxyCreator;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.globus.gsi.GSIConstants;
import org.globus.gsi.X509ExtensionSet;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IFSProxyCreator {

	public static X509Certificate[] createImpersonationProxyCertificate(X509Certificate cert, PrivateKey privateKey,
		PublicKey proxyPublicKey, ProxyLifetime lifetime, int delegationPathLength) throws GeneralSecurityException {
		return ProxyCreator.createImpersonationProxyCertificate(cert, privateKey, proxyPublicKey, lifetime.getHours(),
			lifetime.getMinutes(), lifetime.getSeconds(), delegationPathLength);
	}


	public static X509Certificate[] createImpersonationProxyCertificate(X509Certificate[] certs, PrivateKey privateKey,
		PublicKey proxyPublicKey, ProxyLifetime lifetime, int delegationPathLength) throws GeneralSecurityException {
		return ProxyCreator.createImpersonationProxyCertificate(certs, privateKey, proxyPublicKey, lifetime.getHours(),
			lifetime.getMinutes(), lifetime.getSeconds(), delegationPathLength);
	}

}
