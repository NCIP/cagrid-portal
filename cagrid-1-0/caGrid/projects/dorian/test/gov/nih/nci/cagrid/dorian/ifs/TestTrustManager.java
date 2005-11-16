package gov.nih.nci.cagrid.gums.ifs;

import gov.nih.nci.cagrid.gums.ca.CertificateAuthority;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.common.ca.KeyUtil;
import gov.nih.nci.cagrid.gums.ifs.bean.SAMLAuthenticationMethod;
import gov.nih.nci.cagrid.gums.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.gums.test.TestUtils;

import java.io.File;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.TestCase;

import org.bouncycastle.jce.PKCS10CertificationRequest;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestTrustManager extends TestCase {

	public static String IDP_CONFIG = "resources" + File.separator
			+ "general-test" + File.separator + "idp-config.xml";

	private static final int MIN_NAME_LENGTH = 4;

	private static final int MAX_NAME_LENGTH = 10;

	private Database db;

	private TrustManager tm;

	private List list;
	
	private CertificateAuthority ca;

	public void testSingleUser() {
		try {
			//We want to run this multiple times
			int times = getAuthenticationMethods().size() *2;
			for(int i=0; i<times; i++){
			assertNotNull(tm);
			assertEquals(0, tm.getTrustedIdPs().length);
			String name = "Test Idp";
			TrustedIdP idp = getTrustedIdp(name);
			tm.addTrustedIdP(idp);
			assertEquals(1, tm.getTrustedIdPs().length);
			assertEquals(idp.getAuthenticationMethod().length,tm.getAuthenticationMethods(idp.getName()).length);
			TrustedIdP[] list =tm.getTrustedIdPs();
			assertEquals(idp,list[0]);
			assertTrue(tm.determineTrustedIdPExistsByName(name));
			
			StringReader reader = new StringReader(idp.getIdPCertificate());
			X509Certificate cert = CertUtil.loadCertificate(reader);
			assertTrue(tm.determineTrustedIdPExistsBySubject(cert.getSubjectDN().toString()));
	
			tm.removeTrustedIdP(idp.getName());
			assertEquals(0, tm.getTrustedIdPs().length);
			assertEquals(0,tm.getAuthenticationMethods(idp.getName()).length);
			}		

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
	
	private TrustedIdP getTrustedIdp(String name) throws Exception{
		TrustedIdP idp = new TrustedIdP();
		idp.setName(name);
		idp.setPolicyClass("policy");
		idp.setAuthenticationMethod(getRandomMethodList());
		
		String subject = TestUtils.CA_SUBJECT_PREFIX + ",CN="+name;
		PKCS10CertificationRequest req = CertUtil.generateCertficateRequest(
				subject, KeyUtil.generateRSAKeyPair1024());
		assertNotNull(req);
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.MONTH,10);
		Date end = cal.getTime();
		X509Certificate cert = ca.requestCertificate(req, start, end);
		assertNotNull(cert);
		assertEquals(cert.getSubjectDN().getName(), subject);
		idp.setIdPCertificate(CertUtil.writeCertificateToString(cert));
		return idp;
	}

	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = TestUtils.getDB();
			IFSConfiguration conf = new IFSConfiguration();
			conf.setMinimumIdPNameLength(MIN_NAME_LENGTH);
			conf.setMaximumIdPNameLength(MAX_NAME_LENGTH);
			ca = TestUtils.getCA(db);
			tm = new TrustManager(conf, db);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
	
	public SAMLAuthenticationMethod[] getRandomMethodList(){
		double val = Math.random();	
		int size = getAuthenticationMethods().size()+1;
		double num = 1/((double)size);
		
		int count = (int)(val / num);
		
		SAMLAuthenticationMethod[] methods = new SAMLAuthenticationMethod[count];
		
		for(int i=0; i<count; i++){
			methods[i] = (SAMLAuthenticationMethod)getAuthenticationMethods().get(i);
		}
		
		return methods;
	}

	public List getAuthenticationMethods() {
		if (list == null) {
			list = new ArrayList();

			Field[] fields = SAMLAuthenticationMethod.class.getFields();

			for (int i = 0; i < fields.length; i++) {
				if (SAMLAuthenticationMethod.class.isAssignableFrom(fields[i]
						.getType())) {
					try {
						Object o = fields[i].get(null);
						list.add(o);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return list;
	}

}
