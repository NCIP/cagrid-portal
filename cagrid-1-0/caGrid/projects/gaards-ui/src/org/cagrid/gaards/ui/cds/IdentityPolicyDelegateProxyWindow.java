package org.cagrid.gaards.ui.cds;

import gov.nih.nci.cagrid.common.Runner;
import gov.nih.nci.cagrid.common.Utils;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.cagrid.gaards.cds.client.ClientConstants;
import org.cagrid.gaards.cds.client.DelegationUserClient;
import org.cagrid.gaards.cds.common.AllowedParties;
import org.cagrid.gaards.cds.common.IdentityDelegationPolicy;
import org.cagrid.gaards.ui.dorian.ifs.FindUserDialog;
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
public class IdentityPolicyDelegateProxyWindow extends ApplicationComponent {

	private JPanel jContentPane = null;

	private JPanel topPanel = null;

	private JLabel jLabel = null;

	private JLabel jLabel1 = null;

	private JPanel buttonPanel = null;

	private JButton delegateCredentialButton = null;

	private JButton cancelButton = null;

	private JTextField delegationService = null;

	private JTextField credential = null;

	private JPanel policyPanel = null;

	private JScrollPane jScrollPane = null;

	private GridIdentityTable policyTable = null;

	private JPanel addIdentityPanel = null;

	private JLabel jLabel2 = null;

	private JTextField gridIdentity = null;

	private JButton addIdentityButton = null;

	private JButton removeIdentityButton = null;

	private JPanel identityControlPanel = null;

	private JButton findUserButton = null;

	private DelegationRequestCache cache;

	/**
	 * This is the default constructor
	 */
	public IdentityPolicyDelegateProxyWindow(DelegationRequestCache cache) {
		super();
		this.cache = cache;
		initialize();
		this.getDelegationService().setText(cache.getDelegationURL());
		this.getCredential().setText(cache.getCredential().getIdentity());
		if (cache.getPolicy() != null) {
			if (cache.getPolicy() instanceof IdentityDelegationPolicy) {
				IdentityDelegationPolicy p = (IdentityDelegationPolicy) cache
						.getPolicy();
				AllowedParties ap = p.getAllowedParties();
				if (ap != null) {
					String[] ids = ap.getGridIdentity();
					if (ids != null) {
						for (int i = 0; i < ids.length; i++) {
							getPolicyTable().addIdentity(ids[0]);
						}

					}

				}
			}
		}
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
			delegateCredentialButton.setIcon(CDSLookAndFeel.getDelegateCredentialIcon());
			delegateCredentialButton.addActionListener(new java.awt.event.ActionListener() {
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
		IdentityDelegationPolicy p = new IdentityDelegationPolicy();
		AllowedParties ap = new AllowedParties();
		int count = getPolicyTable().getRowCount();
		String[] ids = new String[count];
		for (int i = 0; i < count; i++) {
			ids[i] = (String) getPolicyTable().getValueAt(i, 0);
		}
		ap.setGridIdentity(ids);
		p.setAllowedParties(ap);
		cache.setPolicy(p);
		return cache;
	}

	private void delegateCredential() {
		try {
			DelegationRequestCache c = getDelegationCache();
			DelegationUserClient client = new DelegationUserClient(c
					.getDelegationURL(), c.getCredential());
			client.delegateCredential(c.getDelegationLifetime(), c
					.getDelegationPathLength(), c.getPolicy(), c
					.getIssuedCredentialLifetime(), c
					.getIssuedCredentialPathLength(),
					ClientConstants.DEFAULT_KEY_SIZE);
			GridApplication.getContext().showMessage(
					"Succesfully delegated the credential for "
							+ c.getCredential().getIdentity() + ".");
			dispose();
		} catch (Exception e) {
			ErrorDialog.showError(e);
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
	private JPanel getPolicyPanel() {
		if (policyPanel == null) {
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints14.weightx = 1.0D;
			gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints14.gridy = 2;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.weightx = 1.0D;
			gridBagConstraints7.gridy = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.BOTH;
			gridBagConstraints6.weighty = 1.0;
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			policyPanel = new JPanel();
			policyPanel.setLayout(new GridBagLayout());
			policyPanel.add(getJScrollPane(), gridBagConstraints6);
			policyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, "Identity Delegation Policy",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					LookAndFeel.getPanelLabelColor()));
			policyPanel.add(getAddIdentityPanel(), gridBagConstraints7);
			policyPanel.add(getIdentityControlPanel(), gridBagConstraints14);
		}
		return policyPanel;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getPolicyTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes policyTable
	 * 
	 * @return javax.swing.JTable
	 */
	private GridIdentityTable getPolicyTable() {
		if (policyTable == null) {
			policyTable = new GridIdentityTable();
		}
		return policyTable;
	}

	/**
	 * This method initializes addIdentityPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAddIdentityPanel() {
		if (addIdentityPanel == null) {
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.gridy = 0;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.gridy = 0;
			gridBagConstraints8.weightx = 1.0;
			jLabel2 = new JLabel();
			jLabel2.setText("Grid Identity");
			addIdentityPanel = new JPanel();
			addIdentityPanel.setLayout(new GridBagLayout());
			addIdentityPanel.add(jLabel2, gridBagConstraints9);
			addIdentityPanel.add(getGridIdentity(), gridBagConstraints8);
			addIdentityPanel.add(getFindUserButton(), new GridBagConstraints());
		}
		return addIdentityPanel;
	}

	/**
	 * This method initializes gridIdentity
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getGridIdentity() {
		if (gridIdentity == null) {
			gridIdentity = new JTextField();
		}
		return gridIdentity;
	}

	/**
	 * This method initializes addIdentityButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddIdentityButton() {
		if (addIdentityButton == null) {
			addIdentityButton = new JButton();
			addIdentityButton.setText("Add");
			addIdentityButton.setIcon(CDSLookAndFeel.getAddIcon());
			addIdentityButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							String gridId = Utils.clean(getGridIdentity()
									.getText());
							if (gridId == null) {
								GridApplication.getContext().showMessage(
										"Please specify a Grid Identity.");
							}
							getPolicyTable().addIdentity(gridId);
							getGridIdentity().setText("");
						}
					});
		}
		return addIdentityButton;
	}

	/**
	 * This method initializes removeIdentityButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRemoveIdentityButton() {
		if (removeIdentityButton == null) {
			removeIdentityButton = new JButton();
			removeIdentityButton.setText("Remove");
			removeIdentityButton.setIcon(CDSLookAndFeel.getRemoveIcon());
			removeIdentityButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							try {
								getPolicyTable().removeSelectedIdentity();
							} catch (Exception ex) {
								GridApplication.getContext().showMessage(
										ex.getMessage());
							}
						}
					});
		}
		return removeIdentityButton;
	}

	/**
	 * This method initializes identityControlPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getIdentityControlPanel() {
		if (identityControlPanel == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.gridx = 1;
			gridBagConstraints13.gridy = 0;
			gridBagConstraints13.insets = new Insets(2, 2, 2, 2);
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.gridy = 0;
			gridBagConstraints10.insets = new Insets(2, 2, 2, 2);
			identityControlPanel = new JPanel();
			identityControlPanel.setLayout(new GridBagLayout());
			identityControlPanel.add(getAddIdentityButton(),
					gridBagConstraints10);
			identityControlPanel.add(getRemoveIdentityButton(),
					gridBagConstraints13);
		}
		return identityControlPanel;
	}

	/**
	 * This method initializes findUserButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getFindUserButton() {
		if (findUserButton == null) {
			findUserButton = new JButton();
			findUserButton.setText("Find...");
			findUserButton.setIcon(CDSLookAndFeel.getQueryIcon());
			findUserButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							FindUserDialog dialog = new FindUserDialog();
							dialog.setModal(true);
							GridApplication.getContext().showDialog(dialog);
							if (dialog.getSelectedUser() != null) {
								getGridIdentity().setText(
										dialog.getSelectedUser());
							}
						}
					});
		}
		return findUserButton;
	}

}
