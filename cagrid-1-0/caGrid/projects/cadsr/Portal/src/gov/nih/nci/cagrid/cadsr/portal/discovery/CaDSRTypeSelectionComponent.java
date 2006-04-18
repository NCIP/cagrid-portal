package gov.nih.nci.cagrid.cadsr.portal.discovery;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cadsr.portal.CaDSRBrowserPanel;
import gov.nih.nci.cagrid.cadsr.portal.PackageSelectedListener;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;


public class CaDSRTypeSelectionComponent extends NamespaceTypeDiscoveryComponent implements PackageSelectedListener {

	public String gmeURL = null;
	public String cadsrURL = null;
	public static String TYPE = "CADSR";

	private CaDSRBrowserPanel caDSRPanel = null;
	private JPanel nsPanel = null;
	private JLabel nsLabel = null;
	private JTextField nsTextField = null;


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
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 1D;
		gridBagConstraints.weighty = 1D;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 0);
		gridBagConstraints4.gridy = 0;
		gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints4.gridwidth = 1;
		gridBagConstraints4.weightx = 1D;
		gridBagConstraints4.weighty = 1D;
		gridBagConstraints4.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.add(getCaDSRPanel(), gridBagConstraints4);
		this.add(getNsPanel(), gridBagConstraints);
	}


	/**
	 * This method initializes gmePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private CaDSRBrowserPanel getCaDSRPanel() {
		if (caDSRPanel == null) {
			caDSRPanel = new CaDSRBrowserPanel(false, false);
			caDSRPanel.addPackageSelectionListener(this);
		}
		return caDSRPanel;
	}


	public NamespaceType createNamespaceType(File schemaDestinationDir) {
		// NamespaceType input = new NamespaceType();
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

		return null;
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


	/**
	 * This method initializes mainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getNsPanel() {
		if (nsPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.insets = new java.awt.Insets(5, 3, 5, 5);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new java.awt.Insets(7, 5, 7, 2);
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.gridx = 0;
			nsLabel = new JLabel();
			nsLabel.setText("Derived Namespace:");
			nsPanel = new JPanel();
			nsPanel.setLayout(new GridBagLayout());
			nsPanel.add(nsLabel, gridBagConstraints1);
			nsPanel.add(getNsTextField(), gridBagConstraints2);
		}
		return nsPanel;
	}


	/**
	 * This method initializes nsTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getNsTextField() {
		if (nsTextField == null) {
			nsTextField = new JTextField();
			nsTextField.setEditable(false);
			nsTextField.setText("unavailable");
		}
		return nsTextField;
	}


	public static void main(String[] args) {
		CaDSRTypeSelectionComponent panel = new CaDSRTypeSelectionComponent(new DiscoveryExtensionDescriptionType());
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}


	public void handlePackageSelection(UMLPackageMetadata pkg) {
		Project proj = getCaDSRPanel().getSelectedProject();
		if (proj != null) {
			// TODO: need to get Context
			String version = proj.getVersion();
			if (version.indexOf(".") < 0) {
				version += ".0";
			}
			getNsTextField().setText("gme://" + proj.getShortName() + ".caBIG/" + version + "/" + pkg.getName());
		} else {
			getNsTextField().setText("unavailable");
		}

	}

}
