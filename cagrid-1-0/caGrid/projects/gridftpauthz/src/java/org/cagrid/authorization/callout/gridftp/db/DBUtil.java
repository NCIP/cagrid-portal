/*-----------------------------------------------------------------------------
 * Copyright (c) 2003-2004, The Ohio State University,
 * Department of Biomedical Informatics, Multiscale Computing Laboratory
 * All rights reserved.
 * 
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3  All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement: This product includes
 *    material developed by the Mobius Project (http://www.projectmobius.org/).
 * 
 * 4. Neither the name of the Ohio State University, Department of Biomedical
 *    Informatics, Multiscale Computing Laboratory nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * 5. Products derived from this Software may not be called "Mobius"
 *    nor may "Mobius" appear in their names without prior written
 *    permission of Ohio State University, Department of Biomedical
 *    Informatics, Multiscale Computing Laboratory
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *---------------------------------------------------------------------------*/

package org.cagrid.authorization.callout.gridftp.db;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.cagrid.authorization.callout.gridftp.GridFTPOperation;
import org.cagrid.authorization.callout.gridftp.GridFTPTuple;
import org.cagrid.authorization.callout.gridftp.UnknownOperationException;

/**
 * 
  *  DBUtil
  *  This is a utility class to create, verify, modify, and check against a
  *  database used for GridFTP authorization.
  *  The database support a simple tuple (user id, operation, URL). Insert
  *  and select operations are used to verify tuples match.
  * 
  * @author <A HREF="MAILTO:jpermar@bmi.osu.edu">Justin Permar</A>
  * 
  * @created Mar 5, 2007 
  * @version $Id: DBUtil.java,v 1.1 2007-03-19 16:58:43 jpermar Exp $
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
	//private Logger _logger;
	
	public DBUtil(String connectionString, String user, String password) throws DatabaseException {
		
		_conn = connect(connectionString, user, password);
		AUTH_TABLE_COLUMNS.add(new ColumnDesc(AUTH_TABLE_COLUMN_0_NAME, AUTH_TABLE_COLUMN_0_TYPE));
		AUTH_TABLE_COLUMNS.add(new ColumnDesc(AUTH_TABLE_COLUMN_1_NAME, AUTH_TABLE_COLUMN_1_TYPE));
		AUTH_TABLE_COLUMNS.add(new ColumnDesc(AUTH_TABLE_COLUMN_2_NAME, AUTH_TABLE_COLUMN_2_TYPE));
		_tables.add(AUTH_TABLE);
		//verifyDB();
		checkTables();
	}

	private void checkTables() throws DatabaseException {
		//see that tables exist. if not, create them
		for(TableDesc tableDesc : _tables) {
			if (!(tableExists(tableDesc))) {
				createTable(tableDesc);
			}
		}
	}
	
	private boolean tableExists(TableDesc table) throws DatabaseException {
		 
		   PreparedStatement stmt = null;
		   ResultSet results = null;
		   try {
		        stmt = _conn.prepareStatement("SELECT COUNT(*) FROM " +
		              table.getTableName() + " WHERE 1 = 2");
		        results = stmt.executeQuery();
		        return true;  // if table does exist, no rows will ever be returned
		   }
		   catch (SQLException e) {
		        return false;  // if table does not exist, an exception will be thrown
		   } finally {
			   try {
		        if (results != null) {
		              results.close();
		        }
		        if (stmt != null) {
		              stmt.close();
		        }
			   }
		        catch(SQLException e) {
		        	//TODO msg
		        	throw new DatabaseException(e);
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
	
	static class TableDesc {
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
	static class ColumnDesc {
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
	 * Checks that the database exists. If it does, checks that the required table(s)
	 * exist. If they do, check that the table schema matches what this class expects.
	 * If the tables don't exist, create them.
	 * @throws DatabaseException 
	 *
	 */
	public Connection connect(String connectionString, String user, String password) throws DatabaseException {
	        try {
				Class.forName("org.hsqldb.jdbcDriver" );
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

	public void insertTuple(GridFTPTuple tuple) throws DatabaseException {
		String expression = "INSERT INTO " + AUTH_TABLE.getTableName() + "(" + AUTH_TABLE_COLUMN_0_NAME + "," + AUTH_TABLE_COLUMN_1_NAME + "," + AUTH_TABLE_COLUMN_2_NAME + ") VALUES('" + tuple.getIdentity() + "', '" + tuple.getOperation() + "', '" + tuple.getURL() + "')";
		try {
		update(expression);
		} catch(DatabaseException e) {
			String msg = "Could not insert: " + expression;
			System.out.println(msg);
			throw e;
		}
	}
	
	public void removeTuple(GridFTPTuple tuple) throws DatabaseException {
		String expression = "DELETE FROM " + AUTH_TABLE.getTableName() + " WHERE ";
		List<ColumnDesc> columns = AUTH_TABLE.getColumns();
		expression += columns.get(0).getColumnName() + "='" + tuple.getIdentity() + "'";
		expression += " AND " + columns.get(1).getColumnName() + "='" + tuple.getOperation() + "'";
		expression += " AND " + columns.get(2).getColumnName() + "='" + tuple.getURL() + "'";
		try {
		update(expression);
		} catch(DatabaseException e) {
			String msg = "Could not insert: " + expression;
			System.out.println(msg);
			throw e;
		}
		
	}
	
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
				//check if tuple matches
				exists = true;
			}
		} catch (SQLException e) {
			// TODO msg
			throw new DatabaseException(e);
		}
		endQuery(result);
		return exists;
	}
	
	/**
	 * Use this to get results for a query. Be sure to clean up the
	 * results using endQuery(), passing in the return value from this
	 * method.
	 * @param expression
	 * @return a QueryResult instance representing the results of the Query
	 * @throws SQLException
	 */
	private QueryResult query(String expression) throws DatabaseException {

        Statement st = null;
        ResultSet rs = null;

        try {
        	//TODO this a synchronization problem?
			st = _conn.createStatement();
	        rs = st.executeQuery(expression);    // run the query
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
        //dump(rs);
        //st.close();    // NOTE!! if you close a statement the associated ResultSet is

        // closed too
        // so you should copy the contents to some other object.
        // the result set is invalidated also  if you recycle an Statement
        // and try to execute some other query before the result set has been
        // completely examined.
    }
	
	private void endQuery(QueryResult result) throws DatabaseException {
		try {
			result.getStatement().close();
		} catch (SQLException e) {
			String msg = "Could not close the JDBC Statement";
			throw new DatabaseException(msg, e);
		} //closes ResultSet as well
	}
	
	static class QueryResult {
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

	//use for SQL commands CREATE, DROP, INSERT and UPDATE
    public void update(String expression) throws DatabaseException {

    	try {
        Statement st = null;

        st = _conn.createStatement();    // statements

        int i = st.executeUpdate(expression);    // run the query

        if (i == -1) {
            System.out.println("db error : " + expression);
        }

        st.close();
    	} catch(SQLException e) {
    		//log msg
    		throw new DatabaseException(e);
    	}
    }    // void update()

	public static void main(String[] args) throws DatabaseException, UnknownOperationException, MalformedURLException {
		String connectionString = "jdbc:hsqldb:hsql://irondale.bmi.ohio-state.edu/" + DB_NAME;
		String dbuser = "sa";
		String password = "ch3ck1t@uth";
		DBUtil util = new DBUtil(connectionString, dbuser, password);
		String user = "/O=cagrid.org/OU=training/OU=caBIG User Group/OU=IdP [1]/CN=gridftp";
		String operation = "read";
		GridFTPOperation.Operation authOp = GridFTPOperation.toAuthOperation(operation);
		String url = "ftp://irondale.bmi.ohio-state.edu/tmp/yayo";
		GridFTPTuple tuple = new GridFTPTuple(user, authOp, url);
		util.insertTuple(tuple);
		System.out.println(util.tupleExists(tuple));
		//util.removeTuple(tuple);
		//System.out.println(util.tupleExists(tuple));
	}
}
