package gov.nih.nci.cagrid.introduce.portal.modification.discovery.gme;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.portal.discoverytools.gme.GMESchemaLocatorPanel;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.projectmobius.client.gme.ImportInfo;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;


/**
 * GMETypeExtractionPanel
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jul 7, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class GMETypeSelectionComponent extends NamespaceTypeDiscoveryComponent {
	public static String GME_URL = "GME_URL";
	public static String TYPE = "GME";

	private GMESchemaLocatorPanel gmePanel = null;


	public GMETypeSelectionComponent(DiscoveryExtensionDescriptionType descriptor) {
		super(descriptor);
		initialize();
		this.getGmePanel().discoverFromGME();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 0);
		gridBagConstraints4.gridy = 0;
		gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints4.gridwidth = 1;
		gridBagConstraints4.weightx = 1.0D;
		gridBagConstraints4.weighty = 1.0D;
		gridBagConstraints4.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.add(getGmePanel(), gridBagConstraints4);
	}


	/**
	 * This method initializes gmePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private GMESchemaLocatorPanel getGmePanel() {
		if (gmePanel == null) {
			gmePanel = new GMESchemaLocatorPanel(false);
		}
		return gmePanel;
	}


	public NamespaceType[] createNamespaceType(File schemaDestinationDir) {
		Namespace selectedNS = getGmePanel().getSelectedSchemaNamespace();
		if (!selectedNS.getRaw().equals(IntroduceConstants.W3CNAMESPACE)) {
			try {
				if (selectedNS != null) {
					List namespaces = new ArrayList();
					NamespaceType root = new NamespaceType();
					// set the package name
					String packageName = CommonTools.getPackageName(selectedNS);
					root.setPackageName(packageName);
					root.setNamespace(selectedNS.getRaw());
					ImportInfo ii = new ImportInfo(selectedNS);
					root.setLocation("./" + ii.getFileName());
					namespaces.add(root);

					gov.nih.nci.cagrid.introduce.portal.extension.ExtensionTools.setSchemaElements(root, XMLUtilities
						.stringToDocument(gmePanel.currentNode.getSchemaContents()));
					List importedNamespaces = cacheSchema(schemaDestinationDir, root.getNamespace());
					Iterator importedNsIter = importedNamespaces.iterator();
					while (importedNsIter.hasNext()) {
						Namespace ns = (Namespace) importedNsIter.next();
						if (!ns.getRaw().equals(root.getNamespace())) {
							ImportInfo importInfo = new ImportInfo(ns);
							String filename = importInfo.getFileName();
							File schemaFile = new File(schemaDestinationDir.getAbsolutePath() + File.separator
								+ filename);
							NamespaceType type = CommonTools.createNamespaceType(schemaFile.getAbsolutePath());
							String relPath = Utils.getRelativePath(schemaDestinationDir, schemaFile);
							type.setLocation(relPath);
							namespaces.add(type);
						}
					}
					NamespaceType[] types = new NamespaceType[namespaces.size()];
					namespaces.toArray(types);
					return types;
				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return new NamespaceType[0];
		}
	}


	private List cacheSchema(File dir, String namespace) throws Exception {
		GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
		XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance().getGridService(
			ResourceManager.getServiceURLProperty(GMETypeSelectionComponent.GME_URL));
		return handle.cacheSchema(new Namespace(namespace), dir);
	}


	public static void main(String[] args) {
		try {
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			ExtensionDescription ext = (ExtensionDescription) Utils.deserializeDocument("extensions" + File.separator
				+ "gme_discovery" + File.separator + "extension.xml", ExtensionDescription.class);
			final GMETypeSelectionComponent panel = new GMETypeSelectionComponent(ext
				.getDiscoveryExtensionDescription());
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(panel, BorderLayout.CENTER);

			JButton createButton = new JButton("Test Create");
			createButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					NamespaceType[] createdNs = panel.createNamespaceType(new File("."));
					if (createdNs != null) {
						for (int i = 0; i < createdNs.length; i++) {
							System.out.println("Created Namespace:" + createdNs[i].getNamespace() + " at location:"
								+ createdNs[i].getLocation());
						}
					} else {
						System.out.println("Problem creating namespace");
					}
				}
			});
			frame.getContentPane().add(createButton, BorderLayout.SOUTH);

			frame.pack();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
