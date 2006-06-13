/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import java.io.File;

import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Step;

public class AdvertiseServiceStep
	extends Step
{
	private File serviceDir;
	private String indexServiceURL;
	
	public AdvertiseServiceStep(File serviceDir, int port) 
		throws MalformedURIException
	{
		this(serviceDir, "http://localhost:" + port + "/wsrf/services/DefaultIndexService");
	}

	public AdvertiseServiceStep(File serviceDir, String indexServiceURL)
	{
		super();
		
		this.serviceDir = serviceDir;
		this.indexServiceURL = indexServiceURL;
	}
	
	public void runStep() throws Throwable
	{
	}
}
