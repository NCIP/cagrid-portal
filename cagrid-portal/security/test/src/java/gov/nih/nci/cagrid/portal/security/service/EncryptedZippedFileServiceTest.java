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
package gov.nih.nci.cagrid.portal.security.service;

import gov.nih.nci.cagrid.portal.authn.EncryptionService;
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
