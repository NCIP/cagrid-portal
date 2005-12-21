package gov.nih.nci.cagrid.introduce.creator;

import gov.nih.nci.cagrid.introduce.templates.etc.RegistationTemplate;
import gov.nih.nci.cagrid.introduce.templates.etc.SecurityDescTemplate;

import java.io.File;
import java.io.FileWriter;
import java.util.Properties;

public class SkeletonEtcCreator {

	public SkeletonEtcCreator() {
	}

	public void createSkeleton(Properties properties) throws Exception {
		File baseDirectory = new File(properties
				.getProperty("introduce.skeleton.destination.dir"));

		File etcDir = new File(baseDirectory.getAbsolutePath() + File.separator
				+ "etc");
		etcDir.mkdir();

		RegistationTemplate registrationT = new RegistationTemplate();
		String registrationS = registrationT.generate(properties);
		File registrationF = new File(etcDir.getAbsolutePath() + File.separator
				+ "registration.xml");
		FileWriter registrationFW = new FileWriter(registrationF);
		registrationFW.write(registrationS);
		registrationFW.close();

		SecurityDescTemplate securityDescT = new SecurityDescTemplate();
		String securityDescS = securityDescT.generate(properties);
		File securityDescF = new File(etcDir.getAbsolutePath() + File.separator
				+ "security-desc.xml");
		FileWriter securityDescFW = new FileWriter(securityDescF);
		securityDescFW.write(securityDescS);
		securityDescFW.close();

	}

}