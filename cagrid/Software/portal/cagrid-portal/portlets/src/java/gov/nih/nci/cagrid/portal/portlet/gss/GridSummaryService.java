package gov.nih.nci.cagrid.portal.portlet.gss;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * User: jbar
 * 
 * @author jbar jaime.barciela@semanticbits.com
 */
public class GridSummaryService implements ApplicationContextAware {

    static private Thread calculatorThread = null;
    static private Map<String, Long> summaryResults = null;

    static public Map<String, Long> getSummaryResults() throws Exception {
        return (calculatorThread.isAlive()) ? null : summaryResults;
    }

    synchronized public void setApplicationContext(ApplicationContext appCtx) throws BeansException {
        if ((GridSummaryService.summaryResults == null) && (GridSummaryService.calculatorThread == null)) {
            GridSummaryService.summaryResults = new HashMap<String, Long>();
            calculatorThread = new Thread(new GridSummaryCalculator(appCtx, GridSummaryService.summaryResults));
            calculatorThread.start();
        }
    }

}
