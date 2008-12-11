/**
 * 
 */
package org.cagrid.installer.tasks.cagrid;

import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class CompileCaGridTask extends CaGridAntTask {


	/**
	 * @param name
	 * @param description
	 */
	public CompileCaGridTask(String name, String description) {
		super(name, description, "all");
	}
	
}