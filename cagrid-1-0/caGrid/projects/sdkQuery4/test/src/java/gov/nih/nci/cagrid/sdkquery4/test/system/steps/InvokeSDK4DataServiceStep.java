package gov.nih.nci.cagrid.sdkquery4.test.system.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;
import java.io.FileReader;
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
 * @version $Id: InvokeSDK4DataServiceStep.java,v 1.4 2008-02-05 16:53:53 dervin Exp $ 
 */
public class InvokeSDK4DataServiceStep extends Step {
    public static final String TEST_RESOURCES_DIR = ".." + File.separator + "sdkQuery4" +
    File.separator + "test" + File.separator + "resources" + File.separator;
    public static final String TEST_QUERIES_DIR = TEST_RESOURCES_DIR + "testQueries" + File.separator;
    public static final String TEST_RESULTS_DIR = TEST_RESOURCES_DIR + "testGoldResults" + File.separator;
    
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
    
    
    private CQLQuery loadQuery(String filename) {
        CQLQuery query = null;
        try {
            FileReader reader = new FileReader(filename);
            query = (CQLQuery) Utils.deserializeObject(reader, CQLQuery.class);
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error deserializing query (" + filename + "): " + ex.getMessage());
        }
        return query;
    }
    
    
    private CQLQueryResults loadQueryResults(String filename)  {
        CQLQueryResults results = null;
        try {
            FileReader reader = new FileReader(filename);
            results = (CQLQueryResults) Utils.deserializeObject(reader, CQLQueryResults.class);
            reader.close();
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
        Set<String> goldXml = new HashSet<String>();
        Set<String> testXml = new HashSet<String>();
        
        CQLQueryResultsIterator goldIter = new CQLQueryResultsIterator(gold, true);
        while (goldIter.hasNext()) {
            goldXml.add((String) goldIter.next());
        }
        
        CQLQueryResultsIterator testIter = new CQLQueryResultsIterator(test, true);
        while (testIter.hasNext()) {
            testXml.add((String) testIter.next());
        }
        
        assertEquals("Result count differed from expected", goldXml.size(), testXml.size());
    }
}
