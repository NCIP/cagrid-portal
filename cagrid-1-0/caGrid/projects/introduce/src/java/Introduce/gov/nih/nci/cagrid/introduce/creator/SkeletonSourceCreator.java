package gov.nih.nci.cagrid.introduce.creator;

import gov.nih.nci.cagrid.introduce.ServiceInformation;
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

public class SkeletonSourceCreator {

	public SkeletonSourceCreator() {
	}

	public void createSkeleton(ServiceInformation info) throws Exception {
		File baseDirectory = new File(info.getServiceProperties().getProperty(
				"introduce.skeleton.destination.dir"));
		System.out.println("Creating new source code in : "
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.destination.dir"));

		File srcDir = new File(baseDirectory.getAbsolutePath() + File.separator
				+ "src");
		srcDir.mkdir();

		new File(srcDir.getAbsolutePath()
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.package.dir")).mkdirs();
		new File(srcDir.getAbsolutePath()
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.package.dir") + File.separator
				+ "client").mkdirs();
		new File(srcDir.getAbsolutePath()
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.package.dir") + File.separator
				+ "common").mkdirs();
		new File(srcDir.getAbsolutePath()
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.package.dir") + File.separator
				+ "service").mkdirs();
		new File(srcDir.getAbsolutePath()
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.package.dir") + File.separator
				+ "service" + File.separator + "globus").mkdirs();
		new File(srcDir.getAbsolutePath()
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.package.dir") + File.separator
				+ "service" + File.separator + "globus" + File.separator
				+ "resource").mkdirs();

		ServiceClientTemplate clientT = new ServiceClientTemplate();
		String clientS = clientT.generate(info);
		File clientF = new File(srcDir.getAbsolutePath()
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.package.dir")
				+ File.separator
				+ "client"
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.service.name") + "Client.java");

		FileWriter clientFW = new FileWriter(clientF);
		clientFW.write(clientS);
		clientFW.close();

		ServiceITemplate iT = new ServiceITemplate();
		String iS = iT.generate(info);
		File iF = new File(srcDir.getAbsolutePath()
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.package.dir")
				+ File.separator
				+ "common"
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.service.name") + "I.java");

		FileWriter iFW = new FileWriter(iF);
		iFW.write(iS);
		iFW.close();

		ServiceImplTemplate implT = new ServiceImplTemplate();
		String implS = implT.generate(info);
		File implF = new File(srcDir.getAbsolutePath()
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.package.dir")
				+ File.separator
				+ "service"
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.service.name") + "Impl.java");

		FileWriter implFW = new FileWriter(implF);
		implFW.write(implS);
		implFW.close();

		ServiceProviderImplTemplate providerImplT = new ServiceProviderImplTemplate();
		String providerImplS = providerImplT.generate(info);
		File providerImplF = new File(srcDir.getAbsolutePath()
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.package.dir")
				+ File.separator
				+ "service"
				+ File.separator
				+ "globus"
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.service.name")
				+ "ProviderImpl.java");

		FileWriter providerImplFW = new FileWriter(providerImplF);
		providerImplFW.write(providerImplS);
		providerImplFW.close();

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

		BaseResourceHomeTemplate baseResourceHomeT = new BaseResourceHomeTemplate();
		String baseResourceHomeS = baseResourceHomeT.generate(info);
		File baseResourceHomeF = new File(srcDir.getAbsolutePath()
				+ File.separator
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.package.dir") + File.separator
				+ "service" + File.separator + "globus" + File.separator
				+ "resource" + File.separator + "BaseResourceHome.java");

		FileWriter baseResourceHomeFW = new FileWriter(baseResourceHomeF);
		baseResourceHomeFW.write(baseResourceHomeS);
		baseResourceHomeFW.close();

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

	}

}