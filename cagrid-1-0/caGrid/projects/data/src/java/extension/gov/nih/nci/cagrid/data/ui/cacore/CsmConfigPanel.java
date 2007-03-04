package gov.nih.nci.cagrid.data.ui.cacore;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


/**
 * CsmConfigPanel Panel to configure CSM security (Using the caCORE SDK
 * ClientSession) for the caCORE service NOTE: this class is presently unused,
 * but will have a place when support for caCORE SDK version 3.2 is pushed
 * through caGrid Data Services
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @created Oct 30, 2006
 * @version $Id$
 */
public class CsmConfigPanel extends AbstractWizardPanel {
	public static final String APPLICATION_SERVICE_URL = "appserviceUrl";
	public static final String USE_CSM_FLAG = "useCsmSecurity";
	public static final String CSM_CONTEXT_NAME = "csmContextName";
	// sdk 3.2 only
	public static final String CSM_CONFIGURATION_FILENAME = "csmConfigurationFilename";

	public static final String PERFORMANCE_WARNING = "NOTE:\n"
		+ "Enabling CSM security for the caCORE SDK data source will cause "
		+ "significant performance degredation when multiple clients connect "
		+ "to the data service.  Due to a bug in the caCORE SDK API, all calls "
		+ "to the query method when CSM is enabled must be synchronized, and " + "cannot be executed in parallel.";

	private JCheckBox useCsmCheckBox = null;
	private JLabel csmContextLabel = null;
	private JTextField csmContextTextField = null;
	private JCheckBox useAppserviceUrlCheckBox = null;
	private JPanel configPanel = null;
	private JTextArea performanceWarningTextArea = null;
	private JScrollPane performanceWarningScrollPane = null;
	private JLabel csmConfigLabel = null;
	private JTextField csmConfigTextField = null;
	private JButton csmConfigBrowseButton = null;
	private JButton csmClearButtonButton = null;
	private JPanel buttonPanel = null;


	public CsmConfigPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
		super(extensionDescription, info);
		initialize();
	}


	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints.gridx = 0;
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.gridx = 0;
		gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints7.weightx = 1.0D;
		gridBagConstraints7.gridy = 2;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints6.gridy = 1;
		this.setLayout(new GridBagLayout());
		this.setSize(new java.awt.Dimension(622, 132));
		this.add(getUseCsmCheckBox(), gridBagConstraints6);
		this.add(getConfigPanel(), gridBagConstraints7);
		this.add(getPerformanceWarningScrollPane(), gridBagConstraints);
	}


	public void update() {
		// load data into gui
		try {
			String useCsmValue = CommonTools.getServicePropertyValue(getServiceInformation().getServiceDescriptor(),
				DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + USE_CSM_FLAG);
			getUseCsmCheckBox().setSelected(Boolean.parseBoolean(useCsmValue));

			String csmContextName = CommonTools.getServicePropertyValue(getServiceInformation().getServiceDescriptor(),
				DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + CSM_CONTEXT_NAME);
			getCsmContextTextField().setText(csmContextName);
			String appserviceUrl = CommonTools.getServicePropertyValue(getServiceInformation().getServiceDescriptor(),
				DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + APPLICATION_SERVICE_URL);
			getUseAppserviceUrlCheckBox().setSelected(csmContextName.equals(appserviceUrl));

			// enable / disable the CSM config file based on SDK version (must
			// be 3.2)
			String sdkVersion = (String) getBitBucket().get(CoreDsIntroPanel.CACORE_VERSION_PROPERTY);
			boolean isSdk32 = (sdkVersion != null) && sdkVersion.equals(CoreDsIntroPanel.CACORE_32_VERSION);
			getCsmConfigLabel().setEnabled(isSdk32);
			getCsmConfigTextField().setEnabled(isSdk32);
			getCsmConfigBrowseButton().setEnabled(isSdk32);
			getCsmClearButtonButton().setEnabled(isSdk32);
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
					PortalUtils.setContainerEnabled(getConfigPanel(), useCsmCheckBox.isSelected());
					// set the use CSM property in the service properties
					CommonTools.setServiceProperty(getServiceInformation().getServiceDescriptor(),
						DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + USE_CSM_FLAG, String
							.valueOf(getUseCsmCheckBox().isSelected()), false);
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
							String appserviceUrl = CommonTools.getServicePropertyValue(getServiceInformation()
								.getServiceDescriptor(), DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX
								+ APPLICATION_SERVICE_URL);
							getCsmContextTextField().setText(appserviceUrl);
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
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 2;
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints8.gridy = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints4.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 0;
			configPanel = new JPanel();
			configPanel.setLayout(new GridBagLayout());
			configPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Configuration Options",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			configPanel.add(getCsmContextLabel(), gridBagConstraints1);
			configPanel.add(getCsmContextTextField(), gridBagConstraints3);
			configPanel.add(getUseAppserviceUrlCheckBox(), gridBagConstraints5);
			configPanel.add(getCsmConfigLabel(), gridBagConstraints2);
			configPanel.add(getCsmConfigTextField(), gridBagConstraints4);
			configPanel.add(getButtonPanel(), gridBagConstraints8);
		}
		return configPanel;
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
			performanceWarningScrollPane.setBorder(BorderFactory.createTitledBorder(null, "Note",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel
					.getPanelLabelColor()));
			performanceWarningScrollPane.setViewportView(getPerformanceWarningTextArea());
		}
		return performanceWarningScrollPane;
	}


	/**
	 * This method initializes csmConfigLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getCsmConfigLabel() {
		if (csmConfigLabel == null) {
			csmConfigLabel = new JLabel();
			csmConfigLabel.setText("CSM Configuration File:");
		}
		return csmConfigLabel;
	}


	/**
	 * This method initializes csmConfigTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCsmConfigTextField() {
		if (csmConfigTextField == null) {
			csmConfigTextField = new JTextField();
			csmConfigTextField.setEditable(false);
		}
		return csmConfigTextField;
	}


	/**
	 * This method initializes csmConfigBrowseButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCsmConfigBrowseButton() {
		if (csmConfigBrowseButton == null) {
			csmConfigBrowseButton = new JButton();
			csmConfigBrowseButton.setText("Browse");
			csmConfigBrowseButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						String selection = ResourceManager.promptFile(null, FileFilters.XML_FILTER);
						if (selection != null) {
							// copy the file into the service
							File inFile = new File(selection);
							File outFile = new File(getServiceInformation().getBaseDirectory().getAbsolutePath()
								+ File.separator + "etc" + File.separator + inFile.getName());
							Utils.copyFile(inFile, outFile);
							// set the CQL configuration property
							CommonTools.setServiceProperty(getServiceInformation().getServiceDescriptor(),
								DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + CSM_CONFIGURATION_FILENAME, inFile
									.getName(), true);
							getCsmConfigTextField().setText(inFile.getName());
						}
					} catch (IOException ex) {
						ex.printStackTrace();
						ErrorDialog.showErrorDialog("Error selecting configuration file", ex);
					}
				}
			});
		}
		return csmConfigBrowseButton;
	}


	/**
	 * This method initializes csmClearButtonButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCsmClearButtonButton() {
		if (csmClearButtonButton == null) {
			csmClearButtonButton = new JButton();
			csmClearButtonButton.setText("Clear");
			csmClearButtonButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// delete the copy of the config file
					File configFile = new File(getServiceInformation().getBaseDirectory().getAbsolutePath()
						+ File.separator + "etc" + File.separator + getCsmConfigTextField().getText());
					configFile.delete();
					// "unset" the configuration property
					CommonTools.setServiceProperty(getServiceInformation().getServiceDescriptor(),
						DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + CSM_CONFIGURATION_FILENAME, "", false);
					// clean up the GUI
					getCsmConfigTextField().setText("");
				}
			});
		}
		return csmClearButtonButton;
	}


	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			gridLayout.setHgap(2);
			gridLayout.setColumns(2);
			buttonPanel = new JPanel();
			buttonPanel.setLayout(gridLayout);
			buttonPanel.add(getCsmConfigBrowseButton(), null);
			buttonPanel.add(getCsmClearButtonButton(), null);
		}
		return buttonPanel;
	}
}
