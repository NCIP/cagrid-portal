package org.cagrid.gme.persistence.hibernate;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.gme.persistence.SchemaPersistenceGeneralException;
import org.cagrid.gme.persistence.SchemaPersistenceI;
import org.hibernate.SessionFactory;


public class HibernateSchemaPersistence implements SchemaPersistenceI {

    private SessionFactory sessionFactory;


    public HibernateSchemaPersistence(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public Collection<XMLSchema> getDependingSchemas(URI namespace) throws SchemaPersistenceGeneralException {
        // TODO Auto-generated method stub
        return null;
    }


    public Collection<URI> getNamespaces() throws SchemaPersistenceGeneralException {
        // TODO Auto-generated method stub
        return null;
    }


    public XMLSchema getSchema(URI schemaTargetNamespace) throws SchemaPersistenceGeneralException {
        // TODO Auto-generated method stub
        return null;
    }


    public void storeSchemas(Map<XMLSchema, List<URI>> schemasToStore) throws SchemaPersistenceGeneralException {

        // foreach XMLSchema
        // -find PersistableXMLSchema (by URI), create if null, save
        // -put in hash of URI->PersistableXMLSchema
        // -foreach URI in import List
        // --if not in hash
        // --- getReference to PersistableXMLSchema, put in hash

        // foreach XMLSchema
        // -get PersistableXMLSchema from hash (assert not null)
        // -setSchema XMLSchema on PersistableXMLSchema
        // -foreach URI in import List
        // --get PersistableXMLSchema from hash (assert not null), add to
        // importSet
        // -set importSet on PersistableXMLSchema

    }

}
