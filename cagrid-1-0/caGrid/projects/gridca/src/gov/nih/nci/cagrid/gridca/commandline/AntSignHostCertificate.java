package gov.nih.nci.cagrid.gridca.commandline;

import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.io.File;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.bouncycastle.asn1.x509.X509Name;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class AntSignHostCertificate {

	public static void main(String[] args) {
		try {
			Security
					.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

			String key = args[0];
			String password = args[1];
			String cert = args[2];
			String pubKey = args[3];
			String daysValid = args[4];
			String caOut = args[5];
			PrivateKey cakey = KeyUtil.loadPrivateKey(new File(key), password);

			X509Certificate cacert = CertUtil.loadCertificate("BC", new File(
					cert));

			File keyFile = new File(pubKey);
			PublicKey publicKey = KeyUtil.loadPublicKey("BC", keyFile);
			int indexP = keyFile.getName().lastIndexOf("-public-key.pem");
			String hostname = keyFile.getName().substring(0, indexP);
			String rootSub = cacert.getSubjectDN().toString();
			int index = rootSub.lastIndexOf(",");
			String subject = rootSub.substring(0, index) + ",CN=host/"
					+ hostname;

			int days = Integer.valueOf(daysValid).intValue();
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

			X509Certificate userCert = CertUtil
					.generateCertificate(new X509Name(subject), start, end,
							publicKey, cacert, cakey,null);
			CertUtil.writeCertificate(userCert, new File(caOut));
			System.out.println("Successfully created the host certificate:");
			System.out.println(userCert.getSubjectDN().toString());
			System.out.println("Host certificate issued by:");
			System.out.println(cacert.getSubjectDN().toString());
			System.out.println("Host certificate valid till:");
			System.out.println(userCert.getNotAfter());
			System.out.println("Host certificate written to:");
			System.out.println(caOut);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}