package gov.nih.nci.cagrid.gridca.commandline;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.io.File;
import java.security.KeyPair;
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
public class AntGenerateCA {

	public static void main(String[] args) {
		try {
			Security
					.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			String dn = args[0];
			String daysValid = args[1];
			String password = args[2];
			String keyOut = args[3];
			String caOut = args[4];

			KeyPair root = KeyUtil.generateRSAKeyPair2048("BC");
			int days = Integer.valueOf(daysValid).intValue();
			while (days <= 0) {
				System.err.println("Days Valid must be >0");
				System.exit(1);
			}
			GregorianCalendar date = new GregorianCalendar(TimeZone
					.getTimeZone("GMT"));

			date.add(Calendar.MINUTE, -5);

			Date start = new Date(date.getTimeInMillis());
			date.add(Calendar.MINUTE, 5);
			date.add(Calendar.DAY_OF_MONTH, days);
			Date end = new Date(date.getTimeInMillis());
			X509Certificate cert = CertUtil.generateCACertificate(new X509Name(
					dn), start, end, root);

			password = Utils.clean(password);
			KeyUtil.writePrivateKey(root.getPrivate(), new File(keyOut),
					password);
			CertUtil.writeCertificate(cert, new File(caOut));
			System.out.println("Successfully create the CA certificate:");
			System.out.println(dn);
			System.out.println("CA Certificate Valid Till:");
			System.out.println(cert.getNotAfter());
			System.out.println("CA Private Key Written to:");
			System.out.println(keyOut);
			System.out.println("CA Certificate Written to:");
			System.out.println(caOut);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}