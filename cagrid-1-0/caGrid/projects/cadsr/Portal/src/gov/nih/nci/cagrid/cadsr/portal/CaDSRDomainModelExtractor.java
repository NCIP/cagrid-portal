package gov.nih.nci.cagrid.cadsr.portal;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Dimension;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import java.io.FileWriter;
import java.util.Vector;

/** 
 *  CaDSRDomainModelExtractor
 *  GUI tool to extract a domain model from the caDSR
 * 
 * @author David Ervin
 * 
 * @created Oct 24, 2007 10:24:40 AM
 * @version $Id: CaDSRDomainModelExtractor.java,v 1.1 2007-10-29 17:06:37 dervin Exp $ 
 */
public class CaDSRDomainModelExtractor extends JFrame {
    
    public static final String PRODUCTION_CADSR_URL = 
        "http://cagrid-service.nci.nih.gov:8080/wsrf/services/cagrid/CaDSRService";
    
    private CaDSRBrowserPanel browserPanel = null;
    private JButton includePackageButton = null;
    private JButton removePackageButton = null;
    private JLabel selectedProjectLabel = null;
    private JTextField selectedProjectTextField = null;
    private JList selectedPackagesList = null;
    private JScrollPane selectedPackagesScrollPane = null;
    private JPanel selectionInfoPanel = null;
    private JPanel packageSelectionPanel = null;
    private JPanel mainPanel = null;
    private JButton saveButton = null;
    private JButton closeButton = null;
    private JPanel buttonPanel = null;
    
    private Project selectedProject = null;
    
    public CaDSRDomainModelExtractor() {
        super();
        initialize();
    }
    
    
    private void initialize() {
        JPanel holder = new JPanel();
        holder.setLayout(new GridBagLayout());
        GridBagConstraints cons1 = new GridBagConstraints();
        cons1.gridx = 0;
        cons1.gridy = 0;
        cons1.weighty = 1.0D;
        cons1.weightx = 1.0D;
        cons1.fill = GridBagConstraints.BOTH;
        holder.add(getMainPanel(), cons1);
        GridBagConstraints cons2 = new GridBagConstraints();
        cons2.gridx = 0;
        cons2.gridy = 1;
        cons2.anchor = GridBagConstraints.EAST;
        cons2.insets = new Insets(2,2,2,2);
        holder.add(getButtonPanel(), cons2);
        setContentPane(holder);
        setSize(750,280);
        setVisible(true);
    }
    
    
    private CaDSRBrowserPanel getBrowserPanel() {
        if (browserPanel == null) {
            browserPanel = new CaDSRBrowserPanel(true, false);
            browserPanel.getCadsr().setText(PRODUCTION_CADSR_URL);
        }
        return browserPanel;
    }
    

    /**
     * This method initializes includePackageButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getIncludePackageButton() {
        if (includePackageButton == null) {
            includePackageButton = new JButton();
            includePackageButton.setText("Include Package");
            includePackageButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Project currentProject = getBrowserPanel().getSelectedProject();
                    if (getSelectedProjectTextField().getText().length() == 0
                        || getSelectedProjectTextField().getText().equals(currentProject.getShortName())) {
                        UMLPackageMetadata pack = getBrowserPanel().getSelectedPackage();
                        boolean okToAdd = true;
                        Vector items = new Vector();
                        for (int i = 0; okToAdd && i < getSelectedPackagesList().getModel().getSize(); i++) {
                            Object ithItem = getSelectedPackagesList().getModel().getElementAt(i);
                            if (ithItem.equals(pack.getName())) {
                                okToAdd = false;
                            }
                            items.add(ithItem);
                        }
                        if (okToAdd) {
                            items.add(pack.getName());
                            getSelectedPackagesList().setListData(items);
                        }
                        selectedProject = currentProject;
                        getSelectedProjectTextField().setText(currentProject.getShortName());
                    } else {
                        JOptionPane.showMessageDialog(CaDSRDomainModelExtractor.this, "Invalid project selection");
                    }
                }
            });
        }
        return includePackageButton;
    }


    /**
     * This method initializes removePackageButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getRemovePackageButton() {
        if (removePackageButton == null) {
            removePackageButton = new JButton();
            removePackageButton.setText("Remove Package");
            removePackageButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Object[] selection = getSelectedPackagesList().getSelectedValues();
                    Vector items = new Vector();
                    for (int i = 0; i < getSelectedPackagesList().getModel().getSize(); i++) {
                        items.add(getSelectedPackagesList().getModel().getElementAt(i));
                    }
                    for (Object removeMe : selection) {
                        items.remove(removeMe);
                    }
                    getSelectedPackagesList().setListData(items);
                    if (items.size() == 0) {
                        selectedProject = null;
                        getSelectedProjectTextField().setText("");
                    }
                }
            });
        }
        return removePackageButton;
    }


    /**
     * This method initializes selectedProjectLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getSelectedProjectLabel() {
        if (selectedProjectLabel == null) {
            selectedProjectLabel = new JLabel();
            selectedProjectLabel.setText("Selected Project:");
        }
        return selectedProjectLabel;
    }


    /**
     * This method initializes selectedProjectTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getSelectedProjectTextField() {
        if (selectedProjectTextField == null) {
            selectedProjectTextField = new JTextField();
            selectedProjectTextField.setEditable(false);
        }
        return selectedProjectTextField;
    }


    /**
     * This method initializes selectedPackagesList	
     * 	
     * @return javax.swing.JList	
     */
    private JList getSelectedPackagesList() {
        if (selectedPackagesList == null) {
            selectedPackagesList = new JList();
        }
        return selectedPackagesList;
    }


    /**
     * This method initializes selectedPackagesScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getSelectedPackagesScrollPane() {
        if (selectedPackagesScrollPane == null) {
            selectedPackagesScrollPane = new JScrollPane();
            selectedPackagesScrollPane.setViewportView(getSelectedPackagesList());
            selectedPackagesScrollPane.setBorder(
                BorderFactory.createTitledBorder(null, "Selected Packages", 
                    TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                    new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
        }
        return selectedPackagesScrollPane;
    }


    /**
     * This method initializes selectionInfoPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getSelectionInfoPanel() {
        if (selectionInfoPanel == null) {
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 1;
            gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.gridy = 2;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = GridBagConstraints.BOTH;
            gridBagConstraints2.gridy = 1;
            gridBagConstraints2.weightx = 1.0;
            gridBagConstraints2.weighty = 1.0D;
            gridBagConstraints2.gridwidth = 2;
            gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints2.gridx = 0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints1.gridx = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.gridy = 0;
            selectionInfoPanel = new JPanel();
            selectionInfoPanel.setLayout(new GridBagLayout());
            selectionInfoPanel.add(getSelectedProjectLabel(), gridBagConstraints);
            selectionInfoPanel.add(getSelectedProjectTextField(), gridBagConstraints1);
            selectionInfoPanel.add(getSelectedPackagesScrollPane(), gridBagConstraints2);
            selectionInfoPanel.add(getPackageSelectionPanel(), gridBagConstraints11);
        }
        return selectionInfoPanel;
    }


    /**
     * This method initializes packageSelectionPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getPackageSelectionPanel() {
        if (packageSelectionPanel == null) {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(1);
            gridLayout.setHgap(4);
            packageSelectionPanel = new JPanel();
            packageSelectionPanel.setLayout(gridLayout);
            packageSelectionPanel.add(getIncludePackageButton(), null);
            packageSelectionPanel.add(getRemovePackageButton(), null);
        }
        return packageSelectionPanel;
    }


    /**
     * This method initializes mainPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            GridLayout gridLayout1 = new GridLayout();
            gridLayout1.setRows(1);
            mainPanel = new JPanel();
            mainPanel.setLayout(gridLayout1);
            mainPanel.setSize(new Dimension(657, 288));
            mainPanel.add(getBrowserPanel(), null);
            mainPanel.add(getSelectionInfoPanel(), null);
        }
        return mainPanel;
    }


    /**
     * This method initializes saveButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getSaveButton() {
        if (saveButton == null) {
            saveButton = new JButton();
            saveButton.setText("Save Domain Model");
            saveButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileFilter(FileFilters.XML_FILTER);
                    int choice = chooser.showSaveDialog(CaDSRDomainModelExtractor.this);
                    if (choice == JFileChooser.APPROVE_OPTION) {
                        try {
                            CaDSRServiceClient client = new CaDSRServiceClient(getBrowserPanel().getCadsr().getText());
                            String[] packages = new String[getSelectedPackagesList().getModel().getSize()];
                            for (int i = 0; i < getSelectedPackagesList().getModel().getSize(); i++) {
                                packages[i] = (String) getSelectedPackagesList().getModel().getElementAt(i);
                            }
                            DomainModel model = client.generateDomainModelForPackages(selectedProject, packages);
                            FileWriter writer = new FileWriter(chooser.getSelectedFile());
                            MetadataUtils.serializeDomainModel(model, writer);
                            writer.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(CaDSRDomainModelExtractor.this,
                                "Error saving domain model: " + ex.getMessage());
                        }
                    }
                }
            });
        }
        return saveButton;
    }


    /**
     * This method initializes closeButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getCloseButton() {
        if (closeButton == null) {
            closeButton = new JButton();
            closeButton.setText("Close");
            closeButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    dispose();
                }
            });
        }
        return closeButton;
    }


    /**
     * This method initializes buttonPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridLayout gridLayout2 = new GridLayout();
            gridLayout2.setRows(1);
            gridLayout2.setColumns(2);
            gridLayout2.setHgap(4);
            buttonPanel = new JPanel();
            buttonPanel.setLayout(gridLayout2);
            buttonPanel.setSize(new Dimension(284, 60));
            buttonPanel.add(getSaveButton(), null);
            buttonPanel.add(getCloseButton(), null);
        }
        return buttonPanel;
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Error setting system look and feel");
        }
        JFrame frame = new CaDSRDomainModelExtractor();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
