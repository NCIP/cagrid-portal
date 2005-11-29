package gov.nih.nci.cagrid.gums.ifs;

import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.common.ca.KeyUtil;
import gov.nih.nci.cagrid.gums.ifs.bean.ProxyLifetime;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import junit.framework.TestCase;

import org.globus.gsi.GlobusCredential;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class ProxyTest extends TestCase {
	public static String RESOURCES_DIR = "resources" + File.separator
			+ "ca-test";
	
	private String identityToSubject(String identity){
		String s = identity.substring(1);
		return s.replace('/',',');
	}

	public void testCreateProxy() {
		try {
				String certLocation = RESOURCES_DIR + File.separator
						+ "gums-cert.pem";
				String keyLocation = RESOURCES_DIR + File.separator
						+ "gums-key.pem";
		
				ProxyLifetime lifetime = new ProxyLifetime();
				lifetime.setHours(2);
				lifetime.setMinutes(0);
				lifetime.setSeconds(0);
				PrivateKey key = KeyUtil.loadPrivateKey(keyLocation, null);
				assertNotNull(key);
				KeyPair pair = KeyUtil.generateRSAKeyPair512();
				assertNotNull(pair);
				PublicKey proxyPublicKey = pair.getPublic();
				assertNotNull(proxyPublicKey);
				X509Certificate cert = CertUtil.loadCertificate(certLocation);
				assertNotNull(cert);
				X509Certificate[] certs = ProxyUtil.createProxyCertificate(cert,key, proxyPublicKey,lifetime);
				assertNotNull(certs);
				assertEquals(2,certs.length);
				GlobusCredential cred = new GlobusCredential(pair.getPrivate(),certs);
				assertNotNull(cred);
				long timeLeft = cred.getTimeLeft();
				assertEquals(cert.getSubjectDN().toString(),identityToSubject(cred.getIdentity()));
				assertEquals(cred.getIssuer(),identityToSubject(cred.getIdentity()));
				
				long okMax = lifetime.getHours()*60*60;
				//Allow some Buffer
				long okMin = okMax-3;
				
				if((okMin>timeLeft)||(timeLeft>okMax)){
					assertTrue(false);
				}
				
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
	
	public void testInvalidProxyTimeToGreat() {
		try {
				String certLocation = RESOURCES_DIR + File.separator
						+ "gums-cert.pem";
				String keyLocation = RESOURCES_DIR + File.separator
						+ "gums-key.pem";
				ProxyLifetime lifetime = new ProxyLifetime();
				lifetime.setHours(50000);
				lifetime.setMinutes(0);
				lifetime.setSeconds(0);
				PrivateKey key = KeyUtil.loadPrivateKey(keyLocation, null);
				assertNotNull(key);
				KeyPair pair = KeyUtil.generateRSAKeyPair512();
				assertNotNull(pair);
				PublicKey proxyPublicKey = pair.getPublic();
				assertNotNull(proxyPublicKey);
				X509Certificate cert = CertUtil.loadCertificate(certLocation);
				assertNotNull(cert);
			    ProxyUtil.createProxyCertificate(cert,key, proxyPublicKey,lifetime);
				assertTrue(false);
		} catch (Exception e) {
			assertEquals("Cannot create a proxy that expires after issuing certificate.",e.getMessage());	
		}
	}

}
