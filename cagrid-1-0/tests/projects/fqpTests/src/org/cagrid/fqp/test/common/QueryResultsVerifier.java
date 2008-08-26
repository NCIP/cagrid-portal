package org.cagrid.fqp.test.common;

import java.util.LinkedList;
import java.util.List;

import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import junit.framework.Assert;

public class QueryResultsVerifier extends Assert {

    public static void verifyDcqlResults(DCQLQueryResultsCollection test, DCQLQueryResultsCollection gold) {
        
    }
    
    
    public static void verifyCqlResults(CQLQueryResults test, CQLQueryResults gold) {
        // iterators to extract objects from the results
        CQLQueryResultsIterator testIter = new CQLQueryResultsIterator(
            test, QueryResultsVerifier.class.getResourceAsStream(FQPTestingConstants.CLIENT_WSDD));
        CQLQueryResultsIterator goldIter = new CQLQueryResultsIterator(
            gold, QueryResultsVerifier.class.getResourceAsStream(FQPTestingConstants.CLIENT_WSDD));
        
        // turn results into lists
        List testItems = new LinkedList();
        List goldItems = new LinkedList();
        while (testIter.hasNext()) {
            testItems.add(testIter.next());
        }
        while (goldIter.hasNext()) {
            goldItems.add(goldIter.next());
        }
        
        // compare
        assertEquals("Incorrect number of results", goldItems.size(), testItems.size());
        
        for (Object testObject : testItems) {
            assertTrue("Unexpected Test data object not found in gold data", goldItems.contains(testObject));
        }
        
        for (Object goldObject : testItems) {
            assertTrue("Expected Gold data object not found in test data", testItems.contains(goldObject));
        }
    }
}
