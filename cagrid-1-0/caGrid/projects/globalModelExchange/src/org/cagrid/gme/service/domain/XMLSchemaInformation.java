package org.cagrid.gme.service.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.cagrid.gme.domain.XMLSchema;
import org.hibernate.annotations.GenericGenerator;


@Entity
@GenericGenerator(name = "id-generator", strategy = "native")
public class XMLSchemaInformation {

    @Id
    @GeneratedValue
    private Long id;

    private XMLSchema schema;

    @OneToMany
    private Set<XMLSchemaInformation> imports = new HashSet<XMLSchemaInformation>();


    /**
     * @return the schema
     */
    public XMLSchema getSchema() {
        return schema;
    }


    /**
     * @param schema
     *            the schema to set
     */
    public void setSchema(XMLSchema schema) {
        this.schema = schema;
    }


    /**
     * @return the imports
     */
    public Set<XMLSchemaInformation> getImports() {
        return imports;
    }


    /**
     * @param imports
     *            the imports to set
     */
    public void setImports(Set<XMLSchemaInformation> imports) {
        this.imports = imports;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }
}