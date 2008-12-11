package org.cagrid.data.sdkquery41.style.wizard;

import gov.nih.nci.cagrid.data.ui.wizard.AbstractWizardPanel;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

/**
 * DomainModelPanel
 * Wizard panel to allow the service developer to select and view the
 * domain model which will be used by the grid data service.
 * 
 * @author David
 */
public class DomainModelPanel extends AbstractWizardPanel {
    
    private JPanel mainPanel = null;

    public DomainModelPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
        super(extensionDescription, info);
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
        // TODO: apply configutation
    }
    
    
    private void initialize() {
        
    }
    
    
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
        }
        return mainPanel;
    }
}
