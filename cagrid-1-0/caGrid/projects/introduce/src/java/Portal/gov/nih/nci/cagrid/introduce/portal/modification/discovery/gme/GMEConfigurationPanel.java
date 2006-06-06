package gov.nih.nci.cagrid.introduce.portal.modification.discovery.gme;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.projectmobius.protocol.gme.SchemaNode;

public class GMEConfigurationPanel extends JPanel {

	public Namespace currentNamespace = null;

	public SchemaNode currentNode = null;

	protected File schemaDir;

	private JComboBox namespaceComboBox = null;

	private JComboBox schemaComboBox = null;

	private JLabel namespaceLabel = null;

	JLabel nameLabel = null;

	public String filterType = null;

	public String url;

	/**
	 * This method initializes
	 */
	public GMEConfigurationPanel(String url) {
		super();
		this.url = url;
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
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
		this.setLayout(new GridBagLayout());
		this.add(getNamespaceComboBox(), gridBagConstraints7);
		this.add(namespaceLabel, gridBagConstraints9);
		this.add(getSchemaComboBox(), gridBagConstraints8);
		this.add(nameLabel, gridBagConstraints10);
	}

	public void discoverFromGME() {

		GridServiceResolver.getInstance().setDefaultFactory(
				new GlobusGMEXMLDataModelServiceFactory());
		List namespaces = null;
		try {
			XMLDataModelService handle = (XMLDataModelService) GridServiceResolver
					.getInstance().getGridService(url);
			namespaces = handle.getNamespaceDomainList();

			getNamespaceComboBox().removeAllItems();
			for (int i = 0; i < namespaces.size(); i++) {
				getNamespaceComboBox().addItem(namespaces.get(i));
			}

		} catch (MobiusException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(GMEConfigurationPanel.this,
					"Unable to connect to the GME");
		}

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
							if (e.getStateChange() == ItemEvent.SELECTED) {
								GridServiceResolver
										.getInstance()
										.setDefaultFactory(
												new GlobusGMEXMLDataModelServiceFactory());
								try {
									if ((String) namespaceComboBox
											.getSelectedItem() != null
											&& ((String) namespaceComboBox
													.getSelectedItem())
													.length() > 0) {
										XMLDataModelService handle = (XMLDataModelService) GridServiceResolver
												.getInstance().getGridService(
														url);
										List schemas = handle
												.getSchemaListForNamespaceDomain((String) namespaceComboBox
														.getSelectedItem());

										getSchemaComboBox().removeAllItems();
										for (int i = 0; i < schemas.size(); i++) {
											Namespace schemaNS = (Namespace) schemas
													.get(i);
											getSchemaComboBox()
													.addItem(
															new SchemaWrapper(
																	schemaNS));
										}
									}
								} catch (MobiusException e1) {
									e1.printStackTrace();
									JOptionPane
											.showMessageDialog(
													GMEConfigurationPanel.this,
													"Please check the GME URL and make sure that you have the appropriate credentials!");
								}
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
					if (e.getStateChange() == ItemEvent.SELECTED) {
						GridServiceResolver.getInstance().setDefaultFactory(
								new GlobusGMEXMLDataModelServiceFactory());
						if (getSchemaComboBox().getSelectedItem() != null) {
							currentNamespace = ((SchemaWrapper) getSchemaComboBox()
									.getSelectedItem()).getNamespace();
							try {
								XMLDataModelService handle = (XMLDataModelService) GridServiceResolver
										.getInstance().getGridService(url);
								currentNode = handle.getSchema(
										currentNamespace, false);
							} catch (MobiusException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			});
		}
		return schemaComboBox;
	}

	class SchemaWrapper {
		Namespace ns;

		public Namespace getNamespace() {
			return ns;
		}

		public SchemaWrapper(Namespace ns) {
			this.ns = ns;
		}

		public String toString() {
			return ns.getName();
		}
	}

} // @jve:decl-index=0:visual-constraint="10,10"
