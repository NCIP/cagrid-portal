package gov.nih.nci.cagrid.portal.portlet.gss;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;

import java.io.StringReader;
import java.io.StringWriter; 
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.UUID;

import org.apache.axis.types.URI.MalformedURIException;

public class ImpromptuQueryRunner implements Runnable {

    private ImpromptuQuery query;

    public ImpromptuQueryRunner(ImpromptuQuery q) {
        this.query = q;
        System.out.println("vvvvvvvvvvvv IMPROMPTU QUERY RUNNER CREATED vvvvvvvvvvvvvvvvvvv");
        System.out.println(this.query);
        System.out.println("^^^^^^^^^^^^ IMPROMPTU QUERY RUNNER CREATED ^^^^^^^^^^^^^^^^^^^");
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
            
            System.out.println("vvvvvvvvvvvv IMPROMPTU QUERY vvvvvvvvvvvvvvvvvvv");
            System.out.println(out);
            Iterator i = ImpromptuQueryViewController.results.keySet().iterator();
            while (i.hasNext()) {
                String k = (String)i.next();
                System.out.println(k + " => " + ImpromptuQueryViewController.results.get(k));
            }
            ImpromptuQueryViewController.results.put(this.query.getUuid().toString(), out);
            i = ImpromptuQueryViewController.results.keySet().iterator();
            while (i.hasNext()) {
                String k = (String)i.next();
                System.out.println(k + " => " + ImpromptuQueryViewController.results.get(k));
            }
            System.out.println("^^^^^^^^^^^^ IMPROMPTU QUERY ^^^^^^^^^^^^^^^^^^^");

        } catch (MalformedQueryExceptionType e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (QueryProcessingExceptionType e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURIException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
