package org.cagrid.gme.xerces;

import java.io.FileInputStream;

import junit.framework.TestCase;

import org.apache.axis.types.URI;
import org.apache.commons.io.IOUtils;
import org.apache.xerces.xni.parser.XMLParseException;
import org.apache.xerces.xs.XSModel;
import org.cagrid.gme.common.XSDUtil;
import org.cagrid.gme.protocol.stubs.Namespace;
import org.cagrid.gme.protocol.stubs.Schema;
import org.cagrid.gme.test.GMETestSchemaBundles;


/**
 * @author oster
 */
public class XercesSchemaTestCase extends TestCase {

	public void testNoImports() {
		try {
			Schema[] schemaArr = new Schema[1];
			Namespace ns = new Namespace(new URI("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common"));
			schemaArr[0] = new Schema(ns, IOUtils.toString(new FileInputStream(
				"test/resources/schema/cagrid/common/common.xsd")));

			XSModel model = XSDUtil.loadSchemas(schemaArr, null);
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


	public void testDuplicateElements() {
		testFailingSchema("gme://duplicateelements", "test/resources/schema/invalid/duplicateelements.xsd");
	}

	
//	   public void testIncludes() {
//	        try {
//
//	            XSModel model = XSDUtil.loadSchemas(GMETestSchemaBundles.getSimpleIncludeBundle(), null);
//
//	            assertEquals(2, model.getNamespaceItems().getLength());
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            fail(e.getMessage());
//	        }
//	    }
//	

	public void testImports() {
		try {
			Schema[] schemaArr = new Schema[2];
			Namespace ns1 = new Namespace(new URI("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice"));
			schemaArr[0] = new Schema(ns1, IOUtils.toString(new FileInputStream(
				"test/resources/schema/cagrid/data/data.xsd")));
			Namespace ns2 = new Namespace(new URI("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common"));
			schemaArr[1] = new Schema(ns2, IOUtils.toString(new FileInputStream(
				"test/resources/schema/cagrid/common/common.xsd")));

			XSModel model = XSDUtil.loadSchemas(schemaArr, null);
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
			Schema[] schemaArr = new Schema[1];
			Namespace ns1 = new Namespace(new URI(namepace));
			schemaArr[0] = new Schema(ns1, IOUtils.toString(new FileInputStream(location)));

			try {
				XSModel model = XSDUtil.loadSchemas(schemaArr, null);
				fail("Parser should have thrown exception due to missing import!");
			} catch (XMLParseException e) {
				// expected
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}


	public static void main(String[] args) {
		junit.textui.TestRunner.run(XercesSchemaTestCase.class);
	}
}