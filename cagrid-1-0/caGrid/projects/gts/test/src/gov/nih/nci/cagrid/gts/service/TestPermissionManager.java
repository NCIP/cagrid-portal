package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gts.bean.Permission;
import gov.nih.nci.cagrid.gts.bean.Role;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.test.Utils;
import junit.framework.TestCase;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestPermissionManager extends TestCase {

	private Database db;

	public void testCreateAndDestroy() {
		try {
			PermissionManager pm = new PermissionManager(db);
			pm.buildDatabase();
			pm.destroy();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
	
	public void testAddPermission() {
		try {
			PermissionManager pm = new PermissionManager(db);
			Permission p1 = new Permission();
			p1.setGridIdentity("O=Test Organization,OU=Test Unit,CN=User");
			p1.setRole(Role.TrustServiceAdmin);
			pm.addPermission(p1);
			assertTrue(pm.doesPermissionExist(p1));
			
			Permission p2 = new Permission();
			p2.setGridIdentity("O=Test Organization,OU=Test Unit,CN=User");
			p2.setRole(Role.TrustAuthorityManager);
			p2.setTrustedAuthorityName("O=Test Organization,OU=Test Unit,CN=CA");
			pm.addPermission(p2);
			assertTrue(pm.doesPermissionExist(p2));
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	

	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = Utils.getDB();
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
