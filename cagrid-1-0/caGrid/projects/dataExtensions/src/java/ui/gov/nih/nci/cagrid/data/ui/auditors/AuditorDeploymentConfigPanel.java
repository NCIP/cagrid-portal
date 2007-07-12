package gov.nih.nci.cagrid.data.ui.auditors;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.data.common.ExtensionDataManager;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.ServiceDeploymentUIPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/** 
 *  AuditorDeploymentConfigPanel
 *  Panel for deployment tiem configuration of Data Service auditors
 * 
 * @author David Ervin
 * 
 * @created May 24, 2007 1:34:32 PM
 * @version $Id: AuditorDeploymentConfigPanel.java,v 1.1 2007-07-12 17:20:52 dervin Exp $ 
 */
public class AuditorDeploymentConfigPanel extends ServiceDeploymentUIPanel {
    
    private AuditorsConfigurationPanel auditorsConfigPanel = null;
    private ExtensionDataManager extensionDataManager = null;
    
    public AuditorDeploymentConfigPanel(ServiceExtensionDescriptionType desc, ServiceInformation info) {
        super(desc, info);
        ExtensionTypeExtensionData extensionData = getExtensionTypeExtensionData();
        extensionDataManager = new ExtensionDataManager(extensionData);
        initialize();
    }
    
    
    private void initialize() {
        setLayout(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 0;
        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 1.0D;
        cons.weighty = 1.0D;
        add(getAuditorsConfigPanel(), cons);
    }
    

    public void resetGUI() {
        try {
            getAuditorsConfigPanel().updateDisplayedConfiguration();
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error displaying auditor configuration", 
                ex.getMessage(), ex);
        }
    }
    
    
    private AuditorsConfigurationPanel getAuditorsConfigPanel() {
        if (auditorsConfigPanel == null) {
            auditorsConfigPanel = new AuditorsConfigurationPanel(getServiceInfo(), extensionDataManager);
        }
        return auditorsConfigPanel;
    }
}
