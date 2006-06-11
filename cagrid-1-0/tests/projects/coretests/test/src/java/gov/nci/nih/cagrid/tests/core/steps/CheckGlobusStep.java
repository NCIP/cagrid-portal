/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import java.io.File;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.AxisClient;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.oasis.wsrf.lifetime.Destroy;

import com.atomicobject.haste.framework.Step;
import com.counter.CounterPortType;
import com.counter.CreateCounter;
import com.counter.CreateCounterResponse;
import com.counter.service.CounterServiceAddressingLocator;

public class CheckGlobusStep
	extends Step
{
	private EndpointReferenceType endpoint;
	
	public CheckGlobusStep(int port) 
		throws MalformedURIException
	{
		this(new EndpointReferenceType(new Address("http://localhost:" + port + "/wsrf/services/CounterService")));
	}

	public CheckGlobusStep(EndpointReferenceType endpoint)
	{
		super();
		
		this.endpoint = endpoint; 
	}
	
	public void runStep() 
		throws ServiceException, RemoteException
	{
		CounterServiceAddressingLocator locator = new CounterServiceAddressingLocator();
		// we found it, so tell axis to configure an engine to use it
		EngineConfiguration engineConfig = new FileProvider(
			System.getenv("GLOBUS_LOCATION") + File.separator + "client-config.wsdd"
		);
		// set the engine of the locator
		locator.setEngine(new AxisClient(engineConfig));
		
		CounterPortType counter = locator.getCounterPortTypePort(this.endpoint);
		CreateCounterResponse response = counter.createCounter(new CreateCounter());	
		EndpointReferenceType endpoint = response.getEndpointReference();
		endpoint.getProperties().get_any()[0].getValue();
		counter = locator.getCounterPortTypePort(endpoint); 
		
		int count = counter.add(0);
		assertEquals(count+10, counter.add(10));
		counter.destroy(new Destroy());
	}
}
