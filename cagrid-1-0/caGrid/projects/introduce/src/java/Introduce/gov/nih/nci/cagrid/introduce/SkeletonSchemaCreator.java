package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.introduce.templates.schema.service.ServiceWSDLTemplate;

import java.io.File;
import java.io.FileWriter;
import java.util.Properties;


public class SkeletonSchemaCreator {

	public SkeletonSchemaCreator() {
	}


	public void createSkeleton(Properties properties) throws Exception {
		File baseDirectory = new File(properties.getProperty("introduce.skeleton.destination.dir"));

		File schemaDir = new File(baseDirectory.getAbsolutePath() + File.separator + "schema");
		schemaDir.mkdir();

		new File(schemaDir.getAbsolutePath() + File.separator + properties.getProperty("introduce.skeleton.service.name"))
			.mkdirs();

		ServiceWSDLTemplate serviceWSDLT = new ServiceWSDLTemplate();
		String serviceWSDLS = serviceWSDLT.generate(properties);
		File serviceWSDLF = new File(schemaDir.getAbsolutePath() + File.separator
			+ properties.getProperty("introduce.skeleton.service.name") + File.separator
			+ properties.getProperty("introduce.skeleton.service.name") + ".wsdl");
		FileWriter serviceWSDLFW = new FileWriter(serviceWSDLF);
		serviceWSDLFW.write(serviceWSDLS);
		serviceWSDLFW.close();
	}

}