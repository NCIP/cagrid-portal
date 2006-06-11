/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.cadsr.common.CaDSRServiceI;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Step;

public class CheckCaDSRServiceStep
	extends Step
{
	private EndpointReferenceType endpoint;
	
	public CheckCaDSRServiceStep(int port) 
		throws MalformedURIException
	{
		this(new EndpointReferenceType(new Address("http://localhost:" + port + "/wsrf/services/cagrid/CaDSRService")));
	}

	public CheckCaDSRServiceStep(EndpointReferenceType endpoint)
	{
		super();
		
		this.endpoint = endpoint; 
	}
	
	public void runStep() 
		throws ServiceException, RemoteException
	{
		CaDSRServiceI cadsr = new CaDSRServiceClient(endpoint.getAddress().toString());
		
		Project[] project = cadsr.findAllProjects();
	}
}
