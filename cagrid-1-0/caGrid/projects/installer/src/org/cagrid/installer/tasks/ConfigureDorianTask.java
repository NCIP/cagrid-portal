/**
 * 
 */
package org.cagrid.installer.tasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.steps.Constants;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ConfigureDorianTask extends CaGridAntTask {

	private static final Log logger = LogFactory
			.getLog(ConfigureDorianTask.class);

	/**
	 * @param name
	 * @param description
	 */
	public ConfigureDorianTask(String name, String description) {
		super(name, description, null);
	}

	protected Object runAntTask(Map state, String target, Map env,
			Properties sysProps) throws Exception {

		Map m = new HashMap(state);
		// logger.debug("########## " + Constants.DORIAN_IFS_PROXYLIFETIME_HOURS
		// + " = " + state.get(Constants.DORIAN_IFS_PROXYLIFETIME_HOURS));
		m.put(Constants.BUILD_FILE_PATH, state.get(Constants.CAGRID_HOME)
				+ "/projects/installer/deployer/build.xml");
		new AntTask("", "", "configure-dorian-conf2", env, sysProps).execute(m);

		if ("true".equals(state.get(Constants.DORIAN_CA_PRESENT))
				|| "true".equals(state.get(Constants.DORIAN_USE_GEN_CA))) {
			m.put(Constants.BUILD_FILE_PATH, state.get(Constants.CAGRID_HOME)
					+ "/projects/dorian/build.xml");
			sysProps.setProperty("cacert.input", (String) state
					.get(Constants.DORIAN_CA_CERT_PATH));
			sysProps.setProperty("cakey.input", (String) state
					.get(Constants.DORIAN_CA_KEY_PATH));
			sysProps.setProperty("password.input", (String) state
					.get(Constants.DORIAN_CA_KEY_PWD));
			new AntTask("", "", "importCA", env, sysProps).execute(m);
		} else {
			// Have to copy CA cert to HOME/.globus/certificates
			logger.debug("Copying CA cert to trust store");
			BufferedReader in = new BufferedReader(new FileReader(
					(String) state.get(Constants.CA_CERT_PATH)));
			File trustDir = new File(System.getProperty("user.home")
					+ "/.globus/certificates");
			if (!trustDir.exists()) {
				trustDir.mkdirs();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(trustDir
					.getAbsolutePath()
					+ "/CA.0"));
			String line = null;
			while ((line = in.readLine()) != null) {
				out.write(line);
			}
			in.close();
			out.flush();
			out.close();
		}

		m.put(Constants.BUILD_FILE_PATH, state.get(Constants.CAGRID_HOME)
				+ "/projects/dorian/build.xml");
		new AntTask("", "", "configureGlobusToTrustDorian", env, sysProps)
				.execute(m);

		return null;
	}
}
