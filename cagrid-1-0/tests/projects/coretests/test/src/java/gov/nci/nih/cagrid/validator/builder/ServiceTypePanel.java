package gov.nci.nih.cagrid.validator.builder;

import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.tests.core.beans.validation.ServiceType;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

/** 
 *  ServiceTypePanel
 *  Panel to display / manage service types and their tests
 * 
 * @author David Ervin
 * 
 * @created Sep 5, 2007 12:25:19 PM
 * @version $Id: ServiceTypePanel.java,v 1.2 2007-09-05 17:12:41 dervin Exp $ 
 */
public class ServiceTypePanel extends JPanel {

    private JList typesList = null;
    private JList stepsList = null;
    private JScrollPane typesScrollPane = null;
    private JScrollPane stepsScrollPane = null;
    private JButton addTypeButton = null;
    private JTextField typeNameTextField = null;
    private JLabel typeNameLabel = null;
    private JButton removeTypeButton = null;
    private JPanel typeInputPanel = null;
    private JPanel typeButtonsPanel = null;
    private JLabel stepClassnameLabel = null;
    private JComboBox stepClassnameComboBox = null;
    private JButton discoverStepsButton = null;
    private JButton addStepButton = null;
    private JButton removeStepButton = null;
    private JButton moveStepUpButton = null;
    private JButton moveStepDownButton = null;
    private JPanel stepInputPanel = null;  //  @jve:decl-index=0:visual-constraint="608,236"
    private JPanel stepButtonsPanel = null;
    private JPanel stepOrderButtonPanel = null;
    private JPanel stepsPanel = null;  //  @jve:decl-index=0:visual-constraint="395,85"
    private JPanel listContainerPanel = null;
    private JPanel inputContainerPanel = null;


    public ServiceTypePanel() {
        super();
        initialize();
    }
    
    
    private void initialize() {
        GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
        gridBagConstraints9.gridx = 0;
        gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints9.gridy = 1;
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        gridBagConstraints8.gridx = 0;
        gridBagConstraints8.fill = GridBagConstraints.BOTH;
        gridBagConstraints8.weightx = 1.0D;
        gridBagConstraints8.weighty = 1.0D;
        gridBagConstraints8.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(695, 265));
        this.add(getListContainerPanel(), gridBagConstraints8);
        this.add(getInputContainerPanel(), gridBagConstraints9);
        
    }
    
    
    public ServiceType[] getServiceTypes() {
        return null;
    }
    
    
    public void setServiceTypes(ServiceType[] types) {
        // TODO: populate fields
    }


    /**
     * This method initializes typesList	
     * 	
     * @return javax.swing.JList	
     */
    private JList getTypesList() {
        if (typesList == null) {
            typesList = new JList();
            typesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        return typesList;
    }


    /**
     * This method initializes stepsList	
     * 	
     * @return javax.swing.JList	
     */
    private JList getStepsList() {
        if (stepsList == null) {
            stepsList = new JList();
            stepsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        return stepsList;
    }


    /**
     * This method initializes typesScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getTypesScrollPane() {
        if (typesScrollPane == null) {
            typesScrollPane = new JScrollPane();
            typesScrollPane.setBorder(BorderFactory.createTitledBorder(null, "Service Types", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            typesScrollPane.setViewportView(getTypesList());
        }
        return typesScrollPane;
    }


    /**
     * This method initializes stepsScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getStepsScrollPane() {
        if (stepsScrollPane == null) {
            stepsScrollPane = new JScrollPane();
            stepsScrollPane.setBorder(BorderFactory.createTitledBorder(null, "Test Steps", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            stepsScrollPane.setViewportView(getStepsList());
        }
        return stepsScrollPane;
    }


    /**
     * This method initializes addTypeButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getAddTypeButton() {
        if (addTypeButton == null) {
            addTypeButton = new JButton();
            addTypeButton.setText("Add Type");
            addTypeButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return addTypeButton;
    }


    /**
     * This method initializes typeNameTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getTypeNameTextField() {
        if (typeNameTextField == null) {
            typeNameTextField = new JTextField();
        }
        return typeNameTextField;
    }


    /**
     * This method initializes typeNameLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getTypeNameLabel() {
        if (typeNameLabel == null) {
            typeNameLabel = new JLabel();
            typeNameLabel.setText("Type Name:");
        }
        return typeNameLabel;
    }


    /**
     * This method initializes removeTypeButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getRemoveTypeButton() {
        if (removeTypeButton == null) {
            removeTypeButton = new JButton();
            removeTypeButton.setText("Remove Type");
            removeTypeButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return removeTypeButton;
    }


    /**
     * This method initializes typeInputPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getTypeInputPanel() {
        if (typeInputPanel == null) {
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 1;
            gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints11.anchor = GridBagConstraints.EAST;
            gridBagConstraints11.gridy = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints1.gridx = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints.gridy = 0;
            typeInputPanel = new JPanel();
            typeInputPanel.setLayout(new GridBagLayout());
            typeInputPanel.add(getTypeNameLabel(), gridBagConstraints);
            typeInputPanel.add(getTypeNameTextField(), gridBagConstraints1);
            typeInputPanel.add(getTypeButtonsPanel(), gridBagConstraints11);
        }
        return typeInputPanel;
    }


    /**
     * This method initializes typeButtonsPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getTypeButtonsPanel() {
        if (typeButtonsPanel == null) {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(1);
            gridLayout.setHgap(4);
            gridLayout.setColumns(2);
            typeButtonsPanel = new JPanel();
            typeButtonsPanel.setLayout(gridLayout);
            typeButtonsPanel.add(getAddTypeButton(), null);
            typeButtonsPanel.add(getRemoveTypeButton(), null);
        }
        return typeButtonsPanel;
    }


    /**
     * This method initializes stepClassnameLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getStepClassnameLabel() {
        if (stepClassnameLabel == null) {
            stepClassnameLabel = new JLabel();
            stepClassnameLabel.setText("Step Classname:");
        }
        return stepClassnameLabel;
    }


    /**
     * This method initializes stepClassnameComboBox	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getStepClassnameComboBox() {
        if (stepClassnameComboBox == null) {
            stepClassnameComboBox = new JComboBox();
        }
        return stepClassnameComboBox;
    }


    /**
     * This method initializes discoverStepsButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getDiscoverStepsButton() {
        if (discoverStepsButton == null) {
            discoverStepsButton = new JButton();
            discoverStepsButton.setText("Discover...");
            discoverStepsButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return discoverStepsButton;
    }


    /**
     * This method initializes addStepButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getAddStepButton() {
        if (addStepButton == null) {
            addStepButton = new JButton();
            addStepButton.setText("Add Step");
            addStepButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return addStepButton;
    }


    /**
     * This method initializes removeStepButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getRemoveStepButton() {
        if (removeStepButton == null) {
            removeStepButton = new JButton();
            removeStepButton.setText("Remove Step");
            removeStepButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return removeStepButton;
    }


    /**
     * This method initializes moveStepUpButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getMoveStepUpButton() {
        if (moveStepUpButton == null) {
            moveStepUpButton = new JButton();
            moveStepUpButton.setIcon(IntroduceLookAndFeel.getUpIcon());
            moveStepUpButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return moveStepUpButton;
    }


    /**
     * This method initializes moveStepDownButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getMoveStepDownButton() {
        if (moveStepDownButton == null) {
            moveStepDownButton = new JButton();
            moveStepDownButton.setIcon(IntroduceLookAndFeel.getDownIcon());
            moveStepDownButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return moveStepDownButton;
    }


    /**
     * This method initializes stepInputPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getStepInputPanel() {
        if (stepInputPanel == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 1;
            gridBagConstraints5.gridwidth = 2;
            gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints5.anchor = GridBagConstraints.EAST;
            gridBagConstraints5.gridy = 1;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 2;
            gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.gridy = 0;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints3.gridx = 1;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints2.gridy = 0;
            stepInputPanel = new JPanel();
            stepInputPanel.setLayout(new GridBagLayout());
            stepInputPanel.add(getStepClassnameLabel(), gridBagConstraints2);
            stepInputPanel.add(getStepClassnameComboBox(), gridBagConstraints3);
            stepInputPanel.add(getDiscoverStepsButton(), gridBagConstraints4);
            stepInputPanel.add(getStepButtonsPanel(), gridBagConstraints5);
        }
        return stepInputPanel;
    }


    /**
     * This method initializes stepButtonsPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getStepButtonsPanel() {
        if (stepButtonsPanel == null) {
            GridLayout gridLayout1 = new GridLayout();
            gridLayout1.setRows(1);
            gridLayout1.setColumns(2);
            gridLayout1.setHgap(4);
            stepButtonsPanel = new JPanel();
            stepButtonsPanel.setLayout(gridLayout1);
            stepButtonsPanel.add(getAddStepButton(), null);
            stepButtonsPanel.add(getRemoveStepButton(), null);
        }
        return stepButtonsPanel;
    }


    /**
     * This method initializes stepOrderButtonPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getStepOrderButtonPanel() {
        if (stepOrderButtonPanel == null) {
            GridLayout gridLayout2 = new GridLayout();
            gridLayout2.setRows(2);
            gridLayout2.setVgap(4);
            gridLayout2.setColumns(1);
            stepOrderButtonPanel = new JPanel();
            stepOrderButtonPanel.setLayout(gridLayout2);
            stepOrderButtonPanel.add(getMoveStepUpButton(), null);
            stepOrderButtonPanel.add(getMoveStepDownButton(), null);
        }
        return stepOrderButtonPanel;
    }


    /**
     * This method initializes stepsPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getStepsPanel() {
        if (stepsPanel == null) {
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 1;
            gridBagConstraints7.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints7.gridy = 0;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.fill = GridBagConstraints.BOTH;
            gridBagConstraints6.gridy = 0;
            gridBagConstraints6.weightx = 1.0D;
            gridBagConstraints6.weighty = 1.0;
            gridBagConstraints6.gridx = 0;
            stepsPanel = new JPanel();
            stepsPanel.setLayout(new GridBagLayout());
            stepsPanel.add(getStepsScrollPane(), gridBagConstraints6);
            stepsPanel.add(getStepOrderButtonPanel(), gridBagConstraints7);
        }
        return stepsPanel;
    }


    /**
     * This method initializes listContainerPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getListContainerPanel() {
        if (listContainerPanel == null) {
            GridLayout gridLayout3 = new GridLayout();
            gridLayout3.setColumns(2);
            gridLayout3.setHgap(4);
            listContainerPanel = new JPanel();
            listContainerPanel.setLayout(gridLayout3);
            listContainerPanel.add(getTypesScrollPane(), null);
            listContainerPanel.add(getStepsPanel(), null);
        }
        return listContainerPanel;
    }


    /**
     * This method initializes inputContainerPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getInputContainerPanel() {
        if (inputContainerPanel == null) {
            GridLayout gridLayout4 = new GridLayout();
            gridLayout4.setRows(1);
            gridLayout4.setHgap(4);
            gridLayout4.setColumns(2);
            inputContainerPanel = new JPanel();
            inputContainerPanel.setLayout(gridLayout4);
            inputContainerPanel.add(getTypeInputPanel(), null);
            inputContainerPanel.add(getStepInputPanel(), null);
        }
        return inputContainerPanel;
    }
}  //  @jve:decl-index=0:visual-constraint="13,9"
