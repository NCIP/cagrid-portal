package gov.nih.nci.cagrid.introduce.codegen.metadata;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.JNDIConfigTemplate;
import gov.nih.nci.cagrid.introduce.templates.etc.RegistationTemplate;
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

	private ServiceInformation info;
	private File srcDir;
	private File etcDir;
	private File baseDir;


	public SyncMetadata(File baseDirectory, ServiceInformation info) {
		this.info = info;
		this.baseDir = baseDirectory;
		srcDir = new File(baseDirectory.getAbsolutePath() + File.separator + "src");
		// schemaDir = new File(baseDirectory.getAbsolutePath() + File.separator
		// + "schema");
		etcDir = new File(baseDirectory.getAbsolutePath() + File.separator + "etc");
	}


	public void sync() throws Exception {
		// check for the override attribute that signifies metadata
		// synchronization should be skipped
		if (this.info.getMetadata() != null && this.info.getMetadata().getSynchronizeMetadataFramework() != null
			&& !this.info.getMetadata().getSynchronizeMetadataFramework().booleanValue()) {
			// the preference is set and set to skip, so do nothing
			return;
		}

		BaseResourceTemplate baseResourceT = new BaseResourceTemplate();
		String baseResourceS = baseResourceT.generate(info);
		File baseResourceF = new File(srcDir.getAbsolutePath() + File.separator
			+ info.getServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_PACKAGE_DIR) + File.separator + "service"
			+ File.separator + "globus" + File.separator + "resource" + File.separator + "BaseResource.java");

		FileWriter baseResourceFW = new FileWriter(baseResourceF);
		baseResourceFW.write(baseResourceS);
		baseResourceFW.close();

		MetadataConfigurationTemplate metadataConfigurationT = new MetadataConfigurationTemplate();
		String metadataConfigurationS = metadataConfigurationT.generate(info);
		File metadataConfigurationF = new File(srcDir.getAbsolutePath() + File.separator
			+ info.getServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_PACKAGE_DIR) + File.separator + "service"
			+ File.separator + "globus" + File.separator + "resource" + File.separator + "MetadataConfiguration.java");

		FileWriter metadataConfigurationFW = new FileWriter(metadataConfigurationF);
		metadataConfigurationFW.write(metadataConfigurationS);
		metadataConfigurationFW.close();

		ResourceConstantsTemplate resourceContanstsT = new ResourceConstantsTemplate();
		String resourceContanstsS = resourceContanstsT.generate(info);
		File resourceContanstsF = new File(srcDir.getAbsolutePath() + File.separator
			+ info.getServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_PACKAGE_DIR) + File.separator + "service"
			+ File.separator + "globus" + File.separator + "resource" + File.separator + "ResourceConstants.java");

		FileWriter resourceContanstsFW = new FileWriter(resourceContanstsF);
		resourceContanstsFW.write(resourceContanstsS);
		resourceContanstsFW.close();

		RegistationTemplate registrationT = new RegistationTemplate();
		String registrationS = registrationT.generate(info);
		File registrationF = new File(etcDir.getAbsolutePath() + File.separator + "registration.xml");
		FileWriter registrationFW = new FileWriter(registrationF);
		registrationFW.write(registrationS);
		registrationFW.close();

		JNDIConfigTemplate jndiConfigT = new JNDIConfigTemplate();
		String jndiConfigS = jndiConfigT.generate(info);
		File jndiConfigF = new File(this.baseDir.getAbsolutePath() + File.separator + "jndi-config.xml");
		FileWriter jndiConfigFW = new FileWriter(jndiConfigF);
		jndiConfigFW.write(jndiConfigS);
		jndiConfigFW.close();

	}

}
