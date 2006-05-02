package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gridca.common.CRLEntry;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gts.bean.AuthorityGTS;
import gov.nih.nci.cagrid.gts.bean.AuthorityPrioritySpecification;
import gov.nih.nci.cagrid.gts.bean.AuthorityPriorityUpdate;
import gov.nih.nci.cagrid.gts.bean.Lifetime;
import gov.nih.nci.cagrid.gts.bean.Permission;
import gov.nih.nci.cagrid.gts.bean.PermissionFilter;
import gov.nih.nci.cagrid.gts.bean.Role;
import gov.nih.nci.cagrid.gts.bean.Status;
import gov.nih.nci.cagrid.gts.bean.TrustLevel;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityTimeToLive;
import gov.nih.nci.cagrid.gts.bean.X509CRL;
import gov.nih.nci.cagrid.gts.bean.X509Certificate;
import gov.nih.nci.cagrid.gts.stubs.IllegalPermissionFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalTrustLevelFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalTrustedAuthorityFault;
import gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault;
import gov.nih.nci.cagrid.gts.test.CA;
import gov.nih.nci.cagrid.gts.test.Utils;
import gov.nih.nci.cagrid.gts.tools.service.PermissionBootstapper;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.bouncycastle.asn1.x509.CRLReason;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestGTS extends TestCase {

	private final static String ADMIN_USER = "O=Test Organization,OU=Test Unit,CN=GTS Admin";

	private final static String LEVEL_ONE = "ONE";
	private final static String LEVEL_TWO = "TWO";

	private int cacount = 0;
	private final String dnPrefix = "O=Organization ABC,OU=Unit XYZ,CN=Certificate Authority";
	private final String GTS_URI = "localhost";


	public void testCreateAndDestroy() {
		try {
			GTS gts = new GTS(Utils.getGTSConfiguration(), GTS_URI);
			// Make sure we start fresh
			gts.destroy();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testAddUpdateRemoveAuthorities() {
		GTS gts = null;
		try {
			GTSConfiguration conf = Utils.getGTSConfiguration();
			gts = new GTS(conf, GTS_URI);
			// Make sure we start fresh
			gts.destroy();

			try {
				gts.findPermissions(new PermissionFilter(), ADMIN_USER);
				fail("Should not be able to fine permissions, no admin permission are configured.");
			} catch (PermissionDeniedFault f) {

			}

			PermissionBootstapper pb = new PermissionBootstapper(conf);
			pb.addAdminUser(ADMIN_USER);
			assertEquals(1, gts.findPermissions(new PermissionFilter(), ADMIN_USER).length);
			int count = 5;
			AuthorityGTS[] a = new AuthorityGTS[count];

			for (int i = 0; i < count; i++) {
				a[i] = getAuthority("GTS " + i, 1);
				gts.addAuthority(a[i], ADMIN_USER);
				assertEquals((i + 1), gts.getAuthorities().length);
				for (int j = 0; j < i; j++) {
					a[j].setPriority(a[j].getPriority() + 1);
				}
			}

			for (int i = 0; i < count; i++) {
				updateAuthority(a[i]);
				gts.updateAuthority(a[i], ADMIN_USER);
				assertEquals(count, gts.getAuthorities().length);
			}
			int priority = 1;
			AuthorityPrioritySpecification[] specs = new AuthorityPrioritySpecification[count];
			for (int i = 0; i < count; i++) {
				a[i].setPriority(priority);
				specs[i] = new AuthorityPrioritySpecification();
				specs[i].setServiceURI(a[i].getServiceURI());
				specs[i].setPriority(a[i].getPriority());
				priority = priority + 1;
			}
			AuthorityPriorityUpdate update = new AuthorityPriorityUpdate();
			update.setAuthorityPrioritySpecification(specs);
			gts.updateAuthorityPriorities(update, ADMIN_USER);
			assertEquals(count, gts.getAuthorities().length);

			AuthorityGTS[] auths = gts.getAuthorities();
			for (int i = 0; i < count; i++) {
				assertEquals(a[i], auths[i]);
			}
			int num = count;
			for (int i = 0; i < count; i++) {
				gts.removeAuthority(a[i].getServiceURI(), ADMIN_USER);
				num = num - 1;
				assertEquals(num, gts.getAuthorities().length);
			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				gts.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	public void testAddFindRevokePermission() {
		GTS gts = null;
		try {
			GTSConfiguration conf = Utils.getGTSConfiguration();
			String user = "O=Test Organization,OU=Test Unit,CN=User";
			String user2 = "O=Test Organization,OU=Test Unit,CN=User2";
			gts = new GTS(conf, GTS_URI);
			// Make sure we start fresh
			gts.destroy();

			try {
				gts.findPermissions(new PermissionFilter(), ADMIN_USER);
				fail("Should not be able to fine permissions, no admin permission are configured.");
			} catch (PermissionDeniedFault f) {

			}

			PermissionBootstapper pb = new PermissionBootstapper(conf);
			pb.addAdminUser(ADMIN_USER);
			assertEquals(1, gts.findPermissions(new PermissionFilter(), ADMIN_USER).length);
			addTrustLevels(gts, ADMIN_USER);
			CA ca = new CA();
			TrustedAuthority ta = new TrustedAuthority();
			ta.setTrustedAuthorityName(ca.getCertificate().getSubjectDN().toString());
			ta.setCertificate(new X509Certificate(CertUtil.writeCertificate(ca.getCertificate())));
			ta.setStatus(Status.Trusted);
			ta.setTrustLevel(LEVEL_ONE);

			Permission userPerm = new Permission();
			userPerm.setGridIdentity(user);
			userPerm.setRole(Role.TrustAuthorityManager);
			userPerm.setTrustedAuthorityName(ta.getTrustedAuthorityName());

			Permission p = new Permission();
			p.setGridIdentity(user2);
			p.setRole(Role.TrustAuthorityManager);
			p.setTrustedAuthorityName(ta.getTrustedAuthorityName());

			// Test null
			try {
				gts.addPermission(p, null);
				fail("Non trust service administrators should not be able to add a permission!!!");
			} catch (PermissionDeniedFault f) {

			}

			try {
				gts.findPermissions(new PermissionFilter(), null);
				fail("Non trust service administrators should not be able to find permissions!!!");
			} catch (PermissionDeniedFault f) {

			}

			try {
				gts.revokePermission(p, null);
				fail("Non trust service administrators should not be able to revoke a permission!!!");
			} catch (PermissionDeniedFault f) {

			}

			// Test Empty String
			try {
				gts.addPermission(p, "");
				fail("Non trust service administrators should not be able to add a permission!!!");
			} catch (PermissionDeniedFault f) {

			}

			try {
				gts.findPermissions(new PermissionFilter(), "");
				fail("Non trust service administrators should not be able to find permissions!!!");
			} catch (PermissionDeniedFault f) {

			}

			try {
				gts.revokePermission(p, "");
				fail("Non trust service administrators should not be able to revoke a permission!!!");
			} catch (PermissionDeniedFault f) {

			}

			// Test user with no permissions
			try {
				gts.addPermission(p, user);
				fail("Non trust service administrators should not be able to add a permission!!!");
			} catch (PermissionDeniedFault f) {

			}

			try {
				gts.findPermissions(new PermissionFilter(), user);
				fail("Non trust service administrators should not be able to find permissions!!!");
			} catch (PermissionDeniedFault f) {

			}

			try {
				gts.revokePermission(p, user);
				fail("Non trust service administrators should not be able to revoke a permission!!!");
			} catch (PermissionDeniedFault f) {

			}
			try {
				gts.addPermission(userPerm, ADMIN_USER);
				fail("Should not be able to add a permission that applies to a Trusted Authority that does not exist.!!!");
			} catch (IllegalPermissionFault f) {

			}
			gts.addTrustedAuthority(ta, ADMIN_USER);
			assertEquals(1, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);
			assertEquals(ta, gts.findTrustAuthorities(new TrustedAuthorityFilter())[0]);
			gts.addPermission(userPerm, ADMIN_USER);
			assertEquals(1, gts.findPermissions(permissionToPermissionFilter(userPerm), ADMIN_USER).length);
			assertEquals(userPerm, gts.findPermissions(permissionToPermissionFilter(userPerm), ADMIN_USER)[0]);
			// Test user with Invalid Permission
			try {
				gts.addPermission(p, user);
				fail("Non trust service administrators should not be able to add a permission!!!");
			} catch (PermissionDeniedFault f) {

			}

			try {
				gts.findPermissions(new PermissionFilter(), user);
				fail("Non trust service administrators should not be able to find permissions!!!");
			} catch (PermissionDeniedFault f) {

			}

			try {
				gts.revokePermission(p, user);
				fail("Non trust service administrators should not be able to revoke a permission!!!");
			} catch (PermissionDeniedFault f) {

			}

			// Now give use Admin rights
			Permission admin = new Permission();
			admin.setGridIdentity(user);
			admin.setRole(Role.TrustServiceAdmin);

			gts.addPermission(admin, ADMIN_USER);
			assertEquals(1, gts.findPermissions(permissionToPermissionFilter(admin), ADMIN_USER).length);
			assertEquals(admin, gts.findPermissions(permissionToPermissionFilter(admin), ADMIN_USER)[0]);

			// Now that the user is admin try again
			gts.addPermission(p, user);
			assertEquals(1, gts.findPermissions(permissionToPermissionFilter(p), user).length);
			assertEquals(p, gts.findPermissions(permissionToPermissionFilter(p), user)[0]);
			gts.revokePermission(p, user);
			assertEquals(0, gts.findPermissions(permissionToPermissionFilter(p), user).length);

			// Now Revoke the user's admin rights and try again
			gts.revokePermission(admin, ADMIN_USER);
			assertEquals(0, gts.findPermissions(permissionToPermissionFilter(admin), ADMIN_USER).length);
			try {
				gts.addPermission(p, user);
				fail("Non trust service administrators should not be able to add a permission!!!");
			} catch (PermissionDeniedFault f) {

			}

			try {
				gts.findPermissions(new PermissionFilter(), user);
				fail("Non trust service administrators should not be able to find permissions!!!");
			} catch (PermissionDeniedFault f) {

			}

			try {
				gts.revokePermission(p, user);
				fail("Non trust service administrators should not be able to revoke a permission!!!");
			} catch (PermissionDeniedFault f) {

			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			if (gts != null) {
				try {
					gts.destroy();
				} catch (Exception e) {
					FaultUtil.printFault(e);
				}
			}
		}
	}


	public void testAddTrustedAuthorityInvalidLevel() {
		GTS gts = null;
		try {
			GTSConfiguration conf = Utils.getGTSConfiguration();

			gts = new GTS(conf, GTS_URI);
			// Make sure we start fresh
			gts.destroy();
			PermissionBootstapper pb = new PermissionBootstapper(conf);
			pb.addAdminUser(ADMIN_USER);
			addTrustLevels(gts, ADMIN_USER);
			CA ca = new CA();
			BigInteger sn = new BigInteger(String.valueOf(System.currentTimeMillis()));
			CRLEntry entry = new CRLEntry(sn, CRLReason.PRIVILEGE_WITHDRAWN);
			ca.updateCRL(entry);
			TrustedAuthority ta = new TrustedAuthority();
			ta.setTrustedAuthorityName(ca.getCertificate().getSubjectDN().toString());
			ta.setCertificate(new X509Certificate(CertUtil.writeCertificate(ca.getCertificate())));
			ta.setCRL(new X509CRL(CertUtil.writeCRL(ca.getCRL())));
			ta.setStatus(Status.Trusted);
			ta.setTrustLevel("INVALID_LEVEL");

			try {
				gts.addTrustedAuthority(ta, ADMIN_USER);
				fail("Should not to be able to add a trusted authority with and invalid trust level!!!");
			} catch (IllegalTrustedAuthorityFault f) {

			}
			assertEquals(0, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);

			gts.destroy();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			if (gts != null) {
				try {
					gts.destroy();
				} catch (Exception e) {
					FaultUtil.printFault(e);
				}
			}
		}

	}


	public void testAddTrustedAuthority() {
		GTS gts = null;
		try {
			GTSConfiguration conf = Utils.getGTSConfiguration();

			gts = new GTS(conf, GTS_URI);
			// Make sure we start fresh
			gts.destroy();
			String user = "O=Test Organization,OU=Test Unit,CN=User";
			PermissionBootstapper pb = new PermissionBootstapper(conf);
			pb.addAdminUser(ADMIN_USER);
			addTrustLevels(gts, ADMIN_USER);
			CA ca = new CA();
			BigInteger sn = new BigInteger(String.valueOf(System.currentTimeMillis()));
			CRLEntry entry = new CRLEntry(sn, CRLReason.PRIVILEGE_WITHDRAWN);
			ca.updateCRL(entry);
			TrustedAuthority ta = new TrustedAuthority();
			ta.setTrustedAuthorityName(ca.getCertificate().getSubjectDN().toString());
			ta.setCertificate(new X509Certificate(CertUtil.writeCertificate(ca.getCertificate())));
			ta.setCRL(new X509CRL(CertUtil.writeCRL(ca.getCRL())));
			ta.setStatus(Status.Trusted);
			ta.setTrustLevel(LEVEL_ONE);

			// Test null
			try {
				gts.addTrustedAuthority(ta, null);
				fail("Non trust service administrators should not be able to update a trust authority!!!");
			} catch (PermissionDeniedFault f) {

			}
			assertEquals(0, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);

			// Test Empty String
			try {
				gts.addTrustedAuthority(ta, "");
				fail("Non trust service administrators should not be able to update a trust authority!!!");
			} catch (PermissionDeniedFault f) {

			}
			assertEquals(0, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);

			// Test User without any permissions
			try {
				gts.addTrustedAuthority(ta, user);
				fail("Non trust service administrators should not be able to update a trust authority!!!");
			} catch (PermissionDeniedFault f) {

			}
			assertEquals(0, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);

			// Finally Add a trust authority so we can create trust manager
			// users
			gts.addTrustedAuthority(ta, ADMIN_USER);
			assertEquals(1, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);
			assertEquals(ta, gts.findTrustAuthorities(new TrustedAuthorityFilter())[0]);

			// Now create a permission for a user on the previous added trust
			// authority.
			Permission p = new Permission();
			p.setGridIdentity(user);
			p.setRole(Role.TrustAuthorityManager);
			p.setTrustedAuthorityName(ta.getTrustedAuthorityName());
			gts.addPermission(p, ADMIN_USER);

			// Check to make sure the permission was properly added
			PermissionFilter pf = permissionToPermissionFilter(p);
			assertEquals(1, gts.findPermissions(pf, ADMIN_USER).length);
			assertEquals(p, gts.findPermissions(pf, ADMIN_USER)[0]);

			// Now Create a new Trust Authority
			CA ca2 = new CA();
			TrustedAuthority ta2 = new TrustedAuthority();
			ta2.setTrustedAuthorityName(ca2.getCertificate().getSubjectDN().toString());
			ta2.setCertificate(new X509Certificate(CertUtil.writeCertificate(ca2.getCertificate())));
			ta2.setStatus(Status.Trusted);
			ta2.setTrustLevel(LEVEL_ONE);

			try {
				gts.addTrustedAuthority(ta, user);
				fail("Non trust service administrators should not be able to update a trust authority!!!");
			} catch (PermissionDeniedFault f) {

			}

			assertEquals(1, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);

			gts.destroy();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			if (gts != null) {
				try {
					gts.destroy();
				} catch (Exception e) {
					FaultUtil.printFault(e);
				}
			}
		}
	}


	public void testRemoveReferencedTrustLevels() {
		GTS gts = null;
		try {
			GTSConfiguration conf = Utils.getGTSConfiguration();

			gts = new GTS(conf, GTS_URI);
			// Make sure we start fresh
			gts.destroy();
			PermissionBootstapper pb = new PermissionBootstapper(conf);
			pb.addAdminUser(ADMIN_USER);
			TrustLevel l1 = new TrustLevel();
			l1.setName(LEVEL_ONE);
			gts.addTrustLevel(l1, ADMIN_USER);
			assertTrue(gts.doesTrustLevelExist(l1.getName()));
			assertEquals(1, gts.getTrustLevels().length);

			CA ca = new CA();
			BigInteger sn = new BigInteger(String.valueOf(System.currentTimeMillis()));
			CRLEntry entry = new CRLEntry(sn, CRLReason.PRIVILEGE_WITHDRAWN);
			ca.updateCRL(entry);
			TrustedAuthority ta = new TrustedAuthority();
			ta.setTrustedAuthorityName(ca.getCertificate().getSubjectDN().toString());
			ta.setCertificate(new X509Certificate(CertUtil.writeCertificate(ca.getCertificate())));
			ta.setCRL(new X509CRL(CertUtil.writeCRL(ca.getCRL())));
			ta.setStatus(Status.Trusted);
			ta.setTrustLevel(LEVEL_ONE);

			gts.addTrustedAuthority(ta, ADMIN_USER);
			assertEquals(1, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);
			assertEquals(ta, gts.findTrustAuthorities(new TrustedAuthorityFilter())[0]);

			// Now try and remove the trust level
			try {
				gts.removeTrustLevel(l1.getName(), ADMIN_USER);
				fail("Should not be able to remove a trust level that is referenced by a Trusted Authority!!!");
			} catch (IllegalTrustLevelFault f) {

			}
			gts.removeTrustedAuthority(ta.getTrustedAuthorityName(), ADMIN_USER);
			gts.removeTrustLevel(l1.getName(), ADMIN_USER);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			if (gts != null) {
				try {
					gts.destroy();
				} catch (Exception e) {
					FaultUtil.printFault(e);
				}
			}
		}

	}


	public void testAddGetUpdateRemoveTrustLevels() {
		GTS gts = null;
		try {
			GTSConfiguration conf = Utils.getGTSConfiguration();
			PermissionBootstapper pb = new PermissionBootstapper(conf);
			pb.addAdminUser(ADMIN_USER);
			String user = "O=Test Organization,OU=Test Unit,CN=User";
			gts = new GTS(conf, GTS_URI);
			int size = 5;
			TrustLevel[] level = new TrustLevel[size];
			for (int i = 0; i < size; i++) {
				level[i] = new TrustLevel();
				level[i].setName("My Level " + i);
				level[i].setDescription("Trust Level " + i);
				try {
					gts.addTrustLevel(level[i], null);
					fail("Non trust service administrators should not be able to add a trust level!!!");
				} catch (PermissionDeniedFault f) {

				}

				try {
					gts.addTrustLevel(level[i], "");
					fail("Non trust service administrators should not be able to add a trust level!!!");
				} catch (PermissionDeniedFault f) {

				}

				try {
					gts.addTrustLevel(level[i], user);
					fail("Non trust service administrators should not be able to add a trust level!!!");
				} catch (PermissionDeniedFault f) {

				}

				gts.addTrustLevel(level[i], ADMIN_USER);
				assertEquals((i + 1), gts.getTrustLevels().length);
				assertEquals(true, gts.doesTrustLevelExist(level[i].getName()));
				level[i].setDescription("Updated Trust Level " + i);

				try {
					gts.updateTrustLevel(level[i], null);
					fail("Non trust service administrators should not be able to update a trust level!!!");
				} catch (PermissionDeniedFault f) {

				}
				try {
					gts.updateTrustLevel(level[i], "");
					fail("Non trust service administrators should not be able to update a trust level!!!");
				} catch (PermissionDeniedFault f) {

				}

				try {
					gts.updateTrustLevel(level[i], user);
					fail("Non trust service administrators should not be able to update a trust level!!!");
				} catch (PermissionDeniedFault f) {

				}

				gts.updateTrustLevel(level[i], ADMIN_USER);
				assertEquals((i + 1), gts.getTrustLevels().length);
				assertEquals(true, gts.doesTrustLevelExist(level[i].getName()));
			}
			int count = size;
			for (int i = 0; i < size; i++) {
				try {
					gts.removeTrustLevel(level[i].getName(), null);
					fail("Non trust service administrators should not be able to remove a trust level!!!");
				} catch (PermissionDeniedFault f) {

				}

				try {
					gts.removeTrustLevel(level[i].getName(), "");
					fail("Non trust service administrators should not be able to remove a trust level!!!");
				} catch (PermissionDeniedFault f) {

				}

				try {
					gts.removeTrustLevel(level[i].getName(), user);
					fail("Non trust service administrators should not be able to remove a trust level!!!");
				} catch (PermissionDeniedFault f) {

				}
				gts.removeTrustLevel(level[i].getName(), ADMIN_USER);
				count = count - 1;
				assertEquals(count, gts.getTrustLevels().length);
				assertEquals(false, gts.doesTrustLevelExist(level[i].getName()));
			}
			assertEquals(0, gts.getTrustLevels().length);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		} finally {
			if (gts != null) {
				try {
					gts.destroy();
				} catch (Exception e) {
					FaultUtil.printFault(e);
				}
			}
		}
	}


	public void testUpdateTrustedAuthority() {
		GTS gts = null;
		try {
			GTSConfiguration conf = Utils.getGTSConfiguration();

			gts = new GTS(conf, GTS_URI);
			// Make sure we start fresh
			gts.destroy();
			String user = "O=Test Organization,OU=Test Unit,CN=User";
			PermissionBootstapper pb = new PermissionBootstapper(conf);
			pb.addAdminUser(ADMIN_USER);
			addTrustLevels(gts, ADMIN_USER);
			CA ca = new CA();
			BigInteger sn = new BigInteger(String.valueOf(System.currentTimeMillis()));
			CRLEntry entry = new CRLEntry(sn, CRLReason.PRIVILEGE_WITHDRAWN);
			ca.updateCRL(entry);
			TrustedAuthority ta = new TrustedAuthority();
			ta.setTrustedAuthorityName(ca.getCertificate().getSubjectDN().toString());
			ta.setCertificate(new X509Certificate(CertUtil.writeCertificate(ca.getCertificate())));
			ta.setCRL(new X509CRL(CertUtil.writeCRL(ca.getCRL())));
			ta.setStatus(Status.Trusted);
			ta.setTrustLevel(LEVEL_ONE);

			ta = gts.addTrustedAuthority(ta, ADMIN_USER);
			assertEquals(1, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);
			TrustedAuthority updated = gts.findTrustAuthorities(new TrustedAuthorityFilter())[0];
			assertEquals(ta, updated);
			CRLEntry crlE = new CRLEntry(new BigInteger(String.valueOf(System.currentTimeMillis())),
				CRLReason.PRIVILEGE_WITHDRAWN);
			ca.updateCRL(crlE);
			updated.setCRL(new X509CRL(CertUtil.writeCRL(ca.getCRL())));
			updated.setStatus(Status.Suspended);
			updated.setTrustLevel(LEVEL_TWO);

			// Test null
			try {
				gts.updateTrustedAuthority(updated, null);
				fail("Non trust service administrators should not be able to update a trust authority!!!");
			} catch (PermissionDeniedFault f) {

			}

			assertEquals(1, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);

			// Test Empty String
			try {
				gts.updateTrustedAuthority(updated, "");
				fail("Non trust service administrators should not be able to update a trust authority!!!");
			} catch (PermissionDeniedFault f) {

			}
			assertEquals(1, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);

			// Test User without any permissions
			try {
				gts.updateTrustedAuthority(updated, user);
				fail("Non trust service administrators should not be able to update a trust authority!!!");
			} catch (PermissionDeniedFault f) {

			}
			assertEquals(1, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);

			// Now create a permission for a user on the previous added trust
			// authority.
			Permission p = new Permission();
			p.setGridIdentity(user);
			p.setRole(Role.TrustAuthorityManager);
			p.setTrustedAuthorityName(ta.getTrustedAuthorityName());
			gts.addPermission(p, ADMIN_USER);

			// Check to make sure the permission was properly added
			PermissionFilter pf = permissionToPermissionFilter(p);
			assertEquals(1, gts.findPermissions(pf, ADMIN_USER).length);
			assertEquals(p, gts.findPermissions(pf, ADMIN_USER)[0]);

			try {
				gts.updateTrustedAuthority(updated, user);
				fail("Non trust service administrators should not be able to update a trust authority!!!");
			} catch (PermissionDeniedFault f) {

			}

			assertEquals(1, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);

			// Now give use Admin rights
			Permission admin = new Permission();
			admin.setGridIdentity(user);
			admin.setRole(Role.TrustServiceAdmin);

			gts.addPermission(admin, ADMIN_USER);
			assertEquals(1, gts.findPermissions(permissionToPermissionFilter(admin), ADMIN_USER).length);
			assertEquals(admin, gts.findPermissions(permissionToPermissionFilter(admin), ADMIN_USER)[0]);

			// Now that the user is admin try again
			gts.updateTrustedAuthority(updated, user);
			assertEquals(1, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);
			assertEquals(updated, gts.findTrustAuthorities(new TrustedAuthorityFilter())[0]);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			if (gts != null) {
				try {
					gts.destroy();
				} catch (Exception e) {
					FaultUtil.printFault(e);
				}
			}
		}
	}


	public void testRemoveTrustedAuthority() {
		GTS gts = null;
		try {
			GTSConfiguration conf = Utils.getGTSConfiguration();

			gts = new GTS(conf, GTS_URI);
			// Make sure we start fresh
			gts.destroy();
			String user = "O=Test Organization,OU=Test Unit,CN=User";
			PermissionBootstapper pb = new PermissionBootstapper(conf);
			pb.addAdminUser(ADMIN_USER);
			addTrustLevels(gts, ADMIN_USER);
			CA ca = new CA();
			BigInteger sn = new BigInteger(String.valueOf(System.currentTimeMillis()));
			CRLEntry entry = new CRLEntry(sn, CRLReason.PRIVILEGE_WITHDRAWN);
			ca.updateCRL(entry);
			TrustedAuthority ta = new TrustedAuthority();
			ta.setTrustedAuthorityName(ca.getCertificate().getSubjectDN().toString());
			ta.setCertificate(new X509Certificate(CertUtil.writeCertificate(ca.getCertificate())));
			ta.setCRL(new X509CRL(CertUtil.writeCRL(ca.getCRL())));
			ta.setStatus(Status.Trusted);
			ta.setTrustLevel(LEVEL_ONE);

			ta = gts.addTrustedAuthority(ta, ADMIN_USER);
			assertEquals(1, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);

			// Test null
			try {
				gts.removeTrustedAuthority(ta.getTrustedAuthorityName(), null);
				fail("Non trust service administrators should not be able to remove a trust authority!!!");
			} catch (PermissionDeniedFault f) {

			}

			assertEquals(1, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);

			// Test Empty String
			try {
				gts.removeTrustedAuthority(ta.getTrustedAuthorityName(), "");
				fail("Non trust service administrators should not be able to remove a trust authority!!!");
			} catch (PermissionDeniedFault f) {

			}
			assertEquals(1, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);

			// Test User without any permissions
			try {
				gts.removeTrustedAuthority(ta.getTrustedAuthorityName(), user);
				fail("Non trust service administrators should not be able to remove a trust authority!!!");
			} catch (PermissionDeniedFault f) {

			}
			assertEquals(1, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);

			// Now create a permission for a user on the previous added trust
			// authority.
			Permission p = new Permission();
			p.setGridIdentity(user);
			p.setRole(Role.TrustAuthorityManager);
			p.setTrustedAuthorityName(ta.getTrustedAuthorityName());
			gts.addPermission(p, ADMIN_USER);

			// Check to make sure the permission was properly added
			PermissionFilter pf = permissionToPermissionFilter(p);
			assertEquals(1, gts.findPermissions(pf, ADMIN_USER).length);
			assertEquals(p, gts.findPermissions(pf, ADMIN_USER)[0]);

			try {
				gts.removeTrustedAuthority(ta.getTrustedAuthorityName(), user);
				fail("Non trust service administrators should not be able to remove a trust authority!!!");
			} catch (PermissionDeniedFault f) {

			}

			assertEquals(1, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);

			// Now give use Admin rights
			Permission admin = new Permission();
			admin.setGridIdentity(user);
			admin.setRole(Role.TrustServiceAdmin);

			gts.addPermission(admin, ADMIN_USER);
			assertEquals(1, gts.findPermissions(permissionToPermissionFilter(admin), ADMIN_USER).length);
			assertEquals(admin, gts.findPermissions(permissionToPermissionFilter(admin), ADMIN_USER)[0]);

			// Now that the user is admin try again
			gts.removeTrustedAuthority(ta.getTrustedAuthorityName(), user);
			assertEquals(0, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			if (gts != null) {
				try {
					gts.destroy();
				} catch (Exception e) {
					FaultUtil.printFault(e);
				}
			}
		}
	}


	public void testSyncTrustedAuthoritiesOverlappingGTSAuthorities() {
		GTS gts = null;
		try {
			GTSConfiguration conf = Utils.getGTSConfiguration();
			gts = new GTS(conf, GTS_URI);
			// Make sure we start fresh
			gts.destroy();

			// Add the admin user
			PermissionBootstapper pb = new PermissionBootstapper(conf);
			pb.addAdminUser(ADMIN_USER);
			addTrustLevels(gts, ADMIN_USER);

			// Create Authorities
			AuthorityGTS auth1 = getAuthority(GTS_URI + " Authority 1", 1);
			gts.addAuthority(auth1, ADMIN_USER);
			AuthorityGTS auth2 = getAuthority(GTS_URI + " Authority 2", 2);
			gts.addAuthority(auth2, ADMIN_USER);
			AuthorityGTS[] list = gts.getAuthorities();
			assertEquals(2, list.length);
			assertEquals(auth1, list[0]);
			assertEquals(auth2, list[1]);

			/*
			int taCount = 11;
			TrustedAuthority[] ta = new TrustedAuthority[taCount];

			for (int j = 0; j < taCount; j++) {
				ta[j] = getTrustedAuthority();
				gts.addTrustedAuthority(ta[j], ADMIN_USER);
				TrustedAuthorityFilter f = new TrustedAuthorityFilter();
				assertEquals((j + 1), gts.findTrustAuthorities(f).length);
				f.setTrustedAuthorityName(ta[j].getTrustedAuthorityName());
				assertEquals(1, gts.findTrustAuthorities(f).length);
				assertEquals(ta[j], gts.findTrustAuthorities(f)[0]);
			}
			// Now Add Trusted Authorities for local GTS
			TrustedAuthority[] local = new TrustedAuthority[3];
			local[0] = ta[0];
			*/
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			if (gts != null) {
				try {
					gts.destroy();
				} catch (Exception e) {
					FaultUtil.printFault(e);
				}
			}
		}

	}


	public void testSyncTrustedAuthoritiesWithSingleAuthorityGTS() {
		GTS gts = null;
		try {
			GTSConfiguration conf = Utils.getGTSConfiguration();
			gts = new GTS(conf, GTS_URI);
			// Make sure we start fresh
			gts.destroy();

			// Add the admin user
			PermissionBootstapper pb = new PermissionBootstapper(conf);
			pb.addAdminUser(ADMIN_USER);

			// Add
			addTrustLevels(gts, ADMIN_USER);

			// Now we add an authority
			String authName = GTS_URI + " Authority";
			TrustedAuthorityTimeToLive ttl = new TrustedAuthorityTimeToLive();
			ttl.setHours(0);
			ttl.setMinutes(0);
			ttl.setSeconds(4);
			AuthorityGTS auth = getAuthority(authName, 1, ttl);
			gts.addAuthority(auth, ADMIN_USER);
			assertEquals(1, gts.getAuthorities().length);
			assertEquals(auth, gts.getAuthorities()[0]);

			int taCount = 2;
			TrustedAuthority[] ta = new TrustedAuthority[taCount];
			for (int j = 0; j < taCount; j++) {
				ta[j] = getTrustedAuthority();
				gts.addTrustedAuthority(ta[j], ADMIN_USER);
				TrustedAuthorityFilter f = new TrustedAuthorityFilter();
				assertEquals((j + 1), gts.findTrustAuthorities(f).length);
				f.setTrustedAuthorityName(ta[j].getTrustedAuthorityName());
				assertEquals(1, gts.findTrustAuthorities(f).length);
				assertEquals(ta[j], gts.findTrustAuthorities(f)[0]);
			}

			int remoteTaCount = 4;
			TrustedAuthority[] remoteta = new TrustedAuthority[remoteTaCount];
			for (int j = 0; j < remoteTaCount; j++) {
				remoteta[j] = getTrustedAuthority();
				remoteta[j].setIsAuthority(Boolean.FALSE);
				remoteta[j].setAuthorityTrustService(authName);
			}

			gts.synchronizeTrustedAuthorities(authName, remoteta);

			Thread.sleep(4100);

			// Test After Expiration
			TrustedAuthorityFilter f1 = new TrustedAuthorityFilter();
			assertEquals(taCount + remoteTaCount, gts.findTrustAuthorities(f1).length);
			f1.setLifetime(Lifetime.Valid);
			f1.setStatus(Status.Trusted);
			assertEquals(taCount, gts.findTrustAuthorities(f1).length);
			f1.setAuthorityTrustService(GTS_URI);
			assertEquals(taCount, gts.findTrustAuthorities(f1).length);

			ttl.setHours(5);
			auth.setTrustedAuthorityTimeToLive(ttl);
			gts.updateAuthority(auth, ADMIN_USER);

			gts.synchronizeTrustedAuthorities(authName, remoteta);

			// Test After Resync and after Longer Expiration
			TrustedAuthorityFilter f2 = new TrustedAuthorityFilter();
			f2.setLifetime(Lifetime.Valid);
			f2.setStatus(Status.Trusted);
			assertEquals(taCount + remoteTaCount, gts.findTrustAuthorities(f2).length);
			f2.setAuthorityTrustService(GTS_URI);
			assertEquals(taCount, gts.findTrustAuthorities(f2).length);
			f2.setAuthorityTrustService(authName);
			assertEquals(remoteTaCount, gts.findTrustAuthorities(f2).length);
			f2.setSourceTrustService(authName);
			assertEquals(remoteTaCount, gts.findTrustAuthorities(f2).length);

			// Test after resync after delete
			int remoteTaCount2 = 2;
			TrustedAuthority[] remoteta2 = new TrustedAuthority[remoteTaCount2];
			for (int j = 0; j < remoteTaCount2; j++) {
				remoteta2[j] = remoteta[j];
			}

			gts.synchronizeTrustedAuthorities(authName, remoteta2);

			// Test After Resync and after Longer Expiration
			TrustedAuthorityFilter f3 = new TrustedAuthorityFilter();
			assertEquals(taCount + remoteTaCount2, gts.findTrustAuthorities(f3).length);
			f3.setLifetime(Lifetime.Valid);
			f3.setStatus(Status.Trusted);
			assertEquals(taCount + remoteTaCount2, gts.findTrustAuthorities(f3).length);
			f3.setAuthorityTrustService(GTS_URI);
			assertEquals(taCount, gts.findTrustAuthorities(f3).length);
			f3.setAuthorityTrustService(authName);
			assertEquals(remoteTaCount2, gts.findTrustAuthorities(f3).length);
			f3.setSourceTrustService(authName);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			if (gts != null) {
				try {
					gts.destroy();
				} catch (Exception e) {
					FaultUtil.printFault(e);
				}
			}
		}
	}


	private TrustedAuthority getTrustedAuthority() throws Exception {
		cacount = cacount + 1;
		String dn = dnPrefix + cacount;
		CA ca = new CA(dn);
		Calendar c = new GregorianCalendar();
		c.add(Calendar.HOUR, 1);
		String name = ca.getCertificate().getSubjectDN().toString();
		BigInteger sn = new BigInteger(String.valueOf(System.currentTimeMillis()));
		CRLEntry entry = new CRLEntry(sn, CRLReason.PRIVILEGE_WITHDRAWN);
		ca.updateCRL(entry);
		TrustedAuthority ta = new TrustedAuthority();
		ta.setTrustedAuthorityName(name);
		ta.setCertificate(new X509Certificate(CertUtil.writeCertificate(ca.getCertificate())));
		ta.setCRL(new X509CRL(CertUtil.writeCRL(ca.getCRL())));
		ta.setStatus(Status.Trusted);
		ta.setTrustLevel(LEVEL_ONE);
		ta.setIsAuthority(Boolean.TRUE);
		return ta;
	}


	private void addTrustLevels(GTS gts, String gridId) throws Exception {
		TrustLevel l1 = new TrustLevel();
		l1.setName(LEVEL_ONE);
		TrustLevel l2 = new TrustLevel();
		l2.setName(LEVEL_TWO);
		gts.addTrustLevel(l1, gridId);
		gts.addTrustLevel(l2, gridId);
	}


	private PermissionFilter permissionToPermissionFilter(Permission p) {
		PermissionFilter pf = new PermissionFilter();
		pf.setGridIdentity(p.getGridIdentity());
		pf.setRole(p.getRole());
		pf.setTrustedAuthorityName(p.getTrustedAuthorityName());
		return pf;
	}


	private AuthorityGTS getAuthority(String uri, int priority) {
		TrustedAuthorityTimeToLive ttl = new TrustedAuthorityTimeToLive();
		ttl.setHours(1);
		ttl.setMinutes(1);
		ttl.setSeconds(1);
		return getAuthority(uri, priority, ttl);
	}


	private AuthorityGTS getAuthority(String uri, int priority, TrustedAuthorityTimeToLive ttl) {
		AuthorityGTS a1 = new AuthorityGTS();
		a1.setServiceURI(uri);
		a1.setPriority(priority);
		a1.setPerformAuthorization(true);
		a1.setServiceIdentity(uri);
		a1.setSyncTrustLevels(true);
		a1.setTrustedAuthorityTimeToLive(ttl);
		return a1;
	}


	private void updateAuthority(AuthorityGTS gts) {
		TrustedAuthorityTimeToLive ttl = new TrustedAuthorityTimeToLive();
		ttl.setHours(10);
		ttl.setMinutes(10);
		ttl.setSeconds(10);
		gts.setPerformAuthorization(false);
		gts.setServiceIdentity(null);
		gts.setSyncTrustLevels(false);
		gts.setTrustedAuthorityTimeToLive(ttl);
	}


	protected void setUp() throws Exception {
		super.setUp();
		cacount = 0;
	}

}
