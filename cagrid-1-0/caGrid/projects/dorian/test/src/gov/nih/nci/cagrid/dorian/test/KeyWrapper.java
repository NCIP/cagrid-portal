package gov.nih.nci.cagrid.dorian.test;

import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;


public class KeyWrapper {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {

			KeyPair pair = KeyUtil.generateRSAKeyPair(1024);
			KeyGenerator generator1 = KeyGenerator.getInstance("AES", "BC");

			generator1.init(256, new SecureRandom());

			Key signer = generator1.generateKey();

			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");

			// ---------------WRAP----------------

			// wrap the RSA private key
			cipher.init(Cipher.WRAP_MODE, signer);

			byte[] wrappedKey = cipher.wrap(pair.getPrivate());
			cipher.init(Cipher.UNWRAP_MODE, signer);
			PrivateKey key = (PrivateKey) cipher.unwrap(wrappedKey, "RSA", Cipher.PRIVATE_KEY);
			if (key.equals(pair.getPrivate())) {
				System.out.println("EQUAL");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
