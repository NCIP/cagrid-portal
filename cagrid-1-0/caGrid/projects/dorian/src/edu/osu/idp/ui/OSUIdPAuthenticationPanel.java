package edu.osu.idp.ui;

import gov.nih.nci.cagrid.dorian.ui.IdPConf;
import gov.nih.nci.cagrid.dorian.ui.ifs.IdPAuthenticationPanel;
import gov.nih.nci.cagrid.gridca.common.SecurityUtil;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLResponse;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.ByteArrayInputStream;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class OSUIdPAuthenticationPanel extends IdPAuthenticationPanel {

	private final static String IDP_URL = "https://authdev.it.ohio-state.edu/shibboleth-grid/GRID?providerId=dorian";
	private JPanel loginPanel = null;
	private JLabel userIdLabel = null;
	private JTextField userId = null;
	private JLabel passwordLabel = null;
	private JPasswordField password = null;
	private JLabel urlLabel = null;
	private JTextField url = null;


	public OSUIdPAuthenticationPanel(IdPConf conf) {
		super(conf);
		SecurityUtil.init();
		initialize();
	}


	public SAMLAssertion authenticate() throws Exception {
		Protocol.registerProtocol("https", new Protocol("https", new EasySSLProtocolSocketFactory(), 443));
		HttpClient client = new HttpClient();
		client.getState().setCredentials(new AuthScope(null, 443, null),
			new UsernamePasswordCredentials(userId.getText(), new String(password.getPassword())));
		GetMethod get = new GetMethod(IDP_URL);

		get.setDoAuthentication(true);

		try {
			// execute the GET
			int status = client.executeMethod(get);
			if (status == 401) {
				throw new Exception("Invalid Login Specified!!!");
			} else if (status > 200) {
				throw new Exception("Error authenticating with server. (" + status + ")");
			}

			// print the status and response
			//System.out.println(status + "\n" + get.getResponseBodyAsString());
			SAMLResponse res = new SAMLResponse(new ByteArrayInputStream(get.getResponseBodyAsString()
				.getBytes()));
			SAMLAssertion saml =  (SAMLAssertion)res.getAssertions().next();
			return saml;
		} finally {
			// release any connection resources used by the method
			get.releaseConnection();
		}
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getLoginPanel(), java.awt.BorderLayout.CENTER);
	}


	/**
	 * This method initializes loginPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getLoginPanel() {
		if (loginPanel == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.gridy = 0;
			gridBagConstraints21.weightx = 1.0;
			gridBagConstraints21.gridx = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints11.gridy = 0;
			urlLabel = new JLabel();
			urlLabel.setText("URL");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 2;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.weighty = 0.0D;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 2;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.gridx = 0;
			passwordLabel = new JLabel();
			passwordLabel.setText("Password");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints.weighty = 0.0D;
			gridBagConstraints.weightx = 1.0;
			userIdLabel = new JLabel();
			userIdLabel.setText("User Id");
			loginPanel = new JPanel();
			loginPanel.setLayout(new GridBagLayout());
			loginPanel.add(userIdLabel, gridBagConstraints1);
			loginPanel.add(getUserId(), gridBagConstraints);
			loginPanel.add(passwordLabel, gridBagConstraints2);
			loginPanel.add(getPassword(), gridBagConstraints3);
			loginPanel.add(urlLabel, gridBagConstraints11);
			loginPanel.add(getUrl(), gridBagConstraints21);
		}
		return loginPanel;
	}


	/**
	 * This method initializes userId
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getUserId() {
		if (userId == null) {
			userId = new JTextField();
		}
		return userId;
	}


	/**
	 * This method initializes password
	 * 
	 * @return javax.swing.JPasswordField
	 */
	private JPasswordField getPassword() {
		if (password == null) {
			password = new JPasswordField();
		}
		return password;
	}


	/**
	 * This method initializes url
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getUrl() {
		if (url == null) {
			url = new JTextField();
			url.setText(IDP_URL);
			url.setEditable(false);
		}
		return url;
	}

}
