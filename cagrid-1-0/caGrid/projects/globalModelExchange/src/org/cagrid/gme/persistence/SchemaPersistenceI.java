package org.cagrid.gme.persistence;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cagrid.gme.domain.XMLSchema;


public interface SchemaPersistenceI {

    /**
     * Returns the the Schema registered with the given namespace, or null if
     * the schema is not found.
     * 
     * @param schemaTargetNamespace
     *            the namespace of the schema of interest
     * @return null or the Schema registered with the given namespace.
     */
    public XMLSchema getSchema(URI schemaTargetNamespace) throws SchemaPersistenceGeneralException;


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
    public Collection<XMLSchema> getDependingSchemas(URI namespace) throws SchemaPersistenceGeneralException;


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
     *            Schemas are schemas to store and Namespaces are that Schema's
     *            imports
     */
    public void storeSchemas(Map<XMLSchema, List<URI>> schemasToStore) throws SchemaPersistenceGeneralException;

}
