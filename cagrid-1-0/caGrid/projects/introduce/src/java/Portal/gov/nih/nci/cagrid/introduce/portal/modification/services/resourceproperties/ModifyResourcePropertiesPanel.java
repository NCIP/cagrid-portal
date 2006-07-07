package gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.portal.modification.types.NamespaceTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.types.NamespacesJTree;
import gov.nih.nci.cagrid.introduce.portal.modification.types.SchemaElementTypeTreeNode;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.xml.namespace.QName;
import javax.swing.JSplitPane;

public class ModifyResourcePropertiesPanel extends JPanel {
	
	private ResourcePropertiesListType properties;
	
	private NamespacesType namespaces;
	
	private JPanel resourcePropertiesPanel = null;
	
	private JScrollPane namespacesScrollPane = null;
	
	private JScrollPane resourcePropertiesScrollPane = null;
	
	private NamespacesJTree namespacesJTree = null;
	
	private ResourcePropertyTable resourcePropertiesTable = null;
	
	private JPanel buttonsPanel = null;
	
	private JButton addResourcePropertyButton = null;
	
	private JButton removeResourcePropertyButton = null;
	
	private JSplitPane mainSplitPane = null;
	
	private boolean showW3Cnamespaces;
	
	public ModifyResourcePropertiesPanel(ResourcePropertiesListType properties,
		NamespacesType namespaces, boolean showW3Cnamespaces) {
		this.properties = properties;
		this.namespaces = namespaces;
		this.showW3Cnamespaces = showW3Cnamespaces;
		initialize();
	}
	
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints2.gridy = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.weighty = 1.0D;
		gridBagConstraints1.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(new java.awt.Dimension(314,211));
		this.add(getMainSplitPane(), gridBagConstraints1);
		this.add(getButtonsPanel(), gridBagConstraints2);	
	}
	
	
	public void reInitialize(ResourcePropertiesListType props, NamespacesType ns) {
		this.properties = props;
		this.namespaces = ns;
		this.namespacesJTree.setNamespaces(ns);
		this.resourcePropertiesTable.setResourceProperties(props);
	}
	
	
	public ResourcePropertyType[] getConfiguredResourceProperties()
	throws Exception {
		int propertyCount = getResourcePropertiesTable().getRowCount();
		ResourcePropertyType[] configuredProperties = new ResourcePropertyType[propertyCount];
		for (int i = 0; i < propertyCount; i++) {
			configuredProperties[i] = getResourcePropertiesTable().getRowData(i);
		}
		return configuredProperties;
	}
	
	
	/**
	 * This method initializes resourcePropertiesPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getResourcePropertiesPanel() {
		if (resourcePropertiesPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.weighty = 1.0;
			gridBagConstraints4.insets = new java.awt.Insets(2, 2, 2, 2);
			resourcePropertiesPanel = new JPanel();
			resourcePropertiesPanel.setLayout(new GridBagLayout());
			resourcePropertiesPanel.add(getResourcePropertiesScrollPane(),
				gridBagConstraints4);
		}
		return resourcePropertiesPanel;
	}
	
	
	/**
	 * This method initializes namespacesScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getNamespacesScrollPane() {
		if (namespacesScrollPane == null) {
			namespacesScrollPane = new JScrollPane();
			namespacesScrollPane.setViewportView(getNamespacesJTree());
		}
		return namespacesScrollPane;
	}
	
	
	/**
	 * This method initializes resourcePropertiesScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getResourcePropertiesScrollPane() {
		if (resourcePropertiesScrollPane == null) {
			resourcePropertiesScrollPane = new JScrollPane();
			resourcePropertiesScrollPane
				.setViewportView(getResourcePropertiesTable());
		}
		return resourcePropertiesScrollPane;
	}
	
	
	/**
	 * This method initializes namespacesJTree
	 * 
	 * @return javax.swing.JTree
	 */
	private NamespacesJTree getNamespacesJTree() {
		if (namespacesJTree == null) {
			namespacesJTree = new NamespacesJTree(this.namespaces, showW3Cnamespaces);
			namespacesJTree.addMouseListener(new MouseAdapter() {				
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					if (e.getClickCount() == 2) {
						addResourceProperty();
					}
				}				
			});
		}
		return namespacesJTree;
	}
	
	
	/**
	 * This method initializes resourcePropertiesTable
	 * 
	 * @return javax.swing.JTable
	 */
	private ResourcePropertyTable getResourcePropertiesTable() {
		if (resourcePropertiesTable == null) {
			resourcePropertiesTable = new ResourcePropertyTable(this.properties);
		}
		return resourcePropertiesTable;
	}
	
	
	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonsPanel() {
		if (buttonsPanel == null) {
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.gridy = 0;
			gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new Insets(2, 2, 2, 2);
			buttonsPanel = new JPanel();
			buttonsPanel.setLayout(new GridBagLayout());
			buttonsPanel.add(getAddButton(), gridBagConstraints);
			buttonsPanel.add(getRemoveButton(), gridBagConstraints8);
		}
		return buttonsPanel;
	}
	
	
	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddButton() {
		if (addResourcePropertyButton == null) {
			addResourcePropertyButton = new JButton();
			addResourcePropertyButton.setToolTipText("add new operation");
			addResourcePropertyButton.setText("Add");
			addResourcePropertyButton.setIcon(PortalLookAndFeel.getAddIcon());
			addResourcePropertyButton.addActionListener(
				new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					addResourceProperty();
				}
			});
		}
		return addResourcePropertyButton;
	}
	
	
	private void addResourceProperty() {
		if (getNamespacesJTree().getCurrentNode() instanceof SchemaElementTypeTreeNode) {
			NamespaceType nt = ((NamespaceType) ((NamespaceTypeTreeNode) getNamespacesJTree()
				.getCurrentNode().getParent()).getUserObject());
			SchemaElementType st = ((SchemaElementType) ((SchemaElementTypeTreeNode) getNamespacesJTree()
				.getCurrentNode()).getUserObject());
			ResourcePropertyType metadata = new ResourcePropertyType();
			metadata.setQName(new QName(nt.getNamespace(), st.getType()));
			metadata.setPopulateFromFile(false);
			metadata.setRegister(false);
			
			if (properties != null && properties.getResourceProperty() != null) {
				for (int i = 0; i < properties.getResourceProperty().length; i++) {
					ResourcePropertyType rp = properties.getResourceProperty(i);
					if (rp.getQName().equals(metadata.getQName())) {
						return;
					}
				}
			}
			
			getResourcePropertiesTable().addRow(metadata);
			
			// add new metadata to array in bean
			// this seems to be a wierd way be adding things....
			ResourcePropertyType[] metadatas;
			int newLength = 0;
			if (properties != null && properties.getResourceProperty() != null) {
				newLength = properties.getResourceProperty().length + 1;
				metadatas = new ResourcePropertyType[newLength];
				System.arraycopy(properties.getResourceProperty(), 0,
					metadatas, 0, properties.getResourceProperty().length);
			} else {
				newLength = 1;
				metadatas = new ResourcePropertyType[newLength];
			}
			metadatas[newLength - 1] = metadata;
			properties.setResourceProperty(metadatas);			
		}
	}
	
	
	/**
	 * This method initializes jButton2
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRemoveButton() {
		if (removeResourcePropertyButton == null) {
			removeResourcePropertyButton = new JButton();
			removeResourcePropertyButton.setToolTipText("remove selected operation");
			removeResourcePropertyButton.setText("Remove");
			removeResourcePropertyButton.setIcon(PortalLookAndFeel.getRemoveIcon());			
			removeResourcePropertyButton.addActionListener(
				new java.awt.event.ActionListener() {
				// remove from table
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ResourcePropertyType resource = null;
					try {
						resource = getResourcePropertiesTable().getRowData(
							getResourcePropertiesTable().getSelectedRow());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					int row = getResourcePropertiesTable().getSelectedRow();
					if ((row < 0)
						|| (row >= getResourcePropertiesTable().getRowCount())) {
						PortalUtils.showErrorMessage("Please select a metdata type to remove.");
						return;
					}
					getResourcePropertiesTable().removeRow(
						getResourcePropertiesTable().getSelectedRow());
					// remove from resource properties list
					ResourcePropertyType[] metadatas;
					int newLength = properties.getResourceProperty().length - 1;
					metadatas = new ResourcePropertyType[newLength];
					int resourcesCount = 0;
					for (int i = 0; i < newLength; i++) {
						ResourcePropertyType oldResource = properties.getResourceProperty(i);
						if (!resource.equals(oldResource)) {
							metadatas[resourcesCount++] = oldResource;
						}
					}
					properties.setResourceProperty(metadatas);
				}
			});
		}
		return removeResourcePropertyButton;
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
			mainSplitPane.setLeftComponent(getNamespacesScrollPane());
			mainSplitPane.setRightComponent(getResourcePropertiesPanel());
		}
		return mainSplitPane;
	}
}
