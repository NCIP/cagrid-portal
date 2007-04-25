package gov.nih.nci.cagrid.introduce.extensions.metadata.editors.servicemetdata;

import gov.nih.nci.cagrid.metadata.common.PointOfContact;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;


/**
 * @author oster
 * 
 */
public class PointsOfContactEditorPanel extends JPanel {
	private PointOfContact[] pointsOfContact;
	private JPanel pocListPanel = null;
	private JPanel detailPanel = null;
	private JList pocList = null;
	private JPanel controlPanel = null;
	private JButton addButton = null;
	private JButton removeButton = null;
	private JLabel fnameLabel = null;
	private JTextField fnameTextField = null;
	private JLabel lnameLabel = null;
	private JTextField lnameTextField = null;
	private JLabel phoneLabel = null;
	private JTextField phoneTextField = null;
	private JTextField emailTextField = null;
	private JLabel emailLabel = null;
	private JLabel affiliationLabel = null;
	private JTextField affiliationTextField = null;
	private JLabel roleLabel = null;
	private JComboBox roleComboBox = null;


	public PointsOfContactEditorPanel() {
		super();
		initialize();
	}


	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.weighty = 1.0;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.gridy = 1;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0,0,0,0);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new java.awt.Dimension(597,305));
        this.add(getPocListPanel(), gridBagConstraints);
        this.add(getDetailPanel(), gridBagConstraints1);
			
	}


	public PointOfContact[] getPointsOfContact() {
		return this.pointsOfContact;
	}


	public void setPointsOfContact(PointOfContact[] pointsOfContact) {
		this.pointsOfContact = pointsOfContact;
	}


	/**
	 * This method initializes pocListPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPocListPanel() {
		if (pocListPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.weightx = 0.0;
			gridBagConstraints3.weighty = 0.0;
			gridBagConstraints3.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0D;
			gridBagConstraints2.weighty = 1.0D;
			pocListPanel = new JPanel();
			pocListPanel.setLayout(new GridBagLayout());
			pocListPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Current Points of Contact", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			pocListPanel.add(getPocList(), gridBagConstraints2);
			pocListPanel.add(getControlPanel(), gridBagConstraints3);
		}
		return pocListPanel;
	}


	/**
	 * This method initializes detailPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDetailPanel() {
		if (detailPanel == null) {
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints17.gridy = 2;
			gridBagConstraints17.weightx = 1.0;
			gridBagConstraints17.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints17.weighty = 0.0;
			gridBagConstraints17.gridx = 3;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 2;
			gridBagConstraints16.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints16.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints16.weighty = 0.0;
			gridBagConstraints16.gridy = 2;
			roleLabel = new JLabel();
			roleLabel.setText("Role");
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints15.gridy = 2;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints15.weighty = 0.0;
			gridBagConstraints15.gridx = 1;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.weighty = 0.0;
			gridBagConstraints14.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints14.gridy = 2;
			affiliationLabel = new JLabel();
			affiliationLabel.setText("Affiliation");
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 2;
			gridBagConstraints13.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints13.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints13.weighty = 0.0;
			gridBagConstraints13.gridy = 1;
			emailLabel = new JLabel();
			emailLabel.setText("Email");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.gridy = 1;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints12.weighty = 0.0;
			gridBagConstraints12.gridx = 3;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints11.weighty = 0.0;
			gridBagConstraints11.gridx = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints10.weighty = 0.0;
			gridBagConstraints10.gridy = 1;
			phoneLabel = new JLabel();
			phoneLabel.setText("Phone Number");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints9.weighty = 0.0;
			gridBagConstraints9.gridx = 3;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 2;
			gridBagConstraints8.weighty = 0.0;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints8.gridy = 0;
			lnameLabel = new JLabel();
			lnameLabel.setText("Last Name");
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints7.weighty = 0.0;
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints6.weighty = 0.0;
			gridBagConstraints6.gridy = 0;
			fnameLabel = new JLabel();
			fnameLabel.setText("First Name");
			detailPanel = new JPanel();
			detailPanel.setLayout(new GridBagLayout());
			detailPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Selected Point of Contact Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			detailPanel.add(fnameLabel, gridBagConstraints6);
			detailPanel.add(getFnameTextField(), gridBagConstraints7);
			detailPanel.add(lnameLabel, gridBagConstraints8);
			detailPanel.add(getLnameTextField(), gridBagConstraints9);
			detailPanel.add(phoneLabel, gridBagConstraints10);
			detailPanel.add(getPhoneTextField(), gridBagConstraints11);
			detailPanel.add(getEmailTextField(), gridBagConstraints12);
			detailPanel.add(emailLabel, gridBagConstraints13);
			detailPanel.add(affiliationLabel, gridBagConstraints14);
			detailPanel.add(getAffiliationTextField(), gridBagConstraints15);
			detailPanel.add(roleLabel, gridBagConstraints16);
			detailPanel.add(getRoleComboBox(), gridBagConstraints17);
		}
		return detailPanel;
	}


	/**
	 * This method initializes pocList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getPocList() {
		if (pocList == null) {
			pocList = new JList();
			pocList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			pocList.setVisibleRowCount(5);
		}
		return pocList;
	}


	/**
	 * This method initializes controlPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getControlPanel() {
		if (controlPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridx = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridx = 0;
			controlPanel = new JPanel();
			controlPanel.setLayout(new GridBagLayout());
			controlPanel.add(getAddButton(), gridBagConstraints4);
			controlPanel.add(getRemoveButton(), gridBagConstraints5);
		}
		return controlPanel;
	}


	/**
	 * This method initializes addButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddButton() {
		if (addButton == null) {
			addButton = new JButton();
			addButton.setText("Add");
			addButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return addButton;
	}


	/**
	 * This method initializes removeButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRemoveButton() {
		if (removeButton == null) {
			removeButton = new JButton();
			removeButton.setText("Remove");
			removeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return removeButton;
	}


	/**
	 * This method initializes fnameTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getFnameTextField() {
		if (fnameTextField == null) {
			fnameTextField = new JTextField();
			fnameTextField.setColumns(10);
		}
		return fnameTextField;
	}


	/**
	 * This method initializes lnameTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getLnameTextField() {
		if (lnameTextField == null) {
			lnameTextField = new JTextField();
			lnameTextField.setColumns(10);
		}
		return lnameTextField;
	}


	/**
	 * This method initializes phoneTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getPhoneTextField() {
		if (phoneTextField == null) {
			phoneTextField = new JTextField();
			phoneTextField.setColumns(10);
		}
		return phoneTextField;
	}


	/**
	 * This method initializes emailTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getEmailTextField() {
		if (emailTextField == null) {
			emailTextField = new JTextField();
			emailTextField.setColumns(10);
		}
		return emailTextField;
	}


	/**
	 * This method initializes affiliationTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getAffiliationTextField() {
		if (affiliationTextField == null) {
			affiliationTextField = new JTextField();
			affiliationTextField.setColumns(10);
		}
		return affiliationTextField;
	}


	/**
	 * This method initializes roleComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getRoleComboBox() {
		if (roleComboBox == null) {
			roleComboBox = new JComboBox();
			roleComboBox.setEditable(true);
		}
		return roleComboBox;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
