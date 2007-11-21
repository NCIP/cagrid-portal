/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;

import java.util.Date;
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
	private long timeout = 60000;
	private Date endTime;

	/**
	 * 
	 */
	public DefaultCQLQueryInstanceExecutor() {

	}

	public boolean cancel() {
		boolean cancelled = doCancel();
		listener.onCancelled(instance, cancelled);
		return cancelled;
	}
	
	public boolean timeout(){
		boolean cancelled = doCancel();
		listener.onTimeout(instance, cancelled);
		return cancelled;
	}
	
	private boolean doCancel(){
		if(future == null){
			throw new IllegalStateException("query has not been started");
		}
		return future.cancel(true);
	}

	public void setCqlQueryInstance(CQLQueryInstance instance) {
		this.instance = instance;
	}
	
	public void setCqlQueryInstanceListener(CQLQueryInstanceListener listener) {
		this.listener = listener;
	}	
	
	public void start() {
		CQLQueryTask task = new CQLQueryTask(instance, listener);
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
	
	private class TimeoutThread extends Thread{
		public void run(){
			while(new Date().before(getEndTime())){
				try{
					Thread.sleep(5000);
				}catch(InterruptedException ex){
					break;
				}
			}
			timeout();
		}
	}

}
