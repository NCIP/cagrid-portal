package gov.nih.nci.cagrid.gts.common;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.projectmobius.db.ConnectionManager;
import org.projectmobius.db.DatabaseException;


public class DerbyDatabase extends Database {

	private String framework = "embedded";
	private String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private String protocol = "jdbc:derby:";
	private String derbyEncryptionPassword = "";
	private String dbLocation;
	private ConnectionManager cm;


	public DerbyDatabase(String db, String derbyEncryptionPassword) {
		super(db);
		this.derbyEncryptionPassword = derbyEncryptionPassword;

		// this.cm = cm;
	}


	protected ConnectionManager getConnectionManager() throws DatabaseException {
		return cm;
	}


	public void createDatabase() throws DatabaseException {
		// TODO Auto-generated method stub

	}


	public void destroyDatabase() throws DatabaseException {
		// TODO Auto-generated method stub

	}


	public boolean tableExists(String tableName) throws DatabaseException {
		Connection c = null;
		boolean exists = false;
		try {
			c = this.cm.getConnection();
			DatabaseMetaData dbMetadata = c.getMetaData();
			String[] names = {"TABLE"};
			names[0] = tableName;
			ResultSet tables = dbMetadata.getTables(null, null, tableName.toUpperCase(), null);
			if (tables.next()) {
				exists = true;
			}
			tables.close();
			this.cm.releaseConnection(c);

		} catch (Exception e) {
			try {
				this.cm.releaseConnection(c);
			} catch (Exception ex) {
				log.error(e.getMessage(), ex);
			}
			log.error(e.getMessage(), e);

			throw new DatabaseException(e.getMessage());
		}
		return exists;
	}
}
