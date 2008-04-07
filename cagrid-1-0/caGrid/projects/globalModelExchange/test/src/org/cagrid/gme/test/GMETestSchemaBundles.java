package org.cagrid.gme.test;

import java.io.File;

import org.apache.axis.types.URI;
import org.cagrid.gme.common.XSDUtil;
import org.cagrid.gme.protocol.stubs.Schema;


/**
 * GMETestSchemaBundles
 * 
 * @author oster
 * @created Sep 13, 2007 4:28:36 PM
 * @version $Id: GMETestSchemaBundles.java,v 1.2 2008-04-07 02:00:04 oster Exp $
 */
public class GMETestSchemaBundles {

    public static final String SCHEMA_RESOURCES_DIR = "test/resources/schema/";

    // Simple Recursive
    public static final String URI_SIMPLE_REC_A = "gme://a";
    public static final String FILE_SIMPLE_REC_A = SCHEMA_RESOURCES_DIR + "simple/A.xsd";
    public static final String URI_SIMPLE_REC_B = "gme://b";
    public static final String FILE_SIMPLE_REC_B = SCHEMA_RESOURCES_DIR + "simple/B.xsd";
    public static final String URI_SIMPLE_REC_C = "gme://c";
    public static final String FILE_SIMPLE_REC_C = SCHEMA_RESOURCES_DIR + "simple/C.xsd";

    // Simple Cycle
    public static final String URI_SIMPLE_CYCLE_A = "gme://a";
    public static final String FILE_SIMPLE_CYCLE_A = SCHEMA_RESOURCES_DIR + "cycle/A.xsd";
    public static final String URI_SIMPLE_CYCLE_B = "gme://b";
    public static final String FILE_SIMPLE_CYCLE_B = SCHEMA_RESOURCES_DIR + "cycle/B.xsd";

    // Simple Include
    public static final String URI_SIMPLE_INCLUDE = "gme://a";
    public static final String FILE_SIMPLE_INCLUDE_INCLUDER = SCHEMA_RESOURCES_DIR + "include/Includer.xsd";
    public static final String FILE_SIMPLE_INCLUDE_INCLUDED = SCHEMA_RESOURCES_DIR + "include/Included.xsd";


    private GMETestSchemaBundles() {
    }


    public static Schema[] getSimpleRecursiveBundle() throws TestInstantiationException {
        try {
            Schema[] schemaArr = new Schema[3];
            URI ns1 = new URI(URI_SIMPLE_REC_A);
            URI ns2 = new URI(URI_SIMPLE_REC_B);
            URI ns3 = new URI(URI_SIMPLE_REC_C);
            schemaArr[0] = XSDUtil.createSchema(ns1, new File(FILE_SIMPLE_REC_A));
            schemaArr[1] = XSDUtil.createSchema(ns2, new File(FILE_SIMPLE_REC_B));
            schemaArr[2] = XSDUtil.createSchema(ns3, new File(FILE_SIMPLE_REC_C));

            return schemaArr;
        } catch (Exception e) {
            throw new TestInstantiationException("Problem creating schema bundle.", e);
        }
    }


    public static Schema[] getSimpleCycleBundle() throws TestInstantiationException {
        try {
            Schema[] schemaArr = new Schema[2];
            URI ns1 = new URI(URI_SIMPLE_CYCLE_A);
            URI ns2 = new URI(URI_SIMPLE_CYCLE_B);
            schemaArr[0] = XSDUtil.createSchema(ns1, new File(FILE_SIMPLE_CYCLE_A));
            schemaArr[1] = XSDUtil.createSchema(ns2, new File(FILE_SIMPLE_CYCLE_B));

            return schemaArr;
        } catch (Exception e) {
            throw new TestInstantiationException("Problem creating schema bundle.", e);
        }
    }


    public static Schema[] getSimpleIncludeBundle() throws TestInstantiationException {
        try {
            Schema[] schemaArr = new Schema[1];
            URI ns1 = new URI(URI_SIMPLE_INCLUDE);
            schemaArr[0] = XSDUtil.createSchema(ns1, new File[]{new File(FILE_SIMPLE_INCLUDE_INCLUDER),
                    new File(FILE_SIMPLE_INCLUDE_INCLUDED)});

            return schemaArr;
        } catch (Exception e) {
            throw new TestInstantiationException("Problem creating schema bundle.", e);
        }
    }
}
