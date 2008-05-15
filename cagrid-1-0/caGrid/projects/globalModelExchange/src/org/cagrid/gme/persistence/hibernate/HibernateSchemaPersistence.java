package org.cagrid.gme.persistence.hibernate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.gme.persistence.SchemaPersistenceGeneralException;
import org.cagrid.gme.persistence.SchemaPersistenceI;
import org.cagrid.gme.service.dao.XMLSchemaInformationDao;
import org.cagrid.gme.service.domain.XMLSchemaInformation;


// TODO: should this be renamed to DaoBasedSchemaPersistence
// REVISIT: or should it be removed all together?
public class HibernateSchemaPersistence implements SchemaPersistenceI {

    private XMLSchemaInformationDao schemaInformationDao;


    public HibernateSchemaPersistence(XMLSchemaInformationDao schemaInformationDao) {
        this.schemaInformationDao = schemaInformationDao;
    }


    /**
     * @return the schemaInformationDao
     */
    protected XMLSchemaInformationDao getSchemaInformationDao() {
        return schemaInformationDao;
    }


    public Collection<XMLSchema> getDependingSchemas(URI namespace) throws SchemaPersistenceGeneralException {
        Collection<XMLSchema> schemas = new ArrayList<XMLSchema>();

        Collection<XMLSchemaInformation> dependingSchemas = getSchemaInformationDao().getDependingSchemas(namespace);
        for (XMLSchemaInformation info : dependingSchemas) {
            schemas.add(info.getSchema());
        }
        return schemas;
    }


    public Collection<URI> getNamespaces() throws SchemaPersistenceGeneralException {
        return getSchemaInformationDao().getAllNamespaces();
    }


    public XMLSchema getSchema(URI schemaTargetNamespace) throws SchemaPersistenceGeneralException {
        return getSchemaInformationDao().getXMLSchemaByTargetNamespace(schemaTargetNamespace);
    }


    public void storeSchemas(Map<XMLSchema, List<URI>> schemasToStore) throws SchemaPersistenceGeneralException {
        // REVISIT: is there a simpler way to do this

        // this is a list of newly persistent XMLSchemaInformation (for those
        // which are being saved), and already persistent XMLSchemaInformation
        // (for those that are being imported and not updated)
        Map<URI, XMLSchemaInformation> persistedInfos = new HashMap<URI, XMLSchemaInformation>();

        // foreach XMLSchema
        for (XMLSchema s : schemasToStore.keySet()) {
            // find PersistableXMLSchema (by URI), create if null, save
            XMLSchemaInformation info = getSchemaInformationDao().getByTargetNamespace(s.getTargetNamespace());
            if (info == null) {
                info = new XMLSchemaInformation();
            }
            // -setSchema XMLSchema on PersistableXMLSchema
            info.setSchema(s);

            getSchemaInformationDao().save(info);

            // -put in hash of URI->XMLSchemaInformation
            persistedInfos.put(s.getTargetNamespace(), info);
        }
        // all new/updated schemas are now in the hash and persistent

        // foreach XMLSchema (make the changes)
        for (XMLSchema s : schemasToStore.keySet()) {
            // -get PersistableXMLSchema from hash
            XMLSchemaInformation info = persistedInfos.get(s.getTargetNamespace());

            Set<XMLSchemaInformation> importSet = new HashSet<XMLSchemaInformation>();
            List<URI> importList = schemasToStore.get(s);
            // -foreach URI in import List
            for (URI importedURI : importList) {
                // --if not in hash
                XMLSchemaInformation importedInfo = persistedInfos.get(importedURI);
                if (importedInfo == null) {
                    // --- getReference to PersistableXMLSchema, put in hash
                    importedInfo = getSchemaInformationDao().getByTargetNamespace(s.getTargetNamespace());
                    // this must either be new and already in the hash (the
                    // containing if), or existing and therefore in the db
                    // already
                    assert importedInfo != null;
                    persistedInfos.put(s.getTargetNamespace(), importedInfo);
                }
                // --add toimportSet
                importSet.add(importedInfo);
            }

            // -set importSet on PersistableXMLSchema
            info.setImports(importSet);
        }

    }
}
