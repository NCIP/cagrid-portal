/*
 * Created on Sep 8, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.util.AntUtils;

import java.io.File;
import java.util.Properties;

import com.atomicobject.haste.framework.Step;

public class GrouperAddAdminStep
	extends Step
{
	private File grouperDir;
	private String userName;
	
	public GrouperAddAdminStep(File grouperDir, String userName)
	{
		super();
		
		this.grouperDir = grouperDir;
		this.userName = userName;
	}
	
	public void runStep() 
		throws Throwable
	{
		Properties sysProps = new Properties();
		sysProps.setProperty("gridId.input", userName);
		
		AntUtils.runAnt(grouperDir, null, "dropGrouperSchema", sysProps, null);
	}
}
