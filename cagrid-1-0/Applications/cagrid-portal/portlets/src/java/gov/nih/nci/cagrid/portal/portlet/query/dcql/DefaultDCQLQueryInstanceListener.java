package gov.nih.nci.cagrid.portal.portlet.query.dcql;

import gov.nih.nci.cagrid.portal.dao.DCQLQueryInstanceDao;
import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstanceState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DefaultDCQLQueryInstanceListener implements DCQLQueryInstanceListener {

    private static final Log logger = LogFactory
            .getLog(DefaultDCQLQueryInstanceListener.class);

    private DCQLQueryInstanceDao dcqlQueryInstanceDao;

    /**
     *
     */
    public DefaultDCQLQueryInstanceListener() {

    }

    public void onCancelled(DCQLQueryInstance instance, boolean cancelled) {
        if (!cancelled) {
            logger.warn("Couldn't cancel DCQLQueryInstance:" + instance.getId());
        } else {
            instance.setState(QueryInstanceState.CANCELLED);
            getDcqlQueryInstanceDao().save(instance);
        }
    }

    public void onTimeout(DCQLQueryInstance instance, boolean cancelled) {
        if (!cancelled) {
            logger.warn("Couldn't cancel DCQLQueryInstance:" + instance.getId());
        } else {
            instance.setState(QueryInstanceState.TIMEDOUT);
            getDcqlQueryInstanceDao().save(instance);
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstanceListener#onComplete(gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance,
      *      java.lang.String)
      */
    public void onComplete(DCQLQueryInstance instance, String results) {
        if (!QueryInstanceState.CANCELLED.equals(instance.getState())) {
            instance.setFinishTime(new Date());
            instance.setState(QueryInstanceState.COMPLETE);
            getDcqlQueryInstanceDao().save(instance);
            instance.setResult(results);
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstanceListener#onError(gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance,
      *      java.lang.Exception)
      */
    public void onError(DCQLQueryInstance instance, Exception error) {
        if (!QueryInstanceState.CANCELLED.equals(instance.getState())) {
            String msg = error.getMessage();
            logger.info("DCQLQueryInstance:" + instance.getId()
                    + " encountered error: " + msg, error);
            StringWriter w = new StringWriter();
            error.printStackTrace(new PrintWriter(w));
            instance.setError(w.getBuffer().toString());
            instance.setState(QueryInstanceState.ERROR);
            getDcqlQueryInstanceDao().save(instance);
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstanceListener#onRunning(gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance)
      */
    public void onRunning(DCQLQueryInstance instance) {
        instance.setState(QueryInstanceState.RUNNING);
        instance.setStartTime(new Date());
        getDcqlQueryInstanceDao().save(instance);
    }

    /*
      * (non-Javadoc)
      *
      * @see gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstanceListener#onSheduled(gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance)
      */
    public void onSheduled(DCQLQueryInstance instance) {
        instance.setState(QueryInstanceState.SCHEDULED);
        getDcqlQueryInstanceDao().save(instance);
    }

    public DCQLQueryInstanceDao getDcqlQueryInstanceDao() {
        return dcqlQueryInstanceDao;
    }

    public void setDcqlQueryInstanceDao(DCQLQueryInstanceDao dcqlQueryInstanceDao) {
        this.dcqlQueryInstanceDao = dcqlQueryInstanceDao;
    }
}
