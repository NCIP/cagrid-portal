package org.cagrid.gaards.cds.common;

import gov.nih.nci.cagrid.gridca.common.CertUtil;

import java.security.cert.X509Certificate;
import java.util.Date;

public class Utils {

	public static Date getEarliestExpiration(X509Certificate[] certs) {
		Date earliestTime = null;
		for (int i = 0; i < certs.length; i++) {
			Date time = certs[i].getNotAfter();
			if (earliestTime == null || time.before(earliestTime)) {
				earliestTime = time;
			}
		}
		return earliestTime;
	}

	public static org.cagrid.gaards.cds.common.X509Certificate convertCertificate(
			X509Certificate cert) throws Exception {
		String str = CertUtil.writeCertificate(cert);
		org.cagrid.gaards.cds.common.X509Certificate x509 = new org.cagrid.gaards.cds.common.X509Certificate();
		x509.setCertificateAsString(str);
		return x509;
	}

	public static X509Certificate convertCertificate(
			org.cagrid.gaards.cds.common.X509Certificate cert) throws Exception {
		return CertUtil.loadCertificate(cert.getCertificateAsString());
	}

	public static CertificateChain toCertificateChain(X509Certificate[] certs)
			throws Exception {
		CertificateChain chain = new CertificateChain();
		if (certs != null) {
			org.cagrid.gaards.cds.common.X509Certificate[] x509 = new org.cagrid.gaards.cds.common.X509Certificate[certs.length];
			for (int i = 0; i < certs.length; i++) {
				x509[i] = convertCertificate(certs[i]);
			}
			chain.setX509Certificate(x509);
		}
		return chain;
	}

	public static X509Certificate[] toCertificateArray(CertificateChain chain)
			throws Exception {
		if (chain != null) {
			org.cagrid.gaards.cds.common.X509Certificate[] certs = chain
					.getX509Certificate();
			if (certs != null) {
				X509Certificate[] x509 = new X509Certificate[certs.length];
				for (int i = 0; i < certs.length; i++) {
					x509[i] = convertCertificate(certs[i]);
				}
				return x509;
			} else {
				return new X509Certificate[0];
			}

		} else {
			return new X509Certificate[0];
		}
	}

}
