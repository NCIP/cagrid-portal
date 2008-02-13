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
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
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
 * @version $Id: InvokeSDK4DataServiceStep.java,v 1.15 2008-02-13 16:35:37 dervin Exp $ 
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
        testGroupOfAssociationsUsingAnd();
        testGroupOfAssociationsUsingOr();
        testNestedAssociations();
        testNestedAssociationsNoRoleNames();
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
    
    
    private void testGroupOfAssociationsUsingAnd() {
        LOG.debug("testGroupOfAssociationsUsingAnd");
        CQLQuery query = loadQuery("groupOfAssociationsUsingAnd.xml");
        CQLQueryResults results = loadQueryResults("goldGroupOfAssociationsUsingAnd.xml");
        invokeValidQueryValidResults(query, results);
    }
    
    
    private void testGroupOfAssociationsUsingOr() {
        LOG.debug("testGroupOfAssociationsUsingOr");
        CQLQuery query = loadQuery("groupOfAssociationsUsingOr.xml");
        CQLQueryResults results = loadQueryResults("goldGroupOfAssociationsUsingOr.xml");
        invokeValidQueryValidResults(query, results);
    }
    
    
    private void testNestedAssociations() {
        LOG.debug("testNestedAssociations");
        CQLQuery query = loadQuery("nestedAssociations.xml");
        CQLQueryResults results = loadQueryResults("goldNestedAssociations.xml");
        invokeValidQueryValidResults(query, results);
    }
    
    
    private void testNestedAssociationsNoRoleNames() {
        LOG.debug("testNestedAssociationsNoRoleNames");
        CQLQuery query = loadQuery("nestedAssociationsNoRoleNames.xml");
        // should have same results as with role names
        CQLQueryResults results = loadQueryResults("goldNestedAssociations.xml");
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
            // assertTrue("Gold and Test contained different objects", goldObjects.containsAll(testObjects));
            compareObjects(goldObjects, testObjects);
        }
    }
    
    
    private void compareObjects(Set<Object> gold, Set<Object> test) {
        if (!gold.containsAll(test)) {
            // fail, but why?
            Set<Object> tempGold = new HashSet<Object>();
            tempGold.addAll(gold);
            tempGold.removeAll(test);
            StringBuffer errors = new StringBuffer();
            errors.append("The following objects were expected but not found\n");
            dumpGetters(tempGold, errors);
            Set<Object> tempTest = new HashSet<Object>();
            tempTest.addAll(test);
            tempTest.removeAll(gold);
            errors.append("\n\nThe following objects were found, but not expected\n");
            dumpGetters(tempTest, errors);
            fail(errors.toString());
        }
    }
    
    
    private void dumpGetters(Collection<Object> objs, StringBuffer buff) {
        for (Object o : objs) {
            buff.append(o.getClass().getName()).append("\n");
            Method[] methods = o.getClass().getMethods();
            for (Method m : methods) {
                if (m.getName().startsWith("get") && m.getParameterTypes().length == 0) {
                    try {
                        Object value = m.invoke(o, new Object[0]);
                        buff.append(m.getName());
                        buff.append(" --> ");
                        buff.append(String.valueOf(value));
                        buff.append("\n");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
    
    
    private void compareTargetAttributes(Set<TargetAttribute[]> gold, Set<TargetAttribute[]> test) {
        // assumes sizes of both sets are equal
        
        // Must find each array of gold attributes (in any order) 
        // in the test attributes set
        Set<TargetAttribute[]> tempTestAttributes = new HashSet<TargetAttribute[]>();
        tempTestAttributes.addAll(test);
        Comparator<TargetAttribute> attributeSorter = new Comparator<TargetAttribute>() {
            public int compare(TargetAttribute o1, TargetAttribute o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
        Iterator<TargetAttribute[]> goldIter = gold.iterator();
        while (goldIter.hasNext()) {
            TargetAttribute[] goldAttributes = goldIter.next();
            Arrays.sort(goldAttributes, attributeSorter);
            // find a matching array of attributes in the test set
            Iterator<TargetAttribute[]> testIter = tempTestAttributes.iterator();
            while (testIter.hasNext()) {
                TargetAttribute[] testAttributes = testIter.next();
                Arrays.sort(testAttributes, attributeSorter);
                // veriy the same number of attributes.  This should be true for every array
                assertEquals("Number of attributes differed from expected", goldAttributes.length, testAttributes.length);
                // check that the current goldAttribute[] matches the test[]
                boolean matching = true;
                for (int i = 0; i < goldAttributes.length && matching; i++) {
                    assertEquals("Unexpected attribute name in test results", 
                        goldAttributes[i].getName(), testAttributes[i].getName());
                    String goldValue = goldAttributes[i].getValue();
                    String testValue = testAttributes[i].getValue();
                    matching = String.valueOf(goldValue).equals(String.valueOf(testValue));
                }
                if (matching) {
                    // found a matching TargetAttribute[] in test for one in gold.
                    // remove it from the test set so anything left in there is
                    // not a valid result when this process completes
                    testIter.remove();
                }
            }
        }
        if (tempTestAttributes.size() != 0) {
            StringBuffer errors = new StringBuffer();
            errors.append("The following attribute arrays were not expected in the test results:");
            for (TargetAttribute[] atts : tempTestAttributes) {
                errors.append("---------\n");
                for (TargetAttribute ta : atts) {
                    errors.append("Attribute: ").append(ta.getName()).append("\t\tValue: ")
                        .append(ta.getValue()).append("\n");
                }
            }
            fail(errors.toString());
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
