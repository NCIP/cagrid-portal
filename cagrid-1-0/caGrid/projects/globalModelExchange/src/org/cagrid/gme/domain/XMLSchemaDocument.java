package org.cagrid.gme.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class XMLSchemaDocument {

    @Column(nullable = false)
    private java.lang.String schemaText;
    // TODO: is there a way to check unique=true within the containing schema?
    // right now if you pass the same system id into the set, one will replace
    // the other
    @Column(nullable = false)
    private java.lang.String systemID;


    public java.lang.String getSchemaText() {
        return schemaText;
    }


    public void setSchemaText(java.lang.String schemaText) {
        this.schemaText = schemaText;
    }


    public java.lang.String getSystemID() {
        return systemID;
    }


    public void setSystemID(java.lang.String systemID) {
        this.systemID = systemID;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((systemID == null) ? 0 : systemID.hashCode());
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
        final XMLSchemaDocument other = (XMLSchemaDocument) obj;
        if (systemID == null) {
            if (other.systemID != null)
                return false;
        } else if (!systemID.equals(other.systemID))
            return false;
        return true;
    }

}
