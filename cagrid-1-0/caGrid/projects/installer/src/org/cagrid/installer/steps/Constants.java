/**
 * 
 */
package org.cagrid.installer.steps;

import java.util.Properties;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class Constants {
	
	/**
	 * Is the network available?
	 */
	public static final String NETWORK_AVAILABLE = "network.available";
	
	/**
	 * The name of the file that holds all installer properties.
	 */
	public static final String CAGRID_INSTALLER_PROPERTIES = ".cagrid.installer";
	public static final String TOMCAT_ZIP_PATH = "tomcat.zip.path";
	public static final String GLOBUS_ZIP_PATH = "globus.zip.path";
	public static final String TEMP_DIR_PATH = "temp.dir.path";
	public static final String BUILD_FILE_PATH = "build.file.path";
	public static final String TOMCAT_DIR_PATH = "tomcat.dir.path";
	public static final String GLOBUS_DIR_PATH = "globus.dir.path";
	public static final Object TOMCAT_SECURE_PORT = "tomcat.secure.port";
	public static final Object TOMCAT_KEY_PATH = "tomcat.key.path";
	public static final Object TOMCAT_CERT_PATH = "tomcat.cert.path";
	public static final String CA_PRESENT = "ca.present";
	public static final String CA_CERT_PATH = "ca.cert.path";
	public static final String CA_KEY_PATH = "ca.key.path";
	public static final String CA_KEY_PWD = "ca.key.pwd";
	public static final String CA_DN = "ca.dn.input";
	public static final String CA_DAYS_VALID = "ca.days.valid";
	public static final String SERVICE_CERT_PATH = "service.cert.path";
	public static final String SERVICE_KEY_PATH = "service.key.path";
	public static final String SERVICE_KEY_PWD = "service.cert.pwd";
	public static final String SERVICE_CERT_PRESENT = "service.cert.present";
	public static final String GRIDCA_BUILD_FILE_PATH = "gridca.build.file.path";

	public static final String ANT_HOME = "ant.home";

	public static final String TOMCAT_HOME = "tomcat.home";

	public static final String GLOBUS_HOME = "globus.home";

	public static final String CAGRID_HOME = "cagrid.home";

	public static final String ANT_INSTALLED = "ant.installed";

	public static final String TOMCAT_INSTALLED = "tomcat.installed";

	public static final String GLOBUS_INSTALLED = "globus.installed";

	public static final String CAGRID_INSTALLED = "cagrid.installed";

	public static final String MESSAGES = "CaGridInstallerMessages";

	public static final String INSTALL_CAGRID = "install.cagrid";

	public static final String INSTALL_SERVICES = "install.services";

	public static final String INSTALL_ANT = "install.ant";

	public static final String ANT_DOWNLOAD_URL = "ant.download.url";

	public static final String ANT_TEMP_FILE_NAME = "ant.temp.file.name";

	public static final String ANT_INSTALL_DIR_PATH = "ant.install.dir.path";

	public static final String ANT_DIR_NAME = "ant.dir.name";

	public static final String INSTALL_TOMCAT = "install.tomcat";

	public static final String TOMCAT_INSTALL_DIR_PATH = "tomcat.install.dir.path";

	public static final String TOMCAT_DOWNLOAD_URL = "tomcat.download.url";

	public static final String TOMCAT_TEMP_FILE_NAME = "tomcat.temp.file.name";

	public static final String TOMCAT_DIR_NAME = "tomcat.dir.name";

	public static final String INSTALL_GLOBUS = "install.globus";

	public static final String GLOBUS_INSTALL_DIR_PATH = "globus.install.dir.path";

	public static final String GLOBUS_DOWNLOAD_URL = "globus.download.url";

	public static final String GLOBUS_TEMP_FILE_NAME = "globus.temp.file.name";

	public static final String GLOBUS_DIR_NAME = "globus.dir.name";

	public static final String CAGRID_INSTALL_DIR_PATH = "cagrid.install.dir.path";

	public static final String CAGRID_DOWNLOAD_URL = "cagrid.download.url";

	public static final String CAGRID_TEMP_FILE_NAME = "cagrid.temp.file.name";

	public static final String CAGRID_DIR_NAME = "cagrid.dir.name";
	
}
