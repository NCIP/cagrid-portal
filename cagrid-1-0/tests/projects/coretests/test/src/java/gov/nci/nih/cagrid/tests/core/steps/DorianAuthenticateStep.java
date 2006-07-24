/*
 * Created on Jul 14, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.common.security.ProxyUtil;
import gov.nih.nci.cagrid.dorian.client.IFSUserClient;
import gov.nih.nci.cagrid.dorian.client.IdPAuthenticationClient;
import gov.nih.nci.cagrid.dorian.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.gridca.portal.ProxyManager;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import org.globus.gsi.GlobusCredential;

import com.atomicobject.haste.framework.Step;

public class DorianAuthenticateStep
	extends Step
{
	private String userId;
	private String password;
	private String serviceId;
	private String ifsService;
	private int hours;
	
	public DorianAuthenticateStep(String userId, String password, int port) 
	{
		this(userId, password, "https://localhost:" + port + "/wsrf/services/cagrid/Dorian", "https://localhost:" + port + "/wsrf/services/cagrid/Dorian", 12);
	}
	
	public DorianAuthenticateStep() 
	{
		this("dorian", "password");
	}
	
	public DorianAuthenticateStep(String userId, String password)
	{
		this(userId, password, "https://localhost:8443/wsrf/services/cagrid/Dorian", "https://localhost:8443/wsrf/services/cagrid/Dorian", 12);
	}
	
	public DorianAuthenticateStep(
		String userId, String password, String serviceId, String ifsService, int hours
	) {
		super();
		
		this.userId = userId;
		this.password = password;
		this.serviceId = serviceId;
		this.ifsService = ifsService;
		this.hours = hours;
	}
	
	public void runStep() 
		throws Throwable
	{
		BasicAuthCredential authCred = new BasicAuthCredential();
		authCred.setUserId(userId);
		authCred.setPassword(password);
		IdPAuthenticationClient client = new IdPAuthenticationClient(serviceId, authCred);
		SAMLAssertion saml = client.authenticate();

		IFSUserClient c2 = new IFSUserClient(ifsService);
		GlobusCredential cred = c2.createProxy(saml, new ProxyLifetime(hours, 0, 0));
		ProxyManager.getInstance().addProxy(cred);
		ProxyUtil.saveProxyAsDefault(cred);
	}
	
	public static void main(String[] args) throws Throwable
	{
		try {
			new DorianAuthenticateStep().runStep();
		} finally {
			ProxyUtil.destroyDefaultProxy();
		}
	}
}
