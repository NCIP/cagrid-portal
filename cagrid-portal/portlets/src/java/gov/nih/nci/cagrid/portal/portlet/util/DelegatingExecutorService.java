/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.DisposableBean;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DelegatingExecutorService implements ExecutorService, DisposableBean {

	private ExecutorService executorService;
	
	/**
	 * 
	 */
	public DelegatingExecutorService() {

	}
	
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return executorService.awaitTermination(timeout, unit);
	}
	public void execute(Runnable command) {
		executorService.execute(command);
	}
	public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
		return executorService.invokeAll(tasks, timeout, unit);
	}
	public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks) throws InterruptedException {
		return executorService.invokeAll(tasks);
	}
	public <T> T invokeAny(Collection<Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return executorService.invokeAny(tasks, timeout, unit);
	}
	public <T> T invokeAny(Collection<Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return executorService.invokeAny(tasks);
	}
	public boolean isShutdown() {
		return executorService.isShutdown();
	}
	public boolean isTerminated() {
		return executorService.isTerminated();
	}
	public void shutdown() {
		executorService.shutdown();
	}
	public List<Runnable> shutdownNow() {
		return executorService.shutdownNow();
	}
	public <T> Future<T> submit(Callable<T> task) {
		return executorService.submit(task);
	}
	public <T> Future<T> submit(Runnable task, T result) {
		return executorService.submit(task, result);
	}
	public Future<?> submit(Runnable task) {
		return executorService.submit(task);
	}

	public void destroy() throws Exception {
		shutdown();
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}
	

}
