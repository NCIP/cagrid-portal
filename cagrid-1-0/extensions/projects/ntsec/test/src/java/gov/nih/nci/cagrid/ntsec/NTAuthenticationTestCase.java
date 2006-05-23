/*
 * Created on May 22, 2006
 */
package gov.nih.nci.cagrid.ntsec;

import java.net.UnknownHostException;

import javax.security.auth.login.LoginException;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class NTAuthenticationTestCase
	extends TestCase
{
	public NTAuthentication ntAuth;
	
	public NTAuthenticationTestCase(String name)
	{
		super(name);
		
		ntAuth = new NTAuthentication(
			System.getProperty("NTAuthenticationTestCase.domainController", "127.0.0.1")
		);
	}
	
	public void testInvalidCredentials()
		throws UnknownHostException
	{
		LoginException exception = null;
		boolean success = true;
		try {
			success = ntAuth.authenticate("DummyDomain", "DummyUser", "DummyPassword");
		} catch (LoginException e) {
			exception = e;
		} catch (UnknownHostException e) {
			throw e;
		}
		assertNull(exception);
		assertFalse(success);
	}
	
	public void testValidCredentials()
		throws UnknownHostException
	{
		String domain = System.getProperty("NTAuthenticationTestCase.domain");
		String name = System.getProperty("NTAuthenticationTestCase.user");
		String password = System.getProperty("NTAuthenticationTestCase.password");
		
		if (domain == null) throw new IllegalArgumentException("System property NTAuthenticationTestCase.domain not set");
		if (name == null) throw new IllegalArgumentException("System property NTAuthenticationTestCase.name not set");
		if (password == null) throw new IllegalArgumentException("System property NTAuthenticationTestCase.password not set");

		LoginException exception = null;
		try {
			ntAuth.authenticate(domain, name, password);
		} catch (LoginException e) {
			exception = e;
		} catch (UnknownHostException e) {
			throw e;
		}
		assertNull(exception);
	}
	
	public static void main(String[] args) throws Exception
	{
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(NTAuthenticationTestCase.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
