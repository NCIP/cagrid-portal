package gov.nih.nci.cagrid.introduce.creator;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.codegen.security.SecurityMetadataGenerator;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.etc.RegistrationTemplate;
import gov.nih.nci.cagrid.introduce.templates.etc.SecurityDescTemplate;
import gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadata;

import java.io.File;
import java.io.FileWriter;

import javax.xml.namespace.QName;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class SkeletonEtcCreator {

	public SkeletonEtcCreator() {
	}


	public void createSkeleton(ServiceInformation info) throws Exception {
		File baseDirectory = new File(info.getIntroduceServiceProperties().getProperty(
			IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR));

		File etcDir = new File(baseDirectory.getAbsolutePath() + File.separator + "etc");
		etcDir.mkdir();

		RegistrationTemplate registrationT = new RegistrationTemplate();
		String registrationS = registrationT.generate(info);
		File registrationF = new File(etcDir.getAbsolutePath() + File.separator + "registration.xml");
		FileWriter registrationFW = new FileWriter(registrationF);
		registrationFW.write(registrationS);
		registrationFW.close();

		if (info.getServices() != null && info.getServices().getService() != null) {
			for (int serviceI = 0; serviceI < info.getServices().getService().length; serviceI++) {
				SpecificServiceInformation ssi = new SpecificServiceInformation(info, info.getServices().getService(
					serviceI));
				SecurityDescTemplate securityDescT = new SecurityDescTemplate();
				String securityDescS = securityDescT.generate(ssi);
				File securityDescF = new File(etcDir.getAbsolutePath() + File.separator
					+ info.getServices().getService(serviceI).getName() + "-security-desc.xml");
				FileWriter securityDescFW = new FileWriter(securityDescF);
				securityDescFW.write(securityDescS);
				securityDescFW.close();
				ServiceSecurityMetadata metadata = SecurityMetadataGenerator.getSecurityMetadata(ssi);
				File meta = new File(etcDir.getAbsolutePath() + File.separator
					+ info.getServices().getService(serviceI).getName() + "-security-metadata.xml");
				QName qn = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security",
					"ServiceSecurityMetadata");
				Utils.serializeDocument(meta.getAbsolutePath(), metadata, qn);
			}
		}
	}
}