/**
 * 
 */
package org.cagrid.installer.steps;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.util.AutoSizingJTable;
import org.cagrid.installer.util.InstallerUtils;
import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ConfigureServiceMetadataStep extends PanelWizardStep implements
		ActionListener {

	private static final Log logger = LogFactory
			.getLog(ConfigureServiceMetadataStep.class);

	private static final int AFFILIATION_COL = 0;

	private static final int EMAIL_COL = 1;

	private static final int FIRST_NAME_COL = 2;

	private static final int LAST_NAME_COL = 3;
	
	private static final int PHONE_NUMBER_COL = 4;

	private static final int ROLE_COL = 5;

	private static final String COMMON_NS = "gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common";

	private static final String SERVICE_NS = "gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.service";

	private static final String METADATA_NS = "gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata";

	

	protected CaGridInstallerModel model;

	private JButton sPOCsCmdAdd;

	private JButton sPOCsCmdDelete;

	private DefaultTableModel sPOCsTableModel;

	private JTable sPOCsTable;

	private JButton rPOCsCmdAdd;

	private JButton rPOCsCmdDelete;

	private DefaultTableModel rPOCsTableModel;

	private JTable rPOCsTable;

	private JTextField rcDisplayNameField;

	private JTextField rcShortNameField;

	private JTextField rcCountryField;

	private JTextField rcLocalityField;

	private JTextField rcPostalCodeField;

	private JTextField rcStateProvinceField;

	private JTextField rcStreet1Field;

	private JTextField rcStreet2Field;

	private JTextField rcDescriptionField;

	private JTextField rcHomepageURLField;

	private JTextField rcImageURLField;

	private JTextField rcRSSNewsURLField;

	private String serviceDirProp;

	private XPathFactory xpFact;

	public ConfigureServiceMetadataStep() {

	}

	public ConfigureServiceMetadataStep(String serviceDirProp, String name,
			String summary) {
		this(serviceDirProp, name, summary, null);
	}

	public ConfigureServiceMetadataStep(String serviceDirProp, String name,
			String summary, Icon icon) {
		super(name, summary, icon);
		this.serviceDirProp = serviceDirProp;
	}

	public void init(WizardModel m) {
		if (!(m instanceof CaGridInstallerModel)) {
			throw new IllegalStateException(
					"This step requires a StatefulWizardModel instance.");
		}
		this.model = (CaGridInstallerModel) m;

		this.xpFact = XPathFactory.newInstance();

		setLayout(new GridBagLayout());

		String[] pocColNames = new String[] { "Affiliation", "Email",
				"First Name", "Last Name", "Phone Number", "Role" };

		JPanel svcPanel = new JPanel();
		svcPanel.setLayout(new GridBagLayout());
		svcPanel.setBorder(new TitledBorder("Service Metadata"));
		add(svcPanel, InstallerUtils.getGridBagConstraints(0, 0));

		JPanel rcPanel = new JPanel();
		rcPanel.setLayout(new GridBagLayout());
		rcPanel.setBorder(new TitledBorder("Research Center Metadata"));
		add(rcPanel, InstallerUtils.getGridBagConstraints(0, 1));

		// Service POCs table
		JPanel sPOCsPanel = new JPanel();
		sPOCsPanel.setLayout(new BorderLayout());
		sPOCsPanel.setPreferredSize(new Dimension(500, 125));
		svcPanel.add(sPOCsPanel, InstallerUtils.getGridBagConstraints(0, 0));

		// Service POCs tabel buttons
		JPanel sPOCsButtonsPanel = new JPanel();
		this.sPOCsCmdAdd = new JButton(this.model.getMessage("add"));
		this.sPOCsCmdAdd.addActionListener(this);
		this.sPOCsCmdDelete = new JButton(this.model.getMessage("delete"));
		this.sPOCsCmdDelete.addActionListener(this);
		sPOCsButtonsPanel.add(this.sPOCsCmdAdd);
		sPOCsButtonsPanel.add(this.sPOCsCmdDelete);

		this.sPOCsTableModel = new DefaultTableModel(new Object[0][0],
				pocColNames);
		this.sPOCsTable = new AutoSizingJTable(this.sPOCsTableModel);
		InstallerUtils.setUpCellRenderer(this.sPOCsTable);
		
		JScrollPane sPOCsScrollPane = new JScrollPane(this.sPOCsTable);
		sPOCsPanel.setLayout(new BorderLayout());
		sPOCsPanel.add(BorderLayout.NORTH, new JLabel(this.model
				.getMessage("service.pocs")));
		sPOCsPanel.add(BorderLayout.CENTER, sPOCsScrollPane);
		sPOCsPanel.add(BorderLayout.SOUTH, sPOCsButtonsPanel);

		// Research Center options
		JPanel rcOptionsPanel = new JPanel();
		rcOptionsPanel.setLayout(new GridBagLayout());
		rcPanel.add(rcOptionsPanel, InstallerUtils.getGridBagConstraints(0, 0));

		this.rcDisplayNameField = new JTextField();
		addRequiredListener(this.rcDisplayNameField);
		JLabel rcDisplayNameLabel = new JLabel("Display Name");
		addOption(rcOptionsPanel, rcDisplayNameLabel, this.rcDisplayNameField,
				0);

		this.rcShortNameField = new JTextField();
		addRequiredListener(this.rcShortNameField);
		JLabel rcShortNameLabel = new JLabel("Short Name");
		addOption(rcOptionsPanel, rcShortNameLabel, this.rcShortNameField, 1);

		this.rcCountryField = new JTextField();
		addRequiredListener(this.rcCountryField);
		JLabel rcCountryLabel = new JLabel("Country");
		addOption(rcOptionsPanel, rcCountryLabel, this.rcCountryField, 2);

		this.rcLocalityField = new JTextField();
		addRequiredListener(this.rcLocalityField);
		JLabel rcLocalityLabel = new JLabel("Locality");
		addOption(rcOptionsPanel, rcLocalityLabel, this.rcLocalityField, 3);

		this.rcPostalCodeField = new JTextField();
		addRequiredListener(this.rcPostalCodeField);
		JLabel rcPostalCodeLabel = new JLabel("Postal Code");
		addOption(rcOptionsPanel, rcPostalCodeLabel, this.rcPostalCodeField, 4);

		this.rcStateProvinceField = new JTextField();
		addRequiredListener(this.rcStateProvinceField);
		JLabel rcStateProvinceLabel = new JLabel("State/Province");
		addOption(rcOptionsPanel, rcStateProvinceLabel,
				this.rcStateProvinceField, 5);

		this.rcStreet1Field = new JTextField();
		addRequiredListener(this.rcStreet1Field);
		JLabel rcStreet1Label = new JLabel("Steet 1");
		addOption(rcOptionsPanel, rcStreet1Label, this.rcStreet1Field, 6);

		this.rcStreet2Field = new JTextField();
		addRequiredListener(this.rcStreet2Field);
		JLabel rcStreet2Label = new JLabel("Street 2");
		addOption(rcOptionsPanel, rcStreet2Label, this.rcStreet2Field, 7);

		this.rcDescriptionField = new JTextField();
		addRequiredListener(this.rcDescriptionField);
		JLabel rcDescriptionLabel = new JLabel("Description");
		addOption(rcOptionsPanel, rcDescriptionLabel, this.rcDescriptionField,
				8);

		this.rcHomepageURLField = new JTextField();
		addRequiredListener(this.rcHomepageURLField);
		JLabel rcHomepageURLLabel = new JLabel("Homepage URL");
		addOption(rcOptionsPanel, rcHomepageURLLabel, this.rcHomepageURLField,
				9);

		this.rcImageURLField = new JTextField();
		addRequiredListener(this.rcImageURLField);
		JLabel rcImageURLLabel = new JLabel("Image URL");
		addOption(rcOptionsPanel, rcImageURLLabel, this.rcImageURLField, 10);

		this.rcRSSNewsURLField = new JTextField();
		addRequiredListener(this.rcRSSNewsURLField);
		JLabel rcRSSNewsUrlLabel = new JLabel("RSS News URL");
		addOption(rcOptionsPanel, rcRSSNewsUrlLabel, this.rcRSSNewsURLField, 11);

		// Research Center POCs table
		JPanel rPOCsPanel = new JPanel();
		rPOCsPanel.setLayout(new BorderLayout());
		rPOCsPanel.setPreferredSize(new Dimension(500, 125));
		rcPanel.add(rPOCsPanel, InstallerUtils.getGridBagConstraints(0, 1));

		// Research Center POCs tabel buttons
		JPanel rPOCsButtonsPanel = new JPanel();
		this.rPOCsCmdAdd = new JButton(this.model.getMessage("add"));
		this.rPOCsCmdAdd.addActionListener(this);
		this.rPOCsCmdDelete = new JButton(this.model.getMessage("delete"));
		this.rPOCsCmdDelete.addActionListener(this);
		rPOCsButtonsPanel.add(this.rPOCsCmdAdd);
		rPOCsButtonsPanel.add(this.rPOCsCmdDelete);

		this.rPOCsTableModel = new DefaultTableModel(new Object[0][0],
				pocColNames);
		this.rPOCsTable = new AutoSizingJTable(this.rPOCsTableModel);
		InstallerUtils.setUpCellRenderer(this.rPOCsTable);
		
		JScrollPane rPOCsScrollPane = new JScrollPane(this.rPOCsTable);
		rPOCsPanel.setLayout(new BorderLayout());
		rPOCsPanel.add(BorderLayout.NORTH, new JLabel(this.model
				.getMessage("research.center.pocs")));
		rPOCsPanel.add(BorderLayout.CENTER, rPOCsScrollPane);
		rPOCsPanel.add(BorderLayout.SOUTH, rPOCsButtonsPanel);

	}

	private void addOption(JPanel panel, JLabel label, Component field, int y) {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = y;
		panel.add(label, gridBagConstraints1);

		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.gridy = y;
		gridBagConstraints2.weightx = 1;
		gridBagConstraints2.insets = new Insets(2, 5, 2, 2);
		panel.add(field, gridBagConstraints2);

	}

	private void addRequiredListener(JTextField field) {
		field.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent evt) {
				ConfigureServiceMetadataStep.this.checkComplete();
			}
		});
	}

	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (source == this.sPOCsCmdAdd) {
			this.sPOCsTableModel.addRow(new String[6]);
		} else if (source == this.sPOCsCmdDelete) {
			this.sPOCsTableModel.removeRow(this.sPOCsTable.getSelectedRow());
		} else if (source == this.rPOCsCmdAdd) {
			this.rPOCsTableModel.addRow(new String[6]);
		} else if (source == this.rPOCsCmdDelete) {
			this.rPOCsTableModel.removeRow(this.rPOCsTable.getSelectedRow());
		}
	}

	protected void checkComplete() {
		// boolean validSvcPOCs = checkValidPOCs(this.sPOCsTableModel);
		// boolean validRcPOCs = checkValidPOCs(this.rPOCsTableModel);
		// if (validSvcPOCs && validRcPOCs &&
		// && !isEmpty(this.rcDescriptionField)
		// && !isEmpty(this.rcHomepageURLField)) {
		if (!isEmpty(this.rcDisplayNameField)
				&& !isEmpty(this.rcShortNameField)
				&& !isEmpty(this.rcCountryField)
				&& !isEmpty(this.rcStreet1Field)) {
			setComplete(true);
		} else {
			setComplete(false);
		}
	}

	private boolean isEmpty(JTextField field) {
		return InstallerUtils.isEmpty(field.getText());
	}

	private boolean checkValidPOCs(DefaultTableModel tableModel) {
		boolean allValid = true;
		int rowLen = tableModel.getRowCount();
		for (int i = 0; i < rowLen && allValid; i++) {

			// Ignore empty rows
			boolean someValueGiven = false;
			for (int j = 0; j < 6 && !someValueGiven; j++) {
				someValueGiven = !InstallerUtils.isEmpty((String) tableModel
						.getValueAt(i, j));
			}
			if (someValueGiven) {

				allValid = !InstallerUtils.isEmpty((String) tableModel
						.getValueAt(i, AFFILIATION_COL))
						&& !InstallerUtils.isEmpty((String) tableModel
								.getValueAt(i, EMAIL_COL))
						&& !InstallerUtils.isEmpty((String) tableModel
								.getValueAt(i, FIRST_NAME_COL))
						&& !InstallerUtils.isEmpty((String) tableModel
								.getValueAt(i, LAST_NAME_COL))
						&& !InstallerUtils.isEmpty((String) tableModel
								.getValueAt(i, ROLE_COL));

			}
		}
		return allValid;
	}

	public void prepare() {
		try {

			File svcMetaFile = new File(getServiceMetadataPath());
			if (!svcMetaFile.exists()) {
				throw new Exception(svcMetaFile.getAbsolutePath()
						+ " not found");
			}

			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			fact.setValidating(false);
			fact.setNamespaceAware(true);
			DocumentBuilder builder = fact.newDocumentBuilder();
			Document doc = builder.parse(svcMetaFile);
			Element root = doc.getDocumentElement();

			// Get the service POCs
			Element svcPOCColl = getSvcPOCColl(root);

			if (svcPOCColl == null) {
				throw new Exception("No service POC collection element found");
			}
			addPOCRows(svcPOCColl, this.sPOCsTableModel);

			// Get the research center info

			Element rcEl = getResearchCenterEl(root);

			if (rcEl != null) {

				this.rcDisplayNameField.setText(rcEl
						.getAttribute("displayName"));
				this.rcShortNameField.setText(rcEl.getAttribute("shortName"));

				Element rcAddrEl = (Element) xpFact.newXPath().compile(
						"./*[local-name()='Address']").evaluate(rcEl,
						XPathConstants.NODE);
				if (rcAddrEl != null) {
					this.rcCountryField.setText(rcAddrEl
							.getAttribute("country"));
					this.rcPostalCodeField.setText(rcAddrEl
							.getAttribute("postalCode"));
					this.rcStateProvinceField.setText(rcAddrEl
							.getAttribute("stateProvince"));
					this.rcStreet1Field.setText(rcAddrEl
							.getAttribute("street1"));
					this.rcStreet2Field.setText(rcAddrEl
							.getAttribute("street2"));
				}

				Element rcDescEl = (Element) xpFact.newXPath().compile(
						"./*[local-name()='ResearchCenterDescription']")
						.evaluate(rcEl, XPathConstants.NODE);
				if (rcDescEl != null) {
					this.rcDescriptionField.setText(rcDescEl
							.getAttribute("description"));
					this.rcHomepageURLField.setText(rcDescEl
							.getAttribute("homepageURL"));
					this.rcImageURLField.setText(rcDescEl
							.getAttribute("imageURL"));
					this.rcRSSNewsURLField.setText(rcDescEl
							.getAttribute("rssNewsURL"));
				}

				// Get the research center POCs
				Element rcPOCColl = (Element) xpFact.newXPath().compile(
						"./*[local-name()='pointOfContactCollection']")
						.evaluate(rcEl, XPathConstants.NODE);
				if (rcPOCColl == null) {
					throw new Exception(
							"No research center POC collection element found");
				}
				addPOCRows(rcPOCColl, this.rPOCsTableModel);
			}

		} catch (Exception ex) {
			String msg = "Error preparing service metadata editor: "
					+ ex.getMessage();
			logger.error(msg, ex);
			JOptionPane.showMessageDialog(null, msg, this.model
					.getMessage("error"), JOptionPane.ERROR_MESSAGE);
		}
	}

	private Element getResearchCenterEl(Element root) throws Exception {
		return (Element) xpFact
				.newXPath()
				.compile(
						"/*[local-name()='ServiceMetadata']/*[local-name()='hostingResearchCenter']/*[local-name()='ResearchCenter']")
				.evaluate(root, XPathConstants.NODE);

	}

	private Element getSvcPOCColl(Element root) throws Exception {
		return (Element) xpFact
				.newXPath()
				.compile(
						"/*[local-name()='ServiceMetadata']/*[local-name()='serviceDescription']/*[local-name()='Service']/*[local-name()='pointOfContactCollection']")
				.evaluate(root, XPathConstants.NODE);
	}

	private void addPOCRows(Element pocCollection, DefaultTableModel tableModel) {

		while (tableModel.getRowCount() > 0) {
			tableModel.removeRow(0);
		}

		NodeList pocs = pocCollection.getElementsByTagNameNS(COMMON_NS,
				"PointOfContact");
		for (int i = 0; i < pocs.getLength(); i++) {
			Element pocEl = (Element) pocs.item(i);
			String[] row = new String[] { pocEl.getAttribute("affiliation"),
					pocEl.getAttribute("email"),
					pocEl.getAttribute("firstName"),
					pocEl.getAttribute("lastName"),
					pocEl.getAttribute("phoneNumber"),
					pocEl.getAttribute("role") };
			tableModel.addRow(row);
		}
	}

	public void applyState() throws InvalidStateException {
		try {

			File svcMetaFile = new File(getServiceMetadataPath());
			if (!svcMetaFile.exists()) {
				throw new Exception(svcMetaFile.getAbsolutePath()
						+ " not found");
			}

			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			fact.setValidating(false);
			fact.setNamespaceAware(true);
			DocumentBuilder builder = fact.newDocumentBuilder();
			Document doc = builder.parse(svcMetaFile);
			Element root = doc.getDocumentElement();

			NSMappings mappings = new NSMappings(svcMetaFile);
			String commNSPrefix = mappings.lookupPrefix(COMMON_NS);
			String svcNSPrefix = mappings.lookupPrefix(SERVICE_NS);
			String metaNSPrefix = mappings.lookupPrefix(METADATA_NS);

			Element oldSvcPOCColl = getSvcPOCColl(root);
			if (oldSvcPOCColl == null) {
				throw new Exception("service POC collection not found");
			}
			Element svcPOCColl = doc.createElementNS(SERVICE_NS, svcNSPrefix
					+ ":pointOfContactCollection");
			Element svcEl = (Element) oldSvcPOCColl.getParentNode();
			svcEl.replaceChild(svcPOCColl, oldSvcPOCColl);

			addPOCEls(doc, svcPOCColl, this.sPOCsTableModel, commNSPrefix);

			Element svcMetaEl = (Element) xpFact.newXPath().compile(
					"/*[local-name()='ServiceMetadata']").evaluate(root,
					XPathConstants.NODE);
			Element hostingRCEl = doc.createElementNS(METADATA_NS, metaNSPrefix
					+ ":hostingResearchCenter");
			Element oldHostingRCEl = (Element) xpFact.newXPath().compile(
					"./*[local-name()='hostingResearchCenter']").evaluate(
					svcMetaEl, XPathConstants.NODE);
			if (oldHostingRCEl != null) {
				svcMetaEl.replaceChild(hostingRCEl, oldHostingRCEl);
			} else {
				svcMetaEl.appendChild(hostingRCEl);
			}

			Element rcEl = doc.createElementNS(COMMON_NS, commNSPrefix
					+ ":ResearchCenter");
			hostingRCEl.appendChild(rcEl);
			rcEl.setAttribute("displayName", this.rcDisplayNameField.getText());
			rcEl.setAttribute("shortName", this.rcShortNameField.getText());

			// Address
			if (!isEmpty(this.rcCountryField) && !isEmpty(this.rcStreet1Field)) {
				List<Attr> atts = new ArrayList<Attr>();
				addAttr(doc, atts, "country", this.rcCountryField);
				addAttr(doc, atts, "locality", this.rcLocalityField);
				addAttr(doc, atts, "postalCode", this.rcPostalCodeField);
				addAttr(doc, atts, "stateProvince", this.rcStateProvinceField);
				addAttr(doc, atts, "street1", this.rcStreet1Field);
				addAttr(doc, atts, "street2", this.rcStreet2Field);
				if (atts.size() > 0) {
					Element addrEl = doc.createElementNS(COMMON_NS,
							commNSPrefix + ":Address");
					rcEl.appendChild(addrEl);
					for (Attr att : atts) {
						addrEl.setAttributeNode(att);
					}
				}
			}

			// Description
			List<Attr> atts = new ArrayList<Attr>();
			addAttr(doc, atts, "description", this.rcDescriptionField);
			addAttr(doc, atts, "homepageURL", this.rcHomepageURLField);
			addAttr(doc, atts, "imageURL", this.rcImageURLField);
			addAttr(doc, atts, "rssNewsURL", this.rcRSSNewsURLField);
			if (atts.size() > 0) {
				Element descEl = doc.createElementNS(COMMON_NS, commNSPrefix
						+ ":ResearchCenterDescription");
				rcEl.appendChild(descEl);
				for (Attr att : atts) {
					descEl.setAttributeNode(att);
				}
			}

			// POCs
			Element rcPOCCollEl = doc.createElementNS(COMMON_NS, commNSPrefix
					+ ":pointOfContactCollection");
			rcEl.appendChild(rcPOCCollEl);
			addPOCEls(doc, rcPOCCollEl, this.rPOCsTableModel, commNSPrefix);

			String xml = InstallerUtils.toString(doc);
			FileWriter w = new FileWriter(svcMetaFile);
			w.write(xml);
			w.flush();
			w.close();

		} catch (Exception ex) {
			String msg = "Error modifying service metadata: " + ex.getMessage();
			logger.error(msg, ex);
			throw new InvalidStateException(msg, ex);
		}
	}

	private boolean addPOCEls(Document doc, Element collEl,
			DefaultTableModel tableModel, String prefix) {
		boolean addedOne = false;
		int rowCount = tableModel.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			List<Attr> atts = new ArrayList<Attr>();
			addAttr(doc, atts, "affiliation", i, AFFILIATION_COL, tableModel);
			addAttr(doc, atts, "email", i, EMAIL_COL, tableModel);
			addAttr(doc, atts, "firstName", i, FIRST_NAME_COL, tableModel);
			addAttr(doc, atts, "lastName", i, LAST_NAME_COL, tableModel);
			addAttr(doc, atts, "phoneNumber", i, PHONE_NUMBER_COL, tableModel);
			addAttr(doc, atts, "role", i, ROLE_COL, tableModel);
			if (atts.size() > 0) {
				addedOne = true;
				Element pocEl = doc.createElementNS(COMMON_NS, prefix
						+ ":PointOfContact");
				collEl.appendChild(pocEl);
				for (Attr att : atts) {
					pocEl.setAttributeNode(att);
				}
			}
		}
		return addedOne;
	}

	private void addAttr(Document doc, List<Attr> atts, String attName,
			int row, int col, DefaultTableModel tableModel) {
		addAttr(doc, atts, attName, (String) tableModel.getValueAt(row, col));

	}

	private void addAttr(Document doc, List<Attr> atts, String attName,
			JTextField field) {
		addAttr(doc, atts, attName, field.getText());
	}

	private void addAttr(Document doc, List<Attr> atts, String attName,
			String value) {
		if (!InstallerUtils.isEmpty(value)) {
			Attr attr = doc.createAttribute(attName);
			attr.setValue(value);
			atts.add(attr);
		}
	}

	protected String getServiceMetadataPath() {
		return this.model.getProperty(this.serviceDirProp)
				+ "/etc/serviceMetadata.xml";
	}

	private class NSMappings extends DefaultHandler2 {

		private Map<String, String> mappings = null;

		private File file = null;

		NSMappings(File file) {
			this.file = file;
		}

		private void init() {
			mappings = new HashMap<String, String>();
			try {
				SAXParserFactory fact = SAXParserFactory.newInstance();
				SAXParser p = fact.newSAXParser();
				XMLReader r = p.getXMLReader();
				r.setFeature("http://xml.org/sax/features/namespaces", true);
				r.setFeature("http://xml.org/sax/features/namespace-prefixes",
						true);
				p.parse(file, this);
			} catch (Exception ex) {
				throw new RuntimeException("Error parsing '"
						+ file.getAbsolutePath() + "': " + ex.getMessage(), ex);
			}
		}

		public String lookupPrefix(String uri) {
			if (mappings == null) {
				init();
			}
			return mappings.get(uri);
		}

		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {

			if (!mappings.containsKey(uri)) {
				mappings.put(uri, prefix);
			}
		}
	}

}
