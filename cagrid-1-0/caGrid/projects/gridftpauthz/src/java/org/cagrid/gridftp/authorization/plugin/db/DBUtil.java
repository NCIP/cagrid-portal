package org.cagrid.gridftp.authorization.plugin.db;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.cagrid.gridftp.authorization.plugin.GridFTPOperation;
import org.cagrid.gridftp.authorization.plugin.GridFTPTuple;


/**
 * This is a utility class to create, verify, modify, and check against a
 * database used for GridFTP authorization. The database schema support a simple
 * tuple (user id, operation, URL). Insert and select operations are used to
 * verify tuples match.
 * 
 * @author <A HREF="MAILTO:jpermar at bmi.osu.edu">Justin Permar</A>
 * @created Mar 5, 2007
 * @version $Id: DBUtil.java,v 1.1 2007-03-22 18:54:44 jpermar Exp $
 */
public class DBUtil {

	static public String DB_NAME = "gridftp_authorization";

	static int AUTH_TABLE_NUM_COLUMNS = 3;
	static String AUTH_TABLE_COLUMN_0_NAME = "USER_ID";
	static String AUTH_TABLE_COLUMN_1_NAME = "OPERATION";
	static String AUTH_TABLE_COLUMN_2_NAME = "URL";

	static String AUTH_TABLE_COLUMN_0_TYPE = "VARCHAR(256)";
	static String AUTH_TABLE_COLUMN_1_TYPE = "VARCHAR(256)";
	static String AUTH_TABLE_COLUMN_2_TYPE = "VARCHAR(256)";

	static int NUM_TABLES = 1;

	private static List<ColumnDesc> AUTH_TABLE_COLUMNS = new ArrayList<ColumnDesc>();
	private static TableDesc AUTH_TABLE = new TableDesc("AUTH_TABLE", AUTH_TABLE_COLUMNS);

	private List<TableDesc> _tables = new ArrayList<TableDesc>();

	private Connection _conn;


	// private Logger _logger;

	/**
	 * @param connectionString
	 *            the connection string to use
	 * @param user
	 *            the DB user to connect to the database as
	 * @param password
	 *            the password for the DB user
	 */
	public DBUtil(String connectionString, String user, String password) throws DatabaseException {

		_conn = connect(connectionString, user, password);
		AUTH_TABLE_COLUMNS.add(new ColumnDesc(AUTH_TABLE_COLUMN_0_NAME, AUTH_TABLE_COLUMN_0_TYPE));
		AUTH_TABLE_COLUMNS.add(new ColumnDesc(AUTH_TABLE_COLUMN_1_NAME, AUTH_TABLE_COLUMN_1_TYPE));
		AUTH_TABLE_COLUMNS.add(new ColumnDesc(AUTH_TABLE_COLUMN_2_NAME, AUTH_TABLE_COLUMN_2_TYPE));
		_tables.add(AUTH_TABLE);
		// verifyDB();
		checkTables();
	}


	private void checkTables() throws DatabaseException {
		// see that tables exist. if not, create them
		for (TableDesc tableDesc : _tables) {
			if (!(tableExists(tableDesc))) {
				createTable(tableDesc);
			}
		}
	}


	private boolean tableExists(TableDesc table) throws DatabaseException {

		PreparedStatement stmt = null;
		ResultSet results = null;
		try {
			stmt = _conn.prepareStatement("SELECT COUNT(*) FROM " + table.getTableName() + " WHERE 1 = 2");
			results = stmt.executeQuery();
			return true; // if table does exist, no rows will ever be
							// returned
		} catch (SQLException e) {
			return false; // if table does not exist, an exception will be
							// thrown
		} finally {
			try {
				if (results != null) {
					results.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				String msg = "Caught SQL exception: " + e.getMessage();
				throw new DatabaseException(msg, e);
			}
		}
	}


	private void createTable(TableDesc table) throws DatabaseException {
		List<ColumnDesc> columns = table.getColumns();
		String creationString = "CREATE TABLE " + table.getTableName() + " ( id INTEGER IDENTITY";
		for (ColumnDesc desc : columns) {
			creationString += ", " + desc.getColumnName() + " " + desc.getDataType();
		}
		creationString += ")";
		update(creationString);

	}


	private static class TableDesc {
		private String _tableName = "AUTH_TABLE";
		private List<ColumnDesc> _columns = new ArrayList<ColumnDesc>();


		public TableDesc(String name, List<ColumnDesc> columns) {
			_tableName = name;
			_columns = columns;
		}


		private String getTableName() {
			return _tableName;
		}


		private List<ColumnDesc> getColumns() {
			return _columns;
		}
	}


	private static class ColumnDesc {
		private String _columnName;
		private String _dataType;


		public ColumnDesc(String columnName, String dataType) {
			_columnName = columnName;
			_dataType = dataType;
		}


		public String getColumnName() {
			return _columnName;
		}


		public String getDataType() {
			return _dataType;
		}
	}


	/**
	 * Checks that the database exists. If it does, checks that the required
	 * table(s) exist. If they do, check that the table schema matches what this
	 * class expects. If the tables don't exist, create them.
	 * 
	 * @throws DatabaseException
	 */
	private Connection connect(String connectionString, String user, String password) throws DatabaseException {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			String msg = "ERROR: failed to load HSQLDB JDBC driver.";
			System.out.println(msg);
			e.printStackTrace();
			throw new DatabaseException(msg, e);
		}

		try {
			return DriverManager.getConnection(connectionString, user, password);
		} catch (SQLException e) {
			String msg = "Could not get connection to BD using connection string: " + connectionString;
			throw new DatabaseException(msg, e);
		}
	}


	/**
	 * This method is used to insert an authorized request into the database
	 * After insertion, the given request will be authorized by the database
	 * authorization plugin.
	 * 
	 * @param tuple
	 *            the tuple representing the GridFTP request
	 * @throws DatabaseException
	 */
	public void insertTuple(GridFTPTuple tuple) throws DatabaseException {
		String expression = "INSERT INTO " + AUTH_TABLE.getTableName() + "(" + AUTH_TABLE_COLUMN_0_NAME + ","
			+ AUTH_TABLE_COLUMN_1_NAME + "," + AUTH_TABLE_COLUMN_2_NAME + ") VALUES('" + tuple.getIdentity() + "', '"
			+ tuple.getOperation() + "', '" + tuple.getURL() + "')";
		try {
			update(expression);
		} catch (DatabaseException e) {
			String msg = "Could not insert: " + expression;
			System.out.println(msg);
			throw e;
		}
	}


	/**
	 * This method is used to remove an authorized request from the database
	 * After removal, the given request will no longer be authorized by the
	 * database authorization plugin.
	 * 
	 * @param tuple
	 * @throws DatabaseException
	 */
	public void removeTuple(GridFTPTuple tuple) throws DatabaseException {
		String expression = "DELETE FROM " + AUTH_TABLE.getTableName() + " WHERE ";
		List<ColumnDesc> columns = AUTH_TABLE.getColumns();
		expression += columns.get(0).getColumnName() + "='" + tuple.getIdentity() + "'";
		expression += " AND " + columns.get(1).getColumnName() + "='" + tuple.getOperation() + "'";
		expression += " AND " + columns.get(2).getColumnName() + "='" + tuple.getURL() + "'";
		try {
			update(expression);
		} catch (DatabaseException e) {
			String msg = "Could not insert: " + expression;
			System.out.println(msg);
			throw e;
		}

	}


	/**
	 * Convenience method to check whether or not the given tuple exists in the
	 * database.
	 * 
	 * @param tuple
	 *            the tuple representing a request
	 * @return true if the tuple exists. This implies that the database
	 *         authorization plugin would authorize any request matching the
	 *         given tuple. Returns false otherwise.
	 * @throws DatabaseException
	 */
	public boolean tupleExists(GridFTPTuple tuple) throws DatabaseException {
		boolean exists = false;
		List<ColumnDesc> columns = AUTH_TABLE.getColumns();
		String expression = "SELECT * from " + AUTH_TABLE.getTableName() + " WHERE ";
		expression += columns.get(0).getColumnName() + "='" + tuple.getIdentity() + "'";
		expression += " AND " + columns.get(1).getColumnName() + "='" + tuple.getOperation() + "'";
		expression += " AND " + columns.get(2).getColumnName() + "='" + tuple.getURL() + "'";

		QueryResult result = query(expression);
		ResultSet rs = result.getResultSet();
		try {
			while (rs.next()) {
				// check if tuple matches
				exists = true;
			}
		} catch (SQLException e) {
			String msg = "Caugh SQL Exception: " + e.getMessage();
			throw new DatabaseException(msg, e);
		}
		endQuery(result);
		return exists;
	}


	/**
	 * Use this to get results for a query. Be sure to clean up the results
	 * using endQuery(), passing in the return value from this method.
	 * 
	 * @param expression
	 * @return a QueryResult instance representing the results of the Query
	 * @throws SQLException
	 */
	private QueryResult query(String expression) throws DatabaseException {

		Statement st = null;
		ResultSet rs = null;

		try {
			// TODO this a synchronization problem?
			st = _conn.createStatement();
			rs = st.executeQuery(expression); // run the query
			QueryResult result = new QueryResult(st, rs);
			return result;
		} catch (SQLException e) {
			String msg = "Exception while processing query: " + expression;
			System.out.println(msg);
			throw new DatabaseException(msg, e);
		}

		// repeated calls to execute but we
		// choose to make a new one each time

		// do something with the result set.
		// dump(rs);
		// st.close(); // NOTE!! if you close a statement the associated
		// ResultSet is

		// closed too
		// so you should copy the contents to some other object.
		// the result set is invalidated also if you recycle an Statement
		// and try to execute some other query before the result set has been
		// completely examined.
	}


	private void endQuery(QueryResult result) throws DatabaseException {
		try {
			result.getStatement().close();
		} catch (SQLException e) {
			String msg = "Could not close the JDBC Statement";
			throw new DatabaseException(msg, e);
		} // closes ResultSet as well
	}


	private static class QueryResult {
		private Statement _statement;
		private ResultSet _rs;


		public QueryResult(Statement s, ResultSet rs) {
			_statement = s;
			_rs = rs;
		}


		public ResultSet getResultSet() {
			return _rs;
		}


		public Statement getStatement() {
			return _statement;
		}

	}


	// use for SQL commands CREATE, DROP, INSERT and UPDATE
	private void update(String expression) throws DatabaseException {

		try {
			Statement st = null;

			st = _conn.createStatement(); // statements

			int i = st.executeUpdate(expression); // run the query

			if (i == -1) {
				System.out.println("db error : " + expression);
			}

			st.close();
		} catch (SQLException e) {
			// log msg
			throw new DatabaseException(e);
		}
	} // void update()


	public static void main(String[] args) throws DatabaseException, MalformedURLException {
		String connectionString = "jdbc:hsqldb:hsql://irondale/" + DB_NAME;
		String dbuser = "sa";
		String password = "";
		DBUtil util = new DBUtil(connectionString, dbuser, password);
		String user = "/O=cagrid.org/OU=training/OU=caBIG User Group/OU=IdP [1]/CN=gridftp";
		GridFTPOperation authOp = GridFTPOperation.READ;
		String url = "ftp://irondale/tmp/yayo";
		GridFTPTuple tuple = new GridFTPTuple(user, authOp, url);
		util.insertTuple(tuple);
		String url2 = "ftp://irondale/tmp/yayo2";
		util.insertTuple(new GridFTPTuple(user, GridFTPOperation.CREATE, url2));
		util.insertTuple(new GridFTPTuple(user, GridFTPOperation.WRITE, url2));
		System.out.println(util.tupleExists(tuple));
		// util.removeTuple(tuple);
		// System.out.println(util.tupleExists(tuple));
	}
}
