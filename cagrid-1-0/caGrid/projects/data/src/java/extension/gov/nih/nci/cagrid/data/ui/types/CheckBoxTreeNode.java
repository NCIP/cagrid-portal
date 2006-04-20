package gov.nih.nci.cagrid.data.ui.types;

import javax.swing.JCheckBox;
import javax.swing.tree.DefaultMutableTreeNode;

/** 
 *  CheckBoxTreeNode
 *  Tree node that has a check box
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 20, 2006 
 * @version $Id$ 
 */
public abstract class CheckBoxTreeNode extends DefaultMutableTreeNode {

	private JCheckBox check;
	
	public CheckBoxTreeNode() {
		super();
		check = new JCheckBox();
	}
	
	
	public JCheckBox getCheckBox() {
		return check;
	}
	
	
	public boolean isChecked() {
		return check.isSelected();
	}
}
