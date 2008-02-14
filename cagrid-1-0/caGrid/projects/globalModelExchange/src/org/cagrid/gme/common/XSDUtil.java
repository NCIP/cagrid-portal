package org.cagrid.gme.common;

import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.xni.parser.XMLParseException;
import org.apache.xerces.xs.LSInputList;
import org.apache.xerces.xs.XSModel;
import org.cagrid.gme.persistence.SchemaPersistenceI;
import org.cagrid.gme.protocol.stubs.Schema;
import org.cagrid.gme.sax.GMEXMLSchemaLoader;
import org.w3c.dom.ls.LSInput;


/**
 * @author oster
 */
public class XSDUtil {

	private XSDUtil() {
	}


	public static final XSModel loadSchemas(final Schema[] schemas, SchemaPersistenceI schemaPersistence)
		throws IllegalArgumentException, XMLParseException {
		if (schemas == null ) {
			throw new IllegalArgumentException("Schemas must be non null.");
		}

		LSInputList list = new LSInputList() {
			public LSInput item(int index) {
				DOMInputImpl input = new DOMInputImpl();
				input.setSystemId(schemas[index].getNamespace().getNamespace().toString());
				input.setStringData(schemas[index].getSchemaText());
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

}
