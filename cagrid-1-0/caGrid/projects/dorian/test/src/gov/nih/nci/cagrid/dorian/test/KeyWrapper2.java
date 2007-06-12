package gov.nih.nci.cagrid.dorian.test;

import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.io.ByteArrayInputStream;
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

public class KeyWrapper2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair(1024);
			/*
			String alias = "dorian-wrapper-key";

			Provider provider = new ERACOMProvider();
			Security.addProvider(provider);
			KeyStore keyStore = KeyStore.getInstance("CRYPTOKI", provider
					.getName());
			keyStore.load(null, "".toCharArray());

			KeyGenerator generator1 = KeyGenerator.getInstance("AES", provider);

			generator1.init(256, new SecureRandom());

			keyStore.setKeyEntry(alias, generator1.generateKey(), null, null);

			Key signer = keyStore.getKey(alias, null);
			Cipher cipher = Cipher
					.getInstance("AES/ECB/PKCS5Padding", provider);
			*/
			
			KeyGenerator generator1 = KeyGenerator.getInstance("AES", "BC");

			generator1.init(256, new SecureRandom());

			Key signer = generator1.generateKey();
			Cipher cipher = Cipher
			.getInstance("AES/ECB/PKCS5Padding", "BC");
			

			// ---------------WRAP----------------

			// wrap the RSA private key
			cipher.init(Cipher.ENCRYPT_MODE, signer);
			byte[] input = KeyUtil.writePrivateKey(pair.getPrivate(),
					(String) null).getBytes();
			byte[] wrappedKey = cipher.doFinal(input);
			cipher.init(Cipher.DECRYPT_MODE, signer);
			byte[] output = cipher.doFinal(wrappedKey);
			PrivateKey key = KeyUtil.loadPrivateKey(new ByteArrayInputStream(
					output), null);
			if (key.equals(pair.getPrivate())) {
				System.out.println("EQUAL");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
