/*
 * Created on Jul 14, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.dorian.client.IdPRegistrationClient;
import gov.nih.nci.cagrid.dorian.idp.bean.Application;

import com.atomicobject.haste.framework.Step;

public class DorianSubmitRegistrationStep
	extends Step
{
	private String serviceURL;
	private Application application;
	
	public DorianSubmitRegistrationStep(int port, Application application) 
	{
		this("https://localhost:" + port + "/wsrf/services/cagrid/Dorian", application);
	}
	
	public DorianSubmitRegistrationStep(String serviceURL, Application application) 
	{
		super();
		
		this.serviceURL = serviceURL;
		this.application = application;
	}
	
	public void runStep() 
		throws Throwable
	{
		IdPRegistrationClient client = new IdPRegistrationClient(serviceURL);
		client.register(application);
	}
}
