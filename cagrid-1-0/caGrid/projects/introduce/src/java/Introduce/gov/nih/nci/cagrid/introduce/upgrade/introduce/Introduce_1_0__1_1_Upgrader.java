package gov.nih.nci.cagrid.introduce.upgrade.introduce;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.etc.RegistrationTemplate;
import gov.nih.nci.cagrid.introduce.upgrade.IntroduceUpgraderBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.projectmobius.common.XMLUtilities;

public class Introduce_1_0__1_1_Upgrader extends IntroduceUpgraderBase {

	public Introduce_1_0__1_1_Upgrader(ServiceDescription sd, String servicePath) {
		super(sd, servicePath, "1.0", "1.1");
	}

	protected void upgrade() throws Exception {
		// need to replace the build.xml
		Utils.copyFile(
				new File(getServicePath() + File.separator + "build.xml"),
				new File(getServicePath() + File.separator + "build.xml.OLD"));
		Utils.copyFile(new File("." + File.separator + "skeleton"
				+ File.separator + "build.xml"), new File(getServicePath()
				+ File.separator + "build.xml"));
		Utils.copyFile(new File("." + File.separator + "skeleton"
				+ File.separator + "dev-build.xml"), new File(getServicePath()
				+ File.separator + "dev-build.xml"));

		// need to replace the build-deploy.xml
		Utils.copyFile(new File(getServicePath() + File.separator
				+ "build-deploy.xml"), new File(getServicePath()
				+ File.separator + "build-deploy.xml.OLD"));
		Utils.copyFile(new File("." + File.separator + "skeleton"
				+ File.separator + "build-deploy.xml"), new File(
				getServicePath() + File.separator + "build-deploy.xml"));
		Utils.copyFile(new File("." + File.separator + "skeleton"
				+ File.separator + "dev-build-deploy.xml"), new File(
				getServicePath() + File.separator + "dev-build-deploy.xml"));

		// need to replace the registration.xml
		Properties serviceProperties = new Properties();
		serviceProperties.load(new FileInputStream(
				new File(getServicePath() + File.separator
						+ IntroduceConstants.INTRODUCE_PROPERTIES_FILE)));
		ServiceInformation info = new ServiceInformation(
				getServiceDescription(), serviceProperties, new File(
						getServicePath()));

		Utils.copyFile(new File(getServicePath() + File.separator + "etc"
				+ File.separator + "registration.xml"), new File(
				getServicePath() + File.separator + "etc" + File.separator
						+ "registration.xml.OLD"));

		RegistrationTemplate registrationT = new RegistrationTemplate();
		String registrationS = registrationT.generate(info);
		File registrationF = new File(getServicePath() + File.separator + "etc"
				+ File.separator + "registration.xml");
		FileWriter registrationFW = new FileWriter(registrationF);
		registrationFW.write(registrationS);
		registrationFW.close();

		// need to add to the deploy.properties
		Properties deployProperties = new Properties();
		deployProperties.load(new FileInputStream(new File(getServicePath()
				+ File.separator + IntroduceConstants.DEPLOY_PROPERTIES_FILE)));
		deployProperties.put("index.service.registration.refresh_seconds",
				"600");
		deployProperties.put("index.service.index.refresh_milliseconds",
				"30000");
		deployProperties.put("perform.index.service.registration", "true");
		deployProperties.store(new FileOutputStream(new File(getServicePath()
				+ File.separator + IntroduceConstants.DEPLOY_PROPERTIES_FILE)),
				"Service Deployment Properties");

		// need to add the new template var to the jndi so that it can be
		// replaced
		// by the new property for shutting off registration
		Document jndiDoc = XMLUtilities.fileNameToDocument(getServicePath()
				+ File.separator + "jndi-config.xml");
		List services = jndiDoc.getRootElement().getChildren("service",
				Namespace.getNamespace("http://wsrf.globus.org/jndi/config"));
		Iterator serviceI = services.iterator();
		while (serviceI.hasNext()) {
			Element service = (Element) serviceI.next();
			List resources = service.getChildren("resource", Namespace
					.getNamespace("http://wsrf.globus.org/jndi/config"));
			Iterator resourceI = resources.iterator();
			while (resourceI.hasNext()) {
				Element resource = (Element) resourceI.next();
				List parameters = resource
						.getChild(
								"resourceParams",
								Namespace
										.getNamespace("http://wsrf.globus.org/jndi/config"))
						.getChildren(
								"parameter",
								Namespace
										.getNamespace("http://wsrf.globus.org/jndi/config"));
				Iterator parameterI = parameters.iterator();
				while (parameterI.hasNext()) {
					Element parameter = (Element) parameterI.next();
					if (parameter
							.getChild(
									"name",
									Namespace
											.getNamespace("http://wsrf.globus.org/jndi/config"))
							.getText().equals("performRegistration")) {
						parameter
								.getChild(
										"value",
										Namespace
												.getNamespace("http://wsrf.globus.org/jndi/config"))
								.setText("PERFORM_REGISTRATION");
					}

				}
			}
		}

		// need to add the soapFix.jar to the lib directory
		Utils.copyFile(new File("." + File.separator + "skeleton"
				+ File.separator + "lib" + File.separator
				+ "caGrid-1.0-Introduce-1.1-soapBindingFix.jar"), new File(
				getServicePath() + File.separator + "lib" + File.separator
						+ "caGrid-1.0-Introduce-1.1-soapBindingFix.jar"));
	}

}
