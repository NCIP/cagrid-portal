package gov.nih.nci.cagrid.introduce.codegen.metadata;

import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.etc.RegistationTemplate;
import gov.nih.nci.cagrid.introduce.templates.schema.service.ServiceWSDLTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.BaseResourceTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.MetadataConfigurationTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.ResourceConstantsTemplate;

import java.io.File;
import java.io.FileWriter;

/**
 * SyncMethodsOnDeployment
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 8, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class SyncMetadata {

	private File baseDirectory;

	private ServiceInformation info;

	public SyncMetadata(File baseDirectory, ServiceInformation info) {

		this.baseDirectory = baseDirectory;

		this.info = info;
	}

	public void sync() throws Exception {

		File srcDir = new File(baseDirectory.getAbsolutePath() + File.separator
				+ "src");
		File schemaDir = new File(baseDirectory.getAbsolutePath()
				+ File.separator + "schema");
		File etcDir = new File(baseDirectory.getAbsolutePath() + File.separator
				+ "etc");

		BaseResourceTemplate baseResourceT = new BaseResourceTemplate();
		String baseResourceS = baseResourceT.generate(info);
		File baseResourceF = new File(srcDir.getAbsolutePath()
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.package.dir") + File.separator
				+ "service" + File.separator + "globus" + File.separator
				+ "resource" + File.separator + "BaseResource.java");

		FileWriter baseResourceFW = new FileWriter(baseResourceF);
		baseResourceFW.write(baseResourceS);
		baseResourceFW.close();

		MetadataConfigurationTemplate metadataConfigurationT = new MetadataConfigurationTemplate();
		String metadataConfigurationS = metadataConfigurationT.generate(info);
		File metadataConfigurationF = new File(srcDir.getAbsolutePath()
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.package.dir") + File.separator
				+ "service" + File.separator + "globus" + File.separator
				+ "resource" + File.separator + "MetadataConfiguration.java");

		FileWriter metadataConfigurationFW = new FileWriter(
				metadataConfigurationF);
		metadataConfigurationFW.write(metadataConfigurationS);
		metadataConfigurationFW.close();

		ResourceConstantsTemplate resourceContanstsT = new ResourceConstantsTemplate();
		String resourceContanstsS = resourceContanstsT.generate(info);
		File resourceContanstsF = new File(srcDir.getAbsolutePath()
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.package.dir") + File.separator
				+ "service" + File.separator + "globus" + File.separator
				+ "resource" + File.separator + "ResourceConstants.java");

		FileWriter resourceContanstsFW = new FileWriter(resourceContanstsF);
		resourceContanstsFW.write(resourceContanstsS);
		resourceContanstsFW.close();

		ServiceWSDLTemplate serviceWSDLT = new ServiceWSDLTemplate();
		String serviceWSDLS = serviceWSDLT.generate(info);
		File serviceWSDLF = new File(schemaDir.getAbsolutePath()
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.service.name")
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.service.name") + ".wsdl");
		FileWriter serviceWSDLFW = new FileWriter(serviceWSDLF);
		serviceWSDLFW.write(serviceWSDLS);
		serviceWSDLFW.close();

		RegistationTemplate registrationT = new RegistationTemplate();
		String registrationS = registrationT.generate(info);
		File registrationF = new File(etcDir.getAbsolutePath() + File.separator
				+ "registration.xml");
		FileWriter registrationFW = new FileWriter(registrationF);
		registrationFW.write(registrationS);
		registrationFW.close();

	}

}
