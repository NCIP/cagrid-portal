package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.introduce.templates.client.ServiceClientTemplate;
import gov.nih.nci.cagrid.introduce.templates.common.ServiceITemplate;
import gov.nih.nci.cagrid.introduce.templates.service.ServiceImplTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.ServiceProviderImplTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.BaseResourceHomeTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.BaseResourceTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.MetadataConfigurationTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.ResourceConstantsTemplate;

import java.io.File;
import java.io.FileWriter;
import java.util.Properties;


public class SkeletonEtcCreator {

	public SkeletonEtcCreator() {
	}


	public void createSkeleton(Properties properties) throws Exception {
		File baseDirectory = new File(properties.getProperty("introduce.skeleton.destination.dir"));

		File etcDir = new File(baseDirectory.getAbsolutePath() + File.separator + "etc");
		etcDir.mkdir();
		
		ServiceClientTemplate registrationT = new ServiceClientTemplate();
		String registrationS = registrationT.generate(properties);
		File registrationF = new File(etcDir.getAbsolutePath() + File.separator + "registration.xml");

		FileWriter registrationFW = new FileWriter(registrationF);
		registrationFW.write(registrationS);
		registrationFW.close();
	}

}