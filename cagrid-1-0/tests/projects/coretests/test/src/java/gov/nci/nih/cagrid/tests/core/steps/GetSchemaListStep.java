/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import java.io.File;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Step;

public class GetSchemaListStep
	extends Step
{
	private EndpointReferenceType endpoint;
	private String namespace;
	private File[] schemas;
	
	public GetSchemaListStep(int port, String namespace, File[] schemas) 
		throws MalformedURIException
	{
		this(new EndpointReferenceType(new Address("http://localhost:" + port + "/wsrf/services/cagrid/GlobalModelExchange")), namespace, schemas);
	}

	public GetSchemaListStep(EndpointReferenceType endpoint, String namespace, File[] schemas)
	{
		super();
		
		this.endpoint = endpoint;
		this.namespace = namespace;
		this.schemas = schemas;
	}
	
	public void runStep() throws Throwable
	{
		// TODO Auto-generated method stub
		
	}

}
