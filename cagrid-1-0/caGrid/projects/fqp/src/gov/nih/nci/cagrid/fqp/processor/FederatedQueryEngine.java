package gov.nih.nci.cagrid.fqp.processor;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.dcqlresult.DCQLResult;
import gov.nih.nci.cagrid.fqp.common.SerializationUtils;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;
import gov.nih.nci.cagrid.fqp.processor.exceptions.RemoteDataServiceException;

import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.gsi.GlobusCredential;


/**
 * @author Srini Akkala
 * @author Scott Oster
 */
public class FederatedQueryEngine {
    protected static Log LOG = LogFactory.getLog(FederatedQueryEngine.class.getName());
    protected GlobusCredential cred;


    public FederatedQueryEngine() {
    }


    public FederatedQueryEngine(GlobusCredential cred) {
        this.cred = cred;
    }


    /**
     * Call Federated Query Processor, and send the generated CQLQuery to each
     * targeted service, placing each results into a single DCQLQueryResults
     * object.
     * 
     * @param dcqlQuery
     * @return The results of executing the DCQL query
     * @throws FederatedQueryProcessingException
     */
    public DCQLQueryResultsCollection execute(DCQLQuery dcqlQuery) throws FederatedQueryProcessingException {
        FederatedQueryProcessor processor = new FederatedQueryProcessor(this.cred);
        debugDCQLQuery("Beginning processing of DCQL", dcqlQuery);

        CQLQuery cqlQuery = processor.processDCQLQuery(dcqlQuery.getTargetObject());

        String[] targetServiceURLs = dcqlQuery.getTargetServiceURL();
        DCQLQueryResultsCollection result = new DCQLQueryResultsCollection();
        DCQLResult results[] = new DCQLResult[targetServiceURLs.length];
        for (int i = 0; i < targetServiceURLs.length; i++) {
            DCQLResult r = new DCQLResult();
            r.setTargetServiceURL(targetServiceURLs[i]);
            // aggregate results
            CQLQueryResults currResults = DataServiceQueryExecutor.queryDataService(cqlQuery, targetServiceURLs[i],
                this.cred);
            r.setCQLQueryResultCollection(currResults);
            if (currResults.getTargetClassname() == null
                || !currResults.getTargetClassname().equals(dcqlQuery.getTargetObject().getName())) {
                throw new RemoteDataServiceException("Data service (" + targetServiceURLs[i]
                    + ") returned results of type (" + currResults.getTargetClassname() + ") when type ("
                    + dcqlQuery.getTargetObject().getName() + ") was requested!");
            }
            results[i] = r;
        }
        result.setDCQLResult(results);
        return result;
    }


    /**
     * Call Federated Query Processor, and send the generated CQLQuery to each
     * targeted service, aggregating the results into a single CQLQueryResults
     * object.
     * 
     * @param dcqlQuery
     * @return Aggregated results of the DCQL query
     * @throws FederatedQueryProcessingException
     */
    public CQLQueryResults executeAndAggregateResults(DCQLQuery dcqlQuery) throws FederatedQueryProcessingException {
        FederatedQueryProcessor processor = new FederatedQueryProcessor(this.cred);
        debugDCQLQuery("Beginning processing of DCQL", dcqlQuery);

        CQLQuery cqlQuery = processor.processDCQLQuery(dcqlQuery.getTargetObject());

        CQLQueryResults aggregateResults = CQLAggregator.aggregateQueryResults(
            cqlQuery, dcqlQuery.getTargetServiceURL(), cred);
        
        return aggregateResults;
    }


    private void debugDCQLQuery(String logMessage, DCQLQuery dcqlQuery) {
        if (LOG.isDebugEnabled()) {
            try {
                StringWriter s = new StringWriter();
                SerializationUtils.serializeDCQLQuery(dcqlQuery, s);
                LOG.debug(logMessage + ":\n" + s.toString());
                s.close();
            } catch (Exception e) {
                LOG.error("Problem in debug printout of DCQL query:" + e.getMessage(), e);
            }
        }
    }
}
