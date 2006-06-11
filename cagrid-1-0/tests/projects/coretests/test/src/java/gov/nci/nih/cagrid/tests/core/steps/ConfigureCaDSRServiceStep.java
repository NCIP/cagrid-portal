/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.GlobusHelper;
import gov.nci.nih.cagrid.tests.core.util.FileUtils;

import java.io.File;
import java.io.IOException;

import com.atomicobject.haste.framework.Step;

public class ConfigureCaDSRServiceStep
extends Step
{
	private GlobusHelper globusHelper;
	
	public ConfigureCaDSRServiceStep(GlobusHelper globusHelper) 
	{
		super();
		
		this.globusHelper = globusHelper;
	}
	
	public void runStep() 
		throws IOException
	{
		FileUtils.replace(
			new File(globusHelper.getTempGlobusLocation(), "etc" + File.separator + "CaDSRService" + File.separator + "jndi-config.xml"),
			"@caCOREServiceURL@",
			"http://cabio.nci.nih.gov/cacore31/http/remoteService"
		);
	}
}
