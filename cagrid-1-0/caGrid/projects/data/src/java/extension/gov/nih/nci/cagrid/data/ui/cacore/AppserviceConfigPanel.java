package gov.nih.nci.cagrid.data.ui.cacore;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.data.ui.cacore.appservice.Appservice31ConfigPanel;
import gov.nih.nci.cagrid.data.ui.cacore.appservice.Appservice32ConfigPanel;
import gov.nih.nci.cagrid.data.ui.cacore.appservice.BaseAppserviceConfigPanel;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


/** 
 *  AppserviceConfigPanel
 *  Panel for configuring the application service used by the
 *  data service
 * 
 * @author David Ervin
 * 
 * @created Mar 23, 2007 3:35:47 PM
 * @version $Id: AppserviceConfigPanel.java,v 1.8 2007-03-28 17:33:11 dervin Exp $ 
 */
public class AppserviceConfigPanel extends AbstractWizardPanel {

    private JPanel configHolderPanel = null;
    
    private Map<String, Class> appserviceConfigPanels;
    
    public AppserviceConfigPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
        super(extensionDescription, info);
        initialize();
    }
    
    
    private Class getAppserviceConfigPanel() {
        if (appserviceConfigPanels == null) {
            appserviceConfigPanels = new HashMap<String, Class>();
            appserviceConfigPanels.put(CoreDsIntroPanel.CACORE_31_VERSION, Appservice31ConfigPanel.class);
            appserviceConfigPanels.put(CoreDsIntroPanel.CACORE_32_VERSION, Appservice32ConfigPanel.class);
            // configuration for future caCORE versions can be added here
        }
        String coreVersion = (String) getBitBucket().get(CoreDsIntroPanel.CACORE_VERSION_PROPERTY);
        return appserviceConfigPanels.get(coreVersion);
    }
    
    
    private void initialize() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0D;
        gridBagConstraints.weighty = 1.0D;
        gridBagConstraints.gridx = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(470, 215));
        this.add(getConfigHolderPanel(), gridBagConstraints);
    }


    public String getPanelShortName() {
        return "Configuration";
    }


    public String getPanelTitle() {
        return "caCORE Application Service Configuration";
    }


    public void update() {
        populateConfigHolderPanel();
    }


    /**
     * This method initializes configHolderPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getConfigHolderPanel() {
        if (configHolderPanel == null) {
            configHolderPanel = new JPanel();
            configHolderPanel.setLayout(new GridBagLayout());
            configHolderPanel.setBorder(BorderFactory.createTitledBorder(
                null, "Application Service Instance Configuration", 
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                null, PortalLookAndFeel.getPanelLabelColor()));
        }
        return configHolderPanel;
    }
    
    
    private void populateConfigHolderPanel() {
        // clean out any contents in the holder
        while (getConfigHolderPanel().getComponentCount() > 0) {
            getConfigHolderPanel().remove(0);
        }
        
        // get the class of the panel for the currently selected SDK version
        Class panelClass = getAppserviceConfigPanel();
        Class[] constructorParamTypes = new Class[] {
            ExtensionTypeExtensionData.class, ServiceInformation.class
        };
        try {
            // get the constructor for the panel
            Constructor panelConstructor = panelClass.getConstructor(constructorParamTypes);
            // create a new instance of the panel
            Object[] panelArgs = new Object[] {
                getExtensionData(), getServiceInformation()
            };
            BaseAppserviceConfigPanel panel = (BaseAppserviceConfigPanel) panelConstructor.newInstance(panelArgs);
            // add the panel to the config holder panel
            GridBagConstraints cons = new GridBagConstraints();
            cons.gridx = 0;
            cons.gridy = 0;
            cons.fill = GridBagConstraints.BOTH;
            cons.weightx = 1.0d;
            cons.weighty = 1.0d;
            getConfigHolderPanel().add(panel, cons);
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog(
                "Error constructing application service config panel", ex);            
        }
    }
}
