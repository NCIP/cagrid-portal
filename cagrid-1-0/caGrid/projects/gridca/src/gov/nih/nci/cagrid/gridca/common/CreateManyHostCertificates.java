package gov.nih.nci.cagrid.gridca.common;

import java.io.File;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.bouncycastle.jce.PKCS10CertificationRequest;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class CreateManyHostCertificates {

	public static void main(String[] args) {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

			String location = "C:\\certificates\\osu-cagrid-service-ca\\";
			int days = 1820;
			String keyEnd = "-key.pem";
			String certEnd = "-cert.pem";

			String key = location + "osu-cagrid-service-cakey.pem";
			String password = "gomets123";
			PrivateKey cakey = KeyUtil.loadPrivateKey(new File(key), password);

			String cert = location + "osu-cagrid-service-cacert.pem";
			X509Certificate cacert = CertUtil.loadCertificate(new File(cert));
			
			List hostList = new ArrayList();
			//hostList.add("cagrid01.bmi.ohio-state.edu");
			//hostList.add("cagrid02.bmi.ohio-state.edu");
			//hostList.add("cagrid03.bmi.ohio-state.edu");
			//hostList.add("cagrid04.bmi.ohio-state.edu");
			//hostList.add("cagrid05.bmi.ohio-state.edu");
			hostList.add("ccis2005.duhs.duke.edu");
			hostList.add("cagrid1.duhs.duke.edu");
			hostList.add("cagrid2.duhs.duke.edu");
			hostList.add("catrip1.duhs.duke.edu");
			hostList.add("mendel.duhs.duke.edu");

			for (int i = 0; i <hostList.size(); i++) {
				String cn = (String)hostList.get(i);
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

				String keyOut = location + cn + keyEnd;
				String caOut = location + cn + certEnd;
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
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}
}