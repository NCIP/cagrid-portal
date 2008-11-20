package org.cagrid.gaards.ui.cds;

import gov.nih.nci.cagrid.common.Runner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cagrid.gaards.cds.client.ClientConstants;
import org.cagrid.gaards.cds.client.DelegationUserClient;
import org.cagrid.grape.ApplicationComponent;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.LookAndFeel;
import org.cagrid.grape.utils.ErrorDialog;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class DelegateProxyWindowStep2 extends ApplicationComponent {
	
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel topPanel = null;

	private JLabel jLabel = null;

	private JLabel jLabel1 = null;

	private JPanel buttonPanel = null;

	private JButton delegateCredentialButton = null;

	private JButton cancelButton = null;

	private JTextField delegationService = null;

	private JTextField credential = null;

	private DelegationPolicyPanel policyPanel = null;

	private DelegationRequestCache cache;

	private String policyType;

	/**
	 * This is the default constructor
	 */
	public DelegateProxyWindowStep2(String policyType,
			DelegationRequestCache cache) {
		super();
		this.policyType = policyType;
		this.cache = cache;
		initialize();
		this.getDelegationService().setText(cache.getDelegationURL());
		this.getCredential().setText(cache.getCredential().getIdentity());
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setSize(600, 400);
		this.setContentPane(getJContentPane());
		this.setTitle("Delegate Credential (Step 2 of 2)");
		this.setFrameIcon(CDSLookAndFeel.getDelegateCredentialIcon());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints11.weightx = 1.0D;
			gridBagConstraints11.weighty = 1.0D;
			gridBagConstraints11.gridy = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints12.gridy = 2;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.ipadx = 0;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 0.0D;
			gridBagConstraints.anchor = GridBagConstraints.NORTH;
			gridBagConstraints.gridy = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getTopPanel(), gridBagConstraints);
			jContentPane.add(getButtonPanel(), gridBagConstraints12);
			jContentPane.add(getPolicyPanel(), gridBagConstraints11);
		}
		return jContentPane;
	}

	/**
	 * This method initializes topPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTopPanel() {
		if (topPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints4.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints4.gridy = 1;
			jLabel1 = new JLabel();
			jLabel1.setText("Credential");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText("Delegation Service");
			topPanel = new JPanel();
			topPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, "Delegate Credential",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					LookAndFeel.getPanelLabelColor()));
			topPanel.setLayout(new GridBagLayout());
			topPanel.add(jLabel, gridBagConstraints2);
			topPanel.add(jLabel1, gridBagConstraints4);
			topPanel.add(getDelegationService(), gridBagConstraints1);
			topPanel.add(getCredential(), gridBagConstraints5);
		}
		return topPanel;
	}

	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getDelegateCredentialButton(), null);
			buttonPanel.add(getCancelButton(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes delegateCredentialButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDelegateCredentialButton() {
		if (delegateCredentialButton == null) {
			delegateCredentialButton = new JButton();
			delegateCredentialButton.setText("Delegate Credential");
			delegateCredentialButton.setIcon(CDSLookAndFeel
					.getDelegateCredentialIcon());
			delegateCredentialButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							Runner runner = new Runner() {
								public void execute() {
									delegateCredential();
								}
							};
							try {
								GridApplication.getContext()
										.executeInBackground(runner);
							} catch (Exception t) {
								t.getMessage();
							}
						}

					});
		}

		return delegateCredentialButton;
	}

	/**
	 * This method initializes cancelButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
			cancelButton.setIcon(LookAndFeel.getCloseIcon());
		}
		return cancelButton;
	}

	private DelegationRequestCache getDelegationCache() {
		cache.setPolicy(getPolicyPanel().getPolicy());
		return cache;
	}

	private synchronized void delegateCredential() {
		try {
			getDelegateCredentialButton().setEnabled(false);
			DelegationRequestCache c = getDelegationCache();
			DelegationUserClient client = new DelegationUserClient(c
					.getDelegationURL(), c.getCredential());
			client.delegateCredential(c.getDelegationLifetime(), c
					.getDelegationPathLength(), c.getPolicy(), c
					.getIssuedCredentialLifetime(), c
					.getIssuedCredentialPathLength(),
					ClientConstants.DEFAULT_KEY_SIZE);
			dispose();
			GridApplication.getContext().showMessage(
					"Succesfully delegated the credential for "
							+ c.getCredential().getIdentity() + ".");

		} catch (Exception e) {
			ErrorDialog.showError(e);
		} finally {
			getDelegateCredentialButton().setEnabled(true);
		}

	}

	/**
	 * This method initializes delegationService
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getDelegationService() {
		if (delegationService == null) {
			delegationService = new JTextField();
			delegationService.setEditable(false);
		}
		return delegationService;
	}

	/**
	 * This method initializes credential
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCredential() {
		if (credential == null) {
			credential = new JTextField();
			credential.setEditable(false);
		}
		return credential;
	}

	/**
	 * This method initializes policyPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private DelegationPolicyPanel getPolicyPanel() {
		if (policyPanel == null) {
			policyPanel = CDSUIUtils.getPolicyPanel(policyType, true);
		}
		return policyPanel;
	}

}
