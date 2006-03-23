package gov.nih.nci.cagrid.introduce.portal.security;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id$
 */
public class GridMapTable extends PortalBaseTable {
	public final static String GID = "Grid Identity";

	public GridMapTable() {
		super(createTableModel());

		this.clearTable();

	}

	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(GID);
		return model;

	}

	public synchronized void addUser(final String gridId) {
		Vector v = new Vector();
		v.add(gridId);
		addRow(v);
	}

	public synchronized String getSelectedUser() throws Exception {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			return (String) getValueAt(row, 0);
		} else {
			throw new Exception("Please select a user!!!");
		}
	}

	public synchronized void removeSelectedUser() throws Exception {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			removeRow(row);
		} else {
			throw new Exception("Please select an user!!!");
		}
	}

	public synchronized String getUserAt(int index) throws Exception {
		return (String) getValueAt(index, 0);
	}

	public int getUserCount() {
		return getRowCount();
	}

	public void doubleClick() throws Exception {

	}

	public void singleClick() throws Exception {
		// TODO Auto-generated method stub

	}

}