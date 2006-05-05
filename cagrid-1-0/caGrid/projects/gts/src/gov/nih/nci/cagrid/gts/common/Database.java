package gov.nih.nci.cagrid.gts.common;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.projectmobius.db.ConnectionManager;
import org.projectmobius.db.DatabaseException;
import org.projectmobius.db.Query;


public abstract class Database {

	private String databaseName;

	protected Log log;


	public Database(String db) {
		this.databaseName = db;
		log = LogFactory.getLog(this.getClass().getName());
	}


	protected abstract ConnectionManager getConnectionManager() throws DatabaseException;


	public abstract void destroyDatabase() throws DatabaseException;


	public abstract void createDatabase() throws DatabaseException;


	public void update(String sql) throws DatabaseException {
		Query.update(getConnectionManager(), sql);
	}


	public long insertGetId(String sql) throws DatabaseException {
		return Query.insertGetId(getConnectionManager(), sql);
	}


	public void releaseConnection(Connection c) {
		try {
			getConnectionManager().releaseConnection(c);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}


	public Connection getConnection() throws DatabaseException {
		return getConnectionManager().getConnection();
	}


	public String getDatabaseName() {
		return databaseName;
	}


	public int getUsedConnectionCount() throws DatabaseException {
		return getConnectionManager().getUsedConnectionCount();
	}


	public boolean tableExists(String tableName) throws DatabaseException {
		boolean exists = false;
		Connection c = null;
		try {
			c = this.getConnectionManager().getConnection();
			DatabaseMetaData dbMetadata = c.getMetaData();
			String[] names = {"TABLE"};
			names[0] = tableName;
			// ResultSet tables = dbMetadata.getTables(null, "%", tableName,
			// names);
			ResultSet tables = dbMetadata.getTables(null, null, tableName, null);
			if (tables.next()) {
				exists = true;
			}
			tables.close();
			this.getConnectionManager().releaseConnection(c);
		} catch (Exception e) {
			try {
				this.getConnectionManager().releaseConnection(c);
			} catch (Exception ex) {
				log.error(e.getMessage(), ex);
			}
			log.error(e.getMessage(), e);

			throw new DatabaseException(e.getMessage());
		}
		return exists;
	}

}
