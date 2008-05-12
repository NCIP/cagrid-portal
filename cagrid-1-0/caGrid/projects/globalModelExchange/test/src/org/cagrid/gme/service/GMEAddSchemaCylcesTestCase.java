package org.cagrid.gme.service;

import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.gme.stubs.types.InvalidSchemaSubmission;
import org.cagrid.gme.test.GMEAddSchemaTestCaseBase;
import org.springframework.test.annotation.ExpectedException;


public class GMEAddSchemaCylcesTestCase extends GMEAddSchemaTestCaseBase {

    public void setTestSchemaCycleA(XMLSchema s) {
        this.schema = s;
    }


    @ExpectedException(InvalidSchemaSubmission.class)
    public void testCycles() throws Exception {
        this.gme.addSchema(new XMLSchema[]{this.schema});
    }
}
