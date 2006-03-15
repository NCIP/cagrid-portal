package gov.nih.nci.cagrid.gts.service;

import java.math.BigInteger;

import org.bouncycastle.asn1.x509.CRLReason;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gridca.common.CRLEntry;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gts.bean.Status;
import gov.nih.nci.cagrid.gts.bean.TrustLevel;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter;
import gov.nih.nci.cagrid.gts.bean.X509CRL;
import gov.nih.nci.cagrid.gts.bean.X509Certificate;
import gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault;
import gov.nih.nci.cagrid.gts.test.CA;
import gov.nih.nci.cagrid.gts.test.Utils;
import gov.nih.nci.cagrid.gts.tools.service.PermissionBootstapper;
import junit.framework.TestCase;


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


	public void testAddTrustedAuthority() {
		try {
			GTSConfiguration conf = Utils.getGTSConfiguration();

			GTS gts = new GTS(conf, "localhost");
			// Make sure we start fresh
			gts.destroy();

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
			gts.addTrustedAuthority(ta, ADMIN_USER);
			assertEquals(1, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);
			assertEquals(ta, gts.findTrustAuthorities(new TrustedAuthorityFilter())[0]);
			gts.destroy();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testAddTrustedAuthorityInvalidPermissions() {
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
			
			//Test Empty String
			try {
				gts.addTrustedAuthority(ta, "");
				fail("Non trust service administrators should not be able to add a trust authority!!!");
			} catch (PermissionDeniedFault f) {

			}
			assertEquals(0, gts.findTrustAuthorities(new TrustedAuthorityFilter()).length);

			//Test User without any permissions
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
			gts.destroy();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
