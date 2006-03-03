package gov.nih.nci.cagrid.gridca.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import org.bouncycastle.openssl.PEMReader;
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

	public static void writePrivateKey(PrivateKey key, File file)
			throws Exception {
		writePrivateKey(key, file, null);
	}

	public static void writePrivateKey(PrivateKey key, File file,
			String password) throws Exception {
		SecurityUtil.init();
		OpenSSLKey ssl = new BouncyCastleOpenSSLKey(key);
		if (password != null) {
			ssl.encrypt(password);
		}
		ssl.writeTo(file.getAbsolutePath());
	}

	public static String writePrivateKey(PrivateKey key, String password)
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

	public static PrivateKey loadPrivateKey(File location, String password)
			throws IOException, GeneralSecurityException {
		SecurityUtil.init();
		OpenSSLKey key = new BouncyCastleOpenSSLKey(location.getAbsolutePath());
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

	public static PublicKey loadPublicKey(String key)
			throws IOException, GeneralSecurityException {
		SecurityUtil.init();
		StringReader in = new StringReader(key);
		PEMReader reader = new PEMReader(in, null, "BC");
		return (PublicKey) reader.readObject();
	}

	public static String writePublicKey(PublicKey key)
			throws IOException {
		SecurityUtil.init();
		StringWriter sw = new StringWriter();
		PEMWriter pem = new PEMWriter(sw);
		pem.writeObject(key);
		pem.close();
		return sw.toString();
	}
}
