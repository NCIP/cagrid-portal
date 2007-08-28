/**
 * 
 */
package org.cagrid.installer.steps;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Icon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.options.BooleanPropertyConfigurationOption;
import org.cagrid.installer.util.InstallerUtils;
import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public abstract class AbstractDropDatabaseStep extends
		PropertyConfigurationStep {

	private static final Log logger = LogFactory
			.getLog(AbstractDropDatabaseStep.class);

	public AbstractDropDatabaseStep() {

	}

	public AbstractDropDatabaseStep(String name, String description) {
		super(name, description);
	}

	public AbstractDropDatabaseStep(String name, String description, Icon icon) {
		super(name, description, icon);
	}

	private CaGridInstallerModel castModel(WizardModel m) {
		if (!(m instanceof CaGridInstallerModel)) {
			throw new IllegalArgumentException(
					"This step requires an instance of CaGridInstallerModel");
		}
		return (CaGridInstallerModel) m;
	}

	public void init(WizardModel m) {

		CaGridInstallerModel model = castModel(m);

		String propName = getCheckPropertyName(model);

		// Add checkbox
		getOptions().add(
				new BooleanPropertyConfigurationOption(propName, model
						.getMessage("yes"), true, true));

		super.init(m);

	}

	public void applyState() throws InvalidStateException {

		super.applyState();

		if (model.isTrue(getCheckPropertyName(model)) && databaseExists(model)) {
			String db = getDatabase(model);

			Connection conn = null;
			try {
				conn = openConnection();
				Statement stmt = conn.createStatement();
				stmt.executeUpdate("drop database " + db);
				stmt.close();
			} catch (Exception ex) {
				String msg = "Error dropping database '" + db + "': "
						+ ex.getMessage();
				logger.error(msg, ex);
				InstallerUtils.showError(msg);
			} finally {
				closeConnection(conn);
			}
		}
	}

	private Connection openConnection() throws Exception {
		String driver = getDriverClassName(model);
		String url = getJdbcUrl(model);
		String usr = getUsername(model);
		String pwd = getPassword(model);
		if(InstallerUtils.isEmpty(pwd)){
			pwd = "";
		}
//		Class.forName(driver);
//		return DriverManager.getConnection(url, usr, pwd);
		return InstallerUtils.getDatabaseConnection(driver, url, usr, pwd);
	}

	private void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public boolean databaseExists(CaGridInstallerModel model) {
		boolean exists = false;
		String db = getDatabase(model);
		if (db != null) {
			Set<String> dbNames = new HashSet<String>();
			Connection conn = null;
			try {
				conn = openConnection();
				DatabaseMetaData meta = conn.getMetaData();
				ResultSet rs = meta.getCatalogs();
				while (rs.next()) {
					dbNames.add(rs.getString(1));
				}
				rs.close();
			} catch (Exception ex) {
				String msg = "Error checking if database '" + db + "' exists: "
						+ ex.getMessage();
				logger.warn(msg);
			} finally {
				closeConnection(conn);
			}

			exists = dbNames.contains(db);
		}
		return exists;
	}

	protected abstract String getJdbcUrl(CaGridInstallerModel model);

	protected abstract String getUsername(CaGridInstallerModel model);

	protected abstract String getPassword(CaGridInstallerModel model);

	protected abstract String getDriverClassName(CaGridInstallerModel model);

	protected abstract String getDatabase(CaGridInstallerModel model);

	protected abstract String getCheckPropertyName(CaGridInstallerModel model);

}
