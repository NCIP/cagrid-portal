package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.ServiceModificationUIPanel;
import gov.nih.nci.cagrid.introduce.portal.modification.types.NamespaceTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.types.NamespacesJTree;
import gov.nih.nci.cagrid.introduce.portal.modification.types.SchemaElementTypeTreeNode;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

/** 
 *  DataServiceModificationPanel
 *  Panel to modify target data types for a data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 5, 2006 
 * @version $Id$ 
 */
public class DataServiceModificationPanel extends ServiceModificationUIPanel {
	
	private NamespacesJTree namespacesTree = null;
	private DataServiceTypesTable typesTable = null;
	private JScrollPane targetTypesScrollPane = null;
	private JScrollPane namespaceTreeScrollPane = null;
	private JButton removeTypeButton = null;
	private JSplitPane mainSplitPane = null;
	private JPanel targetTypesPanel = null;

	public DataServiceModificationPanel(ServiceExtensionDescriptionType desc, ServiceInformation info) {
		super(desc, info);
		initialize();
	}
	
	
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.weighty = 1.0;
		gridBagConstraints2.gridx = 0;
		this.setLayout(new GridBagLayout());
        this.setSize(new java.awt.Dimension(155,105));
        this.add(getMainSplitPane(), gridBagConstraints2);
        // update the namespaces on focus of the panel
        addFocusListener(new FocusAdapter() {
        	public void focusGained(FocusEvent e) {
				getNamespacesTree().setNamespaces(getServiceInfo().getNamespaces());
			}
		});
	}
	
	
	private NamespacesJTree getNamespacesTree() {
		if (namespacesTree == null) {
			namespacesTree = new NamespacesJTree(getServiceInfo().getNamespaces(), true);
			namespacesTree.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
						// add the selected type / namespace to the types table
						DefaultMutableTreeNode selectedNode = getNamespacesTree().getCurrentNode();
						if (selectedNode != null && selectedNode instanceof SchemaElementTypeTreeNode) {
							SchemaElementTypeTreeNode typeNode = (SchemaElementTypeTreeNode) selectedNode;
							NamespaceTypeTreeNode namespaceNode = (NamespaceTypeTreeNode) typeNode.getParent();
							getTypesTable().addType((NamespaceType) namespaceNode.getUserObject(),
								(SchemaElementType) typeNode.getUserObject());
						}
					}
				}
			});
		}
		return namespacesTree;
	}
	
	
	private DataServiceTypesTable getTypesTable() {
		if (typesTable == null) {
			typesTable = new DataServiceTypesTable();
		}
		return typesTable;
	}


	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getTargetTypesScrollPane() {
		if (targetTypesScrollPane == null) {
			targetTypesScrollPane = new JScrollPane();
			targetTypesScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Target Types", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			targetTypesScrollPane.setViewportView(getTypesTable());
		}
		return targetTypesScrollPane;
	}


	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getNamespaceTreeScrollPane() {
		if (namespaceTreeScrollPane == null) {
			namespaceTreeScrollPane = new JScrollPane();
			namespaceTreeScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Available Types", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			namespaceTreeScrollPane.setViewportView(getNamespacesTree());
		}
		return namespaceTreeScrollPane;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRemoveTypeButton() {
		if (removeTypeButton == null) {
			removeTypeButton = new JButton();
			removeTypeButton.setText("Remove Type");
			removeTypeButton.setIcon(PortalLookAndFeel.getRemoveIcon());
			removeTypeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (getTypesTable().getSelectedRow() != -1) {
						getTypesTable().removeSerializationMapping(getTypesTable().getSelectedRow());
					}
				}
			});
		}
		return removeTypeButton;
	}


	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getMainSplitPane() {
		if (mainSplitPane == null) {
			mainSplitPane = new JSplitPane();
			mainSplitPane.setOneTouchExpandable(true);
			mainSplitPane.setRightComponent(getTargetTypesPanel());
			mainSplitPane.setLeftComponent(getNamespaceTreeScrollPane());
		}
		return mainSplitPane;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTargetTypesPanel() {
		if (targetTypesPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.gridx = 0;
			targetTypesPanel = new JPanel();
			targetTypesPanel.setLayout(new GridBagLayout());
			targetTypesPanel.add(getTargetTypesScrollPane(), gridBagConstraints);
			targetTypesPanel.add(getRemoveTypeButton(), gridBagConstraints1);
		}
		return targetTypesPanel;
	}
	
	
	/**
	 * Temporary stub to get the build going again.
	 * 
	 * This method should reload all GUI components that rely on the service model
	 */
	public void resetGUI() {
		// TODO: implement me
	}
}
