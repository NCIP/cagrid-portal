/*-----------------------------------------------------------------------------
 * Copyright (c) 2003-2004, The Ohio State University,
 * Department of Biomedical Informatics, Multiscale Computing Laboratory
 * All rights reserved.
 * 
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3  All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement: This product includes
 *    material developed by the Mobius Project (http://www.projectmobius.org/).
 * 
 * 4. Neither the name of the Ohio State University, Department of Biomedical
 *    Informatics, Multiscale Computing Laboratory nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * 5. Products derived from this Software may not be called "Mobius"
 *    nor may "Mobius" appear in their names without prior written
 *    permission of Ohio State University, Department of Biomedical
 *    Informatics, Multiscale Computing Laboratory
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *---------------------------------------------------------------------------*/

package gov.nih.nci.cagrid.gridgrouper.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class GridGrouperTree extends JTree {

	private GridGroupersTreeNode rootNode;

	public GridGrouperTree(GroupManagementBrowser browser) {
		super();
		this.rootNode = new GridGroupersTreeNode(browser);
		setModel(new DefaultTreeModel(this.rootNode));
		this.addMouseListener(new GridGrouperTreeEventListener(this,browser));
		this.setCellRenderer(new GridGrouperTreeRenderer());
	}

	public GridGroupersTreeNode getRootNode() {
		return this.rootNode;
	}

	public List getGroupNodes() {
		List nodes = new ArrayList();
		this.getGroupNodes(getRootNode(), nodes);
		return nodes;
	}

	private void getGroupNodes(GridGrouperBaseTreeNode node, List nodes) {
		int count = node.getChildCount();
		for (int i = 0; i < count; i++) {
			GridGrouperBaseTreeNode child = (GridGrouperBaseTreeNode) node
					.getChildAt(i);
			if (child instanceof GroupTreeNode) {
				nodes.add(child);
			} else if (child instanceof StemTreeNode) {
				getGroupNodes(child, nodes);
			}
		}
	}

	public GridGrouperBaseTreeNode getCurrentNode() {
		TreePath currentSelection = this.getSelectionPath();
		if (currentSelection != null) {
			DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) currentSelection
					.getLastPathComponent();
			return (GridGrouperBaseTreeNode) currentNode;
		}
		return null;
	}

	/**
	 * Get all the selected service nodes
	 * 
	 * @return A List of GridServiceTreeNodes
	 */
	public List getSelectedNodes() {
		List selected = new LinkedList();
		TreePath[] currentSelection = this.getSelectionPaths();
		if (currentSelection != null) {
			for (int i = 0; i < currentSelection.length; i++) {
				TreePath path = currentSelection[i];
				DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) path
						.getLastPathComponent();
				if (currentNode != this.getRootNode()) {
					selected.add(currentNode);
				}
			}
		}
		return selected;
	}

	/**
	 * Reload a portion of the tree's view in a synchronized way
	 * 
	 * @param reloadPoint
	 *            The node from which to reload
	 */
	public synchronized void reload(TreeNode reloadPoint) {
		((DefaultTreeModel) this.getModel()).reload(reloadPoint);
	}

	/**
	 * Reload from the root
	 */
	public synchronized void reload() {
		this.reload(getRootNode());
	}

}
