package gov.nih.nci.cagrid.introduce.creator;

import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.etc.RegistationTemplate;

import java.io.File;
import java.io.FileWriter;

/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * 
 */
public class SkeletonDocsCreator {

	public SkeletonDocsCreator() {
	}


	public void createSkeleton(ServiceInformation info) throws Exception {
		File baseDirectory = new File(info.getServiceProperties().getProperty("introduce.skeleton.destination.dir"));

		File docsDir = new File(baseDirectory.getAbsolutePath() + File.separator + "docs");
		docsDir.mkdir();

		File apiDir = new File(docsDir.getAbsolutePath() + File.separator + "api");
		apiDir.mkdir();

		RegistationTemplate registrationT = new RegistationTemplate();
		String registrationS = registrationT.generate(info);
		File registrationF = new File(apiDir.getAbsolutePath() + File.separator + "Doxyfile");
		FileWriter registrationFW = new FileWriter(registrationF);
		registrationFW.write(registrationS);
		registrationFW.close();
	}

}