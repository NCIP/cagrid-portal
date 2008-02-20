package org.cagrid.installer.portal;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.tasks.AbstractCreateDatabaseTask;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CreateLiferayDatabaseTask extends AbstractCreateDatabaseTask {

	/**
	 * @param name
	 * @param description
	 */
	public CreateLiferayDatabaseTask(String name, String description) {
		super(name, description);
	}

	protected String getJdbcUrl(CaGridInstallerModel model) {
		return "jdbc:mysql://" + model.getProperty("liferay.db.host") + ":"
				+ model.getProperty("liferay.db.port") + "/"
				+ model.getProperty("liferay.db.id");

	}

	protected String getPassword(CaGridInstallerModel model) {
		return model.getProperty("liferay.db.password");
	}


	protected String getUsername(CaGridInstallerModel model) {
		return model.getProperty("liferay.db.username");
	}

    protected Object internalExecute(CaGridInstallerModel model) throws Exception {
        return super.internalExecute(model);//To change body of overridden methods use File | Settings | File Templates.
    }
}
