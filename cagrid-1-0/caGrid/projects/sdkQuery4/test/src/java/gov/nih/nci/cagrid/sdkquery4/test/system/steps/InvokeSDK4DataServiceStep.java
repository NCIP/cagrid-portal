package gov.nih.nci.cagrid.sdkquery4.test.system.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.cqlresultset.TargetAttribute;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;

/** 
 *  InvokeSDK4DataServiceStep
 *  Step invokes the SDK4 Data service
 *  and verifies results
 * 
 * @author David Ervin
 * 
 * @created Feb 1, 2008 9:02:20 AM
 * @version $Id: InvokeSDK4DataServiceStep.java,v 1.10 2008-02-06 18:55:10 dervin Exp $ 
 */
public class InvokeSDK4DataServiceStep extends Step {
    public static final String TEST_RESOURCES_DIR = "/test/resources/";
    public static final String TEST_QUERIES_DIR = TEST_RESOURCES_DIR + "testQueries/";
    public static final String TEST_RESULTS_DIR = TEST_RESOURCES_DIR + "testGoldResults/";
    
    private static Logger LOG = Logger.getLogger(InvokeSDK4DataServiceStep.class);
    
    private ServiceContainer container;
    private DataTestCaseInfo testInfo;

    public InvokeSDK4DataServiceStep(ServiceContainer container, DataTestCaseInfo testInfo) {
        this.container = container;
        this.testInfo = testInfo;
    }


    public void runStep() throws Throwable {
        testUndergraduateStudentWithName();
        testAllPayments();
        testDistinctAttributeFromCash();
        testAssociationNotNull();
        testAssociationWithAttributeEqual();
        testGroupOfAttributesUsingAnd();
        testGroupOfAttributesUsingOr();
    }
    
    
    private void testUndergraduateStudentWithName() {
        LOG.debug("testUndergraduateStudentWithName");
        CQLQuery query = loadQuery("undergraduateStudentWithName.xml");
        CQLQueryResults results = loadQueryResults("goldUndergraduateStudentWithName.xml");
        invokeValidQueryValidResults(query, results);
    }
    
    
    private void testAllPayments() {
        LOG.debug("testAllPayments");
        CQLQuery query = loadQuery("allPayments.xml");
        CQLQueryResults results = loadQueryResults("goldAllPayments.xml");
        invokeValidQueryValidResults(query, results);
    }
    
    
    private void testDistinctAttributeFromCash() {
        LOG.debug("testDistinctAttributeFromCash");
        CQLQuery query = loadQuery("distinctAttributeFromCash.xml");
        CQLQueryResults results = loadQueryResults("goldDistinctAttributeFromCash.xml");
        invokeValidQueryValidResults(query, results);
    }
    
    
    private void testAssociationNotNull() {
        LOG.debug("testAssociationNotNull");
        CQLQuery query = loadQuery("associationNotNull.xml");
        CQLQueryResults results = loadQueryResults("goldAssociationNotNull.xml");
        invokeValidQueryValidResults(query, results);
    }
    
    
    private void testAssociationWithAttributeEqual() {
        LOG.debug("testAssociationWithAttributeEqual");
        CQLQuery query = loadQuery("associationWithAttributeEqual.xml");
        CQLQueryResults results = loadQueryResults("goldAssociationWithAttributeEqual.xml");
        invokeValidQueryValidResults(query, results);
    }
    
    
    private void testGroupOfAttributesUsingAnd() {
        LOG.debug("testGroupOfAttributesUsingAnd");
        CQLQuery query = loadQuery("groupOfAttributesUsingAnd.xml");
        CQLQueryResults results = loadQueryResults("goldGroupOfAttributesUsingAnd.xml");
        invokeValidQueryValidResults(query, results);
    }
    
    
    private void testGroupOfAttributesUsingOr() {
        LOG.debug("testGroupOfAttributesUsingOr");
        CQLQuery query = loadQuery("groupOfAttributesUsingOr.xml");
        CQLQueryResults results = loadQueryResults("goldGroupOfAttributesUsingOr.xml");
        invokeValidQueryValidResults(query, results);
    }
    
    
    private CQLQuery loadQuery(String filename) {
        String fullFilename = TEST_QUERIES_DIR + filename;
        CQLQuery query = null;
        try {
            InputStream queryInputStream = InvokeSDK4DataServiceStep.class.getResourceAsStream(fullFilename);
            InputStreamReader reader = new InputStreamReader(queryInputStream);
            query = (CQLQuery) Utils.deserializeObject(reader, CQLQuery.class);
            reader.close();
            queryInputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error deserializing query (" + fullFilename + "): " + ex.getMessage());
        }
        return query;
    }
    
    
    private CQLQueryResults loadQueryResults(String filename)  {
        String fullFilename = TEST_RESULTS_DIR + filename;
        CQLQueryResults results = null;
        try {
            InputStream resultInputStream = InvokeSDK4DataServiceStep.class.getResourceAsStream(fullFilename);
            InputStreamReader reader = new InputStreamReader(resultInputStream);
            results = (CQLQueryResults) Utils.deserializeObject(reader, CQLQueryResults.class);
            reader.close();
            resultInputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error deserializing query results (" + fullFilename + "): " + ex.getMessage());
        }
        return results;
    }
    
    
    /**
     * Executes a query, which is expected to be valid and compares the
     * results to gold, which is expected to be valid
     * @param query
     *      The query to execute
     * @param goldResults
     *      The gold results set
     */
    private void invokeValidQueryValidResults(CQLQuery query, CQLQueryResults goldResults) {
        DataServiceClient client = getServiceClient();
        CQLQueryResults queryResults = null;
        try {
            queryResults = client.query(query);
            // If this fails, we need to still be able to exit the jvm
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Query failed to execute: " + ex.getMessage());
        }
        compareResults(goldResults, queryResults);
    }
    
    
    /**
     * Executes a query, which is expected to be invalid and fail
     * @param query
     *      The expected invalid query
     */
    private void invokeInvalidQuery(CQLQuery query) {
        DataServiceClient client = getServiceClient();
        try {
            client.query(query);
            fail("Query returned results, should have failed");
        } catch (Exception ex) {
            // expected
        }
    }
    
    
    private DataServiceClient getServiceClient() {
        DataServiceClient client = null;
        try {
            client = new DataServiceClient(getServiceUrl()); 
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error creating data service client: " + ex.getMessage());
        }
        return client;
    }
    
    
    private String getServiceUrl() {
        String url = null;
        try {
            URI baseUri = container.getContainerBaseURI();
            url = baseUri.toString() + "cagrid/" + testInfo.getName();
        } catch (MalformedURIException ex) {
            ex.printStackTrace();
            fail("Error generating service url: " + ex.getMessage());
        }
        LOG.debug("Data service url: " + url);
        return url;
    }
    
    
    private void compareResults(CQLQueryResults gold, CQLQueryResults test) {
        Set<Object> goldObjects = new HashSet<Object>();
        Set<Object> testObjects = new HashSet<Object>();
        
        boolean goldIsAttributes = false;
        CQLQueryResultsIterator goldIter = new CQLQueryResultsIterator(gold, getClientConfigStream());
        while (goldIter.hasNext()) {
            Object o = goldIter.next();
            if (o instanceof TargetAttribute[]) {
                goldIsAttributes = true;
            }
            goldObjects.add(o);
        }
        
        boolean testIsAttributes = false;
        CQLQueryResultsIterator testIter = new CQLQueryResultsIterator(test, getClientConfigStream());
        while (testIter.hasNext()) {
            Object o = testIter.next();
            if (o instanceof TargetAttribute[]) {
                testIsAttributes = true;
            }
            testObjects.add(o);
        }
        
        assertEquals("Result count differed from expected", goldObjects.size(), testObjects.size());
        assertEquals("Test results as attributes differed from expected", goldIsAttributes, testIsAttributes);
        
        if (goldIsAttributes) {
            Set<TargetAttribute[]> goldAttributes = recastSet(goldObjects);
            Set<TargetAttribute[]> testAttributes = recastSet(testObjects);
            compareTargetAttributes(goldAttributes, testAttributes);
        } else {
            assertTrue("Gold and Test contained different objects", goldObjects.containsAll(testObjects));
        }
    }
    
    
    private void compareTargetAttributes(Set<TargetAttribute[]> gold, Set<TargetAttribute[]> test) {
        // assumes sizes of both sets are equal
        Iterator<TargetAttribute[]> goldIter = gold.iterator();
        Iterator<TargetAttribute[]> testIter = test.iterator();
        while (goldIter.hasNext()) {
            TargetAttribute[] goldAttributes = goldIter.next();
            TargetAttribute[] testAttributes = testIter.next();
            // veriy the same number of attributes
            assertEquals("Number of attributes differed from expected", goldAttributes.length, testAttributes.length);
            for (TargetAttribute goldAttrib : goldAttributes) {
                // find the attribute of the same in the test attributes
                TargetAttribute matchingTestAttrib = null;
                for (TargetAttribute testAttrib : testAttributes) {
                    if (testAttrib.getName().equals(goldAttrib.getName())) {
                        matchingTestAttrib = testAttrib;
                        break;
                    }
                }
                assertNotNull("No attribute named " + goldAttrib.getName() + " found in test result attributes", matchingTestAttrib);
                assertEquals("Value of attribute " + goldAttrib.getName() + " did not match expected", goldAttrib.getValue(), matchingTestAttrib.getValue());
            }
        }
    }
    
    
    private <T> Set<T> recastSet(Set<?> set) {
        Set<T> returnme = new HashSet<T>();
        for (Object o : set) {
            returnme.add((T) o);
        }
        return returnme;
    }
    
    
    private InputStream getClientConfigStream() {
        InputStream is = null;
        try {
            is = InvokeSDK4DataServiceStep.class.getResourceAsStream(TEST_RESOURCES_DIR + "wsdd/client-config.wsdd");
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error obtaining client config input stream: " + ex.getMessage());
        }
        return is;
    }
}
