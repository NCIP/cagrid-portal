package org.cagrid.gme.facade;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.cagrid.gme.common.XSDUtil;
import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.gme.service.GME;
import org.cagrid.gme.stubs.types.InvalidSchemaSubmission;
import org.cagrid.gme.stubs.types.SchemaAlreadyExists;
import org.cagrid.gme.test.GMETestSchemaBundles;


/**
 * @author oster
 */
public class GMEFacadeAddSchemaTestCase extends TestCase {

    private GME getGMEWithFacade(Map<XMLSchema, List<URI>> storedSchemas) {
        return new GME(new FacadeSchemaPersistenceImpl(storedSchemas));
    }


    public void testAddSchema_Include() {
        try {
            GME gme = getGMEWithFacade(null);
            gme.addSchema(GMETestSchemaBundles.getSimpleIncludeBundle());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    public void testAddSchema_Recursive() {
        try {
            GME gme = getGMEWithFacade(null);
            gme.addSchema(GMETestSchemaBundles.getSimpleRecursiveBundle());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    public void testAddSchema_Cycle() {
        try {
            XMLSchema[] simpleCycleBundle = GMETestSchemaBundles.getSimpleCycleBundle();
            GME gme = getGMEWithFacade(null);
            gme.addSchema(simpleCycleBundle);

            // Swap the order and try again
            XMLSchema temp = simpleCycleBundle[0];
            simpleCycleBundle[0] = simpleCycleBundle[1];
            simpleCycleBundle[1] = temp;

            gme = getGMEWithFacade(null);
            gme.addSchema(simpleCycleBundle);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    public void testAddSchema_NoImports() {
        try {
            GME gme = getGMEWithFacade(null);
            URI ns = new URI("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common");
            gme.addSchema(new XMLSchema[]{XSDUtil.createSchema(ns, new File(
                "test/resources/schema/cagrid/common/common.xsd"))});
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    public void testAddSchema_AlreadyExistsFailure() {
        try {
            GME gme = getGMEWithFacade(null);

            URI ns = new URI("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common");
            XMLSchema[] schemaArr = new XMLSchema[]{XSDUtil.createSchema(ns, new File(
                "test/resources/schema/cagrid/common/common.xsd"))};

            gme.addSchema(schemaArr);
            try {
                gme.addSchema(schemaArr);
                fail("GME should have thrown exception!");
            } catch (SchemaAlreadyExists e) {
                // expected
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    public void testAddSchema_EmptySubmissionFailure() {
        try {
            XMLSchema[] schemaArr = new XMLSchema[0];
            GME gme = getGMEWithFacade(null);

            try {
                gme.addSchema(schemaArr);
                fail("GME should have thrown exception!");
            } catch (InvalidSchemaSubmission e) {
                // expected
            }

            try {
                gme.addSchema(null);
                fail("GME should have thrown exception!");
            } catch (InvalidSchemaSubmission e) {
                // expected
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    public void testAddSchema_MissingImportFailure() {
        testFailingSchema("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata",
            "test/resources/schema/cagrid/caGridMetadata.xsd");
    }


    public void testAddSchema_WrongURIFailure() {
        testFailingSchema("gme://wrongURI", "test/resources/schema/cagrid/common/common.xsd");
    }


    public void testAddSchema_MissingTypeFailure() {
        testFailingSchema("gme://missingtype", "test/resources/schema/invalid/missingtype.xsd");
    }


    public void testAddSchema_MissingIncludeFailure() {
        testFailingSchema("gme://missinginclude", "test/resources/schema/invalid/missinginclude.xsd");
    }


    public void testAddSchema_DuplicateElementsFailure() {
        testFailingSchema("gme://duplicateelements", "test/resources/schema/invalid/duplicateelements.xsd");
    }


    public void testAddSchema_ValidImports() {
        try {
            XMLSchema[] schemaArr = new XMLSchema[2];
            URI ns1 = new URI("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice");
            schemaArr[0] = XSDUtil.createSchema(ns1, new File("test/resources/schema/cagrid/data/data.xsd"));

            URI ns2 = new URI("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common");
            schemaArr[1] = XSDUtil.createSchema(ns2, new File("test/resources/schema/cagrid/common/common.xsd"));

            // adding both
            {
                GME gme = getGMEWithFacade(null);
                gme.addSchema(schemaArr);
                URI[] URIs = gme.getNamespaces();
                assertNotNull(URIs);
                assertTrue(URIs.length == 2);
            }

            // adding individually
            {
                GME gme2 = getGMEWithFacade(null);
                gme2.addSchema(new XMLSchema[]{schemaArr[1]});
                URI[] URIs = gme2.getNamespaces();
                assertNotNull(URIs);
                assertTrue(URIs.length == 1);

                // add the one importing the above
                gme2.addSchema(new XMLSchema[]{schemaArr[0]});
                URIs = gme2.getNamespaces();
                assertNotNull(URIs);
                assertTrue(URIs.length == 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    private void testFailingSchema(String namepace, String location) {
        assertNotNull("Cannot test a null URI.", namepace);
        assertNotNull("Cannot test a null location.", location);
        try {
            XMLSchema[] schemaArr = new XMLSchema[1];
            URI ns1 = new URI(namepace);
            schemaArr[0] = XSDUtil.createSchema(ns1, new File(location));
            try {
                GME gme = getGMEWithFacade(null);
                gme.addSchema(schemaArr);
                fail("GME should have thrown exception!");
            } catch (InvalidSchemaSubmission e) {
                // expected
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    public static void main(String[] args) {
        junit.textui.TestRunner.run(GMEFacadeAddSchemaTestCase.class);
    }
}