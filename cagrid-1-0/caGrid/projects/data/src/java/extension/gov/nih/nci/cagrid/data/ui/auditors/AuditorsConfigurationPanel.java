package gov.nih.nci.cagrid.data.ui.auditors;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.auditing.AuditorConfiguration;
import gov.nih.nci.cagrid.data.auditing.AuditorConfigurationConfigurationProperties;
import gov.nih.nci.cagrid.data.auditing.ConfigurationProperty;
import gov.nih.nci.cagrid.data.auditing.DataServiceAuditors;
import gov.nih.nci.cagrid.data.auditing.MonitoredEvents;
import gov.nih.nci.cagrid.data.service.auditing.DataServiceAuditor;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

/** 
 *  AuditorsConfigurationPanel
 *  Panel for configuring data service auditors
 * 
 * @author David Ervin
 * 
 * @created May 21, 2007 10:40:27 AM
 * @version $Id: AuditorsConfigurationPanel.java,v 1.3 2007-05-25 14:34:47 dervin Exp $ 
 */
public class AuditorsConfigurationPanel extends JPanel {

    private AuditorsTable auditorsTable = null;
    private MonitoredEventsPanel monitoredEventsPanel = null;
    private AuditorConfigurationPropertiesTable propertiesTable = null;
    private JScrollPane auditorsTableScrollPane = null;
    private JScrollPane propertiesTableScrollPane = null;
    private AuditorSelectionPanel auditorSelectionPanel = null;
    
    private ServiceInformation serviceInfo;
    
    public AuditorsConfigurationPanel(ServiceInformation serviceInfo) {
        super();
        this.serviceInfo = serviceInfo;
        initialize();
    }
    
    
    public void forceReload() {
        // TODO: reload everything from the configuration file
    }
    
    
    private void initialize() {
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 3;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.fill = GridBagConstraints.BOTH;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.weightx = 1.0;
        gridBagConstraints2.weighty = 1.0D;
        gridBagConstraints2.gridx = 0;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.weighty = 0.0D;
        gridBagConstraints1.ipady = 100;
        gridBagConstraints1.gridx = 0;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weightx = 1.0D;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(570, 460));
        this.add(getAuditorSelectionPanel(), gridBagConstraints);
        this.add(getAuditorsTableScrollPane(), gridBagConstraints1);
        this.add(getPropertiesTableScrollPane(), gridBagConstraints2);
        this.add(getMonitoredEventsPanel(), gridBagConstraints3);
        PortalUtils.setContainerEnabled(getMonitoredEventsPanel(), false);
    }
    
    
    private AuditorsTable getAuditorsTable() {
        if (auditorsTable == null) {
            auditorsTable = new AuditorsTable();
            auditorsTable.addAuditorChangeListener(new AuditorChangeListener() {
                public void auditorSelectionChanged(String className, String instanceName) {
                    boolean validSelection = (className != null) && (instanceName != null);
                    PortalUtils.setContainerEnabled(getMonitoredEventsPanel(), validSelection);
                    
                    if (validSelection) {
                        File libDir = new File(serviceInfo.getBaseDirectory().getAbsolutePath() + File.separator + "lib");
                        try {
                            DataServiceAuditor auditor = AuditorsLoader.loadAuditor(libDir, className);

                            // dig up the properties for this auditor and put them in
                            // the auditor configuration table
                            Properties auditorDefaultProps = auditor.getDefaultConfigurationProperties();
                            DataServiceAuditors auditors = getAuditorsDescription();
                            AuditorConfigurationConfigurationProperties configProps = null;
                            MonitoredEvents monitoredEvents = null;
                            for (AuditorConfiguration config : auditors.getAuditorConfiguration()) {
                                if (config.getClassName().equals(className)
                                    && config.getInstanceName().equals(instanceName)) {
                                    configProps = config.getConfigurationProperties();
                                    monitoredEvents = config.getMonitoredEvents();
                                    if (monitoredEvents == null) {
                                        monitoredEvents = new MonitoredEvents();
                                        config.setMonitoredEvents(monitoredEvents);
                                    }
                                    break;
                                }
                            }
                            getPropertiesTable().setConfigurationProperties(configProps, auditorDefaultProps);

                            // set the monitored events
                            getMonitoredEventsPanel().setMonitoredEvents(monitoredEvents);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            ErrorDialog.showErrorDialog(
                                "Error loading properties for auditor " + className + " : " + instanceName, 
                                ex.getMessage(), ex);
                        }
                    }
                }
            });
        }
        return auditorsTable;
    }
    
    
    private MonitoredEventsPanel getMonitoredEventsPanel() {
        if (monitoredEventsPanel == null) {
            monitoredEventsPanel = new MonitoredEventsPanel();
            monitoredEventsPanel.setBorder(BorderFactory.createTitledBorder(
                null, "Monitored Events", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
            monitoredEventsPanel.addMonitoredEventsChangeListener(new MonitoredEventsChangeListener() {
                public void monitoredEventsChanged() {
                    try {
                        String selectedClassName = getAuditorsTable().getSelectedClassName();
                        String selectedInstanceName = getAuditorsTable().getSelectedInstanceName();
                        DataServiceAuditors auditors = getAuditorsDescription();
                        AuditorConfiguration selectedAuditor = null;
                        for (AuditorConfiguration config : auditors.getAuditorConfiguration()) {
                            if (config.getClassName().equals(selectedClassName)
                                && config.getInstanceName().equals(selectedInstanceName)) {
                                selectedAuditor = config;
                                break;
                            }
                        }
                        selectedAuditor.setMonitoredEvents(
                            getMonitoredEventsPanel().getMonitoredEvents());
                        storeAuditorsDescription(auditors);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog("Error storing monitored events", ex.getMessage(), ex);
                    }
                }
            });
        }
        return monitoredEventsPanel;
    }
    
    
    private AuditorConfigurationPropertiesTable getPropertiesTable() {
        if (propertiesTable == null) {
            propertiesTable = new AuditorConfigurationPropertiesTable();
            propertiesTable.addAuditorPropertyListener(new AuditorPropertyChangeListener() {
                public void propertyValueEdited(String key, String newValue) {
                    // store the property
                    try {
                        String className = getAuditorsTable().getSelectedClassName();
                        String instanceName = getAuditorsTable().getSelectedInstanceName();
                        DataServiceAuditors auditors = getAuditorsDescription();
                        for (AuditorConfiguration config : auditors.getAuditorConfiguration()) {
                            if (config.getClassName().equals(className)
                                && config.getInstanceName().equals(instanceName)) {
                                AuditorConfigurationConfigurationProperties props =
                                    config.getConfigurationProperties();
                                for (ConfigurationProperty prop : props.getProperty()) {
                                    if (prop.getKey().equals(key)) {
                                        prop.setValue(newValue);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        storeAuditorsDescription(auditors);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog("Error storing changed property", ex.getMessage(), ex);
                    }
                }
            });
        }
        return propertiesTable;
    }


    /**
     * This method initializes auditorsTableScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getAuditorsTableScrollPane() {
        if (auditorsTableScrollPane == null) {
            auditorsTableScrollPane = new JScrollPane();
            auditorsTableScrollPane.setBorder(BorderFactory.createTitledBorder(
                null, "Auditors", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
            auditorsTableScrollPane.setViewportView(getAuditorsTable());
        }
        return auditorsTableScrollPane;
    }


    /**
     * This method initializes propertiesTableScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getPropertiesTableScrollPane() {
        if (propertiesTableScrollPane == null) {
            propertiesTableScrollPane = new JScrollPane();
            propertiesTableScrollPane.setBorder(BorderFactory.createTitledBorder(
                null, "Auditor Properties", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
            propertiesTableScrollPane.setViewportView(getPropertiesTable());
        }
        return propertiesTableScrollPane;
    }
    
    
    private AuditorSelectionPanel getAuditorSelectionPanel() {
        if (auditorSelectionPanel == null) {
            auditorSelectionPanel = new AuditorSelectionPanel(serviceInfo.getBaseDirectory());
            auditorSelectionPanel.setBorder(BorderFactory.createTitledBorder(
                null, "Select Auditors", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
            auditorSelectionPanel.addAuditorAdditionListener(new AuditorAdditionListener() {
               public void auditorAdded(DataServiceAuditor auditor, String className, String instanceName) {
                   addAuditor(auditor, className, instanceName);
               }
               
               
               public void auditorRemoved(String className, String instanceName) {
                   try {
                       // remove the auditor from the auditors description file
                       DataServiceAuditors auditorsDescription = getAuditorsDescription();
                       if (auditorExists(auditorsDescription, className, instanceName)) {
                           for (AuditorConfiguration config : auditorsDescription.getAuditorConfiguration()) {
                               if (config.getClassName().equals(className)
                                   && config.getInstanceName().equals(instanceName)) {
                                   AuditorConfiguration[] cleanedUp = 
                                       (AuditorConfiguration[]) Utils.removeFromArray(
                                           auditorsDescription.getAuditorConfiguration(), config);
                                   auditorsDescription.setAuditorConfiguration(cleanedUp);
                                   break;
                               }
                           }
                           // save the edited configuration
                           storeAuditorsDescription(auditorsDescription);
                       } else {
                           PortalUtils.showMessage("Auditor " + className + " : " + instanceName + " not found.");
                       }
                   } catch (Exception ex) {
                       ex.printStackTrace();
                       ErrorDialog.showErrorDialog("Error removing auditor", ex.getMessage(), ex);
                   }
                   // remove the auditor from the GUI
                   getAuditorsTable().removeAuditor(className, instanceName);
                   // clear out the properties table
                   getPropertiesTable().clearTable();
               }
            });
        }
        return auditorSelectionPanel;
    }
    
    
    private DataServiceAuditors getAuditorsDescription() throws Exception {
        // first, locate the data service auditors file
        String filename = null;
        if (CommonTools.servicePropertyExists(serviceInfo.getServiceDescriptor(), 
            DataServiceConstants.DATA_SERVICE_AUDITORS_CONFIG_FILE_PROPERTY)) {
            filename = serviceInfo.getBaseDirectory().getAbsolutePath() + File.separator 
                + "etc" + File.separator + CommonTools.getServicePropertyValue(
                    serviceInfo.getServiceDescriptor(),
                    DataServiceConstants.DATA_SERVICE_AUDITORS_CONFIG_FILE_PROPERTY);
        } else {
            // no property defined, add it and store the default filename
            CommonTools.setServiceProperty(serviceInfo.getServiceDescriptor(),
                DataServiceConstants.DATA_SERVICE_AUDITORS_CONFIG_FILE_PROPERTY, 
                DataServiceConstants.DATA_SERVICE_AUDITORS_CONFIG_FILE_NAME, true);
            filename = serviceInfo.getBaseDirectory().getAbsolutePath() + File.separator 
                + "etc" + File.separator + CommonTools.getServicePropertyValue(
                    serviceInfo.getServiceDescriptor(),
                    DataServiceConstants.DATA_SERVICE_AUDITORS_CONFIG_FILE_PROPERTY);
            // create the document, since it doesn't exist
            DataServiceAuditors tmpAuditors = new DataServiceAuditors();
            Utils.serializeDocument(filename, tmpAuditors, 
                DataServiceConstants.DATA_SERVICE_AUDITORS_QNAME);
        }
        // deserialize that document
        DataServiceAuditors auditorsConfig = (DataServiceAuditors) Utils.deserializeDocument(
            filename, DataServiceAuditors.class);
        return auditorsConfig;
    }
    
    
    private void storeAuditorsDescription(DataServiceAuditors auditors) throws Exception {
        String filename = serviceInfo.getBaseDirectory().getAbsolutePath() + File.separator 
            + "etc" + File.separator + CommonTools.getServicePropertyValue(
                serviceInfo.getServiceDescriptor(),
                DataServiceConstants.DATA_SERVICE_AUDITORS_CONFIG_FILE_PROPERTY);
        Utils.serializeDocument(filename, auditors, DataServiceConstants.DATA_SERVICE_AUDITORS_QNAME);
    }
    
    
    private boolean auditorExists(DataServiceAuditors auditors, String className, String instanceName) {
        if (auditors.getAuditorConfiguration() != null) {
            for (AuditorConfiguration config : auditors.getAuditorConfiguration()) {
                if (config.getClassName().equals(className)
                    && config.getInstanceName().equals(instanceName)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    
    private void addAuditor(DataServiceAuditor auditor, String className, String instanceName) {
        // add the auditor to the auditors description
        try {
            DataServiceAuditors auditorsDescription = getAuditorsDescription();
            if (!auditorExists(auditorsDescription, className, instanceName)) {
                AuditorConfiguration auditorConfig = new AuditorConfiguration();
                auditorConfig.setClassName(className);
                auditorConfig.setInstanceName(instanceName);
                Properties defaultProperties = auditor.getDefaultConfigurationProperties();
                ConfigurationProperty[] configedProperties = 
                    new ConfigurationProperty[defaultProperties.size()];
                int index = 0;
                Enumeration propertyKeys = defaultProperties.keys();
                while (propertyKeys.hasMoreElements()) {
                    String key = (String) propertyKeys.nextElement();
                    String value = defaultProperties.getProperty(key);
                    configedProperties[index] = new ConfigurationProperty(key, value);
                    index++;
                }
                AuditorConfigurationConfigurationProperties auditorConfigProps = 
                    new AuditorConfigurationConfigurationProperties(configedProperties);
                auditorConfig.setConfigurationProperties(auditorConfigProps);
                if (auditorsDescription.getAuditorConfiguration() == null) {
                    auditorsDescription.setAuditorConfiguration(
                        new AuditorConfiguration[] {auditorConfig});
                } else {
                    auditorsDescription.setAuditorConfiguration(
                        (AuditorConfiguration[]) Utils.appendToArray(
                            auditorsDescription.getAuditorConfiguration(), auditorConfig)); 
                }
                auditorConfig.setMonitoredEvents(new MonitoredEvents());
                // store it back to the file
                storeAuditorsDescription(auditorsDescription);

                // add the auditor to the GUI
                getAuditorsTable().addAuditor(className, instanceName);
            } else {
                PortalUtils.showMessage("Auditor " + className + " : " + instanceName + " already exists");
            }                       
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error adding auditor", ex.getMessage(), ex);
        }
    }
}
