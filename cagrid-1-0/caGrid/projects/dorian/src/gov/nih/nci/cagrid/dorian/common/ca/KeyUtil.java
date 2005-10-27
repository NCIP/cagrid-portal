package gov.nih.nci.cagrid.gums.common.ca;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.SecureRandom;

import org.globus.gsi.OpenSSLKey;
import org.globus.gsi.bc.BouncyCastleOpenSSLKey;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
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

	public static String writePrivateKeyToString(PrivateKey key, String password)
			throws Exception {
		SecurityUtil.init();
		OpenSSLKey ssl = new BouncyCastleOpenSSLKey(key);
		if (password != null) {
			ssl.encrypt(password);
		}
		StringWriter sw = new StringWriter();
		ssl.writeTo(sw);
		return sw.toString();
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

	public static PrivateKey loadPrivateKey(InputStream in, String password)
			throws IOException, GeneralSecurityException {
		SecurityUtil.init();
		OpenSSLKey key = new BouncyCastleOpenSSLKey(in);
		if (key.isEncrypted()) {
			key.decrypt(password);	
		}
		return key.getPrivateKey();
	}
}
