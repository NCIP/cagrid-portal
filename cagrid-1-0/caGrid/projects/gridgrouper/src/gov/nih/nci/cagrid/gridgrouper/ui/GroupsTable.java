package gov.nih.nci.cagrid.gridgrouper.ui;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.common.portal.PortalUtils;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class GroupsTable extends PortalBaseTable {
	public final static String GROUP = "Groups";

	public final static String NAME = "Name";

	public GroupsTable() {
		super(createTableModel());
		TableColumn c = this.getColumn(GROUP);
		c.setMaxWidth(0);
		c.setMinWidth(0);
		c.setPreferredWidth(0);
		c.setResizable(false);

		this.clearTable();

	}

	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(GROUP);
		model.addColumn(NAME);
		return model;
	}

	public void addGroup(final GroupTreeNode node) {
		Vector v = new Vector();
		v.add(node);
		v.add(node.getGroup().getDisplayExtension());
		addRow(v);
	}

	public synchronized GroupTreeNode getSelectedGroup() throws Exception {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			return (GroupTreeNode) getValueAt(row, 0);
		} else {
			throw new Exception("Please select a group!!!");
		}
	}

	public void doubleClick() throws Exception {
		GroupTreeNode node =  getSelectedGroup();
		PortalUtils.showMessage("Why is everybody always clicking on me?");
		node.getBrowser().getTabbedContent().addNode(node);
	}

	public void singleClick() throws Exception {
		// TODO Auto-generated method stub

	}

}