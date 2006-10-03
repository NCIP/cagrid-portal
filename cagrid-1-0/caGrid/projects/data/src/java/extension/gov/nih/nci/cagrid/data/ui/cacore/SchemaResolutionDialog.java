package gov.nih.nci.cagrid.data.ui.cacore;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/** 
 *  SchemaResolutionDialog
 *  Dialog to resolve schemas either from the GME or the local file system
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Sep 27, 2006 
 * @version $Id$ 
 */
public class SchemaResolutionDialog extends JDialog {
	
	public static final String SELECT_AN_ITEM = " -- SELECT AN ITEM --";
	
	private transient ServiceInformation serviceInfo;
	private transient CadsrPackage cadsrPackage;
	
	private JButton loadSchemasButton = null;
	private JButton cancelButton = null;
	private JPanel buttonPanel = null;
	private JPanel mainPanel = null;
	private JTabbedPane discoveryTabbedPane = null;
	private boolean resolutionSuccess;
	
	private SchemaResolutionDialog(ServiceInformation info, CadsrPackage pack) {
		super();
		this.serviceInfo = info;
		this.cadsrPackage = pack;
		this.resolutionSuccess = false;
		initialize();
	}
	
	
	public static boolean resolveSchemas(ServiceInformation info, CadsrPackage pack) {
		SchemaResolutionDialog dialog = new SchemaResolutionDialog(info, pack);
		return dialog.resolutionSuccess;
	}
	
	
	private void initialize() {
		setModal(true);
		this.setSize(new java.awt.Dimension(400, 330));
		this.setContentPane(getMainPanel());
		this.setVisible(true);
	}
	

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getLoadSchemasButton() {
		if (loadSchemasButton == null) {
			loadSchemasButton = new JButton();
			loadSchemasButton.setText("Load Schemas");
			loadSchemasButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					resolutionSuccess = loadSchemas();
					dispose();
				}
			});
		}
		return loadSchemasButton;
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
					resolutionSuccess = false;
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
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 1;
			gridBagConstraints15.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints15.gridy = 0;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints14.gridy = 0;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getLoadSchemasButton(), gridBagConstraints14);
			buttonPanel.add(getCancelButton(), gridBagConstraints15);
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
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.gridx = 0;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.gridx = 0;
			gridBagConstraints19.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints19.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints19.gridy = 1;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getButtonPanel(), gridBagConstraints19);
			mainPanel.add(getDiscoveryTabbedPane(), gridBagConstraints);
		}
		return mainPanel;
	}
	
	
	private JTabbedPane getDiscoveryTabbedPane() {
		if (discoveryTabbedPane == null) {
			discoveryTabbedPane = new JTabbedPane();
			// get the discovery extensions from Introduce
			List discoveryTypes = ExtensionsLoader.getInstance().getDiscoveryExtensions();
			if (discoveryTypes != null) {
				Iterator discoIter = discoveryTypes.iterator();
				while (discoIter.hasNext()) {
					DiscoveryExtensionDescriptionType dd = (DiscoveryExtensionDescriptionType) discoIter.next();
					try {
						NamespaceTypeDiscoveryComponent comp = 
							ExtensionTools.getNamespaceTypeDiscoveryComponent(dd.getName());
						if (comp != null) {
							discoveryTabbedPane.addTab(dd.getDisplayName(), comp);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		return discoveryTabbedPane;
	}
	
	
	private boolean loadSchemas() {
		// get the disvovery type component
		NamespaceTypeDiscoveryComponent discComponent = 
			(NamespaceTypeDiscoveryComponent) getDiscoveryTabbedPane().getSelectedComponent();
		// get the service's schema directory
		File schemaDir = new File(CacoreWizardUtils.getServiceBaseDir(serviceInfo) 
			+ File.separator + "schema" 
			+ serviceInfo.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME));
		NamespaceType[] namespaces = discComponent.createNamespaceType(schemaDir);
		if (namespaces == null) {
			ErrorDialog.showErrorDialog("Error getting types from discovery component");
			return false;
		}
		// see if the namespaces have the mapped namespace of the cadsrPackage
		boolean mappedNsFound = false;
		for (int i = 0; i < namespaces.length; i++) {
			if (namespaces[i].getNamespace().equals(cadsrPackage.getMappedNamespace())) {
				mappedNsFound = true;
				break;
			}
		}
		if (!mappedNsFound) {
			// tell user, ask which one should be mapped
			String[] message = {
				"The package's default namespace of",
				cadsrPackage.getMappedNamespace(),
				"could not be found in the selected schema",
				"or any of its imports.\n",
				"Please select a namespace to map",
				"the package to"
			};
			String[] namespaceChoices = new String[namespaces.length];
			for (int i = 0; i < namespaces.length; i++) {
				namespaceChoices[i] = namespaces[i].getNamespace();
			}
			String choice = (String) JOptionPane.showInputDialog(
				this, message, "Namespace Not Found", JOptionPane.QUESTION_MESSAGE, 
				null, namespaceChoices, namespaceChoices[0]);
			if (choice != null) {
				// fix up the mapping
				cadsrPackage.setMappedNamespace(choice);
			} else {
				// if they don't pick a mapping, allow them to try again
				// with no extra garbage in their service dir
				deleteSchemasForNamespaces(namespaces);
				return false;
			}
		}
		// add the namespace types to the service
		for (int i = 0; i < namespaces.length; i++) {
			CommonTools.addNamespace(serviceInfo.getServiceDescriptor(), namespaces[i]);
		}
		// successful schema resolution
		return true;
	}
	
	
	private void deleteSchemasForNamespaces(NamespaceType[] namespaces) {
		String schemaDir = CacoreWizardUtils.getServiceBaseDir(serviceInfo) 
			+ File.separator + "schema" 
			+ serviceInfo.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
		for (int i = 0; i < namespaces.length; i++) {
			File schemaFile = new File(schemaDir + File.separator + namespaces[i].getLocation());
			if (schemaFile.exists()) {
				schemaFile.delete();
			}
		}
	}
}
