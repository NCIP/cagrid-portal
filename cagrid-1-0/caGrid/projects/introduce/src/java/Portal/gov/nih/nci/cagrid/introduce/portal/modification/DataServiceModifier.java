package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.modification.gme.GMETypeSelectionComponent;
import gov.nih.nci.cagrid.introduce.portal.modification.types.NamespacesJTree;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.projectmobius.portal.GridPortalComponent;
import javax.swing.JSplitPane;

/** 
 *  DataServiceModifier
 *  Viewer / modifier interface for data types exposed by a data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Mar 22, 2006 
 * @version $Id$ 
 */
public class DataServiceModifier extends GridPortalComponent {

	private GMETypeSelectionComponent gmeTypeSelector = null;
	private NamespacesJTree namespaceTree = null;
	private DataTypesTable dataTypesTable = null;
	private JTabbedPane configTabbedPane = null;
	private JButton includeNamespaceButton = null;
	private JPanel typeSelectionPanel = null;
	private JScrollPane namespaceTreeScrollPane = null;
	private JScrollPane dataTypesScrollPane = null;
	private JSplitPane typesSplitPane = null;
	private JButton saveButton = null;
	private JButton undoButton = null;
	private JButton cancelButton = null;
	private JPanel buttonPanel = null;
	private JPanel mainPanel = null;
	
	public DataServiceModifier() {
		super();
		initialize();
	}
	
	
	private void initialize() {
		setTitle("Modify Data Service");
		this.setContentPane(getMainPanel());
		setSize(500,400);
		setFrameIcon(IntroduceLookAndFeel.getModifyIcon());
	}
	
	
	private GMETypeSelectionComponent getGmeTypeSelector() {
		if (gmeTypeSelector == null) {
			gmeTypeSelector = new GMETypeSelectionComponent();
		}
		return gmeTypeSelector;
	}


	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getConfigTabbedPane() {
		if (configTabbedPane == null) {
			configTabbedPane = new JTabbedPane();
			configTabbedPane.addTab("Exposed Data Types", null, getTypesSplitPane(), null);
		}
		return configTabbedPane;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getIncludeNamespaceButton() {
		if (includeNamespaceButton == null) {
			includeNamespaceButton = new JButton();
			includeNamespaceButton.setText("Include Namespace");
			includeNamespaceButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
			includeNamespaceButton.setIcon(PortalLookAndFeel.getAddIcon());
		}
		return includeNamespaceButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTypeSelectionPanel() {
		if (typeSelectionPanel == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints11.gridy = 2;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints11.gridx = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.gridy = 0;
			typeSelectionPanel = new JPanel();
			typeSelectionPanel.setLayout(new GridBagLayout());
			typeSelectionPanel.add(getGmeTypeSelector(), gridBagConstraints);
			typeSelectionPanel.add(getIncludeNamespaceButton(), gridBagConstraints1);
			typeSelectionPanel.add(getNamespaceTreeScrollPane(), gridBagConstraints11);
		}
		return typeSelectionPanel;
	}
	
	
	private NamespacesJTree getNamespaceTree() {
		if (namespaceTree == null) {
			namespaceTree = new NamespacesJTree(new NamespacesType());
		}
		return namespaceTree;
	}


	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getNamespaceTreeScrollPane() {
		if (namespaceTreeScrollPane == null) {
			namespaceTreeScrollPane = new JScrollPane();
			namespaceTreeScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Available Types", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			namespaceTreeScrollPane.setViewportView(getNamespaceTree());
		}
		return namespaceTreeScrollPane;
	}
	
	
	private DataTypesTable getDataTypesTable() {
		if (dataTypesTable == null) {
			dataTypesTable = new DataTypesTable();
		}
		return dataTypesTable;
	}
	
	
	private JScrollPane getDataTypesScrollPane() {
		if (dataTypesScrollPane == null) {
			dataTypesScrollPane = new JScrollPane();
			dataTypesScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Selected Types", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			dataTypesScrollPane.setViewportView(getDataTypesTable());
		}
		return dataTypesScrollPane;
	}


	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getTypesSplitPane() {
		if (typesSplitPane == null) {
			typesSplitPane = new JSplitPane();
			typesSplitPane.setLeftComponent(getTypeSelectionPanel());
			typesSplitPane.setRightComponent(getDataTypesScrollPane());
			typesSplitPane.setOneTouchExpandable(true);
		}
		return typesSplitPane;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton();
			saveButton.setText("Save");
			saveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
			saveButton.setIcon(PortalLookAndFeel.getSaveIcon());
		}
		return saveButton;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getUndoButton() {
		if (undoButton == null) {
			undoButton = new JButton();
			undoButton.setText("Undo");
			undoButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
			undoButton.setIcon(IntroduceLookAndFeel.getUndoIcon());
		}
		return undoButton;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
			cancelButton.setIcon(PortalLookAndFeel.getCloseIcon());
		}
		return cancelButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getSaveButton(), null);
			buttonPanel.add(getUndoButton(), null);
			buttonPanel.add(getCancelButton(), null);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.gridx = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getConfigTabbedPane(), gridBagConstraints2);
			mainPanel.add(getButtonPanel(), gridBagConstraints3);
		}
		return mainPanel;
	}
}
