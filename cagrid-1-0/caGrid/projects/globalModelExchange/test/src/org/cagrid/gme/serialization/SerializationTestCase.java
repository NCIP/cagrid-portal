package org.cagrid.gme.serialization;

import gov.nih.nci.cagrid.common.SchemaValidationException;
import gov.nih.nci.cagrid.common.SchemaValidator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.gme.domain.XMLSchemaBundle;
import org.cagrid.gme.domain.XMLSchemaDocument;
import org.cagrid.gme.domain.XMLSchemaImportInformation;
import org.cagrid.gme.domain.XMLSchemaNamespace;


public class SerializationTestCase extends TestCase {
    private static final String GME_SCHEMA_LOCATION = "schema" + File.separator + "GlobalModelExchange"
        + File.separator + "org.cagrid.gme.domain.xsd";

    private static Log log = LogFactory.getLog(SerializationTestCase.class);


    public static void main(String[] args) {
        junit.textui.TestRunner.run(SerializationTestCase.class);
    }


    public void testSerializeXMLSchema() {
        try {

            XMLSchemaDocument d1 = new XMLSchemaDocument();
            d1.setSchemaText("<xml>This is the d1 schema text</xml>");
            d1.setSystemID("d1");
            XMLSchemaDocument d2 = new XMLSchemaDocument();
            d2.setSchemaText("<xml>This is the d2 schema text</xml>");
            d2.setSystemID("d2");
            XMLSchemaDocument d3 = new XMLSchemaDocument();
            d3.setSchemaText("<xml>This is the d3 schema text</xml>");
            d3.setSystemID("d3");

            XMLSchema s1 = new XMLSchema();
            s1.setRootDocument(d1);
            s1.setTargetNamespace(new URI("gme://d1"));
            Set<XMLSchemaDocument> docs = new HashSet<XMLSchemaDocument>();
            docs.add(d2);
            docs.add(d3);
            s1.setAdditionalSchemaDocuments(docs);

            File tmpFile = File.createTempFile("XmlSchema", ".xml");
            tmpFile.deleteOnExit();
            FileWriter tmpFileWriter = new FileWriter(tmpFile);

            SerializationUtils.serializeXMLSchema(s1, tmpFileWriter);
            tmpFileWriter.close();
            log.debug("Wrote to file: " + tmpFile.getCanonicalPath());
            assertTrue(tmpFile.exists());

            validateAgainstSchema(tmpFile);

            Reader r = new FileReader(tmpFile);
            XMLSchema s2 = SerializationUtils.deserializeXMLSchema(r);
            r.close();
            assertNotNull(s2);
            assertEquals(s1, s2);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    public void testSerializeXMLSchemaBundle() {
        try {

            XMLSchemaDocument d1 = new XMLSchemaDocument();
            d1.setSchemaText("<xml>This is the d1 schema text</xml>");
            d1.setSystemID("d1");
            XMLSchemaDocument d2 = new XMLSchemaDocument();
            d2.setSchemaText("<xml>This is the d2 schema text</xml>");
            d2.setSystemID("d2");
            XMLSchemaDocument d3 = new XMLSchemaDocument();
            d3.setSchemaText("<xml>This is the d3 schema text</xml>");
            d3.setSystemID("d3");

            XMLSchema s1 = new XMLSchema();
            s1.setRootDocument(d1);
            s1.setTargetNamespace(new URI("gme://d1"));
            Set<XMLSchemaDocument> docs = new HashSet<XMLSchemaDocument>();
            docs.add(d2);
            docs.add(d3);
            s1.setAdditionalSchemaDocuments(docs);

            XMLSchema s2 = new XMLSchema();
            s2.setRootDocument(d1);
            s2.setTargetNamespace(new URI("gme://d2"));
            Set<XMLSchemaDocument> docs2 = new HashSet<XMLSchemaDocument>();
            docs2.add(d2);
            docs2.add(d3);
            s2.setAdditionalSchemaDocuments(docs);

            XMLSchemaBundle bundle = new XMLSchemaBundle();

            Set<XMLSchema> xmlSchemaCollection = new HashSet<XMLSchema>();
            xmlSchemaCollection.add(s1);
            xmlSchemaCollection.add(s2);
            bundle.setXMLSchemas(xmlSchemaCollection);

            Set<XMLSchemaImportInformation> importList = new HashSet<XMLSchemaImportInformation>();
            XMLSchemaImportInformation ii = new XMLSchemaImportInformation();
            ii.setTargetNamespace(new XMLSchemaNamespace(s1.getTargetNamespace()));
            ArrayList<XMLSchemaNamespace> s1_imports = new ArrayList<XMLSchemaNamespace>();
            s1_imports.add(new XMLSchemaNamespace(s2.getTargetNamespace()));
            s1_imports.add(new XMLSchemaNamespace(s2.getTargetNamespace()));
            ii.setImports(s1_imports);
            importList.add(ii);

            bundle.setImportInformation(importList);

            File tmpFile = File.createTempFile("XmlSchemaBundle", ".xml");
            tmpFile.deleteOnExit();
            FileWriter tmpFileWriter = new FileWriter(tmpFile);

            SerializationUtils.serializeXMLSchemaBundle(bundle, tmpFileWriter);

            tmpFileWriter.close();
            log.debug("Wrote to file: " + tmpFile.getCanonicalPath());
            assertTrue(tmpFile.exists());

            validateAgainstSchema(tmpFile);

            Reader r = new FileReader(tmpFile);
            XMLSchemaBundle bundleFromFile = SerializationUtils.deserializeXMLSchemaBundle(r);
            r.close();
            assertNotNull(bundleFromFile);
            assertEquals(bundle, bundleFromFile);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    private void validateAgainstSchema(File instanceFile) throws SchemaValidationException {
        SchemaValidator xsdValidator = new SchemaValidator(GME_SCHEMA_LOCATION);
        xsdValidator.validate(instanceFile);
    }
}
