/*
 * Created on Sep 21, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.util.AlwaysValidDCQLResultsVerifier;
import gov.nci.nih.cagrid.tests.core.util.DCQLResultsVerifier;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.fqp.client.FederatedQueryProcessorClient;
import gov.nih.nci.cagrid.fqp.results.client.FederatedQueryResultsClient;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.rmi.RemoteException;
import java.util.Calendar;

import org.oasis.wsrf.lifetime.SetTerminationTime;
import org.oasis.wsrf.lifetime.SetTerminationTimeResponse;


public class FQPAsynchronousQueryScheduledTerminationStep extends Step {
    private String endpoint;
    private DCQLQuery dcql;
    private long duration;
    private DCQLResultsVerifier verifier;


    public FQPAsynchronousQueryScheduledTerminationStep(String endpoint, DCQLQuery dcql) {
        this(endpoint, dcql, 10000);
    }


    public FQPAsynchronousQueryScheduledTerminationStep(String endpoint, DCQLQuery dcql, long duration) {
        this(endpoint, dcql, new AlwaysValidDCQLResultsVerifier(), duration);
    }


    public FQPAsynchronousQueryScheduledTerminationStep(String endpoint, DCQLQuery dcql, DCQLResultsVerifier verifier,
        long duration) {
        this.endpoint = endpoint;
        this.dcql = dcql;
        this.verifier = verifier;
        this.duration = duration;
    }


    @Override
    public void runStep() throws Exception {
        FederatedQueryProcessorClient client = new FederatedQueryProcessorClient(this.endpoint);

        // execute query
        FederatedQueryResultsClient resultsClilent = client.executeAsynchronously(this.dcql);

        // wait for it to complete
        while (!resultsClilent.isProcessingComplete()) {
            Thread.sleep(500);
            System.out.print(".");
        }

        // check the results
        DCQLQueryResultsCollection results = resultsClilent.getResults();
        if (!this.verifier.areResultsValid(results)) {
            fail("Results were not considered valid by verifier [" + this.verifier + "]");
        }

        // schedule destruction of the resource
        int terminateAfterSecs = 5;
        SetTerminationTime termTime = new SetTerminationTime();
        Calendar terminateAt = Calendar.getInstance();
        terminateAt.add(Calendar.SECOND, terminateAfterSecs);
        termTime.setRequestedTerminationTime(terminateAt);

        SetTerminationTimeResponse response = resultsClilent.setTerminationTime(termTime);

        System.out.println("Current time " + response.getCurrentTime().getTime());
        System.out.println("Requested termination time " + terminateAt.getTime());
        System.out.println("Scheduled termination time " + response.getNewTerminationTime().getTime());
        System.out.println("Should terminate in:"
            + (response.getNewTerminationTime().getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / 1000
            + " seconds.");

        // wait for it to be expired (twice the duration + plus the time delay
        // should always work)
        Thread.sleep((this.duration * 2) + (1000 * terminateAfterSecs));

        // make sure its gone
        try {
            resultsClilent.isProcessingComplete();
            fail("A resource which should have been destoryed is still around!");
        } catch (RemoteException e) {
            // expected
        }

    }
}
