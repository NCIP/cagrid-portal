package org.cagrid.gme.service;

import junit.framework.TestCase;


/**
 * @author oster
 */
public class GMEServiceAddSchemaTestCase extends TestCase {
    private GME gme = null;


    public void testDummy() {
        // TODO: remove me
    }


    //
    //
    // @Override
    // protected void setUp() throws Exception {
    // super.setUp();
    // try {
    //
    // TorqueSchemaPersistenceImpl schemaPersistenceImpl = new
    // TorqueSchemaPersistenceImpl();
    //
    // File propsFile = new File("etc/torque.properties");
    // PropertiesConfiguration configuration = new
    // PropertiesConfiguration(propsFile);
    // ((ConfigurationInitilizable)
    // schemaPersistenceImpl).setConfiguration(configuration);
    // gme = new GME(schemaPersistenceImpl);
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // fail("Problem initializing the test GME:" + e.getMessage());
    // }
    //
    // }
    //
    //
    // @Override
    // protected void tearDown() throws Exception {
    // super.tearDown();
    // // TODO: need to make a gme.shutdown() which will call
    // // schemaPersistence.shutdown, which can call this
    // Torque.shutdown();
    // }
    //
    //
    // public void testAddSchema_Recursive() {
    // try {
    //
    // gme.addSchema(GMETestSchemaBundles.getSimpleRecursiveBundle());
    // } catch (Exception e) {
    // e.printStackTrace();
    // fail(e.getMessage());
    // }
    // }
    //
    //
    // public void testAddSchema_Cycle() {
    // try {
    // Schema[] simpleCycleBundle = GMETestSchemaBundles.getSimpleCycleBundle();
    //
    // gme.addSchema(simpleCycleBundle);
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // fail(e.getMessage());
    // }
    // }
    //
    //
    // public void testAddSchema_NoImports() {
    // try {
    // Schema[] schemaArr = new Schema[1];
    // Namespace ns = new Namespace(new
    // URI("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common"));
    // schemaArr[0] = new Schema(ns, IOUtils.toString(new FileInputStream(
    // "test/resources/schema/cagrid/common/common.xsd")));
    //
    // gme.addSchema(schemaArr);
    // } catch (Exception e) {
    // e.printStackTrace();
    // fail(e.getMessage());
    // }
    // }
    //
    //
    // public void testAddSchema_EmptySubmissionFailure() {
    // try {
    // Schema[] schemaArr = new Schema[0];
    //
    // try {
    // gme.addSchema(schemaArr);
    // fail("GME should have thrown exception!");
    // } catch (InvalidSchemaSubmission e) {
    // // expected
    // }
    //
    // try {
    // gme.addSchema(null);
    // fail("GME should have thrown exception!");
    // } catch (InvalidSchemaSubmission e) {
    // // expected
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // fail(e.getMessage());
    // }
    // }
    //
    //
    // public void testAddSchema_MissingImportFailure() {
    // testFailingSchema("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata",
    // "test/resources/schema/cagrid/caGridMetadata.xsd");
    // }
    //
    //
    // public void testAddSchema_WrongNamespaceFailure() {
    // testFailingSchema("gme://wrongnamespace",
    // "test/resources/schema/cagrid/common/common.xsd");
    // }
    //
    //
    // public void testAddSchema_MissingTypeFailure() {
    // testFailingSchema("gme://missingtype",
    // "test/resources/schema/invalid/missingtype.xsd");
    // }
    //
    //
    // public void testAddSchema_DuplicateElementsFailure() {
    // testFailingSchema("gme://duplicateelements",
    // "test/resources/schema/invalid/duplicateelements.xsd");
    // }
    //
    //
    // private void testFailingSchema(String namepace, String location) {
    // assertNotNull("Cannot test a null namespace.", namepace);
    // assertNotNull("Cannot test a null location.", location);
    // try {
    // Schema[] schemaArr = new Schema[1];
    // Namespace ns1 = new Namespace(new URI(namepace));
    // schemaArr[0] = new Schema(ns1, IOUtils.toString(new
    // FileInputStream(location)));
    //
    // try {
    //
    // gme.addSchema(schemaArr);
    // fail("GME should have thrown exception!");
    // } catch (InvalidSchemaSubmission e) {
    // // expected
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // fail(e.getMessage());
    // }
    // }
    //

    public static void main(String[] args) {
        junit.textui.TestRunner.run(GMEServiceAddSchemaTestCase.class);
    }
}