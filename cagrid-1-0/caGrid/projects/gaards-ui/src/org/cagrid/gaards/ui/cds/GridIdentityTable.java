package org.cagrid.gaards.ui.cds;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.cagrid.grape.table.GrapeBaseTable;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: GridIdentityTable.java,v 1.1 2007-11-19 14:39:56 langella Exp $
 */
public class GridIdentityTable extends GrapeBaseTable {
	public final static String GRID_IDENTITY = "Grid Identity";

	public GridIdentityTable() {
		super(createTableModel());
		this.clearTable();
	}

	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(GRID_IDENTITY);
		return model;

	}

	public void addIdentity(final String id) {
		Vector<String> v = new Vector<String>();
		v.add(id);
		addRow(v);
	}

	public synchronized String getSelectedIdentity() throws Exception {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			return (String) getValueAt(row, 0);
		} else {
			throw new Exception("Please select a user!!!");
		}
	}

	public synchronized void removeSelectedIdentity() throws Exception {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			removeRow(row);
		} else {
			throw new Exception("Please select a user!!!");
		}
	}

	public void doubleClick() throws Exception {
	}

	public void singleClick() throws Exception {
		// TODO Auto-generated method stub

	}

}