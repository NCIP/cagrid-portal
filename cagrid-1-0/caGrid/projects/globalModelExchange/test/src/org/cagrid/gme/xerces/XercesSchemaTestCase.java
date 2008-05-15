package org.cagrid.gme.xerces;

import java.io.File;
import java.net.URI;

import junit.framework.TestCase;

import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.xni.parser.XMLParseException;
import org.apache.xerces.xs.LSInputList;
import org.apache.xerces.xs.XSModel;
import org.cagrid.gme.common.XSDUtil;
import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.gme.persistence.SchemaPersistenceI;
import org.cagrid.gme.sax.GMEXMLSchemaLoader;
import org.w3c.dom.ls.LSInput;


public class XercesSchemaTestCase extends TestCase {

    public void testNoImports() {
        try {
            XMLSchema[] schemaArr = new XMLSchema[1];
            URI ns = new URI("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common");
            schemaArr[0] = XSDUtil.createSchema(ns, new File("test/resources/schema/cagrid/common/common.xsd"));

            XSModel model = loadSchemas(schemaArr, null);
            assertEquals(2, model.getNamespaceItems().getLength());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    public void testMissingImportFailure() {
        testFailingSchema("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata",
            "test/resources/schema/cagrid/caGridMetadata.xsd");
    }


    public void testMissingType() {
        testFailingSchema("gme://missingtype", "test/resources/schema/invalid/missingtype.xsd");
    }


    public void testMissingInclude() {
        testFailingSchema("gme://missinginclude", "test/resources/schema/invalid/missinginclude.xsd");
    }


    public void testDuplicateElements() {
        testFailingSchema("gme://duplicateelements", "test/resources/schema/invalid/duplicateelements.xsd");
    }


    // public void testIncludes() {
    // try {
    //
    // XSModel model =
    // loadSchemas(GMETestSchemaBundles.getSimpleIncludeBundle(), null);
    //
    // assertEquals(2, model.getNamespaceItems().getLength());
    // } catch (Exception e) {
    // e.printStackTrace();
    // fail(e.getMessage());
    // }
    // }

    public void testImports() {
        try {
            XMLSchema[] schemaArr = new XMLSchema[2];
            URI ns1 = new URI("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice");
            schemaArr[0] = XSDUtil.createSchema(ns1, new File("test/resources/schema/cagrid/data/data.xsd"));

            URI ns2 = new URI("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common");
            schemaArr[1] = XSDUtil.createSchema(ns2, new File("test/resources/schema/cagrid/common/common.xsd"));

            XSModel model = loadSchemas(schemaArr, null);
            // TODO: why is this 4, and not 3? how can we prevent it from
            // processing schemas from imports that we've already processed
            assertEquals(4, model.getNamespaceItems().getLength());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    private void testFailingSchema(String namepace, String location) {
        assertNotNull("Cannot test a null namespace.", namepace);
        assertNotNull("Cannot test a null location.", location);
        try {
            XMLSchema[] schemaArr = new XMLSchema[1];
            URI ns1 = new URI(namepace);
            schemaArr[0] = XSDUtil.createSchema(ns1, new File(location));

            try {
                XSModel model = loadSchemas(schemaArr, null);
                fail("Parser should have thrown exception due to missing import!");
            } catch (XMLParseException e) {
                // expected
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

    }


    private static final XSModel loadSchemas(final XMLSchema[] schemas, SchemaPersistenceI schemaPersistence)
        throws IllegalArgumentException, XMLParseException {
        if (schemas == null) {
            throw new IllegalArgumentException("Schemas must be non null.");
        }

        LSInputList list = new LSInputList() {
            public LSInput item(int index) {
                DOMInputImpl input = new DOMInputImpl();
                input.setSystemId(schemas[index].getRootDocument().getSystemID());
                input.setStringData(schemas[index].getRootDocument().getSchemaText());
                return input;
            }


            public int getLength() {
                return schemas.length;
            }
        };

        GMEXMLSchemaLoader schemaLoader = new GMEXMLSchemaLoader(schemas, schemaPersistence);

        XSModel model = schemaLoader.loadInputList(list);
        if (model == null) {
            throw schemaLoader.getErrorHandler().createXMLParseException();
        }

        return model;
    }


    public static void main(String[] args) {
        junit.textui.TestRunner.run(XercesSchemaTestCase.class);
    }
}