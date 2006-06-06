package gov.nih.nci.cagrid.introduce.portal.creation;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;

import javax.swing.table.DefaultTableModel;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class ExtensionsTable extends PortalBaseTable {

	public static String NAME = "Extension Name";

	public ExtensionsTable() {
		super(createTableModel());
	}


	public boolean isCellEditable(int row, int column) {
		return true;
	}


	public void addRow(String input) {
		for (int i = 0; i < getRowCount(); i++) {
			String existing = (String) getValueAt(i, 0);
			if (existing.equals(input)) {
				return;
			}
		}

		((DefaultTableModel) this.getModel()).addRow(new String[] {input});
		this.setRowSelectionInterval(this.getModel().getRowCount() - 1, this.getModel().getRowCount() - 1);
	}


	public void modifyRow(final String input, int row) throws Exception {
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		setValueAt(input, row, 0);
	}


	public void modifySelectedRow(final String input) throws Exception {
		int row = getSelectedRow();
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		modifyRow(input, getSelectedRow());
	}


	public void moveSelectedRowUp() throws Exception {
		if (getSelectedRow() >0 ) {
			String input1 = getRowData(getSelectedRow());
			String input2 = getRowData(getSelectedRow() - 1);
			modifySelectedRow(input2);
			modifyRow(input1, getSelectedRow() - 1);
			setRowSelectionInterval(getSelectedRow() - 1, getSelectedRow() - 1);
			repaint();
		}
	}


	public void moveSelectedRowDown() throws Exception {
		if (getSelectedRow() < getRowCount()-1 && getRowCount() > 1) {
			String input1 = getRowData(getSelectedRow());
			String input2 = getRowData(getSelectedRow() + 1);
			modifySelectedRow(input2);
			modifyRow(input1, getSelectedRow() + 1);
			setRowSelectionInterval(getSelectedRow() + 1, getSelectedRow() + 1);
			repaint();
		}
	}


	public void removeSelectedRow() throws Exception {
		int row = getSelectedRow();
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		int oldSelectedRow = getSelectedRow();
		((DefaultTableModel) getModel()).removeRow(oldSelectedRow);
		if (oldSelectedRow == 0) {
			oldSelectedRow++;
		}
		if (getRowCount() > 0) {
			setRowSelectionInterval(oldSelectedRow - 1, oldSelectedRow - 1);
		}
	}


	public String getRowData(int row) throws Exception {
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		String name = (String) getValueAt(row, 0);

		return name;
	}


	public String getSelectedRowData() throws Exception {
		return getRowData(getSelectedRow());
	}


	public static MyDefaultTableModel createTableModel() {
		MyDefaultTableModel model = new MyDefaultTableModel();
		return model;
	}


	public void singleClick() throws Exception {
		// TODO Auto-generated method stub

	}


	public void doubleClick() throws Exception {
		// TODO Auto-generated method stub

	}


	public static class MyDefaultTableModel extends DefaultTableModel {

		public MyDefaultTableModel() {
			super();
			addColumn(NAME);
		}


		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}
	}
}
