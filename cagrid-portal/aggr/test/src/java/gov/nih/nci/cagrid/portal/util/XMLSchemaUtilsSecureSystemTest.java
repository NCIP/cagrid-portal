/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
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
