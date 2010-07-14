package gov.nih.nci.cagrid.portal.portlet.impromptu;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLDecoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImpromptuQueryRunner implements Runnable {

    private final Log logger = LogFactory.getLog(getClass());
    
    private ImpromptuQuery query;

    public ImpromptuQueryRunner(ImpromptuQuery q) {
        this.query = q;
    }

    public void run() {

        try {
            /* TODO: this shouldn't be necessary */
            String urlDecodedQuery = this.query.getQuery().replace("& ", "&");
            urlDecodedQuery = urlDecodedQuery.replace("&lt;", "<");
            urlDecodedQuery = urlDecodedQuery.replace("&gt;", ">");
            urlDecodedQuery = URLDecoder.decode(urlDecodedQuery, "UTF-8");

            DataServiceClient client = new DataServiceClient(this.query.getEndpointUrl());
            CQLQuery cqlQuery = (CQLQuery) Utils.deserializeObject(new StringReader(urlDecodedQuery), CQLQuery.class);
            CQLQueryResults result = client.query(cqlQuery);
            StringWriter writer = new StringWriter();
            Utils.serializeObject(result, DataServiceConstants.CQL_RESULT_SET_QNAME, writer);
            String out = writer.getBuffer().toString();

            logger.info("Running impromptu query: " + this.query);

            ImpromptuQueryViewController.results.put(this.query.getUuid().toString(), out);

        } catch (Exception e) {
            logger.error("Exception running impromptu query (" + this.query + ") with message: " + e.getMessage());
        }

    }

}
