/**
 * 
 */
package org.cagrid.installer.dorian;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.tasks.AntTask;
import org.cagrid.installer.tasks.CaGridAntTask;
import org.cagrid.installer.util.InstallerUtils;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ConfigureDorianTask extends CaGridAntTask {


	/**
	 * @param name
	 * @param description
	 */
	public ConfigureDorianTask(String name, String description) {
		super(name, description, null);
	}

	protected Object runAntTask(CaGridInstallerModel model, String target, Map<String,String> env,
			Properties sysProps) throws Exception {

		model.setProperty(Constants.BUILD_FILE_PATH, InstallerUtils
				.getScriptsBuildFilePath());
		String serviceDestDir = model.getServiceDestDir();
		new AntTask("", "", "configure-dorian-conf", env, sysProps).execute(model);

		if (model.isTrue(Constants.DORIAN_CA_PRESENT)) {
			model.setProperty(Constants.BUILD_FILE_PATH, serviceDestDir
					+ "/dorian/build.xml");
			sysProps.setProperty("cacert.input", model.getProperty(Constants.DORIAN_CA_CERT_PATH));
			sysProps.setProperty("cakey.input", model.getProperty(Constants.DORIAN_CA_KEY_PATH));
			sysProps.setProperty("password.input", model.getProperty(Constants.DORIAN_CA_KEY_PWD));
			new AntTask("", "", "importCA", env, sysProps).execute(model);
		}
		model.setProperty(Constants.BUILD_FILE_PATH, serviceDestDir + "/dorian/build.xml");
		sysProps.setProperty("host.input", model.getProperty(Constants.SERVICE_HOSTNAME));
		File dorianHostCredDir = new File(model.getProperty(Constants.DORIAN_HOST_CRED_DIR));
		sysProps.setProperty("dir.input", dorianHostCredDir.getAbsolutePath());
		new AntTask("", "", "createDorianHostCredentials", env, sysProps)
				.execute(model);
		new AntTask("", "", "configureGlobusToTrustDorian", env, sysProps)
				.execute(model);

		return null;
	}

	@Override
	protected String getBuildFilePath(CaGridInstallerModel model) {
		return null;
	}
}
