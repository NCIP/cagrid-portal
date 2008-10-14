import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.fqp.client.FederatedQueryProcessorClient;
import gov.nih.nci.cagrid.fqp.results.client.FederatedQueryResultsClient;

import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;


public class QueryWithTransfer {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            FederatedQueryProcessorClient fqpClient = new FederatedQueryProcessorClient("http://localhost:8080/wsrf/services/cagrid/FederatedQueryProcessor");
            DCQLQuery query = (DCQLQuery) Utils.deserializeDocument("exampleDistributedJoin1.xml", DCQLQuery.class);
            
            final FederatedQueryResultsClient resultsClient = fqpClient.query(query, null, null);
            
            while (!resultsClient.isProcessingComplete()) {
                Thread.sleep(200);
            }
            
            TransferServiceContextReference transferReference = resultsClient.transfer();
            
            System.out.println("Got transfer reference: " + transferReference.getEndpointReference().toString());
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
