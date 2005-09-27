package gov.nih.nci.cagrid.gums.ca.test;

import gov.nih.nci.cagrid.gums.ca.CertUtil;
import gov.nih.nci.cagrid.gums.ca.KeyUtil;
import gov.nih.nci.cagrid.gums.ca.ProxyUtil;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import junit.framework.TestCase;

import org.globus.gsi.GlobusCredential;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: ProxyTest.java,v 1.1 2005-09-27 18:31:18 langella Exp $
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
				int hours = 2;
				PrivateKey key = KeyUtil.loadPrivateKey(keyLocation, null);
				assertNotNull(key);
				KeyPair pair = KeyUtil.generateRSAKeyPair512();
				assertNotNull(pair);
				PublicKey proxyPublicKey = pair.getPublic();
				assertNotNull(proxyPublicKey);
				X509Certificate cert = CertUtil.loadCertificate(certLocation);
				assertNotNull(cert);
				X509Certificate[] certs = ProxyUtil.createProxyCertificate(cert,key, proxyPublicKey,hours);
				assertNotNull(certs);
				assertEquals(2,certs.length);
				GlobusCredential cred = new GlobusCredential(pair.getPrivate(),certs);
				assertNotNull(cred);
				long timeLeft = cred.getTimeLeft();
				assertEquals(cert.getSubjectDN().toString(),identityToSubject(cred.getIdentity()));
				assertEquals(cred.getIssuer(),identityToSubject(cred.getIdentity()));
				
				long okMax = hours*60*60;
				//Allow some Buffer
				long okMin = okMax-3;
				
				if((okMin>timeLeft)||(timeLeft>okMax)){
					assertTrue(false);
				}
				
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	public void testInvalidProxyTimeToGreat() {
		try {
				String certLocation = RESOURCES_DIR + File.separator
						+ "gums-cert.pem";
				String keyLocation = RESOURCES_DIR + File.separator
						+ "gums-key.pem";
				int hours = 50000;
				PrivateKey key = KeyUtil.loadPrivateKey(keyLocation, null);
				assertNotNull(key);
				KeyPair pair = KeyUtil.generateRSAKeyPair512();
				assertNotNull(pair);
				PublicKey proxyPublicKey = pair.getPublic();
				assertNotNull(proxyPublicKey);
				X509Certificate cert = CertUtil.loadCertificate(certLocation);
				assertNotNull(cert);
				X509Certificate[] certs = ProxyUtil.createProxyCertificate(cert,key, proxyPublicKey,hours);
				assertTrue(false);
		} catch (Exception e) {
			assertEquals("Cannot create a proxy that expires after issuing certificate.",e.getMessage());	
		}
	}

}
