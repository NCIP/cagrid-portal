package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.cadsr.portal.CaDSRBrowserPanel;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.common.portal.PromptButtonDialog;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.AdditionalLibraries;
import gov.nih.nci.cagrid.data.extension.CadsrInformation;
import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.ui.browser.AdditionalJarsChangeListener;
import gov.nih.nci.cagrid.data.ui.browser.AdditionalJarsChangedEvent;
import gov.nih.nci.cagrid.data.ui.browser.ClassBrowserPanel;
import gov.nih.nci.cagrid.data.ui.browser.ClassSelectionEvent;
import gov.nih.nci.cagrid.data.ui.browser.ClassSelectionListener;
import gov.nih.nci.cagrid.data.ui.browser.QueryProcessorClassConfigDialog;
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	private TargetTypesTree typesTree = null;
	private JScrollPane typesTreeScrollPane = null;
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
	
	public TargetTypeSelectionPanel(ServiceExtensionDescriptionType desc, ServiceInformation serviceInfo) {
		super(desc, serviceInfo);
		packageToNamespace = new HashMap();
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
	
	
	private TargetTypesTree getTypesTree() {
		if (typesTree == null) {
			typesTree = new TargetTypesTree();
			// listener for check and uncheck operations on the tree
			typesTree.addTypeSelectionListener(new TypeSelectionListener() {
				public void typeSelectionAdded(TypeSelectionEvent e) {
					getTypesTable().addType(e.getNamespaceType(), e.getSchemaElementType());
					updateSelectedClasses();
				}
				
				
				public void typeSelectionRemoved(TypeSelectionEvent e) {
					getTypesTable().removeSchemaElementType(e.getSchemaElementType());
					updateSelectedClasses();
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
			
			// if there's existing cadsr configuration, apply it
			CadsrInformation cadsrInfo = null;
			try {
				cadsrInfo = ExtensionDataUtils.getExtensionData(getExtensionTypeExtensionData()).getCadsrInformation();
			} catch (Exception ex) {
				ErrorDialog.showErrorDialog("Error getting cadsrInformation from extension data: " + ex.getMessage(), ex);
			}
			if (cadsrInfo != null) {
				// walk through packages
				for (int i = 0; cadsrInfo.getPackages() != null && i < cadsrInfo.getPackages().length; i++) {
					CadsrPackage pack = cadsrInfo.getPackages(i);
					String packageName = pack.getName();
					String namespace = pack.getMappedNamespace();
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
						// add the namespace to the types tree
						typesTree.addNamespaceType(nsType);
						// convert the set of type names selected in the cadsr info to an
						// array of schema element types
						Set typeNames = new HashSet();
						if (pack.getSelectedClass() != null) {
							Collections.addAll(typeNames, pack.getSelectedClass());
						}
						List selectedTypes = new ArrayList();
						for (int j = 0; j < nsType.getSchemaElement().length; j++) {
							if (typeNames.contains(nsType.getSchemaElement(j).getType())) {
								selectedTypes.add(nsType.getSchemaElement(j));
							}
						}
						SchemaElementType[] types = new SchemaElementType[selectedTypes.size()];
						selectedTypes.toArray(types);
						
						// check off type nodes on the types tree
						typesTree.checkTypeNodes(nsType, types);
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
			typeSelectionPanel.add(getTypesTreeScrollPane(), gridBagConstraints1);
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
							"Package incompatability...", message, choices, choices[1]);
						if (choice == choices[0]) {
							// ok, clear out the existing packages and classes
							// TODO:  Could remove the namespaces added by deriving from caDSR
							// packages, but those types might be in use elsewhere
							packageToNamespace.clear();
							// clear out the types table
							while (getTypesTable().getRowCount() != 0) {
								getTypesTable().removeSchemaElementType(0);
							}
							// clear out the types tree
							getTypesTree().clearTree();
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
					setProcessorClass(classBrowserPanel.getSelectedClassName());
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
	
	
	private void setProcessorClass(String className) {
		if (className != null) {
			CommonTools.setServiceProperty(
				getServiceInfo(), DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY, className, false); // TODO: is false correct here?
			// blow away the query processor class properties from the extension data
			ExtensionTools.removeExtensionDataElement(getExtensionTypeExtensionData(), DataServiceConstants.QUERY_PROCESSOR_CONFIG_ELEMENT);
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
		
		// selected packages
		Iterator packageIter = packageToNamespace.keySet().iterator();
		while (packageIter.hasNext()) {
			String packName = (String) packageIter.next();
			String nsTypeName = (String) packageToNamespace.get(packName);
			CadsrPackage pack = new CadsrPackage();
			pack.setName(packName);
			pack.setMappedNamespace(nsTypeName);
			
			List selected = new ArrayList();
			// find the namespace type
			for (int i = 0; i < getServiceInfo().getNamespaces().getNamespace().length; i++) {
				NamespaceType ns = getServiceInfo().getNamespaces().getNamespace(i);
				if (ns.getNamespace().equals(nsTypeName)) {
					// selected classes from the namespace
					SchemaElementType[] types = getTypesTree().getCheckedTypes(ns);
					for (int j = 0; j < types.length; j++) {
						selected.add(types[j].getType());
					}
					break;
				}
			}
			String[] selectedClasses = new String[selected.size()];
			selected.toArray(selectedClasses);
			pack.setSelectedClass(selectedClasses);
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
							// ok, clear out the existing packages and classes
							// TODO:  Could remove the namespaces added by deriving from caDSR
							// packages, but those types might be in use elsewhere
							packageToNamespace.clear();
							// clear out the types table
							while (getTypesTable().getRowCount() != 0) {
								getTypesTable().removeSchemaElementType(0);
							}
							// clear out the types tree
							getTypesTree().clearTree();
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
			getTypesTree().addNamespaceType(nsType);
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
							// remove the package from the namespace types tree
							getTypesTree().removeNamespaceType(
								(String) packageToNamespace.get(selectedPackage.getName()));
							// remove namespace from the packageMapping
							packageToNamespace.remove(selectedPackage.getName());
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
					CommonTools.setServiceProperty(getServiceInfo(), DataServiceConstants.VALIDATE_CQL_FLAG, 
						String.valueOf(getCqlSyntaxValidationCheckBox().isSelected()), false);
				}
			});
			// set the check box selection
			if (CommonTools.servicePropertyExists(getServiceInfo(), DataServiceConstants.VALIDATE_CQL_FLAG)) {
				try {
					cqlSyntaxValidationCheckBox.setSelected(Boolean.valueOf(
						CommonTools.getServicePropertyValue(
							getServiceInfo(), DataServiceConstants.VALIDATE_CQL_FLAG)).booleanValue());
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
					CommonTools.setServiceProperty(getServiceInfo(), DataServiceConstants.VALIDATE_DOMAIN_MODEL_FLAG, 
						String.valueOf(getDomainModelValidationCheckBox().isSelected()), false);
				}
			});
			// set the check box selection
			if (CommonTools.servicePropertyExists(getServiceInfo(), DataServiceConstants.VALIDATE_DOMAIN_MODEL_FLAG)) {
				try {
					domainModelValidationCheckBox.setSelected(Boolean.valueOf(
						CommonTools.getServicePropertyValue(
							getServiceInfo(), DataServiceConstants.VALIDATE_DOMAIN_MODEL_FLAG)).booleanValue());
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
	
	
	/**
	 * Temporary stub to get the build going again.
	 * 
	 * This method should reload all GUI components that rely on the service model
	 */
	public void resetGUI() {
		// TODO: implement me
	}
}
