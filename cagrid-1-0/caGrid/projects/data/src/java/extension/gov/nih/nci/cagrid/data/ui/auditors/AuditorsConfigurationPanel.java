package gov.nih.nci.cagrid.data.ui.auditors;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.data.service.auditing.DataServiceAuditor;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

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
 * @version $Id: AuditorsConfigurationPanel.java,v 1.1 2007-05-21 19:07:57 dervin Exp $ 
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
        
    }
    
    
    private AuditorsTable getAuditorsTable() {
        if (auditorsTable == null) {
            auditorsTable = new AuditorsTable();
        }
        return auditorsTable;
    }
    
    
    private MonitoredEventsPanel getMonitoredEventsPanel() {
        if (monitoredEventsPanel == null) {
            monitoredEventsPanel = new MonitoredEventsPanel();
            monitoredEventsPanel.setBorder(BorderFactory.createTitledBorder(
                null, "Monitored Events", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
        }
        return monitoredEventsPanel;
    }
    
    
    private AuditorConfigurationPropertiesTable getPropertiesTable() {
        if (propertiesTable == null) {
            propertiesTable = new AuditorConfigurationPropertiesTable();
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
            auditorSelectionPanel.addAuditorChangeLisener(new AuditorChangeListener() {
               public void auditorAdded(DataServiceAuditor auditor, String className, String instanceName) {
                   
               }
               
               
               public void auditorRemoved(String className, String instanceName) {
                   
               }
            });
        }
        return auditorSelectionPanel;
    }
}
