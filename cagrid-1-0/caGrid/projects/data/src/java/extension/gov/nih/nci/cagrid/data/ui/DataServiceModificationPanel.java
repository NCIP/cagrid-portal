package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.cadsr.portal.CaDSRBrowserPanel;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.common.portal.PromptButtonDialog;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.AdditionalLibraries;
import gov.nih.nci.cagrid.data.extension.CadsrInformation;
import gov.nih.nci.cagrid.data.extension.CadsrPackage;
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
import gov.nih.nci.cagrid.data.ui.tree.CheckTreeSelectionEvent;
import gov.nih.nci.cagrid.data.ui.tree.CheckTreeSelectionListener;
import gov.nih.nci.cagrid.data.ui.tree.uml.UMLClassTreeNode;
import gov.nih.nci.cagrid.data.ui.tree.uml.UMLPackageTreeNode;
import gov.nih.nci.cagrid.data.ui.tree.uml.UMLProjectTree;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.Properties;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.ServiceModificationUIPanel;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClass;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.projectmobius.portal.PortalResourceManager;

/** 
 *  DataServiceModificationPanel
 *  
 *  Panel for configuring a caGrid data service from within the Introduce Toolkit
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Oct 10, 2006 
 * @version $Id$ 
 */
public class DataServiceModificationPanel extends ServiceModificationUIPanel {
	
	private JButton selectDomainModelButton = null;
	private JTextField domainModelNameTextField = null;
	private JPanel domainModelSelectionPanel = null;
	private JButton addFullProjectButton = null;
	private JButton addPackageButton = null;
	private JButton removePackageButton = null;	
	private CaDSRBrowserPanel cadsrBrowserPanel = null;
	private UMLProjectTree umlTree = null;
	private ClassElementSerializationTable classConfigTable = null;
	private ClassBrowserPanel classBrowserPanel = null;
	private JCheckBox cqlSyntaxValidationCheckBox = null;
	private JCheckBox domainModelValidationCheckBox = null;
	private JScrollPane classConfigScrollPane = null;
	private JPanel validationCheckPanel = null;
	private JScrollPane umlClassScrollPane = null;
	private JPanel packageSelectionButtonPanel = null;
	private JRadioButton noDomainModelRadioButton = null;
	private JRadioButton cadsrDomainModelRadioButton = null;
	private JRadioButton suppliedDomainModelRadioButton = null;
	private JPanel domainModelSourcePanel = null;
	private JPanel domainConfigPanel = null;
	private JPanel cadsrDomainModelPanel = null;
	private JTabbedPane mainTabbedPane = null;
	private JPanel processorConfigPanel = null;
	private JPanel detailConfigPanel = null;
	private QueryProcessorParametersTable qpParamsTable = null;
	private JScrollPane qpParamsScrollPane = null;
	
	private transient Project mostRecentProject = null;
	private transient Map packageToNamespace = null;
	private transient Map packageToClassMap = null;
	
	public DataServiceModificationPanel(ServiceExtensionDescriptionType desc, ServiceInformation info) {
		super(desc, info);
		packageToNamespace = new HashMap();
		packageToClassMap = new HashMap();
		loadMostRecentProjectInfo();
		// if there's existing cadsr configuration, apply it
		loadUmlTreeInformation();
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
	
	
	private CaDSRBrowserPanel getCadsrBrowserPanel() {
		if (cadsrBrowserPanel == null) {
			cadsrBrowserPanel = new CaDSRBrowserPanel(true, false);
			String url = null;
			CadsrInformation cadsrInfo = null;
			try {
				cadsrInfo = ExtensionDataUtils.getExtensionData(getExtensionTypeExtensionData()).getCadsrInformation();
			} catch (Exception ex) {
				ex.printStackTrace();
				ErrorDialog.showErrorDialog("Error loading caDSR information from extension data", ex);
			}
			// if there's existing caDSR info in the data service, set the browser panel to show it
			if (cadsrInfo != null) {				
				// url of the cadsr service
				url = cadsrInfo.getServiceUrl();
				
				// project name and version
				String projectName = cadsrInfo.getProjectLongName();
				String projectVersion = cadsrInfo.getProjectVersion();
				
				// store the project info as the most recent project
				mostRecentProject = new Project();
				mostRecentProject.setLongName(projectName);
				mostRecentProject.setVersion(projectVersion);
			} else {
				// get the caDSR url out of the Introduce property bucket
				url = ResourceManager.getServiceURLProperty(DataServiceConstants.CADSR_SERVICE_URL);
				// store the just-loaded URL in the service information
				storeCadsrServiceUrl();
			}
			if (url != null) {
				// configure selected items in the cadsr panel
				cadsrBrowserPanel.setDefaultCaDSRURL(url);
				cadsrBrowserPanel.getCadsr().setText(url);
				cadsrBrowserPanel.discoverFromCaDSR();
			}
			// add listener to the cadsr URL text field
			// TODO: this is probably really slow; make it better
			cadsrBrowserPanel.getCadsr().getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					storeCadsrServiceUrl();
				}

				
				public void removeUpdate(DocumentEvent e) {
					storeCadsrServiceUrl();
				}

			    
			    public void changedUpdate(DocumentEvent e) {
			    	storeCadsrServiceUrl();
			    }
			});
		}
		return cadsrBrowserPanel;
	}
	
	
	private UMLProjectTree getUmlTree() {
		if (umlTree == null) {
			umlTree = new UMLProjectTree();
			umlTree.addCheckTreeSelectionListener(new CheckTreeSelectionListener() {
				public void nodeChecked(CheckTreeSelectionEvent e) {
					if (e.getNode() instanceof UMLClassTreeNode) {
						UMLClassTreeNode classNode = (UMLClassTreeNode) e.getNode();
						// add the type to the configuration table
						String packName = ((UMLPackageTreeNode) classNode.getParent()).getPackageName();
						String className = classNode.getClassName();
						String namespace = (String) packageToNamespace.get(packName);
						NamespaceType nsType = CommonTools.getNamespaceType(getServiceInfo().getNamespaces(), namespace);
						try {
							ClassMapping mapping = getClassMapping(packName, className);
							mapping.setSelected(true);
							getClassConfigTable().addClass(packName, mapping, nsType);
							storeClassMapping(packName, mapping);
						} catch (Exception ex) {
							ex.printStackTrace();
							ErrorDialog.showErrorDialog("Error getting mapping for " + packName + "." + className, ex);
						}						
					}
				}
				
				
				public void nodeUnchecked(CheckTreeSelectionEvent e) {
					if (e.getNode() instanceof UMLClassTreeNode) {
						UMLClassTreeNode classNode = (UMLClassTreeNode) e.getNode();
						// add the type to the configuration table
						String packName = ((UMLPackageTreeNode) classNode.getParent()).getPackageName();
						String className = classNode.getClassName();
						getClassConfigTable().removeRow(packName, className);
						unselectClassMapping(packName, className);
					}
				}
			});
		}
		return umlTree;
	}
	
	
	private ClassElementSerializationTable getClassConfigTable() {
		if (classConfigTable == null) {
			classConfigTable = new ClassElementSerializationTable();
			classConfigTable.addClassInformatonChangeListener(new ClassInformatonChangeListener() {
				public void elementNameChanged(ClassChangeEvent e) {
					// get the namespace type for the class
					NamespaceType nsType = CommonTools.getNamespaceType(
						getServiceInfo().getNamespaces(), e.getNamespace());
					// find the schema element type
					SchemaElementType schemaType = NamespaceUtils.getElementByName(
						nsType, e.getElementName());
					if (schemaType == null) {
						// WARNING: You've selected a non-existant element name!
						ErrorDialog.showErrorDialog("No element named " + e.getElementName() + " in namespace " + e.getNamespace());
					}
					// get class to element mapping
					Map classToElement = (Map) packageToClassMap.get(e.getPackageName());
					// change the element name mapping
					classToElement.put(e.getClassName(), e.getElementName());
					// save the mapping info
					try {
						ClassMapping mapping = getClassMapping(e.getPackageName(), e.getClassName());
						mapping.setElementName(e.getElementName());
						storeClassMapping(e.getPackageName(), mapping);
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
					// very real posibility the user has changed the element name
					// to something not in the schema
					// TODO: can I prevent that???
					if (schemaType != null) {
						schemaType.setSerializer(e.getSerializer());
						schemaType.setDeserializer(e.getDeserializer());
					} else {
						ErrorDialog.showErrorDialog("No element named " + e.getElementName() + " in namespace " + e.getNamespace(), 
							"Serialization for class " + e.getPackageName() + "." + e.getClassName() + " was not changed!");
					}
				}
				
				
				public void targetabilityChanged(ClassChangeEvent e) {
					try {
						// get the old class mapping
						ClassMapping mapping = getClassMapping(e.getPackageName(), e.getClassName());
						// change the targetability
						mapping.setTargetable(e.isTargetable());
						// save the changes
						storeClassMapping(e.getPackageName(), mapping);
					} catch (Exception ex) {
						ex.printStackTrace();
						ErrorDialog.showErrorDialog("Error storing change to targetability", ex);
					}
				}
			});
		}
		return classConfigTable;
	}
	
	
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddPackageButton() {
		if (addPackageButton == null) {
			addPackageButton = new JButton();
			addPackageButton.setText("Add Package");
			addPackageButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// verify we're in the same project as the other packages
					Project selectedProject = getCadsrBrowserPanel().getSelectedProject();
					boolean shouldAddPackage = true;
					if (mostRecentProject != null &&
						(!mostRecentProject.getLongName().equals(selectedProject.getLongName()) ||
						!mostRecentProject.getVersion().equals(selectedProject.getVersion()))) {
						// not the same project, can't allow packages from more than one project!
						String[] choices = {"Remove all other packages and insert", "Cancel"};
						String[] message = {
							"Domain models may only be derived from one project.",
							"To add the package you've selected, all other packages",
							"currently in the domain model will have to be removed.",
							"Should this operation procede?"
						};
						String choice = PromptButtonDialog.prompt(
							PortalResourceManager.getInstance().getGridPortal(),
							"Project incompatability...", message, choices, choices[1]);
						if (choice == choices[0]) {
							// try to remove the namespaces from the service
							Iterator nsNameIter = packageToNamespace.values().iterator();
							while (nsNameIter.hasNext()) {
								String namespace = (String) nsNameIter.next();
								NamespaceType nsType = CommonTools.getNamespaceType(
									getServiceInfo().getNamespaces(), namespace);
								if (!CommonTools.isNamespaceTypeInUse(nsType, getServiceInfo().getServiceDescriptor())) {
									NamespaceType[] allNamespaces = getServiceInfo().getNamespaces().getNamespace();
									NamespaceType[] cleanedNamespaces = (NamespaceType[]) Utils.removeFromArray(
										allNamespaces, nsType);
									getServiceInfo().getNamespaces().setNamespace(cleanedNamespaces);
								}
							}
							// ok, clear out the existing packages and classes
							packageToNamespace.clear();
							packageToClassMap.clear();
							// clear out the types table
							getClassConfigTable().clearTable();
							// clear out the types tree
							getUmlTree().clearTree();
						} else {
							shouldAddPackage = false;
						}
					}
					if (shouldAddPackage) {
						UMLPackageMetadata pack = getCadsrBrowserPanel().getSelectedPackage();
						if (pack != null) {
							addPackageToModel(selectedProject, pack);
							// change the most recently added project
							mostRecentProject = selectedProject;
							// store the (potential) change to project info
							storeMostRecentProjectInformation();
							// store the package information
							storeUpdatedPackageInformation();
						}
					}
				}
			});
		}
		return addPackageButton;
	}
	
	
	private XMLDataModelService getGME() throws MobiusException {
		String serviceId = null;
		// try to find the GME url as configured in the introduce properties
		List discoveryExtensions = ExtensionsLoader.getInstance().getDiscoveryExtensions();
		for (int i = 0; i < discoveryExtensions.size(); i++) {
			DiscoveryExtensionDescriptionType disc = (DiscoveryExtensionDescriptionType) discoveryExtensions.get(i);
			if (disc.getName().equals("gme_discovery")) {
				Properties props = disc.getProperties();
				serviceId = ExtensionTools.getProperty(props, "GME_URL");
			}				
		}
		if (serviceId == null) {
			// get GME url from properties
			serviceId = ExtensionTools.getProperty(getExtensionDescription().getProperties(), "GME_URL");
		}
		GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
		XMLDataModelService gmeHandle = (XMLDataModelService) GridServiceResolver.getInstance()
			.getGridService(serviceId);
		return gmeHandle;
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
					try {
						setProcessorClass(classBrowserPanel.getSelectedClassName());
					} catch (Exception ex) {
						ex.printStackTrace();
						ErrorDialog.showErrorDialog("Error setting the query processor class: " + ex.getMessage(), ex);
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
						ErrorDialog.showErrorDialog("Error storing additional libraries information: " + ex.getMessage(), ex);
					}
				}
			});
		}
		return classBrowserPanel;
	}
	
	
	private void setProcessorClass(String className) throws Exception {
		if (className != null) {
			CommonTools.setServiceProperty(getServiceInfo().getServiceDescriptor(), 
				DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY, className, false);
			// blow away the query processor class properties from the extension data
			Data data = ExtensionDataUtils.getExtensionData(getExtensionTypeExtensionData());
			data.setCQLProcessorConfig(null);
			ExtensionDataUtils.storeExtensionData(getExtensionTypeExtensionData(), data);
			getQpParamsTable().classChanged();
		}
	}
	
	
	private void loadMostRecentProjectInfo() {
		CadsrInformation cadsrInfo = null;
		try {
			cadsrInfo = ExtensionDataUtils.getExtensionData(getExtensionTypeExtensionData()).getCadsrInformation();
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error loading project information: " + ex.getMessage(), ex);
		}
		if (cadsrInfo != null) {
			String longName = cadsrInfo.getProjectLongName();
			String version = cadsrInfo.getProjectVersion();
			Project tempProject = new Project();
			tempProject.setLongName(longName);
			tempProject.setVersion(version);
			mostRecentProject = tempProject;
		}
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
						String filename = ResourceManager.promptFile(null, FileFilters.XML_FILTER);
						getDomainModelNameTextField().setText(filename);
					} catch (Exception ex) {
						ex.printStackTrace();
						ErrorDialog.showErrorDialog("Error selecting file: " + ex.getMessage());
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
			domainModelNameTextField.setEditable(false);
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
			domainModelSelectionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Supplied Domain Model", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			domainModelSelectionPanel.add(getDomainModelNameTextField(), gridBagConstraints6);
			domainModelSelectionPanel.add(getSelectDomainModelButton(), gridBagConstraints7);
		}
		return domainModelSelectionPanel;
	}
	
	
	private void setDomainModelFile() {
		try {
			String filename = getDomainModelNameTextField().getText();
			Data data = ExtensionDataUtils.getExtensionData(getExtensionTypeExtensionData());
			CadsrInformation cadsrInfo = data.getCadsrInformation();
			if (cadsrInfo == null) {
				cadsrInfo = new CadsrInformation();
				data.setCadsrInformation(cadsrInfo);
			}
			// set the domain model file name
			cadsrInfo.setSuppliedDomainModel(filename);
			// get the domain model
			DomainModel model = MetadataUtils.deserializeDomainModel(new FileReader(filename));
			// set the most recent project information
			Project proj = new Project();
			proj.setDescription(model.getProjectDescription());
			proj.setLongName(model.getProjectLongName());
			proj.setShortName(model.getProjectShortName());
			proj.setVersion(model.getProjectVersion());
			mostRecentProject = proj;
			// set cadsr project information
			cadsrInfo.setProjectLongName(model.getProjectLongName());
			cadsrInfo.setProjectVersion(model.getProjectVersion());
			// walk classes, creating package groupings as needed
			Map packageClasses = new HashMap();
			UMLClass[] modelClasses = model.getExposedUMLClassCollection().getUMLClass(); 
			for (int i = 0; i < modelClasses.length; i++) {
				String packageName = modelClasses[i].getPackageName();
				if (packageClasses.containsKey(packageName)) {
					((List) packageClasses.get(packageName)).add(modelClasses[i].getClassName());
				} else {
					List classList = new ArrayList();
					classList.add(modelClasses[i].getClassName());
					packageClasses.put(packageName, classList);
				}
			}
			// create cadsr packages
			CadsrPackage[] packages = new CadsrPackage[packageClasses.keySet().size()];
			int packIndex = 0;
			Iterator packageNameIter = packageClasses.keySet().iterator();
			while (packageNameIter.hasNext()) {
				String packName = (String) packageNameIter.next();
				String mappedNamespace = NamespaceUtils.createNamespaceString(
					model.getProjectShortName(), model.getProjectVersion(), packName);
				CadsrPackage pack = new CadsrPackage();
				pack.setName(packName);
				pack.setMappedNamespace(mappedNamespace);
				// does the mapped namespace exist in the service?
				if (CommonTools.getNamespaceType(getServiceInfo().getNamespaces(), mappedNamespace) != null) {
					String[] message = {
						"The imported domain model has a package which maps to the namespace",
						mappedNamespace + ".",
						"This namespace is not yet loaded into the service.",
						"Please locate a suitable namespace."
					};
					JOptionPane.showMessageDialog(this, message);
					boolean resolved = SchemaResolutionDialog.resolveSchemas(getServiceInfo(), pack);
					if (!resolved) {
						String[] error = {
							"The package " + packName + " was not mapped to a namespace.",
							"This can cause errors when the service builds."
						};
						ErrorDialog.showErrorDialog("No namespace mapping provided", error);
					}
				}
				// create ClassMappings for the package's classes
				List classNameList = (List) packageClasses.get(packName);
				ClassMapping[] mappings = new ClassMapping[classNameList.size()];
				for (int i = 0; i < classNameList.size(); i++) {
					ClassMapping mapping = new ClassMapping();
					String className = (String) classNameList.get(i);
					mapping.setClassName(className);
					mapping.setElementName(className);
					mapping.setSelected(true);
					mapping.setTargetable(true);
					mappings[i] = mapping;
				}
				pack.setCadsrClass(mappings);
				packages[packIndex] = pack;
				packIndex++;
			}
			cadsrInfo.setPackages(packages);
			ExtensionDataUtils.storeExtensionData(getExtensionTypeExtensionData(), data);
			// store the changed information
			ExtensionDataUtils.storeExtensionData(getExtensionTypeExtensionData(), data);
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error loading existing caDSR information: " + ex.getMessage(), ex);
		}
	}
	
	
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddFullProjectButton() {
		if (addFullProjectButton == null) {
			addFullProjectButton = new JButton();
			addFullProjectButton.setText("Add Full Project");
			addFullProjectButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// verify we're in the same project as the other packages
					final Project selectedProject = getCadsrBrowserPanel().getSelectedProject();
					boolean shouldAddPackages = true;
					if (mostRecentProject != null &&
						(!mostRecentProject.getLongName().equals(selectedProject.getLongName()) ||
						!mostRecentProject.getVersion().equals(selectedProject.getVersion()))) {
						// not the same project, can't allow packages from more than one project!
						String[] choices = {"Remove all other packages and insert", "Cancel"};
						String[] message = {
							"Domain models may only be derived from one project.",
							"To add the package you've selected, all other packages",
							"currently in the domain model will have to be removed.",
							"Should this operation procede?"
						};
						String choice = PromptButtonDialog.prompt(
							PortalResourceManager.getInstance().getGridPortal(),
							"Package incompatability...", message, choices, choices[1]);
						if (choice == choices[0]) {
							// try to remove namespaces from the service
							Iterator nsNameIter = packageToNamespace.values().iterator();
							while (nsNameIter.hasNext()) {
								String namespace = (String) nsNameIter.next();
								NamespaceType nsType = CommonTools.getNamespaceType(
									getServiceInfo().getNamespaces(), namespace);
								if (!CommonTools.isNamespaceTypeInUse(nsType, getServiceInfo().getServiceDescriptor())) {
									NamespaceType[] allNamespaces = getServiceInfo().getNamespaces().getNamespace();
									NamespaceType[] cleanedNamespaces = (NamespaceType[]) Utils.removeFromArray(
										allNamespaces, nsType);
									getServiceInfo().getNamespaces().setNamespace(cleanedNamespaces);
								}
							}
							// clear out the existing packages and classes
							packageToNamespace.clear();
							packageToClassMap.clear();
							// clear out the types table
							getClassConfigTable().clearTable();
							// clear out the types tree
							getUmlTree().clearTree();
						} else {
							shouldAddPackages = false;
						}
					}
					if (shouldAddPackages) {
						try {
							CaDSRServiceClient cadsrClient = new CaDSRServiceClient(getCadsrBrowserPanel().getCadsr().getText());
							UMLPackageMetadata[] packages = cadsrClient.findPackagesInProject(selectedProject);
							for (int i = 0; i < packages.length; i++) {
								addPackageToModel(selectedProject, packages[i]);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							ErrorDialog.showErrorDialog("Error adding project: " + ex.getMessage());
						}
						mostRecentProject = selectedProject;
						// store the (potential) change to project info
						storeMostRecentProjectInformation();
						// store the package information
						storeUpdatedPackageInformation();
					}
				}
			});
		}
		return addFullProjectButton;
	}
	
	
	private void addPackageToModel(Project project, UMLPackageMetadata pack) {
		if (!packageToNamespace.containsKey(pack.getName())) {
			// determine if the namespace type already exists in the service
			String namespaceUri = NamespaceUtils.createNamespaceString(project, pack);
			NamespaceType nsType = NamespaceUtils.getServiceNamespaceType(getServiceInfo(), namespaceUri);
			if (nsType == null) {
				// create a new namespace from the package
				try {
					nsType = NamespaceUtils.createNamespaceFromUmlPackage(
						project, pack, getGME(), getSchemaDir());
				} catch (Exception ex) {
					ex.printStackTrace();
					ErrorDialog.showErrorDialog("Error creating namespace type: " + ex.getMessage());
				}
				
				if (nsType != null) {
					// add the new namespace to the service
					CommonTools.addNamespace(getServiceInfo().getServiceDescriptor(), nsType);
				}
			}
			if (nsType != null) {
				// map the package to the new namespace and add it to the types tree
				packageToNamespace.put(pack.getName(), nsType.getNamespace());
				getUmlTree().addUmlPackage(pack.getName());
				// get classes for the package
				String cadsrUrl = getCadsrBrowserPanel().getCadsr().getText();
				try {
					CaDSRServiceClient cadsrClient = new CaDSRServiceClient(cadsrUrl);
					UMLClassMetadata[] classMd = cadsrClient.findClassesInPackage(project, pack.getName());
					for (int i = 0; i < classMd.length; i++) {
						getUmlTree().addUmlClass(pack.getName(), classMd[i].getName());
					}
					// map the classes to schema types
					Map classToType = NamespaceUtils.mapClassesToElementNames(classMd, nsType);
					// store the mapping
					packageToClassMap.put(pack.getName(), classToType);
				} catch (Exception ex) {
					ex.printStackTrace();
					ErrorDialog.showErrorDialog("Error getting classes from caDSR", ex);
				}				
			}
		}
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRemovePackageButton() {
		if (removePackageButton == null) {
			removePackageButton = new JButton();
			removePackageButton.setText("Remove Package");
			removePackageButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Project selectedProject = getCadsrBrowserPanel().getSelectedProject();
					if (selectedProject != null && projectEquals(selectedProject, mostRecentProject)) {
						UMLPackageMetadata selectedPackage = getCadsrBrowserPanel().getSelectedPackage();
						if (selectedPackage != null && packageToNamespace.containsKey(selectedPackage.getName())) {
							// remove the package from the uml types tree
							getUmlTree().removeUmlPackage(selectedPackage.getName());
							String namespace = (String) packageToNamespace.get(selectedPackage.getName());
							NamespaceType nsType = CommonTools.getNamespaceType(
								getServiceInfo().getNamespaces(), namespace);
							// if the namespace type is no longer in use, remove it from the service
							if (!CommonTools.isNamespaceTypeInUse(nsType, getServiceInfo().getServiceDescriptor())) {
								NamespaceType[] allNamespaces = getServiceInfo().getNamespaces().getNamespace();
								NamespaceType[] cleanedNamespaces = (NamespaceType[]) Utils.removeFromArray(
									allNamespaces, nsType);
								getServiceInfo().getNamespaces().setNamespace(cleanedNamespaces);
							}
							// remove namespace from the packageMapping
							packageToNamespace.remove(selectedPackage.getName());
							// remove the mapping for its classes
							packageToClassMap.remove(selectedPackage);
							// store the new information in the extension data
							storeUpdatedPackageInformation();
						}
					} else {
						PortalUtils.showMessage("Please select a package involved in the current domain model.");
					}
				}
			});
		}
		return removePackageButton;
	}
	
	
	private JCheckBox getCqlSyntaxValidationCheckBox() {
		if (cqlSyntaxValidationCheckBox == null) {
			cqlSyntaxValidationCheckBox = new JCheckBox();
			cqlSyntaxValidationCheckBox.setText("Validate CQL Syntax");
			cqlSyntaxValidationCheckBox.setToolTipText("Causes the Data Service to validate all CQL queries for syntactic correctness");
			cqlSyntaxValidationCheckBox.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					CommonTools.setServiceProperty(getServiceInfo().getServiceDescriptor(),
						DataServiceConstants.VALIDATE_CQL_FLAG,	String.valueOf(
							getCqlSyntaxValidationCheckBox().isSelected()), false);
				}
			});
			// set the check box selection
			if (CommonTools.servicePropertyExists(getServiceInfo().getServiceDescriptor(), DataServiceConstants.VALIDATE_CQL_FLAG)) {
				try {
					cqlSyntaxValidationCheckBox.setSelected(Boolean.valueOf(
						CommonTools.getServicePropertyValue(
							getServiceInfo().getServiceDescriptor(), DataServiceConstants.VALIDATE_CQL_FLAG)).booleanValue());
				} catch (Exception ex) {
					System.err.println("Error getting service property value for " + DataServiceConstants.VALIDATE_CQL_FLAG);
					ex.printStackTrace();
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
			domainModelValidationCheckBox.setToolTipText("Causes the data service to ensure all queries remain within the limits of the exposed domain model");
			domainModelValidationCheckBox.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					CommonTools.setServiceProperty(getServiceInfo().getServiceDescriptor(), DataServiceConstants.VALIDATE_DOMAIN_MODEL_FLAG, 
						String.valueOf(getDomainModelValidationCheckBox().isSelected()), false);
				}
			});
			// set the check box selection
			if (CommonTools.servicePropertyExists(getServiceInfo().getServiceDescriptor(), DataServiceConstants.VALIDATE_DOMAIN_MODEL_FLAG)) {
				try {
					domainModelValidationCheckBox.setSelected(Boolean.valueOf(
						CommonTools.getServicePropertyValue(
							getServiceInfo().getServiceDescriptor(), DataServiceConstants.VALIDATE_DOMAIN_MODEL_FLAG)).booleanValue());
				} catch (Exception ex) {
					System.err.println("Error getting service property value for " + DataServiceConstants.VALIDATE_DOMAIN_MODEL_FLAG);
					ex.printStackTrace();
				}
			}
		}
		return domainModelValidationCheckBox;
	}
	
	
	/**
	 * p1 must be non-null!!
	 * @param p1
	 * @param p2
	 * @return
	 */
	private boolean projectEquals(Project p1, Project p2) {
		if (p2 != null) {
			return p1.getLongName().equals(p2.getLongName()) 
				&& p1.getVersion().equals(p2.getVersion());
		}
		return false;
	}
	
	
	private void loadUmlTreeInformation() {
		Runnable loader = new Runnable() {
			public void run() {
				// if there's existing cadsr configuration, apply it
				CadsrInformation cadsrInfo = null;
				try {
					cadsrInfo = ExtensionDataUtils.getExtensionData(getExtensionTypeExtensionData()).getCadsrInformation();
				} catch (Exception ex) {
					ErrorDialog.showErrorDialog("Error getting cadsrInformation from extension data: " + ex.getMessage(), ex);
				}
				if (cadsrInfo != null) {
					getUmlTree().setEnabled(false);
					// set the caDSR service URL in the GUI
					getCadsrBrowserPanel().setDefaultCaDSRURL(cadsrInfo.getServiceUrl());
					getCadsrBrowserPanel().getCadsr().setText(cadsrInfo.getServiceUrl());
					// walk through packages
					for (int i = 0; cadsrInfo.getPackages() != null && i < cadsrInfo.getPackages().length; i++) {
						CadsrPackage pack = cadsrInfo.getPackages(i);
						String packageName = pack.getName();
						String namespace = pack.getMappedNamespace();
						// keep track of the mapped package / namespace combination
						packageToNamespace.put(packageName, namespace);
						// find the namespace needed for this package in the service description
						NamespaceType[] serviceNamespaces = getServiceInfo().getNamespaces().getNamespace();
						NamespaceType nsType = null;
						for (int nsIndex = 0; nsIndex < serviceNamespaces.length; nsIndex++) {
							NamespaceType ns = serviceNamespaces[nsIndex];
							if (ns.getNamespace().equals(namespace)) {
								nsType = ns;
								break;
							}
						}
						if (nsType != null) {
							// add the package to the types tree
							getUmlTree().addUmlPackage(packageName);
							// prepare a mapping of class to element names
							Map classToElementNames = new HashMap();
							packageToClassMap.put(packageName, classToElementNames);
							for (int j = 0; pack.getCadsrClass() != null && j < pack.getCadsrClass().length; j++) {
								ClassMapping map = pack.getCadsrClass(j);
								classToElementNames.put(map.getClassName(), map.getElementName());
								// add the classes for the uml package to the tree
								UMLClassTreeNode node = getUmlTree().addUmlClass(packageName, map.getClassName());
								node.getCheckBox().setSelected(map.isSelected());								// TODO: I may have to add the type to the types table here
							}
						}
					}
				}
				getUmlTree().setEnabled(true);
			}
		};
		SwingUtilities.invokeLater(loader);
	}


	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getClassConfigScrollPane() {
		if (classConfigScrollPane == null) {
			classConfigScrollPane = new JScrollPane();
			classConfigScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Exposed Class Configuration", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
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
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.gridy = 0;
			validationCheckPanel = new JPanel();
			validationCheckPanel.setLayout(new GridBagLayout());
			validationCheckPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Query Validation", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			validationCheckPanel.add(getCqlSyntaxValidationCheckBox(), gridBagConstraints);
			validationCheckPanel.add(getDomainModelValidationCheckBox(), gridBagConstraints1);
		}
		return validationCheckPanel;
	}


	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getUmlClassScrollPane() {
		if (umlClassScrollPane == null) {
			umlClassScrollPane = new JScrollPane();
			umlClassScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "UML Class Selection", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			umlClassScrollPane.setViewportView(getUmlTree());
		}
		return umlClassScrollPane;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPackageSelectionButtonPanel() {
		if (packageSelectionButtonPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 2;
			gridBagConstraints4.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints4.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.gridy = 0;
			packageSelectionButtonPanel = new JPanel();
			packageSelectionButtonPanel.setLayout(new GridBagLayout());
			packageSelectionButtonPanel.add(getAddFullProjectButton(), gridBagConstraints2);
			packageSelectionButtonPanel.add(getAddPackageButton(), gridBagConstraints3);
			packageSelectionButtonPanel.add(getRemovePackageButton(), gridBagConstraints4);
		}
		return packageSelectionButtonPanel;
	}


	/**
	 * This method initializes jRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getNoDomainModelRadioButton() {
		if (noDomainModelRadioButton == null) {
			noDomainModelRadioButton = new JRadioButton();
			noDomainModelRadioButton.setText("No Domain Model");
			noDomainModelRadioButton.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (noDomainModelRadioButton.isSelected()) {
						removeStoredCadsrInformation();
						PortalUtils.setContainerEnabled(getDomainModelSelectionPanel(), false);
						PortalUtils.setContainerEnabled(getCadsrDomainModelPanel(), false);
						getDomainModelNameTextField().setText("");
					}
				}
			});
		}
		return noDomainModelRadioButton;
	}


	/**
	 * This method initializes jRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getCadsrDomainModelRadioButton() {
		if (cadsrDomainModelRadioButton == null) {
			cadsrDomainModelRadioButton = new JRadioButton();
			cadsrDomainModelRadioButton.setText("caDSR Domain Model");
			cadsrDomainModelRadioButton.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (cadsrDomainModelRadioButton.isSelected()) {
						storeCadsrServiceUrl();
						PortalUtils.setContainerEnabled(getDomainModelSelectionPanel(), false);
						PortalUtils.setContainerEnabled(getCadsrDomainModelPanel(), true);
						getDomainModelNameTextField().setText("");
					}
				}
			});
		}
		return cadsrDomainModelRadioButton;
	}


	/**
	 * This method initializes jRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getSuppliedDomainModelRadioButton() {
		if (suppliedDomainModelRadioButton == null) {
			suppliedDomainModelRadioButton = new JRadioButton();
			suppliedDomainModelRadioButton.setText("Supplied Domain Model");
			suppliedDomainModelRadioButton.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (suppliedDomainModelRadioButton.isSelected()) {
						removeStoredCadsrInformation();
						PortalUtils.setContainerEnabled(getDomainModelSelectionPanel(), true);
						PortalUtils.setContainerEnabled(getCadsrDomainModelPanel(), false);
					}
				}
			});
		}
		return suppliedDomainModelRadioButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDomainModelSourcePanel() {
		if (domainModelSourcePanel == null) {
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints9.gridy = 2;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.gridy = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 0;
			domainModelSourcePanel = new JPanel();
			domainModelSourcePanel.setLayout(new GridBagLayout());
			domainModelSourcePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Domain Model Source", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			domainModelSourcePanel.add(getNoDomainModelRadioButton(), gridBagConstraints5);
			domainModelSourcePanel.add(getCadsrDomainModelRadioButton(), gridBagConstraints8);
			domainModelSourcePanel.add(getSuppliedDomainModelRadioButton(), gridBagConstraints9);
			ButtonGroup group = new ButtonGroup();
			group.add(getNoDomainModelRadioButton());
			group.add(getCadsrDomainModelRadioButton());
			group.add(getSuppliedDomainModelRadioButton());
			// decide which domain model mode to auto-select
			try {
				Data data = ExtensionDataUtils.getExtensionData(getExtensionTypeExtensionData());
				CadsrInformation info = data.getCadsrInformation();
				if (info != null) {
					if (info.getSuppliedDomainModel() != null) {
						group.setSelected(getSuppliedDomainModelRadioButton().getModel(), true);
					} else {
						group.setSelected(getCadsrDomainModelRadioButton().getModel(), true);
					}
				} else {
					group.setSelected(getNoDomainModelRadioButton().getModel(), true);
				}
			} catch (Exception ex) {
				
			}
			
			
		}
		return domainModelSourcePanel;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDomainConfigPanel() {
		if (domainConfigPanel == null) {
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints15.gridwidth = 2;
			gridBagConstraints15.weightx = 1.0D;
			gridBagConstraints15.weighty = 1.0D;
			gridBagConstraints15.gridy = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints11.weightx = 1.0D;
			gridBagConstraints11.gridy = 0;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.gridy = 0;
			domainConfigPanel = new JPanel();
			domainConfigPanel.setLayout(new GridBagLayout());
			domainConfigPanel.add(getDomainModelSourcePanel(), gridBagConstraints10);
			domainConfigPanel.add(getDomainModelSelectionPanel(), gridBagConstraints11);
			domainConfigPanel.add(getCadsrDomainModelPanel(), gridBagConstraints15);
		}
		return domainConfigPanel;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCadsrDomainModelPanel() {
		if (cadsrDomainModelPanel == null) {
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints14.gridy = 2;
			gridBagConstraints14.weightx = 1.0;
			gridBagConstraints14.weighty = 1.0;
			gridBagConstraints14.gridx = 0;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridy = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.gridy = 0;
			cadsrDomainModelPanel = new JPanel();
			cadsrDomainModelPanel.setLayout(new GridBagLayout());
			cadsrDomainModelPanel.add(getCadsrBrowserPanel(), gridBagConstraints12);
			cadsrDomainModelPanel.add(getPackageSelectionButtonPanel(), gridBagConstraints13);
			cadsrDomainModelPanel.add(getUmlClassScrollPane(), gridBagConstraints14);
		}
		return cadsrDomainModelPanel;
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
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.weightx = 1.0D;
			gridBagConstraints16.gridy = 0;
			gridBagConstraints16.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 0;
			gridBagConstraints17.gridy = 1;
			gridBagConstraints17.weightx = 1.0D;
			gridBagConstraints17.weighty = 1.0D;
			gridBagConstraints17.fill = GridBagConstraints.BOTH;
			gridBagConstraints17.insets = new java.awt.Insets(6,6,6,6);
			processorConfigPanel = new JPanel();
			processorConfigPanel.setLayout(new GridBagLayout());
			processorConfigPanel.add(getClassBrowserPanel(), gridBagConstraints16);
			processorConfigPanel.add(getQpParamsScrollPane(), gridBagConstraints17);
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
			qpParamsTable = new QueryProcessorParametersTable(
				getExtensionTypeExtensionData(), getServiceInfo());
		}
		return qpParamsTable;
	}
	
	
	private JScrollPane getQpParamsScrollPane() {
		if (qpParamsScrollPane == null) {
			qpParamsScrollPane = new JScrollPane();
			qpParamsScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Processor Parameter Configuration", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			qpParamsScrollPane.setViewportView(getQpParamsTable());
		}
		return qpParamsScrollPane;
	}
	
	
	private ClassMapping getClassMapping(String packName, String className) throws Exception {
		// see if there's a class mapping in the extension data
		Data data = ExtensionDataUtils.getExtensionData(getExtensionTypeExtensionData());
		CadsrInformation info = data.getCadsrInformation();
		if (info != null) {
			CadsrPackage[] packages = info.getPackages();
			// find the package
			for (int i = 0; packages != null && i < packages.length; i++) {
				if (packages[i].getName().equals(packName)) {
					ClassMapping[] mappings = packages[i].getCadsrClass();
					for (int j = 0; mappings != null && j < mappings.length; j++) {
						if (mappings[j].getClassName().equals(className)) {
							return mappings[j];
						}
					}
				}
			}
		}
		// no mapping found in cadsr info, create a new one
		String elemName = (String) ((Map) packageToClassMap.get(packName)).get(className);
		ClassMapping mapping = new ClassMapping();
		mapping.setClassName(className);
		mapping.setElementName(elemName);
		mapping.setSelected(true);
		mapping.setTargetable(true);
		return mapping;
	}
	
	
	private void storeCadsrServiceUrl() {
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionTypeExtensionData());
			CadsrInformation info = data.getCadsrInformation();
			if (info == null) {
				info = new CadsrInformation();
				data.setCadsrInformation(info);
			}
			info.setServiceUrl(getCadsrBrowserPanel().getCadsr().getText());
			ExtensionDataUtils.storeExtensionData(getExtensionTypeExtensionData(), data);
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error storing cadsr service URL", ex);
		}
	}
	
	
	private void storeMostRecentProjectInformation() {
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionTypeExtensionData());
			CadsrInformation info = data.getCadsrInformation();
			if (info == null) {
				info = new CadsrInformation();
				data.setCadsrInformation(info);
			}
			info.setProjectLongName(mostRecentProject.getLongName());
			info.setProjectVersion(mostRecentProject.getVersion());
			ExtensionDataUtils.storeExtensionData(getExtensionTypeExtensionData(), data);
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error storing project information", ex);
		}
	}
	
	
	private void storeUpdatedPackageInformation() {
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionTypeExtensionData());
			CadsrInformation info = data.getCadsrInformation();
			if (info == null) {
				info = new CadsrInformation();
				data.setCadsrInformation(info);
			}
			Map packagesByName = new HashMap();
			// determine what currently stored packages can be kept
			for (int i = 0; info.getPackages() != null && i < info.getPackages().length; i++) {
				String packName = info.getPackages(i).getName();
				// if the package is still in the mapping, keep it
				if (packageToNamespace.containsKey(packName)) {
					// update the namespace mapping
					info.getPackages(i).setMappedNamespace((String) packageToNamespace.get(packName));
					packagesByName.put(packName, info.getPackages(i));
				}
			}
			// iterate the current package mapping, looking for things
			// not already present in the stored package mappings
			Iterator currentPackNames = packageToNamespace.keySet().iterator();
			while (currentPackNames.hasNext()) {
				String name = (String) currentPackNames.next();
				if (!packagesByName.containsKey(name)) {
					// create a new package
					CadsrPackage newPackage = new CadsrPackage();
					newPackage.setName(name);
					newPackage.setMappedNamespace((String) packageToNamespace.get(name));
					// create class mappings for the new package
					Map packageClasses = (Map) packageToClassMap.get(name);
					ClassMapping[] mappings = new ClassMapping[packageClasses.size()];
					int mappingIndex = 0;
					Iterator classNameIter = packageClasses.keySet().iterator();
					while (classNameIter.hasNext()) {
						mappings[mappingIndex] = getClassMapping(name, (String) classNameIter.next());
						mappingIndex++;
					}
					packagesByName.put(name, newPackage);
				}
			}
			// put the updated packages into the cadsr information
			CadsrPackage[] updated = new CadsrPackage[packagesByName.size()];
			packagesByName.values().toArray(updated);
			info.setPackages(updated);
			// store the data
			ExtensionDataUtils.storeExtensionData(getExtensionTypeExtensionData(), data);
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error storing package information", ex);
		}
	}
	
	
	private void storeClassMapping(String packName, ClassMapping mapping) {
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionTypeExtensionData());
			CadsrInformation info = data.getCadsrInformation();
			if (info == null) {
				// this is bad anyway, because we won't have any packages....
				info = new CadsrInformation();
				data.setCadsrInformation(info);
			}
			CadsrPackage pack = null;
			for (int i = 0; info.getPackages() != null && i < info.getPackages().length; i++) {
				if (info.getPackages(i).getName().equals(packName)) {
					pack = info.getPackages(i);
					break;
				}
			}
			if (pack == null) {
				// no package found
				throw new IllegalArgumentException("Package " + packName + " is not stored in the extension data");
			}
			// see if the class mapping is already in the package
			boolean found = false;
			for (int i = 0; pack.getCadsrClass() != null && i < pack.getCadsrClass().length; i++) {
				if (pack.getCadsrClass(i).getClassName().equals(mapping.getClassName())) {
					pack.setCadsrClass(i, mapping);
					found = true;
					break;
				}
			}
			if (!found) {
				ClassMapping[] current = pack.getCadsrClass();
				if (current == null) {
					current = new ClassMapping[] {mapping};
				} else {
					current = (ClassMapping[]) Utils.appendToArray(current, mapping);
				}
				pack.setCadsrClass(current);
			}
			ExtensionDataUtils.storeExtensionData(getExtensionTypeExtensionData(), data);
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error storing class mapping information", ex);
		}
	}
	
	
	private void unselectClassMapping(String packName, String className) {
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionTypeExtensionData());
			CadsrInformation info = data.getCadsrInformation();
			if (info == null) {
				info = new CadsrInformation();
				data.setCadsrInformation(info);
			}
			// find the package
			CadsrPackage pack = null;
			for (int i = 0; info.getPackages() != null && i < info.getPackages().length; i++) {
				if (info.getPackages(i).getName().equals(packName)) {
					pack = info.getPackages(i);
					break;
				}
			}
			if (pack != null) {
				// find the class mapping
				for (int i = 0; pack.getCadsrClass() != null && i < pack.getCadsrClass().length; i++) {
					if (pack.getCadsrClass(i).getClassName().equals(className)) {
						pack.getCadsrClass(i).setSelected(false);
						break;
					}
				}
			}
			ExtensionDataUtils.storeExtensionData(getExtensionTypeExtensionData(), data);
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error removing class mapping", ex);
		}
	}
	
	
	private void removeStoredCadsrInformation() {
		// take out cadsr information from extension data
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionTypeExtensionData());
			data.setCadsrInformation(null);
			ExtensionDataUtils.storeExtensionData(getExtensionTypeExtensionData(), data);
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error removing cadsr information from extension", ex);
		}
	}
}