/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.ServiceCreateStep;
import gov.nci.nih.cagrid.tests.core.util.IntroduceServiceInfo;
import gov.nci.nih.cagrid.tests.core.util.ServiceHelper;

import java.io.File;
import java.util.Vector;

import org.apache.axis.message.addressing.EndpointReferenceType;

import com.atomicobject.haste.framework.Story;

/**
 * This is a base class to be used for creating and invoking Introduce-built services.
 * @see <a href="http://gforge.nci.nih.gov/plugins/scmcvs/cvsweb.php/cagrid-1-0/Documentation/docs/tests/cagrid-1-0-testing.doc?cvsroot=cagrid-1-0">cagrid-1-0-testing.doc</a> 
 * @author Patrick McConnell
 */
public abstract class AbstractServiceTest
	extends Story
{
	protected ServiceHelper helper;
	
	protected void init(String serviceName)
	{
		// service name
		this.helper = new ServiceHelper(serviceName);
	}
	
	@SuppressWarnings("unchecked")
	public void addInvokeSteps(Vector steps) 
		throws Exception
	{
		steps.addAll(helper.getInvokeSteps());
	}
	
	@SuppressWarnings("unchecked")
	public static void addInvokeSteps(Vector steps, File serviceDir, File testDir, File methodsDir, EndpointReferenceType endpoint) 
		throws Exception
	{
		steps.addAll(ServiceHelper.getInvokeSteps(serviceDir, testDir, methodsDir, endpoint));
	}

	protected boolean storySetUp() 
		throws Throwable
	{
		return true;
	}

	protected void storyTearDown() 
		throws Throwable
	{
		if (getGlobus() != null) {
			getGlobus().stopGlobus(getPort());
			getGlobus().cleanupTempGlobus();
		}
	}
	
	/**
	 * used to make sure that if we are going to use a junit testsuite to test
	 * this that the test suite will not error out looking for a single test......
	 */
	public void testDummy() throws Throwable {
	}

	public String getServiceName()
	{
		return helper.getServiceName();
	}

	public File getCadsrServiceDir()
	{
		return helper.getCadsrServiceDir();
	}

	public ServiceCreateStep getCreateServiceStep()
	{
		return helper.getCreateServiceStep();
	}

	public EndpointReferenceType getEndpoint()
	{
		return helper.getEndpoint();
	}

	public GlobusHelper getGlobus()
	{
		return helper.getGlobus();
	}

	public File getGmeServiceDir()
	{
		return helper.getGmeServiceDir();
	}

	public File getIntroduceDir()
	{
		return helper.getIntroduceDir();
	}

	public File getMetadataFile()
	{
		return helper.getMetadataFile();
	}

	public int getPort()
	{
		return helper.getPort();
	}

	public File getServiceDir()
	{
		return helper.getServiceDir();
	}

	public IntroduceServiceInfo getServiceInfo()
	{
		return helper.getServiceInfo();
	}

	public File getTempDir()
	{
		return helper.getTempDir();
	}

	public File getTestDir()
	{
		return helper.getTestDir();
	}
}
