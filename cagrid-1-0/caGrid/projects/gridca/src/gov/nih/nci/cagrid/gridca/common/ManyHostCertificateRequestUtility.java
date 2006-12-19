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
public class ManyHostCertificateRequestUtility {

	public static void main(String[] args) {
		try {
			File dir = new File(args[0]);
			dir.mkdirs();
			for (int i = 1; i < args.length; i++) {
				File publicKey = new File(dir.getAbsolutePath() + File.separator + args[i] + "-public-key.pem");
				File privateKey = new File(dir.getAbsolutePath() + File.separator + args[i] + "-private-key.pem");
				File cert = new File(dir.getAbsolutePath() + File.separator + args[i] + "-cert.pem");
				File des = new File(dir.getAbsolutePath() + File.separator + args[i] + "-security-descriptor.xml");
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
			}

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