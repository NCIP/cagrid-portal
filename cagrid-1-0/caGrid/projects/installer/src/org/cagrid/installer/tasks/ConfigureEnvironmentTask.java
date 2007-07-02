/**
 * 
 */
package org.cagrid.installer.tasks;

import java.io.File;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.steps.Constants;
import org.pietschy.wizard.InvalidStateException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ConfigureEnvironmentTask extends BasicTask {

	private static final Log logger = LogFactory
			.getLog(ConfigureEnvironmentTask.class);

	/**
	 * 
	 */
	public ConfigureEnvironmentTask(String name, String description) {
		super(name, description);
	}

	protected Object internalExecute(Map state) throws Exception {
		state.put(Constants.GLOBUS_DIR_PATH, state.get(Constants.GLOBUS_HOME));
		state.put(Constants.TOMCAT_DIR_PATH, state.get(Constants.TOMCAT_HOME));
		state.put(Constants.GRIDCA_BUILD_FILE_PATH, state
				.get(Constants.CAGRID_HOME)
				+ "/projects/gridca/build.xml");

		int caDaysValid = Integer.valueOf((String) state
				.get(Constants.CA_DAYS_VALID));
		state.put(Constants.SERVICE_CERT_DAYS_VALID, String
				.valueOf(caDaysValid - 1));

		if ("true".equals(state.get(Constants.USE_SECURE_CONTAINER))) {
			if (!"true".equals(state.get(Constants.SERVICE_CERT_PRESENT))) {
				state.put(Constants.GENERATE_SERVICE_CERT, "true");
				if (!"true".equals(state.get(Constants.CA_CERT_PRESENT))) {
					state.put(Constants.GENERATE_CA_CERT, "true");
				}
			} else {
				state.put(Constants.GENERATE_SERVICE_CERT, "false");
				state.put(Constants.GENERATE_CA_CERT, "false");
			}

			state.put(Constants.TOMCAT_KEY, state
					.get(Constants.SERVICE_KEY_PATH));
			state.put(Constants.TOMCAT_CERT, state
					.get(Constants.SERVICE_CERT_PATH));
			state.put(Constants.TOMCAT_KEY_DEST, state
					.get(Constants.TOMCAT_HOME)
					+ "/conf/certs/server.key");
			state.put(Constants.TOMCAT_CERT_DEST, state
					.get(Constants.TOMCAT_HOME)
					+ "/conf/certs/server.cert");

		}

		logger.debug(state);

		return null;
	}

}
