package org.cagrid.gme.service;

import gov.nih.nci.cagrid.common.Utils;

import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.gme.stubs.types.InvalidSchemaSubmission;
import org.cagrid.gme.test.GMETestCaseBase;
import org.cagrid.gme.test.SpringTestApplicationContextConstants;
import org.springframework.test.annotation.ExpectedException;


public class GMEAddSchemaErrorsTestCase extends GMETestCaseBase {

    // these are loaded by Spring
    protected XMLSchema testSchemaDuplicates;
    protected XMLSchema testSchemaMissingInclude;
    protected XMLSchema testSchemaMissingType;
    protected XMLSchema testSchemaNoNamespace;
    protected XMLSchema testSchemaWrongNamespace;
    protected XMLSchema testSchemaNoImports;


    @Override
    protected String[] getConfigLocations() {
        return (String[]) Utils.appendToArray(super.getConfigLocations(),
            SpringTestApplicationContextConstants.ERRORS_LOCATION);
    }


    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        assertNotNull(testSchemaDuplicates);
        assertNotNull(testSchemaMissingInclude);
        assertNotNull(testSchemaMissingType);
        assertNotNull(testSchemaNoNamespace);
        assertNotNull(testSchemaWrongNamespace);
        assertNotNull(testSchemaNoImports);
    }


    @ExpectedException(InvalidSchemaSubmission.class)
    public void testEmptySubmission() throws Exception {
        this.gme.addSchema(new XMLSchema[]{});
    }


    @ExpectedException(InvalidSchemaSubmission.class)
    public void testNullSubmission() throws Exception {
        this.gme.addSchema(null);
    }


    @ExpectedException(InvalidSchemaSubmission.class)
    public void testSchemaDuplicates() throws Exception {
        this.gme.addSchema(new XMLSchema[]{this.testSchemaDuplicates});
    }


    @ExpectedException(InvalidSchemaSubmission.class)
    public void testSchemaMissingInclude() throws Exception {
        this.gme.addSchema(new XMLSchema[]{this.testSchemaMissingInclude});
    }


    @ExpectedException(InvalidSchemaSubmission.class)
    public void testSchemaMissingType() throws Exception {
        this.gme.addSchema(new XMLSchema[]{this.testSchemaMissingType});
    }


    @ExpectedException(InvalidSchemaSubmission.class)
    public void testSchemaNoNamespace() throws Exception {
        this.gme.addSchema(new XMLSchema[]{this.testSchemaNoNamespace});
    }


    @ExpectedException(InvalidSchemaSubmission.class)
    public void testSchemaWrongNamespace() throws Exception {
        this.gme.addSchema(new XMLSchema[]{this.testSchemaWrongNamespace});
    }


    @ExpectedException(InvalidSchemaSubmission.class)
    public void testSchemaNoImports() throws Exception {
        this.gme.addSchema(new XMLSchema[]{this.testSchemaNoImports});
    }

}
