package gov.nih.nci.cagrid.gums.ca;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.SecureRandom;

import org.globus.gsi.OpenSSLKey;
import org.globus.gsi.bc.BouncyCastleOpenSSLKey;
/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: KeyUtil.java,v 1.1 2005-09-27 18:31:18 langella Exp $
 */
public class KeyUtil {
	

	public static KeyPair generateRSAKeyPair1024() throws Exception {
		SecurityUtil.init();
		KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA", "BC");
		kpGen.initialize(1024, new SecureRandom());
		return kpGen.generateKeyPair();
	}
	
	public static KeyPair generateRSAKeyPair512() throws Exception {
		SecurityUtil.init();
		KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA", "BC");
		kpGen.initialize(512, new SecureRandom());
		return kpGen.generateKeyPair();
	}

	public static void writePrivateKey(PrivateKey key, String file)
			throws Exception {
		writePrivateKey(key, file, null);
	}

	public static void writePrivateKey(PrivateKey key, String file,
			String password) throws Exception {
		SecurityUtil.init();
			OpenSSLKey ssl = new BouncyCastleOpenSSLKey(key);
			if (password != null) {
			ssl.encrypt(password);		
		}
         ssl.writeTo(file);			
	}

	public static PrivateKey loadPrivateKey(String location, String password)
			throws IOException, GeneralSecurityException {
		SecurityUtil.init();
		OpenSSLKey key = new BouncyCastleOpenSSLKey(location);
		if (key.isEncrypted()) {
			key.decrypt(password);
		}
		return key.getPrivateKey();

	}
}
