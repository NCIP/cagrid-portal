package gov.nih.nci.cagrid.data.ui.cacore;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.CQLProcessorConfig;
import gov.nih.nci.cagrid.data.extension.CQLProcessorConfigProperty;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** 
 *  AppserviceConfigPanel
 *  Wizard panel to configure parameters for caCORE SDK 3.1 data source
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 9, 2006 
 * @version $Id: AppserviceConfigPanel.java,v 1.1 2006-11-09 19:52:00 dervin Exp $ 
 */
public class AppserviceConfigPanel extends AbstractWizardPanel {
	public static final String APPLICATION_SERVICE_URL = "appserviceUrl";
	public static final String CASE_INSENSITIVE_QUERYING = "queryCaseInsensitive";
	public static final String USE_CSM_FLAG = "useCsmSecurity";
	public static final String CSM_CONTEXT_NAME = "csmContextName";
	
	public static final String PERFORMANCE_WARNING = 
		"NOTE:\n" +
		"Enabling CSM security for the caCORE SDK data source will cause " + 
		"significant performance degredation when multiple clients connect " +
		"to the data service.  Due to a bug in the caCORE SDK API, all calls " +
		"to the query method when CSM is enabled must be synchronized, and " +
		"cannot be executed in parallel.";
	
	private JLabel serviceUrlLabel = null;
	private JTextField serviceUrlTextField = null;
	private JCheckBox useCsmCheckBox = null;
	private JLabel csmContextLabel = null;
	private JTextField csmContextTextField = null;
	private JCheckBox useAppserviceUrlCheckBox = null;
	private JPanel csmOptionsPanel = null;
	private JTextArea performanceWarningTextArea = null;
	private JScrollPane performanceWarningScrollPane = null;
	private JCheckBox caseInsensitiveCheckBox = null;
	private JPanel appserviceConfigPanel = null;
	private JPanel csmInfoPanel = null;

	public AppserviceConfigPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
		super(extensionDescription, info);
		initialize();
	}
	
	
	private void initialize() {
        GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
        gridBagConstraints10.gridx = 0;
        gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints10.weightx = 1.0D;
        gridBagConstraints10.gridy = 2;
        GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
        gridBagConstraints9.gridx = 0;
        gridBagConstraints9.weightx = 1.0D;
        gridBagConstraints9.fill = GridBagConstraints.BOTH;
        gridBagConstraints9.weighty = 1.0D;
        gridBagConstraints9.gridy = 1;
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.gridx = 0;
        gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints6.weightx = 1.0D;
        gridBagConstraints6.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(558, 273));
        this.add(getAppserviceConfigPanel(), gridBagConstraints6);
        this.add(getCsmInfoPanel(), gridBagConstraints9);
        this.add(getCsmOptionsPanel(), gridBagConstraints10);		
	}


	public String getPanelShortName() {
		return "Configuration";
	}


	public String getPanelTitle() {
		return "caCORE Application Service Configuration";
	}


	public void update() {
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
			CQLProcessorConfig config = data.getCQLProcessorConfig();
			if (config != null) {
				CQLProcessorConfigProperty[] props = config.getProperty();
				for (int i = 0; props != null && i < props.length; i++) {
					String propName = props[i].getName();
					if (propName.equals(USE_CSM_FLAG)) {
						boolean selected = Boolean.valueOf(props[i].getValue()).booleanValue();
						getUseCsmCheckBox().setSelected(selected);
					} else if (propName.equals(CSM_CONTEXT_NAME)) {
						String contextName = props[i].getValue();
						getCsmContextTextField().setText(contextName);
						String appserviceUrl = null;
						for (int j = 0; j < props.length; j++) {
							if (props[j].getName().equals(APPLICATION_SERVICE_URL)) {
								appserviceUrl = props[j].getValue();
								break;
							}
						}
						getUseAppserviceUrlCheckBox().setSelected(contextName.equals(appserviceUrl));
					} else if (propName.equals(APPLICATION_SERVICE_URL)) {
						String serviceUrl = props[i].getValue();
						getServiceUrlTextField().setText(serviceUrl);
					} else if (propName.equals(CASE_INSENSITIVE_QUERYING)) {
						boolean selected = Boolean.valueOf(props[i].getValue()).booleanValue();
						getCaseInsensitiveCheckBox().setSelected(selected);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error loading data for CSM", ex);
		}
	}
	
	
	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getServiceUrlLabel() {
		if (serviceUrlLabel == null) {
			serviceUrlLabel = new JLabel();
			serviceUrlLabel.setText("Service URL:");
		}
		return serviceUrlLabel;
	}
	
	
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getServiceUrlTextField() {
		if (serviceUrlTextField == null) {
			serviceUrlTextField = new JTextField();
			serviceUrlTextField.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					changeAppServiceUrl();
				}

			    
			    public void removeUpdate(DocumentEvent e) {
			    	changeAppServiceUrl();
			    }
			    

			    public void changedUpdate(DocumentEvent e) {
			    	changeAppServiceUrl();
			    }
			});
		}
		return serviceUrlTextField;
	}
	
	
	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getUseCsmCheckBox() {
		if (useCsmCheckBox == null) {
			useCsmCheckBox = new JCheckBox();
			useCsmCheckBox.setText("Use CSM Security");
			useCsmCheckBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					PortalUtils.setContainerEnabled(getCsmOptionsPanel(), useCsmCheckBox.isSelected());
					// set the use CSM property in the extension data useCsmSecurity
					try {
						Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
						ExtensionDataUtils.setCQLProcessorProperty(data, USE_CSM_FLAG, 
							String.valueOf(useCsmCheckBox.isSelected()));
						ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
					} catch (Exception ex) {
						ex.printStackTrace();
						ErrorDialog.showErrorDialog("Error storing use CSM property", ex);
					}
				}
			});
		}
		return useCsmCheckBox;
	}


	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getCsmContextLabel() {
		if (csmContextLabel == null) {
			csmContextLabel = new JLabel();
			csmContextLabel.setText("CSM Context Name:");
		}
		return csmContextLabel;
	}


	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getCsmContextTextField() {
		if (csmContextTextField == null) {
			csmContextTextField = new JTextField();
			csmContextTextField.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					changeCsmContext();
				}


				public void removeUpdate(DocumentEvent e) {
					changeCsmContext();
				}


				public void changedUpdate(DocumentEvent e) {
					changeCsmContext();
				}
			});
		}
		return csmContextTextField;
	}


	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getUseAppserviceUrlCheckBox() {
		if (useAppserviceUrlCheckBox == null) {
			useAppserviceUrlCheckBox = new JCheckBox();
			useAppserviceUrlCheckBox.setText("Use Application Service URL");
			useAppserviceUrlCheckBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					boolean selected = useAppserviceUrlCheckBox.isSelected();
					getCsmContextTextField().setEnabled(!selected);
					getCsmContextTextField().setEditable(!selected);
					if (selected) {
						try {
							Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
							CQLProcessorConfigProperty[] props = data.getCQLProcessorConfig().getProperty();
							for (int i = 0; props != null && i < props.length; i++) {
								if (props[i].getName().equals(APPLICATION_SERVICE_URL)) {
									getCsmContextTextField().setText(props[i].getValue());
									break;
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							ErrorDialog.showErrorDialog("Error getting application service URL", ex);
						}
					}
				}
			});
		}
		return useAppserviceUrlCheckBox;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCsmOptionsPanel() {
		if (csmOptionsPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints5.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 0;
			csmOptionsPanel = new JPanel();
			csmOptionsPanel.setLayout(new GridBagLayout());
			csmOptionsPanel.setBorder(BorderFactory.createTitledBorder(
				null, "CSM Configuration Options", TitledBorder.DEFAULT_JUSTIFICATION, 
				TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			csmOptionsPanel.add(getCsmContextLabel(), gridBagConstraints1);
			csmOptionsPanel.add(getCsmContextTextField(), gridBagConstraints3);
			csmOptionsPanel.add(getUseAppserviceUrlCheckBox(), gridBagConstraints5);
		}
		return csmOptionsPanel;
	}


	/**
	 * This method initializes performanceWarningTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getPerformanceWarningTextArea() {
		if (performanceWarningTextArea == null) {
			performanceWarningTextArea = new JTextArea();
			performanceWarningTextArea.setLineWrap(true);
			performanceWarningTextArea.setWrapStyleWord(true);
			performanceWarningTextArea.setEditable(false);
			performanceWarningTextArea.setText(PERFORMANCE_WARNING);
		}
		return performanceWarningTextArea;
	}


	/**
	 * This method initializes performanceWarningScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getPerformanceWarningScrollPane() {
		if (performanceWarningScrollPane == null) {
			performanceWarningScrollPane = new JScrollPane();
			performanceWarningScrollPane.setBorder(BorderFactory.createTitledBorder(
				null, "Note", TitledBorder.DEFAULT_JUSTIFICATION, 
				TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			performanceWarningScrollPane.setViewportView(getPerformanceWarningTextArea());
		}
		return performanceWarningScrollPane;
	}
	
	
	private void changeAppServiceUrl() {
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
			ExtensionDataUtils.setCQLProcessorProperty(data, APPLICATION_SERVICE_URL, 
				getServiceUrlTextField().getText());
			ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error setting the application service URL: " + ex.getMessage(), ex);
		}
	}
	
	
	private void changeCsmContext() {
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
			ExtensionDataUtils.setCQLProcessorProperty(data, CSM_CONTEXT_NAME, 
				getCsmContextTextField().getText());
			ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error setting CSM context: " + ex.getMessage(), ex);
		}
	}


	/**
	 * This method initializes caseInsensitiveCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getCaseInsensitiveCheckBox() {
		if (caseInsensitiveCheckBox == null) {
			caseInsensitiveCheckBox = new JCheckBox();
			caseInsensitiveCheckBox.setText("Case Insensitive Queries");
			caseInsensitiveCheckBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					try {
						Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
						ExtensionDataUtils.setCQLProcessorProperty(data, CASE_INSENSITIVE_QUERYING, 
							String.valueOf(caseInsensitiveCheckBox.isSelected()));
						ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
					} catch (Exception ex) {
						ex.printStackTrace();
						ErrorDialog.showErrorDialog("Error setting the case insensitive flag: " + ex.getMessage(), ex);
					}
				}
			});
		}
		return caseInsensitiveCheckBox;
	}


	/**
	 * This method initializes appserviceConfigPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAppserviceConfigPanel() {
		if (appserviceConfigPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints4.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints2.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridy = 0;
			appserviceConfigPanel = new JPanel();
			appserviceConfigPanel.setLayout(new GridBagLayout());
			appserviceConfigPanel.setBorder(BorderFactory.createTitledBorder(
				null, "Application Service Options", TitledBorder.DEFAULT_JUSTIFICATION, 
				TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			appserviceConfigPanel.add(getServiceUrlLabel(), gridBagConstraints);
			appserviceConfigPanel.add(getServiceUrlTextField(), gridBagConstraints2);
			appserviceConfigPanel.add(getCaseInsensitiveCheckBox(), gridBagConstraints4);
		}
		return appserviceConfigPanel;
	}


	/**
	 * This method initializes csmInfoPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCsmInfoPanel() {
		if (csmInfoPanel == null) {
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.BOTH;
			gridBagConstraints8.gridy = 1;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.weighty = 1.0;
			gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints8.gridx = 0;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.gridy = 0;
			csmInfoPanel = new JPanel();
			csmInfoPanel.setLayout(new GridBagLayout());
			csmInfoPanel.add(getUseCsmCheckBox(), gridBagConstraints7);
			csmInfoPanel.add(getPerformanceWarningScrollPane(), gridBagConstraints8);
		}
		return csmInfoPanel;
	}
}
