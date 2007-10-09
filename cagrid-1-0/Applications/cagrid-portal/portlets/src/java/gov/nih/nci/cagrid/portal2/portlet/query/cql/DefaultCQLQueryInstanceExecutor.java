/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryInstance;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DefaultCQLQueryInstanceExecutor implements CQLQueryInstanceExecutor, InitializingBean {
	
	private ExecutorService executorService;
	private CQLQueryInstance instance;
	private CQLQueryInstanceListener listener;
	private Future future;

	/**
	 * 
	 */
	public DefaultCQLQueryInstanceExecutor() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryExecutor#cancel()
	 */
	public boolean cancel() {
		boolean cancelled = false;
		if(future == null){
			throw new IllegalStateException("query has not been started");
		}
		cancelled = future.cancel(true);
		listener.onCancelled(instance, cancelled);
		return cancelled;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryExecutor#setCQLQuery(gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryInstance)
	 */
	public void setCqlQueryInstance(CQLQueryInstance instance) {
		this.instance = instance;
	}
	
	public void setCqlQueryInstanceListener(CQLQueryInstanceListener listener) {
		this.listener = listener;
	}	
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryExecutor#start()
	 */
	public void start() {
		CQLQueryTask task = new CQLQueryTask(instance, listener);
		future = getExecutorService().submit(task);
		listener.onSheduled(instance);		
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public void afterPropertiesSet() throws Exception {
		if(getExecutorService() == null){
			throw new IllegalStateException("The executorService property is required.");
		}
	}

	public CQLQueryInstance getCqlQueryInstance() {
		return instance;
	}

	public CQLQueryInstanceListener getCqlQueryInstanceListener() {
		return listener;
	}

}
