/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import java.io.File;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Step;

public class ServiceDiscoveryStep
	extends Step
{
	private EndpointReferenceType endpoint;
	private File metadataFile;
	
	public ServiceDiscoveryStep(int port, File metadataFile) 
		throws MalformedURIException
	{
		this(new EndpointReferenceType(new Address("http://localhost:" + port + "/wsrf/services/cagrid/GlobalModelExchange")), metadataFile);
	}

	public ServiceDiscoveryStep(EndpointReferenceType endpoint, File metadataFile)
	{
		super();
		
		this.endpoint = endpoint;
		this.metadataFile = metadataFile;
	}
	
	public void runStep() throws Throwable
	{
	}
}
