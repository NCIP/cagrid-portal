package gov.nih.nci.cagrid.introduce.portal.modification.services.methods;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;

import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * 
 */
public class ExceptionsTable extends PortalBaseTable {

	public static String NAME = "Name";

	public static String DATA1 = "DATA1";

	private MethodType method;


	public ExceptionsTable(MethodType method) {
		super(createTableModel());
		this.method = method;
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(true);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		initialize();
	}


	public boolean isCellEditable(int row, int column) {
		return false;
	}


	public void addRow(final String exception) {
		final Vector v = new Vector();
		v.add(exception);
		v.add(v);

		((DefaultTableModel) this.getModel()).addRow(v);
	}


	public void modifySelectedRow(final String exception) throws Exception {
		int row = getSelectedRow();
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		Vector v = (Vector) getValueAt(getSelectedRow(), 1);
		v.set(0, exception);
	}


	public MethodTypeExceptionsException getSelectedRowData() throws Exception {
		return getRowData(getSelectedRow());
	}


	public MethodTypeExceptionsException getRowData(int row) throws Exception {
		MethodTypeExceptionsException exception = new MethodTypeExceptionsException();
		exception.setName((String) getValueAt(row, 0));
		return exception;
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


	private void initialize() {
		this.getColumn(DATA1).setMaxWidth(0);
		this.getColumn(DATA1).setMinWidth(0);
		this.getColumn(DATA1).setPreferredWidth(0);

		if (method.getExceptions() != null) {
			if (method.getExceptions().getException() != null) {
				for (int i = 0; i < method.getExceptions().getException().length; i++) {
					addRow(method.getExceptions().getException(i).getName());
				}
			}
		}
	}


	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(NAME);
		model.addColumn(DATA1);

		return model;
	}


	public void singleClick() throws Exception {
		// TODO Auto-generated method stub

	}


	public void doubleClick() throws Exception {
		// TODO Auto-generated method stub

	}
}