package gov.nih.nci.cagrid.portal.portlet.gss;

/*
 <ns1:CQLQueryResults targetClassname="gov.nih.nci.caarray.domain.array.Array " xmlns:ns1="http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet">
 <ns1:CountResult count="67"/>
 </ns1:CQLQueryResults> 
 */
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.client.DataServiceClient;

import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GridSummaryCalculator implements Runnable {

    private final Log logger = LogFactory.getLog(getClass());

    private List<SummaryQueryWithLocations> queries;

    public GridSummaryCalculator(List<SummaryQueryWithLocations> _queries) {
        this.queries = _queries;
    }

    public void run() {
        synchronized (GridSummaryService.class) {
            logger.info("Summary queries background tasks started " + new java.util.Date());

            for (SummaryQueryWithLocations currQueryLocs : this.queries) {
                
                Iterator<String> i = currQueryLocs.getUrlsIterator();
                while (i.hasNext()) {
                    String currUrl = i.next();
                    try {
                        DataServiceClient client = new DataServiceClient(currUrl);

                        CQLQueryResults result = client.query(currQueryLocs.getCqlQuery());
                        
                        String out = SummaryQueryWithLocations.queryResultAsString(result);
                        
                        System.out.println("vvvvvvvvvvvvvvv OUT vvvvvvvvvvvvvvvv");
                        System.out.println(out);
                        System.out.println("^^^^^^^^^^^^^^^ OUT ^^^^^^^^^^^^^^^^");

                        currQueryLocs.setCounterFromFullAnswer(currUrl, out);

                    } catch (Exception e) {
                        System.out.println("vvvvvvvvvvvvvvv ERROR vvvvvvvvvvvvvvvv");
                        System.out.println("querying for " + currQueryLocs.getShortClassName());
                        System.out.println("failed for url " + currUrl);
                        System.out.println("with error " + e.getMessage());
                        System.out.println("^^^^^^^^^^^^^^^ ERROR ^^^^^^^^^^^^^^^^");
                    }
                }
            }

            logger.info("Summary queries background tasks finished" + new java.util.Date());
        }

    }
}
