package org.cagrid.gme.service;

import gov.nih.nci.cagrid.common.Utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.gme.stubs.types.InvalidSchemaSubmission;
import org.cagrid.gme.test.GMETestCaseBase;
import org.cagrid.gme.test.SpringTestApplicationContextConstants;
import org.springframework.test.annotation.ExpectedException;


public class GMEAddSchemaRedefinesTestCase extends GMETestCaseBase {

    // these are loaded by Spring
    protected XMLSchema testSchemaRedefine;
    protected XMLSchema testSchemaRedefined;
    protected XMLSchema testSchemaRedefineNoNamespace;
    protected XMLSchema testInvalidSchemaRedefineWrongNamespace;
    protected XMLSchema testSchemaRedefineWrongNamespaceRedefinedOnly;


    @Override
    protected String[] getConfigLocations() {
        return (String[]) Utils.appendToArray(super.getConfigLocations(),
            SpringTestApplicationContextConstants.REDEFINES_LOCATION);
    }


    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        assertNotNull(this.testSchemaRedefine);
        assertNotNull(this.testSchemaRedefined);
        assertNotNull(this.testSchemaRedefineNoNamespace);
        assertNotNull(this.testInvalidSchemaRedefineWrongNamespace);
        assertNotNull(this.testSchemaRedefineWrongNamespaceRedefinedOnly);
    }


    public void testSchemaRedefine() throws Exception {
        List<XMLSchema> schemas = new ArrayList<XMLSchema>();
        schemas.add(this.testSchemaRedefine);
        this.gme.publishSchemas(schemas);

        Collection<URI> namespaces = this.gme.getNamespaces();
        assertEquals(1, namespaces.size());
        assertEquals(this.testSchemaRedefine.getTargetNamespace(), namespaces.iterator().next());
        assertEquals(this.testSchemaRedefine, this.gme.getSchema(this.testSchemaRedefine.getTargetNamespace()));

    }


    public void testSchemaRedefined() throws Exception {
        List<XMLSchema> schemas = new ArrayList<XMLSchema>();
        schemas.add(this.testSchemaRedefined);
        this.gme.publishSchemas(schemas);

        Collection<URI> namespaces = this.gme.getNamespaces();

        assertEquals(1, namespaces.size());
        assertEquals(this.testSchemaRedefined.getTargetNamespace(), namespaces.iterator().next());
        assertEquals(this.testSchemaRedefined, this.gme.getSchema(this.testSchemaRedefined.getTargetNamespace()));
    }


    public void testSchemaRedefineNoNamespace() throws Exception {
        List<XMLSchema> schemas = new ArrayList<XMLSchema>();
        schemas.add(this.testSchemaRedefineNoNamespace);
        this.gme.publishSchemas(schemas);

        Collection<URI> namespaces = this.gme.getNamespaces();

        assertEquals(1, namespaces.size());
        assertEquals(this.testSchemaRedefineNoNamespace.getTargetNamespace(), namespaces.iterator().next());
        assertEquals(this.testSchemaRedefineNoNamespace, this.gme.getSchema(this.testSchemaRedefineNoNamespace
            .getTargetNamespace()));
    }


    public void testSchemaRedefineWrongNamespaceRedefinedOnly() throws Exception {
        List<XMLSchema> schemas = new ArrayList<XMLSchema>();
        schemas.add(this.testSchemaRedefineWrongNamespaceRedefinedOnly);
        this.gme.publishSchemas(schemas);

        Collection<URI> namespaces = this.gme.getNamespaces();

        assertEquals(1, namespaces.size());
        assertEquals(this.testSchemaRedefineWrongNamespaceRedefinedOnly.getTargetNamespace(), namespaces.iterator()
            .next());
        assertEquals(this.testSchemaRedefineWrongNamespaceRedefinedOnly, this.gme
            .getSchema(this.testSchemaRedefineWrongNamespaceRedefinedOnly.getTargetNamespace()));
    }


    @ExpectedException(value = InvalidSchemaSubmission.class)
    public void testInvalidSchemaRedefineWrongNamespace() throws Exception {
        List<XMLSchema> schemas = new ArrayList<XMLSchema>();
        schemas.add(this.testInvalidSchemaRedefineWrongNamespace);
        this.gme.publishSchemas(schemas);
    }

}
