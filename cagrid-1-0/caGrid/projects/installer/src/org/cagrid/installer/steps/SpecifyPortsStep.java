/**
 * 
 */
package org.cagrid.installer.steps;

import java.io.FileInputStream;
import java.io.FileWriter;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.util.InstallerUtils;
import org.pietschy.wizard.InvalidStateException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SpecifyPortsStep extends PropertyConfigurationStep {

	private static final Log logger = LogFactory
			.getLog(SpecifyPortsStep.class);

	private Document doc;

	private Element httpEl;

	private Element httpsEl;

	private Element serverEl;

	/**
	 * 
	 */
	public SpecifyPortsStep() {

	}

	/**
	 * @param name
	 * @param description
	 */
	public SpecifyPortsStep(String name, String description) {
		super(name, description);
	}

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public SpecifyPortsStep(String name, String description, Icon icon) {
		super(name, description, icon);
	}

	public void prepare() {
		JTextField httpsPortField = (JTextField) getOption(Constants.HTTPS_PORT);
		JLabel httpsPortLabel = getLabel(Constants.HTTPS_PORT);

		JTextField httpPortField = (JTextField) getOption(Constants.HTTP_PORT);
		JLabel httpPortLabel = getLabel(Constants.HTTP_PORT);
		if (!this.model.isTrue(Constants.USE_SECURE_CONTAINER)) {
			httpPortField.setVisible(true);
			httpPortLabel.setVisible(true);
			httpsPortField.setVisible(false);
			httpsPortLabel.setVisible(false);
		} else {
			httpPortField.setVisible(false);
			httpPortLabel.setVisible(false);
			httpsPortField.setVisible(true);
			httpsPortLabel.setVisible(true);
		}

		// Pull the tomcat ports from server.xml
		String httpPort = httpPortField.getText();
		String httpsPort = httpsPortField.getText();
		String shutdownPort = null;
		String serverConfigPath = getServerConfigPath();

		try {
			this.doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(
							new FileInputStream(serverConfigPath));
			XPathFactory xpFact = XPathFactory.newInstance();
			this.serverEl = (Element) xpFact.newXPath().compile("/Server")
					.evaluate(doc, XPathConstants.NODE);
			shutdownPort = serverEl.getAttribute("port");

			this.httpEl = getConnectorEl(doc, false);
			this.httpsEl = getConnectorEl(doc, true);

			if (httpEl != null) {
				httpPort = httpEl.getAttribute("port");
				logger.info("Found HTTP port: " + httpPort);
			} else {
				logger.info("Did not find HTTP port in " + serverConfigPath);
			}
			if (httpsEl != null) {
				httpsPort = httpsEl.getAttribute("port");
				logger.info("Found HTTPS port: " + httpsPort);
			} else {
				logger.info("Did not HTTPS port: " + serverConfigPath);
			}

		} catch (Exception ex) {
			logger.warn("Error reading " + serverConfigPath + ": "
					+ ex.getMessage(), ex);
		}

		httpPortField.setText(httpPort);
		httpsPortField.setText(httpsPort);
		JTextField shutdownPortField = (JTextField) getOption(Constants.SHUTDOWN_PORT);
		shutdownPortField.setText(shutdownPort);
	}

	public static Element getConnectorEl(Document doc, boolean https) {
		try {
			XPathFactory xpFact = XPathFactory.newInstance();
			NodeList connectorEls = (NodeList) xpFact.newXPath().compile(
					"/Server/Service[@name='Catalina']/Connector").evaluate(
					doc, XPathConstants.NODESET);

			// logger.debug("Found " + connectorEls.getLength() + " connector
			// elements.");
			Element connEl = null;
			for (int i = 0; i < connectorEls.getLength() && connEl == null; i++) {
				Element connectorEl = (Element) connectorEls.item(i);
				String scheme = connectorEl.getAttribute("scheme");
				String protocol = connectorEl.getAttribute("protocol");
				// logger.debug("scheme = " + scheme + ", protocol = " +
				// protocol);

				if (!https && (isEmpty(scheme) || "http".equals(scheme))
						&& (isEmpty(protocol) || "HTTP/1.1".equals(protocol))) {
					connEl = connectorEl;
				} else if (https && "https".equals(scheme)) {
					connEl = connectorEl;
				}
			}
			return connEl;
		} catch (Exception ex) {
			throw new RuntimeException("Error finding connector element: "
					+ ex.getMessage(), ex);
		}
	}

	private static boolean isEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	public void applyState() throws InvalidStateException {

		String oldTomcatHttpPort = this.model.getProperty(
				Constants.HTTP_PORT);
		if (oldTomcatHttpPort == null) {
			oldTomcatHttpPort = "8080";
		}
		this.model.setProperty(Constants.OLD_HTTP_PORT,
				oldTomcatHttpPort);

		String oldTomcatHttpsPort = this.model.getProperty(
				Constants.HTTPS_PORT);
		if (oldTomcatHttpsPort == null) {
			oldTomcatHttpsPort = "8443";
		}
		this.model.setProperty(Constants.OLD_HTTPS_PORT,
				oldTomcatHttpsPort);

		JTextField shutdownPortField = (JTextField) getOption(Constants.SHUTDOWN_PORT);
		JTextField httpPortField = (JTextField) getOption(Constants.HTTP_PORT);
		JTextField httpsPortField = (JTextField) getOption(Constants.HTTPS_PORT);

		assertIsInteger(shutdownPortField.getText(),
				"Shutdown Port must be an integer.");
		boolean isSecure = this.model.isTrue(
				Constants.USE_SECURE_CONTAINER);
		if (isSecure) {
			assertIsInteger(httpsPortField.getText(),
					"HTTPS Port must be an integer.");
		} else {
			assertIsInteger(httpsPortField.getText(),
					"HTTP Port must be an integer.");
		}

		try {

			this.serverEl.setAttribute("port", shutdownPortField.getText()
					.trim());
			if (isSecure) {
				String httpsPort = httpsPortField.getText().trim();
				if (this.httpEl != null) {
					this.httpEl.getParentNode().removeChild(this.httpEl);
				}
				if (this.httpsEl != null) {
					this.httpsEl.setAttribute("port", httpsPort);
				}
			} else {
				if (this.httpEl != null) {
					this.httpEl.setAttribute("port", httpPortField.getText()
							.trim());
				} else {
					this.httpEl = this.doc.createElement("Connector");
					XPathFactory xpFact = XPathFactory.newInstance();
					Element serviceEl = (Element) xpFact.newXPath().compile(
							"/Server/Service[@name='Catalina']").evaluate(
							this.doc, XPathConstants.NODE);
					serviceEl.appendChild(this.httpEl);
					httpEl.setAttribute("acceptCount", "100");
					httpEl.setAttribute("connectionTimeout", "20000");
					httpEl.setAttribute("debug", "0");
					httpEl.setAttribute("disableUploadTimeout", Constants.TRUE);
					httpEl.setAttribute("enableLookups", Constants.FALSE);
					httpEl.setAttribute("maxSpareThreads", "75");
					httpEl.setAttribute("minSpareThreads", "25");
					httpEl.setAttribute("port", httpPortField.getText().trim());
				}
			}

			String xml = InstallerUtils.toString(this.doc);
			FileWriter w = new FileWriter(getServerConfigPath());
			w.write(xml);
			w.flush();
			w.close();

		} catch (Exception ex) {
			String errMsg = "Error modifying " + getServerConfigPath() + ": "
					+ ex.getMessage();
			logger.error(errMsg, ex);
			throw new InvalidStateException(errMsg, ex);
		}

		super.applyState();

	}

	private void assertIsInteger(String value, String message)
			throws InvalidStateException {
		try {
			Integer.parseInt(value);
		} catch (Exception ex) {
			throw new InvalidStateException(message);
		}
	}

	private String getServerConfigPath() {
		return (String) this.model.getProperty(Constants.TOMCAT_HOME)
				+ "/conf/server.xml";
	}

	public static void main(String[] args) throws Exception {

		String serverConfigPath = "/Users/joshua/packages/tomcat/jakarta-tomcat-5.0.28/conf/server.xml";
		Document doc = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().parse(
						new FileInputStream(serverConfigPath));
		XPathFactory xpFact = XPathFactory.newInstance();
		Element serverEl = (Element) xpFact.newXPath().compile("/Server")
				.evaluate(doc, XPathConstants.NODE);
		String shutdownPort = serverEl.getAttribute("port");

		Element httpEl = getConnectorEl(doc, false);
		Element httpsEl = getConnectorEl(doc, true);

		String httpPort = null;
		String httpsPort = null;
		if (httpEl != null) {
			httpPort = httpEl.getAttribute("port");
			logger.info("Found HTTP port: " + httpPort);
		} else {
			logger.info("Did not find HTTP port in " + serverConfigPath);
		}
		if (httpsEl != null) {
			httpsPort = httpsEl.getAttribute("port");
			logger.info("Found HTTPS port: " + httpsPort);
		} else {
			logger.info("Did not HTTPS port: " + serverConfigPath);
		}
	}

}
