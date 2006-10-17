/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.evsgridservice.client.EVSGridServiceClient;
import gov.nih.nci.evs.domain.Source;

import java.io.File;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Step;

/**
 * This step will pull caDSR metadata from a caDSR grid service and compare it to a
 * locally cached XML metadata extract.  It accomplishes this by comparing a number of
 * fields in the project, classes, associations, and attributes.
 * @author Patrick McConnell
 */
public class EvsCheckServiceStep
	extends Step
{
	private EndpointReferenceType endpoint;
	private File extractFile;
	
	public EvsCheckServiceStep(int port) 
		throws MalformedURIException
	{
		this(new EndpointReferenceType(new Address("http://localhost:" + port + "/wsrf/services/cagrid/EVSGridService")));
	}

	public EvsCheckServiceStep(EndpointReferenceType endpoint)
	{
		super();
		
		this.endpoint = endpoint;
	}
	
	public void runStep() 
		throws Exception
	{
        EVSGridServiceClient client = new EVSGridServiceClient(endpoint);
		Source[] sources = client.getMetaSources();
		assertNotNull(sources);
	}
}
