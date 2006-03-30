package gov.nih.nci.cagrid.introduce.creator;

import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.etc.RegistationTemplate;
import gov.nih.nci.cagrid.introduce.templates.etc.SecurityDescTemplate;

import java.io.File;
import java.io.FileWriter;

/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * 
 */
public class SkeletonEtcCreator {

	public SkeletonEtcCreator() {
	}


	public void createSkeleton(ServiceInformation info) throws Exception {
		File baseDirectory = new File(info.getServiceProperties().getProperty("introduce.skeleton.destination.dir"));

		File etcDir = new File(baseDirectory.getAbsolutePath() + File.separator + "etc");
		etcDir.mkdir();

		RegistationTemplate registrationT = new RegistationTemplate();
		String registrationS = registrationT.generate(info);
		File registrationF = new File(etcDir.getAbsolutePath() + File.separator + "registration.xml");
		FileWriter registrationFW = new FileWriter(registrationF);
		registrationFW.write(registrationS);
		registrationFW.close();

		SecurityDescTemplate securityDescT = new SecurityDescTemplate();
		String securityDescS = securityDescT.generate(info);
		File securityDescF = new File(etcDir.getAbsolutePath() + File.separator + "security-desc.xml");
		FileWriter securityDescFW = new FileWriter(securityDescF);
		securityDescFW.write(securityDescS);
		securityDescFW.close();

	}

}