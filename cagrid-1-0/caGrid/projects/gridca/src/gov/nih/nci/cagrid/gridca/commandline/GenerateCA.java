package gov.nih.nci.cagrid.gridca.commandline;

import gov.nih.nci.cagrid.common.IOUtils;
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
public class GenerateCA {

	public static void main(String[] args) {
		try {
			Security
					.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			StringBuffer sb = new StringBuffer();
			String o = null;
			while ((o == null) || (o.trim().length() == 0)) {
				o = IOUtils.readLine("Enter Organization (O)");
			}
			sb.append("O=").append(o);
			String ou = null;
			int count = 1;
			while (true) {
				ou = IOUtils
						.readLine("Enter Organizational Unit (OU) " + count);
				count++;
				if ((ou == null) || (ou.trim().length() == 0)) {
					break;
				} else {
					sb.append(",OU=" + ou);
				}

			}

			String cn = null;
			while ((cn == null) || (cn.trim().length() == 0)) {
				cn = IOUtils.readLine("Enter Common Name (CN)");
			}
			sb.append(",CN=" + cn);
			KeyPair root = KeyUtil.generateRSAKeyPair1024("BC");
			int days = IOUtils.readInteger("Enter number of days valid");
			while (days <= 0) {
				days = IOUtils.readInteger("Enter days valid (>0)");
			}
			GregorianCalendar date = new GregorianCalendar(TimeZone
					.getTimeZone("GMT"));

			date.add(Calendar.MINUTE, -5);

			Date start = new Date(date.getTimeInMillis());
			date.add(Calendar.MINUTE, 5);
			date.add(Calendar.DAY_OF_MONTH, days);
			Date end = new Date(date.getTimeInMillis());
			X509Certificate cert = CertUtil.generateCACertificate(new X509Name(
					sb.toString()), start, end, root);
			String password = IOUtils.readLine("Enter a key password");
			password = Utils.clean(password);
			String keyOut = IOUtils.readLine("Enter location to write CA key");
			String caOut = IOUtils.readLine("Enter location to write CA cert");
			KeyUtil.writePrivateKey(root.getPrivate(), new File(keyOut),
					password);
			CertUtil.writeCertificate(cert, new File(caOut));
			System.out.println("Successfully create the CA certificate:");
			System.out.println(sb.toString());
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