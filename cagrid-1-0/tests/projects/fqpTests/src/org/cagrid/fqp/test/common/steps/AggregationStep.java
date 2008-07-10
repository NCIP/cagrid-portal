package org.cagrid.fqp.test.common.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;
import gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;

import org.cagrid.fqp.test.common.FederatedQueryProcessorHelper;

/** 
 *  AggregationStep
 *  Performs a simple FQP aggregation and compare the result to Gold
 * 
 * @author David Ervin
 * 
 * @created Jul 10, 2008 12:17:40 PM
 * @version $Id: AggregationStep.java,v 1.1 2008-07-10 20:18:28 dervin Exp $ 
 */
public class AggregationStep extends Step {
    
    private String queryFilename;
    private String goldFilename;
    private FederatedQueryProcessorHelper queryProcessor;
    private String[] testServiceUrls;
    
    public AggregationStep(String queryFilename, String goldFilename, 
        FederatedQueryProcessorHelper queryProcessorHelper, String[] testServiceUrls) {
        this.queryFilename = queryFilename;
        this.goldFilename = goldFilename;
        this.queryProcessor = queryProcessorHelper;
        this.testServiceUrls = testServiceUrls;
    }
    

    public void runStep() throws Throwable {
        DCQLQuery query = deserializeQuery();
        query.setTargetServiceURL(testServiceUrls);
        CQLQueryResults testResults = performAggregation(query);
        CQLQueryResults goldResults = loadGoldResults();
        // TODO: compare results, assert pass / fail
    }
    
    
    private DCQLQuery deserializeQuery() {
        DCQLQuery query = null;
        FileReader reader = null;
        try {
            reader = new FileReader(queryFilename);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            fail("Unable to read query file " + queryFilename);
        }
        try {
            query = (DCQLQuery) Utils.deserializeObject(reader, DCQLQuery.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Unable to deserialize query file " + queryFilename);
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                // nothing else to do
            }
        }
        return query;
    }
    
    
    private CQLQueryResults loadGoldResults() {
        CQLQueryResults goldResults = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(goldFilename);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            fail("Unable to read gold results file " + goldFilename);
        }
        try {
            InputStream wsddStream = getClass().getResourceAsStream("resources/wsdd/client-config.wsdd");
            assertNotNull("Could not locate client-config.wsdd", wsddStream);
            goldResults = (CQLQueryResults) Utils.deserializeObject(
                new InputStreamReader(fis), CQLQueryResults.class, wsddStream);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to deserialize gold results " + goldFilename);
        } finally {
            try {
                fis.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                // we tried
            }
        }
        
        return goldResults;
    }
    
    
    private CQLQueryResults performAggregation(DCQLQuery query) throws RemoteException, 
        FederatedQueryProcessingException, FederatedQueryProcessingFault {
        return queryProcessor.executeAndAggregateResults(query);
    }
}
