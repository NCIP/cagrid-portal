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
import org.cagrid.gme.persistence.SchemaPersistenceGeneralException;
import org.cagrid.gme.persistence.SchemaPersistenceI;
import org.cagrid.gme.protocol.stubs.Namespace;
import org.cagrid.gme.protocol.stubs.Schema;
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


	public void addSchema(Schema[] schemas)
		throws SchemaAlreadyExists,
		InvalidSchemaSubmission, SchemaPersistenceGeneralException {

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
			Map<Namespace, SchemaGrammar> processedSchemas = new HashMap<Namespace, SchemaGrammar>();
			for (Schema submittedSchema : schemas) {
				// verify the schema's have unique and valid namespaces
				Namespace namespace = submittedSchema.getNamespace();
				if (namespace == null || namespace.getNamespace() == null) {
					String message = "The schema submission contained a schema with a null Namespace.";
					LOG.error(message);

					InvalidSchemaSubmission e = new InvalidSchemaSubmission();
					e.setFaultString(message);

					throw e;

				}
				if (processedSchemas.containsKey(namespace)) {
					String message = "The schema submission contained multiple schemas of the same namespace ("
						+ namespace + ").";
					LOG.error(message);

					InvalidSchemaSubmission e = new InvalidSchemaSubmission();
					e.setFaultString(message);

					throw e;
				} else {
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
					// if(storeSchema.getState... != ){
					String message = "The schema [" + namespace.getNamespace()
						+ "] already exists and cannot be mofified.";
					LOG.error(message);

					SchemaAlreadyExists e = new SchemaAlreadyExists();
					e.setFaultString(message);

					throw e;
				}
			}

			// 3. Create a model with error and entity resolver (on
			// callback
			// to imports, entity resolver needs to first load schema from
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
						+ submittedSchema.getNamespace().getNamespace() + "] was not valid:" + e.getMessage();
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

			// if got here with no error, schemas are ok to persist
			// build up DB objects to commit
			Map<Schema, List<Namespace>> toCommit = new HashMap<Schema, List<Namespace>>();
			for (Schema submittedSchema : schemas) {
				// extract the schema model
				SchemaGrammar schemaGrammar = processedSchemas.get(submittedSchema.getNamespace());
				assert schemaGrammar != null;
				assert !toCommit.containsKey(submittedSchema);
				// build an import list
				List<Namespace> importList = new ArrayList<Namespace>();
				Vector importedGrammars = schemaGrammar.getImportedGrammars();
				if (importedGrammars != null) {
					for (int i = 0; i < importedGrammars.size(); i++) {
						SchemaGrammar importedSchema = (SchemaGrammar) importedGrammars.get(i);
						String importedTargetNS = importedSchema.getTargetNamespace();
						LOG.info("Schema [" + schemaGrammar.getTargetNamespace() + "] imports schema ["
							+ importedTargetNS + "]");
						try {
							importList.add(new Namespace(new URI(importedTargetNS)));
						} catch (MalformedURIException e) {
							String message = "Problem processing schema submissions; the schema ["
								+ submittedSchema.getNamespace().getNamespace() + "] imported a schema ["
								+ importedTargetNS + "] whose namespace was not valid:" + e.getMessage();
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


	protected void processSchema(GMEXMLSchemaLoader schemaLoader, Map<Namespace, SchemaGrammar> processedSchemas,
		Schema schemaToProcess) throws XNIException, IOException, SchemaPersistenceGeneralException {
		LOG.debug("About to process schema [" + schemaToProcess.getNamespace().getNamespace() + "].");

		// 6.2. Add the schema to the model which will recursively fire
		// callbacks to the entity resolver for all imports (loading all
		// dependency schemas, and their dependency schemas, etc)
		String ns = schemaToProcess.getNamespace().getNamespace().toString();

		XMLInputSource xis = new XMLInputSource(ns, ns, ns, new StringReader(schemaToProcess.getSchemaText()), "UTF-16");
		SchemaGrammar model = (SchemaGrammar) schemaLoader.loadGrammar(xis);
		if (model == null) {
			GMEErrorHandler errorHandler = schemaLoader.getErrorHandler();
			throw errorHandler.createXMLParseException();
		}

		// we should not have processed this schema before, and if the namespace
		// is in the list it should have a null model (indicating it was
		// preloaded from the submission schemas set)
		assert !processedSchemas.containsKey(schemaToProcess.getNamespace())
			|| processedSchemas.get(schemaToProcess.getNamespace()) == null;

		// store the resultant schema model (this needs to happen before
		// recursion)
		processedSchemas.put(schemaToProcess.getNamespace(), model);

		String targetNamespace = model.getTargetNamespace();
		if (!ns.equals(targetNamespace)) {
			String message = "Problem processing schema submissions; the schema["
				+ schemaToProcess.getNamespace().getNamespace() + "] was not valid as its acutal targetNamespace ["
				+ targetNamespace + "] did not match.";
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
				processSchema(schemaLoader, processedSchemas, dependingSchema);
			}
		}
	}


	public Namespace[] getNamespaces() throws SchemaPersistenceGeneralException {
		this.lock.readLock().lock();
		try {
			Collection<Namespace> nsCol = this.schemaPersistence.getNamespaces();
			Namespace[] nsArr = new Namespace[nsCol.size()];
			return nsCol.toArray(nsArr);
		} finally {
			this.lock.readLock().unlock();
		}
	}
}
