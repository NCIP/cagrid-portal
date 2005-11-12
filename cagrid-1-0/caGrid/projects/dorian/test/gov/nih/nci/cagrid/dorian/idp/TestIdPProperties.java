package gov.nih.nci.cagrid.gums.idp;

import gov.nih.nci.cagrid.gums.ca.CertificateAuthority;
import gov.nih.nci.cagrid.gums.ca.GUMSCertificateAuthority;
import gov.nih.nci.cagrid.gums.ca.GUMSCertificateAuthorityConf;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.common.ca.KeyUtil;
import gov.nih.nci.cagrid.gums.test.TestUtils;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.bouncycastle.asn1.x509.X509Name;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestIdPProperties extends TestCase {

	private Database db;
	
	private CertificateAuthority ca;

	public void testDefaultProperties() {
		try {
		
			IdPProperties props = new IdPProperties(ca,db);
			assertEquals(IdPProperties.DEFAULT_MIN_PASSWORD_LENGTH, props
					.getMinimumPasswordLength());
			assertEquals(IdPProperties.DEFAULT_MAX_PASSWORD_LENGTH, props
					.getMaximumPasswordLength());
			assertEquals(IdPProperties.DEFAULT_MIN_UID_LENGTH, props
					.getMinimumUIDLength());
			assertEquals(IdPProperties.DEFAULT_MAX_UID_LENGTH, props
					.getMaximumUIDLength());
			assertEquals(ManualRegistrationPolicy.class.getName(), props.getRegistrationPolicy().getClass().getName());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				db.destroyDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private CertificateAuthority getCA() {
		
		GUMSCertificateAuthorityConf conf = new GUMSCertificateAuthorityConf();
		conf.setCaPassword("password");
		conf.setAutoRenewal(false);
		GUMSCertificateAuthority ca = new GUMSCertificateAuthority(db, conf);
		try{
		KeyPair rootPair = KeyUtil.generateRSAKeyPair1024();
		assertNotNull(rootPair);
		String rootSub = "O=Ohio State University,OU=BMI,OU=TEST,CN=Temp Certificate Authority";
		X509Name rootSubject = new X509Name(rootSub);
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.YEAR, 1);
		Date end = cal.getTime();
		X509Certificate root = CertUtil.generateCACertificate(rootSubject,
				start, end, rootPair);
		assertNotNull(root);
		ca.setCACredentials(root, rootPair.getPrivate());
		X509Certificate r = ca.getCACertificate();
		assertNotNull(r);
		assertEquals(r, root);
		}catch(Exception e){
			e.printStackTrace();
			assertTrue(false);
		}
		return ca;
		
	}

	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = TestUtils.getDB();
			ca = TestUtils.getCA();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
