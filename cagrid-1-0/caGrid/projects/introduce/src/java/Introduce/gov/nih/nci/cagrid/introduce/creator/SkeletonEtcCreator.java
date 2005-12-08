package gov.nih.nci.cagrid.introduce.creator;

import gov.nih.nci.cagrid.introduce.templates.etc.RegistationTemplate;

import java.io.File;
import java.io.FileWriter;
import java.util.Properties;


public class SkeletonEtcCreator {

	public SkeletonEtcCreator() {
	}


	public void createSkeleton(Properties properties) throws Exception {
		File baseDirectory = new File(properties.getProperty("introduce.skeleton.destination.dir"));

		File etcDir = new File(baseDirectory.getAbsolutePath() + File.separator + "etc");
		etcDir.mkdir();
		
		RegistationTemplate registrationT = new RegistationTemplate();
		String registrationS = registrationT.generate(properties);
		File registrationF = new File(etcDir.getAbsolutePath() + File.separator + "registration.xml");

		FileWriter registrationFW = new FileWriter(registrationF);
		registrationFW.write(registrationS);
		registrationFW.close();
	}

}