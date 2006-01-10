package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MalformedNamespaceException;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.protocol.gme.SchemaNode;

public class GMEConfigurationPanel extends JPanel {

	private JPanel mainPanel = null;

	private JPanel queryPanel = null;

	private JButton queryButton = null;

	private JComboBox typesComboBox = null;

	public Namespace currentNamespace = null;

	public String currentType = null;

	protected File schemaDir;

	private JComboBox namespaceComboBox = null;

	private JComboBox schemaComboBox = null;

	protected JComponent me;

	private JPanel schemaPanel = null;

	private JLabel namespaceLabel = null;

	private JTextField gme = null;

	JLabel gmeAddressLabel = null;

	JLabel nameLabel = null;

	private JLabel elementTypeLabel = null;
	
	public static final String ELEMENT_ONLY = "elements";
	
	public static final String TYPES_ONLY = "types";
	
	public String filterType = null;
	
	


	/**
	 * This method initializes 
	 * 
	 */
	public GMEConfigurationPanel(String filterType) {
		super();
		this.filterType = filterType;
		initialize();
		
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.add(getMainPanel(), null);
			
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	public JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.weightx = 0.0D;
			gridBagConstraints1.weighty = 0.0D;
			mainPanel.add(getQueryPanel(), gridBagConstraints1);
			mainPanel.add(getSchemaPanel(), gridBagConstraints);
		}
		return mainPanel;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	public JPanel getQueryPanel() {
		if (queryPanel == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.gridx = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.weightx = 1.0;
			gmeAddressLabel = new JLabel();
			gmeAddressLabel.setText("GME");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.CENTER;
			gridBagConstraints4.gridwidth = 2;
			gridBagConstraints4.gridx = 0;
			queryPanel = new JPanel();
			queryPanel.setLayout(new GridBagLayout());
			queryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, "Discover Schemas",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					IntroduceLookAndFeel.getPanelLabelColor()));
			queryPanel.add(getQueryButton(), gridBagConstraints4);
			queryPanel.add(gmeAddressLabel, gridBagConstraints6);
			queryPanel.add(getGme(), gridBagConstraints5);
		}
		return queryPanel;
	}


	public void discoverFromGME() {
		GridServiceResolver.getInstance().setDefaultFactory(
				new GlobusGMEXMLDataModelServiceFactory());
		List namespaces = null;
		try {
			XMLDataModelService handle = (XMLDataModelService) GridServiceResolver
					.getInstance().getGridService(gme.getText());
			namespaces = handle.getNamespaceDomainList();

			getNamespaceComboBox().removeAllItems();
			for (int i = 0; i < namespaces.size(); i++) {
				getNamespaceComboBox().addItem(namespaces.get(i));
			}

		} catch (MobiusException e1) {
			e1.printStackTrace();
			JOptionPane
					.showMessageDialog(
							me,
							"Please check the GME URL and make sure that you have the appropriate credentials!");
		}
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getQueryButton() {
		if (queryButton == null) {
			queryButton = new JButton("Refresh from GME", IntroduceLookAndFeel
					.getMobiusIcon());
			queryButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					discoverFromGME();
				}
			});
		}
		return queryButton;
	}

	public void initializeTypes(SchemaNode node) {
		try {
			if(this.filterType.equals(GMEConfigurationPanel.ELEMENT_ONLY)){
				Document doc = XMLUtilities.stringToDocument(node
						.getSchemaContents());
				List elementTypes = doc.getRootElement().getChildren("element",
						doc.getRootElement().getNamespace());
				JComboBox typesBox = this.getTypesComboBox();
				typesBox.removeAllItems();
				for (int i = 0; i < elementTypes.size(); i++) {
					Element element = (Element) elementTypes.get(i);
					String name = element.getAttributeValue("name");
					typesBox.addItem(name);
				}
			} else if(this.filterType.equals(GMEConfigurationPanel.TYPES_ONLY)){
				Document doc = XMLUtilities.stringToDocument(node
						.getSchemaContents());
				List complexTypes = doc.getRootElement().getChildren("complexType",
						doc.getRootElement().getNamespace());
				List simpleTypes = doc.getRootElement().getChildren("simpleType",
						doc.getRootElement().getNamespace());
				JComboBox typesBox = this.getTypesComboBox();
				typesBox.removeAllItems();
				for (int i = 0; i < complexTypes.size(); i++) {
					Element element = (Element) complexTypes.get(i);
					String name = element.getAttributeValue("name");
					typesBox.addItem(name);
				}
				for (int i = 0; i < simpleTypes.size(); i++) {
					Element element = (Element) simpleTypes.get(i);
					String name = element.getAttributeValue("name");
					typesBox.addItem(name);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method initializes jComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	public JComboBox getTypesComboBox() {
		if (typesComboBox == null) {
			typesComboBox = new JComboBox();
			typesComboBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					currentType = (String) typesComboBox.getSelectedItem();
					if (currentType != null) {

						currentNamespace = null;
						try {
							currentNamespace = new Namespace(
									(String) getNamespaceComboBox()
											.getSelectedItem()
											+ (String) getSchemaComboBox()
													.getSelectedItem());
						} catch (MalformedNamespaceException e1) {
							e1.printStackTrace();
						}
					}

				}
			});
		}
		return typesComboBox;
	}

	/**
	 * This method initializes jComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	public JComboBox getNamespaceComboBox() {
		if (namespaceComboBox == null) {
			namespaceComboBox = new JComboBox();
			namespaceComboBox
					.addItemListener(new java.awt.event.ItemListener() {
						public void itemStateChanged(java.awt.event.ItemEvent e) {
							GridServiceResolver
									.getInstance()
									.setDefaultFactory(
											new GlobusGMEXMLDataModelServiceFactory());
							try {

								XMLDataModelService handle = (XMLDataModelService) GridServiceResolver
										.getInstance().getGridService(
												gme.getText());
								List schemas = handle
										.getSchemaListForNamespaceDomain((String) namespaceComboBox
												.getSelectedItem());

								getSchemaComboBox().removeAllItems();
								for (int i = 0; i < schemas.size(); i++) {
									Namespace schemaNS = (Namespace) schemas
											.get(i);
									getSchemaComboBox().addItem(
											"/" + schemaNS.getVersion() + "/"
													+ schemaNS.getName());
								}
							} catch (MobiusException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								JOptionPane
										.showMessageDialog(
												me,
												"Please check the GME URL and make sure that you have the appropriate credentials!");
							}
						}
					});
		}
		return namespaceComboBox;
	}

	/**
	 * This method initializes jComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	public JComboBox getSchemaComboBox() {
		if (schemaComboBox == null) {
			schemaComboBox = new JComboBox();
			schemaComboBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					GridServiceResolver.getInstance().setDefaultFactory(
							new GlobusGMEXMLDataModelServiceFactory());
					try {
						XMLDataModelService handle = (XMLDataModelService) GridServiceResolver
								.getInstance().getGridService(gme.getText());
						if (schemaComboBox.getSelectedItem() != null) {
							SchemaNode node = handle.getSchema(new Namespace(
									(String) namespaceComboBox
											.getSelectedItem()
											+ (String) schemaComboBox
													.getSelectedItem()), false);
							initializeTypes(node);
						}
					} catch (MobiusException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						JOptionPane
								.showMessageDialog(
										me,
										"Please check the GME URL and make sure that you have the appropriate credentials!");
					}
				}
			});
		}
		return schemaComboBox;
	}

	/**
	 * This method initializes schemaPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	public JPanel getSchemaPanel() {
		if (schemaPanel == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridy = 2;
			elementTypeLabel = new JLabel();
			elementTypeLabel.setText("Element Type");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.gridy = 2;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.weighty = 1.0D;
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
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
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints7.weighty = 1.0D;
			gridBagConstraints7.gridx = 1;
			schemaPanel = new JPanel();
			schemaPanel.setLayout(new GridBagLayout());
			schemaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, "Select Schema",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					IntroduceLookAndFeel.getPanelLabelColor()));
			schemaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Type", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), new java.awt.Color(62,109,181)));
			schemaPanel.add(getNamespaceComboBox(), gridBagConstraints7);
			schemaPanel.add(namespaceLabel, gridBagConstraints9);
			schemaPanel.add(getSchemaComboBox(), gridBagConstraints8);
			schemaPanel.add(nameLabel, gridBagConstraints10);
			schemaPanel.add(getTypesComboBox(), gridBagConstraints12);
			schemaPanel.add(elementTypeLabel, gridBagConstraints13);
		}
		return schemaPanel;
	}

	/**
	 * This method initializes gme
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getGme() {
		if (gme == null) {
			gme = new JTextField();
			gme
					.setText("http://dc01.bmi.ohio-state.edu:8080/wsrf/services/cagrid/GlobalModelExchange");
		}
		return gme;
	}


}
