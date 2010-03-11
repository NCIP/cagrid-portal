package gov.nih.nci.cagrid.portal.service;

import gov.nih.nci.cagrid.portal.DBIntegrationTestBase;

import java.io.File;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ZippedFileServiceSystemTest extends DBIntegrationTestBase {

    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                "applicationContext-db.xml",
                "applicationContext-service.xml"

        };
    }


    public void testCreate() throws Exception {
        PortalFileService service = (PortalFileService) getApplicationContext().getBean("portalFileService");
        File file = service.write(new byte[]{});
        assertNotNull(file);
        assertTrue(service.delete(file.getName()));
    }
}
