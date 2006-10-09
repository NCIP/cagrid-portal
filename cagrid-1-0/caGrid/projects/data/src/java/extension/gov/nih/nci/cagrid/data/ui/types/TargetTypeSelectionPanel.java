package gov.nih.nci.cagrid.data.ui.types;

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
import gov.nih.nci.cagrid.data.ui.browser.QueryProcessorClassConfigDialog;
import gov.nih.nci.cagrid.data.ui.tree.CheckTreeSelectionEvent;
import gov.nih.nci.cagrid.data.ui.tree.CheckTreeSelectionListener;
import gov.nih.nci.cagrid.data.ui.types.umltree.UMLClassTreeNode;
import gov.nih.nci.cagrid.data.ui.types.umltree.UMLProjectTree;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.Properties;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.ServiceModificationUIPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
 *  TargetTypeSelectionPanel
 *  Panel for selecting target data types from a domain model
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 20, 2006 
 * @version $Id$ 
 */
public class TargetTypeSelectionPanel extends ServiceModificationUIPanel {
	
	private CaDSRBrowserPanel domainBrowserPanel = null;
	private UMLProjectTree umlTree = null;
	private JScrollPane umlTreeScrollPane = null;
	private DataServiceTypesTable typesTable = null;
	private JScrollPane typesTableScrollPane = null;
	private JPanel typeSelectionPanel = null;
	private JButton addPackageButton = null;
	private ClassBrowserPanel classBrowserPanel = null;
	private JPanel configurationPanel = null;
	private JButton configureButton = null;
	private JButton selectDomainModelButton = null;
	private JTextField domainModelNameTextField = null;
	private JPanel domainModelSelectionPanel = null;
	private JButton addFullProjectButton = null;
	private JPanel addToModelButtonsPanel = null;
	private JButton removePackageButton = null;
	private JPanel validationConfigPanel = null;
	private JCheckBox cqlSyntaxValidationCheckBox = null;
	private JCheckBox domainModelValidationCheckBox = null;
	
	private transient Project mostRecentProject = null;
	private transient Map packageToNamespace = null;
	private transient Map packageToClassMap = null;
	
	public TargetTypeSelectionPanel(ServiceExtensionDescriptionType desc, ServiceInformation serviceInfo) {
		super(desc, serviceInfo);
		packageToNamespace = new HashMap();
		packageToClassMap = new HashMap();
		loadMostRecentProjectInfo();
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
	
	
	private CaDSRBrowserPanel getDomainBrowserPanel() {
		if (domainBrowserPanel == null) {
			domainBrowserPanel = new CaDSRBrowserPanel(true, false);
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
				// get the default caDSR url out of the extension config
				url = ExtensionTools.getProperty(getExtensionDescription().getProperties(), "CADSR_URL");
			}
			if (url != null) {
				// configure selected items in the cadsr panel
				domainBrowserPanel.setDefaultCaDSRURL(url);
				domainBrowserPanel.getCadsr().setText(url);
				domainBrowserPanel.discoverFromCaDSR();
			}
		}
		return domainBrowserPanel;
	}
	
	
	private UMLProjectTree getUmlTree() {
		if (umlTree == null) {
			umlTree = new UMLProjectTree();
			umlTree.addCheckTreeSelectionListener(new CheckTreeSelectionListener() {
				public void nodeChecked(CheckTreeSelectionEvent e) {
					if (e.getNode() instanceof UMLClassTreeNode) {
						
					}
				}
				
				
				public void nodeUnchecked(CheckTreeSelectionEvent e) {
					if (e.getNode() instanceof UMLClassTreeNode) {
						
					}
				}
			});
			// if there's existing cadsr configuration, apply it
			loadCadsrInformation();
			/*
			 // listener for check and uncheck operations on the tree
			  typesTree.addCheckTreeSelectionListener(new CheckTreeSelectionListener() {
			  public void nodeChecked(CheckTreeSelectionEvent e) {
			  if (e.getNode() instanceof TypeTreeNode) {
			  TypeTreeNode typeNode = (TypeTreeNode) e.getNode();
			  DomainTreeNode nsNode = (DomainTreeNode) typeNode.getParent();
			  getTypesTable().addType(nsNode.getNamespace(), typeNode.getType());
			  updateSelectedClasses();
			  }
			  }
			  
			  
			  public void nodeUnchecked(CheckTreeSelectionEvent e) {
			  if (e.getNode() instanceof TypeTreeNode) {
			  TypeTreeNode typeNode = (TypeTreeNode) e.getNode();
			  getTypesTable().removeSchemaElementType(typeNode.getType());
			  updateSelectedClasses();
			  }
			  }
			  
			  
			  private void updateSelectedClasses() {
			  try {
			  storeCaDSRInfo();
			  } catch (Exception ex) {
			  ex.printStackTrace();
			  ErrorDialog.showErrorDialog("Error storing selected classes: " + ex.getMessage());
			  }
			  }
			  });
			  */
		}
		return umlTree;
	}
	
	
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getUmlTreeScrollPane() {
		if (umlTreeScrollPane == null) {
			umlTreeScrollPane = new JScrollPane();
			umlTreeScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Model Data Types", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			umlTreeScrollPane.setViewportView(getUmlTree());
		}
		return umlTreeScrollPane;
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
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1; 
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
			typeSelectionPanel.add(getUmlTreeScrollPane(), gridBagConstraints1);
			typeSelectionPanel.add(getAddToModelButtonsPanel(), gridBagConstraints2);
			typeSelectionPanel.add(getDomainModelSelectionPanel(), gridBagCosntraints32);
		}
		return typeSelectionPanel;
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
					Project selectedProject = getDomainBrowserPanel().getSelectedProject();
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
							while (getTypesTable().getRowCount() != 0) {
								getTypesTable().removeSchemaElementType(0);
							}
							// clear out the types tree
							getUmlTree().clearTree();
						} else {
							shouldAddPackage = false;
						}
					}
					if (shouldAddPackage) {
						UMLPackageMetadata pack = getDomainBrowserPanel().getSelectedPackage();
						if (pack != null) {
							addPackageToModel(selectedProject, pack);
							// change the most recently added project
							mostRecentProject = selectedProject;
							storeCaDSRInfo();
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
	
	
	private void storeCaDSRInfo() {
		CadsrInformation cadsrInfo = new CadsrInformation();
		// cadsr url
		cadsrInfo.setServiceUrl(getDomainBrowserPanel().getCadsr().getText());
		// project name and version
		cadsrInfo.setProjectLongName(mostRecentProject.getLongName());
		cadsrInfo.setProjectVersion(mostRecentProject.getVersion());
		
		List packages = new ArrayList();
		// selected packages and classes
		Iterator packageIter = packageToNamespace.keySet().iterator();
		while (packageIter.hasNext()) {
			String packName = (String) packageIter.next();
			String nsTypeName = (String) packageToNamespace.get(packName);
			CadsrPackage pack = new CadsrPackage();
			pack.setName(packName);
			pack.setMappedNamespace(nsTypeName);
			
			Map classNameToElementName = (Map) packageToClassMap.get(packName);
			Iterator classNameIter = classNameToElementName.keySet().iterator();
			List classMappings = new ArrayList();
			while (classNameIter.hasNext()) {
				ClassMapping mapping = new ClassMapping();
				String className = (String) classNameIter.next();
				mapping.setClassName(className);
				
				// find the element name mapping for the node
				String elementName = (String) classNameToElementName.get(className);
				mapping.setElementName(elementName);
				
				// find the node in the tree
				UMLClassTreeNode node = getUmlTree().getUmlClassNode(packName, className);
				boolean selected = node.isChecked();
				mapping.setSelected(selected);
				classMappings.add(mapping);
			}
			
			ClassMapping[] classMaps = new ClassMapping[classMappings.size()];
			classMappings.toArray(classMaps);
			pack.setCadsrClass(classMaps);
			
			packages.add(pack);
		}
		CadsrPackage[] selectedPackages = new CadsrPackage[packages.size()];
		packages.toArray(selectedPackages);
		cadsrInfo.setPackages(selectedPackages);
		
		// store the cadsr info in the extension data
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionTypeExtensionData());
			data.setCadsrInformation(cadsrInfo);
			ExtensionDataUtils.storeExtensionData(getExtensionTypeExtensionData(), data);
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error storing caDSR information: " + ex.getMessage());
		}
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getConfigurationPanel() {
		if (configurationPanel == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints13.weightx = 1.0D;
			gridBagConstraints13.gridy = 3;
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
			configurationPanel.add(getValidationConfigPanel(), gridBagConstraints13);
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
			domainModelSelectionPanel.setSize(new java.awt.Dimension(295,69));
			domainModelSelectionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Optional Supplied Domain Model", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			domainModelSelectionPanel.add(getDomainModelNameTextField(), gridBagConstraints6);
			domainModelSelectionPanel.add(getSelectDomainModelButton(), gridBagConstraints7);
		}
		return domainModelSelectionPanel;
	}
	
	
	private void setDomainModelFile() {
    	String filename = getDomainModelNameTextField().getText();
    	Data data = null;
    	try {
    		data = ExtensionDataUtils.getExtensionData(getExtensionTypeExtensionData());
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		ErrorDialog.showErrorDialog("Error loading existing caDSR information: " + ex.getMessage(), ex);
    	}
    	CadsrInformation cadsrInfo = data.getCadsrInformation();
    	if (cadsrInfo == null) {
    		cadsrInfo = new CadsrInformation();
    		data.setCadsrInformation(cadsrInfo);
    	}
    	if (filename == null || filename.length() == 0) {
    		cadsrInfo.setSuppliedDomainModel(null);
    	} else {
    		cadsrInfo.setSuppliedDomainModel(filename);
    	}
    	// store the changed information
    	try {
    		ExtensionDataUtils.storeExtensionData(getExtensionTypeExtensionData(), data);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		ErrorDialog.showErrorDialog("Error storing domain model filename: " + ex.getMessage());
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
					final Project selectedProject = getDomainBrowserPanel().getSelectedProject();
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
							// clear out the types table
							while (getTypesTable().getRowCount() != 0) {
								getTypesTable().removeSchemaElementType(0);
							}
							// clear out the types tree
							getUmlTree().clearTree();
						} else {
							shouldAddPackages = false;
						}
					}
					if (shouldAddPackages) {
						try {
							CaDSRServiceClient cadsrClient = new CaDSRServiceClient(getDomainBrowserPanel().getCadsr().getText());
							UMLPackageMetadata[] packages = cadsrClient.findPackagesInProject(selectedProject);
							for (int i = 0; i < packages.length; i++) {
								addPackageToModel(selectedProject, packages[i]);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							ErrorDialog.showErrorDialog("Error adding project: " + ex.getMessage());
						}
						mostRecentProject = selectedProject;
						storeCaDSRInfo();
					}
				}
			});
		}
		return addFullProjectButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAddToModelButtonsPanel() {
		if (addToModelButtonsPanel == null) {
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 2;
			gridBagConstraints10.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints10.gridy = 0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.gridx = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints8.gridy = 0;
			addToModelButtonsPanel = new JPanel();
			addToModelButtonsPanel.setLayout(new GridBagLayout());
			addToModelButtonsPanel.add(getAddFullProjectButton(), gridBagConstraints8);
			addToModelButtonsPanel.add(getAddPackageButton(), gridBagConstraints9);
			addToModelButtonsPanel.add(getRemovePackageButton(), gridBagConstraints10);
		}
		return addToModelButtonsPanel;
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
				String cadsrUrl = getDomainBrowserPanel().getCadsr().getText();
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
					Project selectedProject = getDomainBrowserPanel().getSelectedProject();
					if (selectedProject != null && projectEquals(selectedProject, mostRecentProject)) {
						UMLPackageMetadata selectedPackage = getDomainBrowserPanel().getSelectedPackage();
						if (selectedPackage != null && packageToNamespace.containsKey(selectedPackage.getName())) {
							// remove the package from the uml types tree
							getUmlTree().removeUmlPackage(selectedPackage.getName());
							String namespace = (String) packageToNamespace.get(selectedPackage.getName());
							NamespaceType nsType = CommonTools.getNamespaceType(
								getServiceInfo().getNamespaces(), namespace);
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
							// see about removing the namespace from the service
							// store the new information in the extension data
							storeCaDSRInfo();
						}
					} else {
						PortalUtils.showMessage("Please select a package involved in the current domain model.");
					}
				}
			});
		}
		return removePackageButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getValidationConfigPanel() {
		if (validationConfigPanel == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints12.gridy = 0;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints11.gridy = 0;
			validationConfigPanel = new JPanel();
			validationConfigPanel.setLayout(new GridBagLayout());
			validationConfigPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Query Validation", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			validationConfigPanel.add(getCqlSyntaxValidationCheckBox(), gridBagConstraints11);
			validationConfigPanel.add(getDomainModelValidationCheckBox(), gridBagConstraints12);
		}
		return validationConfigPanel;
	}


	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getCqlSyntaxValidationCheckBox() {
		if (cqlSyntaxValidationCheckBox == null) {
			cqlSyntaxValidationCheckBox = new JCheckBox();
			cqlSyntaxValidationCheckBox.setText("CQL Syntax");
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
			domainModelValidationCheckBox.setText("Domain Model");
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
	
	
	private void loadCadsrInformation() {
		Thread loader = new Thread() {
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
					getDomainBrowserPanel().setDefaultCaDSRURL(cadsrInfo.getServiceUrl());
					getDomainBrowserPanel().getCadsr().setText(cadsrInfo.getServiceUrl());
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
								node.getCheckBox().setSelected(map.isSelected());
								// TODO: I may have to add the type to the types table here
							}
						}
					}
				}
				getUmlTree().setEnabled(true);
			}
		};
		loader.start();
	}
	
	
	/**
	 * Temporary stub to get the build going again.
	 * 
	 * This method should reload all GUI components that rely on the service model
	 */
	public void resetGUI() {
		// TODO: implement me
	}
}
