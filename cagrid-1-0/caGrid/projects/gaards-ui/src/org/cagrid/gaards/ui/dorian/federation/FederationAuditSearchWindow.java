package org.cagrid.gaards.ui.dorian.federation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import org.cagrid.gaards.ui.dorian.SessionPanel;
import org.cagrid.grape.ApplicationComponent;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: AdministratorsWindow.java,v 1.1 2007/04/26 18:43:49 langella
 *          Exp $
 */
public class FederationAuditSearchWindow extends ApplicationComponent {

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private SessionPanel sessionPanel = null;

	private JPanel auditPanel = null;

	/**
	 * This is the default constructor
	 */
	public FederationAuditSearchWindow() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setTitle("Administrators Window");

	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getMainPanel(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 1;
			GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
			gridBagConstraints35.gridx = 0;
			gridBagConstraints35.weightx = 1.0D;
			gridBagConstraints35.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints35.gridy = 0;

			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getSessionPanel(), gridBagConstraints35);
			mainPanel.add(getAuditPanel(), gridBagConstraints);
		}
		return mainPanel;
	}

	/**
	 * This method initializes sessionPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private SessionPanel getSessionPanel() {
		if (sessionPanel == null) {
			sessionPanel = new SessionPanel();
		}
		return sessionPanel;
	}

	/**
	 * This method initializes auditPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAuditPanel() {
		if (auditPanel == null) {
			auditPanel = new FederationAuditPanel(getSessionPanel());
		}
		return auditPanel;
	}

}
