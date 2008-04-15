package org.cagrid.gme.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.StringList;
import org.cagrid.gme.persistence.SchemaPersistenceGeneralException;
import org.cagrid.gme.persistence.SchemaPersistenceI;
import org.cagrid.gme.protocol.stubs.Schema;
import org.cagrid.gme.protocol.stubs.SchemaDocument;
import org.cagrid.gme.sax.GMEErrorHandler;
import org.cagrid.gme.sax.GMEXMLSchemaLoader;
import org.cagrid.gme.stubs.types.InvalidSchemaSubmission;
import org.cagrid.gme.stubs.types.SchemaAlreadyExists;
import org.globus.wsrf.utils.FaultHelper;


/**
 * @author oster
 */
public class GME {
    protected static Log LOG = LogFactory.getLog(GME.class.getName());
    protected SchemaPersistenceI schemaPersistence = null;
    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


    public GME(SchemaPersistenceI schemaPersistence) {
        if (schemaPersistence == null) {
            throw new IllegalArgumentException("Cannot use a null SchemaPersistenceI!");
        }
        LOG.info("Initializing GME with SchemaPersistenceI implementation class:"
            + schemaPersistence.getClass().getName());
        this.schemaPersistence = schemaPersistence;
    }


    public void addSchema(Schema[] schemas) throws SchemaAlreadyExists, InvalidSchemaSubmission,
        SchemaPersistenceGeneralException {

        // 0. sanitity check submission
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
            // 4. Create a list of "processed schemas"
            Map<URI, SchemaGrammar> processedSchemas = new HashMap<URI, SchemaGrammar>();
            for (Schema submittedSchema : schemas) {
                // verify the schema's have unique and valid URIs
                URI namespace = submittedSchema.getNamespace();
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

                    // TODO: should probably check SchemaDocument rules here:
                    // unique systemIDs for all
                    // need to actually check that the schema rules are true
                    // (i.e include must have no ns, or same ns), but basically
                    // need the parsed model to know this... so i can check at
                    // resolution time, but if something is never referenced, it
                    // wont be loaded and so may be invalid... probably need to
                    // check it after we have the grammar (can we ask for
                    // includes and check for correspondence in the submission?)

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
                Schema storedSchema = this.schemaPersistence.getSchema(namespace);
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

            // 3. Create a model with error and entity resolver (on
            // callback
            // to imports/includes/redefines, entity resolver needs to first
            // load schema from
            // submission if present, if not in submission load from DB, if not
            // in
            // DB error out)
            GMEXMLSchemaLoader schemaLoader = new GMEXMLSchemaLoader(schemas, this.schemaPersistence);

            // 5. Call processSchema() for each schema being uploaded
            for (Schema submittedSchema : schemas) {
                try {
                    processSchema(schemaLoader, processedSchemas, submittedSchema);
                } catch (Exception e) {
                    String message = "Problem processing schema submissions; the schema ["
                        + submittedSchema.getNamespace() + "] was not valid:" + e.getMessage();
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

            // TODO: how are includes handled in the SchemaGrammar?

            // if got here with no error, schemas are ok to persist
            // build up DB objects to commit
            Map<Schema, List<URI>> toCommit = new HashMap<Schema, List<URI>>();
            for (Schema submittedSchema : schemas) {
                // extract the schema model
                SchemaGrammar schemaGrammar = processedSchemas.get(submittedSchema.getNamespace());
                assert schemaGrammar != null;
                assert !toCommit.containsKey(submittedSchema);

                // this has all the expanded locations which built up the schema
                // for us (using the namespace as the basesystemID, this should
                // be the ns+systemID)
                // REVISIT: is there a better way to figure out the
                // included/redefined documents?
                StringList documentLocations = schemaGrammar.getDocumentLocations();
                for (SchemaDocument schemaDocument : submittedSchema.getSchemaDocument()) {
                    URI expandedURI;
                    try {
                        expandedURI = new URI(submittedSchema.getNamespace(), schemaDocument.getSystemID());
                    } catch (MalformedURIException e) {
                        String message = "Problem processing schema submissions; the schema ["
                            + submittedSchema.getNamespace() + "] included a SchemaDocument ["
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
                            + submittedSchema.getNamespace() + "] included a SchemaDocument ["
                            + schemaDocument.getSystemID() + "] which was not used by the parsed grammar";
                        LOG.error(message);

                        InvalidSchemaSubmission fault = new InvalidSchemaSubmission();
                        fault.setFaultString(message);
                        FaultHelper helper = new FaultHelper(fault);
                        fault = (InvalidSchemaSubmission) helper.getFault();

                        throw fault;
                    }
                }
                if (documentLocations.getLength() != submittedSchema.getSchemaDocument().length) {
                    String message = "Problem processing schema submissions; the schema ["
                        + submittedSchema.getNamespace() + "] contained [" + submittedSchema.getSchemaDocument().length
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
                        LOG.info("Schema [" + schemaGrammar.getTargetNamespace() + "] imports schema ["
                            + importedTargetNS + "]");
                        try {
                            importList.add(new URI(importedTargetNS));
                        } catch (MalformedURIException e) {
                            String message = "Problem processing schema submissions; the schema ["
                                + submittedSchema.getNamespace() + "] imported a schema [" + importedTargetNS
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

        } finally {
            // release database lock
            this.lock.writeLock().unlock();
        }
    }


    protected void processSchema(GMEXMLSchemaLoader schemaLoader, Map<URI, SchemaGrammar> processedSchemas,
        Schema schemaToProcess) throws XNIException, IOException, SchemaPersistenceGeneralException {
        LOG.debug("About to process schema [" + schemaToProcess.getNamespace() + "].");

        // 6.2. Add the schema to the model which will recursively fire
        // callbacks to the entity resolver for all imports (loading all
        // dependency schemas, and their dependency schemas, etc)
        String ns = schemaToProcess.getNamespace().toString();

        // TODO: need to check the schema is actually valid (1..n
        // schemadocuemnts exist, and the namespace of all is what is specified
        // by the schema... but can only do that after parse time)

        // TODO: how to get "root" schema
        SchemaDocument rootSD = schemaToProcess.getSchemaDocument(0);

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
        assert !processedSchemas.containsKey(schemaToProcess.getNamespace())
            || processedSchemas.get(schemaToProcess.getNamespace()) == null;

        // store the resultant schema model (this needs to happen before
        // recursion)
        processedSchemas.put(schemaToProcess.getNamespace(), model);

        String targetURI = model.getTargetNamespace();
        if (!ns.equals(targetURI)) {
            String message = "Problem processing schema submissions; the schema[" + schemaToProcess.getNamespace()
                + "] was not valid as its acutal targetURI [" + targetURI + "] did not match.";
            LOG.error(message);
            InvalidSchemaSubmission fault = new InvalidSchemaSubmission();
            fault.setFaultString(message);
            throw fault;
        }

        // 6.3. Look in the DB for depending schemas (will only be present if
        // schema was already published and is being updated)
        Collection<Schema> dependingSchemas = this.schemaPersistence
            .getDependingSchemas(schemaToProcess.getNamespace());

        // 6.4. For each depending schema not in the list of "processed schemas"
        // call processSchema()
        for (Schema dependingSchema : dependingSchemas) {
            if (processedSchemas.containsKey(dependingSchema.getNamespace())) {
                LOG.debug("Depending schema [" + dependingSchema.getNamespace()
                    + "] was already processed (or will be processed).");
            } else {
                LOG.debug("Processing depending schema [" + dependingSchema.getNamespace()
                    + "] which is not in the submission package.");
                processSchema(schemaLoader, processedSchemas, dependingSchema);
            }
        }
    }


    public URI[] getNamespaces() throws SchemaPersistenceGeneralException {
        this.lock.readLock().lock();
        try {
            Collection<URI> nsCol = this.schemaPersistence.getNamespaces();
            URI[] nsArr = new URI[nsCol.size()];
            return nsCol.toArray(nsArr);
        } finally {
            this.lock.readLock().unlock();
        }
    }
}
