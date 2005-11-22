package gov.nih.nci.cagrid.gums.ifs;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.common.ca.KeyUtil;
import gov.nih.nci.cagrid.gums.idp.portal.UserRolesComboBox;
import gov.nih.nci.cagrid.gums.ifs.CredentialsManager;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserRole;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidPasswordFault;
import gov.nih.nci.cagrid.gums.test.TestUtils;

import java.io.File;
import java.io.StringReader;
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
public class TestUserManager extends TestCase {

	private Database db;

	private UserManager um;

	public void testSingleUser() {
		try {
			UserManager um = new UserManager(db, getOneYearConf(), TestUtils
					.getCA(db));
			
			IFSUser user = new IFSUser();
			user.setIdPId(1);
			user.setUID("user");
			user = um.addUser(user);
			assertNotNull(user.getCertificate());
			assertNotNull(user.getGridId());
			assertNotNull(user.getUserRole());
			assertNotNull(user.getUserStatus());
			assertEquals(IFSUserRole.Non_Administrator,user.getUserRole());
			assertEquals(IFSUserStatus.Pending,user.getUserStatus());
			StringReader ureader = new StringReader(user.getCertificate());
			X509Certificate cert = CertUtil.loadCertificate(ureader);
			assertEquals(user.getGridId(),cert.getSubjectDN().getName());
			
		
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	private IFSConfiguration getOneYearConf() {
		IFSConfiguration conf = new IFSConfiguration();
		conf.setCredentialsValidYears(1);
		conf.setCredentialsValidMonths(0);
		conf.setCredentialsValidDays(0);
		return conf;
	}

	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = TestUtils.getDB();
			assertEquals(0, db.getUsedConnectionCount());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	protected void tearDown() throws Exception {
		super.setUp();
		try {
			assertEquals(0, db.getUsedConnectionCount());
			db.destroyDatabase();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
