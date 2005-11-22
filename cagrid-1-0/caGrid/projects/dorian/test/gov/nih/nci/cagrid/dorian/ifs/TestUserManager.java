package gov.nih.nci.cagrid.gums.ifs;

import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserRole;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.gums.test.TestUtils;

import java.io.StringReader;
import java.security.cert.X509Certificate;

import junit.framework.TestCase;

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
			
			//Test adding user
			
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
			
			//Test Querying for users
			IFSUserFilter f1 = new IFSUserFilter();
			IFSUser[] l1 = um.getUsers(f1);
			assertEquals(1,l1.length);
			assertEquals(user,l1[0]);
			
			//Test querying by uid
			IFSUserFilter f2 = new IFSUserFilter();
			f2.setUID("nobody");
			IFSUser[] l2 = um.getUsers(f2);
			assertEquals(0,l2.length);
			f2.setUID("use");
			l2 = um.getUsers(f2);
			assertEquals(1,l2.length);
			assertEquals(user,l2[0]);
		
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
