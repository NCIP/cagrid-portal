package org.cagrid.gme.service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.StringList;
import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.gme.domain.XMLSchemaDocument;
import org.cagrid.gme.persistence.SchemaPersistenceGeneralException;
import org.cagrid.gme.persistence.SchemaPersistenceI;
import org.cagrid.gme.sax.GMEErrorHandler;
import org.cagrid.gme.sax.GMEXMLSchemaLoader;
import org.cagrid.gme.stubs.types.InvalidSchemaSubmission;
import org.cagrid.gme.stubs.types.SchemaAlreadyExists;
import org.globus.wsrf.utils.FaultHelper;


public class GME {
    protected static Log LOG = LogFactory.getLog(GME.class.getName());
    protected SchemaPersistenceI schemaPersistence = null;

    // provides coarse grain persistence layer locking, used to ensure integrity
    // of
    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


    public GME(SchemaPersistenceI schemaPersistence) {
        if (schemaPersistence == null) {
            throw new IllegalArgumentException("Cannot use a null SchemaPersistenceI!");
        }
        LOG.info("Initializing GME with SchemaPersistenceI implementation class:"
            + schemaPersistence.getClass().getName());
        this.schemaPersistence = schemaPersistence;
    }


    public void addSchema(XMLSchema[] schemas) throws SchemaAlreadyExists, InvalidSchemaSubmission {

        // 0. sanity check submission
        if (schemas == null || schemas.length == 0) {
            String message = "No schemas were found in the submission.";
            LOG.error(message);

            InvalidSchemaSubmission e = new InvalidSchemaSubmission();
            e.setFaultString(message);

            throw e;
        }

        // need to get a "lock" on the database here
        this.lock.writeLock().lock();
        try {
            // 3. Create a list of "processed schemas"
            Map<URI, SchemaGrammar> processedSchemas = verifySubmissionAndInitializeProcessedSchemasMap(schemas);

            // 4. Create a model with error and entity resolver (on
            // callback
            // to imports/includes/redefines, entity resolver needs to first
            // load schema from
            // submission if present, if not in submission load from DB, if not
            // in
            // DB error out)
            GMEXMLSchemaLoader schemaLoader = new GMEXMLSchemaLoader(schemas, this.schemaPersistence);

            // 5. Call processSchema() for each schema being uploaded
            for (XMLSchema submittedSchema : schemas) {
                try {
                    processSchema(schemaLoader, processedSchemas, submittedSchema);
                } catch (Exception e) {
                    String message = "Problem processing schema submissions; the schema ["
                        + submittedSchema.getTargetNamespace() + "] was not valid:" + e.getMessage();
                    LOG.error(message, e);

                    InvalidSchemaSubmission fault = new InvalidSchemaSubmission();
                    fault.setFaultString(message);
                    FaultHelper helper = new FaultHelper(fault);
                    helper.addFaultCause(e);
                    fault = (InvalidSchemaSubmission) helper.getFault();

                    throw fault;
                }
            }

            // 8. Commit new/modified schemas to database, populating dependency
            // schema information (gathered from imports)

            commitSchemas(schemas, processedSchemas);

        } finally {
            // release database lock
            this.lock.writeLock().unlock();
        }
    }


    protected Map<URI, SchemaGrammar> verifySubmissionAndInitializeProcessedSchemasMap(XMLSchema[] schemas)
        throws InvalidSchemaSubmission, SchemaAlreadyExists {
        Map<URI, SchemaGrammar> processedSchemas = new HashMap<URI, SchemaGrammar>();
        for (XMLSchema submittedSchema : schemas) {
            // verify the schema's have unique and valid URIs
            URI namespace = submittedSchema.getTargetNamespace();
            if (namespace == null) {
                String message = "The schema submission contained a schema with a null URI.";
                LOG.error(message);

                InvalidSchemaSubmission e = new InvalidSchemaSubmission();
                e.setFaultString(message);

                throw e;

            }
            if (processedSchemas.containsKey(namespace)) {
                String message = "The schema submission contained multiple schemas of the same URI ("
                    + namespace
                    + ").  If you intend to submit schemas with includes, you need to package them as a single Schema with multiple SchemaDocuments.";
                LOG.error(message);

                InvalidSchemaSubmission e = new InvalidSchemaSubmission();
                e.setFaultString(message);

                throw e;
            } else {
                if (submittedSchema.getRootDocument() == null) {
                    String message = "The schema submission contained a schema ["
                        + submittedSchema.getTargetNamespace() + "] without a root schema document.";
                    LOG.error(message);

                    InvalidSchemaSubmission e = new InvalidSchemaSubmission();
                    e.setFaultString(message);

                    throw e;
                }

                // REVISIT: should probably check SchemaDocument rules here:
                // unique systemIDs for all [this is accomplished by using
                // Set now]
                // need to actually check that the schema rules are true
                // (i.e include must have no ns, or same ns), but basically
                // need the parsed model to know this... so i can check at
                // resolution time, but if something is never referenced, it
                // wont be loaded and so may be invalid... probably need to
                // check it after we have the grammar (can we ask for
                // includes and check for correspondence in the submission?)
                // [this is checked by combination of parser (checking rules
                // of what it reads) and matching
                // the loaded doc size matches the submitted doc
                // size (meaning they were all read)]

                // preload the submission schemas with "null" models (which
                // will be replaced by the actual models once they are
                // processed) so they don't get inappropriately processed
                // (as could be the case during the loading of "depending
                // schemas" )
                processedSchemas.put(namespace, null);
            }

            // TODO: permission check
            // 1. Check permissions on each schema being published; fail if
            // don't have permissions

            // 2. Check that all schemas being published are either not yet
            // published, or are in a state where the contents can be
            // updated ;
            // fail otherwise
            XMLSchema storedSchema = this.schemaPersistence.getSchema(namespace);
            if (storedSchema != null) {
                // TODO: check that it can be modified before doing this
                // (right now it is never allowed)
                // if(storeSchema.getState... != ){
                String message = "The schema [" + namespace + "] already exists and cannot be mofified.";
                LOG.error(message);

                SchemaAlreadyExists e = new SchemaAlreadyExists();
                e.setFaultString(message);

                throw e;
            }
        }
        return processedSchemas;
    }


    protected void commitSchemas(XMLSchema[] schemas, Map<URI, SchemaGrammar> processedSchemas)
        throws InvalidSchemaSubmission {

        // if got here with no error, schemas are ok to persist
        // build up DB objects to commit
        Map<XMLSchema, List<URI>> toCommit = new HashMap<XMLSchema, List<URI>>();
        for (XMLSchema submittedSchema : schemas) {
            // extract the schema model
            SchemaGrammar schemaGrammar = processedSchemas.get(submittedSchema.getTargetNamespace());
            assert schemaGrammar != null;
            assert !toCommit.containsKey(submittedSchema);

            // this has all the expanded locations which built up the schema
            // for us (using the namespace as the basesystemID, this should
            // be the ns+systemID)
            // REVISIT: is there a better way to figure out the
            // included/redefined documents?
            StringList documentLocations = schemaGrammar.getDocumentLocations();
            for (XMLSchemaDocument schemaDocument : submittedSchema.getAdditionalSchemaDocuments()) {
                URI expandedURI;
                try {
                    // REVISIT: is this the right way to construct this.
                    // NOTE: this must match the behavior of the
                    // GMEEntityResolver when it create the baseSystemID for
                    // the XMLInputSource it loads
                    expandedURI = new URI(submittedSchema.getTargetNamespace().toString() + "/"
                        + schemaDocument.getSystemID());

                } catch (Exception e) {
                    String message = "Problem processing schema submissions; the schema ["
                        + submittedSchema.getTargetNamespace() + "] included a SchemaDocument ["
                        + schemaDocument.getSystemID() + "] whose expanded URI was not valid:" + e.getMessage();
                    LOG.error(message, e);

                    InvalidSchemaSubmission fault = new InvalidSchemaSubmission();
                    fault.setFaultString(message);
                    FaultHelper helper = new FaultHelper(fault);
                    helper.addFaultCause(e);
                    fault = (InvalidSchemaSubmission) helper.getFault();

                    throw fault;
                }
                if (!documentLocations.contains(expandedURI.toString())) {
                    String message = "Problem processing schema submissions; the schema ["
                        + submittedSchema.getTargetNamespace() + "] included a SchemaDocument ["
                        + schemaDocument.getSystemID() + "] which was not used by the parsed grammar";
                    LOG.error(message);

                    InvalidSchemaSubmission fault = new InvalidSchemaSubmission();
                    fault.setFaultString(message);
                    FaultHelper helper = new FaultHelper(fault);
                    fault = (InvalidSchemaSubmission) helper.getFault();

                    throw fault;
                }
            }
            // we must have a document for the root and each additional
            // schema document
            if (documentLocations.getLength() != submittedSchema.getAdditionalSchemaDocuments().size() + 1) {
                String message = "Problem processing schema submissions; the schema ["
                    + submittedSchema.getTargetNamespace() + "] contained ["
                    + submittedSchema.getAdditionalSchemaDocuments().size()
                    + "] SchemaDocuments but the parsed grammar contained [" + documentLocations.getLength()
                    + "].  All SchemaDocuments must be used by the Schema.";
                LOG.error(message);

                InvalidSchemaSubmission fault = new InvalidSchemaSubmission();
                fault.setFaultString(message);
                FaultHelper helper = new FaultHelper(fault);
                fault = (InvalidSchemaSubmission) helper.getFault();

                throw fault;

            }

            // build an import list
            List<URI> importList = new ArrayList<URI>();
            Vector importedGrammars = schemaGrammar.getImportedGrammars();
            if (importedGrammars != null) {
                for (int i = 0; i < importedGrammars.size(); i++) {
                    SchemaGrammar importedSchema = (SchemaGrammar) importedGrammars.get(i);
                    String importedTargetNS = importedSchema.getTargetNamespace();
                    LOG.info("Schema [" + schemaGrammar.getTargetNamespace() + "] imports schema [" + importedTargetNS
                        + "]");
                    try {
                        importList.add(new URI(importedTargetNS));
                    } catch (URISyntaxException e) {
                        String message = "Problem processing schema submissions; the schema ["
                            + submittedSchema.getTargetNamespace() + "] imported a schema [" + importedTargetNS
                            + "] whose URI was not valid:" + e.getMessage();
                        LOG.error(message, e);

                        InvalidSchemaSubmission fault = new InvalidSchemaSubmission();
                        fault.setFaultString(message);
                        FaultHelper helper = new FaultHelper(fault);
                        helper.addFaultCause(e);
                        fault = (InvalidSchemaSubmission) helper.getFault();

                        throw fault;
                    }
                }
            }
            // add the schema and its import list to the Map to commit
            toCommit.put(submittedSchema, importList);
        }

        // commit to database
        this.schemaPersistence.storeSchemas(toCommit);
    }


    protected void processSchema(GMEXMLSchemaLoader schemaLoader, Map<URI, SchemaGrammar> processedSchemas,
        XMLSchema schemaToProcess) throws XNIException, IOException, SchemaPersistenceGeneralException {
        LOG.debug("About to process schema [" + schemaToProcess.getTargetNamespace() + "].");

        // 6.2. Add the schema to the model which will recursively fire
        // callbacks to the entity resolver for all imports (loading all
        // dependency schemas, and their dependency schemas, etc)
        String ns = schemaToProcess.getTargetNamespace().toString();

        // TODO: need to check the schema is actually valid (1..n
        // schemadocuemnts exist, and the namespace of all is what is specified
        // by the schema... but can only do that after parse time)

        XMLSchemaDocument rootSD = schemaToProcess.getRootDocument();

        // REVISIT: what to set for the baseSystemID? it's used to convert
        // includes, etc into full URIs and so is relevant later when examining
        // documentLocations of the SchemaGrammar
        XMLInputSource xis = new XMLInputSource(ns, rootSD.getSystemID(), ns, new StringReader(rootSD.getSchemaText()),
            "UTF-16");
        SchemaGrammar model = (SchemaGrammar) schemaLoader.loadGrammar(xis);
        if (model == null) {
            GMEErrorHandler errorHandler = schemaLoader.getErrorHandler();
            throw errorHandler.createXMLParseException();
        }

        // we should not have processed this schema before, and if the URI
        // is in the list it should have a null model (indicating it was
        // preloaded from the submission schemas set)
        assert !processedSchemas.containsKey(schemaToProcess.getTargetNamespace())
            || processedSchemas.get(schemaToProcess.getTargetNamespace()) == null;

        // store the resultant schema model (this needs to happen before
        // recursion)
        processedSchemas.put(schemaToProcess.getTargetNamespace(), model);

        String targetURI = model.getTargetNamespace();
        if (!ns.equals(targetURI)) {
            String message = "Problem processing schema submissions; the schema["
                + schemaToProcess.getTargetNamespace() + "] was not valid as its acutal targetURI [" + targetURI
                + "] did not match.";
            LOG.error(message);
            InvalidSchemaSubmission fault = new InvalidSchemaSubmission();
            fault.setFaultString(message);
            throw fault;
        }

        // 6.3. Look in the DB for depending schemas (will only be present if
        // schema was already published and is being updated)
        Collection<XMLSchema> dependingSchemas = this.schemaPersistence.getDependingSchemas(schemaToProcess
            .getTargetNamespace());

        // 6.4. For each depending schema not in the list of "processed schemas"
        // call processSchema()
        for (XMLSchema dependingSchema : dependingSchemas) {
            if (processedSchemas.containsKey(dependingSchema.getTargetNamespace())) {
                LOG.debug("Depending schema [" + dependingSchema.getTargetNamespace()
                    + "] was already processed (or will be processed).");
            } else {
                LOG.debug("Processing depending schema [" + dependingSchema.getTargetNamespace()
                    + "] which is not in the submission package.");
                processSchema(schemaLoader, processedSchemas, dependingSchema);
            }
        }
    }


    /**
     * Returns the targetNamespaces (represented by URIs) of all published
     * XMLSchemas
     * 
     * @return the targetNamespaces (represented by URIs) of all published
     *         XMLSchemas
     */
    public URI[] getNamespaces() {
        this.lock.readLock().lock();
        try {
            Collection<URI> nsCol = this.schemaPersistence.getNamespaces();
            URI[] nsArr = new URI[nsCol.size()];
            return nsCol.toArray(nsArr);
        } finally {
            this.lock.readLock().unlock();
        }
    }


    /**
     * Returns a published XMLSchema with a targetNamespace equal to the given
     * URI
     * 
     * @param uri
     *            the targetNamespace of the desired XMLSchema
     * @return a published XMLSchema with a targetNamespace equal to the given
     *         URI
     */
    public XMLSchema getSchema(URI uri) {
        this.lock.readLock().lock();
        try {
            return this.schemaPersistence.getSchema(uri);
        } finally {
            this.lock.readLock().unlock();
        }
    }

}
