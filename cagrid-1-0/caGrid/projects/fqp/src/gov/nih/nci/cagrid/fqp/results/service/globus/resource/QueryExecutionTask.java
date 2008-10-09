package gov.nih.nci.cagrid.fqp.results.service.globus.resource;

import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.fqp.processor.FederatedQueryEngine;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;

import org.cagrid.fqp.execution.QueryExecutionParameters;
import org.cagrid.fqp.results.metadata.ProcessingStatus;
import org.globus.gsi.GlobusCredential;

import commonj.work.Work;
import commonj.work.WorkManager;

/**
 * Encapsulates execution of a DCQL query in a thread so that
 * status of the resource may be maintained and other operations
 * do not block while waiting on the query to complete.
 * 
 * @author ervin
 */
class QueryExecutionTask implements Work {
    private DCQLQuery query = null;
    private GlobusCredential credential = null;
    private QueryExecutionParameters executionParameters = null;
    private WorkManager workManager = null;
    private AsynchronousFQPProcessingStatusListener statusListener = null;
    
    public QueryExecutionTask(
        DCQLQuery query, GlobusCredential credential, 
        QueryExecutionParameters executionParameters, WorkManager workManager,
        AsynchronousFQPProcessingStatusListener statusListener) {
        this.query = query;
        this.credential = credential;
        this.executionParameters = executionParameters;
        this.workManager = workManager;
        this.statusListener = statusListener;
    }
    
    
    public void run() {
        statusListener.processingStatusChanged(ProcessingStatus.Waiting_To_Begin, "Initializing Federated Query Engine");
        FederatedQueryEngine engine =
            new FederatedQueryEngine(credential, executionParameters, workManager);
        if (statusListener != null) {
            engine.addStatusListener(statusListener);
        }
        statusListener.processingStatusChanged(ProcessingStatus.Processing, "Processing Query");
        DCQLQueryResultsCollection results = null;
        try {
            results = engine.execute(query);
            statusListener.processingStatusChanged(ProcessingStatus.Complete, "Query processing complete");
        } catch (FederatedQueryProcessingException ex) {
            ex.printStackTrace();
            statusListener.processingStatusChanged(ProcessingStatus.Complete_With_Error, "Error processing query: " + ex.getMessage());
            statusListener.queryProcessingException(ex);
        }
        statusListener.queryResultsGenerated(results);
    }
    
    
    public void release() {
        // TODO: implement me (is there anything to do?)
    }
    
    
    public boolean isDaemon() {
        return false;
    }
}