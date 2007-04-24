package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.cql.ui.CQLQueryProcessorConfigUI;
import gov.nih.nci.cagrid.data.extension.AdditionalLibraries;
import gov.nih.nci.cagrid.data.extension.ClassMapping;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.ui.browser.AdditionalJarsChangeListener;
import gov.nih.nci.cagrid.data.ui.browser.AdditionalJarsChangedEvent;
import gov.nih.nci.cagrid.data.ui.browser.ClassBrowserPanel;
import gov.nih.nci.cagrid.data.ui.browser.ClassSelectionEvent;
import gov.nih.nci.cagrid.data.ui.browser.ClassSelectionListener;
import gov.nih.nci.cagrid.data.ui.table.ClassChangeEvent;
import gov.nih.nci.cagrid.data.ui.table.ClassElementSerializationTable;
import gov.nih.nci.cagrid.data.ui.table.ClassInformatonChangeListener;
import gov.nih.nci.cagrid.data.ui.table.QueryProcessorParametersTable;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.beans.property.ServicePropertiesProperty;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.ServiceModificationUIPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * DataServiceModificationPanel 
 * Panel for configuring a caGrid data service from
 * within the Introduce Toolkit
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @created Oct 10, 2006
 * @version $Id$
 */
public class DataServiceModificationPanel extends ServiceModificationUIPanel {

	private ClassElementSerializationTable classConfigTable = null;
	private ClassBrowserPanel classBrowserPanel = null;
	private JCheckBox cqlSyntaxValidationCheckBox = null;
	private JCheckBox domainModelValidationCheckBox = null;
	private JScrollPane classConfigScrollPane = null;
	private JPanel validationCheckPanel = null;
	private DomainModelConfigPanel domainConfigPanel = null;
	private JTabbedPane mainTabbedPane = null;
	private JPanel processorConfigPanel = null;
	private JPanel detailConfigPanel = null;
	private QueryProcessorParametersTable qpParamsTable = null;
	private JScrollPane qpParamsScrollPane = null;
    private JPanel processorConfigurationPanel = null;
    private JButton launchProcessorConfigButton = null;

	private transient Map packageToClassMap = null;
    
    private transient ExtensionDataManager dataManager = null;

	public DataServiceModificationPanel(ServiceExtensionDescriptionType desc, ServiceInformation info) {
		super(desc, info);
        packageToClassMap = new HashMap();
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
		// TODO Auto-generated method stub
	}
    

	private ClassElementSerializationTable getClassConfigTable() {
		if (classConfigTable == null) {
			classConfigTable = new ClassElementSerializationTable();
			classConfigTable.addClassInformatonChangeListener(new ClassInformatonChangeListener() {
				public void elementNameChanged(ClassChangeEvent e) {
					// get class to element mapping
					Map classToElement = (Map) packageToClassMap.get(e.getPackageName());
					// get the old element name mapping for this class
					String oldElementName = (String) classToElement.get(e.getClassName());
					// get the namespace type for the class
					NamespaceType nsType = CommonTools.getNamespaceType(
                        getServiceInfo().getNamespaces(), e.getNamespace());
					// find the schema element type
					SchemaElementType schemaType = NamespaceUtils.getElementByName(
                        nsType, e.getElementName());
					if (schemaType == null && oldElementName != null && oldElementName.length() != 0) {
						// WARNING: You've selected a non-existant element name,
						// AND the old element name was NOT non existant as well
						ErrorDialog.showErrorDialog(
                            "No element named " + e.getElementName() 
                            + " in namespace " + e.getNamespace());
					}
					// change the element name mapping
					classToElement.put(e.getClassName(), e.getElementName());
					// save the mapping info
					try {
                        dataManager.setClassElementNameInModel(
                            e.getPackageName(), e.getClassName(), e.getElementName());
					} catch (Exception ex) {
						ex.printStackTrace();
						ErrorDialog.showErrorDialog("Error storing class mapping", ex);
					}
				}


				public void serializationChanged(ClassChangeEvent e) {
					// get the namespace type for the class
					NamespaceType nsType = CommonTools.getNamespaceType(
                        getServiceInfo().getNamespaces(), e.getNamespace());
					// find the schema element type
					SchemaElementType schemaType = NamespaceUtils.getElementByName(
                        nsType, e.getElementName());
					// user may have selected an element type name which is not
					// in the namespace type.
					// TODO: what do I do in that case? maybe prevent that in
					// handling element name changed
					if (schemaType != null) {
						schemaType.setSerializer(e.getSerializer());
						schemaType.setDeserializer(e.getDeserializer());
					}
				}


				public void targetabilityChanged(ClassChangeEvent e) {
					try {
                        dataManager.setClassTargetableInModel(
                            e.getPackageName(), e.getClassName(), e.isTargetable());
					} catch (Exception ex) {
						ex.printStackTrace();
						ErrorDialog.showErrorDialog("Error storing change to targetability", ex);
					}
				}
			});
		}
		return classConfigTable;
	}


	private ClassBrowserPanel getClassBrowserPanel() {
		if (classBrowserPanel == null) {
			classBrowserPanel = new ClassBrowserPanel(getExtensionTypeExtensionData(), getServiceInfo());
			// classBrowserPanel = new ClassBrowserPanel(null, null); //uncomment this line to edit in VE
			classBrowserPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
				"Query Processor Class Selection", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, 
                null, PortalLookAndFeel.getPanelLabelColor()));
			// listen for class selection events
			classBrowserPanel.addClassSelectionListener(new ClassSelectionListener() {
				public void classSelectionChanged(ClassSelectionEvent e) {
					try {
						saveProcessorClassName(classBrowserPanel.getSelectedClassName());
					} catch (Exception ex) {
						ex.printStackTrace();
						ErrorDialog.showErrorDialog(
                            "Error setting the query processor class: " + ex.getMessage(), ex);
					}
				}
			});
			// listen for jar addition events
			classBrowserPanel.addAdditionalJarsChangeListener(new AdditionalJarsChangeListener() {
				public void additionalJarsChanged(AdditionalJarsChangedEvent e) {
					// remove any existing qp jars element from the service data
					AdditionalLibraries additionalLibs = new AdditionalLibraries();
					String[] additionalJars = classBrowserPanel.getAdditionalJars();
					additionalLibs.setJarName(additionalJars);
					try {
						Data data = ExtensionDataUtils.getExtensionData(getExtensionTypeExtensionData());
						data.setAdditionalLibraries(additionalLibs);
						ExtensionDataUtils.storeExtensionData(getExtensionTypeExtensionData(), data);
					} catch (Exception ex) {
						ex.printStackTrace();
						ErrorDialog.showErrorDialog("Error storing additional libraries information: "
							+ ex.getMessage(), ex);
					}
				}
			});
		}
		return classBrowserPanel;
	}


	private JCheckBox getCqlSyntaxValidationCheckBox() {
		if (cqlSyntaxValidationCheckBox == null) {
			cqlSyntaxValidationCheckBox = new JCheckBox();
			cqlSyntaxValidationCheckBox.setText("Validate CQL Syntax");
			cqlSyntaxValidationCheckBox.setToolTipText("Causes the Data Service to "
				+ "validate all CQL queries for syntactic correctness");
			cqlSyntaxValidationCheckBox.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					CommonTools.setServiceProperty(getServiceInfo().getServiceDescriptor(),
						DataServiceConstants.VALIDATE_CQL_FLAG, String.valueOf(
                            getCqlSyntaxValidationCheckBox().isSelected()), false);
				}
			});
			// set the check box selection
			if (CommonTools.servicePropertyExists(getServiceInfo().getServiceDescriptor(),
				DataServiceConstants.VALIDATE_CQL_FLAG)) {
				try {
					cqlSyntaxValidationCheckBox.setSelected(Boolean.parseBoolean(
                        CommonTools.getServicePropertyValue(
                            getServiceInfo().getServiceDescriptor(), 
                            DataServiceConstants.VALIDATE_CQL_FLAG)));
				} catch (Exception ex) {
					ex.printStackTrace();
					ErrorDialog.showErrorDialog("Error getting service property value for "
						+ DataServiceConstants.VALIDATE_CQL_FLAG, ex);
				}
			}
		}
		return cqlSyntaxValidationCheckBox;
	}


	/**
	 * This method initializes jCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getDomainModelValidationCheckBox() {
		if (domainModelValidationCheckBox == null) {
			domainModelValidationCheckBox = new JCheckBox();
			domainModelValidationCheckBox.setText("Validate Domain Model");
			domainModelValidationCheckBox.setToolTipText("Causes the data service to ensure "
				+ "all queries remain within the limits of the exposed domain model");
			domainModelValidationCheckBox.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					CommonTools.setServiceProperty(getServiceInfo().getServiceDescriptor(),
						DataServiceConstants.VALIDATE_DOMAIN_MODEL_FLAG, String
							.valueOf(getDomainModelValidationCheckBox().isSelected()), false);
				}
			});
			// set the check box selection
			if (CommonTools.servicePropertyExists(getServiceInfo().getServiceDescriptor(),
				DataServiceConstants.VALIDATE_DOMAIN_MODEL_FLAG)) {
				try {
					domainModelValidationCheckBox.setSelected(Boolean.parseBoolean(
                        CommonTools.getServicePropertyValue(
                            getServiceInfo().getServiceDescriptor(), 
                            DataServiceConstants.VALIDATE_DOMAIN_MODEL_FLAG)));
				} catch (Exception ex) {
					ex.printStackTrace();
					ErrorDialog.showErrorDialog("Error getting service property value for "
						+ DataServiceConstants.VALIDATE_DOMAIN_MODEL_FLAG, ex);
				}
			}
		}
		return domainModelValidationCheckBox;
	}


	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getClassConfigScrollPane() {
		if (classConfigScrollPane == null) {
			classConfigScrollPane = new JScrollPane();
			classConfigScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
				"Exposed Class Configuration", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, 
                null, PortalLookAndFeel.getPanelLabelColor()));
			classConfigScrollPane.setViewportView(getClassConfigTable());
			classConfigScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return classConfigScrollPane;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getValidationCheckPanel() {
		if (validationCheckPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints.gridy = 0;
			validationCheckPanel = new JPanel();
			validationCheckPanel.setLayout(new GridBagLayout());
			validationCheckPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
                null, "Query Validation", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, 
                null, PortalLookAndFeel.getPanelLabelColor()));
			validationCheckPanel.add(getCqlSyntaxValidationCheckBox(), gridBagConstraints);
			validationCheckPanel.add(getDomainModelValidationCheckBox(), gridBagConstraints1);
		}
		return validationCheckPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private DomainModelConfigPanel getDomainConfigPanel() {
		if (domainConfigPanel == null) {
            domainConfigPanel = new DomainModelConfigPanel(getExtensionTypeExtensionData(), getServiceInfo(), dataManager);
            domainConfigPanel.addClassSelectionListener(new gov.nih.nci.cagrid.data.ui.DomainModelClassSelectionListener() {
                public void classAdded(String packageName, ClassMapping mapping, NamespaceType packageNamespace) {
                    getClassConfigTable().addClass(packageName, mapping, packageNamespace);
                    try {
                        dataManager.setClassSelectedInModel(packageName, mapping.getClassName(), true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog("Error setting class selection state in model",
                            ex.getMessage(), ex);
                    }
                }
                
                
                public void classRemoved(String packageName, String className) {
                    getClassConfigTable().removeRow(packageName, className);
                    try {
                        dataManager.setClassSelectedInModel(packageName, className, false);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorDialog.showErrorDialog("Error setting class selection state in model",
                            ex.getMessage(), ex);
                    }
                }
                
                
                public void classesCleared() {
                    getClassConfigTable().clearTable();
                }
            });
            domainConfigPanel.populateFromExtensionData();
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
			mainTabbedPane.addTab("Domain Model", null, getDomainConfigPanel(), null);
			mainTabbedPane.addTab("Query Processor", null, getProcessorConfigPanel(), null);
			mainTabbedPane.addTab("Details", null, getDetailConfigPanel(),
				"Class to element mapping, serialization, validation");
		}
		return mainTabbedPane;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getProcessorConfigPanel() {
		if (processorConfigPanel == null) {
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 0;
            gridBagConstraints22.gridy = 1;
			gridBagConstraints22.fill = GridBagConstraints.BOTH;
            gridBagConstraints22.weightx = 1.0D;
            gridBagConstraints22.weighty = 1.0D;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.weightx = 1.0D;
			gridBagConstraints16.gridy = 0;
			gridBagConstraints16.fill = GridBagConstraints.HORIZONTAL;
			processorConfigPanel = new JPanel();
			processorConfigPanel.setLayout(new GridBagLayout());
			processorConfigPanel.add(getClassBrowserPanel(), gridBagConstraints16);
			processorConfigPanel.add(getProcessorConfigurationPanel(), gridBagConstraints22);
		}
		return processorConfigPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getDetailConfigPanel() {
		if (detailConfigPanel == null) {
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 0;
			gridBagConstraints18.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints18.gridy = 1;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints17.gridy = 0;
			gridBagConstraints17.weightx = 1.0;
			gridBagConstraints17.weighty = 1.0D;
			gridBagConstraints17.gridx = 0;
			detailConfigPanel = new JPanel();
			detailConfigPanel.setLayout(new GridBagLayout());
			detailConfigPanel.add(getClassConfigScrollPane(), gridBagConstraints17);
			detailConfigPanel.add(getValidationCheckPanel(), gridBagConstraints18);
		}
		return detailConfigPanel;
	}


	private QueryProcessorParametersTable getQpParamsTable() {
		if (qpParamsTable == null) {
			qpParamsTable = new QueryProcessorParametersTable(getExtensionTypeExtensionData(), getServiceInfo());
            // uncomment the following to edit with VE
            // qpParamsTable = new QueryProcessorParametersTable(null, null);
		}
		return qpParamsTable;
	}


	private JScrollPane getQpParamsScrollPane() {
		if (qpParamsScrollPane == null) {
			qpParamsScrollPane = new JScrollPane();
			qpParamsScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
				"Processor Parameter Configuration", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			qpParamsScrollPane.setViewportView(getQpParamsTable());
		}
		return qpParamsScrollPane;
	}


	private void saveProcessorClassName(String className) throws Exception {
		// store the property
		CommonTools.setServiceProperty(getServiceInfo().getServiceDescriptor(),
			DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY, className, false);
		// remove all query processor config properties from the service properties
		ServicePropertiesProperty[] oldProperties = getServiceInfo().getServiceProperties().getProperty();
		List keptProperties = new ArrayList();
		for (int i = 0; i < oldProperties.length; i++) {
			if (!oldProperties[i].getKey().startsWith(DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX)) {
				keptProperties.add(oldProperties[i]);
			}
		}
		ServicePropertiesProperty[] properties = new ServicePropertiesProperty[keptProperties.size()];
		keptProperties.toArray(properties);
		getServiceInfo().getServiceDescriptor().getServiceProperties().setProperty(properties);
		// inform the parameters table that the class name is different
		getQpParamsTable().classChanged();
	}


    /**
     * This method initializes processorConfigurationPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getProcessorConfigurationPanel() {
        if (processorConfigurationPanel == null) {
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.gridx = 0;
            gridBagConstraints21.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints21.gridy = 0;
            GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
            gridBagConstraints20.fill = GridBagConstraints.BOTH;
            gridBagConstraints20.gridx = 0;
            gridBagConstraints20.gridy = 1;
            gridBagConstraints20.weightx = 1.0D;
            gridBagConstraints20.weighty = 1.0D;
            gridBagConstraints20.insets = new Insets(6, 6, 6, 6);
            processorConfigurationPanel = new JPanel();
            processorConfigurationPanel.setLayout(new GridBagLayout());
            processorConfigurationPanel.add(getQpParamsScrollPane(), gridBagConstraints20);
            processorConfigurationPanel.add(getLaunchProcessorConfigButton(), gridBagConstraints21);
        }
        return processorConfigurationPanel;
    }


    /**
     * This method initializes launchProcessorConfigButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getLaunchProcessorConfigButton() {
        if (launchProcessorConfigButton == null) {
            launchProcessorConfigButton = new JButton();
            launchProcessorConfigButton.setText("Launch Query Processor Configurator");
            launchProcessorConfigButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    launchQueryProcessorConfigUi();
                }
            });
        }
        return launchProcessorConfigButton;
    }
    
    
    private String[] getJarFilenames() {
        String libDir = getServiceInfo().getIntroduceServiceProperties().getProperty(
            IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR)
            + File.separator + "lib";
        String[] qpJarNames = getClassBrowserPanel().getAdditionalJars();
        if (qpJarNames != null) {
            for (int i = 0; i < qpJarNames.length; i++) {
                qpJarNames[i] = libDir + File.separator + qpJarNames[i];
            }
        }
        return qpJarNames;
    }
    
    
    private void launchQueryProcessorConfigUi() {
        String qpClassname = getClassBrowserPanel().getSelectedClassName();
        if (qpClassname != null && qpClassname.length() != 0) {
            try {
                // reflect-load the class
                String[] libs = getJarFilenames();
                URL[] urls = new URL[libs.length];
                for (int i = 0; i < libs.length; i++) {
                    File libFile = new File(libs[i]);
                    urls[i] = libFile.toURL();
                }
                ClassLoader loader = new URLClassLoader(
                    urls, Thread.currentThread().getContextClassLoader());
                Class qpClass = loader.loadClass(qpClassname);
                CQLQueryProcessor processorInstance = (CQLQueryProcessor) qpClass.newInstance();
                String configUiCLassname = processorInstance.getConfigurationUiClassname();
                if (configUiCLassname != null) {
                    Class uiClass = loader.loadClass(configUiCLassname);
                    CQLQueryProcessorConfigUI uiPanel = 
                        (CQLQueryProcessorConfigUI) uiClass.newInstance();
                    // get the current configuration out of the table
                    Properties currentConfig = getQpParamsTable().getNonPrefixedConfiguredProperties();
                    Properties postUiConfig = QueryProcessorConfigurationDialog
                        .showConfigurationUi(uiPanel, currentConfig);
                    // store the configuration that came back from the UI config dialog
                    // start by removing the old query processor properties
                    ServicePropertiesProperty[] oldProperties = 
                        getServiceInfo().getServiceProperties().getProperty();
                    List<ServicePropertiesProperty> keptProperties = new ArrayList();
                    for (ServicePropertiesProperty prop : oldProperties) {
                        if (!prop.getKey().startsWith(DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX)) {
                            keptProperties.add(prop);
                        }
                    }
                    // add the changed properties
                    Iterator postUiPropKeys = postUiConfig.keySet().iterator();
                    while (postUiPropKeys.hasNext()) {
                        String key = (String) postUiPropKeys.next();
                        String value = postUiConfig.getProperty(key);
                        String prefixedKey = DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + key;
                        ServicePropertiesProperty configProperty = new ServicePropertiesProperty();
                        configProperty.setKey(prefixedKey);
                        configProperty.setValue(value);
                        configProperty.setIsFromETC(Boolean.FALSE);
                        keptProperties.add(configProperty);
                    }
                    // set the properties into the model
                    ServicePropertiesProperty[] properties = 
                        new ServicePropertiesProperty[keptProperties.size()];
                    keptProperties.toArray(properties);
                    getServiceInfo().getServiceDescriptor().getServiceProperties().setProperty(properties);
                    // inform the parameters table that it should update
                    getQpParamsTable().classChanged();
                } else {
                    PortalUtils.showMessage(new String[] {
                        "The query processor " + qpClassname,
                    "did not supply a configuration UI"});
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                ErrorDialog.showErrorDialog("Error loading query processor class: " + ex.getMessage(), ex);
            }
        }
    }
}