package gov.nih.nci.cagrid.portal.portlet.query.dcql;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.dcqlresult.DCQLResult;
import gov.nih.nci.cagrid.fqp.client.FederatedQueryProcessorClient;
import gov.nih.nci.cagrid.fqp.results.client.FederatedQueryResultsClient;
import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQueryInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.gsi.GlobusCredential;
import org.oasis.wsrf.lifetime.Destroy;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.concurrent.Callable;

import javax.xml.namespace.QName;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DCQLQueryTask implements Callable {

    private static final Log logger = LogFactory.getLog(DCQLQueryTask.class);

    private DCQLQueryInstance instance;

    private DCQLQueryInstanceListener listener;

    private GlobusCredential cred;

    public DCQLQueryTask(DCQLQueryInstance instance,
                         DCQLQueryInstanceListener listener, GlobusCredential cred) {
        this.instance = instance;
        this.listener = listener;
        this.cred = cred;
    }


    public Object call() throws Exception {
        logger.debug("Running QueryInstance:" + instance.getId());
        StringBuffer out = new StringBuffer();
        try {
            listener.onRunning(instance);
            StringReader reader = new StringReader(instance.getQuery().getXml());
            DCQLQuery query = (DCQLQuery) Utils.deserializeObject(reader,
                    DCQLQuery.class);
            FederatedQueryProcessorClient client = null;

            if (cred != null) {
                client = new FederatedQueryProcessorClient(instance.getFqpService().getUrl(), cred);
            } else {
                client = new FederatedQueryProcessorClient(instance.getFqpService().getUrl());

            }
            logger.debug("Requesting FQP to execute DCQL");
            FederatedQueryResultsClient resultsClilent = client.executeAsynchronously(query);

            // hackish... need to subscribe to isComplete RP
            while (!resultsClilent.isProcessingComplete()) {
                Thread.sleep(5000);
                logger.debug(".");
            }

            DCQLQueryResultsCollection dcqlResultsCol = resultsClilent.getResults();
            
            StringWriter writer = new StringWriter();
    		Utils.serializeObject(dcqlResultsCol,
    				new QName("http://caGrid.caBIG/1.0/gov.nih.nci.cagrid.dcqlresult", "DCQLQueryResultsCollection"), writer);

            
//            DCQLResult[] dcqlResults = dcqlResultsCol.getDCQLResult();
//            if (dcqlResults != null) {
//                for (DCQLResult result : dcqlResults) {
//                    String targetServiceURL = result.getTargetServiceURL();
//                    System.out.println("Got results from:" + targetServiceURL);
//                    CQLQueryResults queryResultCollection = result.getCQLQueryResultCollection();
//                    CQLQueryResultsIterator iterator = new CQLQueryResultsIterator(queryResultCollection, true);
//                    while (iterator.hasNext()) {
//                        logger.debug("DCQL resultset. Appending");
//                        out.append(iterator.next());
//                    }
//
//                }
//            } else {
//                System.out.println("Got no results.");
//            }
            listener.onComplete(instance, writer.getBuffer().toString());
            resultsClilent.destroy(new Destroy());

        } catch (Exception ex) {
            logger.debug("Error running query: " + ex.getMessage(), ex);
            listener.onError(instance, ex);
            throw ex;
        }
        return out;
    }
}
