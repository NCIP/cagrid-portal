package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;

import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.xml.namespace.QName;

import org.uispec4j.ComboBox;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * 
 */
public class InputParametersTable extends JTable {

	public static String NAME = "Name";
	
	public static String ISARRAY = "Is Array";

	public static String NAMESPACE = "Namespace";

	public static String TYPE = "Type";

	public static String DATA1 = "DATA1";

	private MethodType method;


	public InputParametersTable(MethodType method) {
		super();
		this.setModel(createTableModel());
		this.method = method;
		//this.setColumnSelectionAllowed(true);
		//this.setRowSelectionAllowed(true);
		//this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//TableColumn col = getColumnModel().getColumn(1);
	    //col.setCellEditor(new DefaultCellEditor(new JCheckBox()));
	    initialize();
	}


	public boolean isCellEditable(int row, int column) {
		return false;
	}


	public void addRow(final MethodTypeInputsInput input) {
		final Vector v = new Vector();
		v.add(input.getName());
		v.add(new Boolean(input.isIsArray()));
		v.add(input.getQName().getNamespaceURI());
		v.add(input.getQName().getLocalPart());

		v.add(v);

		((DefaultTableModel) this.getModel()).addRow(v);
		this.setRowSelectionInterval(this.getModel().getRowCount() - 1, this.getModel().getRowCount() - 1);
	}


	public void modifySelectedRow(final MethodTypeInputsInput input) throws Exception {
		int row = getSelectedRow();
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		Vector v = (Vector) getValueAt(getSelectedRow(), 4);
		v.set(0, input.getName());
		v.set(1,new Boolean(input.isIsArray()));
		v.set(2, input.getQName().getNamespaceURI());
		v.set(3, input.getQName().getLocalPart());
		v.set(4, v);
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
		
		String name = ((String) getValueAt(row, 0));
		boolean isArray = ((Boolean) getValueAt(row, 1)).booleanValue();
		String namespace = ((String) getValueAt(row, 2));
		String type = ((String) getValueAt(row, 3));

			input.setIsArray(isArray);
	
		if (name != null && !name.equals("")) {
			input.setName(name);
		}
		if (namespace != null && !namespace.equals("") && type != null && !type.equals("")) {
			input.setQName(new QName(namespace,type));
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


	public MyDefaultTableModel createTableModel() {
		MyDefaultTableModel model = new MyDefaultTableModel();
		return model;
	}


	public void singleClick() throws Exception {
		// TODO Auto-generated method stub

	}


	public void doubleClick() throws Exception {
		// TODO Auto-generated method stub

	}
	
	public class MyDefaultTableModel extends DefaultTableModel {
		
		public MyDefaultTableModel(){
			super();
			addColumn(NAME);
			addColumn(ISARRAY);
			addColumn(NAMESPACE);
			addColumn(TYPE);
			addColumn(DATA1);
		}
		
		
	}
}

