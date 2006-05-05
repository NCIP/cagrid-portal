package gov.nih.nci.cagrid.gts.common;

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
		return super.tableExists(tableName.toUpperCase());
	}
}
