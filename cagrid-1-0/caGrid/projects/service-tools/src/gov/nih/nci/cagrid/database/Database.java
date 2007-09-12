package gov.nih.nci.cagrid.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: Database.java,v 1.1 2007-09-12 19:52:28 langella Exp $
 */
public class Database {

	private BasicDataSource root;

	private BasicDataSource coreDB = null;

	private String database;

	private boolean dbBuilt = false;

	private DatabaseConfiguration conf;

	private Log log;


	public Database(DatabaseConfiguration conf, String database) throws DatabaseException {
		log = LogFactory.getLog(this.getClass().getName());
		this.database = database;
		this.conf = conf;
		String driver = "com.mysql.jdbc.Driver";
		String dbURL = "jdbc:mysql://" + conf.getHost() + ":" + conf.getPort() + "/";
		root = new BasicDataSource();
		root.setDriverClassName(driver);
		root.setUsername(conf.getUsername());
		root.setPassword(conf.getPassword());
		root.setUrl(dbURL);
		root.setValidationQuery("select 1");
		root.setTestOnBorrow(true);

	}


	private void update(BasicDataSource source, String sql) throws DatabaseException {
		Connection c = null;
		Statement s = null;
		try {
			c = source.getConnection();
			s = c.createStatement();
			s.executeUpdate(sql);
			s.close();

		} catch (SQLException e) {
			String err = "Unexpected Database Error: " + e.getMessage();
			log.error(err, e);
			throw new DatabaseException(err, e);
		} finally {
			try {
				s.close();
			} catch (Exception e) {
			}
			try {
				c.close();
			} catch (Exception e) {
			}
		}
	}


	public void createDatabaseIfNeeded() throws DatabaseException {

		try {
			if (!dbBuilt) {
				if (!databaseExists(database)) {

					// Query.update(this.root, "create database " + database+ "
					// COLLATE ascii_bin");
					update(this.root, "create database " + database);
				}
				if (coreDB == null) {
					String driver = "com.mysql.jdbc.Driver";
					String dbURL = "jdbc:mysql://" + conf.getHost() + ":" + conf.getPort() + "/" + database;
					coreDB = new BasicDataSource();
					coreDB.setDriverClassName(driver);
					coreDB.setUsername(conf.getUsername());
					coreDB.setPassword(conf.getPassword());
					coreDB.setUrl(dbURL);
					coreDB.setValidationQuery("select 1");
					coreDB.setTestOnBorrow(true);
				}
				dbBuilt = true;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		}

	}


	public void destroyDatabase() throws DatabaseException {
		try {
			if (databaseExists(database)) {
				update(this.root, "drop database if exists " + database);
			}
			if (coreDB != null) {
				coreDB.close();
			}
			if (root != null) {
				root.close();
			}
			coreDB = null;
			dbBuilt = false;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		}
	}


	public boolean tableExists(String tableName) throws DatabaseException {
		Connection c = null;
		PreparedStatement stmt = null;
		ResultSet results = null;
		try {
			c = this.coreDB.getConnection();
			stmt = c.prepareStatement("SELECT COUNT(*) FROM " + tableName + " WHERE 1 = 2");
			results = stmt.executeQuery();
			return true; // if table does exist, no rows will ever be
			// returned
		} catch (SQLException e) {
			return false; // if table does not exist, an exception will be
			// thrown
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			try {
				if (results != null) {
					results.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
			try {
				this.coreDB.close();
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}

		}
	}


	public void update(String sql) throws DatabaseException {
		update(coreDB, sql);
	}


	public long getLastAutoId(Connection connection) throws DatabaseException {
		long id = -1;
		StringBuffer query = new StringBuffer();
		query.append("select LAST_INSERT_ID()");
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
			// Execute the query
			rs = stmt.executeQuery(query.toString());
			// Examine the result set
			if (rs.next()) {
				id = rs.getLong(1);
			}
		} catch (SQLException e) {
			String err = "Unexpected Database Error: " + e.getMessage();
			log.error(e.getMessage(), e);
			throw new DatabaseException(err, e);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				stmt.close();
			} catch (Exception e) {
			}

		}
		return id;
	}


	private long insertGetId(BasicDataSource source, String sql) throws DatabaseException {
		long id = -2;
		Connection c = null;
		Statement s = null;
		synchronized (c) {
			try {
				c = source.getConnection();
				s = c.createStatement();
				s.executeUpdate(sql);
				id = getLastAutoId(c);
				s.close();
			} catch (SQLException e) {
				String err = "Unexpected Database Error: " + e.getMessage();
				log.error(err, e);
				throw new DatabaseException(err, e);
			} finally {
				try {
					s.close();
				} catch (Exception e) {
				}
				try {
					c.close();
				} catch (Exception e) {
				}
			}
		}
		return id;
	}


	public long insertGetId(String sql) throws DatabaseException {
		return insertGetId(coreDB, sql);
	}


	public void releaseConnection(Connection c) {
		try {
			c.close();
		} catch (Exception e) {
		}
	}


	public Connection getConnection() throws DatabaseException {
		Connection c = null;
		try {
			c = this.coreDB.getConnection();
			return c;
		} catch (Exception e) {
			try {
				c.close();
			} catch (Exception ex) {
			}
			String err = "Unexpected Database Error: " + e.getMessage();
			log.error(err, e);
			throw new DatabaseException(err, e);
		}
	}


	private boolean databaseExists(String db) throws DatabaseException {
		boolean exists = false;
		Connection c = null;
		ResultSet dbs = null;
		if (coreDB == root) {
			return true;
		}
		try {
			c = this.root.getConnection();
			DatabaseMetaData dbMetadata = c.getMetaData();

			dbs = dbMetadata.getCatalogs();
			while (dbs.next()) {
				if (dbs.getString(1).equalsIgnoreCase(db)) {
					exists = true;
				}
			}
			dbs.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new DatabaseException(e.getMessage(), e);
		} finally {
			try {
				dbs.close();
			} catch (Exception e) {
			}
			try {
				c.close();
			} catch (Exception e) {
			}
		}
		return exists;
	}


	public int getUsedConnectionCount() {
		return this.coreDB.getNumActive();
	}


	public int getRootUsedConnectionCount() {
		return this.root.getNumActive();
	}


	public String getDatabaseName() {
		return database;
	}

}