/**
 * 
 */
package org.cagrid.installer.steps;


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
	public static final String CAGRID_INSTALLER_PROPERTIES = "cagrid.installer.properties";
	public static final String TOMCAT_ZIP_PATH = "tomcat.zip.path";
	public static final String GLOBUS_ZIP_PATH = "globus.zip.path";
	public static final String TEMP_DIR_PATH = "temp.dir.path";
	public static final String BUILD_FILE_PATH = "build.file.path";
	public static final Object TOMCAT_SECURE_PORT = "tomcat.secure.port";
	public static final String CA_PRESENT = "ca.present";
	public static final String CA_CERT_PATH = "ca.cert.path";
	public static final String CA_KEY_PATH = "ca.key.path";
	public static final String CA_KEY_PWD = "ca.key.pwd";
	public static final String CA_DN = "ca.dn.input";
	public static final String CA_DAYS_VALID = "ca.days.valid";
	public static final String SERVICE_CERT_PATH = "service.cert.path";
	public static final String SERVICE_KEY_PATH = "service.key.path";
	public static final String SERVICE_KEY_PWD = "service.cert.pwd";
	public static final String SERVICE_CERT_DAYS_VALID = "service.cert.days.valid";
	public static final String SERVICE_CERT_PRESENT = "service.cert.present";
	public static final String GRIDCA_BUILD_FILE_PATH = "gridca.build.file.path";

	public static final String ANT_HOME = "ant.home";

	public static final String TOMCAT_HOME = "tomcat.home";

	public static final String GLOBUS_HOME = "globus.home";

	public static final String CAGRID_HOME = "cagrid.home";
	
	public static final String ACTIVEBPEL_HOME = "activebpel.home";

	public static final String ANT_INSTALLED = "ant.installed";

	public static final String TOMCAT_INSTALLED = "tomcat.installed";

	public static final String GLOBUS_INSTALLED = "globus.installed";

	public static final String CAGRID_INSTALLED = "cagrid.installed";
	
	public static final String ACTIVEBPEL_INSTALLED = "activebpel.installed";

	public static final String MESSAGES = "CaGridInstallerMessages";

	public static final String INSTALL_CAGRID = "install.cagrid";

	public static final String INSTALL_SERVICES = "install.services";

	public static final String INSTALL_ANT = "install.ant";
	
	public static final String INSTALL_ACTIVEBPEL = "install.activebpel";

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

	public static final String ACTIVEBPEL_INSTALL_DIR_PATH = "activebpel.install.dir.path";

	public static final String ACTIVEBPEL_DOWNLOAD_URL = "activebpel.download.url";

	public static final String ACTIVEBPEL_TEMP_FILE_NAME = "activebpel.temp.file.name";

	public static final String ACTIVEBPEL_DIR_NAME = "activebpel.dir.name";

	public static final String REDEPLOY_GLOBUS = "redeploy.globus";

	public static final String USE_SECURE_CONTAINER = "use.secure.container";

	public static final String INSTALL_DORIAN = "install.dorian";
	
	public static final String INSTALL_CDS = "install.cds";

	public static final String INSTALL_GTS = "install.gts";

	public static final String INSTALL_AUTHN_SVC = "install.authn.svc";

	public static final String INSTALL_GRID_GROUPER = "install.grid.grouper";

	public static final String INSTALL_GME = "install.gme";

	public static final String INSTALL_EVS = "install.evs";
	
	public static final String INSTALL_TRANSFER = "install.transfer";

	public static final String INSTALL_CADSR = "install.cadsr";

	public static final String INSTALL_FQP = "install.fqp";

	public static final String INSTALL_WORKFLOW = "install.workflow";

	public static final String CA_CERT_PRESENT = "ca.cert.present";

	public static final String SERVICE_HOSTNAME = "service.hostname";

	public static final String INSTALL_INDEX_SVC = "install.index.svc";

	public static final String DOWNLOAD_URL = "download.url";

	public static final long CONNECT_TIMEOUT = 30000;

	public static final String JDBC_DRIVER_PATH = "jdbc.driver.path";

	public static final String JDBC_DRIVER_CLASSNAME = "jdbc.driver.classname";

	public static final String DORIAN_DB_HOST = "dorian.db.host";

	public static final String DORIAN_DB_PORT = "dorian.db.port";

	public static final String DORIAN_DB_USERNAME = "dorian.db.username";

	public static final String DORIAN_DB_PASSWORD = "dorian.db.password";

	public static final String DORIAN_DB_ID = "dorian.db.id";

	public static final String DORIAN_IDP_NAME = "dorian.idp.name";

	public static final String DORIAN_IDP_UID_MIN = "dorian.idp.uid.min";

	public static final String DORIAN_IDP_UID_MAX = "dorian.idp.uid.max";

	public static final String DORIAN_IDP_PWD_MIN = "dorian.idp.pwd.min";

	public static final String DORIAN_IDP_PWD_MAX = "dorian.idp.pwd.max";

	public static final String DORIAN_IDP_REGPOLICY = "dorian.idp.regpolicy";

	public static final String DORIAN_IDP_SAML_AUTORENEW = "dorian.idp.saml.autorenew";

	public static final String DORIAN_IDP_SAML_KEYPWD = "dorian.idp.saml.keypwd";

	public static final String DORIAN_IFS_IDPNAME_MIN = "dorian.ifs.idpname.min";

	public static final String DORIAN_IFS_IDPNAME_MAX = "dorian.ifs.idpname.max";

	public static final String DORIAN_IFS_IDPOLICY = "dorian.ifs.idpolicy";

	public static final String DORIAN_IFS_CREDLIFETIME_YEARS = "dorian.ifs.credlifetime.years";

	public static final String DORIAN_IFS_CREDLIFETIME_MONTHS = "dorian.ifs.credlifetime.months";
	
	public static final String DORIAN_IFS_CREDLIFETIME_DAYS = "dorian.ifs.credlifetime.days";
	
	public static final String DORIAN_IFS_CREDLIFETIME_HOURS = "dorian.ifs.credlifetime.hours";
	
	public static final String DORIAN_IFS_CREDLIFETIME_MINUTES = "dorian.ifs.credlifetime.minutes";
	
	public static final String DORIAN_IFS_CREDLIFETIME_SECONDS = "dorian.ifs.credlifetime.seconds";

	public static final String DORIAN_IFS_HOSTCERT_AUTOAPPROVE = "dorian.ifs.hostcert.autoapprove";
	
	public static final String DORIAN_IFS_PROXYLIFETIME_HOURS = "dorian.ifs.proxylifetime.hours";
	
	public static final String DORIAN_IFS_PROXYLIFETIME_MINUTES = "dorian.ifs.proxylifetime.minutes";
	
	public static final String DORIAN_IFS_PROXYLIFETIME_SECONDS = "dorian.ifs.proxylifetime.seconds";	

	public static final String DORIAN_IFS_GTS_URL = "dorian.ifs.gts.url";

	public static final String DORIAN_CA_PRESENT = "dorian.ca.present";

	public static final String DORIAN_CA_CERT_PATH = "dorian.ca.cert.path";

	public static final String DORIAN_CA_KEY_PATH = "dorian.ca.key.path";

	public static final String DORIAN_CA_KEY_PWD = "dorian.ca.key.pwd";

	public static final String DORIAN_CA_OID = "dorian.ca.oid";

	public static final String DORIAN_CA_USERKEY_SIZE = "dorian.ca.userkey.size";

	public static final String DORIAN_CA_AUTORENEW_YEARS = "dorian.ca.autorenew.years";
	
	public static final String DORIAN_CA_AUTORENEW_MONTHS = "dorian.ca.autorenew.months";
	
	public static final String DORIAN_CA_AUTORENEW_DAYS = "dorian.ca.autorenew.days";
	
	public static final String DORIAN_CA_AUTORENEW_HOURS = "dorian.ca.autorenew.hours";
	
	public static final String DORIAN_CA_AUTORENEW_MINUTES = "dorian.ca.autorenew.minutes";
	
	public static final String DORIAN_CA_AUTORENEW_SECONDS = "dorian.ca.autorenew.seconds";

	public static final String DORIAN_CA_CAKEY_SIZE = "dorian.ca.cakey.size";

	public static final String DORIAN_CA_LIFETIME_YEARS = "dorian.ca.lifetime.years";
	
	public static final String DORIAN_CA_LIFETIME_MONTHS = "dorian.ca.lifetime.months";
	
	public static final String DORIAN_CA_LIFETIME_DAYS = "dorian.ca.lifetime.days";
	
	public static final String DORIAN_CA_LIFETIME_HOURS = "dorian.ca.lifetime.hours";
	
	public static final String DORIAN_CA_LIFETIME_MINUTES = "dorian.ca.lifetime.minutes";
	
	public static final String DORIAN_CA_LIFETIME_SECONDS = "dorian.ca.lifetime.seconds";

	public static final String DORIAN_CA_SUBJECT = "dorian.ca.subject";

	public static final String CONTAINER_TYPE = "container.type";

	public static final String DORIAN_USE_GEN_CA = "dorian.use.gen.ca";

	public static final String AVAILABLE_TARGET_GRIDS = "available.target.grids";

	public static final String TARGET_GRID = "target.grid";

	public static final String SERVICE_DEST_DIR = "service.dest.dir";

	public static final String GTS_ADMIN_IDENT = "gts.admin.ident";
	
	public static final String CDS_ADMIN_IDENT = "cds.admin.ident";
	
	public static final String CDS_MAX_DELEGATION_PATH_LENGTH = "gaards.cds.max.delegation.path.length";
	
	public static final String CDS_DB_KEY_MANAGER_PASSWORD = "gaards.cds.dbkeymanager.key.encyption.password";

	public static final String TOMCAT_SHUTDOWN_PORT = "tomcat.shutdown.port";

	public static final String TOMCAT_HTTP_PORT = "tomcat.http.port";

	public static final String TOMCAT_HTTPS_PORT = "tomcat.https.port";

	public static final String TOMCAT_OLD_HTTP_PORT = "tomcat.old.http.port";

	public static final String TOMCAT_OLD_HTTPS_PORT = "tomcat.old.https.port";

	public static final String AUTHN_SVC_CRED_PROVIDER_TYPE = "authn.svc.cred.provider.type";

	public static final String AUTHN_SVC_CRED_PROVIDER_TYPE_RDBMS = "authn.svc.cred.provider.type.rdbms";

	public static final String AUTHN_SVC_CRED_PROVIDER_TYPE_LDAP = "authn.svc.cred.provider.type.ldap";
	
	public static final String AUTHN_SVC_USE_SVC_CREDS = "authn.svc.use.svc.creds";

	public static final String AUTHN_SVC_CSM_CTX = "authn.svc.csm.ctx";

	public static final String AUTHN_SVC_RDBMS_URL = "authn.svc.rdbms.url";

	public static final String AUTHN_SVC_RDBMS_DRIVER_JAR = "authn.svc.rdbms.driver.jar";

	public static final String AUTHN_SVC_RDBMS_DRIVER = "authn.svc.rdbms.driver";

	public static final String AUTHN_SVC_RDBMS_USERNAME = "authn.svc.rdbms.username";

	public static final String AUTHN_SVC_RDBMS_PASSWORD = "authn.svc.rdbms.password";

	public static final String AUTHN_SVC_RDBMS_TABLE_NAME = "authn.svc.rdbms.table.name";

	public static final String AUTHN_SVC_RDBMS_LOGIN_ID_COLUMN = "authn.svc.rdbms.login.id.column";

	public static final String AUTHN_SVC_RDBMS_PASSWORD_COLUMN = "authn.svc.rdbms.password.column";

	public static final String AUTHN_SVC_RDBMS_FIRST_NAME_COLUMN = "authn.svc.rdbms.first.name.column";

	public static final String AUTHN_SVC_RDBMS_LAST_NAME_COLUMN = "authn.svc.rdbms.last.name.column";

	public static final String AUTHN_SVC_RDBMS_EMAIL_ID_COLUMN = "authn.svc.rdbms.email.id.column";

	public static final String AUTHN_SVC_LDAP_HOSTNAME = "authn.svc.ldap.hostname";

	public static final String AUTHN_SVC_LDAP_SEARCH_BASE = "authn.svc.ldap.search.base";

	public static final String AUTHN_SVC_LDAP_LOGIN_ID_ATTRIBUTE = "authn.svc.ldap.login.id.attribute";

	public static final String AUTHN_SVC_LDAP_FIRST_NAME_ATTRIBUTE = "authn.svc.ldap.first.name.attribute";

	public static final String AUTHN_SVC_LDAP_LAST_NAME_ATTRIBUTE = "authn.svc.ldap.last.name.attribute";

	public static final String AUTHN_SVC_RDBMS_ENCRYPTION_ENABLED = "authn.svc.rdbms.encryption.enabled";

	public static final String AUTHN_SVC_OVERWRITE_JAAS = "authn.svc.overwrite.jaas";

	public static final String AUTHN_SVC_OVERWRITE_JAAS_YES = "authn.svc.overwrite.jaas.yes";

	public static final String AUTHN_SVC_OVERWRITE_JAAS_NO = "authn.svc.overwrite.jaas.no";

	public static final String GRID_GROUPER_ADMIN_IDENT = "grid.grouper.admin.ident";

	public static final String GRID_GROUPER_DB_URL = "grid.grouper.db.url";

	public static final String GRID_GROUPER_DB_USERNAME = "grid.grouper.db.username";

	public static final String GRID_GROUPER_DB_PASSWORD = "grid.grouper.db.password";

	public static final String SYNC_GTS_GTS_URI = "sync.gts.gts.uri";

	public static final String SYNC_GTS_EXPIRATION_HOURS = "sync.gts.expiration.hours";

	public static final String SYNC_GTS_EXPIRATION_MINUTES = "sync.gts.expiration.minutes";

	public static final String SYNC_GTS_EXPIRATION_SECONDS = "sync.gts.expiration.seconds";

	public static final String SYNC_GTS_FILTER_LIFETIME = "sync.gts.filter.lifetime";

	public static final String SYNC_GTS_AUTH_FILTER = "sync.gts.auth.filter";

	public static final String SYNC_GTS_PERFORM_AUTHZ = "sync.gts.perform.authz";

	public static final String SYNC_GTS_GTS_IDENT = "sync.gts.gts.ident";

	public static final String SYNC_GTS_DELETE_INVALID = "sync.gts.delete.invalid";

	public static final String SYNC_GTS_NEXT_SYNC = "sync.gts.next.sync";

	public static final String INSTALL_SYNC_GTS = "install.sync.gts";

	public static final String DORIAN_CA_TYPE = "dorian.ca.type";

	public static final String DORIAN_CA_TYPE_DBCA = "DBCA";

	public static final String DORIAN_CA_TYPE_ERACOM = "Eracom";

	public static final String DORIAN_CA_TYPE_ERACOM_HYBRID = "EracomHybrid";

	public static final String DORIAN_CA_ERACOM_SLOT = "dorian.ca.eracom.slot";

	public static final String DORIAN_HOST_CRED_DIR = "dorian.host.cred.dir";

	public static final String SYNC_GTS_PERFORM_FIRST_SYNC = "sync.gts.perform.first.sync";

	//TODO: This property is no longer set. It needs to be removed from all conditions.
	public static final String RECONFIGURE_GLOBUS = "reconfigure.globus";

	public static final String RECONFIGURE_CAGRID = "reconfigure.cagrid";

	public static final String INSTALL_MY_SERVICE = "install.my.service";

	public static final String MY_SERVICE_DIR = "my.service.dir";

	public static final String REPLACE_DEFAULT_GTS_CA = "replace.default.gts.ca";

	public static final String REPLACEMENT_GTS_CA_CERT_PATH = "replacement.gts.ca.cert.path";

	public static final String AUTHN_SVC_LDAP_EMAIL_ID_ATTRIBUTE = "authn.svc.ldap.email.id.attribute";

	public static final String CONFIGURE_CONTAINER = "configure.container";

	public static final String FALSE = "false";

	public static final String TRUE = "true";

	public static final String INSTALL_PORTAL = "install.portal";

	public static final String INSTALL_BROWSER = "install.browser";

	public static final String PORTAL_META_AGG_FREQ = "portal.meta.agg.freq";

	public static final String PORTAL_GOOGLE_MAP_KEY = "portal.google.map.key";

	public static final String PORTAL_GEOCODER_APP_ID = "portal.geocoder.app.id";

	public static final String PORTAL_INDEX_SVC_URLS = "portal.index.svc.urls";

	public static final String PORTAL_CONTEXT_NAME = "portal.context.name";

	public static final String CAGRID_VERSION = "1.1";

	public static final String BROWSER_HOME = "browser.home";

	public static final String BROWSER_INSTALLED = "browser.installed";

	public static final String BROWSER_INSTALL_DIR_PATH = "browser.install.dir.path";

	public static final String BROWSER_DOWNLOAD_URL = "browser.download.url";

	public static final String BROWSER_TEMP_FILE_NAME = "browser.temp.file.name";

	public static final String BROWSER_DIR_NAME = "browser.dir.name";

	public static final String BROWSER_INDEX_SERVICE_URLS = "browser.index.svc.urls";

	public static final String BROWSER_EVS_SVC_URL = "browser.evs.svc.url";

	public static final String BROWSER_IDP_URL1 = "browser.idp.url1";

	public static final String BROWSER_IDP_URL2 = "browser.idp.url2";

	public static final String BROWSER_IFS_URL = "browser.ifs.url";

	public static final String BROWSER_CONTEXT_NAME = "browser.context.name";

	public static final String BPEL_ADMIN_ROLE = "bpel.admin.role";

	public static final String BPEL_ADMIN_USERNAME = "bpel.admin.username";

	public static final String BPEL_ADMIN_PASSWORD = "bpel.admin.password";

	public static final String AUTH_SVC_SAML_PROVIDER_CERT_PATH = "authn.svc.saml.provider.cert.path";

	public static final String AUTH_SVC_SAML_PROVIDER_KEY_PATH = "authn.svc.saml.provider.key.path";

	public static final String AUTH_SVC_SAML_PROVIDER_KEY_PWD = "authn.svc.saml.provider.key.pwd";

	public static final Object JAVA_VERSION_PATTERN = "java.version.pattern";

	public static final String DORIAN_IDP_PWD_LOCKOUT_HOURS = "dorian.idp.pwd.lockout.hours";

	public static final String DORIAN_IDP_PWD_LOCKOUT_MINUTES = "dorian.idp.pwd.lockout.minutes";

	public static final String DORIAN_IDP_PWD_LOCKOUT_SECONDS = "dorian.idp.pwd.lockout.seconds";

	public static final String DORIAN_IDP_MAX_CONSEC_INVALID_LOGINS = "dorian.idp.max.consec.invalid.logins";

	public static final String DORIAN_IDP_MAX_TOTAL_INVALID_LOGINS = "dorian.idp.max.total.invalid.logins";

	


}
