/**
 * 
 */
package org.cagrid.installer.steps;

import javax.swing.Icon;

import org.cagrid.installer.model.CaGridInstallerModel;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DropServiceDatabaseStep extends AbstractDropDatabaseStep {

	private String propPrefix;
	private String checkPropName;
	
	/**
	 * 
	 */
	public DropServiceDatabaseStep() {

	}

	/**
	 * @param name
	 * @param description
	 */
	public DropServiceDatabaseStep(String name, String description, String propPrefix, String checkPropName) {
		this(name, description, propPrefix, checkPropName, null);
	}

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public DropServiceDatabaseStep(String name, String description, String propPrefix, String checkPropName, Icon icon) {
		super(name, description, icon);
		this.propPrefix = propPrefix;
		this.checkPropName = checkPropName;
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.steps.AbstractDropDatabaseStep#getCheckPropertyName(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	@Override
	protected String getCheckPropertyName(CaGridInstallerModel model) {
		return this.checkPropName;
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.steps.AbstractDropDatabaseStep#getDatabase(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	@Override
	protected String getDatabase(CaGridInstallerModel model) {
		return model.getProperty(this.propPrefix + "db.id");
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.steps.AbstractDropDatabaseStep#getDriverClassName(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	@Override
	protected String getDriverClassName(CaGridInstallerModel model) {
		return "org.gjt.mm.mysql.Driver";
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.steps.AbstractDropDatabaseStep#getJdbcUrl(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	@Override
	protected String getJdbcUrl(CaGridInstallerModel model) {
		String host = model.getProperty(this.propPrefix + "db.host");
		String port = model.getProperty(this.propPrefix + "db.port");
		return "jdbc:mysql://" + host + ":" + port +  "/mysql";
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.steps.AbstractDropDatabaseStep#getPassword(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	@Override
	protected String getPassword(CaGridInstallerModel model) {
		return model.getProperty(this.propPrefix + "db.password");
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.steps.AbstractDropDatabaseStep#getUsername(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	@Override
	protected String getUsername(CaGridInstallerModel model) {
		return model.getProperty(this.propPrefix + "db.username");
	}

}
