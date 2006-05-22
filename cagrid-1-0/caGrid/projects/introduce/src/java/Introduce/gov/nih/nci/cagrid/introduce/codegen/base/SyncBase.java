package gov.nih.nci.cagrid.introduce.codegen.base;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.codegen.common.SyncTool;
import gov.nih.nci.cagrid.introduce.codegen.common.SynchronizationException;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.ClasspathTemplate;
import gov.nih.nci.cagrid.introduce.templates.JNDIConfigTemplate;
import gov.nih.nci.cagrid.introduce.templates.etc.RegistationTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.ServiceConfigurationTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.ResourceConstantsTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.base.BaseResourceTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.main.MainConfigurationTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.main.MainResourceTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.singleton.SingletonConfigurationTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.singleton.SingletonResourceTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
public class SyncBase extends SyncTool {

	private File srcDir;

	private File etcDir;

	public SyncBase(File baseDirectory, ServiceInformation info) {
		super(baseDirectory, info);
		srcDir = new File(baseDirectory.getAbsolutePath() + File.separator
				+ "src");
		// schemaDir = new File(baseDirectory.getAbsolutePath() + File.separator
		// + "schema");
		etcDir = new File(baseDirectory.getAbsolutePath() + File.separator
				+ "etc");
	}

	public void sync() throws SynchronizationException {
		try {
			ClasspathTemplate classpathT = new ClasspathTemplate();
			String classpathS = classpathT.generate(getServiceInformation());
			File classpathF = new File(getBaseDirectory().getAbsolutePath()
					+ File.separator + ".classpath");
			FileWriter classpathFW = new FileWriter(classpathF);
			classpathFW.write(classpathS);
			classpathFW.close();
			
			RegistationTemplate registrationT = new RegistationTemplate();
			String registrationS = registrationT
					.generate(getServiceInformation());
			File registrationF = new File(etcDir.getAbsolutePath()
					+ File.separator + "registration.xml");
			FileWriter registrationFW = new FileWriter(registrationF);
			registrationFW.write(registrationS);
			registrationFW.close();

			JNDIConfigTemplate jndiConfigT = new JNDIConfigTemplate();
			String jndiConfigS = jndiConfigT.generate(getServiceInformation());
			File jndiConfigF = new File(getBaseDirectory().getAbsolutePath()
					+ File.separator + "jndi-config.xml");
			FileWriter jndiConfigFW = new FileWriter(jndiConfigF);
			jndiConfigFW.write(jndiConfigS);
			jndiConfigFW.close();

		} catch (IOException e) {
			throw new SynchronizationException("Error writing file:"
					+ e.getMessage(), e);
		}
	}

}
