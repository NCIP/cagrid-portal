package gov.nih.nci.cagrid.data.ui.table;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.extension.AdditionalLibraries;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.property.ServiceProperties;
import gov.nih.nci.cagrid.introduce.beans.property.ServicePropertiesProperty;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;


/**
 * QueryProcessorParametersTable Table for configuring and displaying query
 * processor parameters
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @created Oct 10, 2006
 * @version $Id$
 */
public class QueryProcessorParametersTable extends JTable {
	private ExtensionTypeExtensionData extData;
	private ServiceInformation serviceInfo;
	private JTextField editorTextField = null;
    private Set<String> propertiesFromEtc = null;

	public QueryProcessorParametersTable(ExtensionTypeExtensionData extensionData, ServiceInformation serviceInfo) {
		super(createModel());
		extData = extensionData;
		this.serviceInfo = serviceInfo;
		setDefaultEditor(Object.class, new DefaultCellEditor(getEditorTextField()));
		classChanged();
	}


	private JTextField getEditorTextField() {
		if (editorTextField == null) {
			editorTextField = new JTextField();
			editorTextField.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					store(editorTextField.getText());
				}


				public void removeUpdate(DocumentEvent e) {
					store(editorTextField.getText());
				}


				public void changedUpdate(DocumentEvent e) {
					store(editorTextField.getText());
				}


				private void store(String text) {
					setValueAt(text, getSelectedRow(), getSelectedColumn());
					try {
						storeProperties();
					} catch (Exception ex) {
						ex.printStackTrace();
						ErrorDialog.showErrorDialog("Error storing query processor properties!", ex);
					}
				}
			});
		}
		return editorTextField;
	}


	public void classChanged() {
		// clear the table
		while (getRowCount() != 0) {
			((DefaultTableModel) getModel()).removeRow(0);
		}
		try {
			// get the selected class
			Class selected = getQueryProcessorClass();
			if (selected != null) {
				// get an instance of the class
				CQLQueryProcessor proc = (CQLQueryProcessor) selected.newInstance();
				// get the default parameters
				Properties defaultProps = proc.getRequiredParameters();
                // get the parameters required to be from etc
                this.propertiesFromEtc = proc.getPropertiesFromEtc();
				// get any configured parameters
				Properties configuredProps = new Properties();
				ServiceProperties serviceProps = serviceInfo.getServiceProperties();
				if (serviceProps != null && serviceProps.getProperty() != null) {
                    for (ServicePropertiesProperty property : serviceProps.getProperty()) {
                        String rawKey = property.getKey();
                        if (rawKey.startsWith(DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX)) {
							String key = rawKey.substring(DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX.length());
							configuredProps.setProperty(key, property.getValue());
						}
					}
				}
				Enumeration propKeys = defaultProps.keys();
				while (propKeys.hasMoreElements()) {
					String key = (String) propKeys.nextElement();
					String def = defaultProps.getProperty(key);
					String val = null;
					if (configuredProps.containsKey(key)) {
						val = configuredProps.getProperty(key);
					} else {
						val = defaultProps.getProperty(key);
					}
					((DefaultTableModel) getModel()).addRow(new String[]{key, def, val});
				}
				storeProperties();
			} else {
				// no query processor selected, clear all the rows
				clearTable();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error loading query processor", ex);
		}
	}


	public boolean isCellEditable(int row, int column) {
		return column == 2;
	}


	public void clearTable() {
		while (getRowCount() != 0) {
			((DefaultTableModel) getModel()).removeRow(0);
		}
	}
    
    
    public Properties getNonPrefixedConfiguredProperties() {
        Properties props = new Properties();
        for (int i = 0; i < getRowCount(); i++) {
            String key = (String) getValueAt(i, 0);
            String value = (String) getValueAt(i, 2);
            props.put(key, value);
        }
        return props;
    }


	private String getQpClassname() throws Exception {
		return CommonTools.getServicePropertyValue(serviceInfo.getServiceDescriptor(),
			DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY);
	}


	private Class getQueryProcessorClass() throws Exception {
		String className = getQpClassname();
		if ((className != null) && (className.length() != 0)
			&& !className.endsWith(DataServiceConstants.QUERY_PROCESSOR_STUB_NAME)) {
			String[] libs = getJarFilenames();
			URL[] urls = new URL[libs.length];
			for (int i = 0; i < libs.length; i++) {
				File libFile = new File(libs[i]);
				urls[i] = libFile.toURL();
			}
			ClassLoader loader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
			Class qpClass = loader.loadClass(className);
			return qpClass;
		}
		return null;
	}


	private String[] getJarFilenames() throws Exception {
		String libDir = serviceInfo.getBaseDirectory()
			+ File.separator + "lib";
		AdditionalLibraries additionalLibs = ExtensionDataUtils.getExtensionData(extData).getAdditionalLibraries();
		List namesList = new ArrayList();
		if ((additionalLibs != null) && (additionalLibs.getJarName() != null)) {
			for (int i = 0; i < additionalLibs.getJarName().length; i++) {
				String name = additionalLibs.getJarName(i);
				namesList.add(libDir + File.separator + name);
			}
		}
		String[] names = new String[namesList.size()];
		namesList.toArray(names);
		return names;
	}


	private void storeProperties() throws Exception {
		// set / add service properties to match the information in this table
		for (int i = 0; i < getRowCount(); i++) {
			String key = (String) getValueAt(i, 0);
			String userVal = (String) getValueAt(i, 2);
			String prefixedKey = DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + key;
            boolean isFromEtc = propertiesFromEtc != null && propertiesFromEtc.contains(key);
			CommonTools.setServiceProperty(
                serviceInfo.getServiceDescriptor(), prefixedKey, userVal, isFromEtc);
		}
	}


	private static DefaultTableModel createModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Parameter");
		model.addColumn("Default");
		model.addColumn("Current Value");
		return model;
	}
}
