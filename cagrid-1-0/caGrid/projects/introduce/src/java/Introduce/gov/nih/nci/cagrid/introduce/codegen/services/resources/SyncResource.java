package gov.nih.nci.cagrid.introduce.codegen.services.resources;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.codegen.common.SyncTool;
import gov.nih.nci.cagrid.introduce.codegen.common.SynchronizationException;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.service.globus.ServiceConfigurationTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.ResourceConstantsTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.base.BaseResourceTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.main.MainConfigurationTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.main.MainResourceTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.singleton.SingletonConfigurationTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.singleton.SingletonResourceTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
public class SyncResource extends SyncTool {

	private File srcDir;

	private File etcDir;

	private ServiceType service;

	public SyncResource(File baseDirectory, ServiceInformation info,
			ServiceType service) {
		super(baseDirectory, info);
		srcDir = new File(baseDirectory.getAbsolutePath() + File.separator
				+ "src");
		// schemaDir = new File(baseDirectory.getAbsolutePath() + File.separator
		// + "schema");
		etcDir = new File(baseDirectory.getAbsolutePath() + File.separator
				+ "etc");
		this.service = service;
	}

	public void sync() throws SynchronizationException {
		try {

			ServiceConfigurationTemplate serviceConfT = new ServiceConfigurationTemplate();
			String serviceConfS = serviceConfT
					.generate(new SpecificServiceInformation(
							getServiceInformation(), service));
			File serviceConfF = new File(srcDir.getAbsolutePath()
					+ File.separator + CommonTools.getPackageDir(service)
					+ File.separator + "service" + File.separator
					+ "ServiceConfiguration.java");
			FileWriter serviceConfFW = new FileWriter(serviceConfF);
			serviceConfFW.write(serviceConfS);
			serviceConfFW.close();

			if (service.getResourcePropertiesList() != null
					&& service.getResourcePropertiesList()
							.getSynchronizeResourceFramework() != null
					&& !service.getResourcePropertiesList()
							.getSynchronizeResourceFramework().booleanValue()) {
				// the preference is set and set to skip, so do nothing
			} else if (service.getResourceFrameworkType().equals(
					IntroduceConstants.INTRODUCE_BASE_RESOURCE)) {
				// not the base service do nothing....
				BaseResourceTemplate baseResourceT = new BaseResourceTemplate();
				String baseResourceS = baseResourceT
						.generate(new SpecificServiceInformation(
								getServiceInformation(), service));
				File baseResourceF = new File(srcDir.getAbsolutePath()
						+ File.separator + CommonTools.getPackageDir(service)
						+ File.separator + "service" + File.separator
						+ "globus" + File.separator + "resource"
						+ File.separator + "BaseResource.java");

				FileWriter baseResourceFW = new FileWriter(baseResourceF);
				baseResourceFW.write(baseResourceS);
				baseResourceFW.close();

				ResourceConstantsTemplate resourceContanstsT = new ResourceConstantsTemplate();
				String resourceContanstsS = resourceContanstsT
						.generate(new SpecificServiceInformation(
								getServiceInformation(), service));
				File resourceContanstsF = new File(srcDir.getAbsolutePath()
						+ File.separator + CommonTools.getPackageDir(service)
						+ File.separator + "service" + File.separator
						+ "globus" + File.separator + "resource"
						+ File.separator + "ResourceConstants.java");

				FileWriter resourceContanstsFW = new FileWriter(
						resourceContanstsF);
				resourceContanstsFW.write(resourceContanstsS);
				resourceContanstsFW.close();

			} else if (service.getResourceFrameworkType().equals(
					IntroduceConstants.INTRODUCE_SINGLETON_RESOURCE)) {
				SingletonResourceTemplate baseResourceT = new SingletonResourceTemplate();
				String baseResourceS = baseResourceT
						.generate(new SpecificServiceInformation(
								getServiceInformation(), service));
				File baseResourceF = new File(srcDir.getAbsolutePath()
						+ File.separator + CommonTools.getPackageDir(service)
						+ File.separator + "service" + File.separator
						+ "globus" + File.separator + "resource"
						+ File.separator + "BaseResource.java");

				FileWriter baseResourceFW = new FileWriter(baseResourceF);
				baseResourceFW.write(baseResourceS);
				baseResourceFW.close();

				SingletonConfigurationTemplate metadataConfigurationT = new SingletonConfigurationTemplate();
				String metadataConfigurationS = metadataConfigurationT
						.generate(new SpecificServiceInformation(
								getServiceInformation(), service));
				File metadataConfigurationF = new File(srcDir.getAbsolutePath()
						+ File.separator + CommonTools.getPackageDir(service)
						+ File.separator + "service" + File.separator
						+ "globus" + File.separator + "resource"
						+ File.separator + "ResourceConfiguration.java");

				FileWriter metadataConfigurationFW = new FileWriter(
						metadataConfigurationF);
				metadataConfigurationFW.write(metadataConfigurationS);
				metadataConfigurationFW.close();

				ResourceConstantsTemplate resourceContanstsT = new ResourceConstantsTemplate();
				String resourceContanstsS = resourceContanstsT
						.generate(new SpecificServiceInformation(
								getServiceInformation(), service));
				File resourceContanstsF = new File(srcDir.getAbsolutePath()
						+ File.separator + CommonTools.getPackageDir(service)
						+ File.separator + "service" + File.separator
						+ "globus" + File.separator + "resource"
						+ File.separator + "ResourceConstants.java");

				FileWriter resourceContanstsFW = new FileWriter(
						resourceContanstsF);
				resourceContanstsFW.write(resourceContanstsS);
				resourceContanstsFW.close();

			} else if (service.getResourceFrameworkType().equals(
					IntroduceConstants.INTRODUCE_MAIN_RESOURCE)) {
				MainResourceTemplate baseResourceT = new MainResourceTemplate();
				String baseResourceS = baseResourceT
						.generate(new SpecificServiceInformation(
								getServiceInformation(), service));
				File baseResourceF = new File(srcDir.getAbsolutePath()
						+ File.separator + CommonTools.getPackageDir(service)
						+ File.separator + "service" + File.separator
						+ "globus" + File.separator + "resource"
						+ File.separator + "BaseResource.java");

				FileWriter baseResourceFW = new FileWriter(baseResourceF);
				baseResourceFW.write(baseResourceS);
				baseResourceFW.close();

				MainConfigurationTemplate metadataConfigurationT = new MainConfigurationTemplate();
				String metadataConfigurationS = metadataConfigurationT
						.generate(new SpecificServiceInformation(
								getServiceInformation(), service));
				File metadataConfigurationF = new File(srcDir.getAbsolutePath()
						+ File.separator + CommonTools.getPackageDir(service)
						+ File.separator + "service" + File.separator
						+ "globus" + File.separator + "resource"
						+ File.separator + "ResourceConfiguration.java");

				FileWriter metadataConfigurationFW = new FileWriter(
						metadataConfigurationF);
				metadataConfigurationFW.write(metadataConfigurationS);
				metadataConfigurationFW.close();

				ResourceConstantsTemplate resourceContanstsT = new ResourceConstantsTemplate();
				String resourceContanstsS = resourceContanstsT
						.generate(new SpecificServiceInformation(
								getServiceInformation(), service));
				File resourceContanstsF = new File(srcDir.getAbsolutePath()
						+ File.separator + CommonTools.getPackageDir(service)
						+ File.separator + "service" + File.separator
						+ "globus" + File.separator + "resource"
						+ File.separator + "ResourceConstants.java");

				FileWriter resourceContanstsFW = new FileWriter(
						resourceContanstsF);
				resourceContanstsFW.write(resourceContanstsS);
				resourceContanstsFW.close();
			}
		} catch (IOException e) {
			throw new SynchronizationException("Error writing file:"
					+ e.getMessage(), e);
		}
	}

}
