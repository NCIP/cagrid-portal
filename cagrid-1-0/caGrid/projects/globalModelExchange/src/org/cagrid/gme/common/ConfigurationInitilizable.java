package org.cagrid.gme.common;

import org.apache.commons.configuration.Configuration;
import org.cagrid.gme.common.exceptions.InitializationException;


/**
 * @author oster
 */
public interface ConfigurationInitilizable {

	/**
	 * Sets the Configuration which can be used to initialize this.
	 * 
	 * @param configuration
	 *            the Configuration
	 */
	public void setConfiguration(Configuration configuration) throws InitializationException;

}