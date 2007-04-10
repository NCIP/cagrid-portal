package gov.nih.nci.cagrid.introduce.codegen.services.security;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.codegen.common.SyncTool;
import gov.nih.nci.cagrid.introduce.codegen.common.SynchronizationException;
import gov.nih.nci.cagrid.introduce.codegen.services.security.tools.SecurityMetadataGenerator;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.common.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.etc.SecurityDescTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.ServiceAuthorizationTemplate;
import gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadata;

import java.io.File;
import java.io.FileWriter;

import javax.xml.namespace.QName;


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
public class SyncSecurity extends SyncTool {

	private ServiceType service;


	public SyncSecurity(File baseDirectory, ServiceInformation info, ServiceType service) {
		super(baseDirectory, info);
		this.service = service;
	}


	public void sync() throws SynchronizationException {
		SpecificServiceInformation ssi = new SpecificServiceInformation(getServiceInformation(), service);
		try {
			// regenerate the services security descriptor
			SecurityDescTemplate secDescT = new SecurityDescTemplate();
			String secDescS = secDescT.generate(ssi);
			File secDescF = new File(getBaseDirectory() + File.separator + "etc" + File.separator + service.getName()
				+ "-security-desc.xml");
			FileWriter secDescFW = new FileWriter(secDescF);
			secDescFW.write(secDescS);
			secDescFW.close();

			// regenerate the services security metadata
			ServiceSecurityMetadata metadata = SecurityMetadataGenerator.getSecurityMetadata(ssi);
			File meta = new File(getBaseDirectory() + File.separator + "etc" + File.separator + service.getName()
				+ "-security-metadata.xml");
			QName qn = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security",
				"ServiceSecurityMetadata");
			Utils.serializeDocument(meta.getAbsolutePath(), metadata, qn);

			// regenerate the authorization class for the service
			ServiceAuthorizationTemplate authorizationT = new ServiceAuthorizationTemplate();
			String authorizationS = authorizationT.generate(new SpecificServiceInformation(getServiceInformation(), ssi
				.getService()));
			File authorizationF = new File(getBaseDirectory().getAbsolutePath() + File.separator + "src"
				+ File.separator + CommonTools.getPackageDir(ssi.getService()) + File.separator + "service"
				+ File.separator + "globus" + File.separator + ssi.getService().getName() + "Authorization.java");

			FileWriter authorizationFW = new FileWriter(authorizationF);
			authorizationFW.write(authorizationS);
			authorizationFW.close();

		} catch (Exception e) {
			throw new SynchronizationException(e.getMessage(), e);
		}

	}

}
