package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.gridca.common.CRLEntry;
import gov.nih.nci.cagrid.gts.test.CA;
import gov.nih.nci.cagrid.gts.test.Credential;

import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.bouncycastle.asn1.x509.CRLReason;
import org.globus.gsi.proxy.ProxyPathValidator;
import org.globus.gsi.proxy.ProxyPathValidatorException;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class TestProxyPathValidator extends TestCase {

	public void testProxyValidator() {
		try {
			CA caX = new CA();
			CA caY = new CA();
			String userA = "User A";
			String userB = "User B";
			Credential credX1 = caX.createIdentityCertificate(userA);
			Credential credX2 = caX.createIdentityCertificate(userB);
			Credential credY1 = caY.createIdentityCertificate(userA);

			CRLEntry credX2CRL = new CRLEntry(credX2.getCertificate().getSerialNumber(), CRLReason.PRIVILEGE_WITHDRAWN);
			caX.updateCRL(credX2CRL);

			ProxyPathValidator valid = new ProxyPathValidator();

			X509Certificate[] trusted1 = new X509Certificate[1];
			trusted1[0] = caX.getCertificate();

			Map crlMap = new HashMap();
			crlMap.put(caX.getCRL().getIssuerDN().getName(), caX.getCRL());
			// CertificateRevocationLists crls = new
			// CertificateRevocationLists();

			X509Certificate[] chainX1 = new X509Certificate[1];
			chainX1[0] = credX1.getCertificate();
			valid.validate(chainX1, trusted1);

			X509Certificate[] chainX2 = new X509Certificate[1];
			chainX2[0] = credX2.getCertificate();
			valid.validate(chainX2, trusted1);

			try {
				X509Certificate[] chainY1 = new X509Certificate[1];
				chainY1[0] = credY1.getCertificate();
				valid.validate(chainY1, trusted1);
				fail("Should not be able to validate certificate!!!");
			} catch (ProxyPathValidatorException ex) {

			}

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
