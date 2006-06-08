package gov.nih.nci.cagrid.introduce.portal.preferences;

import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.modification.services.ServiceTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.ServicesJTree;
import gov.nih.nci.cagrid.introduce.portal.modification.services.ServicesTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.methods.MethodTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.methods.MethodsTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties.ResourcePropertiesTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties.ResourcePropertyTypeTreeNode;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.CardLayout;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;
import javax.swing.tree.TreePath;

import sun.security.krb5.internal.r;

public class PreferencesDialog extends JDialog {

	private JPanel mainPanel = null;

	private JPanel preferenceNavigationPanel = null;

	private JPanel preferenceConfigurationPanel = null;

	private JScrollPane prefencesScrollPane = null;

	private PreferencesJTree preferencesTree = null;

	private JPanel buttonPanel = null;

	private JPanel preferneceConfigViewPanel = null;

	private JButton okButton = null;

	/**
	 * This method initializes
	 * 
	 */
	public PreferencesDialog() {
		super();
		setModal(true);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setSize(new java.awt.Dimension(347, 290));
		this.setContentPane(getMainPanel());
		this.setTitle("Preferences");

	}

	/**
	 * This method initializes mainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.weightx = 2.0D;
			gridBagConstraints1.weighty = 1.0D;
			gridBagConstraints1.gridwidth = 2;
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridy = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getPreferenceNavigationPanel(), gridBagConstraints);
			mainPanel.add(getPreferenceConfigurationPanel(),
					gridBagConstraints1);
		}
		return mainPanel;
	}

	/**
	 * This method initializes preferenceNavigationPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPreferenceNavigationPanel() {
		if (preferenceNavigationPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.gridx = 0;
			preferenceNavigationPanel = new JPanel();
			preferenceNavigationPanel.setLayout(new GridBagLayout());
			preferenceNavigationPanel
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Preferences",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									null, IntroduceLookAndFeel
											.getPanelLabelColor()));
			preferenceNavigationPanel.add(getPrefencesScrollPane(),
					gridBagConstraints2);
		}
		return preferenceNavigationPanel;
	}

	/**
	 * This method initializes preferenceConfigurationPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPreferenceConfigurationPanel() {
		if (preferenceConfigurationPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.weighty = 1.0D;
			gridBagConstraints4.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.ipadx = 0;
			gridBagConstraints3.ipady = 0;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.weighty = 0.0D;
			gridBagConstraints3.gridy = 1;
			preferenceConfigurationPanel = new JPanel();
			preferenceConfigurationPanel.setLayout(new GridBagLayout());
			preferenceConfigurationPanel.add(getButtonPanel(),
					gridBagConstraints3);
			preferenceConfigurationPanel.add(getPreferneceConfigViewPanel(),
					gridBagConstraints4);
		}
		return preferenceConfigurationPanel;
	}

	/**
	 * This method initializes prefencesScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getPrefencesScrollPane() {
		if (prefencesScrollPane == null) {
			prefencesScrollPane = new JScrollPane();
			prefencesScrollPane.setViewportView(getPreferencesTree());
		}
		return prefencesScrollPane;
	}

	private void addTreePanels(PreferencesTypeTreeNode node) {
		// Add node to list
		((CardLayout) getPreferneceConfigViewPanel().getLayout())
				.addLayoutComponent(node.getConfigurationPanel(), node
						.getName());
		// Create paths for all children
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				PreferencesTypeTreeNode n = (PreferencesTypeTreeNode) e
						.nextElement();
				addTreePanels(n);
			}
		}

	}

	/**
	 * This method initializes preferencesTree
	 * 
	 * @return javax.swing.JTree
	 */
	private PreferencesJTree getPreferencesTree() {
		if (preferencesTree == null) {
			preferencesTree = new PreferencesJTree();
			preferencesTree.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					preferencesTree.setSelectionRow(preferencesTree
							.getRowForLocation(e.getX(), e.getY()));
						List nodes = preferencesTree.getSelectedNodes();
						if (nodes.size() >= 1) {
							((CardLayout) getPreferneceConfigViewPanel()
									.getLayout()).show(getPreferneceConfigViewPanel(),
									((PreferencesTypeTreeNode) nodes.get(0))
											.getName());
							System.out.println("Showing : "  + ((PreferencesTypeTreeNode) nodes.get(0))
											.getName());
						}
					}
			});
		}
		return preferencesTree;
	}

	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 0;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getOkButton(), gridBagConstraints5);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes preferneceConfigViewPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPreferneceConfigViewPanel() {
		if (preferneceConfigViewPanel == null) {
			preferneceConfigViewPanel = new JPanel(new CardLayout());
			preferneceConfigViewPanel
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Preference Configureation",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									null, IntroduceLookAndFeel
											.getPanelLabelColor()));
			addTreePanels((PreferencesTypeTreeNode) preferencesTree.getModel()
					.getRoot());
		}
		return preferneceConfigViewPanel;
	}

	/**
	 * This method initializes okButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("Ok");
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					PreferencesDialog.this.setVisible(false);
				}
			});
		}
		return okButton;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
