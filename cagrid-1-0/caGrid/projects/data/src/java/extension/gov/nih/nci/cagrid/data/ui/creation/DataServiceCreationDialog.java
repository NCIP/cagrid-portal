package gov.nih.nci.cagrid.data.ui.creation;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.extension.utils.AxisJdomUtils;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.CreationExtensionUIDialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.apache.axis.message.MessageElement;
import org.jdom.Element;
import org.projectmobius.portal.PortalResourceManager;

/** 
 *  DataServiceCreationDialog
 *  Dialog for post-creation changes to a data service 
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 1, 2006 
 * @version $Id$ 
 */
public class DataServiceCreationDialog extends CreationExtensionUIDialog {
	public static final String WS_ENUM_EXTENSION_NAME = "cagrid_wsEnum";

	private JPanel mainPanel = null;
	private JCheckBox wsEnumCheckBox = null;
	private JButton okButton = null;
	private JPanel featuresPanel = null;
	private JCheckBox gridIdentCheckBox = null;
	private JRadioButton customDataRadioButton = null;
	private JRadioButton sdkDataRadioButton = null;
	private JPanel dataSourcePanel = null;
	private ButtonGroup dataButtonGroup = null;

	public DataServiceCreationDialog(ServiceExtensionDescriptionType desc, ServiceInformation info) {
		super(desc, info);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setFeatureStatus();
			}
		});
		initialize();
	}
	
	
	private void initialize() {
        this.setTitle("Data Service Configuration");
        this.setContentPane(getMainPanel());
        getDataButtonGroup();
		pack();
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.insets = new java.awt.Insets(4,4,4,4);
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints5.gridwidth = 2;
			gridBagConstraints5.gridy = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.insets = new java.awt.Insets(4,4,4,4);
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new java.awt.Insets(4,4,4,4);
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.gridy = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getDataSourcePanel(), gridBagConstraints1);
			mainPanel.add(getFeaturesPanel(), gridBagConstraints4);
			mainPanel.add(getOkButton(), gridBagConstraints5);
		}
		return mainPanel;
	}


	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getWsEnumCheckBox() {
		if (wsEnumCheckBox == null) {
			wsEnumCheckBox = new JCheckBox();
			wsEnumCheckBox.setText("Use WS-Enumeration");
			wsEnumCheckBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (wsEnumCheckBox.isSelected()) {
						if (wsEnumExtensionInstalled()) {
							if (!wsEnumExtensionUsed()) {
								// add the ws Enumeration extension
								ExtensionDescription ext = 
									ExtensionsLoader.getInstance().getExtension(WS_ENUM_EXTENSION_NAME);
								ExtensionType extType = new ExtensionType();
								extType.setName(ext.getServiceExtensionDescription().getName());
								extType.setExtensionType(ext.getExtensionType());
								ExtensionType[] serviceExtensions = getServiceInfo().getExtensions().getExtension();
								ExtensionType[] allExtensions = new ExtensionType[serviceExtensions.length + 1];
								System.arraycopy(serviceExtensions, 0, allExtensions, 0, serviceExtensions.length);
								allExtensions[allExtensions.length - 1] = extType;
								getServiceInfo().getExtensions().setExtension(allExtensions);
							}
						} else {
							// extension not installed, complain loudly
							String[] message = {
								"The extension " + WS_ENUM_EXTENSION_NAME,
								"is not installed in Introduce.",
								"Are you sure you wish to enable this feature?"
							};
							int choice = JOptionPane.showConfirmDialog(
								PortalResourceManager.getInstance().getGridPortal(),
								message, "WS-Enumeration not installed", JOptionPane.YES_NO_OPTION);
							if (choice == JOptionPane.NO_OPTION) {
								// you have chosen wisely
								wsEnumCheckBox.setSelected(false);
							}
						}
					}
				}
			});
		}
		return wsEnumCheckBox;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("OK");
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setFeatureStatus();
					dispose();
				}
			});
		}
		return okButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getFeaturesPanel() {
		if (featuresPanel == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.weightx = 1.0D;
			gridBagConstraints11.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.insets = new Insets(2, 2, 2, 2);
			featuresPanel = new JPanel();
			featuresPanel.setLayout(new GridBagLayout());
			featuresPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Optional Features", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			featuresPanel.add(getWsEnumCheckBox(), gridBagConstraints);
			featuresPanel.add(getGridIdentCheckBox(), gridBagConstraints11);
		}
		return featuresPanel;
	}


	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getGridIdentCheckBox() {
		if (gridIdentCheckBox == null) {
			gridIdentCheckBox = new JCheckBox();
			gridIdentCheckBox.setEnabled(false);
			gridIdentCheckBox.setText("Use Grid Identifier");
		}
		return gridIdentCheckBox;
	}


	/**
	 * This method initializes jRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getCustomDataRadioButton() {
		if (customDataRadioButton == null) {
			customDataRadioButton = new JRadioButton();
			customDataRadioButton.setText("Custom Data Source");
		}
		return customDataRadioButton;
	}


	/**
	 * This method initializes jRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getSdkDataRadioButton() {
		if (sdkDataRadioButton == null) {
			sdkDataRadioButton = new JRadioButton();
			sdkDataRadioButton.setEnabled(false);
			sdkDataRadioButton.setText("caCORE SDK Data Source");
		}
		return sdkDataRadioButton;
	}
	
	
	private ButtonGroup getDataButtonGroup() {
		if (dataButtonGroup == null) {
			dataButtonGroup = new ButtonGroup();
			dataButtonGroup.add(getCustomDataRadioButton());
			dataButtonGroup.add(getSdkDataRadioButton());
			dataButtonGroup.setSelected(getCustomDataRadioButton().getModel(), true);
		}
		return dataButtonGroup;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDataSourcePanel() {
		if (dataSourcePanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.weightx = 1.0D;
			gridBagConstraints2.gridy = 0;
			dataSourcePanel = new JPanel();
			dataSourcePanel.setLayout(new GridBagLayout());
			dataSourcePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data Source", 
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			dataSourcePanel.add(getCustomDataRadioButton(), gridBagConstraints2);
			dataSourcePanel.add(getSdkDataRadioButton(), gridBagConstraints3);
		}
		return dataSourcePanel;
	}
	
	
	private void setFeatureStatus() {
		ExtensionTypeExtensionData data = 
			getExtensionTypeExtensionData();		
		Element featuresElement = new Element(DataServiceConstants.DS_FEATURES);
		try {
			featuresElement.setAttribute(DataServiceConstants.USE_CUSTOM_DATA_SORUCE,
				String.valueOf(getCustomDataRadioButton().isSelected()));
			featuresElement.setAttribute(DataServiceConstants.USE_GRID_IDENTIFIERS, 
				String.valueOf(getGridIdentCheckBox().isSelected()));
			featuresElement.setAttribute(DataServiceConstants.USE_SDK_DATA_SOURCE,
				String.valueOf(getSdkDataRadioButton().isSelected()));
			featuresElement.setAttribute(DataServiceConstants.USE_WS_ENUM,
				String.valueOf(getWsEnumCheckBox().isSelected()));
			MessageElement featuresMessageElement = AxisJdomUtils.fromElement(featuresElement);
			ExtensionTools.updateExtensionDataElement(data, featuresMessageElement);
		} catch (Exception ex) {
			ex.printStackTrace();
			PortalUtils.showErrorMessage("Error storing configuration", ex);
		}
	}
	
	
	private boolean wsEnumExtensionInstalled() {
		List extensionDescriptors = ExtensionsLoader.getInstance().getServiceExtensions();
		for (int i = 0; i < extensionDescriptors.size(); i++) {
			ServiceExtensionDescriptionType ex = (ServiceExtensionDescriptionType) extensionDescriptors.get(i);
			if (ex.getName().equals(WS_ENUM_EXTENSION_NAME)) {
				return true;
			}
		}
		return false;
	}
	
	
	private boolean wsEnumExtensionUsed() {
		ServiceInformation info = getServiceInfo();
		if (info.getExtensions() != null && info.getExtensions().getExtension() != null) {
			for (int i = 0; i < info.getExtensions().getExtension().length; i++) {
				if (info.getExtensions().getExtension(i).getName().equals(WS_ENUM_EXTENSION_NAME)) {
					return true;
				}
			}
		}		
		return false;
	}
}
