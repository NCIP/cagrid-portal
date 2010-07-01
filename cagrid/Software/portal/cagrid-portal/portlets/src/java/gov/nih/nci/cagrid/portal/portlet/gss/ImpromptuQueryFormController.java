package gov.nih.nci.cagrid.portal.portlet.gss;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.client.DataServiceClient;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLDecoder;

import org.springframework.web.servlet.mvc.SimpleFormController;

public class ImpromptuQueryFormController extends SimpleFormController {

    @Override
    protected void doSubmitAction(Object command) throws Exception {
        ImpromptuQuery q = (ImpromptuQuery) command;
        
        System.out.println("==============  impromptu query ===============");
        System.out.println(q);
        
        String urlDecodedQuery = URLDecoder.decode(q.getQuery().replace("& ", "&"), "UTF-8");
        System.out.println("==============  decoded query ===============");
        System.out.println(urlDecodedQuery);

        DataServiceClient client = new DataServiceClient(q.getEndpointUrl());
        CQLQuery cqlQuery = (CQLQuery) Utils.deserializeObject(new StringReader(urlDecodedQuery), CQLQuery.class);
        CQLQueryResults result = client.query(cqlQuery);
        StringWriter writer = new StringWriter();
        Utils.serializeObject(result, DataServiceConstants.CQL_RESULT_SET_QNAME, writer);
        String out = writer.getBuffer().toString();
        q.setResult(out);
    }

}
