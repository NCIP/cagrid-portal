package org.cagrid.gme.service;

import java.net.URI;
import java.util.Arrays;

import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.gme.stubs.types.InvalidSchemaSubmission;
import org.cagrid.gme.test.GMETestCaseBase;
import org.springframework.test.annotation.ExpectedException;


public class GMEAddSchemaCyclesTestCase extends GMETestCaseBase {

    protected XMLSchema testSchemaCycleA;
    protected XMLSchema testSchemaCycleB;


    @ExpectedException(InvalidSchemaSubmission.class)
    public void testCycleAMissingDocumentB() throws Exception {
        this.gme.addSchema(new XMLSchema[]{this.testSchemaCycleA});
    }


    @ExpectedException(InvalidSchemaSubmission.class)
    public void testCycleBMissingDocumentA() throws Exception {
        this.gme.addSchema(new XMLSchema[]{this.testSchemaCycleB});
    }


    public void testCyclesAFirst() throws Exception {
        XMLSchema[] schemas = new XMLSchema[]{this.testSchemaCycleA, this.testSchemaCycleB};
        this.gme.addSchema(schemas);
        URI[] namespaces = new URI[]{this.testSchemaCycleA.getTargetNamespace(),
                this.testSchemaCycleB.getTargetNamespace()};
        URI[] gmenamespaces = this.gme.getNamespaces();
        Arrays.sort(namespaces);
        Arrays.sort(gmenamespaces);
        assertTrue(Arrays.equals(namespaces, gmenamespaces));
    }


    public void testCyclesBFirst() throws Exception {
        XMLSchema[] schemas = new XMLSchema[]{this.testSchemaCycleB, this.testSchemaCycleA};
        this.gme.addSchema(schemas);
        URI[] namespaces = new URI[]{this.testSchemaCycleA.getTargetNamespace(),
                this.testSchemaCycleB.getTargetNamespace()};
        URI[] gmenamespaces = this.gme.getNamespaces();
        Arrays.sort(namespaces);
        Arrays.sort(gmenamespaces);
        Arrays.equals(namespaces, gmenamespaces);
    }
}
