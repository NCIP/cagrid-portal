/*
 * Created on Sep 8, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.util.AntUtils;

import java.io.File;

import com.atomicobject.haste.framework.Step;

public class GrouperCleanupStep
	extends Step
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
	}
}
