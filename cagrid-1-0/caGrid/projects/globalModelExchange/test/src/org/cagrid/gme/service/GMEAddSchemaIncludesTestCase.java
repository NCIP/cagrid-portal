package org.cagrid.gme.service;

import gov.nih.nci.cagrid.common.Utils;

import java.net.URI;

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
        assertNotNull(testSchemaInclude);
        assertNotNull(testSchemaIncludeCycle);
        assertNotNull(testSchemaIncludeNoNamespace);
    }


    public void testSchemaInclude() throws Exception {
        XMLSchema[] schemas = new XMLSchema[]{this.testSchemaInclude};
        this.gme.addSchema(schemas);
        URI[] namespaces = this.gme.getNamespaces();
        assertEquals(1, namespaces.length);
        assertEquals(this.testSchemaInclude.getTargetNamespace(), namespaces[0]);
    }


    public void testSchemaIncludeCycle() throws Exception {
        XMLSchema[] schemas = new XMLSchema[]{this.testSchemaIncludeCycle};
        this.gme.addSchema(schemas);
        URI[] namespaces = this.gme.getNamespaces();
        assertEquals(1, namespaces.length);
        assertEquals(this.testSchemaIncludeCycle.getTargetNamespace(), namespaces[0]);
    }


    public void testSchemaIncludeNoNamespace() throws Exception {
        XMLSchema[] schemas = new XMLSchema[]{this.testSchemaIncludeNoNamespace};
        this.gme.addSchema(schemas);
        URI[] namespaces = this.gme.getNamespaces();
        assertEquals(1, namespaces.length);
        assertEquals(this.testSchemaIncludeNoNamespace.getTargetNamespace(), namespaces[0]);
    }
}
