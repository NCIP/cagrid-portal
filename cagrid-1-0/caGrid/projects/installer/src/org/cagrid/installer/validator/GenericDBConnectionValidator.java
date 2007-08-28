/**
 * 
 */
package org.cagrid.installer.validator;

import java.util.Map;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class GenericDBConnectionValidator extends DBConnectionValidator {

	private String jdbcDriverJarUrlProp;

	private String jdbcUrlProp;

	private String jdbcDriverProp;

	/**
	 * @param usernameProp
	 * @param passwordProp
	 * @param query
	 * @param message
	 * @param jdbcDriverJarUrlProp
	 * @param jdbcUrlProp
	 * @param jdbcDriverProp
	 */
	public GenericDBConnectionValidator(String usernameProp,
			String passwordProp, String query, String message,
			String jdbcDriverJarUrlProp, String jdbcUrlProp, String jdbcDriverProp) {
		super(usernameProp, passwordProp, query, message);
		this.jdbcDriverJarUrlProp = jdbcDriverJarUrlProp;
		this.jdbcUrlProp = jdbcUrlProp;
		this.jdbcDriverProp = jdbcDriverProp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.validator.DBConnectionValidator#getJdbcDriverJarUrl(java.util.Map)
	 */
	@Override
	protected String getJdbcDriverJarUrl(Map state) {
		return (String) state.get(this.jdbcDriverJarUrlProp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.validator.DBConnectionValidator#getJdbcUrl(java.util.Map)
	 */
	@Override
	protected String getJdbcUrl(Map state) {
		return (String) state.get(this.jdbcUrlProp);
	}

	@Override
	protected String getJdbcDriver(Map state) {
		return (String)state.get(this.jdbcDriverProp);
	}

}
