package gov.nih.nci.cagrid.introduce.extensions.metadata.editors.servicemetadata;

import gov.nih.nci.cagrid.metadata.common.PointOfContact;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * @author oster
 */
public class PointsOfContactEditorPanel extends JPanel implements ListSelectionListener {
    private List<PointOfContact> pointsOfContact;
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
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.weighty = 1.0;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.gridy = 1;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new java.awt.Dimension(597, 305));
        this.add(getPocListPanel(), gridBagConstraints);
        this.add(getDetailPanel(), gridBagConstraints1);

    }


    public List<PointOfContact> getPointsOfContact() {
        return this.pointsOfContact;
    }


    public void setPointsOfContact(List<PointOfContact> pointsOfContact) {
        this.pointsOfContact = pointsOfContact;
        updateView();
    }


    protected void updateView() {
        DefaultListModel model = new DefaultListModel();

        if (this.pointsOfContact != null) {
            for (PointOfContact poc : this.pointsOfContact) {
                model.addElement(new PointOfContactDisplay(poc));
            }
        }

        getPocList().setModel(model);
        updatePOCView();
    }


    /**
     * This method initializes pocListPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getPocListPanel() {
        if (this.pocListPanel == null) {
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
            this.pocListPanel = new JPanel();
            this.pocListPanel.setLayout(new GridBagLayout());
            this.pocListPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Current Points of Contact",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            this.pocListPanel.add(getPocList(), gridBagConstraints2);
            this.pocListPanel.add(getControlPanel(), gridBagConstraints3);
        }
        return this.pocListPanel;
    }


    /**
     * This method initializes detailPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getDetailPanel() {
        if (this.detailPanel == null) {
            GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
            gridBagConstraints17.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints17.gridy = 2;
            gridBagConstraints17.weightx = 1.0;
            gridBagConstraints17.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints17.weighty = 0.0;
            gridBagConstraints17.gridx = 3;
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.gridx = 2;
            gridBagConstraints16.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints16.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints16.weighty = 0.0;
            gridBagConstraints16.gridy = 2;
            this.roleLabel = new JLabel();
            this.roleLabel.setText("Role");
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints15.gridy = 2;
            gridBagConstraints15.weightx = 1.0;
            gridBagConstraints15.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints15.weighty = 0.0;
            gridBagConstraints15.gridx = 1;
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.gridx = 0;
            gridBagConstraints14.weighty = 0.0;
            gridBagConstraints14.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints14.gridy = 2;
            this.affiliationLabel = new JLabel();
            this.affiliationLabel.setText("Affiliation");
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.gridx = 2;
            gridBagConstraints13.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints13.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints13.weighty = 0.0;
            gridBagConstraints13.gridy = 1;
            this.emailLabel = new JLabel();
            this.emailLabel.setText("Email");
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints12.gridy = 1;
            gridBagConstraints12.weightx = 1.0;
            gridBagConstraints12.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints12.weighty = 0.0;
            gridBagConstraints12.gridx = 3;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.gridy = 1;
            gridBagConstraints11.weightx = 1.0;
            gridBagConstraints11.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints11.weighty = 0.0;
            gridBagConstraints11.gridx = 1;
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 0;
            gridBagConstraints10.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints10.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints10.weighty = 0.0;
            gridBagConstraints10.gridy = 1;
            this.phoneLabel = new JLabel();
            this.phoneLabel.setText("Phone Number");
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints9.gridy = 0;
            gridBagConstraints9.weightx = 1.0;
            gridBagConstraints9.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints9.weighty = 0.0;
            gridBagConstraints9.gridx = 3;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 2;
            gridBagConstraints8.weighty = 0.0;
            gridBagConstraints8.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints8.gridy = 0;
            this.lnameLabel = new JLabel();
            this.lnameLabel.setText("Last Name");
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints7.gridy = 0;
            gridBagConstraints7.weightx = 1.0;
            gridBagConstraints7.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints7.weighty = 0.0;
            gridBagConstraints7.gridx = 1;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints6.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints6.weighty = 0.0;
            gridBagConstraints6.gridy = 0;
            this.fnameLabel = new JLabel();
            this.fnameLabel.setText("First Name");
            this.detailPanel = new JPanel();
            this.detailPanel.setLayout(new GridBagLayout());
            this.detailPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                "Selected Point of Contact Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            this.detailPanel.add(this.fnameLabel, gridBagConstraints6);
            this.detailPanel.add(getFnameTextField(), gridBagConstraints7);
            this.detailPanel.add(this.lnameLabel, gridBagConstraints8);
            this.detailPanel.add(getLnameTextField(), gridBagConstraints9);
            this.detailPanel.add(this.phoneLabel, gridBagConstraints10);
            this.detailPanel.add(getPhoneTextField(), gridBagConstraints11);
            this.detailPanel.add(getEmailTextField(), gridBagConstraints12);
            this.detailPanel.add(this.emailLabel, gridBagConstraints13);
            this.detailPanel.add(this.affiliationLabel, gridBagConstraints14);
            this.detailPanel.add(getAffiliationTextField(), gridBagConstraints15);
            this.detailPanel.add(this.roleLabel, gridBagConstraints16);
            this.detailPanel.add(getRoleComboBox(), gridBagConstraints17);
        }
        return this.detailPanel;
    }


    /**
     * This method initializes pocList
     * 
     * @return javax.swing.JList
     */
    private JList getPocList() {
        if (this.pocList == null) {
            DefaultListModel model = new DefaultListModel();
            this.pocList = new JList(model);
            this.pocList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            this.pocList.addListSelectionListener(this);
            this.pocList.setVisibleRowCount(5);
        }
        return this.pocList;
    }


    /**
     * This method initializes controlPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getControlPanel() {
        if (this.controlPanel == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints5.gridy = 1;
            gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.gridx = 0;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.gridx = 0;
            this.controlPanel = new JPanel();
            this.controlPanel.setLayout(new GridBagLayout());
            this.controlPanel.add(getAddButton(), gridBagConstraints4);
            this.controlPanel.add(getRemoveButton(), gridBagConstraints5);
        }
        return this.controlPanel;
    }


    /**
     * This method initializes addButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getAddButton() {
        if (this.addButton == null) {
            this.addButton = new JButton();
            this.addButton.setText("Add");
            this.addButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    addPOC();
                }
            });
        }
        return this.addButton;
    }


    protected void addPOC() {
        PointOfContact poc = new PointOfContact("", "", "", "", "", "");
        if (this.pointsOfContact == null) {
            this.pointsOfContact = new ArrayList<PointOfContact>();
        }

        this.pointsOfContact.add(poc);
        updateView();
    }


    /**
     * This method initializes removeButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getRemoveButton() {
        if (this.removeButton == null) {
            this.removeButton = new JButton();
            this.removeButton.setText("Remove");
            this.removeButton.setEnabled(false);
            this.removeButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    removeSelectedPOC();
                }
            });
        }
        return this.removeButton;
    }


    protected void removeSelectedPOC() {
        int selectedIndex = getPocList().getSelectedIndex();
        if (selectedIndex != -1) {
            this.pointsOfContact.remove(selectedIndex);
        }
        updateView();
    }


    /**
     * This method initializes fnameTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getFnameTextField() {
        if (this.fnameTextField == null) {
            this.fnameTextField = new JTextField();
            this.fnameTextField.setColumns(10);
            this.fnameTextField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    updatePOCModel();
                }


                public void removeUpdate(DocumentEvent e) {
                    updatePOCModel();
                }


                public void insertUpdate(DocumentEvent e) {
                    updatePOCModel();
                }
            });
        }
        return this.fnameTextField;
    }


    protected void updatePOCModel() {
        PointOfContactDisplay pocD = (PointOfContactDisplay) getPocList().getSelectedValue();
        if (pocD == null) {
            return;
        }
        PointOfContact poc = pocD.getPoc();
        poc.setFirstName(getFnameTextField().getText());
        poc.setLastName(getLnameTextField().getText());
        poc.setAffiliation(getAffiliationTextField().getText());
        poc.setEmail(getEmailTextField().getText());
        poc.setPhoneNumber(getPhoneTextField().getText());
        poc.setRole((String) getRoleComboBox().getSelectedItem());
    }


    /**
     * This method initializes lnameTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getLnameTextField() {
        if (this.lnameTextField == null) {
            this.lnameTextField = new JTextField();
            this.lnameTextField.setColumns(10);
            this.lnameTextField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    updatePOCModel();
                }


                public void removeUpdate(DocumentEvent e) {
                    updatePOCModel();
                }


                public void insertUpdate(DocumentEvent e) {
                    updatePOCModel();
                }
            });
        }
        return this.lnameTextField;
    }


    /**
     * This method initializes phoneTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getPhoneTextField() {
        if (this.phoneTextField == null) {
            this.phoneTextField = new JTextField();
            this.phoneTextField.setColumns(10);
            this.phoneTextField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    updatePOCModel();
                }


                public void removeUpdate(DocumentEvent e) {
                    updatePOCModel();
                }


                public void insertUpdate(DocumentEvent e) {
                    updatePOCModel();
                }
            });
        }
        return this.phoneTextField;
    }


    /**
     * This method initializes emailTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getEmailTextField() {
        if (this.emailTextField == null) {
            this.emailTextField = new JTextField();
            this.emailTextField.setColumns(10);
            this.emailTextField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    updatePOCModel();
                }


                public void removeUpdate(DocumentEvent e) {
                    updatePOCModel();
                }


                public void insertUpdate(DocumentEvent e) {
                    updatePOCModel();
                }
            });
        }
        return this.emailTextField;
    }


    /**
     * This method initializes affiliationTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getAffiliationTextField() {
        if (this.affiliationTextField == null) {
            this.affiliationTextField = new JTextField();
            this.affiliationTextField.setColumns(10);
            this.affiliationTextField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    updatePOCModel();
                }


                public void removeUpdate(DocumentEvent e) {
                    updatePOCModel();
                }


                public void insertUpdate(DocumentEvent e) {
                    updatePOCModel();
                }
            });
        }
        return this.affiliationTextField;
    }


    /**
     * This method initializes roleComboBox
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getRoleComboBox() {
        if (this.roleComboBox == null) {
            this.roleComboBox = new JComboBox();
            this.roleComboBox.addItem("Developer");
            this.roleComboBox.addItem("Maintainer");
            this.roleComboBox.setEditable(true);
            this.roleComboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    updatePOCModel();
                }
            });
        }
        return this.roleComboBox;
    }


    protected class PointOfContactDisplay {
        PointOfContact poc;


        public PointOfContactDisplay(PointOfContact poc) {
            this.poc = poc;
        }


        @Override
        public String toString() {
            if (this.poc == null) {
                return "null";
            }

            return (this.poc.getFirstName() == null || this.poc.getFirstName().trim().equals("")
                ? "<unspecified>"
                : this.poc.getFirstName())
                + " "
                + (this.poc.getLastName() == null || this.poc.getLastName().trim().equals("")
                    ? "<unspecified>"
                    : this.poc.getLastName());
        }


        public PointOfContact getPoc() {
            return this.poc;
        }


        public void setPoc(PointOfContact poc) {
            this.poc = poc;
        }
    }


    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            updatePOCView();
        }
    }


    private void setInfoFieldsEnabled(boolean enabled) {
        for (Component c : getDetailPanel().getComponents()) {
            c.setEnabled(enabled);
        }
    }


    private void updatePOCView() {
        PointOfContactDisplay pocD = (PointOfContactDisplay) getPocList().getSelectedValue();
        PointOfContact poc = null;
        if (pocD != null) {
            poc = pocD.getPoc();
        }
        String fname = null;
        String lname = null;
        String phone = null;
        String email = null;
        String affiliation = null;
        String role = null;

        if (poc == null) {
            getRemoveButton().setEnabled(false);
            setInfoFieldsEnabled(false);

        } else {
            getRemoveButton().setEnabled(true);
            setInfoFieldsEnabled(true);

            // get the values to use
            fname = poc.getFirstName();
            lname = poc.getLastName();
            phone = poc.getPhoneNumber();
            email = poc.getEmail();
            affiliation = poc.getAffiliation();
            role = poc.getRole();
        }

        getFnameTextField().setText(fname);
        getLnameTextField().setText(lname);
        getPhoneTextField().setText(phone);
        getEmailTextField().setText(email);
        getAffiliationTextField().setText(affiliation);
        getRoleComboBox().setSelectedItem(role);
    }
} // @jve:decl-index=0:visual-constraint="10,10"
