/*
 * Created on Aug 2, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import com.atomicobject.haste.framework.Step;

/**
 * This step causes the test to sleep for a given amount of time before continuing.
 * @author Patrick McConnell
 */
public class SleepStep
	extends Step
{
	private long millisec;
	
	public SleepStep()
	{
		this(3000);
	}
	
	public SleepStep(long millisec)
	{
		super();
		
		this.millisec = millisec;
	}
	
	public void runStep() throws Throwable
	{
		Object sleep = new Object();
		synchronized (sleep) { sleep.wait(millisec); }
	}
}
