package org.cagrid.gme.sax;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.axis.types.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLSchemaDescription;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.cagrid.gme.common.exceptions.SchemaParsingException;
import org.cagrid.gme.persistence.SchemaPersistenceGeneralException;
import org.cagrid.gme.persistence.SchemaPersistenceI;
import org.cagrid.gme.protocol.stubs.Namespace;
import org.cagrid.gme.protocol.stubs.Schema;


/**
 * @author oster
 */
public class GMEEntityResolver implements XMLEntityResolver {
    protected Schema[] submissionSchemas = null;
    protected SchemaPersistenceI schemaPersistence = null;

    protected static Log LOG = LogFactory.getLog(GMEEntityResolver.class.getName());


    public GMEEntityResolver(Schema[] submissionSchemas, SchemaPersistenceI schemaPersistence) {
        super();
        this.submissionSchemas = submissionSchemas;
        this.schemaPersistence = schemaPersistence;
        if (submissionSchemas != null) {
            LOG.debug("Initializing with " + submissionSchemas.length + " submission schemas; SchemaPersistence="
                + schemaPersistence);
        } else {
            LOG.debug("Initializing with no submission schemas; SchemaPersistence=" + schemaPersistence);
        }
    }


    public XMLInputSource resolveEntity(XMLResourceIdentifier identifier) throws XNIException, IOException {
        String idNamespace = identifier.getNamespace();
        String systemId = identifier.getLiteralSystemId();
        // TODO: handle different types (import/include/redefine)
        boolean isImport = true;
        if (identifier instanceof XMLSchemaDescription) {
            XMLSchemaDescription desc = (XMLSchemaDescription) identifier;
            short contextType = desc.getContextType();
            if (contextType == XMLSchemaDescription.CONTEXT_IMPORT) {
                isImport = true;
            } else if (contextType == XMLSchemaDescription.CONTEXT_INCLUDE) {
                isImport = false;
            } else if (contextType == XMLSchemaDescription.CONTEXT_REDEFINE) {
                isImport = false;
            }
        }

        XMLInputSource result = new XMLInputSource(identifier);
        result.setPublicId(idNamespace);
        result.setSystemId(systemId);

        LOG.debug("Got a request namespace (" + idNamespace + ") and systemId (" + systemId + ")");

        URI namespace = new URI(idNamespace);

        // first load schema from submission if present
        Schema schema = getSchemaFromSchemas(namespace);

        // if not in submission load from DB
        if (schema == null) {
            if (this.schemaPersistence != null) {
                Namespace ns = new Namespace(namespace);
                try {
                    schema = this.schemaPersistence.getSchema(ns);
                } catch (SchemaPersistenceGeneralException e) {
                    LOG.error("Problem trying to load schema from backend.", e);
                    throw new SchemaParsingException("Problem trying to load schema from backend:" + e.getMessage());
                }
            } else {
                LOG.debug("No SchemaPersistence found, and schema wasn't found in submission.");
            }
        }

        // if not in DB error out
        if (schema == null) {
            // TODO: does this always cause failure?
            throw new SchemaParsingException("Unable to resolve reference to schema (" + namespace + " - " + systemId
                + "); reference not found in submission package, nor as an existing schema.");
        } else {
            // convert schema to InputSource
            result.setByteStream(new ByteArrayInputStream(schema.getSchemaText().getBytes()));
            return result;
        }
    }


    protected Schema getSchemaFromSchemas(URI namespace) throws SchemaParsingException {
        Schema result = null;
        if (this.submissionSchemas != null) {
            for (int i = 0; i < this.submissionSchemas.length; i++) {
                Schema schema = this.submissionSchemas[i];
                if (schema.getNamespace() != null && namespace.equals(schema.getNamespace().getNamespace())) {
                    LOG.debug("Found desired schema in submission package, at index (" + i + ").");
                    if (result == null) {
                        result = schema;
                    } else {
                        throw new SchemaParsingException(
                            "Schema submission contains multiple schemas claiming the same namespace(" + namespace
                                + ").");
                    }
                }
            }
        } else {
            LOG.debug("No schemas to search.");
        }

        return result;
    }
}
