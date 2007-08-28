/**
 * 
 */
package org.cagrid.installer.portal;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.tasks.AbstractCreateDatabaseTask;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class CreatePortalDatabaseTask extends AbstractCreateDatabaseTask {

	/**
	 * @param name
	 * @param description
	 */
	public CreatePortalDatabaseTask(String name, String description) {
		super(name, description);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.tasks.AbstractCreateDatabaseTask#getJdbcUrl(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	@Override
	protected String getJdbcUrl(CaGridInstallerModel model) {
		return "jdbc:mysql://" + model.getProperty("portal.db.host") + ":"
				+ model.getProperty("portal.db.port") + "/"
				+ model.getProperty("portal.db.id");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.tasks.AbstractCreateDatabaseTask#getPassword(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	@Override
	protected String getPassword(CaGridInstallerModel model) {
		return model.getProperty("portal.db.password");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.tasks.AbstractCreateDatabaseTask#getUsername(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	@Override
	protected String getUsername(CaGridInstallerModel model) {
		return model.getProperty("portal.db.username");
	}

}
