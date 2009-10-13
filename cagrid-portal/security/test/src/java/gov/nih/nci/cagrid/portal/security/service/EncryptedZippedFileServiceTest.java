package gov.nih.nci.cagrid.portal.security.service;

import gov.nih.nci.cagrid.portal.security.EncryptionService;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class EncryptedZippedFileServiceTest {

    protected EncryptionService encryptionService;

    @Before
    public void init() {
        encryptionService = new EncryptionService("12345678910121416182022x");
    }


    @Test
    public void create() throws Exception {
        EncryptedZippedFileService service = new EncryptedZippedFileService();
        service.setEncryptionService(encryptionService);

        byte[] data = "String".getBytes();

        File file = service.write(data);
        assertNotNull(file.exists());

        assertEquals(new String(service.read(file.getName())), "String");

        file.delete();
        assertFalse(file.exists());
    }
}
