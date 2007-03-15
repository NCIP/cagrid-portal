package org.cagrid.grape;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class ConfigurationTreeRenderer extends DefaultTreeCellRenderer {

	public ConfigurationTreeRenderer() {
		super();
	}


	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, 
		boolean expanded, boolean leaf, int row, boolean localHasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, localHasFocus);
		if (isBaseTreeNode(value)) {
			ConfigurationBaseTreeNode node = (ConfigurationBaseTreeNode) value;
			this.setIcon(node.getIcon());
			this.setText(node.toString());
		}
		return this;
	}


	private boolean isBaseTreeNode(Object value) {
		if (value instanceof ConfigurationBaseTreeNode) {
			return true;
		}
		return false;
	}
}
