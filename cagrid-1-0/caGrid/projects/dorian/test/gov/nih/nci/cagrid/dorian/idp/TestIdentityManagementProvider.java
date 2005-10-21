package gov.nih.nci.cagrid.gums.idp;

import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.idp.bean.Application;
import gov.nih.nci.cagrid.gums.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.gums.idp.bean.User;
import gov.nih.nci.cagrid.gums.idp.bean.UserFilter;

import java.io.File;

import junit.framework.TestCase;

import org.jdom.Document;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.db.ConnectionManager;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestIdentityManagementProvider extends TestCase {

	private static final String DB = "TEST_GUMS";

	public static String DB_CONFIG = "resources" + File.separator
			+ "general-test" + File.separator + "db-config.xml";

	private Database db;
	private int count =0;
	
	public void testAutomaticRegistration() {
		try {
			IdentityManagerProvider imp = new IdentityManagerProvider(db);
			imp.getProperties().setRegistrationPolicy(new AutomaticRegistrationPolicy());
			assertEquals(AutomaticRegistrationPolicy.class.getName(), imp.getProperties().getRegistrationPolicy().getClass().getName());
			Application a = createApplication();
			imp.register(a);
			BasicAuthCredential cred = getAdminCreds();
			UserFilter uf = new UserFilter();
			uf.setUserId(a.getUserId());
			User[] users = imp.findUsers(cred,uf);
			assertEquals(1,users.length);
			assertEquals(UserManager.ACTIVE,users[0].getStatus());
			assertEquals(UserManager.NON_ADMINISTRATOR,users[0].getRole());
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

	public void testManualRegistration() {
		try {
			IdentityManagerProvider imp = new IdentityManagerProvider(db);
			imp.getProperties().setRegistrationPolicy(new ManualRegistrationPolicy());
			assertEquals(ManualRegistrationPolicy.class.getName(), imp.getProperties().getRegistrationPolicy().getClass().getName());
			Application a = createApplication();
			imp.register(a);
			BasicAuthCredential cred = getAdminCreds();
			UserFilter uf = new UserFilter();
			uf.setUserId(a.getUserId());
			User[] users = imp.findUsers(cred,uf);
			assertEquals(1,users.length);
			assertEquals(UserManager.PENDING,users[0].getStatus());
			assertEquals(UserManager.NON_ADMINISTRATOR,users[0].getRole());
			users[0].setStatus(UserManager.ACTIVE);
			imp.updateUser(cred,users[0]);
			users = imp.findUsers(cred,uf);
			assertEquals(1,users.length);
			assertEquals(UserManager.ACTIVE,users[0].getStatus());
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
	
	private BasicAuthCredential getAdminCreds(){
		BasicAuthCredential cred = new BasicAuthCredential();
		cred.setUserId(IdentityManagerProvider.ADMIN_USER_ID);
		cred.setPassword(IdentityManagerProvider.ADMIN_PASSWORD);
		return cred;
	}
	
	private Application createApplication() {
		Application u = new Application();
		u.setUserId(count+"user");
		u.setEmail(count+"user@mail.com");
		u.setPassword(count+"password");
		u.setFirstName(count+"first");
		u.setLastName(count+"last");
		u.setAddress(count+"address");
		u.setAddress2(count+"address2");
		u.setCity("Columbus");
		u.setState("OH");
		u.setZipcode("43210");
		u.setPhoneNumber("614-555-5555");
		u.setOrganization(count+"organization");
		count = count + 1;
		return u;
	}

	protected void setUp() throws Exception {
		super.setUp();
		try {
			count =0;
			Document doc = XMLUtilities.fileNameToDocument(DB_CONFIG);
			ConnectionManager cm = new ConnectionManager(doc.getRootElement());
			db = new Database(cm, DB);
			db.destroyDatabase();
			db.createDatabaseIfNeeded();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
}
