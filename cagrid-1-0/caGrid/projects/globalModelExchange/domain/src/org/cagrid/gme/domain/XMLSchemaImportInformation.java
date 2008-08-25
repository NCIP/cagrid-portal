package org.cagrid.gme.domain;

import java.util.List;


public class XMLSchemaImportInformation {
    private XMLSchemaNamespace targetNamespace;
    private List<XMLSchemaNamespace> imports;


    public XMLSchemaNamespace getTargetNamespace() {
        return this.targetNamespace;
    }


    public void setTargetNamespace(XMLSchemaNamespace targetNamespace) {
        this.targetNamespace = targetNamespace;
    }


    public List<XMLSchemaNamespace> getImports() {
        return this.imports;
    }


    public void setImports(List<XMLSchemaNamespace> imports) {
        this.imports = imports;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.imports == null) ? 0 : this.imports.hashCode());
        result = prime * result + ((this.targetNamespace == null) ? 0 : this.targetNamespace.hashCode());
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
        XMLSchemaImportInformation other = (XMLSchemaImportInformation) obj;
        if (this.imports == null) {
            if (other.imports != null) {
                return false;
            }
        } else if (!this.imports.equals(other.imports)) {
            return false;
        }
        if (this.targetNamespace == null) {
            if (other.targetNamespace != null) {
                return false;
            }
        } else if (!this.targetNamespace.equals(other.targetNamespace)) {
            return false;
        }
        return true;
    }

}