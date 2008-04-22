package org.cagrid.gme.persistence.hibernate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.axis.types.URI;
import org.cagrid.gme.persistence.SchemaPersistenceGeneralException;
import org.cagrid.gme.persistence.SchemaPersistenceI;
import org.cagrid.gme.protocol.stubs.Schema;
import org.hibernate.SessionFactory;


public class HibernateSchemaPersistence implements SchemaPersistenceI {

    private SessionFactory sessionFactory;


    public HibernateSchemaPersistence(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public Collection<Schema> getDependingSchemas(URI namespace) throws SchemaPersistenceGeneralException {
        // TODO Auto-generated method stub
        return null;
    }


    public Collection<URI> getNamespaces() throws SchemaPersistenceGeneralException {
        // TODO Auto-generated method stub
        return null;
    }


    public Schema getSchema(URI schemaTargetNamespace) throws SchemaPersistenceGeneralException {
        // TODO Auto-generated method stub
        return null;
    }


    public void storeSchemas(Map<Schema, List<URI>> schemasToStore) throws SchemaPersistenceGeneralException {
        // TODO Auto-generated method stub

    }

}
