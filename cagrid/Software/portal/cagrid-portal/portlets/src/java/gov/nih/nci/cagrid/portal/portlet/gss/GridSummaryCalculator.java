package gov.nih.nci.cagrid.portal.portlet.gss;

/*
 <ns1:CQLQueryResults targetClassname="gov.nih.nci.caarray.domain.array.Array " xmlns:ns1="http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet">
 <ns1:CountResult count="67"/>
 </ns1:CQLQueryResults> 
 */
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.client.DataServiceClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GridSummaryCalculator implements Runnable {

    private final Log logger = LogFactory.getLog(getClass());

    private GridSummaryService gss;

    public GridSummaryCalculator(GridSummaryService _gss) {
        this.gss = _gss;
    }

    private void l(Object o) {
        //logger.info(o);
        System.out.println(o);
    }
    
    public void run() {
        synchronized (GridSummaryService.class) {
            
            l("Summary queries background tasks started " + new java.util.Date());
            List<SummaryQueryResults> accum = new ArrayList<SummaryQueryResults>();
            for (SummaryQueryWithLocations currentQueryWithLocations : gss.getQueries()) {
                List<CalculatorHelper> detailsForCurrQuery = new ArrayList<CalculatorHelper>();
                Iterator<String> i = currentQueryWithLocations.getUrlsIterator();
                while (i.hasNext()) {
                    String currUrl = i.next();
                    try {
                        DataServiceClient client = new DataServiceClient(currUrl);
                        CQLQueryResults result = client.query(currentQueryWithLocations.getCqlQuery());
                        CalculatorHelper resUrl = new CalculatorHelper(currUrl, result);
                        detailsForCurrQuery.add(resUrl);
                        l(resUrl.getResultNumber());
                        l(resUrl.getStrResult());
                    } catch (Exception e) {
                        logger.error("querying for " + currentQueryWithLocations.getShortClassName());
                        logger.error("failed for url " + currUrl);
                        logger.error("with error " + e.getMessage());
                    }
                }
                Map<String, Long> m = new HashMap<String, Long>(); 
                for (CalculatorHelper r: detailsForCurrQuery) {
                    m.put(r.getUrl(), r.getResultNumber());
                }
                SummaryQueryResults resultsForCurrQuery = new SummaryQueryResults(currentQueryWithLocations, m);
                accum.add(resultsForCurrQuery);
            }
            GSSRun currRun = new GSSRun(accum);
            GridSummaryService.lastUpdated = new Date();
            this.gss.addResult(currRun);

            l("Summary queries background tasks finished" + new java.util.Date());
        }

    }
}
