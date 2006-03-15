package gov.nih.nci.cagrid.gts.service;

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
import gov.nih.nci.cagrid.gts.stubs.IllegalPermissionFault;
import gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault;
import gov.nih.nci.cagrid.gts.test.CA;
import gov.nih.nci.cagrid.gts.test.Utils;
import gov.nih.nci.cagrid.gts.tools.service.PermissionBootstapper;

import java.math.BigInteger;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

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


	public void testCreateAndDestroy() {
		try {
			GTS gts = new GTS(Utils.getGTSConfiguration(), "localhost");
			// Make sure we start fresh
			gts.destroy();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testAddFindRevokePermission() {
		try {
			GTSConfiguration conf = Utils.getGTSConfiguration();
			String user = "O=Test Organization,OU=Test Unit,CN=User";
			String user2 = "O=Test Organization,OU=Test Unit,CN=User2";
			GTS gts = new GTS(conf, "localhost");
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

			CA ca = new CA();
			TrustedAuthority ta = new TrustedAuthority();
			ta.setTrustedAuthorityName(ca.getCertificate().getSubjectDN().toString());
			ta.setCertificate(new X509Certificate(CertUtil.writeCertificate(ca.getCertificate())));
			ta.setStatus(Status.Trusted);
			ta.setTrustLevel(TrustLevel.Five);

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
			assertEquals(1,gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);
			assertEquals(ta,gts.findTrustAuthorities(new TrustedAuthorityFilter())[0]);
			gts.addPermission(userPerm, ADMIN_USER);
			assertEquals(1,gts.findPermissions(permissionToPermissionFilter(userPerm),ADMIN_USER).length);
			assertEquals(userPerm,gts.findPermissions(permissionToPermissionFilter(userPerm),ADMIN_USER)[0]);
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
			
			//Now give use Admin rights
			Permission admin = new Permission();
			admin.setGridIdentity(user);
			admin.setRole(Role.TrustServiceAdmin);
			
			gts.addPermission(admin, ADMIN_USER);
			assertEquals(1,gts.findPermissions(permissionToPermissionFilter( admin),ADMIN_USER).length);
			assertEquals(admin,gts.findPermissions(permissionToPermissionFilter( admin),ADMIN_USER)[0]);
			
			//Now that the user is admin try again
			gts.addPermission(p, user);
			assertEquals(1,gts.findPermissions(permissionToPermissionFilter(p),user).length);
			assertEquals(p,gts.findPermissions(permissionToPermissionFilter(p),user)[0]);
			gts.revokePermission(p,user);
			assertEquals(0,gts.findPermissions(permissionToPermissionFilter(p),user).length);
			
			//Now Revoke the user's admin rights and try again
			gts.revokePermission(admin,ADMIN_USER);
			assertEquals(0,gts.findPermissions(permissionToPermissionFilter( admin),ADMIN_USER).length);
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
			
			gts.destroy();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testAddTrustedAuthority() {
		try {
			GTSConfiguration conf = Utils.getGTSConfiguration();

			GTS gts = new GTS(conf, "localhost");
			// Make sure we start fresh
			gts.destroy();
			String user = "O=Test Organization,OU=Test Unit,CN=User";
			PermissionBootstapper pb = new PermissionBootstapper(conf);
			pb.addAdminUser(ADMIN_USER);
			CA ca = new CA();
			BigInteger sn = new BigInteger(String.valueOf(System.currentTimeMillis()));
			CRLEntry entry = new CRLEntry(sn, CRLReason.PRIVILEGE_WITHDRAWN);
			ca.updateCRL(entry);
			TrustedAuthority ta = new TrustedAuthority();
			ta.setTrustedAuthorityName(ca.getCertificate().getSubjectDN().toString());
			ta.setCertificate(new X509Certificate(CertUtil.writeCertificate(ca.getCertificate())));
			ta.setCRL(new X509CRL(CertUtil.writeCRL(ca.getCRL())));
			ta.setStatus(Status.Trusted);
			ta.setTrustLevel(TrustLevel.Five);

			// Test null
			try {
				gts.addTrustedAuthority(ta, null);
				fail("Non trust service administrators should not be able to add a trust authority!!!");
			} catch (PermissionDeniedFault f) {

			}
			assertEquals(0, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);

			// Test Empty String
			try {
				gts.addTrustedAuthority(ta, "");
				fail("Non trust service administrators should not be able to add a trust authority!!!");
			} catch (PermissionDeniedFault f) {

			}
			assertEquals(0, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);

			// Test User without any permissions
			try {
				gts.addTrustedAuthority(ta, user);
				fail("Non trust service administrators should not be able to add a trust authority!!!");
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
			ta2.setTrustLevel(TrustLevel.Five);

			try {
				gts.addTrustedAuthority(ta, user);
				fail("Non trust service administrators should not be able to add a trust authority!!!");
			} catch (PermissionDeniedFault f) {

			}

			assertEquals(1, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);

			gts.destroy();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	private PermissionFilter permissionToPermissionFilter(Permission p) {
		PermissionFilter pf = new PermissionFilter();
		pf.setGridIdentity(p.getGridIdentity());
		pf.setRole(p.getRole());
		pf.setTrustedAuthorityName(p.getTrustedAuthorityName());
		return pf;
	}

}
