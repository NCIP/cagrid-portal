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
import org.cagrid.gme.common.XSDUtil;
import org.cagrid.gme.common.exceptions.SchemaParsingException;
import org.cagrid.gme.persistence.SchemaPersistenceGeneralException;
import org.cagrid.gme.persistence.SchemaPersistenceI;
import org.cagrid.gme.protocol.stubs.Schema;
import org.cagrid.gme.protocol.stubs.SchemaDocument;


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
            } else {
                SchemaParsingException e = new SchemaParsingException(
                    "Internal Error!  Got a request to resolve an entity of unknown/unsupported type [" + contextType
                        + "].");
                LOG.error(e.getMessage(), e);
                throw e;
            }
            LOG.debug("Got a request to " + (getDisplayNameForContextType(contextType)) + " namespace (" + idNamespace
                + ") and systemId (" + systemId + ").");
        }

        XMLInputSource result = new XMLInputSource(identifier);
        result.setPublicId(idNamespace);
        result.setSystemId(systemId);
        URI namespace = new URI(idNamespace);

        // first load schema from submission if present
        Schema schema = getSchemaFromSchemas(namespace);

        // if not in submission load from DB
        if (schema == null) {
            if (this.schemaPersistence != null) {
                URI ns = new URI(namespace);
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
            SchemaParsingException e = new SchemaParsingException("Unable to resolve reference to Schema (" + namespace
                + " - " + systemId + "); reference not found in submission package, nor as an existing Schema.");
            LOG.error(e.getMessage(), e);
            throw e;
        }

        SchemaDocument sd = null;
        if (isImport) {
            // if it is an import, we need to return the "root" SchemaDocument
            // for the namespace

            // TODO: how to find the "root" schema?
            sd = schema.getSchemaDocument(0);
        } else {
            // else, we need to find the SchemaDocument with a matching systemid
            // convert schema to InputSource
            sd = XSDUtil.getSchemaDocumentFromSchema(schema, systemId);

        }

        // if not found in schema, error out
        if (sd == null) {
            SchemaParsingException e = new SchemaParsingException("Unable to resolve reference to SchemaDocument ("
                + namespace + " - " + systemId + "); Schema was located but no matching SchemaDocument was found.");
            LOG.error(e.getMessage(), e);
            throw e;
        }

        // load the text from the SchemaDocument
        result.setByteStream(new ByteArrayInputStream(sd.getSchemaText().getBytes()));

        return result;

    }


    private String getDisplayNameForContextType(short contextType) {
        if (contextType == XMLSchemaDescription.CONTEXT_IMPORT) {
            return "import";
        } else if (contextType == XMLSchemaDescription.CONTEXT_INCLUDE) {
            return "include";
        } else if (contextType == XMLSchemaDescription.CONTEXT_REDEFINE) {
            return "redifine";
        } else {
            return "?";
        }
    }


    protected Schema getSchemaFromSchemas(URI namespace) throws SchemaParsingException {
        Schema result = null;
        if (this.submissionSchemas != null) {
            for (int i = 0; i < this.submissionSchemas.length; i++) {
                Schema schema = this.submissionSchemas[i];
                if (schema.getNamespace() != null && namespace.equals(schema.getNamespace())) {
                    LOG.debug("Found desired schema in submission package, at index (" + i + ").");
                    if (result == null) {
                        result = schema;
                    } else {
                        SchemaParsingException e = new SchemaParsingException(
                            "Schema submission contains multiple schemas claiming the same namespace(" + namespace
                                + ").");
                        LOG.error(e.getMessage(), e);
                        throw e;
                    }
                }
            }
        } else {
            LOG.debug("No schemas to search.");
        }

        return result;
    }

}
