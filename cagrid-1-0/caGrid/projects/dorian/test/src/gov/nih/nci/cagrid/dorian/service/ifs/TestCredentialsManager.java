package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidPasswordFault;
import gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.test.Utils;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

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
public class TestCredentialsManager extends TestCase {

	private static final String TABLE = "test_credentials";

	private static final String CA_USER = "CA";

	private static final String CA_PASSWORD = "CA_PASSWORD";

	private Database db;

	private CredentialsManager cred;


	public void testCreateObtainCACredentials() {
		try {
			createAndStoreCA();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testInsertObtainUserCredentials() {
		try {
			createAndStoreCA();
			createAndStoreUserCredentials("foo", "foobar");
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testDeleteUserCredentials() {
		try {
			createAndStoreCA();
			String user = "user";
			String password = "password";
			createAndStoreUserCredentials(user, password);
			cred.deleteCredentials(user);
			assertFalse(cred.hasCredentials(user));
			try {
				cred.getPrivateKey(user, password);
				assertTrue(false);
			} catch (DorianInternalFault gie) {

			}
			try {
				cred.getCertificate(user);
				assertTrue(false);
			} catch (DorianInternalFault gie) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testInsertObtainManyUserCredentials() {
		try {
			createAndStoreCA();
			for (int i = 0; i < 10; i++) {
				createAndStoreUserCredentials("user" + i, "password" + i);
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testDeleteManyUserCredentials() {
		try {
			createAndStoreCA();
			for (int i = 0; i < 10; i++) {
				createAndStoreUserCredentials("user" + i, "password" + i);
			}

			for (int i = 0; i < 10; i++) {
				cred.deleteCredentials("user" + i);
				assertFalse(cred.hasCredentials("user" + i));
				try {
					cred.getPrivateKey("user" + i, "password" + i);
					assertTrue(false);
				} catch (DorianInternalFault gie) {

				}
				try {
					cred.getCertificate("user" + i);
					assertTrue(false);
				} catch (DorianInternalFault gie) {

				}
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testInsertAndObtainBadPassword() {
		try {
			createAndStoreCA();
			createAndStoreUserCredentials("foo", "foobar");
			cred.getPrivateKey("foo", "foobad");
			fail("Should have thrown InvalidPasswordFault!");
		} catch (InvalidPasswordFault ipf) {
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	private void createAndStoreCA() throws Exception {
		KeyPair rootPair = KeyUtil.generateRSAKeyPair1024();
		assertNotNull(rootPair);
		String rootSub = "O=Ohio State University,OU=BMI,OU=MSCL,CN=Temp Certificate Authority";
		X509Name rootSubject = new X509Name(rootSub);
		X509Certificate root = CertUtil.generateCACertificate(rootSubject, new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis() + 500000000), rootPair);
		assertNotNull(root);
		storeRetrieveAndConfirmCredentials(CA_USER, CA_PASSWORD, root, rootPair.getPrivate());

	}


	private Creds createAndStoreUserCredentials(String username, String password) throws Exception {
		PrivateKey rootKey = cred.getPrivateKey(CA_USER, CA_PASSWORD);
		assertNotNull(rootKey);
		X509Certificate rootCert = cred.getCertificate(CA_USER);
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
		String sub = rootSub.substring(0, index) + ",CN=" + username;
		PKCS10CertificationRequest request = CertUtil.generateCertficateRequest(sub, pair);

		// validate the certification request
		if (!request.verify("BC")) {
			System.out.println("request failed to verify!");
			System.exit(1);
		}

		X509Certificate issuedCert = CertUtil.signCertificateRequest(request, new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis() + 500000000), rootCert, rootKey);
		assertNotNull(issuedCert);
		storeRetrieveAndConfirmCredentials(username, password, issuedCert, pair.getPrivate());
		return new Creds(issuedCert, pair.getPrivate());
	}


	private void storeRetrieveAndConfirmCredentials(String username, String password, X509Certificate cert,
		PrivateKey key) throws Exception {
		cred.addCredentials(username, password, cert, key);
		assertTrue(cred.hasCredentials(username));
		X509Certificate dbCert = cred.getCertificate(username);
		assertEquals(cert.getSubjectDN(), dbCert.getSubjectDN());
		assertEquals(cert, dbCert);
		PrivateKey dbKey = cred.getPrivateKey(username, password);
		assertEquals(key, dbKey);
	}


	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = Utils.getDB();
			assertEquals(0, db.getUsedConnectionCount());
			CredentialsManager.CREDENTIALS_TABLE = TABLE;
			cred = new CredentialsManager(db);
			//cred.destroyTable();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	protected void tearDown() throws Exception {
		super.setUp();
		try {
			cred.clearDatabase();
			assertEquals(0, db.getUsedConnectionCount());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


public void stessTestInsertAndObtainBadPassword() throws Exception {
		try {
			createAndStoreCA();
			Creds temp = createAndStoreUserCredentials("foo", "foobar");
			if(temp.key.equals(cred.getPrivateKey("foo", "foobad"))){
				assertTrue(false);
			}
			//assertTrue(false);
		} catch (InvalidPasswordFault ipf) {
		}
	}	public class Creds {
		public X509Certificate cert;
		public PrivateKey key;


		public Creds(X509Certificate cert, PrivateKey key) {
			this.cert = cert;
			this.key = key;
		}
	}


	public static void main(String[] args) {
		try {
			int count = 0;
			while (true) {
				count++;
				System.err.println("Run " + count);
				TestCredentialsManager tcm = new TestCredentialsManager();
				tcm.setUp();
				tcm.stessTestInsertAndObtainBadPassword();
				tcm.tearDown();
			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			System.exit(1);
		}

	}

}
