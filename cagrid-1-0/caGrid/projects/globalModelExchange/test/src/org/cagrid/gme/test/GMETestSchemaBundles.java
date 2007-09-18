package org.cagrid.gme.test;

import java.io.FileInputStream;

import org.apache.axis.types.URI;
import org.apache.commons.io.IOUtils;
import org.cagrid.gme.protocol.stubs.Namespace;
import org.cagrid.gme.protocol.stubs.Schema;


/**
 * GMETestSchemaBundles
 * 
 * @author oster
 * @created Sep 13, 2007 4:28:36 PM
 * @version $Id: GMETestSchemaBundles.java,v 1.1 2007-09-18 03:48:12 oster Exp $
 */
public class GMETestSchemaBundles {

    public static final String SCHEMA_RESOURCES_DIR = "test/resources/schema/";

    // Simple Recursive
    public static final String NAMESPACE_SIMPLE_REC_A = "gme://a";
    public static final String FILE_SIMPLE_REC_A = SCHEMA_RESOURCES_DIR + "simple/A.xsd";
    public static final String NAMESPACE_SIMPLE_REC_B = "gme://b";
    public static final String FILE_SIMPLE_REC_B = SCHEMA_RESOURCES_DIR + "simple/B.xsd";
    public static final String NAMESPACE_SIMPLE_REC_C = "gme://c";
    public static final String FILE_SIMPLE_REC_C = SCHEMA_RESOURCES_DIR + "simple/C.xsd";

    // Simple Cycle
    public static final String NAMESPACE_SIMPLE_CYCLE_A = "gme://a";
    public static final String FILE_SIMPLE_CYCLE_A = SCHEMA_RESOURCES_DIR + "cycle/A.xsd";
    public static final String NAMESPACE_SIMPLE_CYCLE_B = "gme://b";
    public static final String FILE_SIMPLE_CYCLE_B = SCHEMA_RESOURCES_DIR + "cycle/B.xsd";

    // Simple Include
    public static final String NAMESPACE_SIMPLE_INCLUDE_A = "gme://a";
    public static final String FILE_SIMPLE_INCLUDE_A = SCHEMA_RESOURCES_DIR + "include/A.xsd";
    public static final String FILE_SIMPLE_INCLUDE_INCLUDE = SCHEMA_RESOURCES_DIR + "include/include.xsd";


    private GMETestSchemaBundles() {
    }


    public static Schema[] getSimpleRecursiveBundle() throws TestInstantiationException {
        try {
            Schema[] schemaArr = new Schema[3];
            Namespace ns1 = new Namespace(new URI(NAMESPACE_SIMPLE_REC_A));
            Namespace ns2 = new Namespace(new URI(NAMESPACE_SIMPLE_REC_B));
            Namespace ns3 = new Namespace(new URI(NAMESPACE_SIMPLE_REC_C));
            schemaArr[0] = new Schema(ns1, IOUtils.toString(new FileInputStream(FILE_SIMPLE_REC_A)));
            schemaArr[1] = new Schema(ns2, IOUtils.toString(new FileInputStream(FILE_SIMPLE_REC_B)));
            schemaArr[2] = new Schema(ns3, IOUtils.toString(new FileInputStream(FILE_SIMPLE_REC_C)));
            return schemaArr;
        } catch (Exception e) {
            throw new TestInstantiationException("Problem creating schema bundle.", e);
        }
    }


    public static Schema[] getSimpleCycleBundle() throws TestInstantiationException {
        try {
            Schema[] schemaArr = new Schema[2];
            Namespace ns1 = new Namespace(new URI(NAMESPACE_SIMPLE_CYCLE_A));
            Namespace ns2 = new Namespace(new URI(NAMESPACE_SIMPLE_CYCLE_B));
            schemaArr[0] = new Schema(ns1, IOUtils.toString(new FileInputStream(FILE_SIMPLE_CYCLE_A)));
            schemaArr[1] = new Schema(ns2, IOUtils.toString(new FileInputStream(FILE_SIMPLE_CYCLE_B)));
            return schemaArr;
        } catch (Exception e) {
            throw new TestInstantiationException("Problem creating schema bundle.", e);
        }
    }
    
    public static Schema[] getSimpleIncludeBundle() throws TestInstantiationException {
        try {
            Schema[] schemaArr = new Schema[2];
            Namespace ns1 = new Namespace(new URI(NAMESPACE_SIMPLE_INCLUDE_A));
            Namespace ns2 = new Namespace(new URI(NAMESPACE_SIMPLE_INCLUDE_A));
            schemaArr[0] = new Schema(ns1, IOUtils.toString(new FileInputStream(FILE_SIMPLE_INCLUDE_A)));
            schemaArr[1] = new Schema(ns2, IOUtils.toString(new FileInputStream(FILE_SIMPLE_INCLUDE_INCLUDE)));
            return schemaArr;
        } catch (Exception e) {
            throw new TestInstantiationException("Problem creating schema bundle.", e);
        }
    }


}
