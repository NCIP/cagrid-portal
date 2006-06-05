/*
 * Created on Jun 5, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import java.io.File;

import gov.nci.nih.cagrid.tests.core.util.BeanComparator;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.discovery.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;

import org.apache.axis.message.addressing.EndpointReferenceType;

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

}
