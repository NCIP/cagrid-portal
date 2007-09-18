package org.cagrid.gme.sax;

import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.cagrid.gme.persistence.SchemaPersistenceI;
import org.cagrid.gme.protocol.stubs.Schema;


/**
 * @author oster
 */
public class GMEXMLSchemaLoader extends XMLSchemaLoader {

	public GMEXMLSchemaLoader(Schema[] submissionSchemas, SchemaPersistenceI schemaPersistence) {
		setEntityResolver(new GMEEntityResolver(submissionSchemas, schemaPersistence));
		setErrorHandler(new GMEErrorHandler());
	}


	@Override
	public GMEErrorHandler getErrorHandler() {
		return (GMEErrorHandler) super.getErrorHandler();
	}
}
