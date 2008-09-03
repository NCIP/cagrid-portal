package org.cagrid.gme.service;

import gov.nih.nci.cagrid.common.Utils;

import java.net.URI;

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
        XMLSchema[] schemas = new XMLSchema[]{this.testSchemaRedefine};
        this.gme.addSchema(schemas);
        URI[] namespaces = this.gme.getNamespaces();
        assertEquals(1, namespaces.length);
        assertEquals(this.testSchemaRedefine.getTargetNamespace(), namespaces[0]);
        assertEquals(this.testSchemaRedefine, this.gme.getSchema(this.testSchemaRedefine.getTargetNamespace()));

    }


    public void testSchemaRedefined() throws Exception {
        XMLSchema[] schemas = new XMLSchema[]{this.testSchemaRedefined};
        this.gme.addSchema(schemas);
        URI[] namespaces = this.gme.getNamespaces();
        assertEquals(1, namespaces.length);
        assertEquals(this.testSchemaRedefined.getTargetNamespace(), namespaces[0]);
        assertEquals(this.testSchemaRedefined, this.gme.getSchema(this.testSchemaRedefined.getTargetNamespace()));
    }


    public void testSchemaRedefineNoNamespace() throws Exception {
        XMLSchema[] schemas = new XMLSchema[]{this.testSchemaRedefineNoNamespace};
        this.gme.addSchema(schemas);
        URI[] namespaces = this.gme.getNamespaces();
        assertEquals(1, namespaces.length);
        assertEquals(this.testSchemaRedefineNoNamespace.getTargetNamespace(), namespaces[0]);
        assertEquals(this.testSchemaRedefineNoNamespace, this.gme.getSchema(this.testSchemaRedefineNoNamespace
            .getTargetNamespace()));
    }


    public void testSchemaRedefineWrongNamespaceRedefinedOnly() throws Exception {
        XMLSchema[] schemas = new XMLSchema[]{this.testSchemaRedefineWrongNamespaceRedefinedOnly};
        this.gme.addSchema(schemas);
        URI[] namespaces = this.gme.getNamespaces();
        assertEquals(1, namespaces.length);
        assertEquals(this.testSchemaRedefineWrongNamespaceRedefinedOnly.getTargetNamespace(), namespaces[0]);
        assertEquals(this.testSchemaRedefineWrongNamespaceRedefinedOnly, this.gme
            .getSchema(this.testSchemaRedefineWrongNamespaceRedefinedOnly.getTargetNamespace()));
    }


    @ExpectedException(value = InvalidSchemaSubmission.class)
    public void testInvalidSchemaRedefineWrongNamespace() throws Exception {
        XMLSchema[] schemas = new XMLSchema[]{this.testInvalidSchemaRedefineWrongNamespace};
        this.gme.addSchema(schemas);
    }

}
