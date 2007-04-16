package gov.nih.nci.cagrid.dorian.common;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp.BasicDataSource;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: Database.java,v 1.26 2007-04-16 13:58:31 langella Exp $
 */
public class Database extends LoggingObject {

	private BasicDataSource root;

	private BasicDataSource dorian = null;

	private String database;

	private boolean dbBuilt = false;

	private gov.nih.nci.cagrid.dorian.conf.Database conf;


	public Database(gov.nih.nci.cagrid.dorian.conf.Database conf, String database) throws Exception {
		this.database = database;
		this.conf = conf;
		String driver = "com.mysql.jdbc.Driver";
		String dbURL = "jdbc:mysql://" + conf.getHost() + ":" + conf.getPort() + "/";
		root = new BasicDataSource();
		root.setDriverClassName(driver);
		root.setUsername(conf.getUsername());
		root.setPassword(conf.getPassword());
		root.setUrl(dbURL);
		root.setTestOnBorrow(true);
	}


	private void update(BasicDataSource source, String sql) throws DorianInternalFault {
		Connection c = null;
		Statement s = null;
		try {
			c = source.getConnection();
			s = c.createStatement();
			s.executeUpdate(sql);
			s.close();

		} catch (SQLException e) {
			String err = "Unexpected Database Error: " + e.getMessage();
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString(err);
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
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


	public void createDatabaseIfNeeded() throws DorianInternalFault {

		try {
			if (!dbBuilt) {
				if (!databaseExists(database)) {

					// Query.update(this.root, "create database " + database+ "
					// COLLATE ascii_bin");
					update(this.root, "create database " + database);
				}
				if (dorian == null) {
					String driver = "com.mysql.jdbc.Driver";
					String dbURL = "jdbc:mysql://" + conf.getHost() + ":" + conf.getPort() + "/" + database;
					dorian = new BasicDataSource();
					dorian.setDriverClassName(driver);
					dorian.setUsername(conf.getUsername());
					dorian.setPassword(conf.getPassword());
					dorian.setUrl(dbURL);
					dorian.setTestOnBorrow(true);
				}
				dbBuilt = true;
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An error occured while trying to create the Dorian database (" + database + ")");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}

	}


	public void destroyDatabase() throws DorianInternalFault {
		try {
			if (databaseExists(database)) {
				update(this.root, "drop database if exists " + database);
			}
			if (dorian != null) {
				dorian.close();
			}
			if (root != null) {
				root.close();
			}
			dorian = null;
			dbBuilt = false;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An error occured while trying to destroy the Dorian database (" + database + ")");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}


	public boolean tableExists(String tableName) throws DorianInternalFault {
		Connection c = null;
		PreparedStatement stmt = null;
		ResultSet results = null;
		try {
			c = this.dorian.getConnection();
			stmt = c.prepareStatement("SELECT COUNT(*) FROM " + tableName + " WHERE 1 = 2");
			results = stmt.executeQuery();
			return true; // if table does exist, no rows will ever be
			// returned
		} catch (SQLException e) {
			return false; // if table does not exist, an exception will be
			// thrown
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Database Error " + e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			throw (DorianInternalFault) helper.getFault();
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
				this.dorian.close();
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}

		}
	}


	public void update(String sql) throws DorianInternalFault {
		update(dorian, sql);
	}


	public long getLastAutoId(Connection connection) throws DorianInternalFault {
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
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString(err);
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
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


	private long insertGetId(BasicDataSource source, String sql) throws DorianInternalFault {
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
				logError(e.getMessage(), e);
				DorianInternalFault fault = new DorianInternalFault();
				fault.setFaultString(err);
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
				throw fault;
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


	public long insertGetId(String sql) throws DorianInternalFault {
		return insertGetId(dorian, sql);
	}


	public void releaseConnection(Connection c) {
		try {
			c.close();
		} catch (Exception e) {
		}
	}


	public Connection getConnection() throws DorianInternalFault {
		Connection c = null;
		try {
			c = this.dorian.getConnection();
			return c;
		} catch (Exception e) {
			try {
				c.close();
			} catch (Exception ex) {
			}
			String err = "Unexpected Database Error: " + e.getMessage();
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString(err);
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}


	private boolean databaseExists(String db) throws DorianInternalFault {
		boolean exists = false;
		Connection c = null;
		ResultSet dbs = null;
		if (dorian == root) {
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
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Database Error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
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
		return this.dorian.getNumActive();
	}


	public int getRootUsedConnectionCount() {
		return this.root.getNumActive();
	}


	public String getDatabaseName() {
		return database;
	}

}