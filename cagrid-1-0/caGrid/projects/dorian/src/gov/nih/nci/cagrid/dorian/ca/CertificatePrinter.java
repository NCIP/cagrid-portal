package gov.nih.nci.cagrid.gums.ca;

import java.security.cert.X509Certificate;

public class CertificatePrinter {
	public static void main(String[] args) {
		try {
			X509Certificate cert = CertUtil
					.loadCertificate("c:/certificates/usercert.pem");
		System.out.println("Subject: "+cert.getSubjectDN());
		System.out.println("Created: "+cert.getNotBefore());
		System.out.println("Expires: "+cert.getNotAfter());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
