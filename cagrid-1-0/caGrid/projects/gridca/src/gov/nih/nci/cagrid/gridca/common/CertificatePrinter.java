package gov.nih.nci.cagrid.gridca.common;

import java.io.File;
import java.security.cert.X509Certificate;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class CertificatePrinter {
	public static void main(String[] args) {
		try {
			X509Certificate cert = CertUtil.loadCertificate(new File("c:/certificates/usercert.pem"));
			System.out.println("Subject: " + cert.getSubjectDN());
			System.out.println("Created: " + cert.getNotBefore());
			System.out.println("Expires: " + cert.getNotAfter());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
