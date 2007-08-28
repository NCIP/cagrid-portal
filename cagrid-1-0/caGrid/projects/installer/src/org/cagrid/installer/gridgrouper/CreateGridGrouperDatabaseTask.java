/**
 * 
 */
package org.cagrid.installer.gridgrouper;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.tasks.AbstractCreateDatabaseTask;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class CreateGridGrouperDatabaseTask extends AbstractCreateDatabaseTask {
	
	/**
	 * @param name
	 * @param description
	 */
	public CreateGridGrouperDatabaseTask(String name, String description) {
		super(name, description);
	}

	@Override
	protected String getJdbcUrl(CaGridInstallerModel model) {
		return model.getProperty(Constants.GRID_GROUPER_DB_URL);
	}

	@Override
	protected String getPassword(CaGridInstallerModel model) {
		return model.getProperty(Constants.GRID_GROUPER_DB_PASSWORD);
	}

	@Override
	protected String getUsername(CaGridInstallerModel model) {
		return model.getProperty(Constants.GRID_GROUPER_DB_USERNAME);
	}

}
