package gov.nih.nci.cagrid.gums.ca;

import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.globus.gsi.GlobusCredential;
import org.globus.util.ConfigUtil;
/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GumsFutureProxy {

	public static void main(String args[]) {
		try {
			// CLIENT SIDE

			// Key Pair should be generated on the client side

			KeyPair pair = KeyUtil.generateRSAKeyPair512();
			PublicKey clientProxyPublicKey = pair.getPublic();

			// SERVER SIDE

			// Server side needs user id/password and public key to generate
			// proxy
			// for user.

			// Load user certificate
			X509Certificate cert = gov.nih.nci.cagrid.gums.ca.CertUtil.loadCertificate("c:/certificates/langella-cert.pem");
			PrivateKey key = KeyUtil.loadPrivateKey("c:/certificates/langella-key.pem", "gomets123");
			X509Certificate[] certs = ProxyUtil
					.createProxyCertificate(new X509Certificate[] { cert },
							key, clientProxyPublicKey,36);
			
			for(int i=0; i<certs.length; i++){
				System.out.println("*~*~*~*~* Certificate *~*~*~*~*~*");
				System.out.println("Subject: "+certs[i].getSubjectDN());
				System.out.println("Issuer: "+certs[i].getIssuerDN());
				System.out.println(certs[i].getNotAfter());
			}

			// CLIENT SIDE

			// Send certs to client and have client finish creating the proxy;
			GlobusCredential cred = new GlobusCredential(pair.getPrivate(),
					certs);
			FileOutputStream fos = new FileOutputStream(ConfigUtil
					.discoverProxyLocation());
			cred.save(fos);
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
