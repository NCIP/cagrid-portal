package gov.nih.nci.cagrid.portal.util;

/**
 * Secure test. Needs local system to be synched to the trust store.
 */

public class XMLSchemaUtilsSecureSystemTest extends
        PortalAggrIntegrationTestBase {


    public void testGme() {
        XMLSchemaUtils xmlSchemaUtils = (XMLSchemaUtils) getApplicationContext().getBean("xmlSchemaUtils");
        String xmlSchema = xmlSchemaUtils.getXmlSchemaContent("gme://b");
        assertNotNull("Could not get schema", xmlSchema);
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:applicationContext-aggr.xml",
                "classpath*:applicationContext-db.xml"

        };
    }

}
