package gov.nih.nci.cagrid.introduce.portal.types.data;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.BusyDialogRunnable;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.IntroducePortalConf;
import gov.nih.nci.cagrid.introduce.portal.modification.gme.GMETypeSelectionComponent;
import gov.nih.nci.cagrid.introduce.portal.modification.types.NamespaceTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.types.NamespacesJTree;
import gov.nih.nci.cagrid.introduce.portal.modification.types.SchemaElementTypeTreeNode;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.namespace.QName;

import org.jdom.Document;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.portal.PortalResourceManager;

/** 
 *  DataServiceModifier
 *  Viewer / modifier interface for data types exposed by a data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Mar 22, 2006 
 * @version $Id$ 
 */
public class DataServiceModifier extends GridPortalComponent {
	private GMETypeSelectionComponent gmeTypeSelector = null;
	private NamespacesJTree namespaceTree = null;
	private DataTypesTable dataTypesTable = null;
	private JTabbedPane configTabbedPane = null;
	private JButton includeNamespaceButton = null;
	private JPanel typeSelectionPanel = null;
	private JScrollPane namespaceTreeScrollPane = null;
	private JScrollPane dataTypesScrollPane = null;
	private JSplitPane typesSplitPane = null;
	private JButton saveButton = null;
	private JButton undoButton = null;
	private JButton cancelButton = null;
	private JPanel buttonPanel = null;
	private JPanel mainPanel = null;
	private JButton addTypeButton = null;
	private JButton removeTypeButton = null;
	private JPanel typeButtonPanel = null;
	private JPanel namespaceButtonPanel = null;
	private JButton removeNamespaceButton = null;
	private JPanel availableTypesPanel = null;
	
	// service information
	private ServiceDescription dataServiceDescription;
	private ServiceInformation dataServiceInformation;
	private File serviceDirectory;
	
	// flag to indicate data has been modified
	private boolean dirty;
	
	public DataServiceModifier() {
		this(promptForDirectory());
	}
	
	
	public DataServiceModifier(File serviceDirectory) {
		super();
		this.dirty = false;
		this.serviceDirectory = serviceDirectory;
		loadStoredServiceData();
		initialize();
	}
	
	
	private static File promptForDirectory() {
		File dir = null;
		boolean choiceMade = false;
		while (!choiceMade) {
			try {
				String dirName = ResourceManager.promptDir(PortalResourceManager.getInstance().getGridPortal(), (dir == null ? null : dir.getAbsolutePath()));
				if (dirName != null) {
					dir = new File(dirName);
					File introduceFile = new File(dir.getAbsolutePath() + File.separator + "introduce.xml");
					if (introduceFile.exists() && introduceFile.canRead()) {
						choiceMade = true;
					} else {
						String[] message = {
							"Error reading the required introduce template file",
							introduceFile.getAbsolutePath(),
							"OK to procede with this directory?"
						};
						int choice = JOptionPane.showConfirmDialog(PortalResourceManager.getInstance().getGridPortal(),
							message, "Error reading template", JOptionPane.YES_NO_OPTION);
						if (choice == JOptionPane.OK_OPTION) {
							// alright, then...
							choiceMade = true;
						}
					}
				} else {
					// user canceled the dialog, must be happy with no directory...
					dir = null;
					choiceMade = true;
				}
			} catch (Exception ex) {
				choiceMade = false;
				PortalUtils.showErrorMessage(ex);
			}
		}
		return dir;
	}
	
	
	private void initialize() {
		setTitle("Modify Data Service");
		setFrameIcon(IntroduceLookAndFeel.getModifyIcon());
		this.setContentPane(getMainPanel());
		setSize(800, 500);
	}
	
	
	private void loadStoredServiceData() {
		if (serviceDirectory != null) {
			System.out.println("Loading stored service data");
			try {
				dataServiceDescription = (ServiceDescription) Utils.deserializeDocument(serviceDirectory.getAbsolutePath()
					+ File.separator + "introduce.xml", ServiceDescription.class);
				if (dataServiceDescription.getIntroduceVersion() == null
					|| !dataServiceDescription.getIntroduceVersion().equals(IntroduceConstants.INTRODUCE_VERSION)) {
					throw new Exception(
						"Introduce version in project does not match version provided by Introduce Toolkit ( "
						+ IntroduceConstants.INTRODUCE_VERSION + " ): " + dataServiceDescription.getIntroduceVersion());
				}
				
				Properties dataServiceProperties = new Properties();
				dataServiceProperties.load(new FileInputStream(serviceDirectory.getAbsolutePath() + File.separator
					+ IntroduceConstants.INTRODUCE_PROPERTIES_FILE));
				dataServiceProperties.setProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR, serviceDirectory.getAbsolutePath());
				dataServiceInformation = new ServiceInformation(dataServiceDescription, dataServiceProperties, serviceDirectory);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception ex) {
				PortalUtils.showErrorMessage(ex);
				ex.printStackTrace();
			}
		}
	}
	
	
	private GMETypeSelectionComponent getGmeTypeSelector() {
		if (gmeTypeSelector == null) {
			gmeTypeSelector = new GMETypeSelectionComponent();
		}
		return gmeTypeSelector;
	}


	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getConfigTabbedPane() {
		if (configTabbedPane == null) {
			configTabbedPane = new JTabbedPane();
			configTabbedPane.addTab("Available Data Types", null, getTypesSplitPane(), null);
		}
		return configTabbedPane;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getIncludeNamespaceButton() {
		if (includeNamespaceButton == null) {
			includeNamespaceButton = new JButton();
			includeNamespaceButton.setText("Include Namespace");
			includeNamespaceButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// get namespace from the GME panel
					dirty = true;
					NamespaceType nsType = getGmeTypeSelector().createNamespace();
					getNamespaceTree().addNode(nsType);
					cacheSchema(new File(serviceDirectory + File.separator + "schema" + File.separator
						+ dataServiceInformation.getServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME)), 
						nsType.getNamespace());
					addNamespaceToServiceDescription(nsType);
				}
			});
			includeNamespaceButton.setIcon(PortalLookAndFeel.getAddIcon());
		}
		return includeNamespaceButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTypeSelectionPanel() {
		if (typeSelectionPanel == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.gridy = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints11.gridy = 2;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints11.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.gridy = 0;
			typeSelectionPanel = new JPanel();
			typeSelectionPanel.setLayout(new GridBagLayout());
			typeSelectionPanel.add(getGmeTypeSelector(), gridBagConstraints);
			typeSelectionPanel.add(getNamespaceTreeScrollPane(), gridBagConstraints11);
			typeSelectionPanel.add(getNamespaceButtonPanel(), gridBagConstraints12);
		}
		return typeSelectionPanel;
	}
	
	
	private NamespacesJTree getNamespaceTree() {
		if (namespaceTree == null) {
			namespaceTree = new NamespacesJTree(dataServiceDescription.getNamespaces());
			namespaceTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		}
		return namespaceTree;
	}


	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getNamespaceTreeScrollPane() {
		if (namespaceTreeScrollPane == null) {
			namespaceTreeScrollPane = new JScrollPane();
			namespaceTreeScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Available Types", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			namespaceTreeScrollPane.setViewportView(getNamespaceTree());
		}
		return namespaceTreeScrollPane;
	}
	
	
	private DataTypesTable getDataTypesTable() {
		if (dataTypesTable == null) {
			dataTypesTable = new DataTypesTable();
		}
		return dataTypesTable;
	}
	
	
	private JScrollPane getDataTypesScrollPane() {
		if (dataTypesScrollPane == null) {
			dataTypesScrollPane = new JScrollPane();
			dataTypesScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Selected Types", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			dataTypesScrollPane.setViewportView(getDataTypesTable());
		}
		return dataTypesScrollPane;
	}


	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getTypesSplitPane() {
		if (typesSplitPane == null) {
			typesSplitPane = new JSplitPane();
			typesSplitPane.setLeftComponent(getTypeSelectionPanel());
			typesSplitPane.setRightComponent(getAvailableTypesPanel());
			typesSplitPane.setOneTouchExpandable(true);
		}
		return typesSplitPane;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton();
			saveButton.setText("Save");
			saveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int choice = JOptionPane.showConfirmDialog(DataServiceModifier.this,
						"Are you sure you would like to save changes?", "Confirm Save", JOptionPane.OK_OPTION);
					if (choice == JOptionPane.OK_OPTION) {
						saveService();
					}
				}
			});
			saveButton.setIcon(PortalLookAndFeel.getSaveIcon());
		}
		return saveButton;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getUndoButton() {
		if (undoButton == null) {
			undoButton = new JButton();
			undoButton.setText("Undo");
			undoButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int choice = JOptionPane.OK_OPTION;
					if (dirty) {
						choice = JOptionPane.showConfirmDialog(DataServiceModifier.this,
							"Are you sure you want to roll back all changes?", "Confirm Undo", JOptionPane.OK_CANCEL_OPTION);
					}
					if (choice == JOptionPane.OK_OPTION) {
						BusyDialogRunnable undo = new BusyDialogRunnable(PortalResourceManager.getInstance().getGridPortal(), "Undo") {
							public void process() {
								setProgressText("restoring from local cache");
								try {
									Properties dataServiceProperties = dataServiceInformation.getServiceProperties();
									ResourceManager.restoreLatest(
										dataServiceProperties.getProperty("introduce.skeleton.timestamp"), 
										dataServiceProperties.getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME),
										dataServiceProperties.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR));
									dispose();
									PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
										new DataServiceModifier(/*directory*/));
								} catch (Exception ex) {
									ex.printStackTrace();
									PortalUtils.showMessage("Unable to roll back, there may be no previous versions available");
								}
							}
						};
						Thread executor = new Thread(undo);
						executor.start();
					}
				}
			});
			undoButton.setIcon(IntroduceLookAndFeel.getUndoIcon());
		}
		return undoButton;
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
					int choice = JOptionPane.OK_OPTION;
					if (dirty) {
						choice = JOptionPane.showConfirmDialog(DataServiceModifier.this, "Cancel all changes?", 
							"Are you sure?", JOptionPane.YES_NO_OPTION);
					}
					if (choice == JOptionPane.OK_OPTION) {
						dispose();
					}
				}
			});
			cancelButton.setIcon(PortalLookAndFeel.getCloseIcon());
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
			buttonPanel = new JPanel();
			buttonPanel.add(getSaveButton(), null);
			buttonPanel.add(getUndoButton(), null);
			buttonPanel.add(getCancelButton(), null);
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
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.gridx = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getConfigTabbedPane(), gridBagConstraints2);
			mainPanel.add(getButtonPanel(), gridBagConstraints3);
		}
		return mainPanel;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddTypeButton() {
		if (addTypeButton == null) {
			addTypeButton = new JButton();
			addTypeButton.setText("Add Type");
			addTypeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					List selectedNodes = getNamespaceTree().getSelectedNodes();
					if (selectedNodes.size() != 0 && selectedNodes.get(0) instanceof SchemaElementTypeTreeNode) {
						dirty = true;
						SchemaElementTypeTreeNode selectedNode = (SchemaElementTypeTreeNode) selectedNodes.get(0);
						NamespaceTypeTreeNode namespaceNode = (NamespaceTypeTreeNode) selectedNode.getParent();
						NamespaceType namespace = (NamespaceType) namespaceNode.getUserObject();
						SchemaElementType type = (SchemaElementType) selectedNode.getUserObject();
						getDataTypesTable().addSchemaElement(namespace, type);
					}
				}
			});
			addTypeButton.setIcon(PortalLookAndFeel.getAddIcon());
		}
		return addTypeButton;
	}


	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRemoveTypeButton() {
		if (removeTypeButton == null) {
			removeTypeButton = new JButton();
			removeTypeButton.setText("Remove Type");
			removeTypeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dirty = true;
					getDataTypesTable().removeSelectedType();
				}
			});
			removeTypeButton.setIcon(PortalLookAndFeel.getRemoveIcon());
		}
		return removeTypeButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTypeButtonPanel() {
		if (typeButtonPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints5.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints4.gridy = 0;
			typeButtonPanel = new JPanel();
			typeButtonPanel.setLayout(new GridBagLayout());
			typeButtonPanel.add(getAddTypeButton(), gridBagConstraints4);
			typeButtonPanel.add(getRemoveTypeButton(), gridBagConstraints5);
		}
		return typeButtonPanel;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getNamespaceButtonPanel() {
		if (namespaceButtonPanel == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints6.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.gridx = 0;
			namespaceButtonPanel = new JPanel();
			namespaceButtonPanel.setLayout(new GridBagLayout());
			namespaceButtonPanel.add(getIncludeNamespaceButton(), gridBagConstraints1);
			namespaceButtonPanel.add(getRemoveNamespaceButton(), gridBagConstraints6);
		}
		return namespaceButtonPanel;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRemoveNamespaceButton() {
		if (removeNamespaceButton == null) {
			removeNamespaceButton = new JButton();
			removeNamespaceButton.setText("Remove Namespace");
			removeNamespaceButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (getNamespaceTree().getCurrentNode() != null && getNamespaceTree().getCurrentNode() instanceof NamespaceTypeTreeNode) {
						dirty = true;
						NamespaceType nsType = (NamespaceType) getNamespaceTree().getCurrentNode().getUserObject();
						getNamespaceTree().removeSelectedNode();
						removeSchemaFromCache(nsType.getNamespace());
						removeNamespaceFromServiceDescription(nsType);
					}
				}
			});
			removeNamespaceButton.setIcon(PortalLookAndFeel.getRemoveIcon());
		}
		return removeNamespaceButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAvailableTypesPanel() {
		if (availableTypesPanel == null) {
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridy = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.weighty = 1.0D;
			gridBagConstraints7.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints7.gridx = 0;
			availableTypesPanel = new JPanel();
			availableTypesPanel.setLayout(new GridBagLayout());
			availableTypesPanel.add(getDataTypesScrollPane(), gridBagConstraints7);
			availableTypesPanel.add(getTypeButtonPanel(), gridBagConstraints8);
		}
		return availableTypesPanel;
	}

	
	/**
	 * Pulls the schema from the GME and caches it in the service's schema directory
	 * @param dir
	 * @param namespace
	 */
	private void cacheSchema(File dir, String namespace) {
		if (namespace.equals(IntroduceConstants.W3CNAMESPACE)) {
			// this is "natively supported" so we don't need to cache it
			return;
		}
		IntroducePortalConf conf = (IntroducePortalConf) PortalResourceManager.getInstance().getResource(
			IntroducePortalConf.RESOURCE);
		GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
		try {
			XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance().getGridService(
				conf.getGME());
			handle.cacheSchema(new Namespace(namespace), dir);
		} catch (MobiusException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(DataServiceModifier.this,
				"Please check the GME URL and make sure that you have the appropriate credentials!");
		}
	}
	
	
	private void removeSchemaFromCache(String namespace) {
		/*
		 * The following ugly hack brought to you by the fact that the GME's
		 * cacheSchema() method doesn't tell you what file name it saved the schema
		 * to.  Therefore, I'm opening the schema directory, iterating all XSDs,
		 * looking at their namespace, and removing the one that matches.
		 */
		final File schemaDir = new File(serviceDirectory.getAbsolutePath() + File.separator + "schema");
		File[] schemaFiles = schemaDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return (dir.equals(schemaDir) && name.toLowerCase().endsWith(".xsd"));
			}
		});
		for (int i = 0; i < schemaFiles.length; i++) {
			try {
				Namespace ns = new Namespace(namespace);
				Document schemaDoc = XMLUtilities.fileNameToDocument(schemaFiles[i].getAbsolutePath());
				String targetNamespace = schemaDoc.getRootElement().getAttributeValue("targetNamespace");
				Namespace targetNs = new Namespace(targetNamespace);
				if (ns.getNamespace().equals(targetNs.getNamespace())) {
					schemaFiles[i].delete();
					break;
				}
			} catch (MobiusException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	private void addNamespaceToServiceDescription(NamespaceType ns) {
		// copy existing namespaces over to another array
		NamespaceType[] namespaces;
		NamespaceType[] existingNamespaces = dataServiceDescription.getNamespaces().getNamespace();
		if (existingNamespaces == null) {
			namespaces = new NamespaceType[1];
		} else {
			namespaces = new NamespaceType[existingNamespaces.length + 1];
			System.arraycopy(existingNamespaces, 0, namespaces, 0, existingNamespaces.length);
		}
		// insert the new namespace
		namespaces[namespaces.length - 1] = ns;
		dataServiceDescription.getNamespaces().setNamespace(namespaces);
	}
	
	
	private void removeNamespaceFromServiceDescription(NamespaceType ns) {
		// create a new array of namespace types
		NamespaceType[] existingNamespaces = dataServiceDescription.getNamespaces().getNamespace();
		NamespaceType[] namespaces = new NamespaceType[existingNamespaces.length - 1];
		int namespaceIndex = 0;
		for (int i = 0; i < existingNamespaces.length; i++) {
			if (!ns.equals(existingNamespaces[i])) {
				namespaces[namespaceIndex] = existingNamespaces[i];
				namespaceIndex++;
			}
		}
		// set the namespaces into the service description
		dataServiceDescription.getNamespaces().setNamespace(namespaces);
	}
	
	
	private void saveService() {
		BusyDialogRunnable saver = new BusyDialogRunnable(PortalResourceManager.getInstance().getGridPortal(), "Save Service") {
			public void process() {
				setProgressText("saving service description");
				try {
					Utils.serializeDocument(serviceDirectory.getAbsolutePath() + File.separator
						+ "introduce.xml", dataServiceDescription, new QName(
							"gme://gov.nih.nci.cagrid/1/Introduce", "ServiceSkeleton"));
				} catch (Exception ex) {
					ex.printStackTrace();
					PortalUtils.showErrorMessage("Error saving description", ex);
				}
				setProgressText("synchronizing the service");
				try {
					// call the sync tools
					SyncTools sync = new SyncTools(serviceDirectory);
					sync.sync();
				} catch (Exception ex) {
					ex.printStackTrace();
					PortalUtils.showErrorMessage("Error synchronizing the service", ex);
				}
				setProgressText("rebuilding skeleton");
				try {
					String cmd = CommonTools.getAntCommand("clean all", serviceDirectory.getAbsolutePath());
					Process p = CommonTools.createAndOutputProcess(cmd);
					p.waitFor();
					if (p.exitValue() != 0) {
						JOptionPane.showMessageDialog(DataServiceModifier.this,
						"Error: Unable to rebuild the skeleton");
						return;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					PortalUtils.showErrorMessage("Error rebuilding skeleton", ex);
				}
				dirty = false;
				setProgressText("reloading service properties");
				loadStoredServiceData();
				this.setProgressText("");
			}
		};
		Thread runner = new Thread(saver);
		runner.run();
	}
}
