package gov.nih.nci.cagrid.data.ui.auditors;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.ServiceDeploymentUIPanel;

/** 
 *  AuditorDeploymentConfigPanel
 *  Panel for deployment tiem configuration of Data Service auditors
 * 
 * @author David Ervin
 * 
 * @created May 24, 2007 1:34:32 PM
 * @version $Id: AuditorDeploymentConfigPanel.java,v 1.1 2007-05-25 14:34:25 dervin Exp $ 
 */
public class AuditorDeploymentConfigPanel extends ServiceDeploymentUIPanel {
    
    private AuditorsConfigurationPanel auditorsConfigPanel = null;
    
    public AuditorDeploymentConfigPanel(ServiceExtensionDescriptionType desc, ServiceInformation info) {
        super(desc, info);
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
        getAuditorsConfigPanel().forceReload();
    }
    
    
    private AuditorsConfigurationPanel getAuditorsConfigPanel() {
        if (auditorsConfigPanel == null) {
            auditorsConfigPanel = new AuditorsConfigurationPanel(getServiceInfo());
        }
        return auditorsConfigPanel;
    }
}
