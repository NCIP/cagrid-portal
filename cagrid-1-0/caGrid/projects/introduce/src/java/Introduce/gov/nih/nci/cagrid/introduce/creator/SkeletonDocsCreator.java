package gov.nih.nci.cagrid.introduce.creator;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.docs.api.DoxyfileTemplate;
import gov.nih.nci.cagrid.introduce.templates.etc.RegistrationTemplate;

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
		File baseDirectory = new File(info.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR));

		File docsDir = new File(baseDirectory.getAbsolutePath() + File.separator + "docs");
		docsDir.mkdir();

		File apiDir = new File(docsDir.getAbsolutePath() + File.separator + "api");
		apiDir.mkdir();

		DoxyfileTemplate doxyfileT = new DoxyfileTemplate();
		String doxyfileS = doxyfileT.generate(info);
		File doxyfileF = new File(apiDir.getAbsolutePath() + File.separator + "Doxyfile");
		FileWriter doxyfileFW = new FileWriter(doxyfileF);
		doxyfileFW.write(doxyfileS);
		doxyfileFW.close();
	}

}