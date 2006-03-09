package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gridca.common.CRLEntry;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gts.bean.Status;
import gov.nih.nci.cagrid.gts.bean.TrustLevel;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.bean.X509CRL;
import gov.nih.nci.cagrid.gts.bean.X509Certificate;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.IllegalTrustedAuthorityFault;
import gov.nih.nci.cagrid.gts.test.CA;
import gov.nih.nci.cagrid.gts.test.Utils;

import java.math.BigInteger;

import junit.framework.TestCase;

import org.bouncycastle.asn1.x509.CRLReason;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestTrustedAuthorityManager extends TestCase {

	private Database db;


	public void testCreateAndDestroy() {
		try {
			TrustedAuthorityManager trust = new TrustedAuthorityManager("localhost", db);
			trust.buildDatabase();
			trust.destroy();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
	
	public void testAddTrustedAuthorityWithCRL() {
		try {
			TrustedAuthorityManager trust = new TrustedAuthorityManager("localhost", db);
			CA ca = new CA();
			BigInteger sn = new BigInteger(String.valueOf(System.currentTimeMillis()));
			CRLEntry entry = new CRLEntry(sn,CRLReason.PRIVILEGE_WITHDRAWN);
			ca.updateCRL(entry);
			TrustedAuthority ta = new TrustedAuthority();
			ta.setTrustedAuthorityName(ca.getCertificate().getSubjectDN().toString());
			ta.setCertificate(new X509Certificate(CertUtil.writeCertificate(ca.getCertificate())));
			ta.setCRL(new X509CRL(CertUtil.writeCRL(ca.getCRL())));
			ta.setIsAuthority(true);
			ta.setStatus(Status.Trusted);
			ta.setTrustLevel(TrustLevel.Five);		
			trust.addTrustedAuthority(ta);
			assertEquals(ta,trust.getTrustedAuthority(ta.getTrustedAuthorityId()));
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}
	
	public void testAddTrustedAuthorityWithInvalidCRL() {
		try {
			TrustedAuthorityManager trust = new TrustedAuthorityManager("localhost", db);
			CA ca = new CA();
			CA ca2 = new CA();
			BigInteger sn = new BigInteger(String.valueOf(System.currentTimeMillis()));
			CRLEntry entry = new CRLEntry(sn,CRLReason.PRIVILEGE_WITHDRAWN);
			ca2.updateCRL(entry);
			try{
			TrustedAuthority ta = new TrustedAuthority();
			ta.setTrustedAuthorityName(ca.getCertificate().getSubjectDN().toString());
			ta.setCertificate(new X509Certificate(CertUtil.writeCertificate(ca.getCertificate())));
			ta.setCRL(new X509CRL(CertUtil.writeCRL(ca2.getCRL())));
			ta.setIsAuthority(true);
			ta.setStatus(Status.Trusted);
			ta.setTrustLevel(TrustLevel.Five);		
			trust.addTrustedAuthority(ta);
			fail("Did not generate error when an invalidly signed CRL was provided.");
			}catch(IllegalTrustedAuthorityFault f){
				
			}
			
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}


	public void testAddTrustedAuthorityNoCRL() {
		try {
			TrustedAuthorityManager trust = new TrustedAuthorityManager("localhost", db);
			CA ca = new CA();
			TrustedAuthority ta = new TrustedAuthority();
			ta.setTrustedAuthorityName(ca.getCertificate().getSubjectDN().toString());
			ta.setCertificate(new X509Certificate(CertUtil.writeCertificate(ca.getCertificate())));
			ta.setIsAuthority(true);
			ta.setStatus(Status.Trusted);
			ta.setTrustLevel(TrustLevel.Five);
			trust.addTrustedAuthority(ta);
			assertEquals(ta,trust.getTrustedAuthority(ta.getTrustedAuthorityId()));
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
