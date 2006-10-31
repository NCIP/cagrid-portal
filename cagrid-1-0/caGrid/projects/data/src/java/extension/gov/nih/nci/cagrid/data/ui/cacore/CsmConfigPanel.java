package gov.nih.nci.cagrid.data.ui.cacore;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.CQLProcessorConfigProperty;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** 
 *  CsmConfigPanel
 *  Panel to configure CSM security for the caCORE service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Oct 30, 2006 
 * @version $Id$ 
 */
public class CsmConfigPanel extends AbstractWizardPanel {
	public static final String APPLICATION_SERVICE_URL = "appserviceUrl";
	public static final String USE_CSM_FLAG = "useCsmSecurity";
	public static final String CSM_CONFIGURATION_FILENAME = "csmConfigurationFilename";
	public static final String CSM_CONTEXT_NAME = "csmContextName";
	
	/**
	 * Directory for authz libraries
	 */
	public static final String AUTHZ_LIB_DIR = ".." + File.separator + "sdkQuery" + File.separator 
		+ "ext" + File.separator + "lib";
	
	private JCheckBox useCsmCheckBox = null;
	private JLabel csmConfigFileLabel = null;
	private JTextField csmConfigFileTextField = null;
	private JButton csmConfigBrowseButton = null;
	private JLabel csmContextLabel = null;
	private JTextField csmContextTextField = null;
	private JCheckBox useAppserviceUrlCheckBox = null;
	private JPanel configPanel = null;

	public CsmConfigPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
		super(extensionDescription, info);
		initialize();
	}
	
	
	private void initialize() {
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.gridx = 0;
        gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints7.weightx = 1.0D;
        gridBagConstraints7.gridy = 1;
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.gridx = 0;
        gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
        gridBagConstraints6.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new java.awt.Dimension(622,132));
        this.add(getUseCsmCheckBox(), gridBagConstraints6);
        this.add(getConfigPanel(), gridBagConstraints7);
		
	}
	

	public void update() {
		// load data into gui
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
			CQLProcessorConfigProperty[] props = data.getCQLProcessorConfig().getProperty();
			for (int i = 0; props != null && i < props.length; i++) {
				String propName = props[i].getName();
				if (propName.equals(USE_CSM_FLAG)) {
					boolean selected = Boolean.valueOf(props[i].getValue()).booleanValue();
					getUseCsmCheckBox().setSelected(selected);
				} else if (propName.equals(CSM_CONFIGURATION_FILENAME)) {
					getCsmConfigFileTextField().setText(props[i].getValue());
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
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error loading data for CSM", ex);
		}
	}


	public String getPanelTitle() {
		return "Configure CSM Security Options";
	}


	public String getPanelShortName() {
		return "CSM Security";
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
					// copy libraries around
					if (useCsmCheckBox.isSelected()) {
						copyAuthzJars();
					} else {
						deleteAuthzJars();
					}
					PortalUtils.setContainerEnabled(getConfigPanel(), useCsmCheckBox.isSelected());
					// set the use CSM property in the extension data useCsmSecurity
					try {
						Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
						CQLProcessorConfigProperty[] props = data.getCQLProcessorConfig().getProperty();
						boolean propertyFound = false;
						for (int i = 0; props != null && i < props.length; i++) {
							if (props[i].getName().equals(USE_CSM_FLAG)) {
								props[i].setValue(String.valueOf(useCsmCheckBox.isSelected()));
								propertyFound = true;
								break;
							}
						}
						if (!propertyFound) {
							CQLProcessorConfigProperty csmProp = new CQLProcessorConfigProperty(
								USE_CSM_FLAG, String.valueOf(useCsmCheckBox.isSelected()));
							if (props == null) {
								props = new CQLProcessorConfigProperty[] {csmProp};
							} else {
								props = (CQLProcessorConfigProperty[]) Utils.appendToArray(props, csmProp);
							}
						}
						data.getCQLProcessorConfig().setProperty(props);
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
	private JLabel getCsmConfigFileLabel() {
		if (csmConfigFileLabel == null) {
			csmConfigFileLabel = new JLabel();
			csmConfigFileLabel.setText("CSM Configuration File:");
		}
		return csmConfigFileLabel;
	}


	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getCsmConfigFileTextField() {
		if (csmConfigFileTextField == null) {
			csmConfigFileTextField = new JTextField();
			csmConfigFileTextField.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					setCsmConfigFile();
				}

			    
				public void removeUpdate(DocumentEvent e) {
					setCsmConfigFile();
				}

			    
				public void changedUpdate(DocumentEvent e) {
					setCsmConfigFile();
				}
			});
		}
		return csmConfigFileTextField;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCsmConfigBrowseButton() {
		if (csmConfigBrowseButton == null) {
			csmConfigBrowseButton = new JButton();
			csmConfigBrowseButton.setText("Browse...");
			csmConfigBrowseButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// prompt for a file name
					try {
						String selection = ResourceManager.promptFile(
							CsmConfigPanel.this, null, new FileFilters.XMLFileFilter());
						if (selection != null) {
							getCsmConfigFileTextField().setText(selection);
						} else {
							getCsmConfigFileTextField().setText("");
						}
						setCsmConfigFile();
					} catch (IOException ex) {
						ex.printStackTrace();
						ErrorDialog.showErrorDialog("Error selecting a CSM configuration file", ex);
					}
				}
			});
		}
		return csmConfigBrowseButton;
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
	private JPanel getConfigPanel() {
		if (configPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints5.gridy = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 2;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints4.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			configPanel = new JPanel();
			configPanel.setLayout(new GridBagLayout());
			configPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Configuration Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			configPanel.add(getCsmConfigFileLabel(), gridBagConstraints);
			configPanel.add(getCsmContextLabel(), gridBagConstraints1);
			configPanel.add(getCsmConfigFileTextField(), gridBagConstraints2);
			configPanel.add(getCsmContextTextField(), gridBagConstraints3);
			configPanel.add(getCsmConfigBrowseButton(), gridBagConstraints4);
			configPanel.add(getUseAppserviceUrlCheckBox(), gridBagConstraints5);
		}
		return configPanel;
	}
	
	
	private void setCsmConfigFile() {
		String filename = getCsmConfigFileTextField().getText();
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
			CQLProcessorConfigProperty[] props = data.getCQLProcessorConfig().getProperty();
			boolean propertyFound = false;
			for (int i = 0; props != null && i < props.length; i++) {
				if (props[i].getName().equals(CSM_CONFIGURATION_FILENAME)) {
					props[i].setValue(filename);
					propertyFound = true;
					break;
				}
			}
			if (!propertyFound) {
				CQLProcessorConfigProperty property = 
					new CQLProcessorConfigProperty(CSM_CONFIGURATION_FILENAME, filename);
				if (props != null) {
					props = (CQLProcessorConfigProperty[]) Utils.appendToArray(props, property);
				} else {
					props = new CQLProcessorConfigProperty[] {property};
				}
				data.getCQLProcessorConfig().setProperty(props);
			}
			ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error setting csm config filename", ex);
		}
	}
	
	
	private void copyAuthzJars() {
		File[] libs = getAuthzLibs();
		String outLibDir = CacoreWizardUtils.getServiceBaseDir(getServiceInformation())
			+ File.separator + "lib" + File.separator;
		for (int i = 0; i < libs.length; i++) {
			File outLib = new File(outLibDir + libs[i].getName());
			try {
				Utils.copyFile(libs[i], outLib);
			} catch (IOException ex) {
				ex.printStackTrace();
				ErrorDialog.showErrorDialog("Error copying " + libs[i].getName(), ex);
			}
		}
	}
	
	
	private void deleteAuthzJars() {
		File[] libs = getAuthzLibs();
		String outLibDir = CacoreWizardUtils.getServiceBaseDir(getServiceInformation())
			+ File.separator + "lib" + File.separator;
		for (int i = 0; i < libs.length; i++) {
			File outLib = new File(outLibDir + libs[i].getName());
			outLib.delete();
		}
	}
	
	
	private File[] getAuthzLibs() {
		FileFilter authzFileFilter = new FileFilter() {
			public boolean accept(File pathname) {
				String filename = pathname.getName();
				if (filename.endsWith(".jar")) {
					return (filename.indexOf("authz") != -1)
						|| (filename.indexOf("grouper") != -1)
						|| (filename.equals("csmapi.jar"));
				}
				return false;
			}
		};
		File fromLibDir = new File(AUTHZ_LIB_DIR);
		File[] libs = fromLibDir.listFiles(authzFileFilter);
		return libs;
	}
}
