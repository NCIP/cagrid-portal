package gov.nih.nci.cagrid.introduce.portal.modification.discovery.files;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.portal.ExtensionTools;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.jdom.Document;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.XMLUtilities;


public class FileTypesSelectionComponent extends NamespaceTypeDiscoveryComponent {

	public Namespace currentNamespace = null;

	public String currentFile = null;

	private JLabel namespaceLabel = null;

	public String filterType = null;

	private JButton browseButton = null;

	private JTextField namespaceText = null;

	private JTextField filenameText = null;


	/**
	 * This method initializes
	 */
	public FileTypesSelectionComponent(DiscoveryExtensionDescriptionType descriptor) {
		super(descriptor);
		initialize();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints2.gridx = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints1.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints.gridy = 0;
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints9.gridy = 1;
		gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints9.gridx = 0;
		namespaceLabel = new JLabel();
		namespaceLabel.setText("Namespace");
		this.setLayout(new GridBagLayout());
		this.add(namespaceLabel, gridBagConstraints9);
		this.add(getBrowseButton(), gridBagConstraints);
		this.add(getNamespaceText(), gridBagConstraints1);
		this.add(getFilenameText(), gridBagConstraints2);
	}


	/**
	 * This method initializes browseButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBrowseButton() {
		if (browseButton == null) {
			browseButton = new JButton();
			browseButton.setText("Browse");
			browseButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						currentFile = new File(ResourceManager.promptFile(FileTypesSelectionComponent.this, null, FileFilters.XSD_FILTER))
							.getAbsolutePath();
						getFilenameText().setText(currentFile);
						Document doc = XMLUtilities.fileNameToDocument(currentFile);
						currentNamespace = new Namespace(doc.getRootElement().getAttributeValue("targetNamespace"));
						getNamespaceText().setText(currentNamespace.getRaw());
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(FileTypesSelectionComponent.this,
							"Please make sure the file is a valid XML Schema");
					}
				}
			});
		}
		return browseButton;
	}


	public NamespaceType createNamespaceType(File schemaDestinationDir) {
		NamespaceType input = new NamespaceType();

		try {
			// set the package name
			String packageName = CommonTools.getPackageName(currentNamespace);
			input.setPackageName(packageName);
			input.setNamespace(currentNamespace.getRaw());
			input.setLocation("./" + new File(currentFile).getName());

			ExtensionTools.setSchemaElements(input, XMLUtilities.fileNameToDocument(currentFile));
			copySchemas(currentFile, schemaDestinationDir);
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
		return input;
	}


	public static void copySchemas(String fileName, File copyToDirectory) throws Exception {
		System.out.println("Looking at schema " + fileName);
		Utils.copyFile(new File(fileName), new File(copyToDirectory.getAbsolutePath() + File.separator
			+ new File(fileName).getName()));
		Document schema = XMLUtilities.fileNameToDocument(fileName);
		List importEls = schema.getRootElement().getChildren("import",
			schema.getRootElement().getNamespace(IntroduceConstants.W3CNAMESPACE));
		if (importEls != null) {
			for (int i = 0; i < importEls.size(); i++) {
				org.jdom.Element importEl = (org.jdom.Element) importEls.get(i);
				String location = importEl.getAttributeValue("schemaLocation");
				if ((location.indexOf(".\\") == 0) || (location.indexOf("./") == 0) || (location.indexOf("\\") > 0)
					|| (location.indexOf("/") == 0)) {
					File file = new File(fileName);
					copySchemas(file.getParentFile().getAbsolutePath() + File.separator + location, copyToDirectory);
				} else {
					copySchemas(location, copyToDirectory);
				}
			}
		}

	}


	/**
	 * This method initializes namespaceText
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getNamespaceText() {
		if (namespaceText == null) {
			namespaceText = new JTextField();
			namespaceText.setEditable(false);
		}
		return namespaceText;
	}


	/**
	 * This method initializes filenameText
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getFilenameText() {
		if (filenameText == null) {
			filenameText = new JTextField();
			filenameText.setEnabled(true);
			filenameText.setEditable(false);
		}
		return filenameText;
	}

} // @jve:decl-index=0:visual-constraint="16,10"
