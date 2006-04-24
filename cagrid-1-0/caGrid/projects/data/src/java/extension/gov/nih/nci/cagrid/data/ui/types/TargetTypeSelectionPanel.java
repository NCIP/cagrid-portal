package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cadsr.portal.CaDSRBrowserPanel;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.extension.ServiceModificationUIPanel;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
	
	private CaDSRBrowserPanel domainBrowserPanel = null;
	private TargetTypesTree typesTree = null;
	private JScrollPane typesTreeScrollPane = null;
	private DataServiceTypesTable typesTable = null;
	private TypeSerializationConfigPanel serializationConfigPanel = null;
	private JScrollPane typesTableScrollPane = null;
	private JPanel serializationPanel = null;
	private JPanel typeSelectionPanel = null;
	private JButton setModelButton = null;
	
	private XMLDataModelService gmeHandle = null;
	
	public TargetTypeSelectionPanel(ServiceExtensionDescriptionType desc, ServiceInformation serviceInfo) {
		super(desc, serviceInfo);
		initialize();
	}
	
	
	private void initialize() {
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 1;
		gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints5.weightx = 1.0D;
		gridBagConstraints5.weighty = 1.0D;
		gridBagConstraints5.gridy = 0;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints4.weighty = 1.0D;
		gridBagConstraints4.weightx = 1.0D;
		gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTH;
		gridBagConstraints4.gridy = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(new java.awt.Dimension(1017,548));
		this.add(getTypeSelectionPanel(), gridBagConstraints4);
		this.add(getSerializationPanel(), gridBagConstraints5);	
	}
	
	
	private CaDSRBrowserPanel getDomainBrowserPanel() {
		if (domainBrowserPanel == null) {
			domainBrowserPanel = new CaDSRBrowserPanel(true, false);
		}
		return domainBrowserPanel;
	}
	
	
	private TargetTypesTree getTypesTree() {
		if (typesTree == null) {
			typesTree = new TargetTypesTree();
			typesTree.addTypeSelectionListener(new TypeSelectionListener() {
				public void typeSelectionAdded(TypeSelectionEvent e) {
					getTypesTable().addType(getTypesTree().getOriginalNamespace(), e.getSchemaElementType());
				}
				
				
				public void typeSelectionRemoved(TypeSelectionEvent e) {
					getTypesTable().removeSchemaElementType(e.getSchemaElementType());
				}
			});
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
			typesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			typesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					SchemaElementType selected = getTypesTable().getSelectedElementType();
					if (selected != null) {
						getSerializationConfigPanel().setSchemaElementType(selected);
					} else {
						getSerializationConfigPanel().clear();
					}
				}
			});
		}
		return typesTable;
	}
	
	
	private TypeSerializationConfigPanel getSerializationConfigPanel() {
		if (serializationConfigPanel == null) {
			serializationConfigPanel = new TypeSerializationConfigPanel(getTypesTable());
		}
		return serializationConfigPanel;
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
	private JPanel getSerializationPanel() {
		if (serializationPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.weightx = 1.0D;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.weighty = 1.0D;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.gridx = 0;
			serializationPanel = new JPanel();
			serializationPanel.setLayout(new GridBagLayout());
			serializationPanel.add(getTypesTableScrollPane(), gridBagConstraints3);
			serializationPanel.add(getSerializationConfigPanel(), gridBagConstraints2);
		}
		return serializationPanel;
	}
	
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTypeSelectionPanel() {
		if (typeSelectionPanel == null) {
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 0;
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
						NamespaceType nsType = createNamespaceType(pack);
						NamespaceType oldNsType = getTypesTree().getOriginalNamespace();
						getTypesTree().setNamespace(nsType);
						// add the namespace model to the service information
						NamespaceType[] serviceNsTypes = getServiceInfo().getServiceDescriptor().getNamespaces().getNamespace();
						if (serviceNsTypes != null && serviceNsTypes.length != 0) {
							for (int i = 0; i < serviceNsTypes.length; i++) {
								if (serviceNsTypes[i] == oldNsType) {
									serviceNsTypes[i] = nsType;
									break;
								}
							}
						} else {
							serviceNsTypes = new NamespaceType[] {nsType};
						}
						getServiceInfo().getServiceDescriptor().getNamespaces().setNamespace(serviceNsTypes);
					}
				}
			});
		}
		return setModelButton;
	}
	
	
	private NamespaceType createNamespaceType(UMLPackageMetadata pack) {
		String namespaceString = null;
		Project proj = getDomainBrowserPanel().getSelectedProject();
		if (proj != null) {
			// TODO: need to get Context
			String version = proj.getVersion();
			if (version.indexOf(".") < 0) {
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
				
				nsType.setNamespace(namespaceString);
				ImportInfo ii = new ImportInfo(namespace);
				nsType.setLocation("./" + ii.getFileName());
				
				// popualte the schema elements
				gov.nih.nci.cagrid.introduce.portal.ExtensionTools.setSchemaElements(
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
			"schema" + File.separator + getServiceInfo().getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
		return new File(dir);
	}
}
