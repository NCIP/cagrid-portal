/**
 * 
 */
package org.cagrid.installer.validator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.util.Utils;
import org.pietschy.wizard.InvalidStateException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DBConnectionValidator implements Validator {
	
	private static final Log logger = LogFactory.getLog(DBConnectionValidator.class);
	
	
	private String hostProp;
	private String portProp;
	private String database;
	private String usernameProp;
	private String passwordProp;
	private String query;
	private String message;
	
	public DBConnectionValidator(String hostProp, String portProp, String database, String usernameProp, String passwordProp, String query, String message){
		this.hostProp = hostProp;
		this.portProp = portProp;
		this.database = database;
		this.usernameProp = usernameProp;
		this.passwordProp = passwordProp;
		this.query = query;
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.validator.Validator#validate(java.util.Map)
	 */
	public void validate(Map state) throws InvalidStateException {
		
		String url = System.getProperty("user.dir") + "/" + state.get(Constants.JDBC_DRIVER_PATH);
		try {
			logger.info("Adding " + url + " to classpath.");
			Utils.addToClassPath(url);
		} catch (Exception ex) {
			String msg = "Error loading " + url + ": " + ex.getMessage();
			logger.error(msg, ex);
			throw new InvalidStateException(this.message);
		}
		String driverClass = (String)state.get(Constants.JDBC_DRIVER_CLASSNAME);
		try{
			logger.info("Loading driver: " + driverClass);
			Class.forName(driverClass);
		}catch(Exception ex){
			String msg = "Error loading JDBC driver: " + ex.getMessage(); 
			logger.error(msg);
			throw new InvalidStateException(this.message);
		}
		String host = (String)state.get(this.hostProp);
		String port = (String)state.get(this.portProp);
		String username = (String)state.get(this.usernameProp);
		String password = (String)state.get(this.passwordProp);
		String dbUrl = "jdbc:mysql://" + host + ":" + port +  "/" + this.database;
		
		Connection conn = null;
		try{
			conn = DriverManager.getConnection(dbUrl, username, password);
			Statement stmt = conn.createStatement();
			stmt.executeQuery(this.query);
		}catch(Exception ex){
			logger.error("Error connecting to " + dbUrl + ": " + ex.getMessage(), ex);
			throw new InvalidStateException(this.message);
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception ex){
					
				}
			}
		}
	}

}
