package org.cagrid.gme.domain;

import java.util.Set;


/**
 * This contains a collection of XMLSchemas, indexed by their respective
 * targetNamespaces, as well as a List of the targetNamespaces each of them
 * imports (also index by the importing schema's targetNamespace). This
 * information can be used to reconstruct a graph of schemas and their
 * relationships to each other. It could be processed by a library like JUNG
 * (http://jung.sourceforge.net/).
 */
public class XMLSchemaBundle {
    private Set<XMLSchema> xmlSchemaCollection;
    private Set<XMLSchemaImportInformation> importInformation;


    public void setXMLSchemas(Set<XMLSchema> xmlSchemaCollection) {
        this.xmlSchemaCollection = xmlSchemaCollection;
    }


    public void setImportInformation(Set<XMLSchemaImportInformation> importInformation) {
        this.importInformation = importInformation;
    }


    public Set<XMLSchema> getXMLSchemas() {
        return this.xmlSchemaCollection;
    }


    public Set<XMLSchemaNamespace> getXMLSchemaTargetNamespaces() {
        return null;
    }


    public XMLSchema getXMLSchema(XMLSchemaNamespace targetNamespace) {
        return null;
    }


    public Set<XMLSchema> getImportedXMLSchemas(XMLSchemaNamespace targetNamespace) {
        return null;
    }


    public Set<XMLSchemaNamespace> getImportedXMLSchemaNamespaces(XMLSchemaNamespace targetNamespace) {
        return null;
    }


    public Set<XMLSchemaImportInformation> getImportInformation() {
        return importInformation;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.importInformation == null) ? 0 : this.importInformation.hashCode());
        result = prime * result + ((this.xmlSchemaCollection == null) ? 0 : this.xmlSchemaCollection.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        XMLSchemaBundle other = (XMLSchemaBundle) obj;
        if (this.importInformation == null) {
            if (other.importInformation != null) {
                return false;
            }
        } else if (!this.importInformation.equals(other.importInformation)) {
            return false;
        }
        if (this.xmlSchemaCollection == null) {
            if (other.xmlSchemaCollection != null) {
                return false;
            }
        } else if (!this.xmlSchemaCollection.equals(other.xmlSchemaCollection)) {
            return false;
        }
        return true;
    }

}
