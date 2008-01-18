package gov.nih.nci.cagrid.sdkquery4.processor.testing;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.sdkquery4.processor.SDK4QueryProcessor;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/** 
 *  QueryProcessorInvoker
 *  TODO:DOCUMENT ME
 * 
 * @author David Ervin
 * 
 * @created Jan 4, 2008 2:07:12 PM
 * @version $Id: QueryProcessorInvoker.java,v 1.1 2008-01-18 15:13:29 dervin Exp $ 
 */
public class QueryProcessorInvoker {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream("test/resources/testProcessorConfig.properties");
            Properties props = new Properties();
            props.load(fis);
            
            InputStream wsddStream = new FileInputStream("test/resources/test-client-config.wsdd");
            
            SDK4QueryProcessor proc = new SDK4QueryProcessor();
            proc.initialize(props, wsddStream);
            
            CQLQuery query = (CQLQuery) Utils.deserializeDocument(
                "test/resources/testQueries/studentWithName.xml", CQLQuery.class);
            
            CQLQueryResults results = proc.processQuery(query);
            
            Utils.serializeDocument(
                "test/resources/testGoldResults/studentWithNameResults.xml", results, DataServiceConstants.CQL_QUERY_QNAME);            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
