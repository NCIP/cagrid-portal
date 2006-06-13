/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import java.io.File;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Step;

public class GetSchemaStep
	extends Step
{
	private EndpointReferenceType endpoint;
	private File schema;
	
	public GetSchemaStep(int port, File schema) 
		throws MalformedURIException
	{
		this(new EndpointReferenceType(new Address("http://localhost:" + port + "/wsrf/services/cagrid/GlobalModelExchange")), schema);
	}

	public GetSchemaStep(EndpointReferenceType endpoint, File schema)
	{
		super();
		
		this.endpoint = endpoint;
		this.schema = schema;
	}
	
	public void runStep() throws Throwable
	{
		// TODO Auto-generated method stub
		
	}

}
