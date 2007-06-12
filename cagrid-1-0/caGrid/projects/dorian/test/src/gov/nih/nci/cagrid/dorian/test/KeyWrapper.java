package gov.nih.nci.cagrid.dorian.test;

import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import au.com.eracom.crypto.provider.ERACOMProvider;

public class KeyWrapper {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair(1024);
			String alias = "dorian-wrapper-key";

			Provider provider = new ERACOMProvider();
			Security.addProvider(provider);

			KeyStore keyStore = KeyStore.getInstance("CRYPTOKI", provider.getName());
			// TODO: Determine which password this is.
			keyStore.load(null, ""
					.toCharArray());

			KeyGenerator generator1 = KeyGenerator.getInstance("AES", provider);

			generator1.init(256, new SecureRandom());
			
			keyStore.setKeyEntry(alias, generator1.generateKey(), null,  null);

			Key signer = keyStore.getKey(alias, null);

			Cipher cipher = Cipher
					.getInstance("AES/ECB/PKCS5Padding", provider);

			// ---------------WRAP----------------

			// wrap the RSA private key
			cipher.init(Cipher.WRAP_MODE, signer);

			byte[] wrappedKey = cipher.wrap(pair.getPrivate());
			cipher.init(Cipher.UNWRAP_MODE, signer);
			PrivateKey key = (PrivateKey) cipher.unwrap(wrappedKey, "RSA",
					Cipher.PRIVATE_KEY);
			if (key.equals(pair.getPrivate())) {
				System.out.println("EQUAL");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
