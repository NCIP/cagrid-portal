package org.cagrid.data.sdkquery41.style.wizard;

import gov.nih.nci.cagrid.data.common.ExtensionDataManager;
import gov.nih.nci.cagrid.data.extension.CadsrInformation;
import gov.nih.nci.cagrid.data.ui.wizard.AbstractWizardPanel;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.cagrid.data.sdkquery41.style.wizard.model.ModelFromCaDSRPanel;
import org.cagrid.data.sdkquery41.style.wizard.model.ModelFromConfigPanel;
import org.cagrid.data.sdkquery41.style.wizard.model.ModelFromFileSystemPanel;
import org.cagrid.grape.utils.CompositeErrorDialog;

/**
 * DomainModelPanel
 * Wizard panel to allow the service developer to select and view the
 * domain model which will be used by the grid data service.
 * 
 * @author David
 */
public class DomainModelPanel extends AbstractWizardPanel {
    
    private JPanel modelSelectionPanel = null;
    private JPanel mainPanel = null;
    private JLabel modelSourceLabel = null;
    private JComboBox modelSourceComboBox = null;
    
    private SortedMap<String, DomainModelSourcePanel> domainModelSources = null;

    public DomainModelPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
        super(extensionDescription, info);
        domainModelSources = new TreeMap<String, DomainModelSourcePanel>();
        initialize();
    }


    public String getPanelShortName() {
        return "Domain Model";
    }


    public String getPanelTitle() {
        return "Domain Model selection";
    }


    public void update() {
        // TODO: update GUI from configuration
    }
    
    
    public void movingNext() {
        String selectedSourceName = (String) getModelSourceComboBox().getSelectedItem();
        DomainModelSourcePanel selectedSource = domainModelSources.get(selectedSourceName);
        try {
            CadsrInformation cadsrInfo = selectedSource.getCadsrDomainInformation();
            // set the cadsr info on the extension data model
            ExtensionDataManager manager = new ExtensionDataManager(getExtensionData());
            manager.storeCadsrInformation(cadsrInfo);
        } catch (Exception ex) {
            ex.printStackTrace();
            CompositeErrorDialog.showErrorDialog("Error obtaining domain model information", ex.getMessage(), ex);
        }
    }
    
    
    private void initialize() {
        populateModelPanels();
        setLayout(new GridLayout());
        add(getMainPanel());
    }
    
    
    private void populateModelPanels() {
        DomainModelSourceValidityListener validityListener = new DomainModelSourceValidityListener() {
            public void domainModelSourceValid(DomainModelSourcePanel source, boolean valid) {
                String selectedSourceName = (String) getModelSourceComboBox().getSelectedItem();
                DomainModelSourcePanel selectedSource = domainModelSources.get(selectedSourceName);
                if (selectedSource == source) {
                    setNextEnabled(valid);
                }
            }
        };
        
        DomainModelSourcePanel cadsrSourcePanel = new ModelFromCaDSRPanel(validityListener);
        DomainModelSourcePanel configSourcePanel = new ModelFromConfigPanel(validityListener);
        DomainModelSourcePanel fileSourcePanel = new ModelFromFileSystemPanel(validityListener);
        domainModelSources.put(cadsrSourcePanel.getName(), cadsrSourcePanel);
        domainModelSources.put(configSourcePanel.getName(), configSourcePanel);
        domainModelSources.put(fileSourcePanel.getName(), fileSourcePanel);
        
        // add the model source panels to the combo box and display panel
        for (String name : domainModelSources.keySet()) {
            getModelSourceComboBox().addItem(name);
            getModelSelectionPanel().add(domainModelSources.get(name), name);
        }
    }
    
    
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.fill = GridBagConstraints.BOTH;
            gridBagConstraints2.gridwidth = 2;
            gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints2.weightx = 1.0D;
            gridBagConstraints2.weighty = 1.0D;
            gridBagConstraints2.gridy = 1;
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
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.setSize(new Dimension(414, 145));
            mainPanel.add(getModelSourceLabel(), gridBagConstraints);
            mainPanel.add(getModelSourceComboBox(), gridBagConstraints1);
            mainPanel.add(getModelSelectionPanel(), gridBagConstraints2);
        }
        return mainPanel;
    }
    
    
    private JPanel getModelSelectionPanel() {
        if (modelSelectionPanel == null) {
            modelSelectionPanel = new JPanel();
            modelSelectionPanel.setLayout(new CardLayout());
            modelSelectionPanel.setBorder(BorderFactory.createTitledBorder(null, "Domain Model Source", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
        }
        return modelSelectionPanel;
    }


    /**
     * This method initializes modelSourceLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getModelSourceLabel() {
        if (modelSourceLabel == null) {
            modelSourceLabel = new JLabel();
            modelSourceLabel.setText("Domain Model Source:");
        }
        return modelSourceLabel;
    }


    /**
     * This method initializes modelSourceComboBox	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getModelSourceComboBox() {
        if (modelSourceComboBox == null) {
            modelSourceComboBox = new JComboBox();
            modelSourceComboBox.setToolTipText("Select the domain model source");
            modelSourceComboBox.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    System.out.println("Selected model source " + 
                        getModelSourceComboBox().getSelectedItem().toString());
                    ((CardLayout) getModelSelectionPanel().getLayout()).show(
                        getModelSelectionPanel(), 
                        getModelSourceComboBox().getSelectedItem().toString());
                }
            });
        }
        return modelSourceComboBox;
    }
}
