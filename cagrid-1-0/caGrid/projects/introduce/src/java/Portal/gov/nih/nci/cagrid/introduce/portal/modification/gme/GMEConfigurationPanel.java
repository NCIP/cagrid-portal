package gov.nih.nci.cagrid.introduce.portal.modification.gme;

import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.IntroducePortalConf;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.projectmobius.portal.PortalResourceManager;
import org.projectmobius.protocol.gme.SchemaNode;


public class GMEConfigurationPanel extends JPanel {

	private JPanel mainPanel = null;

	public Namespace currentNamespace = null;
	
	public SchemaNode currentNode = null;

	protected File schemaDir;

	private JComboBox namespaceComboBox = null;

	private JComboBox schemaComboBox = null;

	protected JComponent me;

	private JPanel schemaPanel = null;

	private JLabel namespaceLabel = null;

	JLabel nameLabel = null;

	public String filterType = null;


	/**
	 * This method initializes
	 */
	public GMEConfigurationPanel() {
		super();
		initialize();

	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(new java.awt.Dimension(128, 92));
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
			gridBagConstraints.gridx = 1;
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridy = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getSchemaPanel(), gridBagConstraints);
		}
		return mainPanel;
	}


	public void discoverFromGME() {
		GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
		List namespaces = null;
		try {
			IntroducePortalConf conf = (IntroducePortalConf) PortalResourceManager.getInstance().getResource(
				IntroducePortalConf.RESOURCE);
			XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance().getGridService(
				conf.getGME());
			namespaces = handle.getNamespaceDomainList();

			getNamespaceComboBox().removeAllItems();
			for (int i = 0; i < namespaces.size(); i++) {
				getNamespaceComboBox().addItem(namespaces.get(i));
			}

		} catch (MobiusException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(me,
				"Please check the GME URL and make sure that you have the appropriate credentials!");
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
			namespaceComboBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
					try {
						if ((String) namespaceComboBox.getSelectedItem() != null
							&& ((String) namespaceComboBox.getSelectedItem()).length() > 0) {
							IntroducePortalConf conf = (IntroducePortalConf) PortalResourceManager.getInstance()
								.getResource(IntroducePortalConf.RESOURCE);
							XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance()
								.getGridService(conf.getGME());
							List schemas = handle.getSchemaListForNamespaceDomain((String) namespaceComboBox
								.getSelectedItem());

							getSchemaComboBox().removeAllItems();
							for (int i = 0; i < schemas.size(); i++) {
								Namespace schemaNS = (Namespace) schemas.get(i);
								getSchemaComboBox().addItem(new SchemaWrapper(schemaNS));
							}
						}
					} catch (MobiusException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(me,
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
					GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
					if (getSchemaComboBox().getSelectedItem() != null) {
						currentNamespace = ((SchemaWrapper) getSchemaComboBox().getSelectedItem()).getNamespace();
						IntroducePortalConf conf = (IntroducePortalConf) PortalResourceManager.getInstance()
						.getResource(IntroducePortalConf.RESOURCE);
						try {
							XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance().getGridService(conf.getGME());
							currentNode = handle.getSchema(currentNamespace,false);
						} catch (MobiusException e1) {
							e1.printStackTrace();
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
			schemaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Type",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
				IntroduceLookAndFeel.getPanelLabelColor()));
			schemaPanel.add(getNamespaceComboBox(), gridBagConstraints7);
			schemaPanel.add(namespaceLabel, gridBagConstraints9);
			schemaPanel.add(getSchemaComboBox(), gridBagConstraints8);
			schemaPanel.add(nameLabel, gridBagConstraints10);
		}
		return schemaPanel;
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
