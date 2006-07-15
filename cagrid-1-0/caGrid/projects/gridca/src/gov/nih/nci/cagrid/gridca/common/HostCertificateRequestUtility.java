package gov.nih.nci.cagrid.gridca.common;

import gov.nih.nci.cagrid.common.IOUtils;
import gov.nih.nci.cagrid.common.Utils;

import java.io.File;
import java.security.KeyPair;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class HostCertificateRequestUtility {

	public static void main(String[] args) {
		try {
			String caAdminEmail = "langella@bmi.osu.edu";
			String emailSubject = "caGrid 1.0 Beta Host Certificate Request";
			String days = "5";
			boolean manual = true;
			System.out.println("*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*");
			System.out.println("*            GridCA Host Certificate Request Utility          *");
			System.out.println("*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*");
			System.out.println();
			// Manual Vs Automatic
			// Directions
			File dir = new File(Utils.getCaGridUserHome().getAbsolutePath() + File.separator + "gridca");
			dir.mkdirs();
			String hostname = null;
			while (true) {
				hostname = IOUtils.readLine("Enter Hostname", false);
				if (!validateHostname(hostname)) {
					System.err.println("Invalid Hostname!!!");
				} else {
					break;
				}
			}
			File publicKey = new File(dir.getAbsolutePath() + File.separator + hostname + "-public-key.pem");
			File privateKey = new File(dir.getAbsolutePath() + File.separator + hostname + "-private-key.pem");
			File cert = new File(dir.getAbsolutePath() + File.separator + hostname + "-cert.pem");
			File des = new File(dir.getAbsolutePath() + File.separator + hostname + "-security-descriptor.xml");

			if (publicKey.exists() || privateKey.exists() || cert.exists()) {
				while (true) {
					System.out.println("A key pair for the host " + hostname + " already exists!!!");
					String cmd = IOUtils.readLine("Do you wish to overwite? (Y or N):", false);
					if ((cmd != null) && (cmd.equalsIgnoreCase("Y"))) {
						publicKey.delete();
						privateKey.delete();
						cert.delete();
						des.delete();
						break;
					} else if ((cmd != null) && (cmd.equalsIgnoreCase("N"))) {
						System.exit(0);
					}
				}
			}
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			KeyUtil.writePrivateKey(pair.getPrivate(), privateKey);
			KeyUtil.writePublicKey(pair.getPublic(), publicKey);
			StringBuffer sb = new StringBuffer();
			sb.append("<securityConfig xmlns=\"http://www.globus.org\">");
			sb.append("	<credential>");
			sb.append("		<key-file value=\"" + privateKey.getAbsolutePath() + "\"/>");
			sb.append("  	<cert-file value=\"" + cert.getAbsolutePath() + "\"/>");
			sb.append("  </credential>");
			sb.append("</securityConfig>");
			Utils.stringBufferToFile(sb, des.getAbsolutePath());
			System.out.println("Wrote the public key to: " + publicKey.getAbsolutePath());
			System.out.println("Wrote the private key to: " + privateKey.getAbsolutePath());
			System.out.println("Wrote the security descriptor to: " + des.getAbsolutePath());
			System.out.println();
			System.out.println("*** Directions ***");
			System.out.println();
			if (manual) {
				File f = new File(dir.getAbsolutePath() + File.separator + hostname + "-directions.txt");
				f.delete();
				String directions = "To request a host certificate, email your CA administrator at "
					+ caAdminEmail+".\n" +"Please set the subject of the email to: "
					+ emailSubject
					+ "\nand in the body of the email please specify you reason for needing a certificate.\n"+
					"It is also required that the public key you just generated is attached to the email.\n"+
					"The public key you need to attach to the email can be found at: \n\n	"
					+ publicKey.getAbsolutePath()
					+ "\n\nYou should receive an email response to your certificate request within\n"
					+ days
					+ " business day(s).  If your certificate request is approved, your certificate\n"+
					"will come attached in the email response.  Please save the attached certificate to:\n\n	"
					+ cert.getAbsolutePath()
					+ "\n\nTo use these credentials for your globus container, type the following command from your GLOBUS_LOCATION:\n\n bin/globus-start-container -containerDesc "
					+ des.getAbsolutePath() + "\n\nThese directions have been written to the file:\n "
					+ f.getAbsolutePath();
				System.out.println(directions);
				StringBuffer sb2 = new StringBuffer(directions);
				Utils.stringBufferToFile(sb2, f.getAbsolutePath());
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}


	private static boolean validateHostname(String hostname) {
		if (Utils.clean(hostname) == null) {
			return false;
		} else if (hostname.indexOf(" ") >= 0) {
			return false;
		} else {
			return true;
		}
	}
}