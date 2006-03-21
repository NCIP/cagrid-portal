package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.xml.namespace.QName;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class OutputTypeTable extends PortalBaseTable {

	public static String ISARRAY = "Is Array";

	public static String NAMESPACE = "Namespace";

	public static String TYPE = "Type";

	public static String DATA1 = "DATA1";

	private MethodType method;


	public OutputTypeTable(MethodType method) {
		super(createTableModel());
		this.method = method;
		initialize();
	}


	public boolean isCellEditable(int row, int column) {
		return true;
		// if (column == 4) {
		// return true;
		// } else {
		// return false;
		// }
	}


	private void initialize() {
		this.getColumn(DATA1).setMaxWidth(0);
		this.getColumn(DATA1).setMinWidth(0);
		this.getColumn(DATA1).setPreferredWidth(0);
		MethodTypeOutput output = method.getOutput();
		if (output != null) {
			final Vector v = new Vector();
			v.add(String.valueOf(output.isIsArray()));
			v.add(output.getQName().getNamespaceURI());
			v.add(output.getQName().getLocalPart());
			v.add(v);
			((DefaultTableModel) this.getModel()).addRow(v);
		}
	}


	public void modifyRow(int row, final MethodTypeOutput output) throws Exception {
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		Vector v = (Vector) getValueAt(row, 3);
		v.set(0, (String.valueOf(output.isIsArray())));
		v.set(1, output.getQName().getNamespaceURI());
		v.set(2, output.getQName().getLocalPart());
		v.set(3, v);
		paint(getGraphics());
	}


	public void modifySelectedRow(final MethodTypeOutput output) throws Exception {
		modifyRow(getSelectedRow(), output);
	}


	public MethodTypeOutput getRowData(int row) throws Exception {

		MethodTypeOutput output = new MethodTypeOutput();

		boolean isArray = new Boolean(((String) getValueAt(row, 0))).booleanValue();
		String namespace = ((String) getValueAt(row, 1));
		String type = ((String) getValueAt(row, 2));

		output.setIsArray(isArray);
		if (namespace != null && type != null) {
			output.setQName(new QName(namespace, type));
		}

		return output;
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


	public MethodTypeInputsInput getSelectedRowData() throws Exception {
		int row = getSelectedRow();
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		Vector v = (Vector) getValueAt(getSelectedRow(), 1);
		return (MethodTypeInputsInput) v.get(6);
	}


	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(ISARRAY);
		model.addColumn(NAMESPACE);
		model.addColumn(TYPE);
		model.addColumn("DATA1");
		return model;
	}


	public void singleClick() throws Exception {
		// TODO Auto-generated method stub

	}


	public void doubleClick() throws Exception {
		// TODO Auto-generated method stub

	}
}