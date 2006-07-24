/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.atomicobject.haste.framework.Step;

public abstract class AbstractDbCleanupStep
	extends Step
{
	private String dbUrl;
	private String user;
	private String password;
	
	public AbstractDbCleanupStep() 
	{
		this("jdbc:mysql://localhost/test", "root", "");
	}
	
	public AbstractDbCleanupStep(String dbUrl, String user, String password) 
	{
		super();
		
		this.dbUrl = dbUrl;
		this.user = user;
		this.password = password;
	}
	
	protected void dropDatabases(String[] dbNames) 
		throws Throwable
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(dbUrl, user, password);
		
		try {
			Statement stmt = con.createStatement();
	
			for (String dbName : dbNames) {
				try {
					stmt.executeUpdate("drop database " + dbName);
				} catch (SQLException e) {
					if (e.getMessage().indexOf("Database doesn't exist") == -1) throw e;
				}
			}
			
			// make sure no gme databases left
			ResultSet rs = stmt.executeQuery("show databases");
			while (rs.next()) {
				String dbName = rs.getString(1);
				assertTrue(dbName.indexOf("gme") == -1);
			}
		} finally {
			try { con.close(); } catch (Exception e) { }
		}
	}
}
