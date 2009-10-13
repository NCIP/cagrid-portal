package gov.nih.nci.cagrid.portal.security;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class EncryptionServiceTest {

    protected EncryptionService encryptionService;

    @Before
    public void init() {
        encryptionService = new EncryptionService("12345678910121416182022x");
    }


    @Test
    public void encryptDecryptStr() {
        String str = "hello";
        String encrypted = encryptionService.encrypt(str);
        assertNotNull(encrypted);
        assertNotSame(str, encrypted);

        String decrypted = encryptionService.decrypt(encrypted);
        assertNotNull(decrypted);
        assertEquals(str, decrypted);
    }

    @Test
    public void encryptDecryptbyteArr() {
        byte[] str = "hello".getBytes();
        byte[] encrypted = encryptionService.encrypt(str);
        assertNotNull(encrypted);
        assertNotSame(str, encrypted);

        byte[] decrypted = encryptionService.decrypt(encrypted);
        assertNotNull(decrypted);
        assertEquals(new String(str), new String(decrypted));
    }


    @Test
    public void encryptDecryptStrbyteArr() {
        String str = "hello";
        byte[] bstr = "hello".getBytes();
        String encrypted = encryptionService.encrypt(str);
        assertEquals(encryptionService.encrypt(str), new String(encryptionService.encrypt(bstr)));
        assertEquals(encryptionService.decrypt(encryptionService.encrypt(str)), new String(encryptionService.decrypt(encryptionService.encrypt(bstr))));

    }

}
