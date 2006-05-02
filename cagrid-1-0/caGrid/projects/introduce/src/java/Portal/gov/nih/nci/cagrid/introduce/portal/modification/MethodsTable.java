package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;

import java.io.File;
import java.util.Properties;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class MethodsTable extends PortalBaseTable {
	public static final String OPERATION = "Operation";
	private MethodsType methodsType;


	public MethodsTable(MethodsType methodsType, File methodsDirectory, Properties serviceProperties) {
		super(createTableModel());
		this.methodsType = methodsType;
		this.setRowSelectionAllowed(true);
		initialize();
	}


	public boolean isCellEditable(int row, int column) {
		return false;
	}


	private void initialize() {
		if (methodsType!= null && methodsType.getMethod() != null) {
			for (int i = 0; i < methodsType.getMethod().length; i++) {
				this.addRow(methodsType.getMethod(i));
			}
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


	public MethodType getMethodType(int row) {
		if ((row < 0) || (row >= getRowCount())) {
			return null;
		}

		return ((MethodTypeContainer) getValueAt(row, 0)).getMethod();
	}


	public MethodType getSelectedMethodType() {
		return getMethodType(getSelectedRow());
	}


	public void refreshRowFromMethodType(int rowIndex) {
		TableModelEvent event = new TableModelEvent(getModel(), rowIndex, rowIndex);
		tableChanged(event);
	}


	public void addRow(final MethodType method) {
		final Vector v = new Vector();
		v.add(new MethodTypeContainer(method));

		((DefaultTableModel) this.getModel()).addRow(v);
		this.setRowSelectionInterval(this.getModel().getRowCount() - 1, this.getModel().getRowCount() - 1);

	}


	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(OPERATION);
		return model;
	}


	class MethodTypeContainer {
		private MethodType method;


		public MethodType getMethod() {
			return method;
		}


		public void setMethod(MethodType method) {
			this.method = method;
		}


		public MethodTypeContainer(MethodType method) {
			this.method = method;
		}


		public String toString() {
			// assume its void to start with
			String output = "void";

			MethodTypeOutput outputType = this.method.getOutput();
			if (outputType != null) {
				// use classname if set, else use schema type
				if (outputType.getQName() != null && outputType.getQName().getLocalPart() != null
					&& !outputType.getQName().getLocalPart().trim().equals("")) {
					output = outputType.getQName().getLocalPart();
				}

				// add array notation if its an array
				if (outputType.isIsArray()) {
					output += "[]";
				}
			}

			String input = "";
			MethodTypeInputs inputs = this.method.getInputs();
			if (inputs != null) {
				MethodTypeInputsInput[] inputarr = inputs.getInput();
				if (inputarr != null) {
					for (int i = 0; i < inputarr.length; i++) {
						MethodTypeInputsInput inputType = inputarr[i];
						// use classname if set, else use schema type
						if (inputType.getQName() != null && inputType.getQName().getLocalPart() != null
							&& !inputType.getQName().getLocalPart().trim().equals("")) {
							if (!input.equals("")) {
								input += ", ";
							}
							input += inputType.getQName().getLocalPart();
						} else {
							// why would this be the case?
							continue;
						}

						// add array notation if its an array
						if (inputType.isIsArray()) {
							input += "[]";
						}

						input += " " + inputType.getName();
					}
				}
			}

			output += "  " + method.getName() + "(" + input + ")";

			return output;
		}
	}


	public void singleClick() throws Exception {
		// TODO Auto-generated method stub

	}


	public void doubleClick() throws Exception {
		// TODO Auto-generated method stub

	}

}