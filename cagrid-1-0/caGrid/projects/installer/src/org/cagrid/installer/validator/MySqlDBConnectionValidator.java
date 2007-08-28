/**
 * 
 */
package org.cagrid.installer.validator;

import java.util.Map;

import org.cagrid.installer.steps.Constants;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class MySqlDBConnectionValidator extends DBConnectionValidator {

	private String hostProp;
	private String portProp;

	/**
	 * @param database
	 * @param usernameProp
	 * @param passwordProp
	 * @param query
	 * @param message
	 */
	public MySqlDBConnectionValidator(String hostProp, String portProp,
			String usernameProp, String passwordProp,
			String query, String message) {
		
		super(usernameProp, passwordProp, query,
				message);
		this.hostProp = hostProp;
		this.portProp = portProp;
	}
	
	protected String getJdbcUrl(Map state) {
		String host = (String)state.get(this.hostProp);
		String port = (String)state.get(this.portProp);		
		return "jdbc:mysql://" + host + ":" + port +  "/mysql";
	}
	
	protected String getJdbcDriverJarUrl(Map state) {
		return System.getProperty("user.dir") + "/" + state.get(Constants.JDBC_DRIVER_PATH);
	}
	
	protected String getJdbcDriver(Map state){
		return "org.gjt.mm.mysql.Driver";
	}

}
