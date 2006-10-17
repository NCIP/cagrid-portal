/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.GlobusHelper;

import java.io.IOException;

import com.atomicobject.haste.framework.Step;

/**
 * This is a step that replaces \@caCOREServiceURL\@ in the jndi-config.xml of a Globus deployed caDSR service
 * with the production location of the caDSR APIs (http://cabio.nci.nih.gov/cacore31/http/remoteService).
 * @author Patrick McConnell
 */
public class EvsServiceConfigStep
	extends Step
{
	private GlobusHelper globusHelper;
	
	public EvsServiceConfigStep(GlobusHelper globusHelper) 
	{
		super();
		
		this.globusHelper = globusHelper;
	}
	
	public void runStep() 
		throws IOException
	{
		// does nothing - perform any configuration (like setting the EVS url)
		// currently the EVS url is hard coded in gov.nih.nci.cagrid.evsgridservice.service.CACORE_31_URL
	}
}
