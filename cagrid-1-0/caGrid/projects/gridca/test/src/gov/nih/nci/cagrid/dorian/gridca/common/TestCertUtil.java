package gov.nih.nci.cagrid.dorian.gridca.common;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;

import junit.framework.TestCase;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestCertUtil extends TestCase {

	public void testCreateCertificateSimpleCARoot() {
		try {
			InputStream certLocation = TestCase.class.getResourceAsStream(Constants.SIMPLECA_CACERT);
			InputStream keyLocation = TestCase.class.getResourceAsStream(Constants.SIMPLECA_CAKEY);

			String keyPassword = "gomets123";
			X509Certificate[] certs = createCertificateSpecifyRootCA(certLocation, keyLocation, keyPassword);
			assertEquals(2, certs.length);
			String rootSub = "O=caBIG,OU=Ohio State University,OU=Department of Biomedical Informatics,CN=caBIG Certificate Authority";
			String issuedSub = "O=caBIG,OU=Ohio State University,OU=Department of Biomedical Informatics,CN=John Doe";
			X509Certificate rootCert = certs[1];
			X509Certificate issuedCert = certs[0];
			checkCert(rootCert, rootSub, rootSub);
			checkCert(issuedCert, rootSub, issuedSub);
			checkWriteReadCertificate(rootCert);
			checkWriteReadCertificate(issuedCert);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testCreateCertificateDorianCARoot() {
		try {
			InputStream certLocation = TestCase.class.getResourceAsStream(Constants.BMI_CACERT);
			InputStream keyLocation = TestCase.class.getResourceAsStream(Constants.BMI_CAKEY);

			String keyPassword = "gomets123";
			X509Certificate[] certs = createCertificateSpecifyRootCA(certLocation, keyLocation, keyPassword);
			assertEquals(2, certs.length);
			String rootSub = "O=Ohio State University,OU=BMI,OU=MSCL,CN=BMI Certificate Authority";
			String issuedSub = "O=Ohio State University,OU=BMI,OU=MSCL,CN=John Doe";
			X509Certificate rootCert = certs[1];
			X509Certificate issuedCert = certs[0];
			checkCert(rootCert, rootSub, rootSub);
			checkCert(issuedCert, rootSub, issuedSub);
			checkWriteReadCertificate(rootCert);
			checkWriteReadCertificate(issuedCert);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testCreateCertificateNewDorianCARootCert() {
		try {
			KeyPair rootPair = KeyUtil.generateRSAKeyPair1024();
			assertNotNull(rootPair);
			String rootSub = "O=Ohio State University,OU=BMI,OU=MSCL,CN=Temp Certificate Authority";
			X509Name rootSubject = new X509Name(rootSub);
			X509Certificate root = CertUtil.generateCACertificate(rootSubject, new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis() + 500000000), rootPair);
			assertNotNull(root);
			String certLocation = "temp-cacert.pem";
			String keyLocation = "temp-cakey.pem";
			String keyPassword = "gomets123";
			KeyUtil.writePrivateKey(rootPair.getPrivate(), new File(keyLocation), keyPassword);
			CertUtil.writeCertificate(root, new File(certLocation));

			X509Certificate[] certs = createCertificateSpecifyRootCA(certLocation, keyLocation, keyPassword);
			File f1 = new File(certLocation);
			f1.delete();
			File f2 = new File(keyLocation);
			f2.delete();
			assertEquals(2, certs.length);
			String issuedSub = "O=Ohio State University,OU=BMI,OU=MSCL,CN=John Doe";
			X509Certificate rootCert = certs[1];
			X509Certificate issuedCert = certs[0];
			checkCert(rootCert, rootSub, rootSub);
			checkCert(issuedCert, rootSub, issuedSub);
			checkWriteReadCertificate(rootCert);
			checkWriteReadCertificate(issuedCert);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testCreateCertificateExpiredRootCert() {
		try {
			KeyPair rootPair = KeyUtil.generateRSAKeyPair1024();
			assertNotNull(rootPair);
			String rootSub = "O=Ohio State University,OU=BMI,OU=MSCL,CN=Temp Certificate Authority";
			X509Name rootSubject = new X509Name(rootSub);
			X509Certificate root = CertUtil.generateCACertificate(rootSubject, new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()), rootPair);
			Thread.sleep(10);
			assertNotNull(root);
			String certLocation = "temp-cacert.pem";
			String keyLocation = "temp-cakey.pem";
			String keyPassword = "gomets123";
			KeyUtil.writePrivateKey(rootPair.getPrivate(), new File(keyLocation), keyPassword);
			CertUtil.writeCertificate(root, new File(certLocation));
			checkCert(root, rootSub, rootSub);
			checkWriteReadCertificate(root);
			try {
				createCertificateSpecifyRootCA(certLocation, keyLocation, keyPassword);
				assertTrue(false);
			} catch (Exception e) {
				assertEquals("Root Certificate Expired.", e.getMessage());
			}

			File f1 = new File(certLocation);
			f1.delete();
			File f2 = new File(keyLocation);
			f2.delete();

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testCreateCertificateNotYetValidRootCert() {
		try {
			KeyPair rootPair = KeyUtil.generateRSAKeyPair1024();
			assertNotNull(rootPair);
			String rootSub = "O=Ohio State University,OU=BMI,OU=MSCL,CN=Temp Certificate Authority";
			X509Name rootSubject = new X509Name(rootSub);
			X509Certificate root = CertUtil.generateCACertificate(rootSubject, new Date(
				System.currentTimeMillis() + 50000), new Date(System.currentTimeMillis() + 500000), rootPair);
			assertNotNull(root);
			String certLocation = "temp-cacert.pem";
			String keyLocation = "temp-cakey.pem";
			String keyPassword = "gomets123";
			KeyUtil.writePrivateKey(rootPair.getPrivate(), new File(keyLocation), keyPassword);
			CertUtil.writeCertificate(root, new File(certLocation));
			checkCert(root, rootSub, rootSub);
			checkWriteReadCertificate(root);
			try {
				createCertificateSpecifyRootCA(certLocation, keyLocation, keyPassword);
				assertTrue(false);
			} catch (Exception e) {
				assertEquals("Root Certificate not yet valid.", e.getMessage());
			}

			File f1 = new File(certLocation);
			f1.delete();
			File f2 = new File(keyLocation);
			f2.delete();

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	private void checkWriteReadCertificate(X509Certificate cert) throws Exception {
		String temp = "temp.pem";
		CertUtil.writeCertificate(cert, new File(temp));
		X509Certificate in = CertUtil.loadCertificate(new File(temp));
		assertEquals(cert, in);
		File f = new File(temp);
		f.delete();
	}


	private void checkCert(X509Certificate cert, String issuer, String subject) {
		assertEquals(subject, cert.getSubjectDN().toString());
		assertEquals(issuer, cert.getIssuerDN().toString());
	}


	public X509Certificate[] createCertificateSpecifyRootCA(String certLocation, String keyLocation, String keyPassword)
		throws Exception {
		return createCertificateSpecifyRootCA(getFileInputStream(certLocation), getFileInputStream(keyLocation),
			keyPassword);

	}


	public X509Certificate[] createCertificateSpecifyRootCA(InputStream certLocation, InputStream keyLocation,
		String keyPassword) throws Exception {
		// Load a root certificate
		PrivateKey rootKey = KeyUtil.loadPrivateKey(keyLocation, keyPassword);
		assertNotNull(rootKey);
		X509Certificate rootCert = CertUtil.loadCertificate(certLocation);
		assertNotNull(rootCert);
		String rootSub = rootCert.getSubjectDN().toString();

		Date now = new Date(System.currentTimeMillis());

		if (now.after(rootCert.getNotAfter())) {
			throw new Exception("Root Certificate Expired.");
		}

		if (now.before(rootCert.getNotBefore())) {
			throw new Exception("Root Certificate not yet valid.");
		}

		// create the certification request
		KeyPair pair = KeyUtil.generateRSAKeyPair1024();
		assertNotNull(pair);
		int index = rootSub.lastIndexOf(",");
		String sub = rootSub.substring(0, index) + ",CN=John Doe";
		PKCS10CertificationRequest request = CertUtil.generateCertficateRequest(sub, pair);

		// validate the certification request
		if (!request.verify("BC")) {
			System.out.println("request failed to verify!");
			System.exit(1);
		}

		X509Certificate issuedCert = CertUtil.signCertificateRequest(request, new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis() + 500000000), rootCert, rootKey);
		assertNotNull(issuedCert);

		return new X509Certificate[]{issuedCert, rootCert};
	}


	private static FileInputStream getFileInputStream(String file) throws Exception {
		return new FileInputStream(new File(file));

	}

}
