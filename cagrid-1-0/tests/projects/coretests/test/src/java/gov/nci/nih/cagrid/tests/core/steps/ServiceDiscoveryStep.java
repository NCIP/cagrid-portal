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

/**
 * This step attempts to discover a service registered in an index service based upon a locally
 * cached metadata XML file.
 * @author Patrick McConnell
 */
public class ServiceDiscoveryStep
	extends Step
{
	private EndpointReferenceType indexServiceEndpoint;
	private String domainServicePath;
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
		this.metadata = (ServiceMetadata) Utils.deserializeDocument(metadataFile.toString(), ServiceMetadata.class);

		domainServicePath = domainServiceEndpoint.getAddress().toString();
		String search = ":" + domainServiceEndpoint.getAddress().getPort() + "/";
		int index = domainServicePath.indexOf(search);
		domainServicePath = domainServicePath.substring(index+search.length());
	}
	
	public void runStep() throws Throwable
	{
		DiscoveryClient client = new DiscoveryClient(indexServiceEndpoint);
		assertTrue(foundService(client.getAllServices(false)));
		assertTrue(foundService(client.getAllServices(true)));
		
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
			client.discoverServicesByResearchCenter(metadata.getHostingResearchCenter().getResearchCenter().getShortName())
		));
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
		if (endpoints == null) return false;
		
		for (EndpointReferenceType endpoint : endpoints) {
			if (endpoint.getAddress().toString().endsWith(domainServicePath)) return true;
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
