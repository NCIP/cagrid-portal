/**
 * 
 */
package org.cagrid.installer.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.steps.Constants;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ConfigureDorianTask extends CaGridAntTask {

	/**
	 * @param name
	 * @param description
	 * @param targetName
	 */
	public ConfigureDorianTask(String name, String description) {
		super(name, description, null);
	}

	protected Object runAntTask(Map state, String target, Map env,
			Properties sysProps) throws Exception {

		Map m = new HashMap(state);
		m.put(Constants.BUILD_FILE_PATH, state.get(Constants.CAGRID_HOME)
				+ "/projects/installer/deployer/build.xml");
		new AntTask("", "", "configure-dorian-conf", env, sysProps)
				.execute(m);

		if ("true".equals(state.get(Constants.DORIAN_CA_PRESENT))) {
			m.put(Constants.BUILD_FILE_PATH, state.get(Constants.CAGRID_HOME)
					+ "/projects/dorian/build.xml");
			sysProps.setProperty("cacert.input", (String) state
					.get(Constants.DORIAN_CA_CERT_PATH));
			sysProps.setProperty("cakey.input", (String) state
					.get(Constants.DORIAN_CA_KEY_PATH));
			sysProps.setProperty("password.input", (String) state
					.get(Constants.DORIAN_CA_KEY_PWD));
			new AntTask("", "", "importCA", env, sysProps).execute(m);
		}

		m.put(Constants.BUILD_FILE_PATH, state.get(Constants.CAGRID_HOME)
				+ "/projects/dorian/build.xml");
		new AntTask("", "", "configureGlobusToTrustDorian", env, sysProps)
				.execute(m);

		return null;
	}
}
