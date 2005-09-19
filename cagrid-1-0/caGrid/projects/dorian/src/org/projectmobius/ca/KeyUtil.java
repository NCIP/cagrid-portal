package org.projectmobius.ca;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.SecureRandom;

import org.globus.gsi.OpenSSLKey;
import org.globus.gsi.bc.BouncyCastleOpenSSLKey;

public class KeyUtil {
	public static KeyPair generateRSAKeyPair() throws Exception {
		KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA", "BC");
		kpGen.initialize(1024, new SecureRandom());
		return kpGen.generateKeyPair();
	}

	public static void writePrivateKey(PrivateKey key, String file)
			throws Exception {
		writePrivateKey(key, file, null);
	}

	public static void writePrivateKey(PrivateKey key, String file,
			String password) throws Exception {
			OpenSSLKey ssl = new BouncyCastleOpenSSLKey(key);
			if (password != null) {
			ssl.encrypt(password);		
		}
         ssl.writeTo(file);			
	}

	public static PrivateKey loadPrivateKey(String location, String password)
			throws IOException, GeneralSecurityException {
		OpenSSLKey key = new BouncyCastleOpenSSLKey(location);
		if (key.isEncrypted()) {
			key.decrypt(password);
		}
		return key.getPrivateKey();

	}
}
