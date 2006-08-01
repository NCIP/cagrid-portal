/*
 * Created on Jul 31, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.GlobusHelper;
import gov.nci.nih.cagrid.tests.core.util.SimpleXmlReplacer;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/**
 * This step sets the mysql username and password fields in dorian-conf.xml of a deployed Dorian service
 * @author Patrick McConnell
 */
public class DorianConfigureStep
	extends Step
{
	private GlobusHelper globus;
	private String user;
	private String password;
	
	public DorianConfigureStep(GlobusHelper globus)
	{
		this(globus,
			System.getProperty("mysql.user", "root"),
			System.getProperty("mysql.password", "")
		);
	}
	
	public DorianConfigureStep(GlobusHelper globus, String user, String password)
	{
		super();
		
		this.globus = globus;
		this.user = user;
		this.password = password;
	}

	public void runStep() throws Throwable
	{
		File configFile = new File(globus.getTempGlobusLocation(), 
			"etc" + File.separator + "Dorian" + File.separator + "dorian-conf.xml"
		);
		SimpleXmlReplacer replacer = new SimpleXmlReplacer();
		replacer.addReplacement("username", user);
		replacer.addReplacement("password", password);
		replacer.performReplacement(configFile);
	}
}
