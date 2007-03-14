package gov.nih.nci.cagrid.introduce.portal.modification.services;

import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.beans.service.ServicesType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
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


	public ServicesJTree(ServicesType services, ServiceInformation info, JPanel optionsPanel) {
		this.optionsPanel = optionsPanel;
		setCellRenderer(new ServicesTreeRenderer());
		setServices(services, info);
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				ServicesJTree.this.setSelectionRow(ServicesJTree.this.getRowForLocation(e.getX(), e.getY()));
				List nodes = ServicesJTree.this.getSelectedNodes();
				if (nodes.size() >= 1) {
					currentNode = (DefaultMutableTreeNode) nodes.get(0);
					if (SwingUtilities.isRightMouseButton(e)) {
						if (nodes.get(0) instanceof MethodsTypeTreeNode) {
							((MethodsTypeTreeNode) nodes.get(0)).getPopUpMenu().show(e.getComponent(), e.getX(),
								e.getY());
						} else if (nodes.get(0) instanceof MethodTypeTreeNode) {
							((MethodTypeTreeNode) nodes.get(0)).getPopUpMenu().show(e.getComponent(), e.getX(),
								e.getY());
						} else if (nodes.get(0) instanceof ResourcePropertiesTypeTreeNode) {
							((ResourcePropertiesTypeTreeNode) nodes.get(0)).getPopUpMenu().show(e.getComponent(),
								e.getX(), e.getY());
						} else if (nodes.get(0) instanceof ServicesTypeTreeNode) {
							((ServicesTypeTreeNode) nodes.get(0)).getPopUpMenu().show(e.getComponent(), e.getX(),
								e.getY());
						} else if (nodes.get(0) instanceof ResourcePropertyTypeTreeNode) {
							((ResourcePropertyTypeTreeNode) nodes.get(0)).getPopUpMenu().show(e.getComponent(),
								e.getX(), e.getY());
						} else if (nodes.get(0) instanceof ServiceTypeTreeNode) {
							((ServiceTypeTreeNode) nodes.get(0)).getPopUpMenu().show(e.getComponent(), e.getX(),
								e.getY());
						}
					} else if (SwingUtilities.isLeftMouseButton(e)) {

						if (nodes.get(0) instanceof MethodsTypeTreeNode) {
							((CardLayout) ServicesJTree.this.optionsPanel.getLayout()).show(
								ServicesJTree.this.optionsPanel, "methods");
						} else if (nodes.get(0) instanceof MethodTypeTreeNode) {
							((MethodButtonPanel) ServicesJTree.this.optionsPanel.getComponent(3)).setCanModify(true);
							// show the correct card for editing a method
							((CardLayout) ServicesJTree.this.optionsPanel.getLayout()).show(
								ServicesJTree.this.optionsPanel, "method");
						} else if (nodes.get(0) instanceof ResourcePropertiesTypeTreeNode) {
							((CardLayout) ServicesJTree.this.optionsPanel.getLayout()).show(
								ServicesJTree.this.optionsPanel, "resources");
						} else if (nodes.get(0) instanceof ServicesTypeTreeNode) {
							((CardLayout) ServicesJTree.this.optionsPanel.getLayout()).show(
								ServicesJTree.this.optionsPanel, "services");
						} else if (nodes.get(0) instanceof ResourcePropertyTypeTreeNode) {
							((CardLayout) ServicesJTree.this.optionsPanel.getLayout()).show(
								ServicesJTree.this.optionsPanel, "blank");
						} else if (nodes.get(0) instanceof ServiceTypeTreeNode) {
							((CardLayout) ServicesJTree.this.optionsPanel.getLayout()).show(
								ServicesJTree.this.optionsPanel, "service");
						}
					}
				}
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
				((DefaultTreeModel) getModel()).removeNodeFromParent((MutableTreeNode) node);
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
		expandAll(true);
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
				DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) path.getLastPathComponent();
				// if (currentNode != this.root) {
				selected.add(currentNode);
				// }
			}
		}
		return selected;
	}


	public void removeSelectedNode() {
		DefaultMutableTreeNode currentNode = getCurrentNode();

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


	protected void setExpandedState(TreePath path, boolean state) {
		// Ignore all collapse requests; collapse events will not be fired
		if (path.getLastPathComponent() != root) {
			super.setExpandedState(path, state);
		} else if (state && (path.getLastPathComponent() == root)) {
			super.setExpandedState(path, state);
		}
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
