/*
 * Created on Sep 26, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import java.util.HashSet;
import java.util.Map;

import com.atomicobject.haste.framework.Step;

public class StackTraceStep
	extends Step
{
	private long delay;
	private long frequency;
	
	public StackTraceStep()
	{
		this(1000 * 60 * 10, 1000 * 5);
	}
	
	public StackTraceStep(long delay, long frequency)
	{
		super();
		
		this.delay = delay;
		this.frequency = frequency;
	}
	
	public void runStep() 
		throws Throwable
	{
		new StackTraceThread().start();
	}
	
	private class StackTraceThread
		extends Thread
	{
		private HashSet<String> ignoreThreads = new HashSet<String>();
		
		public StackTraceThread()
		{
			super("StackTraceThread");
			
			super.setDaemon(true);
			
			ignoreThreads.add("Signal Dispatcher");
			ignoreThreads.add("Reference Handler");
			ignoreThreads.add("Finalizer");
			ignoreThreads.add("StackTraceThread");
			ignoreThreads.add("DestroyJavaVM");
		}
		
		public void run()
		{
			try { sleep(delay); }
			catch (InterruptedException e) { return; }
			
			while (true) {
				Map<Thread, StackTraceElement[]> threadTable = Thread.getAllStackTraces();
				for (Thread t : threadTable.keySet()) {
					if (ignoreThreads.contains(t.getName())) continue;
					System.out.println(t.getName());
					for (StackTraceElement ste : threadTable.get(t)) {
						System.out.println("  " + ste.getFileName() + " (" + ste.getLineNumber() + ")");
					}
				}
				try { sleep(frequency); }
				catch (InterruptedException e) { break; }
			}
		}
	}
	
	public static void main(String[] args) 
		throws Throwable
	{
		new StackTraceStep(5000, 1000).runStep();
		Object sleep = new Object();
		synchronized (sleep) {
			sleep.wait(10000);
		}
	}
}
