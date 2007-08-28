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
import org.cagrid.installer.util.InstallerUtils;
import org.pietschy.wizard.InvalidStateException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class DBConnectionValidator implements Validator {
	
	private static final Log logger = LogFactory.getLog(DBConnectionValidator.class);
	
	

	private String usernameProp;
	private String passwordProp;
	private String query;
	private String message;
	
	public DBConnectionValidator(String usernameProp, String passwordProp, String query, String message){
		this.usernameProp = usernameProp;
		this.passwordProp = passwordProp;
		this.query = query;
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.validator.Validator#validate(java.util.Map)
	 */
	public void validate(Map state) throws InvalidStateException {
		
		String url = getJdbcDriverJarUrl(state);
		try {
			logger.info("Adding " + url + " to classpath.");
			InstallerUtils.addToClassPath(url);
		} catch (Exception ex) {
			String msg = "Error loading " + url + ": " + ex.getMessage();
			logger.error(msg, ex);
			throw new InvalidStateException(getMessage());
		}
		
		String driverClass = getJdbcDriver(state);
		try{
			logger.info("Loading driver: " + driverClass);
			Class.forName(driverClass);
		}catch(Exception ex){
			String msg = "Error loading JDBC driver: " + ex.getMessage(); 
			logger.error(msg);
			throw new InvalidStateException(getMessage());
		}

		String username = (String)state.get(getUsernameProp());
		String password = (String)state.get(getPasswordProp());
		if(password == null || password.trim().length() == 0){
			password = "";
		}
		String dbUrl = getJdbcUrl(state);
		
		Connection conn = null;
		try{
			conn = DriverManager.getConnection(dbUrl, username, password);
			Statement stmt = conn.createStatement();
			stmt.executeQuery(getQuery());
		}catch(Exception ex){
			logger.error("Error connecting to " + dbUrl + ": " + ex.getMessage(), ex);
			throw new InvalidStateException(getMessage());
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception ex){
					
				}
			}
		}
	}
	
	protected abstract String getJdbcUrl(Map state);

	protected abstract String getJdbcDriverJarUrl(Map state);
	
	protected abstract String getJdbcDriver(Map state);

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPasswordProp() {
		return passwordProp;
	}

	public void setPasswordProp(String passwordProp) {
		this.passwordProp = passwordProp;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getUsernameProp() {
		return usernameProp;
	}

	public void setUsernameProp(String usernameProp) {
		this.usernameProp = usernameProp;
	}

}
