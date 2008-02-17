package gov.nih.nci.cagrid.introduce.portal.modification.services;

import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.beans.service.ServicesType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.common.PopupTreeNode;
import gov.nih.nci.cagrid.introduce.portal.common.SortableJTreeModel;
import gov.nih.nci.cagrid.introduce.portal.modification.services.methods.MethodTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.methods.MethodsTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties.ResourcePropertiesTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties.ResourcePropertyTypeTreeNode;

import java.awt.CardLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class ServicesJTree extends JTree {
	private ServicesTypeTreeNode root;

	private DefaultTreeModel model;

	private ServicesType services;

	private JPanel optionsPanel;

	private DefaultMutableTreeNode currentNode = null;

	public ServicesJTree(ServicesType services, ServiceInformation info,
			JPanel optionsPanel) {
		super(new SortableJTreeModel(null, new ServiceJTreeComparator()));
		this.optionsPanel = optionsPanel;
		setCellRenderer(new ServicesTreeRenderer());
		setServices(services, info);
		this.addMouseListener(new MouseAdapter() {
			
			
			public void mouseEntered(MouseEvent e) {
				setSelectionRow(getRowForLocation(e.getX(), e.getY()));
			}

			public void mouseClicked(MouseEvent e) {
				setSelectionRow(getRowForLocation(e.getX(), e.getY()));
			}

			public void mousePressed(MouseEvent e) {
				List nodes = getSelectedNodes();
				if (nodes.size() >= 1) {
					currentNode = (DefaultMutableTreeNode) nodes.get(0);
					if (currentNode instanceof MethodsTypeTreeNode) {
						((CardLayout) ServicesJTree.this.optionsPanel
								.getLayout()).show(
								ServicesJTree.this.optionsPanel, "methods");
					} else if (currentNode instanceof MethodTypeTreeNode) {
						((MethodButtonPanel) ServicesJTree.this.optionsPanel
								.getComponent(3)).setCanModify(true);
						// show the correct card for editing a method
						((CardLayout) ServicesJTree.this.optionsPanel
								.getLayout()).show(
								ServicesJTree.this.optionsPanel, "method");
					} else if (currentNode instanceof ResourcePropertiesTypeTreeNode) {
						((CardLayout) ServicesJTree.this.optionsPanel
								.getLayout()).show(
								ServicesJTree.this.optionsPanel, "resources");
					} else if (currentNode instanceof ServicesTypeTreeNode) {
						((CardLayout) ServicesJTree.this.optionsPanel
								.getLayout()).show(
								ServicesJTree.this.optionsPanel, "services");
					} else if (currentNode instanceof ResourcePropertyTypeTreeNode) {
						((CardLayout) ServicesJTree.this.optionsPanel
								.getLayout()).show(
								ServicesJTree.this.optionsPanel, "resourceProperty");
					} else if (currentNode instanceof ServiceTypeTreeNode) {
						((CardLayout) ServicesJTree.this.optionsPanel
								.getLayout()).show(
								ServicesJTree.this.optionsPanel, "service");
					} else {
						((CardLayout) ServicesJTree.this.optionsPanel
								.getLayout()).show(
								ServicesJTree.this.optionsPanel, "blank");
					}
				} else {
					((CardLayout) ServicesJTree.this.optionsPanel.getLayout())
							.show(ServicesJTree.this.optionsPanel, "blank");
				}
				if (SwingUtilities.isRightMouseButton(e)) {
					if (currentNode instanceof PopupTreeNode) {
						((PopupTreeNode) currentNode).getPopUpMenu().show(
								e.getComponent(), e.getX(), e.getY());
					}
				}

			}
		});
		
		//expand anything in the tree if it changes
		this.getModel().addTreeModelListener(new TreeModelListener() {
        
            public void treeStructureChanged(TreeModelEvent e) {
                expandPath(e.getTreePath());
        
            }
        
        
            public void treeNodesRemoved(TreeModelEvent e) {
                expandPath(e.getTreePath());
        
            }
        
        
            public void treeNodesInserted(TreeModelEvent e) {
                expandPath(e.getTreePath());
        
            }
        
        
            public void treeNodesChanged(TreeModelEvent e) {
                expandPath(e.getTreePath());
        
            }
        
        });

	}

	public ServicesTypeTreeNode getRoot() {
		return root;
	}

	public void removeAllNodes(TreeNode node) {
		if (node != null) {
			// node is visited exactly once
			if (!node.equals(root)) {
				((DefaultTreeModel) getModel())
						.removeNodeFromParent((MutableTreeNode) node);
			}

			if (node.getChildCount() >= 0) {
				for (Enumeration e = node.children(); e.hasMoreElements();) {
					TreeNode n = (TreeNode) e.nextElement();
					removeAllNodes(n);
				}
			}
		}
	}

	public void setServices(ServicesType ns, ServiceInformation info) {
		services = ns;
		removeAllNodes(root);
		((DefaultTreeModel) this.getModel()).setRoot(null);
		root = new ServicesTypeTreeNode(info);
		((DefaultTreeModel) this.getModel()).setRoot(root);
		root.setServices(services, (DefaultTreeModel) this.getModel());
		//expand the root
		this.expandPath(new TreePath(root));
		
	}

	public DefaultMutableTreeNode getCurrentNode() {
		return currentNode;
	}

	public List getSelectedNodes() {
		List selected = new LinkedList();
		TreePath[] currentSelection = this.getSelectionPaths();
		if (currentSelection != null) {
			for (int i = 0; i < currentSelection.length; i++) {
				TreePath path = currentSelection[i];
				DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) path
						.getLastPathComponent();
				selected.add(lastNode);
			}
		}
		return selected;
	}

	public void removeSelectedNode() {
		// keep the services object in sync
		if (currentNode instanceof ServiceTypeTreeNode) {
			ServiceType[] namespaceTypes = services.getService();
			if (namespaceTypes.length > 1) {
				ServiceType[] newServiceTypes = new ServiceType[namespaceTypes.length - 1];
				int kept = 0;
				for (int i = 0; i < namespaceTypes.length; i++) {
					ServiceType type = namespaceTypes[i];
					if (!type.equals(currentNode.getUserObject())) {
						newServiceTypes[kept] = type;
						kept++;
					}
				}
				services.setService(newServiceTypes);
			} else {
				services.setService(null);
			}
		}

		if (currentNode != null) {
			model.removeNodeFromParent(currentNode);
		}
	}

	// If expand is true, expands all nodes in the tree.
	// Otherwise, collapses all nodes in the tree.
	public void expandAll(boolean expand) {
		JTree tree = this;
		TreeNode currRoot = (TreeNode) tree.getModel().getRoot();

		// Traverse tree from root
		expandAll(new TreePath(currRoot), expand);
	}

	private void expandAll(TreePath parent, boolean expand) {
		JTree tree = this;
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (java.util.Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(path, expand);
			}
		}

		// Expansion or collapse must be done bottom-up
		if (expand) {
			tree.expandPath(parent);
		} else {
			tree.collapsePath(parent);
		}
	}
}
