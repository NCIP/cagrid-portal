package gov.nih.nci.cagrid.introduce.creator;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionsType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;

import java.io.File;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class SkeletonCreator extends Task {

	public SkeletonCreator() {
	}


	public void execute() throws BuildException {
		super.execute();

		Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

		Properties properties = new Properties();
		properties.putAll(this.getProject().getProperties());

		File baseDirectory = new File(properties.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR));

		ServiceDescription introService = null;
		try {
			introService = (ServiceDescription) Utils.deserializeDocument(baseDirectory + File.separator
				+ "introduce.xml", ServiceDescription.class);
		} catch (Exception e1) {
			BuildException be = new BuildException(e1.getMessage());
			be.setStackTrace(e1.getStackTrace());
			be.printStackTrace();
			throw be;
		}

		// need to add the base service....
		ServiceType serviceType = new ServiceType();
		serviceType.setName(properties.getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME));
		serviceType.setNamespace(properties.getProperty(IntroduceConstants.INTRODUCE_SKELETON_NAMESPACE_DOMAIN));
		serviceType.setPackageName(properties.getProperty(IntroduceConstants.INTRODUCE_SKELETON_PACKAGE));
		serviceType.setResourceFrameworkType(IntroduceConstants.INTRODUCE_MAIN_RESOURCE);

		// add new service to the services
		// add new method to array in bean
		// this seems to be a wierd way be adding things....
		ServiceType[] newServices;
		int newLength = 0;
		if (introService.getServices() != null && introService.getServices().getService() != null) {
			newLength = introService.getServices().getService().length + 1;
			newServices = new ServiceType[newLength];
			System.arraycopy(introService.getServices().getService(), 0, newServices, 0, introService.getServices()
				.getService().length);
		} else {
			newLength = 1;
			newServices = new ServiceType[newLength];
		}
		newServices[newLength - 1] = serviceType;
		introService.getServices().setService(newServices);

		// write the modified document back out....
		try {
			Utils.serializeDocument(baseDirectory + File.separator + "introduce.xml", introService, new QName(
				"gme://gov.nih.nci.cagrid/1/Introduce", "ServiceDescription"));
		} catch (Exception e1) {
			BuildException be = new BuildException(e1.getMessage());
			be.setStackTrace(e1.getStackTrace());
			be.printStackTrace();
			throw be;
		}

		// for each extension in the properties make sure to add the xml to the
		// introduce model for them to use.....
		String extensionsList = properties.getProperty(IntroduceConstants.INTRODUCE_SKELETON_EXTENSIONS);
		StringTokenizer strtok = new StringTokenizer(extensionsList, ",", false);
		ExtensionType[] types = new ExtensionType[strtok.countTokens()];
		int count = 0;
		while (strtok.hasMoreTokens()) {
			String token = strtok.nextToken();
			ExtensionType type = new ExtensionType();
			type.setName(token);
			types[count++] = type;
		}
		ExtensionsType exts = new ExtensionsType();
		exts.setExtension(types);
		introService.setExtensions(exts);

		String service = properties.getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
		if (!service.matches("[A-Z]++[A-Za-z0-9\\_\\$]*")) {
			System.err.println("Service Name can only contain [A-Z]++[A-Za-z0-9\\_\\$]*");
			return;
		}
		if (service.substring(0, 1).toLowerCase().equals(service.substring(0, 1))) {
			System.err.println("Service Name cannnot start with lower case letters.");
			return;
		}

		// create the dirs to the basedir if needed
		baseDirectory.mkdirs();

		ServiceInformation info = new ServiceInformation(introService, properties, baseDirectory);
		SkeletonBaseCreator sbc = new SkeletonBaseCreator();
		SkeletonSourceCreator ssc = new SkeletonSourceCreator();
		SkeletonSchemaCreator sscc = new SkeletonSchemaCreator();
		SkeletonEtcCreator sec = new SkeletonEtcCreator();
		SkeletonDocsCreator sdc = new SkeletonDocsCreator();
		SkeletonSecurityOperationProviderCreator ssopc = new SkeletonSecurityOperationProviderCreator();

		// Generate the source
		try {
			if (info.getServices() != null && info.getServices().getService() != null) {
				for (int i = 0; i < info.getServices().getService().length; i++) {
					ssc.createSkeleton(baseDirectory, info, info.getServices().getService(i));
					sscc.createSkeleton(baseDirectory, info, info.getServices().getService(i));
					ssopc.createSkeleton(new SpecificServiceInformation(info, info.getServices().getService(i)));
				}
			}
			sec.createSkeleton(info);
			sdc.createSkeleton(info);
			sbc.createSkeleton(info);
		} catch (Exception e) {
			BuildException be = new BuildException(e.getMessage());
			be.setStackTrace(e.getStackTrace());
			be.printStackTrace();
			throw be;
		}

		try {
			Utils.serializeDocument(baseDirectory + File.separator + "introduce.xml", introService,
				IntroduceConstants.INTRODUCE_SKELETON_QNAME);
		} catch (Exception e) {
			BuildException be = new BuildException(e.getMessage());
			be.setStackTrace(e.getStackTrace());
			be.printStackTrace();
			throw be;
		}
	}
}