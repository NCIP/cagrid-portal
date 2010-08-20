package gov.nih.nci.cagrid.portal.portlet.gss;

import gov.nih.nci.cagrid.portal.dao.catalog.GridServiceEndPointCatalogEntryDao;

import java.util.Date;
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
    
    static public GridSummaryService instance;

    static public volatile Date lastUpdated;

    private final Log logger = LogFactory.getLog(getClass());

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private int minMillisecondsBetweenRuns;
    private int secondsBetweenAutoRuns;
    private int maxHistoryCount;

    private long lastTriggeredRunTime = 0;

    private volatile List<SummaryQueryWithLocations> queries;
    private MaxCapacityList<GSSRun> history;

    synchronized public void addResult(GSSRun res) {
        history.add(res);
    }

    private void l(Object o) {
        System.out.println(o);
    }
    
    /*
    static public String refreshStatsSecurityRole;
    public void setRefreshStatsSecurityRole(String s) {
        GridSummaryService.refreshStatsSecurityRole = s;
    }*/
    
    synchronized public boolean triggerCalculator() {
        boolean res = false;
        long l = new Date().getTime();
        l("=================");
        l("now =" + l);
        l("lastTriggeredRunTime = " + lastTriggeredRunTime);
        l("this.minMillisecondsBetweenRuns = " + this.minMillisecondsBetweenRuns);
        l("=================");
        if ((l - lastTriggeredRunTime) > this.minMillisecondsBetweenRuns) {
            getNewCalculatorThread().start();
            this.lastTriggeredRunTime = l;
            res = true;
        }
        return res;
    }

    synchronized private Thread getNewCalculatorThread() {
        GridSummaryCalculator c = new GridSummaryCalculator(this);
        Thread t = new Thread(c);
        t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            public void uncaughtException(Thread t, Throwable e) {
                // just keep going , try next time
                logger.warn("exception in summary queries background task with message = " + e.getMessage());
            }

        });
        return t;
    }

    public void setApplicationContext(ApplicationContext appCtx) throws BeansException {

        GridServiceEndPointCatalogEntryDao dao = (GridServiceEndPointCatalogEntryDao) appCtx.getBean("gridServiceEndPointCatalogEntryDao");
        for (SummaryQueryWithLocations q : this.queries) {
            q.addUrls(dao.getByUmlClassNameAndPartialUrl(q.getPackage(), q.getShortClassName(), "%"));
        }

        GridSummaryService.instance = this;
        
        executor.scheduleAtFixedRate(this.getNewCalculatorThread(), 0, this.secondsBetweenAutoRuns, TimeUnit.SECONDS);
    }

    public List<SummaryQueryWithLocations> getQueries() {
        return queries;
    }

    public void setQueries(List<SummaryQueryWithLocations> queries) {
        this.queries = queries;
    }

    public int getMinMillisecondsBetweenRuns() {
        return minMillisecondsBetweenRuns;
    }

    public void setMinMillisecondsBetweenRuns(int minMillisecondsBetweenRuns) {
        this.minMillisecondsBetweenRuns = minMillisecondsBetweenRuns;
    }

    public int getMaxHistoryCount() {
        return maxHistoryCount;
    }

    public void setMaxHistoryCount(int maxHistoryCount) {
        this.maxHistoryCount = maxHistoryCount;
        this.history = new MaxCapacityList<GSSRun>(this.maxHistoryCount);
    }

    public int getSecondsBetweenAutoRuns() {
        return secondsBetweenAutoRuns;
    }

    public void setSecondsBetweenAutoRuns(int secondsBetweenAutoRuns) {
        this.secondsBetweenAutoRuns = secondsBetweenAutoRuns;
    }

    public MaxCapacityList<GSSRun> getHistory() {
        return history;
    }

}
