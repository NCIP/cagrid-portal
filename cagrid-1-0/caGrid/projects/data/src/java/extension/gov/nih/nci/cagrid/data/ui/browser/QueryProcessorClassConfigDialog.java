package gov.nih.nci.cagrid.data.ui.browser;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.extension.AdditionalLibraries;
import gov.nih.nci.cagrid.data.extension.CQLProcessorConfig;
import gov.nih.nci.cagrid.data.extension.CQLProcessorConfigProperty;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.projectmobius.portal.PortalResourceManager;

/** 
 *  QueryProcessorClassConfigDialog
 *  Dialog to configure properties of a query processor class
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jun 19, 2006 
 * @version $Id$ 
 */
public class QueryProcessorClassConfigDialog extends JDialog {
	private ExtensionTypeExtensionData extensionData = null;
	private ServiceInformation serviceInfo = null;
	private JButton setPropertiesButton = null;
	private JButton cancelButton = null;
	private JPanel buttonPanel = null;
	private JPanel mainPanel = null;
	private JPanel propertiesPanel = null;
	private JScrollPane propertiesScrollPane = null;
	
	public QueryProcessorClassConfigDialog(ExtensionTypeExtensionData extensionData, 
		ServiceInformation serviceInfo) {
		super(PortalResourceManager.getInstance().getGridPortal(),
			"Query Processor Configuration", true);
		this.extensionData = extensionData;
		this.serviceInfo = serviceInfo;
		initialize();
	}
	
	
	private void initialize() {
		this.setTitle("Configuration for processor class " + getQpClassname());
		this.setContentPane(getMainPanel());
		populateProperties();
		pack();
		this.setSize(new java.awt.Dimension(325, 180));
		setVisible(true);
	}
	
	
	private String getQpClassname() {
		String classname = null;
		try {
			classname = CommonTools.getServicePropertyValue(serviceInfo, DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return classname;
	}
	
	
	private String[] getJarFilenames() throws Exception {
		String libDir = serviceInfo.getIntroduceServiceProperties().getProperty(
			IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "lib";
		AdditionalLibraries additionalLibs = 
			ExtensionDataUtils.getExtensionData(extensionData).getAdditionalLibraries();
		if (additionalLibs != null && additionalLibs.getJarName() != null) {
			List namesList = new ArrayList();
			for (int i = 0; i < additionalLibs.getJarName().length; i++) {
				String name = additionalLibs.getJarName(i);
				namesList.add(libDir + File.separator + name);
			}
			String[] names = new String[namesList.size()];
			namesList.toArray(names);
			return names;
		}
		return null;
	}
	

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSetPropertiesButton() {
		if (setPropertiesButton == null) {
			setPropertiesButton = new JButton();
			setPropertiesButton.setText("Set Properties");
			setPropertiesButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						storeProperties();
						dispose();
					} catch (Exception ex) {
						ex.printStackTrace();
						ErrorDialog.showErrorDialog("Error storing properties", ex);
					}
				}
			});
		}
		return setPropertiesButton;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return cancelButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.gridy = 0;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getSetPropertiesButton(), gridBagConstraints);
			buttonPanel.add(getCancelButton(), gridBagConstraints1);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.gridx = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 1;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getButtonPanel(), gridBagConstraints3);
			mainPanel.add(getPropertiesScrollPane(), gridBagConstraints2);
		}
		return mainPanel;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPropertiesPanel() {
		if (propertiesPanel == null) {
			propertiesPanel = new JPanel();
			propertiesPanel.setLayout(new GridBagLayout());
		}
		return propertiesPanel;
	}


	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getPropertiesScrollPane() {
		if (propertiesScrollPane == null) {
			propertiesScrollPane = new JScrollPane();
			propertiesScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Properties", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			propertiesScrollPane.setViewportView(getPropertiesPanel());
			propertiesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return propertiesScrollPane;
	}
	
	
	private void populateProperties() {
		// get the requires properties for the processor
		Class qpClass = null;
		try {
			qpClass = getQueryProcessorClass();
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error obtaining query processor class: " + ex.getMessage(), ex);
			return;
		}
		CQLQueryProcessor processor = null;
		try {
			processor = (CQLQueryProcessor) qpClass.newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error getting an instance of query processor class: " + ex.getMessage(), ex);
			return;
		}
		// get parameters required by the query processor
		Properties requiredParams = processor.getRequiredParameters();
		
		// get any properties currently defined for the query processor
		Properties currentProperties = null;
		try {
			currentProperties = getCurrentQueryProcessorParams();
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error getting current query processor properties: " + ex.getMessage(), ex);
			return;
		}
		
		// merge the two sets of properties
		Properties configuredParams = new Properties();
		Iterator keyIter = requiredParams.keySet().iterator();
		while (keyIter.hasNext()) {
			String key = (String) keyIter.next();
			String value = currentProperties.getProperty(key);
			if (value == null) {
				// no configured value, get default
				value = requiredParams.getProperty(key);
			}
			configuredParams.setProperty(key, value);
		}
		
		// put the map into the GUI
		Iterator configKeyIter = configuredParams.keySet().iterator();
		int paramIndex = 0;
		while (configKeyIter.hasNext()) {
			String key = (String) configKeyIter.next();
			String value = configuredParams.getProperty(key);
			PropertyRow row = new PropertyRow(key, value);
			GridBagConstraints rowConstraints = new GridBagConstraints();
			rowConstraints.gridx = 0;
			rowConstraints.gridy = paramIndex;
			rowConstraints.fill = GridBagConstraints.HORIZONTAL;
			rowConstraints.weightx = 1.0D;
			getPropertiesPanel().add(row, rowConstraints);
			paramIndex++;
			getPropertiesPanel().repaint();
		}
	}
	
	
	private Properties getCurrentQueryProcessorParams() throws Exception {
		Properties props = new Properties();
		Data data = ExtensionDataUtils.getExtensionData(extensionData);
		CQLProcessorConfig config = data.getCQLProcessorConfig();
		if (config != null && config.getProperty() != null) {
			for (int i = 0; i < config.getProperty().length; i++) {
				CQLProcessorConfigProperty prop = config.getProperty(i);
				props.setProperty(prop.getName(), prop.getValue());					
			}
		}
		return props;
	}
	
	
	private Class getQueryProcessorClass() throws Exception {
		String[] libs = getJarFilenames();
		URL[] urls = new URL[libs.length];
		for (int i = 0; i < libs.length; i++) {
			File libFile = new File(libs[i]);
			urls[i] = libFile.toURL();
		}
		ClassLoader loader = new URLClassLoader(urls, getClass().getClassLoader());
		Class qpClass = loader.loadClass(getQpClassname());
		return qpClass;
	}
	
	
	private void storeProperties() throws Exception {
		Data data = ExtensionDataUtils.getExtensionData(extensionData);
		CQLProcessorConfig config = data.getCQLProcessorConfig();
		if (config == null) {
			config = new CQLProcessorConfig();
			data.setCQLProcessorConfig(config);
		}
		CQLProcessorConfigProperty[] props = new CQLProcessorConfigProperty[getPropertiesPanel().getComponentCount()];
		for (int i = 0; i < getPropertiesPanel().getComponentCount(); i++) {
			PropertyRow row = (PropertyRow) getPropertiesPanel().getComponent(i);
			CQLProcessorConfigProperty property = new CQLProcessorConfigProperty();
			property.setName(row.getPropertyName());
			property.setValue(row.getValue());
			props[i] = property;
		}
		config.setProperty(props);
		ExtensionDataUtils.storeExtensionData(extensionData, data);
	}
	
	
	private static class PropertyRow extends JPanel {
		private String propertyName;
		private String value;
		private boolean dirty;
		
		private JLabel nameLabel = null;
		private JTextField valueTextField = null;
		
		public PropertyRow(String propName, String current) {
			this.propertyName = propName;
			this.value = current;
			this.dirty = false;
			initialize();
		}
		
		
		public String getPropertyName() {
			return propertyName;
		}
		
		
		public String getValue() {
			return valueTextField.getText();
		}
		
		
		public boolean isDirty() {
			return dirty;
		}
		
		
		private void initialize() {
			setLayout(new GridBagLayout());
			nameLabel = new JLabel(propertyName);
			valueTextField = new JTextField(value);
			valueTextField.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					dirty = true;
				}

				
			    public void removeUpdate(DocumentEvent e) {
			    	dirty = true;
			    }


			    public void changedUpdate(DocumentEvent e) {
			    	dirty = true;
			    }
			});
			GridBagConstraints labelConst = new GridBagConstraints();
			labelConst.gridx = 0;
			labelConst.gridy = 0;
			labelConst.insets = new Insets(2,2,2,2);
			labelConst.anchor = GridBagConstraints.WEST;
			GridBagConstraints valueConst = new GridBagConstraints();
			valueConst.gridx = 1;
			valueConst.gridy = 0;
			valueConst.insets = new Insets(2,2,2,2);
			valueConst.fill = GridBagConstraints.HORIZONTAL;
			valueConst.anchor = GridBagConstraints.CENTER;
			valueConst.weightx = 1.0D;
			add(nameLabel, labelConst);
			add(valueTextField, valueConst);
		}
	}
}
