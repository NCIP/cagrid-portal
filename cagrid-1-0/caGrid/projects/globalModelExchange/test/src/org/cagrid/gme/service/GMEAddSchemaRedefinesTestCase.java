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
        assertNotNull(testSchemaRedefine);
        assertNotNull(testSchemaRedefined);
        assertNotNull(testSchemaRedefineNoNamespace);
        assertNotNull(testInvalidSchemaRedefineWrongNamespace);
        assertNotNull(testSchemaRedefineWrongNamespaceRedefinedOnly);
    }


    public void testSchemaRedefine() throws Exception {
        XMLSchema[] schemas = new XMLSchema[]{this.testSchemaRedefine};
        this.gme.addSchema(schemas);
        URI[] namespaces = this.gme.getNamespaces();
        assertEquals(1, namespaces.length);
        assertEquals(this.testSchemaRedefine.getTargetNamespace(), namespaces[0]);
    }


    public void testSchemaRedefined() throws Exception {
        XMLSchema[] schemas = new XMLSchema[]{this.testSchemaRedefined};
        this.gme.addSchema(schemas);
        URI[] namespaces = this.gme.getNamespaces();
        assertEquals(1, namespaces.length);
        assertEquals(this.testSchemaRedefined.getTargetNamespace(), namespaces[0]);
    }


    public void testSchemaRedefineNoNamespace() throws Exception {
        XMLSchema[] schemas = new XMLSchema[]{this.testSchemaRedefineNoNamespace};
        this.gme.addSchema(schemas);
        URI[] namespaces = this.gme.getNamespaces();
        assertEquals(1, namespaces.length);
        assertEquals(this.testSchemaRedefineNoNamespace.getTargetNamespace(), namespaces[0]);
    }


    public void testSchemaRedefineWrongNamespaceRedefinedOnly() throws Exception {
        XMLSchema[] schemas = new XMLSchema[]{this.testSchemaRedefineWrongNamespaceRedefinedOnly};
        this.gme.addSchema(schemas);
        URI[] namespaces = this.gme.getNamespaces();
        assertEquals(1, namespaces.length);
        assertEquals(this.testSchemaRedefineWrongNamespaceRedefinedOnly.getTargetNamespace(), namespaces[0]);
    }


    @ExpectedException(value = InvalidSchemaSubmission.class)
    public void testInvalidSchemaRedefineWrongNamespace() throws Exception {
        XMLSchema[] schemas = new XMLSchema[]{this.testInvalidSchemaRedefineWrongNamespace};
        this.gme.addSchema(schemas);
    }

}
