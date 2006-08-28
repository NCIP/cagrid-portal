package gov.nih.nci.cagrid.introduce.portal.discoverytools.gme;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.projectmobius.protocol.gme.SchemaNode;


public class GMESchemaLocatorPanel extends JPanel {

	public static String GME_URL = "GME_URL";

	public static String TYPE = "gme_discovery";

	private JPanel mainPanel = null;

	private JPanel queryPanel = null;

	private JButton queryButton = null;

	protected File schemaDir;

	private JComboBox namespaceComboBox = null;

	private JComboBox schemaComboBox = null;

	private JPanel schemaPanel = null;

	private JLabel namespaceLabel = null;

	private JTextField gme = null;

	JLabel gmeAddressLabel = null;

	JLabel nameLabel = null;

	public SchemaNode currentNode = null;

	public String url;

	private boolean showGMESelection;


	/**
	 * This method initializes
	 */
	public GMESchemaLocatorPanel(String url, boolean showGMESelection) {
		super();
		this.url = url;
		this.showGMESelection = showGMESelection;
		initialize();

	}


	/**
	 * This method initializes this
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
			JPanel qPanel = getQueryPanel();
			if (this.showGMESelection) {
				mainPanel.add(qPanel, gridBagConstraints1);
			}
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
			queryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Discover Schemas",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			queryPanel.add(getQueryButton(), gridBagConstraints4);
			queryPanel.add(gmeAddressLabel, gridBagConstraints6);
			queryPanel.add(getGme(), gridBagConstraints5);
		}
		return queryPanel;
	}


	public void discoverFromGME() {
		GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
		makeCombosEnabled(false);

		Thread t = new Thread() {
			public void run() {
				try {
					XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance()
						.getGridService(gme.getText());
					List namespaces = handle.getNamespaceDomainList();

					getNamespaceComboBox().removeAllItems();
					for (int i = 0; i < namespaces.size(); i++) {
						getNamespaceComboBox().addItem(namespaces.get(i));
					}
					makeCombosEnabled(true);
				} catch (Throwable e1) {
					e1.printStackTrace();
					 JOptionPane.showMessageDialog(GMESchemaLocatorPanel.this,
					 	"Please check the GME URL and make sure that you have the appropriate credentials!");
					//ErrorDialog.showErrorDialog("Please check the GME URL and make sure that you have the appropriate credentials!");
				}

			}
		};
		t.start();
	}


	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getQueryButton() {
		if (queryButton == null) {
			queryButton = new JButton("Refresh from GME", IntroduceLookAndFeel.getMobiusIcon());
			queryButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					discoverFromGME();
				}
			});
		}
		return queryButton;
	}


	/**
	 * This method initializes jComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	public JComboBox getNamespaceComboBox() {
		if (namespaceComboBox == null) {
			namespaceComboBox = new JComboBox();
			namespaceComboBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						if ((String) namespaceComboBox.getSelectedItem() != null
							&& ((String) namespaceComboBox.getSelectedItem()).length() > 0) {
							GridServiceResolver.getInstance().setDefaultFactory(
								new GlobusGMEXMLDataModelServiceFactory());
							Thread t = new Thread() {
								public void run() {
									try {
										makeCombosEnabled(false);
										XMLDataModelService handle = (XMLDataModelService) GridServiceResolver
											.getInstance().getGridService(gme.getText());
										List schemas = handle
											.getSchemaListForNamespaceDomain((String) namespaceComboBox
												.getSelectedItem());

										getSchemaComboBox().removeAllItems();
										for (int i = 0; i < schemas.size(); i++) {
											Namespace schemaNS = (Namespace) schemas.get(i);
											getSchemaComboBox().addItem(new SchemaWrapper(schemaNS));
										}
										makeCombosEnabled(true);
									} catch (MobiusException e1) {
										e1.printStackTrace();
										JOptionPane
											.showMessageDialog(GMESchemaLocatorPanel.this,
												"Please check the GME URL and make sure that you have the appropriate credentials!");
									}
								}
							};
							t.start();
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
						GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
						try {
							XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance()
								.getGridService(gme.getText());
							if (schemaComboBox.getSelectedItem() != null) {
								currentNode = handle.getSchema(((SchemaWrapper) schemaComboBox.getSelectedItem())
									.getNamespace(), false);
							}
						} catch (MobiusException e1) {
							e1.printStackTrace();
							// JOptionPane.showMessageDialog(GMESchemaLocatorPanel.this,
							// 	"Please check the GME URL and make sure that you have the appropriate credentials!");
							ErrorDialog.showErrorDialog("Please check the GME URL and make sure that you have the appropriate credentials!");
						}
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
			schemaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Schema",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			schemaPanel.add(getNamespaceComboBox(), gridBagConstraints7);
			schemaPanel.add(namespaceLabel, gridBagConstraints9);
			schemaPanel.add(getSchemaComboBox(), gridBagConstraints8);
			schemaPanel.add(nameLabel, gridBagConstraints10);
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
			gme.setText(url);
		}
		return gme;
	}


	private synchronized void makeCombosEnabled(boolean enabled) {
		getNamespaceComboBox().setEnabled(enabled);
		getSchemaComboBox().setEnabled(enabled);
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


	public static void main(String[] args) {
		GMESchemaLocatorPanel panel = new GMESchemaLocatorPanel(
			"http://localhost:8080/wsrf/services/cagrid/GlobalModelExchange", true);
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}


	public Namespace getSelectedSchemaNamespace() {
		SchemaWrapper schema = (SchemaWrapper) getSchemaComboBox().getSelectedItem();
		if (schema != null) {
			return schema.getNamespace();
		}
		return null;
	}

}
