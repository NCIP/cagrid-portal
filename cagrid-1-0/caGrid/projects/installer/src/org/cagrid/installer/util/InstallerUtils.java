/**
 * 
 */
package org.cagrid.installer.util;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.options.FilePropertyConfigurationOption;
import org.cagrid.installer.steps.options.ListPropertyConfigurationOption;
import org.cagrid.installer.steps.options.PasswordPropertyConfigurationOption;
import org.cagrid.installer.steps.options.TextPropertyConfigurationOption;
import org.cagrid.installer.validator.KeyAccessValidator;
import org.cagrid.installer.validator.MySqlDBConnectionValidator;
import org.cagrid.installer.validator.PathExistsValidator;
import org.pietschy.wizard.InvalidStateException;
import org.w3c.dom.Node;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class InstallerUtils {

	private static final Log logger = LogFactory.getLog(InstallerUtils.class);

	public InstallerUtils() {

	}

	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().indexOf("windows") != -1;
	}

	public static void showError(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	
	public static void addToClassPath(String s) throws IOException {
		File f = new File(s);
		addToClassPath(f);
	}

	public static void addToClassPath(File f) throws IOException {
		addToClassPath(f.toURL());
	}

	public static void addToClassPath(URL u) throws IOException {

		URLClassLoader sysloader = (URLClassLoader) ClassLoader
				.getSystemClassLoader();
		Class sysclass = URLClassLoader.class;

		try {
			Method method = sysclass.getDeclaredMethod("addURL",
					new Class[] { URL.class });
			method.setAccessible(true);
			method.invoke(sysloader, new Object[] { u });
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IOException(
					"Error, could not add URL to system classloader");
		}

	}

	public static boolean checkGenerateCA(CaGridInstallerModel model) {
		return model.isTrue(Constants.USE_SECURE_CONTAINER)
				&& !model.isTrue(Constants.SERVICE_CERT_PRESENT)
				&& !model.isTrue(Constants.CA_CERT_PRESENT);
	}

	public static void copyCACertToTrustStore(String certPath)
			throws IOException {
		copyCACertToTrustStore(certPath, "CA.0");
	}

	public static void copyCACertToTrustStore(String certPath, String caFileName)
			throws IOException {

		File trustDir = new File(System.getProperty("user.home")
				+ "/.globus/certificates");
		copyFile(certPath, trustDir.getAbsolutePath() + "/" + caFileName);

	}

	public static void copyFile(String from, String to) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(from));
		File toFile = new File(to);
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		BufferedWriter out = new BufferedWriter(new FileWriter(toFile));
		String line = null;
		while ((line = in.readLine()) != null) {
			out.write(line + "\n");
		}
		in.close();
		out.flush();
		out.close();
	}

	public static String getScriptsBuildFilePath() {
		return new File("scripts/build.xml").getAbsolutePath();
	}

	public static String toString(Node node) throws Exception {
		StringWriter w = new StringWriter();
		Source s = new DOMSource(node);
		Result r = new StreamResult(w);
		Transformer t = TransformerFactory.newInstance().newTransformer();
		// t.setOutputProperty("omit-xml-declaration", "yes");
		t.setOutputProperty("indent", "yes");
		t.transform(s, r);
		return w.getBuffer().toString();
	}

	public static String getInstallerTempDir() {
		return getInstallerDir() + "/tmp";

	}

	public static String getInstallerDir() {
		return System.getProperty("user.home") + "/.cagrid/installer";
	}

	public static boolean isEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	public static void setUpCellRenderer(JTable table) {
		DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				Component renderer = super.getTableCellRendererComponent(table,
						value, isSelected, hasFocus, row, column);
				setBorder(BorderFactory.createEtchedBorder());
				return renderer;
			}
		};
		int colCount = table.getColumnCount();
		for (int i = 0; i < colCount; i++) {
			TableColumn col = table.getColumnModel().getColumn(i);
			col.setCellRenderer(r);
		}
	}

	public static GridBagConstraints getGridBagConstraints(int x, int y) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.gridy = y;
		return gbc;
	}

	public static String getDbNameFromJdbcUrl(String jdbcUrl) {
		return jdbcUrl.substring(jdbcUrl.lastIndexOf("/") + 1);
	}

	public static String getJdbcBaseFromJdbcUrl(String jdbcUrl) {
		return jdbcUrl.substring(0, jdbcUrl.lastIndexOf("/"));
	}

	public static boolean checkCaGridVersion(String home) {
		boolean isCorrectVersion = false;
		try {
			File propsFile = new File(home
					+ "/share/resources/cagrid.properties");
			if (propsFile.exists()) {

				Properties props = new Properties();
				props.load(new FileInputStream(propsFile));

				if (Constants.CAGRID_VERSION.equals(props
						.getProperty("cagrid.master.project.version"))) {
					isCorrectVersion = true;
				}
			}
		} catch (Exception ex) {
			logger.debug("Error checking caGrid version: " + ex.getMessage(),
					ex);
		}
		return isCorrectVersion;
	}



	public static String getJavaVersion() {

		String version = null;
		
		String java = "java";
		if (isWindows()) {
			java += ".exe";
		}
		try {
			Process p = Runtime.getRuntime().exec(
					new String[] { getJavaHomePath() + "/bin/" + java, "-version" }, new String[0]);

			StringBuffer stdout = new StringBuffer();
			new IOThread(p.getInputStream(), System.out, stdout).start();
			
			StringBuffer stderr = new StringBuffer();
			new IOThread(p.getErrorStream(), System.err, stderr).start();
			
			int code = p.waitFor();
			
			logger.info("CODE: " + code);
			logger.info("STDOUT: " + stdout);
			logger.info("STDERR: " + stderr);
			
			version = stdout.toString();
			if(InstallerUtils.isEmpty(version)){
				version = stderr.toString();
			}
			try{
				version = version.substring(version.indexOf("\"") + 1, version.lastIndexOf("\""));
			}catch(Exception ex){
				logger.warn("Couldn't parse out version from '" + version + "'");
			}
			
		} catch (Exception ex) {
			throw new RuntimeException("Error checking java version: "
					+ ex.getMessage(), ex);
		}

		return version;
	}

	public static String getJavaHomePath() {
		String javaHome = System.getenv("JAVA_HOME");
		if (isEmpty(javaHome)) {
			javaHome = System.getProperty("java.home");
		}
		return javaHome;
	}
	
	public static String getJavacVersion() {

		String version = "version";


		String javacCmd = "javac";
		if (isWindows()) {
			javacCmd += ".exe";
		}

		try {
			Process p = Runtime.getRuntime().exec(
					new String[] { javacCmd, "-help",
							"-version" }, new String[0]);

			StringBuffer stdout = new StringBuffer();
			new IOThread(p.getInputStream(), System.out, stdout).start();

			StringBuffer stderr = new StringBuffer();
			new IOThread(p.getErrorStream(), System.err, stderr).start();

			int code = p.waitFor();

			logger.info("CODE: " + code);
			logger.info("STDOUT: " + stdout);
			logger.info("STDERR: " + stderr);

			String out = stdout.toString();
			if (InstallerUtils.isEmpty(out)) {
				out = stderr.toString();
			}
			try {
				int idx = out.lastIndexOf("javac");
				version = out.substring(idx + "javac".length() + 1).trim();
				logger.info("Found javac version '" + version + "'");
			} catch (Exception ex) {
				logger.warn("Couldn't parse out javac version from '" + version
						+ "'");
			}

		} catch (Exception ex) {
			logger.warn("Error checking javac version: "
					+ ex.getMessage(), ex);
		}

		return version;
	}

	public static boolean checkTomcatVersion(String home) {
		boolean correctVersion = false;
		try {
			String javaHome = getJavaHomePath();
			String[] envp = new String[] { "JAVA_HOME=" + javaHome,
					"CATALINA_HOME=" + home };

			String[] cmd = null;
			if (InstallerUtils.isWindows()) {
				cmd = new String[] { "cmd.exe", "/c", home + "/bin/version.bat" };
			} else {
				cmd = new String[] { "sh", home + "/bin/version.sh" };
			}
			Process p = Runtime.getRuntime().exec(cmd, envp);
			StringBuffer stdout = new StringBuffer();

			new IOThread(p.getInputStream(), System.out, stdout).start();
			StringBuffer stderr = new StringBuffer();

			new IOThread(p.getErrorStream(), System.err, stderr).start();
			int code = p.waitFor();

			correctVersion = stdout.toString().indexOf("Apache Tomcat/5.0.28") != -1;
			if (!correctVersion) {

				logger.warn("The Tomcat version utility indicates "
						+ "that the correct tomcat version is not "
						+ "installed. Here is the output from that tool: \n"
						+ stdout);

				logger.warn("Exit code: " + code);
				logger.warn("STDERR:\n" + stderr);

			}
		} catch (Exception ex) {
			logger
					.warn("Error checking Tomcat version: " + ex.getMessage(),
							ex);
		}
		return correctVersion;

	}

	public static boolean checkGlobusVersion(String home) {
		return home.indexOf("4.0.3") != -1;
	}

	public static boolean checkAntVersion(String home) {
		boolean correctVersion = false;
		try {
			String[] envp = new String[] { "JAVA_HOME="
					+ getJavaHomePath(), "ANT_HOME=" + home };

			String[] cmd = null;
			if (InstallerUtils.isWindows()) {
				cmd = new String[] { "cmd.exe", "/c", home + "/bin/ant.bat",
						"-version" };
			} else {
				cmd = new String[] { "sh", home + "/bin/ant", "-version" };
			}

			Process p = Runtime.getRuntime().exec(cmd, envp);
			StringBuffer stdout = new StringBuffer();
			new IOThread(p.getInputStream(), System.out, stdout).start();
			p.waitFor();
			correctVersion = stdout.toString().indexOf(
					"Apache Ant version 1.6.5") != -1;
		} catch (Exception ex) {
			logger.warn("Error checking Ant version: " + ex.getMessage(), ex);
		}
		return correctVersion;
	}

	public static boolean checkActiveBPELVersion(String home) {
		// TODO: improve this check
		return new File(home).exists();
	}

	public static boolean checkBrowserVersion(String homeDir) {
		File coreJar = new File(homeDir + "/ext/lib/caGrid-"
				+ Constants.CAGRID_VERSION + "-core.jar");
		return coreJar.exists();
	}

	public static void addDBConfigPropertyOptions(CaGridInstallerModel model,
			PropertyConfigurationStep step, String propPrefix,
			String dbIdDefault) {
		step.getOptions().add(
				new TextPropertyConfigurationOption(propPrefix + "db.host",
						model.getMessage(propPrefix + "db.host"), model
								.getProperty(propPrefix + "db.host",
										"localhost"), true));
		step.getOptions().add(
				new TextPropertyConfigurationOption(propPrefix + "db.port",
						model.getMessage(propPrefix + "db.port"), model
								.getProperty(propPrefix + "db.port", "3306"),
						true));
		step.getOptions().add(
				new TextPropertyConfigurationOption(propPrefix + "db.id", model
						.getMessage(propPrefix + "db.id"), model.getProperty(
						propPrefix + "db.id", dbIdDefault), true));
		step.getOptions().add(
				new TextPropertyConfigurationOption(propPrefix + "db.username",
						model.getMessage(propPrefix + "db.username"),
						model.getProperty(propPrefix + "db.username", "root"),
						true));
		step.getOptions().add(
				new PasswordPropertyConfigurationOption(propPrefix
						+ "db.password", model.getMessage(propPrefix
						+ "db.password"), model.getProperty(propPrefix
						+ "db.password"), false));
		step.getValidators().add(
				new MySqlDBConnectionValidator(propPrefix + "db.host",
						propPrefix + "db.port", propPrefix + "db.username",
						propPrefix + "db.password", "select 1", model
								.getMessage("db.validation.failed")));
	}

	public static void addCommonDorianCAConfigFields(
			CaGridInstallerModel model, PropertyConfigurationStep step) {

		step.getOptions().add(
				new PasswordPropertyConfigurationOption(
						Constants.DORIAN_CA_KEY_PWD, model
								.getMessage("dorian.ca.cert.info.key.pwd"),
						model.getProperty(Constants.DORIAN_CA_KEY_PWD), true));
		step.getOptions().add(
				new TextPropertyConfigurationOption(Constants.DORIAN_CA_OID,
						model.getMessage("dorian.ca.cert.info.oid"), model
								.getProperty(Constants.DORIAN_CA_OID), false));
		step.getOptions().add(
				new ListPropertyConfigurationOption(
						Constants.DORIAN_CA_USERKEY_SIZE,
						model.getMessage("dorian.ca.cert.info.userkey.size"),
						new String[] { String.valueOf(1024),
								String.valueOf(2048), String.valueOf(512) },
						true));
		step
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.DORIAN_CA_AUTORENEW_YEARS,
								model
										.getMessage("dorian.ca.cert.info.autorenew.years"),
								model.getProperty(
										Constants.DORIAN_CA_AUTORENEW_YEARS,
										"1"), true));
		step
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.DORIAN_CA_AUTORENEW_MONTHS,
								model
										.getMessage("dorian.ca.cert.info.autorenew.months"),
								model.getProperty(
										Constants.DORIAN_CA_AUTORENEW_MONTHS,
										"0"), true));
		step.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_CA_AUTORENEW_DAYS,
						model.getMessage("dorian.ca.cert.info.autorenew.days"),
						model.getProperty(Constants.DORIAN_CA_AUTORENEW_DAYS,
								"0"), true));
		step
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.DORIAN_CA_AUTORENEW_HOURS,
								model
										.getMessage("dorian.ca.cert.info.autorenew.hours"),
								model.getProperty(
										Constants.DORIAN_CA_AUTORENEW_HOURS,
										"0"), true));
		step
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.DORIAN_CA_AUTORENEW_MINUTES,
								model
										.getMessage("dorian.ca.cert.info.autorenew.minutes"),
								model.getProperty(
										Constants.DORIAN_CA_AUTORENEW_MINUTES,
										"0"), true));
		step
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.DORIAN_CA_AUTORENEW_SECONDS,
								model
										.getMessage("dorian.ca.cert.info.autorenew.seconds"),
								model.getProperty(
										Constants.DORIAN_CA_AUTORENEW_SECONDS,
										"0"), true));

	}

	public static void addCommonCACertFields(CaGridInstallerModel model,
			PropertyConfigurationStep step, String caCertPathProp,
			String caKeyPathProp, String caKeyPwdProp, boolean validate) {

		FilePropertyConfigurationOption caCertPathOption = new FilePropertyConfigurationOption(
				caCertPathProp, model.getMessage("ca.cert.info.cert.path"),
				model.getProperty(caCertPathProp, InstallerUtils
						.getInstallerDir()
						+ "/certs/ca.cert"), true);
		caCertPathOption.setDirectoriesOnly(false);
		caCertPathOption.setBrowseLabel(model.getMessage("browse"));
		step.getOptions().add(caCertPathOption);

		FilePropertyConfigurationOption caKeyPathOption = new FilePropertyConfigurationOption(
				caKeyPathProp, model.getMessage("ca.cert.info.key.path"), model
						.getProperty(caKeyPathProp, InstallerUtils
								.getInstallerDir()
								+ "/certs/ca.key"), true);
		caKeyPathOption.setDirectoriesOnly(false);
		caKeyPathOption.setBrowseLabel(model.getMessage("browse"));
		step.getOptions().add(caKeyPathOption);

		step.getOptions().add(
				new PasswordPropertyConfigurationOption(caKeyPwdProp, model
						.getMessage("ca.cert.info.key.pwd"), model
						.getProperty(caKeyPwdProp), true));

		if (validate) {
			step.getValidators().add(
					new PathExistsValidator(caCertPathProp, model
							.getMessage("error.cert.file.not.found")));
			step.getValidators().add(
					new PathExistsValidator(caKeyPathProp, model
							.getMessage("error.key.file.not.found")));
			step.getValidators().add(
					new KeyAccessValidator(caKeyPathProp, caKeyPwdProp, model
							.getMessage("error.key.no.access")));
		}
	}

	public static void addCommonNewCACertFields(CaGridInstallerModel model,
			PropertyConfigurationStep step, String caCertPathProp,
			String caKeyPathProp, String caKeyPwdProp, String caDnProp,
			String caDaysValidProp) {

		addCommonCACertFields(model, step, caCertPathProp, caKeyPathProp,
				caKeyPwdProp, false);

		step.getOptions().add(
				new TextPropertyConfigurationOption(caDnProp, model
						.getMessage("ca.cert.info.dn"), model.getProperty(
						caDnProp, "O=org,OU=unit,CN=name"), true));
		step.getOptions().add(
				new TextPropertyConfigurationOption(caDaysValidProp, model
						.getMessage("ca.cert.info.days.valid"), model
						.getProperty(caDaysValidProp, "1000"), true));
	}

	public static void addCommonCertFields(CaGridInstallerModel model,
			PropertyConfigurationStep step, String certPathProp,
			String keyPathProp, String keyPwdProp) {
		FilePropertyConfigurationOption escpOption = new FilePropertyConfigurationOption(
				certPathProp, model.getMessage("service.cert.info.cert.path"),
				model
						.getProperty(certPathProp, System
								.getProperty("user.home")), true);
		escpOption.setDirectoriesOnly(false);
		escpOption.setBrowseLabel(model.getMessage("browse"));
		step.getOptions().add(escpOption);
		FilePropertyConfigurationOption eskpOption = new FilePropertyConfigurationOption(
				keyPathProp,
				model.getMessage("service.cert.info.key.path"),
				model.getProperty(keyPathProp, System.getProperty("user.home")),
				true);
		eskpOption.setDirectoriesOnly(false);
		eskpOption.setBrowseLabel(model.getMessage("browse"));
		step.getOptions().add(eskpOption);
		if (keyPwdProp != null) {
			step.getOptions().add(
					new PasswordPropertyConfigurationOption(keyPwdProp, model
							.getMessage("service.cert.info.key.pwd"), model
							.getProperty(keyPwdProp, ""), false));
		}
		step.getValidators().add(
				new PathExistsValidator(certPathProp, model
						.getMessage("error.cert.file.not.found")));
		step.getValidators().add(
				new PathExistsValidator(keyPathProp, model
						.getMessage("error.key.file.not.found")));
		step.getValidators().add(
				new KeyAccessValidator(keyPathProp, keyPwdProp, model
						.getMessage("error.key.no.access")));
	}

	public static String trim(String s) {
		String trimmed = s;
		if (trimmed != null) {
			trimmed = trimmed.trim();
		}
		return trimmed;
	}
	
	public static Connection getDatabaseConnection(String driver, String dbUrl, String username, String password) throws InvalidStateException {
		
		if(driver != null){
			try{
				Class.forName(driver);
			}catch(Exception ex){
				throw new InvalidStateException("Error loading driver '" + driver + "': " + ex.getMessage(), ex);
			}
		}
		
		ConnectThread t = new ConnectThread(dbUrl, username, password);
		t.start();
		try{
			t.join(Constants.CONNECT_TIMEOUT);
		}catch(InterruptedException ex){
			logger.warn("Connection interrupted: " + ex.getMessage(), ex);
		}
		
		if(t.getEx() != null){
			throw new InvalidStateException("Error connecting to " + dbUrl + ": " + t.getEx().getMessage(), t.getEx());
		}
		if(!t.isFinished()){
			throw new InvalidStateException("Connection to " + dbUrl + " timed out.");
		}
		Connection conn = t.getConnection();
		if(conn == null){
			throw new InvalidStateException("Couldn't get connection to " + dbUrl + ". Connection is null.");
		}
		return conn;
	}
	
	private static class ConnectThread extends Thread {
		
		private boolean finished;
		private Connection conn;
		private String dbUrl;
		private String username;
		private String password;
		private Exception ex;
		
		ConnectThread(String dbUrl, String username, String password){
			this.dbUrl = dbUrl;
			this.username = username;
			this.password = password;
		}
		
		public void run(){
			try{
				this.conn = DriverManager.getConnection(this.dbUrl, this.username, this.password);
				this.finished = true;
			}catch(Exception ex){
				this.ex = ex;
			}
		}

		public Connection getConnection() {
			return conn;
		}

		public Exception getEx() {
			return ex;
		}


		public boolean isFinished() {
			return finished;
		}

		
	}

}
