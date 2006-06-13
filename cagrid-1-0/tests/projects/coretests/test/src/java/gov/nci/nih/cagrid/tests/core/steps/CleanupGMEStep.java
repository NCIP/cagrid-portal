/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import com.atomicobject.haste.framework.Step;

public class CleanupGMEStep
	extends Step
{
	private String dbUrl;
	private String user;
	private String password;
	
	public CleanupGMEStep() 
	{
		this("mysql://localhost", "root", "");
	}
	
	public CleanupGMEStep(String dbUrl, String user, String password) 
	{
		super();
		
		this.dbUrl = dbUrl;
		this.user = user;
		this.password = password;
	}
	
	public void runStep() throws Throwable
	{
		// TODO Auto-generated method stub
		
	}

}
