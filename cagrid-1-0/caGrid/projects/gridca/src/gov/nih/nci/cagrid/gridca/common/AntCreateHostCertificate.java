package gov.nih.nci.cagrid.gridca.common;

import java.io.File;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.bouncycastle.jce.PKCS10CertificationRequest;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class AntCreateHostCertificate {

	public static void main(String[] args) {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			String key = args[0];
			String password = args[1];
			String cert = args[2];
			String cn = args[3];
			String daysValid = args[4];
			String keyOut = args[5];
			String certOut = args[6];

			int days = Integer.valueOf(daysValid).intValue();
			while (days <= 0) {
				System.err.println("Days Valid must be >0");
				System.exit(1);
			}
			PrivateKey cakey = KeyUtil.loadPrivateKey(new File(key), password);

			X509Certificate cacert = CertUtil.loadCertificate(new File(cert));

			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			String rootSub = cacert.getSubjectDN().toString();
			int index = rootSub.lastIndexOf(",");
			String subject = rootSub.substring(0, index) + ",CN=host/" + cn;
			PKCS10CertificationRequest request = CertUtil.generateCertficateRequest(subject, pair);

			GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
			/* Allow for a five minute clock skew here. */
			date.add(Calendar.MINUTE, -5);
			Date start = new Date(date.getTimeInMillis());
			Date end = null;
			/* If hours = 0, then cert lifetime is set to user cert */
			if (days <= 0) {
				end = cacert.getNotAfter();
			} else {
				date.add(Calendar.MINUTE, 5);
				date.add(Calendar.DAY_OF_MONTH, days);
				Date d = new Date(date.getTimeInMillis());
				if (cacert.getNotAfter().before(d)) {
					throw new GeneralSecurityException(
						"Cannot create a certificate that expires after issuing certificate.");
				}
				end = d;
			}
			X509Certificate userCert = CertUtil.signCertificateRequest(request, start, end, cacert, cakey);

			KeyUtil.writePrivateKey(pair.getPrivate(), new File(keyOut));
			CertUtil.writeCertificate(userCert, new File(certOut));
			System.out.println("Successfully create the user certificate:");
			System.out.println(userCert.getSubjectDN().toString());
			System.out.println("User certificate issued by:");
			System.out.println(cacert.getSubjectDN().toString());
			System.out.println("User Certificate Valid Till:");
			System.out.println(userCert.getNotAfter());
			System.out.println("User Private Key Written to:");
			System.out.println(keyOut);
			System.out.println("User Certificate Written to:");
			System.out.println(certOut);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}