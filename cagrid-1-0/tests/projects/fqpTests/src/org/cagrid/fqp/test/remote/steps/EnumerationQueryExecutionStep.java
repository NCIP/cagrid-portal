package org.cagrid.fqp.test.remote.steps;

import gov.nih.nci.cagrid.cqlresultset.CQLObjectResult;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.dcqlresult.DCQLResult;
import gov.nih.nci.cagrid.enumeration.stubs.response.EnumerationResponseContainer;
import gov.nih.nci.cagrid.fqp.client.FederatedQueryProcessorClient;
import gov.nih.nci.cagrid.fqp.common.FederatedQueryProcessorConstants;
import gov.nih.nci.cagrid.fqp.results.client.FederatedQueryResultsClient;
import gov.nih.nci.cagrid.wsenum.utils.EnumerationResponseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.soap.SOAPElement;

import org.apache.axis.message.MessageElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.fqp.test.common.QueryResultsVerifier;
import org.cagrid.fqp.test.common.UrlReplacer;
import org.cagrid.fqp.test.common.steps.BaseQueryExecutionStep;
import org.globus.ws.enumeration.ClientEnumIterator;
import org.globus.wsrf.NotificationConsumerManager;
import org.globus.wsrf.NotifyCallback;
import org.globus.wsrf.utils.SubscriptionPersistenceUtils;
import org.oasis.wsn.Subscribe;
import org.oasis.wsn.SubscribeResponse;
import org.oasis.wsn.SubscriptionManager;

/**
 * EnumerationQueryExecutionStep
 * Executes a federated query using enumeration for results
 *  
 * @author ervin
 */
public class EnumerationQueryExecutionStep extends BaseQueryExecutionStep {
    
    private static Log LOG = LogFactory.getLog(EnumerationQueryExecutionStep.class);
    
    public static final String[] QUERY_URL_PLACEHOLDERS = {
        "DATA_SERVICE_1", "DATA_SERVICE_2"
    };
    
    public static final int MAX_NO_SUCH_ELEMENT = 5;
    
    private FederatedQueryProcessorClient fqpClient;
    private String[] dataServiceUrls;
    
    public EnumerationQueryExecutionStep(String queryFilename, String goldFilename,
        FederatedQueryProcessorClient fqpClient, String[] serviceUrls) {
        super(queryFilename, goldFilename);
        this.fqpClient = fqpClient;
        this.dataServiceUrls = serviceUrls;
    }
    

    public void runStep() throws Throwable {
        DCQLQuery query = getCompletedQuery();
        
        LOG.debug("Executing query with enumeration");
        FederatedQueryResultsClient resultsClient = fqpClient.query(query, null, null);
        // wait for notification that the data is ready
        SubscribeResponse subscribeResponse = resultsClient.subscribe(FederatedQueryProcessorConstants.RESULTS_METADATA_QNAME);
        NotificationConsumerManager notificationConsumer = NotificationConsumerManager.getInstance();
        
        EnumerationResponseContainer enumerationResponse = resultsClient.enumerate();
        
        LOG.debug("Creating client side enumeration iterator");
        ClientEnumIterator iterator = EnumerationResponseHelper.createClientIterator(enumerationResponse);
        
        List<SOAPElement> resultElements = new ArrayList<SOAPElement>();
        int noSuchElementCount = 0;
        while (iterator.hasNext()) {
            try {
                SOAPElement element = (SOAPElement) iterator.next();
                resultElements.add(element);
            } catch (NoSuchElementException ex) {
                noSuchElementCount++;
                if (noSuchElementCount >= MAX_NO_SUCH_ELEMENT) {
                    ex.printStackTrace();
                    fail("Too many (" + MAX_NO_SUCH_ELEMENT + ") NoSuchElementExceptions thrown by client iterator!");
                }
            }
        }
        
        // validate
        verifyResults(resultElements);
        
        // release the enumeration resource
        iterator.release();
    }
    
    
    private DCQLQuery getCompletedQuery() {
        assertEquals("Unexpected number of service urls", 
            QUERY_URL_PLACEHOLDERS.length, dataServiceUrls.length);
        LOG.debug("Loading original DCQL query from " + getQueryFilename());
        DCQLQuery original = deserializeQuery();
        Map<String, String> urlReplacements = new HashMap<String, String>();
        for (int i = 0; i < QUERY_URL_PLACEHOLDERS.length; i++) {
            urlReplacements.put(QUERY_URL_PLACEHOLDERS[i], dataServiceUrls[i]);
        }
        LOG.debug("Filling placeholder URLs with real ones");
        DCQLQuery replaced = null;
        try {
            replaced = UrlReplacer.replaceUrls(original, urlReplacements);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Unable to replace URL placeholders in DCQL query: " + ex.getMessage());
        }
        return replaced;
    }
    
    
    private void verifyResults(List<SOAPElement> results) {
        LOG.debug("Verifying test results");
        LOG.debug("Loading gold results from " + getGoldFilenname());
        DCQLQueryResultsCollection goldResults = loadGoldDcqlResults();
        
        // convert the list of elements into DCQL results
        DCQLQueryResultsCollection testResults = new DCQLQueryResultsCollection();
        DCQLResult testResult = new DCQLResult();
        testResult.setTargetServiceURL("http://fake.service");
        CQLQueryResults cqlResults = new CQLQueryResults();
        // what's the target classname supposed to be?
        String targetName = deserializeQuery().getTargetObject().getName();
        LOG.debug("Setting test results target classname to " + targetName);
        cqlResults.setTargetClassname(targetName);
        CQLObjectResult[] objectResults = new CQLObjectResult[results.size()];
        for (int i = 0; i < results.size(); i++) {
            CQLObjectResult obj = new CQLObjectResult();
            obj.set_any(new MessageElement[] {new MessageElement(results.get(i))});
            objectResults[i] = obj;
        }
        cqlResults.setObjectResult(objectResults);
        testResult.setCQLQueryResultCollection(cqlResults);
        testResults.setDCQLResult(new DCQLResult[] {testResult});
        
        QueryResultsVerifier.verifyDcqlResults(testResults, goldResults);
    }
}
