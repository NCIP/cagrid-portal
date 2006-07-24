/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

public class CleanupGMEStep
	extends AbstractDbCleanupStep
{
	public CleanupGMEStep() 
	{
		super();
	}
	
	public CleanupGMEStep(String dbUrl, String user, String password) 
	{
		super(dbUrl, user, password);		
	}
	
	public void runStep() 
		throws Throwable
	{
		super.dropDatabases(new String[] { 
			"globusgme_gme_registry", 
			"globusgme_gme_schema_cache", 
			"globusgme_gme_schema_store"
		});
	}
	
	public static void main(String[] args)
		throws Throwable
	{
		new CleanupGMEStep().runStep(); 
	}
}
