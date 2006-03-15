package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;

import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * 
 */
public class InputParametersTable extends PortalBaseTable {

	public static String PACKAGENAME = "Package Name";

	public static String CLASSNAME = "Classname";

	public static String ISARRAY = "Is Array";

	public static String NAME = "Name";

	public static String NAMESPACE = "Namespace";

	public static String TYPE = "Type";

	public static String LOCATION = "Location";

	public static String GME = "Get Type From GME";

	public static String DATA1 = "DATA1";

	private MethodType method;


	public InputParametersTable(MethodType method) {
		super(createTableModel());
		this.method = method;
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(true);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		initialize();
	}


	public boolean isCellEditable(int row, int column) {
		return true;
	}


	public void addRow(final MethodTypeInputsInput input) {
		final Vector v = new Vector();
		v.add(input.getPackageName());
		v.add(input.getClassName());
		if (input.getIsArray() != null) {
			v.add((String.valueOf(input.getIsArray().booleanValue())));
		} else {
			v.add("");
		}
		v.add(input.getName());
		v.add(input.getNamespace());
		v.add(input.getType());
		v.add(input.getLocation());

		v.add(v);

		((DefaultTableModel) this.getModel()).addRow(v);
		this.setRowSelectionInterval(this.getModel().getRowCount() - 1, this.getModel().getRowCount() - 1);
	}


	public void modifySelectedRow(final MethodTypeInputsInput input) throws Exception {
		int row = getSelectedRow();
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		Vector v = (Vector) getValueAt(getSelectedRow(), 7);
		v.set(0, input.getPackageName());
		v.set(1, input.getClassName());
		if (input.getIsArray() != null) {
			v.set(2, (String.valueOf(input.getIsArray().booleanValue())));
		} else {
			v.set(2, "");
		}
		v.set(3, input.getName());
		v.set(4, input.getNamespace());
		v.set(5, input.getType());
		v.set(6, input.getLocation());

		v.set(7, v);
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


	public MethodTypeInputsInput getRowData(int row) throws Exception {
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		MethodTypeInputsInput input = new MethodTypeInputsInput();

		String packageName = ((String) getValueAt(row, 0));
		String className = ((String) getValueAt(row, 1));
		Boolean isArray = new Boolean((String) getValueAt(row, 2));
		String name = ((String) getValueAt(row, 3));
		String namespace = ((String) getValueAt(row, 4));
		String type = ((String) getValueAt(row, 5));
		String location = ((String) getValueAt(row, 6));

		if (packageName != null && !packageName.equals("")) {
			input.setPackageName(packageName);
		}
		if (className != null && !className.equals("")) {
			input.setClassName(className);
		}
		if (isArray != null) {
			input.setIsArray(isArray);
		}
		if (name != null && !name.equals("")) {
			input.setName(name);
		}
		if (namespace != null && !namespace.equals("")) {
			input.setNamespace(namespace);
		}
		if (type != null && !type.equals("")) {
			input.setType(type);
		}
		if (location != null && !location.equals("")) {
			input.setLocation(location);
		}

		return input;
	}


	public MethodTypeInputsInput getSelectedRowData() throws Exception {
		return getRowData(getSelectedRow());
	}


	private void initialize() {
		this.getColumn(DATA1).setMaxWidth(0);
		this.getColumn(DATA1).setMinWidth(0);
		this.getColumn(DATA1).setPreferredWidth(0);
		if (this.method.getInputs() != null && this.method.getInputs().getInput() != null) {
			for (int i = 0; i < this.method.getInputs().getInput().length; i++) {
				addRow(this.method.getInputs().getInput(i));
			}
		}
	}


	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(PACKAGENAME);
		model.addColumn(CLASSNAME);
		model.addColumn(ISARRAY);
		model.addColumn(NAME);
		model.addColumn(NAMESPACE);
		model.addColumn(TYPE);
		model.addColumn(LOCATION);
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