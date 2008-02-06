package gov.nih.nci.cagrid.sdkquery4.test.system.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
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
 * @version $Id: InvokeSDK4DataServiceStep.java,v 1.6 2008-02-06 15:44:04 dervin Exp $ 
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
    }
    
    
    private void testUndergraduateStudentWithName() {
        CQLQuery query = loadQuery(TEST_QUERIES_DIR + "undergraduateStudentWithName.xml");
        CQLQueryResults results = loadQueryResults(TEST_RESULTS_DIR + "goldUndergraduateStudentWithName.xml");
        invokeValidQueryValidResults(query, results);
    }
    
    
    private void testAllPayments() {
        CQLQuery query = loadQuery(TEST_QUERIES_DIR + "allPayments.xml");
        CQLQueryResults results = loadQueryResults(TEST_RESULTS_DIR + "goldAllPayments.xml");
        invokeValidQueryValidResults(query, results);
    }
    
    
    private void testDistinctAttributeFromCash() {
        CQLQuery query = loadQuery(TEST_QUERIES_DIR + "distinctAttributeFromCash.xml");
        CQLQueryResults results = loadQueryResults(TEST_RESULTS_DIR + "goldDistinctAttributeFromCash.xml");
        invokeValidQueryValidResults(query, results);
    }
    
    
    private void testAssociationNotNull() {
        CQLQuery query = loadQuery(TEST_QUERIES_DIR + "associationNotNull.xml");
        CQLQueryResults results = loadQueryResults(TEST_RESULTS_DIR + "goldAssociationNotNull.xml");
        invokeValidQueryValidResults(query, results);
    }
    
    
    private CQLQuery loadQuery(String filename) {
        CQLQuery query = null;
        try {
            InputStream queryInputStream = InvokeSDK4DataServiceStep.class.getResourceAsStream(filename);
            InputStreamReader reader = new InputStreamReader(queryInputStream);
            query = (CQLQuery) Utils.deserializeObject(reader, CQLQuery.class);
            reader.close();
            queryInputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error deserializing query (" + filename + "): " + ex.getMessage());
        }
        return query;
    }
    
    
    private CQLQueryResults loadQueryResults(String filename)  {
        CQLQueryResults results = null;
        try {
            InputStream resultInputStream = InvokeSDK4DataServiceStep.class.getResourceAsStream(filename);
            InputStreamReader reader = new InputStreamReader(resultInputStream);
            results = (CQLQueryResults) Utils.deserializeObject(reader, CQLQueryResults.class);
            reader.close();
            resultInputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error deserializing query results (" + filename + "): " + ex.getMessage());
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
        Set<Object> goldXml = new HashSet<Object>();
        Set<Object> testXml = new HashSet<Object>();
        
        CQLQueryResultsIterator goldIter = new CQLQueryResultsIterator(gold, getClientConfigStream());
        while (goldIter.hasNext()) {
            goldXml.add(goldIter.next());
        }
        
        CQLQueryResultsIterator testIter = new CQLQueryResultsIterator(test, getClientConfigStream());
        while (testIter.hasNext()) {
            testXml.add(testIter.next());
        }
        
        assertEquals("Result count differed from expected", goldXml.size(), testXml.size());
        
        // TODO: compare objects
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
