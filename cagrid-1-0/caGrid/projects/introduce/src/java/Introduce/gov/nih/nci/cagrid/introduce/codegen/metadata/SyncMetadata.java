package gov.nih.nci.cagrid.introduce.codegen.metadata;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.beans.metadata.ServiceMetadataListType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.templates.etc.RegistationTemplate;
import gov.nih.nci.cagrid.introduce.templates.schema.service.ServiceWSDLTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.BaseResourceTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.MetadataConfigurationTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.ResourceConstantsTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

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

	public static final String DIR_OPT = "d";

	public static final String DIR_OPT_FULL = "directory";

	private File baseDirectory;

	private ServiceMetadataListType metadatas;

	private MethodsType methods;

	private Properties serviceProperties;

	public SyncMetadata(File baseDirectory) {

		this.baseDirectory = baseDirectory;
	}

	public void sync() throws Exception {

		methods = (MethodsType) CommonTools.deserializeDocument(baseDirectory
				+ File.separator + "introduceMethods.xml", MethodsType.class);
		metadatas = (ServiceMetadataListType) CommonTools.deserializeDocument(
				baseDirectory + File.separator + "introduceMetadata.xml",
				ServiceMetadataListType.class);

		File servicePropertiesFile = new File(baseDirectory.getAbsolutePath()
				+ File.separator + "introduce.properties");
		serviceProperties = new Properties();
		serviceProperties.load(new FileInputStream(servicePropertiesFile));

		ServiceInformation info = new ServiceInformation(this.methods,
				this.metadatas, this.serviceProperties);

		File baseDirectory = new File(info.getServiceProperties().getProperty(
				"introduce.skeleton.destination.dir"));
		System.out.println("Creating new source code in : "
				+ info.getServiceProperties().getProperty(
						"introduce.skeleton.destination.dir"));

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
		String serviceWSDLS = serviceWSDLT
				.generate(info.getServiceProperties());
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
		String registrationS = registrationT.generate(info
				.getServiceProperties());
		File registrationF = new File(etcDir.getAbsolutePath() + File.separator
				+ "registration.xml");
		FileWriter registrationFW = new FileWriter(registrationF);
		registrationFW.write(registrationS);
		registrationFW.close();

	}

	public static void main(String[] args) {
		Options options = new Options();
		Option directoryOpt = new Option(DIR_OPT, DIR_OPT_FULL, true,
				"The include tool directory");
		options.addOption(directoryOpt);

		CommandLineParser parser = new PosixParser();

		File directory = null;

		try {
			CommandLine line = parser.parse(options, args);
			directory = new File(line.getOptionValue(DIR_OPT));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		SyncMetadata sync = new SyncMetadata(directory);
		try {
			sync.sync();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
