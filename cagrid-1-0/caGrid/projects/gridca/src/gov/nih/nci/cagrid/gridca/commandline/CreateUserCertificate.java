package gov.nih.nci.cagrid.gridca.commandline;

import gov.nih.nci.cagrid.common.IOUtils;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

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
public class CreateUserCertificate {

	public static void main(String[] args) {
		try {
			Security
					.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

			PrivateKey cakey = null;
			while (true) {
				try {
					String key = IOUtils
							.readLine("Enter CA key location", true);
					String password = IOUtils
							.readLine("Enter CA key password.");
					cakey = KeyUtil.loadPrivateKey(new File(key), password);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			X509Certificate cacert = null;

			while (true) {
				try {
					String cert = IOUtils.readLine(
							"Enter CA certificate location", true);
					cacert = CertUtil.loadCertificate("BC", new File(cert));
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			String cn = IOUtils.readLine("Enter Common Name (CN)", true);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024("BC");
			String rootSub = cacert.getSubjectDN().toString();
			int index = rootSub.lastIndexOf(",");
			String subject = rootSub.substring(0, index) + ",CN=" + cn;
			PKCS10CertificationRequest request = CertUtil
					.generateCertficateRequest(subject, pair);

			int days = IOUtils.readInteger("Enter number of days valid");
			GregorianCalendar date = new GregorianCalendar(TimeZone
					.getTimeZone("GMT"));
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
			X509Certificate userCert = CertUtil.signCertificateRequest(request,
					start, end, cacert, cakey,null);

			String keyOut = IOUtils
					.readLine("Enter location to write user key");
			String caOut = IOUtils
					.readLine("Enter location to write user cert");
			KeyUtil.writePrivateKey(pair.getPrivate(), new File(keyOut));
			CertUtil.writeCertificate(userCert, new File(caOut));
			System.out.println("Successfully create the user certificate:");
			System.out.println(userCert.getSubjectDN().toString());
			System.out.println("User certificate issued by:");
			System.out.println(cacert.getSubjectDN().toString());
			System.out.println("User Certificate Valid Till:");
			System.out.println(userCert.getNotAfter());
			System.out.println("User Private Key Written to:");
			System.out.println(keyOut);
			System.out.println("User Certificate Written to:");
			System.out.println(caOut);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}