package org.cagrid.gme.persistence.torque;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import org.cagrid.gme.common.ConfigurationInitilizable;
import org.cagrid.gme.common.exceptions.InitializationException;
import org.cagrid.gme.persistence.SchemaPersistenceGeneralException;
import org.cagrid.gme.persistence.SchemaPersistenceI;
import org.cagrid.gme.persistence.torque.generated.SchemaEntity;
import org.cagrid.gme.persistence.torque.generated.SchemaEntityPeer;
import org.cagrid.gme.protocol.stubs.Namespace;
import org.cagrid.gme.protocol.stubs.Schema;


public class TorqueSchemaPersistenceImpl implements SchemaPersistenceI, ConfigurationInitilizable {
	protected static Log LOG = LogFactory.getLog(TorqueSchemaPersistenceImpl.class.getName());


	public TorqueSchemaPersistenceImpl() {

	}


	public Schema getSchema(Namespace schemaTargetNamespace) throws SchemaPersistenceGeneralException {
		if (schemaTargetNamespace == null || schemaTargetNamespace.getNamespace() == null) {
			return null;
		}
		SchemaEntity schemaEntity = null;
		try {
			schemaEntity = SchemaEntityPeer.getByNamespace(schemaTargetNamespace.getNamespace().toString());
			if (schemaEntity == null) {
				return null;
			}

			Schema schema = new Schema();
			Namespace ns = new Namespace(schemaTargetNamespace.getNamespace());
			schema.setNamespace(ns);
			schema.setSchemaText(schemaEntity.getSchemaText().getSchemaText());

			return schema;
		} catch (TorqueException e) {
			LOG.error(e);
			throw new SchemaPersistenceGeneralException(e);
		}

	}


	public Collection<Schema> getDependingSchemas(Namespace namespace) throws SchemaPersistenceGeneralException {

		// TODO: implement
		return new ArrayList<Schema>();

	}


	public Collection<Namespace> getNamespaces() throws SchemaPersistenceGeneralException {
		ArrayList<Namespace> result = new ArrayList<Namespace>();
		try {
			List allSchemas = SchemaEntityPeer.doSelectAll();
			for (Iterator iterator = allSchemas.iterator(); iterator.hasNext();) {
				SchemaEntity schema = (SchemaEntity) iterator.next();
				String namespaceUri = schema.getNamespaceUri();
				try {
					Namespace ns = new Namespace(new URI(namespaceUri));
					result.add(ns);
				} catch (MalformedURIException e) {
					String message = "Problem loading schema from database:" + e.getMessage();
					LOG.error(message, e);
					throw new SchemaPersistenceGeneralException(message, e);
				}
			}
		} catch (TorqueException e) {
			LOG.error(e);
			throw new SchemaPersistenceGeneralException(e);
		}

		return result;
	}


	public void storeSchemas(Map<Schema, List<Namespace>> schemasToStore) {
		// TODO implement
	}


	public void setConfiguration(Configuration config) throws InitializationException {
		try {
			if (Torque.isInit()) {
				String error = "Torque was already initialized!";
				LOG.error(error);
				throw new InitializationException(error);
			} else {
				Torque.init(config);
			}
		} catch (TorqueException e) {
			LOG.error("Problem initializing properties.", e);
			throw new InitializationException(e);
		}
	}
}
