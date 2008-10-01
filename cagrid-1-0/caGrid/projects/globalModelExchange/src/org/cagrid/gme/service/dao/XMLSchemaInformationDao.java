package org.cagrid.gme.service.dao;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.gme.service.domain.XMLSchemaInformation;


public class XMLSchemaInformationDao extends AbstractDao<XMLSchemaInformation> {

    @Override
    public Class<XMLSchemaInformation> domainClass() {
        return XMLSchemaInformation.class;
    }


    public XMLSchema getXMLSchemaByTargetNamespace(URI targetNamespace) {
        XMLSchemaInformation info = getByTargetNamespace(targetNamespace);
        if (info == null) {
            return null;
        }
        return info.getSchema();
    }


    public XMLSchemaInformation getByTargetNamespace(URI targetNamespace) {
        XMLSchemaInformation s = null;

        List<XMLSchemaInformation> schemas = getHibernateTemplate().find(
            "SELECT s FROM " + domainClass().getName() + " s WHERE s.schema.targetNamespace= ?",
            new Object[]{targetNamespace});

        if (schemas.size() > 1) {
            throw new NonUniqueResultException("Found " + schemas.size() + " " + domainClass().getSimpleName()
                + " objects for URI '" + targetNamespace + "'");
        } else if (schemas.size() == 1) {
            s = schemas.get(0);
            // REVISIT: is this necessary?
            // Hibernate.initialize(s.getSchema());
            //Hibernate.initialize(s.getSchema().getAdditionalSchemaDocuments())
            // ;
        }
        return s;
    }


    public Collection<XMLSchemaInformation> getDependingXMLSchemaInformation(URI schemaTargetNamespace) {
        return getHibernateTemplate().find(
            "SELECT s FROM " + domainClass().getName()
                + " s JOIN s.imports as import WHERE import.schema.targetNamespace= ?", schemaTargetNamespace);
    }


    public Collection<XMLSchema> getDependingXMLSchemas(URI namespace) {
        Collection<XMLSchema> schemas = new ArrayList<XMLSchema>();

        Collection<XMLSchemaInformation> dependingSchemas = getDependingXMLSchemaInformation(namespace);
        for (XMLSchemaInformation info : dependingSchemas) {
            schemas.add(info.getSchema());
        }
        return schemas;
    }


    public Collection<URI> getAllNamespaces() {
        return getHibernateTemplate().find("SELECT s.schema.targetNamespace FROM " + domainClass().getName() + " s");
    }

}
