/*
 * Created on Sep 21, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.util.AlwaysValidDCQLResultsVerifier;
import gov.nci.nih.cagrid.tests.core.util.DCQLResultsVerifier;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.fqp.client.FederatedQueryProcessorClient;

import com.atomicobject.haste.framework.Step;


public class FQPQueryStep extends Step {
    private String endpoint;
    private DCQLQuery dcql;
    private DCQLResultsVerifier verifier;


    public FQPQueryStep(String endpoint, DCQLQuery dcql) {
        this(endpoint, dcql, new AlwaysValidDCQLResultsVerifier());
    }


    public FQPQueryStep(String endpoint, DCQLQuery dcql, DCQLResultsVerifier verifier) {
        this.endpoint = endpoint;
        this.dcql = dcql;
        this.verifier = verifier;
    }


    @Override
    public void runStep() throws Exception {
        FederatedQueryProcessorClient client = new FederatedQueryProcessorClient(this.endpoint);

        DCQLQueryResultsCollection results = client.execute(this.dcql);
        if (!this.verifier.areResultsValid(results)) {
            fail("Results were not considered valid by verifier [" + this.verifier + "]");
        }

    }

}
