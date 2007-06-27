package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.extension.ClassMapping;
import gov.nih.nci.cagrid.data.ui.auditors.AuditorsConfigurationPanel;
import gov.nih.nci.cagrid.data.ui.table.ClassChangeEvent;
import gov.nih.nci.cagrid.data.ui.table.ClassElementSerializationTable;
import gov.nih.nci.cagrid.data.ui.table.ClassInformatonChangeListener;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.ServiceModificationUIPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;

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
	private JCheckBox cqlSyntaxValidationCheckBox = null;
	private JCheckBox domainModelValidationCheckBox = null;
	private JScrollPane classConfigScrollPane = null;
	private JPanel validationCheckPanel = null;
	private DomainModelConfigPanel domainConfigPanel = null;
	private JTabbedPane mainTabbedPane = null;
	private QueryProcessorConfigPanel processorConfigPanel = null;
	private JPanel detailConfigPanel = null;
	private EnumIteratorSelectionPanel iterSelectionPanel = null;
    private AuditorsConfigurationPanel auditorConfigPanel = null;

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
        getDomainConfigPanel().populateFromExtensionData();
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


    private EnumIteratorSelectionPanel getIterSelectionPanel() {
        if (iterSelectionPanel == null) {
            iterSelectionPanel = new EnumIteratorSelectionPanel(getServiceInfo());
        }
        return iterSelectionPanel;
    }
    
    
    private AuditorsConfigurationPanel getAuditorConfigPanel() {
        if (auditorConfigPanel == null) {
            auditorConfigPanel = new AuditorsConfigurationPanel(getServiceInfo());
        }
        return auditorConfigPanel;
    }
}