/*
 * Created on Jun 5, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import java.io.File;

import gov.nci.nih.cagrid.tests.core.util.BeanComparator;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.discovery.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Step;

public class CheckServiceMetadataStep
	extends Step
{
	private EndpointReferenceType endpoint;
	private File localMetadata;
	
	public CheckServiceMetadataStep(
		EndpointReferenceType endpoint,
		File localMetadata
	) {
		super();
		
		this.endpoint = endpoint;
		this.localMetadata = localMetadata;
	}
	
	public void runStep() throws Throwable
	{
		ServiceMetadata serviceMetadata = MetadataUtils.getServiceMetadata(endpoint);
		ServiceMetadata localMetadata = (ServiceMetadata) Utils.deserializeDocument(
			this.localMetadata.toString(), ServiceMetadata.class
		);
		
		new BeanComparator(this).assertEquals(localMetadata, serviceMetadata);
	}
	
	public static void main(String[] args) throws Throwable
	{
		int port = Integer.parseInt(System.getProperty("test.globus.port", "8080"));

		EndpointReferenceType endpoint;
		try {
			endpoint = new EndpointReferenceType(new Address("http://localhost:" + port + "/wsrf/services/cagrid/BasicAnalyticalService"));
		} catch (MalformedURIException e) {
			throw new IllegalArgumentException("endpoint badly formed");
		}
		File metadataFile = new File(System.getProperty("GlobusHelperTest.file", 
			"test" + File.separator + "data" + File.separator + "serviceMetadata.xml"
		));
		CheckServiceMetadataStep step = new CheckServiceMetadataStep(endpoint, metadataFile);
		step.runStep();
	}

}
