package org.cagrid.gme.persistence;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.axis.types.URI;
import org.cagrid.gme.protocol.stubs.Schema;


public interface SchemaPersistenceI {

    /**
     * Returns the the Schema registered with the given namespace, or null if
     * the schema is not found.
     * 
     * @param schemaTargetNamespace
     *            the namespace of the schema of interest
     * @return null or the Schema registered with the given namespace.
     */
    public Schema getSchema(URI schemaTargetNamespace) throws SchemaPersistenceGeneralException;


    /**
     * Returns a Collection of Schemas which depend on (i.e. xs:import) the
     * schema identified by the given namespace.
     * 
     * @param namespace
     *            the targetNamespace of the schema for which depending schemas
     *            should be located
     * @return a Collection of Schemas which depend on (i.e. xs:import) the
     *         schema identified by the given namespace.
     */
    public Collection<Schema> getDependingSchemas(URI namespace) throws SchemaPersistenceGeneralException;


    /**
     * Returns the Collection of Namespaces which correspond to the
     * targetNamespaces of the schemas which are published.
     * 
     * @return the Collection of Namespaces which correspond to the
     *         targetNamespaces of the schemas which are published.
     */
    public Collection<URI> getNamespaces() throws SchemaPersistenceGeneralException;


    /**
     * Persists each Schema in map, and stores its List of Namespaces as its
     * imports
     * 
     * @param toCommit
     *            Map with Schema as key, and List of Namespaces as value, where
     *            Schemas are schemas to store and Namespaces are that Schemas
     *            imports
     */
    public void storeSchemas(Map<Schema, List<URI>> schemasToStore) throws SchemaPersistenceGeneralException;

}
