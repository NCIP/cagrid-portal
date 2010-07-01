package gov.nih.nci.cagrid.portal.portlet.gss;

import gov.nih.nci.cagrid.portal.dao.catalog.GridServiceEndPointCatalogEntryDao;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

 

/**
 * User: jbar
 * 
 * @author jbar jaime.barciela@semanticbits.com
 */
public class GridSummaryService implements ApplicationContextAware {

    private volatile List<SummaryQueryWithLocations> queries;
    
    public List<SummaryQueryWithLocations> getQueries() {
        return queries;
    }

    public void setQueries(List<SummaryQueryWithLocations> queries) {
        this.queries = queries;
    }

    private final Log logger = LogFactory.getLog(getClass());
    
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    
    public void setApplicationContext(ApplicationContext appCtx) throws BeansException {
        
        GridServiceEndPointCatalogEntryDao dao = (GridServiceEndPointCatalogEntryDao) appCtx.getBean("gridServiceEndPointCatalogEntryDao");
        for (SummaryQueryWithLocations q : this.queries) {
            q.addUrls(dao.getByUmlClassNameAndPartialUrl(q.getPackage(), q.getShortClassName(), "%"));
        }
        
        GridSummaryCalculator c = new GridSummaryCalculator(this.queries);
        Thread t = new Thread(c);
        t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            
            public void uncaughtException(Thread t, Throwable e) {
                // 
                // just keep going , try next time
                //
                logger.warn("exception in summary queries background task with message = " + e.getMessage());
            }
            
        });
        //executor.scheduleAtFixedRate(t, 0, 24 * 60 * 60, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(t, 0, 5 * 60, TimeUnit.SECONDS);
    }

}
