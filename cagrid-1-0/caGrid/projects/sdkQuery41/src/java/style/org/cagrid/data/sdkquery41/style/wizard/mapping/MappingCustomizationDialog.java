package org.cagrid.data.sdkquery41.style.wizard.mapping;

import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;

/**
 * Customizes the class to element mapping
 * 
 * @author David
 */
public class MappingCustomizationDialog extends JDialog {
    
    private static final String NO_ELEMENT_SELECTED = "-- Select Element --";

    private NamespaceType nsType = null;
    private CadsrPackage cadsrPackage = null;
    
    private JTable mappingTable = null;
    private JScrollPane mappingScrollPane = null;
    private JPanel mainPanel = null;
    private JPanel infoPanel = null;
    private JLabel packageNameLabel = null;
    private JTextField packageNameTextField = null;
    private JLabel namespaceLabel = null;
    private JTextField namespaceTextField = null;
    private JButton doneButton = null;

    private MappingCustomizationDialog(NamespaceType nsType, CadsrPackage cadsrPackage) {
        super((JFrame) null, "Element Mapping Customization", true);
        this.nsType = nsType;
        this.cadsrPackage = cadsrPackage;
        initialize();
    }
    
    
    public static void customizeElementMapping(NamespaceType nsType, CadsrPackage cadsrPakcage) {
        MappingCustomizationDialog dialog = new MappingCustomizationDialog(nsType, cadsrPakcage);
        dialog.setVisible(true);
    }
    
    
    private void initialize() {
        this.setSize(new Dimension(420, 350));
        this.setContentPane(getMainPanel());
        
    }
    
    
    private JTable getMappingTable() {
        if (mappingTable == null) {
            DefaultTableModel mappingTableModel = new DefaultTableModel() {
                public boolean isCellEditable(int row, int column) {
                    return row == 1;
                }
            };
            mappingTableModel.addColumn("Class Name");
            mappingTableModel.addColumn("Element Name");
            mappingTable = new JTable(mappingTableModel);
            mappingTable.setDefaultRenderer(Object.class, new ValidatingTableCellRenderer() {
                protected void validateCell(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                    if (row == 1) {
                        JComboBox combo = (JComboBox) value;
                        // TODO: ensure not NO_ELEMENT_SELECTED
                        setErrorBackground();
                    }
                }
            });
            mappingTable.setDefaultEditor(Object.class, new ComponentTableCellEditor());
        }
        return mappingTable;
    }
    
    
    private JScrollPane getMappingScrollPane() {
        if (mappingScrollPane == null) {
            mappingScrollPane = new JScrollPane();
            mappingScrollPane.setViewportView(getMappingTable());
            mappingScrollPane.setBorder(BorderFactory.createTitledBorder(
                null, "Class Mappings", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
        }
        return mappingScrollPane;
    }


    /**
     * This method initializes mainPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints6.anchor = GridBagConstraints.EAST;
            gridBagConstraints6.gridy = 2;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.fill = GridBagConstraints.BOTH;
            gridBagConstraints5.gridy = 1;
            gridBagConstraints5.weightx = 1.0;
            gridBagConstraints5.weighty = 1.0D;
            gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints5.gridx = 0;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints4.gridy = 0;
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.add(getInfoPanel(), gridBagConstraints4);
            mainPanel.add(getMappingScrollPane(), gridBagConstraints5);
            mainPanel.add(getDoneButton(), gridBagConstraints6);
        }
        return mainPanel;
    }


    /**
     * This method initializes infoPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getInfoPanel() {
        if (infoPanel == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.gridy = 1;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints3.gridx = 1;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.weightx = 1.0;
            gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints2.gridx = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints1.gridy = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints.gridy = 0;
            infoPanel = new JPanel();
            infoPanel.setLayout(new GridBagLayout());
            infoPanel.setBorder(BorderFactory.createTitledBorder(
                null, "Package Mapping", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            infoPanel.add(getPackageNameLabel(), gridBagConstraints);
            infoPanel.add(getNamespaceLabel(), gridBagConstraints1);
            infoPanel.add(getPackageNameTextField(), gridBagConstraints2);
            infoPanel.add(getNamespaceTextField(), gridBagConstraints3);
        }
        return infoPanel;
    }


    /**
     * This method initializes packageNameLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getPackageNameLabel() {
        if (packageNameLabel == null) {
            packageNameLabel = new JLabel();
            packageNameLabel.setText("Package Name:");
        }
        return packageNameLabel;
    }


    /**
     * This method initializes packageNameTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getPackageNameTextField() {
        if (packageNameTextField == null) {
            packageNameTextField = new JTextField();
            packageNameTextField.setEditable(false);
        }
        return packageNameTextField;
    }


    /**
     * This method initializes namespaceLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getNamespaceLabel() {
        if (namespaceLabel == null) {
            namespaceLabel = new JLabel();
            namespaceLabel.setText("Namespace:");
        }
        return namespaceLabel;
    }


    /**
     * This method initializes namespaceTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getNamespaceTextField() {
        if (namespaceTextField == null) {
            namespaceTextField = new JTextField();
            namespaceTextField.setEditable(false);
        }
        return namespaceTextField;
    }


    /**
     * This method initializes doneButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getDoneButton() {
        if (doneButton == null) {
            doneButton = new JButton();
            doneButton.setText("Done");
            doneButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return doneButton;
    }
}  //  @jve:decl-index=0:visual-constraint="193,21"
