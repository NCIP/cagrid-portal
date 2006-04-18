package gov.nih.nci.cagrid.cadsr.portal.discovery;

import gov.nih.nci.cagrid.cadsr.portal.CaDSRBrowserPanel;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.JFrame;

import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;


public class CaDSRTypeSelectionComponent extends NamespaceTypeDiscoveryComponent {

	public String gmeURL = null;
	public String cadsrURL = null;
	public static String TYPE = "CADSR";

	private CaDSRBrowserPanel caDSRPanel = null;


	public CaDSRTypeSelectionComponent(DiscoveryExtensionDescriptionType desc) {
		super(desc);
		this.cadsrURL = ExtensionTools.getProperty(desc.getProperties(), "CADSR_URL");
		this.gmeURL = ExtensionTools.getProperty(desc.getProperties(), "GME_URL");
		initialize();
		this.getCaDSRPanel().discoverFromCaDSR();

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
		this.add(getCaDSRPanel(), gridBagConstraints4);
	}


	/**
	 * This method initializes gmePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private CaDSRBrowserPanel getCaDSRPanel() {
		if (caDSRPanel == null) {
			caDSRPanel = new CaDSRBrowserPanel(false, false);
		}
		return caDSRPanel;
	}


	public NamespaceType createNamespaceType(File schemaDestinationDir) {
		NamespaceType input = new NamespaceType();
		// try {
		// // set the package name
		// String packageName =
		// CommonTools.getPackageName(gmePanel.currentNamespace);
		// input.setPackageName(packageName);
		//
		// if (this.gmePanel.currentNamespace != null) {
		// input.setNamespace(this.gmePanel.currentNamespace.getRaw());
		// ImportInfo ii = new ImportInfo(this.gmePanel.currentNamespace);
		// input.setLocation("./" + ii.getFileName());
		// } else {
		// return null;
		// }
		//
		// ExtensionTools.setSchemaElements(input,
		// XMLUtilities.stringToDocument(gmePanel.currentNode
		// .getSchemaContents()));
		// cacheSchema(schemaDestinationDir, input.getNamespace());
		// } catch (Exception e) {
		// e.printStackTrace();
		// return null;
		// }

		return input;
	}


	private void cacheSchema(File dir, String namespace) throws Exception {
		if (namespace.equals(IntroduceConstants.W3CNAMESPACE)) {
			// this is "natively supported" so we don't need to cache it
			return;
		}

		GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
		XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance()
			.getGridService(this.gmeURL);
		handle.cacheSchema(new org.projectmobius.common.Namespace(namespace), dir);

	}


	public static void main(String[] args) {
		CaDSRTypeSelectionComponent panel = new CaDSRTypeSelectionComponent(new DiscoveryExtensionDescriptionType());
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
