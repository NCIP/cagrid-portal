/*
 * Created on Jul 24, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault;

public class DorianAuthenticateFailStep
	extends DorianAuthenticateStep
{
	public DorianAuthenticateFailStep()
	{
		super();
	}

	public DorianAuthenticateFailStep(String userId, String password, int port)
	{
		super(userId, password, port);
	}

	public DorianAuthenticateFailStep(String userId, String password, String serviceURL, int hours)
	{
		super(userId, password, serviceURL, hours);
	}

	public DorianAuthenticateFailStep(String userId, String password)
	{
		super(userId, password);
	}

	public void runStep() 
		throws Throwable
	{
		Exception exception = null;
		try {
			super.runStep();
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertTrue(exception instanceof PermissionDeniedFault);
	}
}
