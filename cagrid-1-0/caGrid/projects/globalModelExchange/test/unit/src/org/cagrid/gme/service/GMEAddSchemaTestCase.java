package org.cagrid.gme.service;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.axis.types.URI;
import org.apache.commons.io.IOUtils;
import org.cagrid.gme.persistance.test.FacadeSchemaPersistenceImpl;
import org.cagrid.gme.protocol.stubs.Namespace;
import org.cagrid.gme.protocol.stubs.Schema;
import org.cagrid.gme.stubs.types.InvalidSchemaSubmission;
import org.cagrid.gme.stubs.types.SchemaAlreadyExists;
import org.cagrid.gme.test.GMETestSchemaBundles;


/**
 * @author oster
 */
public class GMEAddSchemaTestCase extends TestCase {

    private GME getGMEWithFacade(Map<Schema, List<Namespace>> storedSchemas) {
        return new GME(new FacadeSchemaPersistenceImpl(storedSchemas));
    }


    // public void testAddSchema_Include() {
    // try {
    // GME gme = getGMEWithFacade(null);
    // gme.addSchema(GMETestSchemaBundles.getSimpleIncludeBundle());
    // } catch (Exception e) {
    // e.printStackTrace();
    // fail(e.getMessage());
    // }
    // }

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
            Schema[] simpleCycleBundle = GMETestSchemaBundles.getSimpleCycleBundle();
            GME gme = getGMEWithFacade(null);
            gme.addSchema(simpleCycleBundle);

            // Swap the order and try again
            Schema temp = simpleCycleBundle[0];
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
            Schema[] schemaArr = new Schema[1];
            Namespace ns = new Namespace(new URI("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common"));
            schemaArr[0] = new Schema(ns, IOUtils.toString(new FileInputStream(
                "test/resources/schema/cagrid/common/common.xsd")));

            GME gme = getGMEWithFacade(null);
            gme.addSchema(schemaArr);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    public void testAddSchema_AlreadyExistsFailure() {
        try {
            Schema[] schemaArr = new Schema[1];
            Namespace ns1 = new Namespace(new URI("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common"));
            schemaArr[0] = new Schema(ns1, IOUtils.toString(new FileInputStream(
                "test/resources/schema/cagrid/common/common.xsd")));
            GME gme = getGMEWithFacade(null);
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
            Schema[] schemaArr = new Schema[0];
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


    public void testAddSchema_WrongNamespaceFailure() {
        testFailingSchema("gme://wrongnamespace", "test/resources/schema/cagrid/common/common.xsd");
    }


    public void testAddSchema_MissingTypeFailure() {
        testFailingSchema("gme://missingtype", "test/resources/schema/invalid/missingtype.xsd");
    }


    public void testAddSchema_DuplicateElementsFailure() {
        testFailingSchema("gme://duplicateelements", "test/resources/schema/invalid/duplicateelements.xsd");
    }


    public void testAddSchema_ValidImports() {
        try {
            Schema[] schemaArr = new Schema[2];
            Namespace ns1 = new Namespace(new URI("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice"));
            schemaArr[0] = new Schema(ns1, IOUtils.toString(new FileInputStream(
                "test/resources/schema/cagrid/data/data.xsd")));
            Namespace ns2 = new Namespace(new URI("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common"));
            schemaArr[1] = new Schema(ns2, IOUtils.toString(new FileInputStream(
                "test/resources/schema/cagrid/common/common.xsd")));

            // adding both
            {
                GME gme = getGMEWithFacade(null);
                gme.addSchema(schemaArr);
                Namespace[] namespaces = gme.getNamespaces();
                assertNotNull(namespaces);
                assertTrue(namespaces.length == 2);
            }

            // adding individually
            {
                GME gme2 = getGMEWithFacade(null);
                gme2.addSchema(new Schema[]{schemaArr[1]});
                Namespace[] namespaces = gme2.getNamespaces();
                assertNotNull(namespaces);
                assertTrue(namespaces.length == 1);

                // add the one importing the above
                gme2.addSchema(new Schema[]{schemaArr[0]});
                namespaces = gme2.getNamespaces();
                assertNotNull(namespaces);
                assertTrue(namespaces.length == 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    private void testFailingSchema(String namepace, String location) {
        assertNotNull("Cannot test a null namespace.", namepace);
        assertNotNull("Cannot test a null location.", location);
        try {
            Schema[] schemaArr = new Schema[1];
            Namespace ns1 = new Namespace(new URI(namepace));
            schemaArr[0] = new Schema(ns1, IOUtils.toString(new FileInputStream(location)));

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
        junit.textui.TestRunner.run(GMEAddSchemaTestCase.class);
    }
}