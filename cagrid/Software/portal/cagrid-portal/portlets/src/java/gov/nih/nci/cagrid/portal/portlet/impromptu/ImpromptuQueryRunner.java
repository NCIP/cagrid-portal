package gov.nih.nci.cagrid.portal.portlet.impromptu;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.fqp.client.FederatedQueryProcessorClient;
import gov.nih.nci.cagrid.fqp.results.client.FederatedQueryResultsClient;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLDecoder;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis.wsrf.lifetime.Destroy;

public class ImpromptuQueryRunner implements Runnable {

    static public String fqpUrl;

    private final Log logger = LogFactory.getLog(getClass());

    private ImpromptuQuery query;

    public ImpromptuQueryRunner(ImpromptuQuery q) {
        this.query = q;
    }

    public void run() {

        try {
            logger.info("=====> Start running impromptu query: " + this.query);

            /* TODO: this shouldn't be necessary */
            String urlDecodedQuery = this.query.getQuery().replace("& ", "&");
            urlDecodedQuery = urlDecodedQuery.replace("&lt;", "<");
            urlDecodedQuery = urlDecodedQuery.replace("&gt;", ">");
            urlDecodedQuery = URLDecoder.decode(urlDecodedQuery, "UTF-8");

            logger.debug("PortletUtils.isDCQL(urlDecodedQuery)=" + PortletUtils.isDCQL(urlDecodedQuery));
            logger.debug("PortletUtils.isCQL(urlDecodedQuery)=" + PortletUtils.isCQL(urlDecodedQuery));

            String out = "";
            if (PortletUtils.isDCQL(urlDecodedQuery)) {
                FederatedQueryProcessorClient client = new FederatedQueryProcessorClient(ImpromptuQueryRunner.fqpUrl);
                DCQLQuery dcqlQuery = (DCQLQuery) Utils.deserializeObject(new StringReader(urlDecodedQuery), DCQLQuery.class);
                FederatedQueryResultsClient resultsClilent = client.executeAsynchronously(dcqlQuery);
                while (!resultsClilent.isProcessingComplete()) {
                    Thread.sleep(5000);
                }
                DCQLQueryResultsCollection dcqlResultsCol = resultsClilent.getResults();

                StringWriter writer = new StringWriter();
                Utils.serializeObject(dcqlResultsCol, new QName("http://caGrid.caBIG/1.0/gov.nih.nci.cagrid.dcqlresult", "DCQLQueryResultsCollection"), writer);
                out = writer.getBuffer().toString();
                ImpromptuQueryStorage.instance.setResult(this.query, out);
                resultsClilent.destroy(new Destroy());
            }

            if (PortletUtils.isCQL(urlDecodedQuery)) { 
                DataServiceClient client = new DataServiceClient(this.query.getEndpointUrl());
                CQLQuery cqlQuery = (CQLQuery) Utils.deserializeObject(new StringReader(urlDecodedQuery), CQLQuery.class);
                CQLQueryResults result = client.query(cqlQuery);
                StringWriter writer = new StringWriter();
                Utils.serializeObject(result, DataServiceConstants.CQL_RESULT_SET_QNAME, writer);
                out = writer.getBuffer().toString();

                ImpromptuQueryStorage.instance.setResult(this.query, out);
            }
            
            logger.info("=====> Done running impromptu query: " + this.query);
            logger.info("=====> " + out);
            
        } catch (Exception e) {
            logger.error("Exception running impromptu query (" + this.query + ") with message: " + e.getMessage());
        }

    }

}
