package gov.nih.nci.cagrid.introduce.portal.modification.discovery.files;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.XMLUtilities;


public class FileTypesSelectionComponent extends NamespaceTypeDiscoveryComponent {

	public Namespace currentNamespace = null;

	public String currentFile = null;

	private JComboBox schemaComboBox = null;

	private JLabel namespaceLabel = null;

	JLabel nameLabel = null;

	public String filterType = null;

	private JButton browseButton = null;


	/**
	 * This method initializes
	 */
	public FileTypesSelectionComponent() {
		super();
		initialize();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		this.setSize(new java.awt.Dimension(161, 92));
		nameLabel = new JLabel();
		nameLabel.setText("Name");

		GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints10.gridy = 1;
		gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints10.gridx = 0;
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints9.gridy = 0;
		gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints9.gridx = 0;
		namespaceLabel = new JLabel();
		namespaceLabel.setText("Namespace");
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints8.gridy = 1;
		gridBagConstraints8.weightx = 1.0;
		gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints8.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints8.weighty = 1.0D;
		gridBagConstraints8.gridx = 1;
		this.setLayout(new GridBagLayout());
		this.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Type",
			javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
			new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), PortalLookAndFeel.getPanelLabelColor()));
		this.add(namespaceLabel, gridBagConstraints9);
		this.add(getSchemaComboBox(), gridBagConstraints8);
		this.add(nameLabel, gridBagConstraints10);
		this.add(getBrowseButton(), gridBagConstraints);
	}


	/**
	 * This method initializes jComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	public JComboBox getSchemaComboBox() {
		if (schemaComboBox == null) {
			schemaComboBox = new JComboBox();
		}
		return schemaComboBox;
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
						currentFile = new File(ResourceManager.promptFile(FileTypesSelectionComponent.this, null))
							.getAbsolutePath();
						Document doc = XMLUtilities.fileNameToDocument(currentFile);
						currentNamespace = new Namespace(doc.getRootElement().getAttributeValue("targetNamespace"));
						browseButton.setText(currentNamespace.getRaw());
						List elements = doc.getRootElement().getChildren("element",
							doc.getRootElement().getNamespace(Namespace.W3C_XMLSCHEMA));
						getSchemaComboBox().removeAllItems();
						for (int i = 0; i < elements.size(); i++) {
							Element el = (Element) elements.get(i);
							String name = el.getAttributeValue("name");
							getSchemaComboBox().addItem(name);
						}
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

		// set the package name
		String packageName = CommonTools.getPackageName(currentNamespace);
		input.setPackageName(packageName);
		input.setNamespace(currentNamespace.getRaw());
		input.setLocation("./" + new File(currentFile).getName());
		
		Document doc = null;
		try {
			doc = XMLUtilities.fileNameToDocument(currentFile);
		} catch (MobiusException e1) {
			e1.printStackTrace();
		}
		
		List elementTypes = doc.getRootElement().getChildren("element", doc.getRootElement().getNamespace());
		SchemaElementType[] schemaTypes = new SchemaElementType[elementTypes.size()];
		List elements = doc.getRootElement().getChildren("element",
			doc.getRootElement().getNamespace(Namespace.W3C_XMLSCHEMA));
		for (int i = 0; i < elements.size(); i++) {
			Element el = (Element) elements.get(i);
			SchemaElementType type = new SchemaElementType();
			type.setType(el.getAttributeValue("name"));
			schemaTypes[i] = type;
		}
		input.setSchemaElement(schemaTypes);

		try {
			copySchemas(currentFile, schemaDestinationDir);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(FileTypesSelectionComponent.this,
				"Unable to move schema files to service skeleton");
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

} // @jve:decl-index=0:visual-constraint="10,10"
