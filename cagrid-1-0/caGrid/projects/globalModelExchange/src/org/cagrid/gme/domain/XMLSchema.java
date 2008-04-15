package org.cagrid.gme.domain;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


@Entity
@GenericGenerator(name = "id-generator", strategy = "native")
public class XMLSchema {

    @CollectionOfElements
    private Set<XMLSchemaDocument> schemaDocuments = new HashSet<XMLSchemaDocument>();

    @Column(nullable = false, unique = true)
    @Type(type = "org.cagrid.gme.persistence.hibernate.types.URIUserType")
    private URI targetNamespace;

    @Id
    @GeneratedValue
    private Long id;


    public Set<XMLSchemaDocument> getSchemaDocuments() {
        return schemaDocuments;
    }


    public void setSchemaDocuments(Set<XMLSchemaDocument> schemaDocuments) {
        this.schemaDocuments = schemaDocuments;
    }


    public java.net.URI getTargetNamespace() {
        return targetNamespace;
    }


    public void setTargetNamespace(URI targetNamespace) {
        this.targetNamespace = targetNamespace;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((schemaDocuments == null) ? 0 : schemaDocuments.hashCode());
        result = prime * result + ((targetNamespace == null) ? 0 : targetNamespace.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final XMLSchema other = (XMLSchema) obj;
        if (schemaDocuments == null) {
            if (other.schemaDocuments != null)
                return false;
        } else if (!schemaDocuments.equals(other.schemaDocuments))
            return false;
        if (targetNamespace == null) {
            if (other.targetNamespace != null)
                return false;
        } else if (!targetNamespace.equals(other.targetNamespace))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return this.targetNamespace.toString();
    }
}