package gov.nih.nci.cagrid.portal.portlet.query.dcql;

import gov.nih.nci.cagrid.portal.authn.EncryptionService;
import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQueryInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.gsi.GlobusCredential;
import org.springframework.beans.factory.InitializingBean;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DefaultDCQLQueryInstanceExecutor implements
        DCQLQueryInstanceExecutor, InitializingBean {

    private static final Log logger = LogFactory.getLog(DefaultDCQLQueryInstanceExecutor.class);

    private EncryptionService encryptionService;
    private ExecutorService executorService;
    private DCQLQueryInstance instance;
    private DCQLQueryInstanceListener listener;
    private Future future;
    private long timeout = 60000;
    private Date endTime;
    private String defaultFQPServiceUrl;


    /**
     *
     */
    public DefaultDCQLQueryInstanceExecutor() {

    }

    public boolean cancel() {
        boolean cancelled = doCancel();
        listener.onCancelled(instance, cancelled);
        return cancelled;
    }

    public boolean timeout() {
        boolean cancelled = doCancel();
        listener.onTimeout(instance, cancelled);
        return cancelled;
    }

    private boolean doCancel() {
        if (future == null) {
            throw new IllegalStateException("query has not been started");
        }
        return future.cancel(true);
    }

    public void setQueryInstance(DCQLQueryInstance instance) {
        this.instance = instance;
    }


    public void start() {
        GlobusCredential cred = null;

        if (instance.getPortalUser() != null) {
            String proxyStr = instance.getPortalUser().getGridCredential();
            proxyStr = getEncryptionService().decrypt(proxyStr);
            if (proxyStr != null) {
                try {
                    cred = new GlobusCredential(new ByteArrayInputStream(
                            proxyStr.getBytes()));
                } catch (Exception ex) {
                    logger.warn("Error instantiating GlobusCredential: "
                            + ex.getMessage(), ex);
                }
            }
        }

        DCQLQueryTask task = new DCQLQueryTask(instance, listener, cred);
        if(instance.getFqpService()==null){
            task.setFqpUrl(getDefaultFQPServiceUrl());
            logger.info("FQP Service not in Portal. Using default to execute DCQL");
        }

        listener.onSheduled(instance);
        future = getExecutorService().submit(task);
        setEndTime(new Date(new Date().getTime() + getTimeout()));

    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void afterPropertiesSet() throws Exception {
        if (getExecutorService() == null) {
            throw new IllegalStateException(
                    "The executorService property is required.");
        }
    }

    public DCQLQueryInstance getQueryInstance() {
        return instance;
    }

    public void setDcqlQueryInstanceListener(DCQLQueryInstanceListener listener) {
        this.listener = listener;
    }

    public DCQLQueryInstanceListener getDcqlQueryInstanceListener() {
        return listener;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    private class TimeoutThread extends Thread {
        public void run() {
            while (new Date().before(getEndTime())) {
            	System.out.println("Time left: " + (((new Date().getTime())-getEndTime().getTime())/1000));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    break;
                }
            }
            timeout();
        }
    }

    public EncryptionService getEncryptionService() {
        return encryptionService;
    }

    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public String getDefaultFQPServiceUrl() {
        return defaultFQPServiceUrl;
    }

    public void setDefaultFQPServiceUrl(String defaultFQPServiceUrl) {
        this.defaultFQPServiceUrl = defaultFQPServiceUrl;
    }
}

