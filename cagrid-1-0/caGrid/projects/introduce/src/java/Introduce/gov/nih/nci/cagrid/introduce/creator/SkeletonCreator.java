package gov.nih.nci.cagrid.introduce.creator;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.creator.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.creator.extension.CreationExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;

import java.io.File;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import antlr.CommonToken;

/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * 
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
			e1.printStackTrace();
		}

		ServiceInformation info = new ServiceInformation(introService, properties, baseDirectory);
		SkeletonBaseCreator sbc = new SkeletonBaseCreator();
		SkeletonSourceCreator ssc = new SkeletonSourceCreator();
		SkeletonSchemaCreator sscc = new SkeletonSchemaCreator();
		SkeletonEtcCreator sec = new SkeletonEtcCreator();
		SkeletonDocsCreator sdc = new SkeletonDocsCreator();

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

		// Generate the source
		try {
			sbc.createSkeleton(info);
			ssc.createSkeleton(info);
			sscc.createSkeleton(info);
			sec.createSkeleton(info);
			sdc.createSkeleton(info);
		} catch (Exception e) {
			BuildException be = new BuildException(e.getMessage());
			be.setStackTrace(e.getStackTrace());
			be.printStackTrace();
			throw be;
		}
		
		//run any extensions that need to be ran
		if(introService.getExtensions()!=null && introService.getExtensions().getExtension()!=null){
			ExtensionType[] extensions = introService.getExtensions().getExtension();
			for(int i =0; i < extensions.length; i++){
				CreationExtensionPostProcessor pp = ExtensionTools.getCreationPostProcessor(extensions[i].getName());
				try {
					pp.postCreate(properties);
				} catch (CreationExtensionException e) {
					e.printStackTrace();
				}
			}
		}
	}
}