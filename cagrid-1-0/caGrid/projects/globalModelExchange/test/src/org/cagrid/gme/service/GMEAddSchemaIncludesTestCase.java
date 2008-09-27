package org.cagrid.gme.service;

import gov.nih.nci.cagrid.common.Utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.gme.test.GMETestCaseBase;
import org.cagrid.gme.test.SpringTestApplicationContextConstants;


public class GMEAddSchemaIncludesTestCase extends GMETestCaseBase {

    // these are loaded by Spring
    protected XMLSchema testSchemaInclude;
    protected XMLSchema testSchemaIncludeCycle;
    protected XMLSchema testSchemaIncludeNoNamespace;


    @Override
    protected String[] getConfigLocations() {
        return (String[]) Utils.appendToArray(super.getConfigLocations(),
            SpringTestApplicationContextConstants.INCLUDES_LOCATION);
    }


    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        assertNotNull(this.testSchemaInclude);
        assertNotNull(this.testSchemaIncludeCycle);
        assertNotNull(this.testSchemaIncludeNoNamespace);
    }


    public void testSchemaInclude() throws Exception {
        List<XMLSchema> schemas = new ArrayList<XMLSchema>();
        schemas.add(this.testSchemaInclude);
        this.gme.publishSchemas(schemas);

        Collection<URI> namespaces = this.gme.getNamespaces();
        assertEquals(1, namespaces.size());
        assertEquals(this.testSchemaInclude.getTargetNamespace(), namespaces.iterator().next());
        assertEquals(this.testSchemaInclude, this.gme.getSchema(this.testSchemaInclude.getTargetNamespace()));
    }


    public void testSchemaIncludeCycle() throws Exception {
        List<XMLSchema> schemas = new ArrayList<XMLSchema>();
        schemas.add(this.testSchemaIncludeCycle);
        this.gme.publishSchemas(schemas);

        Collection<URI> namespaces = this.gme.getNamespaces();

        assertEquals(1, namespaces.size());
        assertEquals(this.testSchemaIncludeCycle.getTargetNamespace(), namespaces.iterator().next());
        assertEquals(this.testSchemaIncludeCycle, this.gme.getSchema(this.testSchemaIncludeCycle.getTargetNamespace()));
    }


    public void testSchemaIncludeNoNamespace() throws Exception {
        List<XMLSchema> schemas = new ArrayList<XMLSchema>();
        schemas.add(this.testSchemaIncludeNoNamespace);
        this.gme.publishSchemas(schemas);

        Collection<URI> namespaces = this.gme.getNamespaces();

        assertEquals(1, namespaces.size());
        assertEquals(this.testSchemaIncludeNoNamespace.getTargetNamespace(), namespaces.iterator().next());
        assertEquals(this.testSchemaIncludeNoNamespace, this.gme.getSchema(this.testSchemaIncludeNoNamespace
            .getTargetNamespace()));
    }
}
