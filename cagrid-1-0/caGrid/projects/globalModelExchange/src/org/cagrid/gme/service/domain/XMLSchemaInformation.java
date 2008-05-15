package org.cagrid.gme.service.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.cagrid.gme.domain.XMLSchema;
import org.hibernate.annotations.GenericGenerator;


@Entity
@GenericGenerator(name = "id-generator", strategy = "native")
@Table(name = "xmlschemas")
public class XMLSchemaInformation {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private XMLSchema schema;

    @OneToMany
    @JoinTable(name = "xmlschema_imports", joinColumns = {@JoinColumn(name = "importing_xmlschema_id")}, inverseJoinColumns = @JoinColumn(name = "imported_xmlschema_id"))
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