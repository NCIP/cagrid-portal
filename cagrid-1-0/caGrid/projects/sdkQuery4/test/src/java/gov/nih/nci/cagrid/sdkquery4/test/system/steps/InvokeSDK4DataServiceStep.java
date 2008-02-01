package gov.nih.nci.cagrid.sdkquery4.test.system.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.FileReader;

import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;

/** 
 *  InvokeSDK4DataServiceStep
 *  Step invokes the SDK4 Data service
 *  and verifies results
 * 
 * @author David Ervin
 * 
 * @created Feb 1, 2008 9:02:20 AM
 * @version $Id: InvokeSDK4DataServiceStep.java,v 1.1 2008-02-01 16:19:34 dervin Exp $ 
 */
public class InvokeSDK4DataServiceStep extends Step {
    
    private ServiceContainer container;
    private DataTestCaseInfo testInfo;

    public InvokeSDK4DataServiceStep(ServiceContainer container, DataTestCaseInfo testInfo) {
        this.container = container;
        this.testInfo = testInfo;
    }


    public void runStep() throws Throwable {
        // TODO Auto-generated method stub

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
    
    
    private CQLQueryResults loadQueryResults(String filename) {
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
        
    }
    
    
    /**
     * Executes a query, which is expected to be invalid and fail
     * @param query
     *      The expected invalid query
     */
    private void invokeInvalidQuery(CQLQuery query) {
        
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
        return url;
    }
}
