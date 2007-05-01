package gov.nih.nci.cagrid.gridca.common;

import gov.nih.nci.cagrid.common.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
			String caAdminEmail = args[0];
			String emailSubject = args[1];
			String days = args[2];
			String hostname = args[3];
			String firstName = args[4];
			String lastName = args[5];
			String organization = args[6];
			String project = args[7];
			String phoneNumber = args[8];
			String reason = args[9];

			File dir = new File(Utils.getCaGridUserHome().getAbsolutePath() + File.separator + "gridca");
			dir.mkdirs();

			File publicKey = new File(dir.getAbsolutePath() + File.separator + hostname + "-public-key.pem");
			File privateKey = new File(dir.getAbsolutePath() + File.separator + hostname + "-private-key.pem");
			File cert = new File(dir.getAbsolutePath() + File.separator + hostname + "-cert.pem");
			File des = new File(dir.getAbsolutePath() + File.separator + hostname + "-security-descriptor.xml");

			publicKey.delete();
			privateKey.delete();
			cert.delete();
			des.delete();

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

			File f = new File(dir.getAbsolutePath() + File.separator + hostname + "-directions.txt");
			f.delete();
			String directions = "To request a host certificate, email your CA administrator at "
				+ caAdminEmail
				+ "."
				+ "Please set the subject of the email to:\n\n"
				+ emailSubject
				+ "\n\nIn the body of the email please include the following:\n\n"
				+ "----------------START BODY----------------\n"
				+ "Hostname: "
				+ hostname
				+ "\n"
				+ "First Name: "
				+ firstName
				+ "\n"
				+ "Last Name: "
				+ lastName
				+ "\n"
				+ "Organization: "
				+ organization
				+ "\n"
				+ "Project: "
				+ project
				+ "\n"
				+ "Phone Number: "
				+ phoneNumber
				+ "\n"
				+ "Reason: "
				+ reason
				+ "\n"
				+ "----------------END BODY----------------\n"
				+ "\n"
				+ "It is also required that the public key you just generated is attached to the email.\n"
				+ "The public key you need to attach to the email can be found at: \n\n	"
				+ publicKey.getAbsolutePath()
				+ "\n\nYou should receive an email response to your certificate request within\n"
				+ days
				+ " business day(s).  If your certificate request is approved, your certificate\n"
				+ "will come attached in the email response.  Please save the attached certificate to:\n\n	"
				+ cert.getAbsolutePath()
				+ "\n\nTo use these credentials for your globus container, type the following command from your GLOBUS_LOCATION:\n\n bin/globus-start-container -containerDesc "
				+ des.getAbsolutePath() + "\n\nThese directions have been written to the file:\n "
				+ f.getAbsolutePath();
			System.out.println(directions);
			StringBuffer sb2 = new StringBuffer(directions);
			Utils.stringBufferToFile(sb2, f.getAbsolutePath());

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}


	public static String readLine(String prompt) {
		String s = null;
		try {
			System.out.println(prompt + ":");
			System.out.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			s = br.readLine();
		} catch (IOException ioe) {
			System.out.println("IO error trying to read your name!");
			System.exit(1);
		}
		return s;
	}

}