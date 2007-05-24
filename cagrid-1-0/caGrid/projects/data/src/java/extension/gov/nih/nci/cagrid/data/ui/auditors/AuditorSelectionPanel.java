package gov.nih.nci.cagrid.data.ui.auditors;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.data.service.auditing.DataServiceAuditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** 
 *  AuditorSelectionPanel
 *  Panel to select / add / remove auditors
 * 
 * @author David Ervin
 * 
 * @created May 21, 2007 11:38:54 AM
 * @version $Id: AuditorSelectionPanel.java,v 1.2 2007-05-24 16:11:22 dervin Exp $ 
 */
public class AuditorSelectionPanel extends JPanel {

    private JComboBox auditorClassComboBox = null;
    private JLabel auditorClassLabel = null;
    private JLabel instanceNameLabel = null;
    private JTextField instanceNameTextField = null;
    private JPanel buttonPanel = null;
    private JButton addButton = null;
    private JButton removeButton = null;
    
    private File serviceBaseDir = null;
    private List<AuditorAdditionListener> auditorAdditionListeners = null;    


    public AuditorSelectionPanel(File serviceBaseDir) {
        super();
        this.serviceBaseDir = serviceBaseDir;
        auditorAdditionListeners = new LinkedList();
        initialize();
    }
    
    
    public void addAuditorAdditionListener(AuditorAdditionListener listener) {
        auditorAdditionListeners.add(listener);
    }
    
    
    public boolean removeAuditorAdditionListener(AuditorAdditionListener listener) {
        return auditorAdditionListeners.remove(listener);
    }
    
    
    public AuditorAdditionListener[] getAuditorAdditionListeners() {
        AuditorAdditionListener[] listeners = 
            new AuditorAdditionListener[auditorAdditionListeners.size()];
        auditorAdditionListeners.toArray(listeners);
        return listeners;
    }
    
    
    public void setSelectedAuditor(String className, String instanceName) {
        for (int i = 0; i < getAuditorClassComboBox().getItemCount(); i++) {
            Class classInCombo = (Class) getAuditorClassComboBox().getItemAt(i);
            if (classInCombo.getName().equals(className)) {
                getAuditorClassComboBox().setSelectedIndex(i);
                break;
            }
        }
        getInstanceNameTextField().setText(instanceName);
    }
    
    
    private void initialize() {
        try {
            populateClassDropdown();
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog(
                "Error loading classes for data service auditor selection", ex.getMessage(), ex);
        }
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.gridy = 2;
        gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.fill =  GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.weightx = 1.0;
        gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.weightx = 1.0;
        gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(361, 116));
        this.add(getAuditorClassLabel(), gridBagConstraints);
        this.add(getInstanceNameLabel(), gridBagConstraints1);
        this.add(getAuditorClassComboBox(), gridBagConstraints2);
        this.add(getInstanceNameTextField(), gridBagConstraints3);
        this.add(getButtonPanel(), gridBagConstraints4);
    }


    /**
     * This method initializes auditorClassComboBox	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getAuditorClassComboBox() {
        if (auditorClassComboBox == null) {
            auditorClassComboBox = new JComboBox();
            auditorClassComboBox.setRenderer(new DefaultListCellRenderer() {
                public Component getListCellRendererComponent(
                    JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Class) {
                        setText(((Class) value).getName());
                    }
                    return this;
                }
            });
        }
        return auditorClassComboBox;
    }


    /**
     * This method initializes auditorClassLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getAuditorClassLabel() {
        if (auditorClassLabel == null) {
            auditorClassLabel = new JLabel();
            auditorClassLabel.setText("Auditor:");
        }
        return auditorClassLabel;
    }


    /**
     * This method initializes instanceNameLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getInstanceNameLabel() {
        if (instanceNameLabel == null) {
            instanceNameLabel = new JLabel();
            instanceNameLabel.setText("Instance Name:");
        }
        return instanceNameLabel;
    }


    /**
     * This method initializes instanceNameTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getInstanceNameTextField() {
        if (instanceNameTextField == null) {
            instanceNameTextField = new JTextField();
        }
        return instanceNameTextField;
    }


    /**
     * This method initializes buttonPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(1);
            gridLayout.setHgap(2);
            gridLayout.setColumns(2);
            buttonPanel = new JPanel();
            buttonPanel.setLayout(gridLayout);
            buttonPanel.add(getAddButton(), null);
            buttonPanel.add(getRemoveButton(), null);
        }
        return buttonPanel;
    }


    /**
     * This method initializes addButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getAddButton() {
        if (addButton == null) {
            addButton = new JButton();
            addButton.setText("Add Auditor");
            addButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    fireAuditorAdded();
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
            removeButton.setText("Remove Auditor");
            removeButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    fireAuditorRemoved();
                }
            });
        }
        return removeButton;
    }
    
    
    private void populateClassDropdown() throws MalformedURLException, IOException {
        File libDir = new File(serviceBaseDir.getAbsolutePath() + File.separator + "lib");
        List<Class> auditorClassses = AuditorsLoader.getAvailableAuditorClasses(libDir);
        
        // add the classes to the drop-down
        for (Class c : auditorClassses) {
            getAuditorClassComboBox().addItem(c);
        }
    }
    
    
    protected void fireAuditorAdded() {
        if (auditorAdditionListeners.size() != 0) {
            Class auditorClass = (Class) getAuditorClassComboBox().getSelectedItem();
            DataServiceAuditor auditor = null;
            try {
                auditor = (DataServiceAuditor) auditorClass.newInstance();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
                ErrorDialog.showErrorDialog("Error instantiating selected auditor", ex.getMessage(), ex);
                return;
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                ErrorDialog.showErrorDialog("Error accessing auditor's constructpr", ex.getMessage(), ex);
                return;
            }
            
            for (AuditorAdditionListener listener : auditorAdditionListeners) {
                listener.auditorAdded(auditor, auditorClass.getName(), getInstanceNameTextField().getText());
            }
        }
    }
    
    
    protected void fireAuditorRemoved() {
        for (AuditorAdditionListener listener : auditorAdditionListeners) {
            listener.auditorRemoved(
                ((Class) getAuditorClassComboBox().getSelectedItem()).getName(),
                getInstanceNameTextField().getText());
        }
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
