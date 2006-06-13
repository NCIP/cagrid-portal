package gov.nih.nci.cagrid.cadsr.portal.discovery;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cadsr.portal.CaDSRBrowserPanel;
import gov.nih.nci.cagrid.cadsr.portal.PackageSelectedListener;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.projectmobius.client.gme.ImportInfo;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.common.gme.NoSuchSchemaException;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.projectmobius.protocol.gme.SchemaNode;


public class CaDSRTypeSelectionComponent extends NamespaceTypeDiscoveryComponent implements PackageSelectedListener {

	private String gmeURL = null;
	private String cadsrURL = null;

	private CaDSRBrowserPanel caDSRPanel = null;
	private JPanel nsPanel = null;
	private JLabel nsLabel = null;
	private JTextField nsTextField = null;


	public CaDSRTypeSelectionComponent(DiscoveryExtensionDescriptionType desc) {
		super(desc);
		this.cadsrURL = ExtensionTools.getProperty(desc.getProperties(), "CADSR_URL");
		this.gmeURL = ExtensionTools.getProperty(desc.getProperties(), "GME_URL");
		initialize();
		this.getCaDSRPanel().setDefaultCaDSRURL(this.cadsrURL);
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
		NamespaceType input = new NamespaceType();
		try {
			String ns = getNsTextField().getText();
			if (ns.equals("") || ns.equals("unavailable")) {
				return null;
			}
			Namespace namespace = new Namespace(ns);
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
					getNsTextField().setText(namespace.getRaw());
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
					getNsTextField().setText(namespace.getRaw());
				} else {
					return null;
				}
				schemaContents = getSchema(namespace);
			}

			// set the package name
			String packageName = CommonTools.getPackageName(namespace);
			input.setPackageName(packageName);

			input.setNamespace(namespace.getRaw());
			ImportInfo ii = new ImportInfo(namespace);
			input.setLocation("./" + ii.getFileName());

			// popualte the schema elements
			gov.nih.nci.cagrid.introduce.portal.extension.ExtensionTools.setSchemaElements(input, XMLUtilities
				.stringToDocument(schemaContents));
			// write the schema and its imports to the filesystem
			getGME().cacheSchema(namespace, schemaDestinationDir);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return input;
	}


	private String getSchema(Namespace namespace) throws Exception {
		SchemaNode schema = getGME().getSchema(namespace, false);
		return schema.getSchemaContents();

	}


	private XMLDataModelService getGME() throws MobiusException {
		GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
		XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance()
			.getGridService(this.gmeURL);
		return handle;
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
			nsLabel.setText("Namespace:");
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
		try {
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			ExtensionDescription ext = (ExtensionDescription) Utils.deserializeDocument("extension.xml",
				ExtensionDescription.class);
			final CaDSRTypeSelectionComponent panel = new CaDSRTypeSelectionComponent(ext
				.getDiscoveryExtensionDescription());
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(panel, BorderLayout.CENTER);

			JButton createButton = new JButton("Test Create");
			createButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					NamespaceType createdNs = panel.createNamespaceType(new File("."));
					if (createdNs != null) {
						System.out.println("Created Namespace:" + createdNs.getNamespace() + " at location:"
							+ createdNs.getLocation());
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
