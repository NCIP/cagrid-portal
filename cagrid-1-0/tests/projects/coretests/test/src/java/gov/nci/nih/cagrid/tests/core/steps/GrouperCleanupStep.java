/*
 * Created on Sep 8, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.util.AntUtils;

import java.io.File;

public class GrouperCleanupStep
	extends AbstractDbCleanupStep
{
	private File grouperDir;
	
	public GrouperCleanupStep(File grouperDir)
	{
		super();
		
		this.grouperDir = grouperDir;
	}
	
	public void runStep() 
		throws Throwable
	{
		AntUtils.runAnt(grouperDir, null, "dropGrouperSchema", null, null);
		super.dropDatabases(new String[] { 
			"grouper", 
		});
	}
}
