package org.cagrid.gme.test;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.cagrid.gme.common.XSDUtil;
import org.cagrid.gme.domain.XMLSchema;


/**
 * GMETestSchemaBundles
 * 
 * @author oster
 * @created Sep 13, 2007 4:28:36 PM
 * @version $Id: GMETestSchemaBundles.java,v 1.3 2008-05-12 15:39:32 oster Exp $
 */
public class GMETestSchemaBundles {

    public static final String XMLSchema_RESOURCES_DIR = "test/resources/schema/";

    // Simple Recursive
    public static final String URI_SIMPLE_REC_A = "gme://a";
    public static final String FILE_SIMPLE_REC_A = XMLSchema_RESOURCES_DIR + "simple/A.xsd";
    public static final String URI_SIMPLE_REC_B = "gme://b";
    public static final String FILE_SIMPLE_REC_B = XMLSchema_RESOURCES_DIR + "simple/B.xsd";
    public static final String URI_SIMPLE_REC_C = "gme://c";
    public static final String FILE_SIMPLE_REC_C = XMLSchema_RESOURCES_DIR + "simple/C.xsd";

    // Simple Cycle
    public static final String URI_SIMPLE_CYCLE_A = "gme://a";
    public static final String FILE_SIMPLE_CYCLE_A = XMLSchema_RESOURCES_DIR + "cycle/A.xsd";
    public static final String URI_SIMPLE_CYCLE_B = "gme://b";
    public static final String FILE_SIMPLE_CYCLE_B = XMLSchema_RESOURCES_DIR + "cycle/B.xsd";

    // Simple Include
    public static final String URI_SIMPLE_INCLUDE = "gme://a";
    public static final String FILE_SIMPLE_INCLUDE_INCLUDER = XMLSchema_RESOURCES_DIR + "include/Includer.xsd";
    public static final String FILE_SIMPLE_INCLUDE_INCLUDED = XMLSchema_RESOURCES_DIR + "include/Included.xsd";


    private GMETestSchemaBundles() {
    }


    public static XMLSchema[] getSimpleRecursiveBundle() throws TestInstantiationException {
        try {
            XMLSchema[] XMLSchemaArr = new XMLSchema[3];
            URI ns1 = new URI(URI_SIMPLE_REC_A);
            URI ns2 = new URI(URI_SIMPLE_REC_B);
            URI ns3 = new URI(URI_SIMPLE_REC_C);
            XMLSchemaArr[0] = XSDUtil.createSchema(ns1, new File(FILE_SIMPLE_REC_A));
            XMLSchemaArr[1] = XSDUtil.createSchema(ns2, new File(FILE_SIMPLE_REC_B));
            XMLSchemaArr[2] = XSDUtil.createSchema(ns3, new File(FILE_SIMPLE_REC_C));

            return XMLSchemaArr;
        } catch (Exception e) {
            throw new TestInstantiationException("Problem creating XMLSchema bundle.", e);
        }
    }


    public static XMLSchema[] getSimpleCycleBundle() throws TestInstantiationException {
        try {
            XMLSchema[] XMLSchemaArr = new XMLSchema[2];
            URI ns1 = new URI(URI_SIMPLE_CYCLE_A);
            URI ns2 = new URI(URI_SIMPLE_CYCLE_B);
            XMLSchemaArr[0] = XSDUtil.createSchema(ns1, new File(FILE_SIMPLE_CYCLE_A));
            XMLSchemaArr[1] = XSDUtil.createSchema(ns2, new File(FILE_SIMPLE_CYCLE_B));

            return XMLSchemaArr;
        } catch (Exception e) {
            throw new TestInstantiationException("Problem creating XMLSchema bundle.", e);
        }
    }


    public static XMLSchema[] getSimpleIncludeBundle() throws TestInstantiationException {
        try {
            XMLSchema[] XMLSchemaArr = new XMLSchema[1];
            URI ns1 = new URI(URI_SIMPLE_INCLUDE);
            List<File> files = new ArrayList<File>();
            files.add(new File(FILE_SIMPLE_INCLUDE_INCLUDER));
            files.add(new File(FILE_SIMPLE_INCLUDE_INCLUDED));

            XMLSchemaArr[0] = XSDUtil.createSchema(ns1, files);

            return XMLSchemaArr;
        } catch (Exception e) {
            throw new TestInstantiationException("Problem creating XMLSchema bundle.", e);
        }
    }
}
