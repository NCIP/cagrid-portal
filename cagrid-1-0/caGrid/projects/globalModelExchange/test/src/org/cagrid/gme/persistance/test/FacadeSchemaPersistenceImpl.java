package org.cagrid.gme.persistance.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.types.URI;
import org.cagrid.gme.persistence.SchemaPersistenceI;
import org.cagrid.gme.protocol.stubs.Schema;


public class FacadeSchemaPersistenceImpl implements SchemaPersistenceI {
    Map<Schema, List<URI>> schemaStore = null;


    public FacadeSchemaPersistenceImpl(Map<Schema, List<URI>> initialStore) {
        if (initialStore == null) {
            schemaStore = new HashMap<Schema, List<URI>>();
        } else {
            this.schemaStore = initialStore;
        }
    }


    public Schema getSchema(URI schemaTargetURI) {
        for (Schema s : this.schemaStore.keySet()) {
            if (s.getNamespace().equals(schemaTargetURI)) {
                return s;
            }
        }
        return null;
    }


    public Collection<Schema> getDependingSchemas(URI URI) {
        ArrayList<Schema> result = new ArrayList<Schema>();
        // for each schema in the store
        for (Schema s : this.schemaStore.keySet()) {
            List<URI> imports = this.schemaStore.get(s);
            // if the stored schema's imports contain the URI in
            // question, add the schema
            if (imports.contains(URI)) {
                result.add(s);
            }
        }

        return result;
    }


    public Collection<URI> getNamespaces() {
        ArrayList<URI> result = new ArrayList<URI>();
        for (Schema s : this.schemaStore.keySet()) {
            result.add(s.getNamespace());
        }

        return result;
    }


    public void storeSchemas(Map<Schema, List<URI>> schemasToStore) {
        this.schemaStore.putAll(schemasToStore);
    }
}
