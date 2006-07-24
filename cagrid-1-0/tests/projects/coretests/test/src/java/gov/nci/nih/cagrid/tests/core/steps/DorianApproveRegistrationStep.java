/*
 * Created on Jul 14, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import org.globus.gsi.GlobusCredential;

import gov.nih.nci.cagrid.dorian.client.IdPAdministrationClient;
import gov.nih.nci.cagrid.dorian.idp.bean.Application;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUser;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserStatus;

import com.atomicobject.haste.framework.Step;

public class DorianApproveRegistrationStep
	extends Step
{
	private String serviceURL;
	private Application application;
	private GlobusCredential credential;
	
	public DorianApproveRegistrationStep(Application application, int port, DorianAuthenticateStep auth) 
	{
		this(application, "https://localhost:" + port + "/wsrf/services/cagrid/Dorian", auth.getCredential());
	}
	
	public DorianApproveRegistrationStep(Application application, String serviceURL, GlobusCredential credential) 
	{
		super();
		
		this.application = application;
		this.serviceURL = serviceURL;
		this.credential = credential;
	}
	
	public void runStep() 
		throws Throwable
	{
		IdPAdministrationClient client = new IdPAdministrationClient(serviceURL, credential);
		
		// find users
		IdPUserFilter filter = new IdPUserFilter();
		filter.setUserId(application.getUserId());
		filter.setStatus(IdPUserStatus.Pending);
		IdPUser[] users = client.findUsers(filter);
		assertNotNull(users);
		assertTrue(users.length > 0);

		// find user
		IdPUser user = findUser(users, application);
		assertNotNull(user);
		
		// accept application
		user.setStatus(IdPUserStatus.Active);
		client.updateUser(user);
	}
	
	private IdPUser findUser(IdPUser[] users, Application application)
	{
		for (IdPUser user : users) {
			if (user.getUserId().equals(application.getUserId())) return user;
		}
		return null;
	}
}
