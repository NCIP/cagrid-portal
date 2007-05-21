package gov.nih.nci.cagrid.data.ui.auditors;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.data.service.auditing.DataServiceAuditor;
import gov.nih.nci.cagrid.introduce.common.FileFilters;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
 * @version $Id: AuditorSelectionPanel.java,v 1.1 2007-05-21 19:07:57 dervin Exp $ 
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
    private List<AuditorChangeListener> auditorChangeListeners = null;    


    public AuditorSelectionPanel(File serviceBaseDir) {
        super();
        this.serviceBaseDir = serviceBaseDir;
        auditorChangeListeners = new LinkedList();
        initialize();
    }
    
    
    public void addAuditorChangeLisener(AuditorChangeListener listener) {
        auditorChangeListeners.add(listener);
    }
    
    
    public boolean removeAuditorChangeListener(AuditorChangeListener listener) {
        return auditorChangeListeners.remove(listener);
    }
    
    
    public AuditorChangeListener[] getAuditorChangeListeners() {
        AuditorChangeListener[] listeners = 
            new AuditorChangeListener[auditorChangeListeners.size()];
        auditorChangeListeners.toArray(listeners);
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
        // list jars from the service lib dir as URLs
        File libDir = new File(serviceBaseDir.getAbsolutePath() + File.separator + "lib");
        List jarFiles = Utils.recursiveListFiles(libDir, new FileFilters.JarFileFilter());
        List<URL> jarUrls = new LinkedList();
        for (int i = 0; i < jarFiles.size(); i++) {
            File jarFile = (File) jarFiles.get(i);
            if (jarFile.isFile()) {
                jarUrls.add(jarFile.toURL());
            }
        }
        URL[] urlArray = new URL[jarUrls.size()];
        jarUrls.toArray(urlArray);
        
        // load all subclasses of DataServiceAuditor
        List<Class> subclasses = new LinkedList();
        ClassLoader loader = new URLClassLoader(urlArray, getClass().getClassLoader());
        Class baseClass = DataServiceAuditor.class;
        for (int i = 0; i < jarFiles.size(); i++) {
            JarFile jar = new JarFile((File) jarFiles.get(i));
            Enumeration jarEntries = jar.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry entry = (JarEntry) jarEntries.nextElement();
                String name = entry.getName();
                if (name.endsWith(".class")) {
                    name = name.replace('/', '.');
                    name = name.substring(0, name.length() - 6);
                    Class loadedClass = null;
                    try {
                        loadedClass = loader.loadClass(name);
                    } catch (Throwable e) {
                        // theres a lot of these...
                        // System.err.println("Error loading class (" + name
                        // + "):" + e.getMessage());
                    }
                    if (loadedClass != null && baseClass.isAssignableFrom(loadedClass)) {
                        subclasses.add(loadedClass);
                    }
                }
            }
        }
        
        // add the classes to the drop-down
        for (Class c : subclasses) {
            getAuditorClassComboBox().addItem(c);
        }
    }
    
    
    protected void fireAuditorAdded() {
        if (auditorChangeListeners.size() != 0) {
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
            
            for (AuditorChangeListener listener : auditorChangeListeners) {
                listener.auditorAdded(auditor, auditorClass.getName(), getInstanceNameTextField().getText());
            }
        }
    }
    
    
    protected void fireAuditorRemoved() {
        for (AuditorChangeListener listener : auditorChangeListeners) {
            listener.auditorRemoved(
                getAuditorClassComboBox().getSelectedItem().toString(), 
                getInstanceNameTextField().getText());
        }
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
