/*
 * Created on May 23, 2006
 */
package gov.nci.nih.cagrid.tests.performance;

import java.io.File;

public class MemoryTracker
	extends Thread
{
	private File logFile;
	private Exception exception;
	
	public MemoryTracker(File logFile)
	{
		super();
		
		this.logFile = logFile;
	}
	
	public Exception getException()
	{
		return exception;
	}
	
	public void run()
	{
		
	}
}
