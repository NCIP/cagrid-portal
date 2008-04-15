package org.cagrid.gme.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;


// TODO: should this be Schema or URI based?
// Schema is more convenient for use and hibernate can probably handle fine, but
// is much less efficient when returning over the grid (i.e., returning a
// namespace tree rather than a fully populated tree of schemas, which might
// have multiple copies of the same schema)
// I will probably want to return a list of schema, and schemaimportinformation
// ideally Schema would have getImportedSchemas, but I want to use the same
// object for submission and dont want that to be there
// can use objects and do this:
// http://www.castor.org/how-to-use-references-in-xml.html
@Entity
@GenericGenerator(name = "id-generator", strategy = "foreign", parameters = {@org.hibernate.annotations.Parameter(name = "property", value = "schema")})
public class XMLSchemaImports {

    @OneToOne(optional = false)
    private XMLSchema schema;

    @OneToMany
    private Set<XMLSchema> imports;
    @Id
    @GeneratedValue
    private Long id;


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((imports == null) ? 0 : imports.hashCode());
        result = prime * result + ((schema == null) ? 0 : schema.hashCode());
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
        final XMLSchemaImports other = (XMLSchemaImports) obj;
        if (imports == null) {
            if (other.imports != null)
                return false;
        } else if (!imports.equals(other.imports))
            return false;
        if (schema == null) {
            if (other.schema != null)
                return false;
        } else if (!schema.equals(other.schema))
            return false;
        return true;
    }


    public XMLSchema getSchema() {
        return schema;
    }


    public void setSchema(XMLSchema schema) {
        this.schema = schema;
    }


    public Set<XMLSchema> getImports() {
        return imports;
    }


    public void setImports(Set<XMLSchema> imports) {
        this.imports = imports;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

}
