/**
 * 
 */
package org.cagrid.installer.tasks;

import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.util.InstallerUtils;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class GenerateCATask extends CaGridInstallerAntTask {

	/**
	 * @param name
	 * @param description
	 */
	public GenerateCATask(String name, String description) {
		super(name, description, "generate-ca");
	}
	
	protected Object runAntTask(CaGridInstallerModel model, String target, Map<String,String> env,
			Properties sysProps) throws Exception {

		new AntTask("", "", target, env, sysProps).execute(model);
		
		InstallerUtils.copyCACertToTrustStore(model.getProperty(Constants.CA_CERT_PATH));

		return null;
	}

}
