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
import gov.nih.nci.cagrid.data.common.ExtensionDataManager;
import gov.nih.nci.cagrid.data.service.auditing.DataServiceAuditor;
import gov.nih.nci.cagrid.data.ui.DataServiceModificationSubPanel;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

/** 
 *  AuditorsConfigurationPanel
 *  Panel for configuring data service auditors
 * 
 * @author David Ervin
 * 
 * @created May 21, 2007 10:40:27 AM
 * @version $Id: AuditorsConfigurationPanel.java,v 1.1 2007-07-12 17:20:52 dervin Exp $ 
 */
public class AuditorsConfigurationPanel extends DataServiceModificationSubPanel {

    private AuditorsTable auditorsTable = null;
    private JScrollPane auditorsTableScrollPane = null;
    private AuditorSelectionPanel auditorSelectionPanel = null;
    
    public AuditorsConfigurationPanel(ServiceInformation serviceInfo, ExtensionDataManager dataManager) {
        super(serviceInfo, dataManager);
        initialize();
    }
    
    
    public void updateDisplayedConfiguration() throws Exception {
        AuditorConfiguration[] configs = getAuditorsDescription().getAuditorConfiguration();
        if (configs != null) {
            for (AuditorConfiguration config : configs) {
                getAuditorsTable().addAuditor(config.getClassName(), config.getInstanceName());
            }
        }
    }
    
    
    private void initialize() {
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.weighty = 1.0;
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
    }
    
    
    private AuditorsTable getAuditorsTable() {
        if (auditorsTable == null) {
            auditorsTable = new AuditorsTable();
            auditorsTable.addAuditorChangeListener(new AuditorChangeListener() {
                public void auditorConfigureButtonClicked(String className, String instanceName) {
                    new AuditorConfigurationDialog(getServiceInfo(), className, instanceName);
                }
                
                
                public void auditorRemoveButtonClicked(String className, String instanceName) {
                    // remove the auditor from the configuration file
                    try {
                        DataServiceAuditors auditors = getAuditorsDescription();
                        List<AuditorConfiguration> keptConfigurations = 
                            new ArrayList(auditors.getAuditorConfiguration().length - 1);
                        for (AuditorConfiguration config : auditors.getAuditorConfiguration()) {
                            if (!(config.getClassName().equals(className) 
                                && config.getInstanceName().equals(instanceName))) {
                                keptConfigurations.add(config);
                            }
                        }
                        AuditorConfiguration[] config = new AuditorConfiguration[keptConfigurations.size()];
                        keptConfigurations.toArray(config);
                        auditors.setAuditorConfiguration(config);
                        storeAuditorsDescription(auditors);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog("Error removing auditor", ex.getMessage(), ex);
                    }
                    // remove the auditor from the UI
                    getAuditorsTable().removeAuditor(className, instanceName);                    
                }
            });
        }
        return auditorsTable;
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


    private AuditorSelectionPanel getAuditorSelectionPanel() {
        if (auditorSelectionPanel == null) {
            auditorSelectionPanel = new AuditorSelectionPanel(getServiceInfo().getBaseDirectory());
            auditorSelectionPanel.setBorder(BorderFactory.createTitledBorder(
                null, "Select Auditors", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
            auditorSelectionPanel.addAuditorAdditionListener(new AuditorAdditionListener() {
               public void auditorAdded(DataServiceAuditor auditor, String className, String instanceName) {
                   addAuditor(auditor, className, instanceName);
               }
            });
        }
        return auditorSelectionPanel;
    }
    
    
    private DataServiceAuditors getAuditorsDescription() throws Exception {
        // first, locate the data service auditors file
        String filename = null;
        if (CommonTools.servicePropertyExists(getServiceInfo().getServiceDescriptor(), 
            DataServiceConstants.DATA_SERVICE_AUDITORS_CONFIG_FILE_PROPERTY)) {
            filename = getServiceInfo().getBaseDirectory().getAbsolutePath() + File.separator 
                + "etc" + File.separator + CommonTools.getServicePropertyValue(
                    getServiceInfo().getServiceDescriptor(),
                    DataServiceConstants.DATA_SERVICE_AUDITORS_CONFIG_FILE_PROPERTY);
        } else {
            // no property defined, add it and store the default filename
            CommonTools.setServiceProperty(getServiceInfo().getServiceDescriptor(),
                DataServiceConstants.DATA_SERVICE_AUDITORS_CONFIG_FILE_PROPERTY, 
                DataServiceConstants.DATA_SERVICE_AUDITORS_CONFIG_FILE_NAME, true);
            filename = getServiceInfo().getBaseDirectory().getAbsolutePath() + File.separator 
                + "etc" + File.separator + CommonTools.getServicePropertyValue(
                    getServiceInfo().getServiceDescriptor(),
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
        String filename = getServiceInfo().getBaseDirectory().getAbsolutePath() + File.separator 
            + "etc" + File.separator + CommonTools.getServicePropertyValue(
                getServiceInfo().getServiceDescriptor(),
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
