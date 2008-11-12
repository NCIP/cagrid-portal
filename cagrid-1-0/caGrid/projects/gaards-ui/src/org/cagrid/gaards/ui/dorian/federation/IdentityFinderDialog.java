package org.cagrid.gaards.ui.dorian.federation;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.border.TitledBorder;

import org.cagrid.gaards.dorian.common.AuditConstants;
import org.cagrid.gaards.ui.dorian.DorianSessionProvider;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.LookAndFeel;

public class IdentityFinderDialog extends JDialog {

	private static final String SYSTEM = "System"; // @jve:decl-index=0:
	private static final String USER = "User"; // @jve:decl-index=0:
	private static final String HOST = "Host";
	private static final String IDENTITY_PROVIDER = "Identity Provider";
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel identityPanel = null;
	private JPanel buttonPanel = null;
	private JButton select = null;
	private JButton cancel = null;
	private JComboBox identityType = null;
	private String identity;
	private DorianSessionProvider session;

	/**
	 * @param owner
	 */
	public IdentityFinderDialog(DorianSessionProvider session, Frame owner) {
		super(owner);
		this.session = session;
		setModal(true);
		setTitle("Find Identity");
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(200, 150);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridwidth = 1;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.ipady = 0;
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridheight = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.ipadx = 0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getIdentityPanel(), gridBagConstraints);
			jContentPane.add(getButtonPanel(), gridBagConstraints1);
		}
		return jContentPane;
	}

	/**
	 * This method initializes identityPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getIdentityPanel() {
		if (identityPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints2.weightx = 1.0;
			identityPanel = new JPanel();
			identityPanel.setLayout(new GridBagLayout());
			identityPanel.add(getIdentityType(), gridBagConstraints2);
			identityPanel.setBorder(BorderFactory.createTitledBorder(null,
					"Select Identity Type", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, LookAndFeel
							.getPanelLabelColor()));
		}
		return identityPanel;
	}

	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout());
			buttonPanel.add(getSelect(), null);
			buttonPanel.add(getCancel(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes select
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSelect() {
		if (select == null) {
			select = new JButton();
			select.setText("Select");
			select.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String selected = (String)getIdentityType().getSelectedItem();
					if(selected.equals(SYSTEM)){
						identity = AuditConstants.SYSTEM_ID;
					}else if(selected.equals(USER)){
						UserSearchDialog dialog = new UserSearchDialog(session);
						dialog.setModal(true);
						GridApplication.getContext().showDialog(dialog);
						if (dialog.getSelectedUser() != null) {
							identity = dialog.getSelectedUser();
						}

						
					}else if(selected.equals(HOST)){
						
					}else if(selected.equals(IDENTITY_PROVIDER)){
						
					}
					dispose();
				}
			});
		}
		return select;
	}

	/**
	 * This method initializes cancel
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancel() {
		if (cancel == null) {
			cancel = new JButton();
			cancel.setText("Cancel");
			cancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});

		}
		return cancel;
	}

	/**
	 * This method initializes identityType
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getIdentityType() {
		if (identityType == null) {
			identityType = new JComboBox();
			identityType.addItem(IDENTITY_PROVIDER);
			identityType.addItem(HOST);
			identityType.addItem(SYSTEM);
			identityType.addItem(USER);
		}
		return identityType;
	}

	public String getIdentity() {
		return identity;
	}

}
