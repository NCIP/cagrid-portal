package org.cagrid.gme.sax;

import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.gme.persistence.SchemaPersistenceI;


/**
 * @author oster
 */
public class GMEXMLSchemaLoader extends XMLSchemaLoader {

    public GMEXMLSchemaLoader(XMLSchema[] submissionSchemas, SchemaPersistenceI schemaPersistence) {
        setEntityResolver(new GMEEntityResolver(submissionSchemas, schemaPersistence));
        setErrorHandler(new GMEErrorHandler());
    }


    @Override
    public GMEErrorHandler getErrorHandler() {
        return (GMEErrorHandler) super.getErrorHandler();
    }
}
