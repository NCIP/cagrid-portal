package gov.nih.nci.cagrid.introduce.creator;

import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.schema.service.ServiceWSDLTemplate;

import java.io.File;
import java.io.FileWriter;
import java.util.Properties;


public class SkeletonSchemaCreator {

	public SkeletonSchemaCreator() {
	}


	public void createSkeleton(ServiceInformation info) throws Exception {
		File baseDirectory = new File(info.getServiceProperties().getProperty("introduce.skeleton.destination.dir"));

		File schemaDir = new File(baseDirectory.getAbsolutePath() + File.separator + "schema");
		schemaDir.mkdir();

		new File(schemaDir.getAbsolutePath() + File.separator + info.getServiceProperties().getProperty("introduce.skeleton.service.name"))
			.mkdirs();

		ServiceWSDLTemplate serviceWSDLT = new ServiceWSDLTemplate();
		String serviceWSDLS = serviceWSDLT.generate(info.getServiceProperties());
		File serviceWSDLF = new File(schemaDir.getAbsolutePath() + File.separator
			+ info.getServiceProperties().getProperty("introduce.skeleton.service.name") + File.separator
			+ info.getServiceProperties().getProperty("introduce.skeleton.service.name") + ".wsdl");
		FileWriter serviceWSDLFW = new FileWriter(serviceWSDLF);
		serviceWSDLFW.write(serviceWSDLS);
		serviceWSDLFW.close();
	}

}