package gov.nih.nci.cagrid.portal.portlet.gss;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class ImpromptuQueryFormController extends SimpleFormController {

    private final Log logger = LogFactory.getLog(getClass());
    
    @Override
    protected void doSubmitAction(Object command) throws Exception {
        ImpromptuQuery q = (ImpromptuQuery) command;

        /*
         * String nq = PortletUtils.normalizeCQL(q.getQuery()); System.out.println("==============  normalized query ===============");
         * System.out.println(nq);
         */

        /* TODO: this shouldn't be necessary */
        String urlDecodedQuery = q.getQuery().replace("& ", "&");
        urlDecodedQuery = urlDecodedQuery.replace("&lt;", "<");
        urlDecodedQuery = urlDecodedQuery.replace("&gt;", ">");
        urlDecodedQuery = URLDecoder.decode(urlDecodedQuery, "UTF-8");

        DataServiceClient client = new DataServiceClient(q.getEndpointUrl());
        CQLQuery cqlQuery = (CQLQuery) Utils.deserializeObject(new StringReader(urlDecodedQuery), CQLQuery.class);
        CQLQueryResults result = client.query(cqlQuery);
        StringWriter writer = new StringWriter();
        Utils.serializeObject(result, DataServiceConstants.CQL_RESULT_SET_QNAME, writer);
        String out = writer.getBuffer().toString();
        q.setResult(out);
        q.setUuid(UUID.randomUUID());
        
        ImpromptuQueryViewController.results.put(q.getUuid(), q.getResult());
        
        logger.info("IMPROMPTU QUERY \n" + out);
        System.out.println("IMPROMPTU QUERY \n" + out);
    }

}
