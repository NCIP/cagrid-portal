package org.cagrid.gme.serialization;

import gov.nih.nci.cagrid.common.Utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.gme.client.GlobalModelExchangeClient;
import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.gme.domain.XMLSchemaDocument;


public class SerializationTestCase extends TestCase {
    private static Log log = LogFactory.getLog(SerializationTestCase.class);


    public static void main(String[] args) {
        junit.textui.TestRunner.run(SerializationTestCase.class);
    }


    public void testSerializeXMLSchemaDocument() {
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

            File tmpFile = File.createTempFile("XmlSchemaDocument", ".xml");
            tmpFile.deleteOnExit();
            FileWriter tmpFileWriter = new FileWriter(tmpFile);

            Utils.serializeObject(s1, new QName("foo", "bar"), tmpFileWriter, GlobalModelExchangeClient.class
                .getResourceAsStream("client-config.wsdd"));
            tmpFileWriter.close();
            log.debug("Wrote to file: " + tmpFile.getCanonicalPath());
            assertTrue(tmpFile.exists());

            Reader r = new FileReader(tmpFile);
            XMLSchema s2 = (XMLSchema) Utils.deserializeObject(r, XMLSchema.class, GlobalModelExchangeClient.class
                .getResourceAsStream("client-config.wsdd"));
            r.close();
            assertNotNull(s2);
            assertEquals(s1, s2);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
