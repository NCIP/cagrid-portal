/**
 * 
 */
package org.cagrid.installer.tasks;

import java.sql.Connection;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.util.InstallerUtils;
import org.pietschy.wizard.InvalidStateException;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public abstract class AbstractCreateDatabaseTask extends BasicTask {

	private static final Log logger = LogFactory.getLog(AbstractCreateDatabaseTask.class);
	
	/**
	 * @param name
	 * @param description
	 */
	public AbstractCreateDatabaseTask(String name, String description) {
		super(name, description);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.tasks.BasicTask#internalExecute(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	protected Object internalExecute(CaGridInstallerModel model)
			throws Exception {

		String driverUrl = System.getProperty("user.dir") + "/"
				+ model.getProperty(Constants.JDBC_DRIVER_PATH);
		try {
			InstallerUtils.addToClassPath(driverUrl);
		} catch (Exception ex) {
			String msg = "Error adding JDBC driver at '" + driverUrl
					+ "' to classpath: " + ex.getMessage();
			logger.error(msg, ex);
			throw new InvalidStateException(msg, ex);
		}

		String jdbcUrl = getJdbcUrl(model);

		String jdbcBase = InstallerUtils.getJdbcBaseFromJdbcUrl(jdbcUrl);
		String dbName = InstallerUtils.getDbNameFromJdbcUrl(jdbcUrl);

		String mysqlDBUrl = jdbcBase + "/mysql";
		logger.info("Attempting to create database '" + dbName
				+ "' using JDBC URL '" + mysqlDBUrl + "'");

		try {
			Class.forName("org.gjt.mm.mysql.Driver");
		} catch (Exception ex) {
			String msg = "Coudn't load driver 'org.gjt.mm.mysql.Driver': "
					+ ex.getMessage();
			logger.error(msg, ex);
			throw new InvalidStateException(msg, ex);
		}

		String usr = getUsername(model);
		String pwd = getPassword(model);
		
		if (InstallerUtils.isEmpty(pwd)) {
			pwd = "";
		}

		Connection conn = null;
		try {
//			conn = DriverManager.getConnection(mysqlDBUrl, usr, pwd);
			conn = InstallerUtils.getDatabaseConnection(null, mysqlDBUrl, usr, pwd);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("create database " + dbName + ";");
		} catch (Exception ex) {
			String msg = "Error creating database '" + dbName + "': "
					+ ex.getMessage();
			logger.error(msg, ex);
			throw new InvalidStateException(msg, ex);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception ex) {
					logger.error(
							"Error closing connection: " + ex.getMessage(), ex);
				}
			}
		}

		return null;
	}
	
	protected abstract String getPassword(CaGridInstallerModel model);

	protected abstract String getUsername(CaGridInstallerModel model);

	protected abstract String getJdbcUrl(CaGridInstallerModel model);
		
}
