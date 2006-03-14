package gov.nih.nci.cagrid.gts.service;

import java.math.BigInteger;

import org.bouncycastle.asn1.x509.CRLReason;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gridca.common.CRLEntry;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gts.bean.Permission;
import gov.nih.nci.cagrid.gts.bean.PermissionFilter;
import gov.nih.nci.cagrid.gts.bean.Role;
import gov.nih.nci.cagrid.gts.bean.Status;
import gov.nih.nci.cagrid.gts.bean.TrustLevel;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter;
import gov.nih.nci.cagrid.gts.bean.X509CRL;
import gov.nih.nci.cagrid.gts.bean.X509Certificate;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.IllegalPermissionFault;
import gov.nih.nci.cagrid.gts.stubs.InvalidPermissionFault;
import gov.nih.nci.cagrid.gts.test.CA;
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
			p2
					.setTrustedAuthorityName("O=Test Organization,OU=Test Unit,CN=CA");
			pm.addPermission(p2);
			assertTrue(pm.doesPermissionExist(p2));
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	public void testRevokePermission() {
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
			p2
					.setTrustedAuthorityName("O=Test Organization,OU=Test Unit,CN=CA");
			pm.addPermission(p2);
			assertTrue(pm.doesPermissionExist(p2));
			pm.revokePermission(p1);
			assertFalse(pm.doesPermissionExist(p1));
			assertTrue(pm.doesPermissionExist(p2));
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	public void testRevokeNonExistingPermission() {
		try {
			PermissionManager pm = new PermissionManager(db);

			Permission p = new Permission();
			p.setGridIdentity("O=Test Organization,OU=Test Unit,CN=User");
			p.setRole(Role.TrustAuthorityManager);
			p.setTrustedAuthorityName("O=Test Organization,OU=Test Unit,CN=CA");

			try {
				pm.revokePermission(p);
				fail("Should not be able to revoke a permission that does not exist.");
			} catch (InvalidPermissionFault f) {

			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	public void testAddInvalidPermission() {
		try {
			PermissionManager pm = new PermissionManager(db);

			// Test adding the same permission twice

			Permission p1 = new Permission();
			p1.setGridIdentity("O=Test Organization,OU=Test Unit,CN=User");
			p1.setRole(Role.TrustServiceAdmin);
			pm.addPermission(p1);
			assertTrue(pm.doesPermissionExist(p1));

			try {
				Permission p2 = new Permission();
				p2.setGridIdentity("O=Test Organization,OU=Test Unit,CN=User");
				p2.setRole(Role.TrustServiceAdmin);
				pm.addPermission(p2);
				fail("Should not be able to add an existing permission.");
			} catch (IllegalPermissionFault f) {

			}

			try {
				Permission p3 = new Permission();
				p3.setRole(Role.TrustServiceAdmin);
				pm.addPermission(p3);
				fail("Should not be able to add a permission without a grid identity.");
			} catch (IllegalPermissionFault f) {

			}

			try {
				Permission p4 = new Permission();
				p4.setGridIdentity("O=Test Organization,OU=Test Unit,CN=User");
				pm.addPermission(p4);
				fail("Should not be able to add a permission without a role.");
			} catch (IllegalPermissionFault f) {

			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}

	}

	public void testFindTrustedAuthorities() {
		try {
			PermissionManager pm = new PermissionManager(db);

			int count = 5;
			String dnPrefix1 = "O=Organization ABC,OU=Unit XYZ,CN=User X";
			String dnPrefix2 = "O=Organization ABC,OU=Unit XYZ,CN=User Y";
			String ta = "O=Organization ABC,OU=Unit XYZ,CN=Certificate Authority";
			Permission[] perms1 = new Permission[count];
			Permission[] perms2 = new Permission[count];
			for (int i = 0; i < count; i++) {
				String dn1 = dnPrefix1 + i;
				String dn2 = dnPrefix2 + i;

				perms1[i] = new Permission();
				perms1[i].setGridIdentity(dn1);
				perms1[i].setRole(Role.TrustServiceAdmin);
				pm.addPermission(perms1[i]);
				assertTrue(pm.doesPermissionExist(perms1[i]));
				PermissionFilter fx = new PermissionFilter();
				fx.setGridIdentity(perms1[i].getGridIdentity());
				fx.setRole(perms1[i].getRole());
				fx.setTrustedAuthorityName(perms1[i].getTrustedAuthorityName());
				Permission[] px = pm.findPermissions(fx);
				assertEquals(1, px.length);
				assertEquals(perms1[i], px[0]);

				perms2[i] = new Permission();
				perms2[i].setGridIdentity(dn2);
				perms2[i].setRole(Role.TrustServiceAdmin);
				perms2[i].setTrustedAuthorityName(ta);
				pm.addPermission(perms2[i]);
				assertTrue(pm.doesPermissionExist(perms2[i]));

				PermissionFilter fy = new PermissionFilter();
				fy.setGridIdentity(perms2[i].getGridIdentity());
				fy.setRole(perms2[i].getRole());
				fy.setTrustedAuthorityName(perms2[i].getTrustedAuthorityName());
				Permission[] py = pm.findPermissions(fy);
				assertEquals(1, py.length);
				assertEquals(perms2[i], py[0]);
			}
			// Test Remove
			for (int i = 0; i < count; i++) {
				pm.revokePermission(perms1[i]);
				assertFalse(pm.doesPermissionExist(perms1[i]));
				pm.revokePermission(perms2[i]);
				assertFalse(pm.doesPermissionExist(perms2[i]));
			}
			assertEquals(0, pm.findPermissions(new PermissionFilter()).length);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
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
