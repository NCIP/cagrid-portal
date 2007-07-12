package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.data.common.ExtensionDataManager;
import gov.nih.nci.cagrid.data.extension.ClassMapping;
import gov.nih.nci.cagrid.data.style.ServiceStyleContainer;
import gov.nih.nci.cagrid.data.style.ServiceStyleLoader;
import gov.nih.nci.cagrid.data.ui.auditors.AuditorsConfigurationPanel;
import gov.nih.nci.cagrid.data.ui.domain.DomainModelConfigPanel;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.ServiceModificationUIPanel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JTabbedPane;


/**
 * DataServiceModificationPanel 
 * Panel for configuring a caGrid data service from
 * within the Introduce Toolkit
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @created Oct 10, 2006
 * @version $Id: DataServiceModificationPanel.java,v 1.1 2007-07-12 17:20:52 dervin Exp $
 */
public class DataServiceModificationPanel extends ServiceModificationUIPanel {

    private DomainModelConfigPanel domainConfigPanel = null;
	private JTabbedPane mainTabbedPane = null;
	private QueryProcessorConfigPanel processorConfigPanel = null;
	private DetailsConfigurationPanel detailConfigPanel = null;
	private EnumIteratorSelectionPanel iterSelectionPanel = null;
    private AuditorsConfigurationPanel auditorConfigPanel = null;

    private transient ExtensionDataManager dataManager = null;

	public DataServiceModificationPanel(ServiceExtensionDescriptionType desc, ServiceInformation info) {
		super(desc, info);
        dataManager = new ExtensionDataManager(getExtensionTypeExtensionData());
		initialize();
	}


	private void initialize() {
		setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridx = 0;
		cons.gridy = 0;
		cons.weightx = 1.0D;
		cons.weighty = 1.0D;
		cons.fill = GridBagConstraints.BOTH;
		add(getMainTabbedPane(), cons);
	}


	protected void resetGUI() {
        int tabs = getMainTabbedPane().getTabCount();
        for (int i = 0; i < tabs; i++) {
            Component tab = getMainTabbedPane().getComponentAt(i);
            if (tab instanceof UpdatablePanel) {
                try {
                    ((UpdatablePanel) tab).updateDisplayedConfiguration();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ErrorDialog.showErrorDialog("Error updating information on " 
                        + getMainTabbedPane().getTitleAt(i), ex.getMessage(), ex);
                }
            }
        }
	}
    

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private DomainModelConfigPanel getDomainConfigPanel() {
		if (domainConfigPanel == null) {
            domainConfigPanel = new DomainModelConfigPanel(getServiceInfo(), dataManager);
            domainConfigPanel.addClassSelectionListener(new gov.nih.nci.cagrid.data.ui.domain.DomainModelClassSelectionListener() {
                public void classAdded(String packageName, ClassMapping mapping, NamespaceType packageNamespace) {
                    getDetailConfigPanel().getClassConfigTable().addClass(packageName, mapping, packageNamespace);
                    try {
                        dataManager.setClassSelectedInModel(packageName, mapping.getClassName(), true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog("Error setting class selection state in model",
                            ex.getMessage(), ex);
                    }
                }
                
                
                public void classRemoved(String packageName, String className) {
                    getDetailConfigPanel().getClassConfigTable().removeRow(packageName, className);
                    try {
                        dataManager.setClassSelectedInModel(packageName, className, false);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog("Error setting class selection state in model",
                            ex.getMessage(), ex);
                    }
                }
                
                
                public void classesCleared() {
                    getDetailConfigPanel().getClassConfigTable().clearTable();
                }
            });
            domainConfigPanel.updateDisplayedConfiguration();
		}
		return domainConfigPanel;
	}


	/**
	 * This method initializes jTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getMainTabbedPane() {
		if (mainTabbedPane == null) {
			mainTabbedPane = new JTabbedPane();
			mainTabbedPane.addTab("Domain Model", null, getDomainConfigPanel(), 
                "Selection of packages and classes in domain model");
			mainTabbedPane.addTab("Query Processor", null, getProcessorConfigPanel(), 
                "Selection and configuration of the CQL query processor");
			try {
			    if (dataManager.isUseBdt() || dataManager.isUseWsEnumeration()) {
			        mainTabbedPane.addTab("Enumeration", null, getIterSelectionPanel(), 
			        "Selection of WS-Enumeration implementation");
			    }
            } catch (Exception ex) {
                ex.printStackTrace();
                ErrorDialog.showErrorDialog(
                    "Error getting enumeration use status", ex.getMessage(), ex);
            }
			mainTabbedPane.addTab("Details", null, getDetailConfigPanel(),
				"Class to element mapping, serialization, validation");
            mainTabbedPane.addTab("Auditing", null, getAuditorConfigPanel(),
                "Optional selection and configuration of auditors");
            // tab for the service style
            try {
                String styleName = dataManager.getServiceStyle();
                if (styleName != null) {
                    ServiceStyleContainer styleContainer = ServiceStyleLoader.getStyle(styleName);
                    if (styleContainer == null) {
                        String[] message = {
                            "This service specifies the " + styleName + " service style,",
                            "but the style could not be loaded.  Please check that it is ",
                            "installed and properly configured."
                        };
                        ErrorDialog.showErrorDialog("Service style " + styleName + " not found", message);
                    } else {
                        DataServiceModificationSubPanel panel = StyleUiLoader.loadModificationUiPanel(
                            styleContainer, getServiceInfo(), dataManager);
                        if (panel != null) {
                            mainTabbedPane.addTab(styleName, null, panel, 
                                "Configuration for the " + styleName + " data service style");
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                ErrorDialog.showErrorDialog(
                    "Error loading service style configuration tab", ex.getMessage(), ex);
            }
		}
		return mainTabbedPane;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private QueryProcessorConfigPanel getProcessorConfigPanel() {
		if (processorConfigPanel == null) {
			processorConfigPanel = new QueryProcessorConfigPanel(
                getServiceInfo(), dataManager);
		}
		return processorConfigPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private DetailsConfigurationPanel getDetailConfigPanel() {
		if (detailConfigPanel == null) {
			detailConfigPanel = new DetailsConfigurationPanel(getServiceInfo(), dataManager);
		}
		return detailConfigPanel;
	}


    private EnumIteratorSelectionPanel getIterSelectionPanel() {
        if (iterSelectionPanel == null) {
            iterSelectionPanel = new EnumIteratorSelectionPanel(getServiceInfo(), dataManager);
        }
        return iterSelectionPanel;
    }
    
    
    private AuditorsConfigurationPanel getAuditorConfigPanel() {
        if (auditorConfigPanel == null) {
            auditorConfigPanel = new AuditorsConfigurationPanel(getServiceInfo(), dataManager);
        }
        return auditorConfigPanel;
    }
}