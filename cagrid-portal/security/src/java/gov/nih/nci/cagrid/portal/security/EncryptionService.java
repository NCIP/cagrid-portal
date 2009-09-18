/**
 * 
 */
package gov.nih.nci.cagrid.portal.security;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class EncryptionService {

	private static final String FORMAT = "UTF8";
	private static final String SCHEME = "DESede";
	private KeySpec keySpec;
	private SecretKeyFactory keyFactory;
	private Cipher cipher;

	/**
	 * 
	 */
	public EncryptionService(String key) {
		if (key == null) {
			throw new IllegalArgumentException("key property required");
		}
		if (key.length() < 24) {
			throw new IllegalArgumentException(
					"key must be greater than 24 characters");
		}
		try {
			keySpec = new DESedeKeySpec(key.getBytes(FORMAT));
			keyFactory = SecretKeyFactory.getInstance(SCHEME);
			cipher = Cipher.getInstance(SCHEME);
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	public String encrypt(String in) {
		String out = null;
		if (in != null) {
			try {
				SecretKey key = keyFactory.generateSecret(keySpec);
				cipher.init(Cipher.ENCRYPT_MODE, key);
				byte[] cleartext = in.getBytes(FORMAT);
				byte[] ciphertext = cipher.doFinal(cleartext);
				out = new BASE64Encoder().encode(ciphertext);
			} catch (Exception ex) {
				throw new RuntimeException("Error encrypting: "
						+ ex.getMessage(), ex);
			}
		}
		return out;
	}

	public String decrypt(String in) {
		String out = null;
		if (in != null) {
			try {
				SecretKey key = keyFactory.generateSecret(keySpec);
				cipher.init(Cipher.DECRYPT_MODE, key);
				byte[] cleartext = new BASE64Decoder().decodeBuffer(in);
				byte[] ciphertext = cipher.doFinal(cleartext);
				out = new String(ciphertext);
			} catch (Exception ex) {
				throw new RuntimeException("Error decrypting: "
						+ ex.getMessage(), ex);
			}
		}
		return out;
	}
	
	public static void main(String[] args){
		String cleartext = "Hi There!";
		String key = "yadda12345678901234567890";
		EncryptionService e = new EncryptionService(key);
		String ciphertext = e.encrypt(cleartext);
		System.out.println(cleartext);
		System.out.println(ciphertext);
		System.out.println(e.decrypt(ciphertext));
	}

}
