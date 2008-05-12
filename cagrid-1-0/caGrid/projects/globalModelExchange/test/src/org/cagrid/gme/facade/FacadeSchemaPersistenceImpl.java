package org.cagrid.gme.facade;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.gme.persistence.SchemaPersistenceI;


public class FacadeSchemaPersistenceImpl implements SchemaPersistenceI {
    Map<XMLSchema, List<URI>> schemaStore = null;


    public FacadeSchemaPersistenceImpl(Map<XMLSchema, List<URI>> initialStore) {
        if (initialStore == null) {
            schemaStore = new HashMap<XMLSchema, List<URI>>();
        } else {
            this.schemaStore = initialStore;
        }
    }


    public XMLSchema getSchema(URI schemaTargetURI) {
        for (XMLSchema s : this.schemaStore.keySet()) {
            if (s.getTargetNamespace().equals(schemaTargetURI)) {
                return s;
            }
        }
        return null;
    }


    public Collection<XMLSchema> getDependingSchemas(URI URI) {
        ArrayList<XMLSchema> result = new ArrayList<XMLSchema>();
        // for each schema in the store
        for (XMLSchema s : this.schemaStore.keySet()) {
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
        for (XMLSchema s : this.schemaStore.keySet()) {
            result.add(s.getTargetNamespace());
        }

        return result;
    }


    public void storeSchemas(Map<XMLSchema, List<URI>> schemasToStore) {
        this.schemaStore.putAll(schemasToStore);
    }
}
