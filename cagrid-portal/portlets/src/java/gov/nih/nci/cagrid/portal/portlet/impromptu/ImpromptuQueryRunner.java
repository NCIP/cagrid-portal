/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis.wsrf.lifetime.Destroy;

import javax.xml.namespace.QName;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLDecoder;

public class ImpromptuQueryRunner implements Runnable {

    static public String fqpUrl;

    private final Log logger = LogFactory.getLog(getClass());

    private ImpromptuQuery query;

    public ImpromptuQueryRunner(ImpromptuQuery q) {
        this.query = q;
    }

    public void run() {

        try {

            String urlDecodedQuery = this.query.getQuery();
            try {
                /* TODO: this shouldn't be necessary */
                urlDecodedQuery = urlDecodedQuery.replace("& ", "&");
                urlDecodedQuery = urlDecodedQuery.replace("&lt;", "<");
                urlDecodedQuery = urlDecodedQuery.replace("&gt;", ">");
                urlDecodedQuery = URLDecoder.decode(urlDecodedQuery, "UTF-8");
            } catch (Exception e) {
                logger.info("Error decoding query. Will skip");
            }


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

        } catch (Exception e) {
            logger.error("Exception running impromptu query (" + this.query + ") with message: " + e.getMessage());
        }

    }

}
