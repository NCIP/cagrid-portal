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
import javax.crypto.spec.IvParameterSpec;

import au.com.eracom.crypto.provider.ERACOMProvider;

public class KeyWrapper2 {
	
	
	public static byte[] pad(int blockSize, byte[] b){
		byte padded[] = new byte[b.length+blockSize-(b.length%blockSize)];
		System.arraycopy(b, 0, padded, 0, b.length);
		for(int i=0; i<blockSize-(b.length%blockSize); i++){
			padded[b.length+i]=0;
		}
		return padded;
	}
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair(1024);
			
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
			
			//Key signer = keyStore.getKey("dorianca", null);
			//Key signer = keyStore.getKey(alias, null);
			/*
			Cipher cipher = Cipher
					.getInstance("AES/ECB/PKCS5Padding", provider);
			*/
			Cipher cipher = Cipher
			.getInstance("AES/CBC/PKCS5Padding", provider);
			
			
			/*
			KeyGenerator generator1 = KeyGenerator.getInstance("AES", "BC");

			generator1.init(256, new SecureRandom());

			Key signer = generator1.generateKey();
			Cipher cipher = Cipher
			.getInstance("AES/ECB/PKCS5Padding", "BC");
			*/

			// ---------------WRAP----------------

			// wrap the RSA private key
			cipher.init(Cipher.ENCRYPT_MODE, signer);
			byte[] input = KeyUtil.writePrivateKey(pair.getPrivate(),
					(String) null).getBytes();
			System.out.println("------------ INPUT --------------");
			System.out.println(input.length);
			System.out.println(new String(input));
			System.out.println("---------------------------------");
			/*
			byte[] padded = pad(cipher.getBlockSize(),input);
			System.out.println("------------ PADDED --------------");
			System.out.println(padded.length);
			System.out.println(new String(padded));
			System.out.println("---------------------------------");
			*/
			
			byte[] wrappedKey = cipher.doFinal(input);
			byte[] iv = cipher.getIV();
			System.out.println("------------ WRAPPED --------------");
			System.out.println(wrappedKey.length);
			System.out.println(new String(wrappedKey));
			System.out.println("---------------------------------");
			IvParameterSpec dps = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, signer,dps);
			byte[] output = cipher.doFinal(wrappedKey);
			System.out.println("------------ OUTPUT --------------");
			System.out.println(output.length);
			System.out.println(new String(output));
			System.out.println("---------------------------------");
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
