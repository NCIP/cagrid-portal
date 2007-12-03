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

import org.oasis.wsrf.lifetime.Destroy;


public class FQPAsynchronousQueryImmediateTerminationStep extends Step {
    private String endpoint;
    private DCQLQuery dcql;
    private DCQLResultsVerifier verifier;


    public FQPAsynchronousQueryImmediateTerminationStep(String endpoint, DCQLQuery dcql) {
        this(endpoint, dcql, new AlwaysValidDCQLResultsVerifier());
    }


    public FQPAsynchronousQueryImmediateTerminationStep(String endpoint, DCQLQuery dcql, DCQLResultsVerifier verifier) {
        this.endpoint = endpoint;
        this.dcql = dcql;
        this.verifier = verifier;
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

        // destroy the resource
        resultsClilent.destroy(new Destroy());

        // make sure its gone
        try {
            resultsClilent.isProcessingComplete();
            fail("A resource which should have been destoryed is still around!");
        } catch (RemoteException e) {
            // // this is stupid... how are you supposed to do this (can't just
            // // catch NoSuchResourceException ?
            // if
            // (e.getFaultString().equals(NoSuchResourceException.class.getName()))
            // {
            // // expected
            // } else {
            // FaultUtil.printFault(e);
            // fail("Unexpected fault!" + e.dumpToString());
            // }
        }

    }
}
