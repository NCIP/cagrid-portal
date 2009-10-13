package gov.nih.nci.cagrid.portal.service;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ZippedPortalFileServiceTest {

    @Test
    public void create() throws Exception {
        PortalFileService service = new ZippedFileService();
        byte[] data = "String".getBytes();

        File file = service.write(data);
        assertNotNull(file.exists());

        assertEquals(new String(service.read(file.getName())), "String");

        file.delete();
        assertFalse(file.exists());
    }
}
