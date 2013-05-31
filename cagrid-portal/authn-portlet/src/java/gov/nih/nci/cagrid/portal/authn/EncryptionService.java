/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 *
 */
package gov.nih.nci.cagrid.portal.authn;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.io.ByteArrayInputStream;
import java.security.spec.KeySpec;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author kherm manav.kher@semanticbits.com
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

    public byte[] encrypt(byte[] in) {
        byte[] out = null;
        if (in != null) {
            try {
                SecretKey key = keyFactory.generateSecret(keySpec);
                cipher.init(Cipher.ENCRYPT_MODE, key);
                byte[] ciphertext = cipher.doFinal(in);
                out = new BASE64Encoder().encode(ciphertext).getBytes();
            } catch (Exception ex) {
                throw new RuntimeException("Error encrypting: "
                        + ex.getMessage(), ex);
            }
        }
        return out;
    }

    public String encrypt(String in) {
        return new String(encrypt(in.getBytes()));
    }

    public byte[] decrypt(byte[] in) {
        byte[] out = null;
        if (in != null) {
            try {
                SecretKey key = keyFactory.generateSecret(keySpec);
                cipher.init(Cipher.DECRYPT_MODE, key);
                ByteArrayInputStream byteStream = new ByteArrayInputStream(in);
                byte[] cleartext = new BASE64Decoder().decodeBuffer(byteStream);
                out = cipher.doFinal(cleartext);
            } catch (Exception ex) {
                throw new RuntimeException("Error decrypting: "
                        + ex.getMessage(), ex);
            }
        }
        return out;
    }


    public String decrypt(String in) {
        return new String(decrypt(in.getBytes()));
    }

    public static void main(String[] args) {
        String cleartext = "Hi There!";
        String key = "yadda12345678901234567890";
        EncryptionService e = new EncryptionService(key);
        String ciphertext = e.encrypt(cleartext);
        System.out.println(cleartext);
        System.out.println(ciphertext);
        System.out.println(e.decrypt(ciphertext));
    }

}
