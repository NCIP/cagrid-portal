package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ui.browser.AdditionalJarsChangeListener;
import gov.nih.nci.cagrid.data.ui.browser.AdditionalJarsChangedEvent;
import gov.nih.nci.cagrid.data.ui.browser.ClassBrowserPanel;
import gov.nih.nci.cagrid.data.ui.browser.ClassSelectionEvent;
import gov.nih.nci.cagrid.data.ui.browser.ClassSelectionListener;
import gov.nih.nci.cagrid.data.ui.browser.QueryProcessorClassConfigDialog;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.extension.utils.AxisJdomUtils;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.ServiceModificationUIPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.axis.message.MessageElement;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.projectmobius.client.gme.ImportInfo;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.common.gme.NoSuchSchemaException;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.projectmobius.protocol.gme.SchemaNode;

/** 
 *  TargetTypeSelectionPanel
 *  Panel for selecting target data types from a domain model
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 20, 2006 
 * @version $Id$ 
 */
public class TargetTypeSelectionPanel extends ServiceModificationUIPanel {
	
	private DomainBrowserPanel domainBrowserPanel = null;
	private TargetTypesTree typesTree = null;
	private JScrollPane typesTreeScrollPane = null;
	private DataServiceTypesTable typesTable = null;
	private JScrollPane typesTableScrollPane = null;
	private JPanel typeSelectionPanel = null;
	private JButton setModelButton = null;
	private ClassBrowserPanel classBrowserPanel = null;
	private JPanel configurationPanel = null;
	private JButton configureButton = null;
	
	private transient XMLDataModelService gmeHandle = null;
	private JButton selectDomainModelButton = null;
	private JTextField domainModelNameTextField = null;
	private JPanel domainModelSelectionPanel = null;  //  @jve:decl-index=0:visual-constraint="446,639"
	
	public TargetTypeSelectionPanel(ServiceExtensionDescriptionType desc, ServiceInformation serviceInfo) {
		super(desc, serviceInfo);
		initialize();
	}
	
	
	private void initialize() {
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.gridy = 0;
		gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints4.weighty = 1.0D;
		gridBagConstraints4.weightx = 1.0D;				
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 1;
		gridBagConstraints5.gridy = 0;
		gridBagConstraints5.fill = GridBagConstraints.BOTH;
		gridBagConstraints5.weightx = 1.0D;
		gridBagConstraints5.weighty = 1.0D;
		this.setLayout(new GridBagLayout());
		this.setSize(new java.awt.Dimension(1017,548));
		this.add(getTypeSelectionPanel(), gridBagConstraints4);
		this.add(getConfigurationPanel(), gridBagConstraints5);
	}
	
	
	private DomainBrowserPanel getDomainBrowserPanel() {
		if (domainBrowserPanel == null) {
			domainBrowserPanel = new DomainBrowserPanel(true, false);
			String url = null;
			MessageElement cadsrElement = ExtensionTools.getExtensionDataElement(
				getExtensionTypeExtensionData(), DataServiceConstants.CADSR_ELEMENT_NAME);
			if (cadsrElement != null) {
				// if there's existing caDSR info in the data service, set the browser panel to show it
				url = cadsrElement.getAttribute(DataServiceConstants.CADSR_URL_ATTRIB);
				String project = cadsrElement.getAttribute(DataServiceConstants.CADSR_PROJECT_ATTRIB);
				String pack = cadsrElement.getAttribute(DataServiceConstants.CADSR_PACKAGE_ATTRIB);
				domainBrowserPanel.setDefaultCaDSRURL(url);
				domainBrowserPanel.getCadsr().setText(url);
				domainBrowserPanel.blockingCadsrRefresh();
				domainBrowserPanel.setSelectedProject(project);
				domainBrowserPanel.setSelectedPackage(pack);
			} else {
				// get the default caDSR url out of the extension config
				url = ExtensionTools.getProperty(getExtensionDescription().getProperties(), "CADSR_URL");
				if (url != null) {
					domainBrowserPanel.setDefaultCaDSRURL(url);
					domainBrowserPanel.getCadsr().setText(url);
					domainBrowserPanel.blockingCadsrRefresh();
				}
			}
		}
		return domainBrowserPanel;
	}
	
	
	private TargetTypesTree getTypesTree() {
		if (typesTree == null) {
			typesTree = new TargetTypesTree();
			typesTree.addTypeSelectionListener(new TypeSelectionListener() {
				public void typeSelectionAdded(TypeSelectionEvent e) {
					getTypesTable().addType(getTypesTree().getOriginalNamespace(), e.getSchemaElementType());
					addTreeNamespaceToServiceDescription();
				}
				
				
				public void typeSelectionRemoved(TypeSelectionEvent e) {
					getTypesTable().removeSchemaElementType(e.getSchemaElementType());
					addTreeNamespaceToServiceDescription();
				}
			});
			// see if there is already a targeted namespace to display
			String targetNamespace = getTargetModelNamespace();
			if (targetNamespace != null) {
				for (int i = 0; i < getServiceInfo().getNamespaces().getNamespace().length; i++) {
					NamespaceType ns = getServiceInfo().getNamespaces().getNamespace(i);
					if (ns.getNamespace().equals(targetNamespace)) {
						typesTree.setNamespace(ns);
						typesTree.checkSchemaNodes();
						break;
					}
				}
			}
		}
		return typesTree;
	}
	
	
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getTypesTreeScrollPane() {
		if (typesTreeScrollPane == null) {
			typesTreeScrollPane = new JScrollPane();
			typesTreeScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Model Data Types", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			typesTreeScrollPane.setViewportView(getTypesTree());
		}
		return typesTreeScrollPane;
	}
	
	
	private DataServiceTypesTable getTypesTable() {
		if (typesTable == null) {
			typesTable = new DataServiceTypesTable();
		}
		return typesTable;
	}
	
	
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getTypesTableScrollPane() {
		if (typesTableScrollPane == null) {
			typesTableScrollPane = new JScrollPane();
			typesTableScrollPane.setViewportView(getTypesTable());
			typesTableScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Type Serialization", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
		}
		return typesTableScrollPane;
	}
	
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTypeSelectionPanel() {
		if (typeSelectionPanel == null) {
			GridBagConstraints gridBagCosntraints32 = new GridBagConstraints();
			gridBagCosntraints32.gridx = 0;
			gridBagCosntraints32.gridy = 3;
			gridBagCosntraints32.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints31.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 2;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			typeSelectionPanel = new JPanel();
			typeSelectionPanel.setLayout(new GridBagLayout());
			typeSelectionPanel.add(getDomainBrowserPanel(), gridBagConstraints);
			typeSelectionPanel.add(getTypesTreeScrollPane(), gridBagConstraints1);
			typeSelectionPanel.add(getSetModelButton(), gridBagConstraints31);
			typeSelectionPanel.add(getDomainModelSelectionPanel(), gridBagCosntraints32);
		}
		return typeSelectionPanel;
	}
	
	
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSetModelButton() {
		if (setModelButton == null) {
			setModelButton = new JButton();
			setModelButton.setText("Set Model");
			setModelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					UMLPackageMetadata pack = getDomainBrowserPanel().getSelectedPackage();
					if (pack != null) {
						NamespaceType nsType = createNamespaceFromUmlPackage(pack);
						if (nsType != null) {
							getTypesTree().setNamespace(nsType);
							addTreeNamespaceToServiceDescription();
							storeTargetModelNamespace(nsType.getNamespace());
						}
					}
					storeCaDSRInfo();
					// clear out the types table
					while (getTypesTable().getRowCount() != 0) {
						getTypesTable().removeSchemaElementType(0);
					}
				}
			});
		}
		return setModelButton;
	}
	
	
	private NamespaceType createNamespaceFromUmlPackage(UMLPackageMetadata pack) {
		String namespaceString = null;
		Project proj = getDomainBrowserPanel().getSelectedProject();
		if (proj != null) {
			// TODO: need to get Context
			String version = proj.getVersion();
			if (version.indexOf(".") == -1) {
				version += ".0";
			}
			namespaceString = "gme://" + proj.getShortName() + ".caBIG/" + version + "/" + pack.getName();
			NamespaceType nsType = new NamespaceType();
			try {
				Namespace namespace = new Namespace(namespaceString);
				List namespaceDomainList = getGME().getNamespaceDomainList();
				if (!namespaceDomainList.contains(namespace.getDomain())) {
					// prompt for alternate
					String alternativeDomain = (String) JOptionPane.showInputDialog(this,
						"The GME does not appear to contain schemas under the specified domain.\n"
						+ "Select an alternative domain, or cancel if no viable option is available.\n"
						+ "\nExpected domain: " + namespace.getDomain(), "Schema Location Error",
						JOptionPane.ERROR_MESSAGE, null, namespaceDomainList.toArray(), null);
					
					if (alternativeDomain != null) {
						namespace = new Namespace(namespace.getProtocol() + "://" + alternativeDomain + "/"
							+ namespace.getName());
					} else {
						return null;
					}
				}
				String schemaContents = null;
				try {
					schemaContents = getSchema(namespace);
				} catch (NoSuchSchemaException e) {
					// prompt for alternate
					List schemas = getGME().getSchemaListForNamespaceDomain(namespace.getDomain());
					Namespace alternativeSchema = (Namespace) JOptionPane.showInputDialog(this,
						"Unable to locate schema for the selected caDSR package.\n"
						+ "This package may not have a published Schema."
						+ "\nSelect an alternative Schema, or cancel.\n\nExpected schema: " + namespace.getName(),
						"Schema Location Error", JOptionPane.ERROR_MESSAGE, null, schemas.toArray(), null);
					
					if (alternativeSchema != null) {
						namespace = alternativeSchema;
					} else {
						return null;
					}
					schemaContents = getSchema(namespace);
				}
				
				// set the package name
				String packageName = CommonTools.getPackageName(namespace);
				nsType.setPackageName(packageName);
				
				nsType.setNamespace(namespace.getRaw());
				ImportInfo ii = new ImportInfo(namespace);
				nsType.setLocation("./" + ii.getFileName());
				
				// popualte the schema elements
				gov.nih.nci.cagrid.introduce.portal.extension.ExtensionTools.setSchemaElements(
					nsType, XMLUtilities.stringToDocument(schemaContents));
				// write the schema and its imports to the filesystem
				getGME().cacheSchema(namespace, getSchemaDir());
				return nsType;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	
	private void addTreeNamespaceToServiceDescription() {
		NamespaceType userNamespace = getTypesTree().getUserDefinedNamespace();
		NamespaceType[] serviceNamespaces = getServiceInfo().getServiceDescriptor().getNamespaces().getNamespace();
		boolean namespaceFound = false;
		for (int i = 0; i < serviceNamespaces.length && !namespaceFound; i++) {
			NamespaceType ns = serviceNamespaces[i];
			if (ns.getNamespace().equals(userNamespace.getNamespace())) {
				// System.out.println("Found namespace " + ns.getNamespace() + " and replacing with user's namespace");
				serviceNamespaces[i] = userNamespace;
				namespaceFound = true;
			}
		}
		if (!namespaceFound) {
			// System.out.println("Never found namespace (new namespace).  Adding tree's namespace");
			NamespaceType[] moreNamespaces = new NamespaceType[serviceNamespaces.length + 1];
			System.arraycopy(serviceNamespaces, 0, moreNamespaces, 0, serviceNamespaces.length);
			moreNamespaces[moreNamespaces.length - 1] = userNamespace;
			serviceNamespaces = moreNamespaces;
		}
		getServiceInfo().getServiceDescriptor().getNamespaces().setNamespace(serviceNamespaces);
	}
	
	
	private XMLDataModelService getGME() throws MobiusException {
		if (gmeHandle == null) {
			String serviceId = ExtensionTools.getProperty(getExtensionDescription().getProperties(), "GME_URL");
			GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
			gmeHandle = (XMLDataModelService) GridServiceResolver.getInstance()
				.getGridService(serviceId);
		}
		return gmeHandle;
	}
	
	
	private String getSchema(Namespace namespace) throws Exception {
		SchemaNode schema = getGME().getSchema(namespace, false);
		return schema.getSchemaContents();
	}
	
	
	private File getSchemaDir() {
		String dir = getServiceInfo().getBaseDirectory().getAbsolutePath() + File.separator +
			"schema" + File.separator + getServiceInfo().getIntroduceServiceProperties()
			.getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
		return new File(dir);
	}
	
	
	private ClassBrowserPanel getClassBrowserPanel() {
		if (classBrowserPanel == null) {
			classBrowserPanel = new ClassBrowserPanel(getExtensionTypeExtensionData(), getServiceInfo());
			// classBrowserPanel = new ClassBrowserPanel(null, null); // uncomment this line to edit in VE
			classBrowserPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Query Processor Class Selection", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			// listen for class selection events
			classBrowserPanel.addClassSelectionListener(new ClassSelectionListener() {
				public void classSelectionChanged(ClassSelectionEvent e) {
					setProcessorClass(classBrowserPanel.getSelectedClassName());
				}
			});
			// listen for jar addition events
			classBrowserPanel.addAdditionalJarsChangeListener(new AdditionalJarsChangeListener() {
				public void additionalJarsChanged(AdditionalJarsChangedEvent e) {
					// remove any existing qp jars element from the service data
					ExtensionTools.removeExtensionDataElement(
						getExtensionTypeExtensionData(), DataServiceConstants.QUERY_PROCESSOR_ADDITIONAL_JARS_ELEMENT);
					// create a new qp jars element
					Element qpJars = new Element(DataServiceConstants.QUERY_PROCESSOR_ADDITIONAL_JARS_ELEMENT);
					String[] additionalJars = classBrowserPanel.getAdditionalJars();
					for (int i = 0; i < additionalJars.length; i++) {
						Element jarElem = new Element(DataServiceConstants.QUERY_PROCESSOR_JAR_ELEMENT);
						jarElem.setText(additionalJars[i]);
						qpJars.addContent(jarElem);
					}
					try {
						MessageElement qpJarsElement = AxisJdomUtils.fromElement(qpJars);
						ExtensionTools.updateExtensionDataElement(
							getExtensionTypeExtensionData(), qpJarsElement);
					} catch (JDOMException ex) {
						ex.printStackTrace();
						PortalUtils.showErrorMessage("Error storing query processor jars", ex);
					}
				}
			});
		}
		return classBrowserPanel;
	}
	
	
	private void setProcessorClass(String className) {
		if (className != null) {
			CommonTools.setServiceProperty(
				getServiceInfo(), DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY, className);
		}
	}
	
	
	private void storeCaDSRInfo() {
		ExtensionTypeExtensionData extensionData = getExtensionTypeExtensionData();
		Element cadsr = new Element(DataServiceConstants.CADSR_ELEMENT_NAME);
		cadsr.setAttribute(DataServiceConstants.CADSR_URL_ATTRIB,
			getDomainBrowserPanel().getCadsr().getText());
		cadsr.setAttribute(DataServiceConstants.CADSR_PROJECT_ATTRIB,
			getDomainBrowserPanel().getSelectedProject().getLongName());
		cadsr.setAttribute(DataServiceConstants.CADSR_PACKAGE_ATTRIB,
			getDomainBrowserPanel().getSelectedPackage().getName());
		MessageElement cadsrElement = null;
		try {
			cadsrElement = AxisJdomUtils.fromElement(cadsr);
			ExtensionTools.updateExtensionDataElement(extensionData, cadsrElement);
		} catch (JDOMException ex) {
			// TODO: probably throw a codegen exception or something reasonable
			ex.printStackTrace();
			PortalUtils.showErrorMessage("Error storing caDSR information!", ex);
		}
	}
	
	
	private void storeTargetModelNamespace(String namespace) {
		ExtensionTypeExtensionData data = getExtensionTypeExtensionData();
		Element target = new Element(DataServiceConstants.DATA_MODEL_ELEMENT_NAME);
		target.setText(namespace);
		MessageElement targetElement = null;
		try {
			targetElement = AxisJdomUtils.fromElement(target);
			ExtensionTools.updateExtensionDataElement(data, targetElement);
		} catch (JDOMException ex) {
			ex.printStackTrace();
			PortalUtils.showErrorMessage("Error storing target model's namespace", ex);
		}		
	}
	
	
	private String getTargetModelNamespace() {
		ExtensionTypeExtensionData data = getExtensionTypeExtensionData();
		MessageElement modelElement = ExtensionTools.getExtensionDataElement(data, DataServiceConstants.DATA_MODEL_ELEMENT_NAME);
		if (modelElement != null) {
			if (modelElement.getValue() != null && modelElement.getValue().length() != 0) {
				return modelElement.getValue();
			}
		}
		return null;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getConfigurationPanel() {
		if (configurationPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 2;
			gridBagConstraints4.gridx = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.ipady = 80;
			gridBagConstraints2.gridx = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.weighty = 1.0D;
			gridBagConstraints3.gridx = 0;
			configurationPanel = new JPanel();
			configurationPanel.setLayout(new GridBagLayout());
			configurationPanel.add(getConfigureButton(), gridBagConstraints4);
			configurationPanel.add(getTypesTableScrollPane(), gridBagConstraints3);
			configurationPanel.add(getClassBrowserPanel(), gridBagConstraints2);
		}
		return configurationPanel;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getConfigureButton() {
		if (configureButton == null) {
			configureButton = new JButton();
			configureButton.setText("Configure Selected Query Processor");
			configureButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (getClassBrowserPanel().getSelectedClassName() != null) {
						new QueryProcessorClassConfigDialog(
							getExtensionTypeExtensionData(), getServiceInfo());
					}
				}
			});
		}
		return configureButton;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSelectDomainModelButton() {
		if (selectDomainModelButton == null) {
			selectDomainModelButton = new JButton();
			selectDomainModelButton.setText("Select Domain Model");
			selectDomainModelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						String filename = ResourceManager.promptFile(
							TargetTypeSelectionPanel.this, null, new FileFilters.XMLFileFilter());
						getDomainModelNameTextField().setText(filename);
					} catch (Exception ex) {
						ex.printStackTrace();
						PortalUtils.showErrorMessage("Error selecting file", ex);
					}
				}
			});
		}
		return selectDomainModelButton;
	}


	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDomainModelNameTextField() {
		if (domainModelNameTextField == null) {
			domainModelNameTextField = new JTextField();
			domainModelNameTextField.setToolTipText("Optional xml file name of an existing domain model");
			domainModelNameTextField.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					setDomainModelFile();
				}

				
			    public void removeUpdate(DocumentEvent e) {
			    	setDomainModelFile();
			    }

			    
			    public void changedUpdate(DocumentEvent e) {
			    	setDomainModelFile();
			    }
			    
			    
			    private void setDomainModelFile() {
			    	ExtensionTypeExtensionData data = getExtensionTypeExtensionData();
			    	String filename = getDomainModelNameTextField().getText();
			    	if (filename == null || filename.length() == 0) {
			    		ExtensionTools.removeExtensionDataElement(data, DataServiceConstants.SUPPLIED_DOMAIN_MODEL);
			    	} else {
			    		Element elem = new Element(DataServiceConstants.SUPPLIED_DOMAIN_MODEL);
			    		elem.setText(filename);
			    		try {
			    			MessageElement messageElem = AxisJdomUtils.fromElement(elem);
			    			ExtensionTools.updateExtensionDataElement(data, messageElem);
			    		} catch (Exception ex) {
			    			ex.printStackTrace();
			    			PortalUtils.showErrorMessage("Error storing domain model filename", ex);
			    		}
			    	}
			    }
			});
		}
		return domainModelNameTextField;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDomainModelSelectionPanel() {
		if (domainModelSelectionPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints7.gridy = 0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints6.gridx = 0;
			domainModelSelectionPanel = new JPanel();
			domainModelSelectionPanel.setLayout(new GridBagLayout());
			domainModelSelectionPanel.setSize(new java.awt.Dimension(295,69));
			domainModelSelectionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Optional Supplied Domain Model", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			domainModelSelectionPanel.add(getDomainModelNameTextField(), gridBagConstraints6);
			domainModelSelectionPanel.add(getSelectDomainModelButton(), gridBagConstraints7);
		}
		return domainModelSelectionPanel;
	}
}
