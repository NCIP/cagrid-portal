package org.cagrid.gme.persistance.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cagrid.gme.persistence.SchemaPersistenceI;
import org.cagrid.gme.protocol.stubs.Namespace;
import org.cagrid.gme.protocol.stubs.Schema;


public class FacadeSchemaPersistenceImpl implements SchemaPersistenceI {
    Map<Schema, List<Namespace>> schemaStore = null;


    public FacadeSchemaPersistenceImpl(Map<Schema, List<Namespace>> initialStore) {
        if (initialStore == null) {
            schemaStore = new HashMap<Schema, List<Namespace>>();
        } else {
            this.schemaStore = initialStore;
        }
    }


    public Schema getSchema(Namespace schemaTargetNamespace) {
        for (Schema s : this.schemaStore.keySet()) {
            if (s.getNamespace().equals(schemaTargetNamespace)) {
                return s;
            }
        }
        return null;
    }


    public Collection<Schema> getDependingSchemas(Namespace namespace) {
        ArrayList<Schema> result = new ArrayList<Schema>();
        // for each schema in the store
        for (Schema s : this.schemaStore.keySet()) {
            List<Namespace> imports = this.schemaStore.get(s);
            // if the stored schema's imports contain the namespace in
            // question, add the schema
            if (imports.contains(namespace)) {
                result.add(s);
            }
        }

        return result;
    }


    public Collection<Namespace> getNamespaces() {
        ArrayList<Namespace> result = new ArrayList<Namespace>();
        for (Schema s : this.schemaStore.keySet()) {
            result.add(s.getNamespace());
        }

        return result;
    }


    public void storeSchemas(Map<Schema, List<Namespace>> schemasToStore) {
        this.schemaStore.putAll(schemasToStore);
    }
}
