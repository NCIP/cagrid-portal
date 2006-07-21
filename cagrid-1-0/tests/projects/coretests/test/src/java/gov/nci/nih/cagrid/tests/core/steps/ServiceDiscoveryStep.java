/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.metadata.service.Operation;
import gov.nih.nci.cagrid.metadata.service.ServiceContext;

import java.io.File;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;

import com.atomicobject.haste.framework.Step;

public class ServiceDiscoveryStep
	extends Step
{
	private EndpointReferenceType indexServiceEndpoint;
	private EndpointReferenceType domainServiceEndpoint;
	private ServiceMetadata metadata;
	
	public ServiceDiscoveryStep(int port, EndpointReferenceType domainServiceEndpoint, File metadataFile) 
		throws Exception
	{
		this(new EndpointReferenceType(new Address("http://localhost:" + port + "/wsrf/services/DefaultIndexService")), domainServiceEndpoint, metadataFile);
	}

	public ServiceDiscoveryStep(EndpointReferenceType indexServiceEndpoint, EndpointReferenceType domainServiceEndpoint, File metadataFile) throws Exception
	{
		super();
		
		this.indexServiceEndpoint = indexServiceEndpoint;
		this.domainServiceEndpoint = domainServiceEndpoint;
		this.metadata = (ServiceMetadata) Utils.deserializeDocument(metadataFile.toString(), ServiceMetadata.class);
	}
	
	public void runStep() throws Throwable
	{
		DiscoveryClient client = new DiscoveryClient(indexServiceEndpoint);
		EndpointReferenceType[] allServices = client.getAllServices(false);
		assertTrue(0 < allServices.length);
		assertTrue(0 < client.getAllServices(true).length);
		
		// service
		assertTrue(foundService(
			client.discoverServicesByName(metadata.getServiceDescription().getService().getName())
		));
		for (ServiceContext context : metadata.getServiceDescription().getService().getServiceContextCollection().getServiceContext()) {
			for (Operation operation : context.getOperationCollection().getOperation()) {
				assertTrue(foundService(
					client.discoverServicesByOperationName(operation.getName())
				));
			}
		}
		
		// center
		assertTrue(foundService(
			client.discoverServicesByResearchCenter(metadata.getHostingResearchCenter().getResearchCenter().getDisplayName())
		));
		for (PointOfContact poc : metadata.getHostingResearchCenter().getResearchCenter().getPointOfContactCollection().getPointOfContact()) {
			assertTrue(foundService(
				client.discoverServicesByPointOfContact(poc)
			));
		}			
	}
	
	private boolean foundService(EndpointReferenceType[] endpoints)
	{
		for (EndpointReferenceType endpoint : endpoints) {
			if (domainServiceEndpoint.toString().equals(endpoint.toString())) return true;
		}
		return false;
	}

	public static void main(String[] args) throws Throwable
	{
		ServiceDiscoveryStep step = new ServiceDiscoveryStep(
			8080,
			new EndpointReferenceType(new Address("http://localhost:8080/wsrf/services/cagrid/BasicAnalyticalServiceWithMetadata")),
			new File("test\\resources\\services\\BasicAnalyticalServiceWithMetadata\\etc\\serviceMetadata.xml")
		);
		step.runStep();
	}
}
