package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.data.ui.cacore.CacoreWizardUtils;
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
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.projectmobius.portal.PortalResourceManager;

/** 
 *  SchemaResolutionDialog
 *  Dialog to resolve schemas from all available namespace type discovery extension components
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
		super(PortalResourceManager.getInstance().getGridPortal(), "Schema Resolution", true);
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
			+ File.separator + "schema" + File.separator
			+ serviceInfo.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME));
		NamespaceType[] namespaces = discComponent.createNamespaceType(schemaDir);
		if (namespaces == null) {
			ErrorDialog.showErrorDialog("Error getting types from discovery component");
			return false;
		}
		if (namespaces.length != 0) {
			// user may have mapped the package to a different namespace...
			cadsrPackage.setMappedNamespace(namespaces[0].getNamespace());
			// add the namespace types to the service
			for (int i = 0; i < namespaces.length; i++) {
				CommonTools.addNamespace(serviceInfo.getServiceDescriptor(), namespaces[i]);
			}
			// successful schema resolution
			return true;
		}
		return false;
	}
}
