package gov.nih.nci.cagrid.data.ui.tree;

import java.util.EventObject;

/** 
 *  CheckTreeSelectionEvent
 *  Event object for changes to check box selection
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 21, 2006 
 * @version $Id$ 
 */
public class CheckTreeSelectionEvent extends EventObject {
	private CheckBoxTreeNode node;
	
	public CheckTreeSelectionEvent(CheckBoxTree tree, CheckBoxTreeNode node) {
		super(tree);
		this.node = node;
	}
	
	
	public CheckBoxTree getTree() {
		return (CheckBoxTree) super.getSource();
	}
	
	
	public CheckBoxTreeNode getNode() {
		return node;
	}
}
