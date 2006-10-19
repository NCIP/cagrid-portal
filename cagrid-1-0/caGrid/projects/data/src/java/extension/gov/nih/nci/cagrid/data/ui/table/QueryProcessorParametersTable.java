package gov.nih.nci.cagrid.data.ui.table;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.extension.AdditionalLibraries;
import gov.nih.nci.cagrid.data.extension.CQLProcessorConfig;
import gov.nih.nci.cagrid.data.extension.CQLProcessorConfigProperty;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.property.ServiceProperties;
import gov.nih.nci.cagrid.introduce.beans.property.ServicePropertiesProperty;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/** 
 *  QueryProcessorParametersTable
 *  Table for configuring and displaying query processor parameters
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Oct 10, 2006 
 * @version $Id$ 
 */
public class QueryProcessorParametersTable extends JTable {
	private ExtensionTypeExtensionData extData;
	private ServiceInformation serviceInfo;

	public QueryProcessorParametersTable(ExtensionTypeExtensionData extensionData, ServiceInformation serviceInfo) {
		super(createModel());
		this.extData = extensionData;
		this.serviceInfo = serviceInfo;
		classChanged();
		// add table model listener to store changes to properties
		getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					try {
						storeProperties();
					} catch (Exception ex) {
						ex.printStackTrace();
						ErrorDialog.showErrorDialog("Error storing properties", ex);
					}
				}
			}
		});		
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
				// get any configured parameters
				Properties configuredProps = new Properties();
				ServiceProperties serviceProps = serviceInfo.getServiceProperties();
				if (serviceProps != null) {
					for (int i = 0; serviceProps.getProperty() != null && i < serviceProps.getProperty().length; i++) {
						ServicePropertiesProperty property = serviceProps.getProperty(i);
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
					((DefaultTableModel) getModel()).addRow(new String[] {key, def, val});
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
	
	
	private String getQpClassname() throws Exception {
		return CommonTools.getServicePropertyValue(serviceInfo.getServiceDescriptor(),
			DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY);
	}
	
	
	private Class getQueryProcessorClass() throws Exception {
		String[] libs = getJarFilenames();
		URL[] urls = new URL[libs.length];
		for (int i = 0; i < libs.length; i++) {
			File libFile = new File(libs[i]);
			urls[i] = libFile.toURL();
		}
		ClassLoader loader = new URLClassLoader(urls, getClass().getClassLoader());
		String className = getQpClassname();
		if (className != null && !className.endsWith(DataServiceConstants.QUERY_PROCESSOR_STUB_NAME)) {
			Class qpClass = loader.loadClass(className);
			return qpClass;
		}
		return null;
	}
	
	
	private String[] getJarFilenames() throws Exception {
		String libDir = serviceInfo.getIntroduceServiceProperties().getProperty(
			IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "lib";
		AdditionalLibraries additionalLibs = 
			ExtensionDataUtils.getExtensionData(extData).getAdditionalLibraries();
		List namesList = new ArrayList();
		if (additionalLibs != null && additionalLibs.getJarName() != null) {
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
		Data data = ExtensionDataUtils.getExtensionData(extData);
		CQLProcessorConfig config = data.getCQLProcessorConfig();
		if (config == null) {
			config = new CQLProcessorConfig();
			data.setCQLProcessorConfig(config);
		}
		List userProps = new ArrayList();
		for (int i = 0; i < getRowCount(); i++) {
			String key = (String) getValueAt(i, 0);
			String defaultVal = (String) getValueAt(i, 1);
			String userVal = (String) getValueAt(i, 2);
			if (!defaultVal.equals(userVal)) {
				CQLProcessorConfigProperty property = new CQLProcessorConfigProperty();
				property.setName(key);
				property.setValue(userVal);
				userProps.add(property);
			}
		}
		CQLProcessorConfigProperty[] props = new CQLProcessorConfigProperty[userProps.size()];
		userProps.toArray(props);
		config.setProperty(props);
		ExtensionDataUtils.storeExtensionData(extData, data);
	}
	
	
	private static DefaultTableModel createModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Parameter");
		model.addColumn("Default");
		model.addColumn("Current Value");
		return model;
	}
}
